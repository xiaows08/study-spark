package cn.xiaows.rpc.nio

import java.net.InetSocketAddress
import java.nio.channels.{SelectionKey, Selector, ServerSocketChannel}

import org.slf4j.LoggerFactory

import scala.util.control.Breaks

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/12 17:36
 * @version: v1.0
 */
object Server {
	private val log = LoggerFactory.getLogger(this.getClass)

	def main(args: Array[String]): Unit = {
		//创建ServerSocketChannel
		val serverChannel = ServerSocketChannel.open()
		//设置通道为非阻塞
		serverChannel.configureBlocking(false)
		//打开socket,并绑定端口
		val serverSocket = serverChannel.socket()
		serverSocket.bind(new InetSocketAddress(1234))
		val selector = Selector.open()
		//注册accept事件
		serverChannel.register(selector, SelectionKey.OP_ACCEPT)
		while (true) {
			//阻塞1秒
			if (selector.select(1000) == 0) {
				Breaks.breakable {
					log.info("1s内没有连接")
					Breaks.break // continue
				}
			}
			selector
		}

	}

}
