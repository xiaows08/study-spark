package cn.xiaows.rpc.bio

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket
import java.util.UUID

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/5 16:33
 * @version: v1.0
 */
object Client {

	def main(args: Array[String]): Unit = {
		val clientId = UUID.randomUUID().toString
		//创建 socket
		val socket = new Socket("localhost", 1234)
		println(s"客户端 $clientId 启动...")
		val out = new ObjectOutputStream(socket.getOutputStream)
		val in = new ObjectInputStream(socket.getInputStream)

		// 通信注册
		out.writeObject(HeartBeat(clientId, System.currentTimeMillis()))
		// 监听
		while (true) {
			in.readObject() match {
				case HeartBeat(id, ts) =>
					println(s"$id $ts")
					out.writeObject(HeartBeat(clientId, System.currentTimeMillis()))
				case _ => print("null")
			}
			println("client done ...")
		}
	}
}
