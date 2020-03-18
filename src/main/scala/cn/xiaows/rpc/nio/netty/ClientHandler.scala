package cn.xiaows.rpc.nio.netty

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import org.slf4j.LoggerFactory

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/18 15:36
 * @version: v1.0
 */
class ClientHandler(id: String) extends ChannelInboundHandlerAdapter {
	private val log = LoggerFactory.getLogger(this.getClass)
	private val pool = new ScheduledThreadPoolExecutor(1)

	override def channelActive(ctx: ChannelHandlerContext): Unit = {
		log.info("与服务器建立连接,注册Worker信息 ...")
		ctx.writeAndFlush(RegisteWorker(id, 4, 2048))
	}

	override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
		log.info(s"==> $msg")
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
		log.warn(cause.getMessage)
		pool.shutdownNow()
		ctx.close()
	}
}
