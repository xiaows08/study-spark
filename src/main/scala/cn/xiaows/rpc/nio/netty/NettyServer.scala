package cn.xiaows.rpc.nio.netty

import cn.xiaows.rpc.akka.demo.spark.WorkerInfo
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.{NioServerSocketChannel, NioSocketChannel}
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelInitializer}
import io.netty.handler.codec.serialization.{ClassResolvers, ObjectDecoder, ObjectEncoder}
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/18 14:56
 * @version: v1.0
 */
class NettyServer(host: String, port: Int) {
	def this() = this("localhost", 8088)

	private val log = LoggerFactory.getLogger(this.getClass)
	private val id2woker = new mutable.HashMap[String, WorkerInfo]()

	//定义线程池组
	val boss = new NioEventLoopGroup()
	val worker = new NioEventLoopGroup()

	try {
		val server = new ServerBootstrap()
		server.group(boss, worker)
			.channel(classOf[NioServerSocketChannel])
			.childHandler(new ChannelInitializer[NioSocketChannel] {
				override def initChannel(ch: NioSocketChannel): Unit = {
					ch.pipeline()
						.addLast(new ObjectEncoder())
						.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(getClass.getClassLoader)))
						.addLast(new ChannelInboundHandlerAdapter() {
							log.info("init ...")

							override def channelActive(ctx: ChannelHandlerContext): Unit = {
								log.info(s"${ctx.channel().remoteAddress()} 建立连接 ... ")
							}

							override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
								//log.info(s"${ctx.channel().remoteAddress()}: $msg")
								msg match {
									case RegisteWorker(id, core, memory) =>
										log.info("registed workker ")
										ctx.writeAndFlush(RegistedWorker)
									case HeartBeat(id) =>
										log.info(s"worker $id heartbeat")
										ctx.writeAndFlush(System.currentTimeMillis())
									case _ => log.info("illegal msg")
								}
							}

							override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
								log.warn(cause.getMessage)
								ctx.close()
							}
						})
				}
			})
		val future = server.bind(port).sync()
		log.info("服务已开启 ...")
		future.channel().closeFuture().sync()
	} finally {
		boss.shutdownGracefully()
		worker.shutdownGracefully()
	}
}

object NettyServer {
	def main(args: Array[String]): Unit = {
		new NettyServer()
	}
}
