package cn.xiaows.rpc.nio.netty

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import org.slf4j.LoggerFactory

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/18 15:07
 * @version: v1.0
 */
class ServerHandler extends ChannelInboundHandlerAdapter {
	private val log = LoggerFactory.getLogger(this.getClass)

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

	override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
		log.warn(cause.getMessage)
		ctx.close()
	}
}
