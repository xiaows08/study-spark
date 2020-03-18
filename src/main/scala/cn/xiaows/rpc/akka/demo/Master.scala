package cn.xiaows.rpc.akka.demo

import akka.actor.Actor

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/12 15:02
 * @version: v1.0
 */
class Master() extends Actor {
	override def receive: Receive = {
		case Some(value) =>
			println(value)
		case _ => println("unknown msg!")
	}
}