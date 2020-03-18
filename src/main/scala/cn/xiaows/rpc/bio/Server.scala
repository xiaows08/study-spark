package cn.xiaows.rpc.bio

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.ServerSocket

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/5 16:33
 * @version: v1.0
 */
object Server {
	def main(args: Array[String]): Unit = {
		val server = new ServerSocket(1234)
		println("服务端启动 ...")
		while (true) {
			val socket = server.accept()
			val in = new ObjectInputStream(socket.getInputStream)
			val out = new ObjectOutputStream(socket.getOutputStream)
			new Thread(new Runnable {
				override def run(): Unit = {
					println("socket 创建成功, 子线程启动 ...")
					while (true) {
						in.readObject() match {
							case HeartBeat(id, ts) =>
								println(s"$id $ts 注册成功...")
								Thread.sleep(2000)
								out.writeObject(HeartBeat("server", System.currentTimeMillis))
								out.flush()
							case _ => println(s"other code ...")
						}
					}
				}
			}).start()
			println("done ...")
		}
	}
}
