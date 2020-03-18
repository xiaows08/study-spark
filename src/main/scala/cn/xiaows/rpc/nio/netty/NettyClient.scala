package cn.xiaows.rpc.nio.netty

import java.util.UUID
import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import io.netty.bootstrap.Bootstrap
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelInitializer}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import org.slf4j.LoggerFactory

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/18 15:26
 * @version: v1.0
 */
class NettyClient(host: String, port: Int, id: String) {
	private val log = LoggerFactory.getLogger(this.getClass)
	private val pool = new ScheduledThreadPoolExecutor(1)

	def this() = this("localhost", 8088, UUID.randomUUID().toString)

	// 创建客户端线程组
	val event = new NioEventLoopGroup()
	val bootstrap = new Bootstrap()
	try {
		bootstrap.group(event)
			.channel(classOf[NioSocketChannel])
			.handler(new ChannelInitializer[NioSocketChannel] {
				override def initChannel(ch: NioSocketChannel): Unit = {
					ch.pipeline()
						.addLast(new ObjectEncoder())
						.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(getClass.getClassLoader)))
						.addLast(new ChannelInboundHandlerAdapter {
							log.info("init ...")

							override def channelActive(ctx: ChannelHandlerContext): Unit = {
								log.info("与服务器建立连接,注册Worker信息 ...")
								ctx.writeAndFlush(RegisteWorker(id, 4, 2048))
							}

							override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
								//log.info(s"==> $msg")
								msg match {
									case RegistedWorker =>
										pool.scheduleAtFixedRate(new Runnable {
											override def run(): Unit = ctx.writeAndFlush(HeartBeat(id))
										}, 0, 2, TimeUnit.SECONDS)
									case s: String =>
										log.info(s)
									case s: Long =>
										log.info(s"$s")
									case _ =>
										log.info("illegal msg")
								}
							}

							override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
								log.warn(s"${cause.getStackTrace}")
								pool.shutdownNow()
								ctx.close()
							}
						})
				}
			})
		val future = bootstrap.connect(host, port).sync()
		future.channel().closeFuture().sync()
	} catch {
		case e: Exception => log.warn(s"${e.getStackTrace}")
		case e: Throwable => log.warn(s"${e.getStackTrace}")
	} finally {
		event.shutdownGracefully()
	}
}

object NettyClient {
	def main(args: Array[String]): Unit = {
		new NettyClient()
	}
}