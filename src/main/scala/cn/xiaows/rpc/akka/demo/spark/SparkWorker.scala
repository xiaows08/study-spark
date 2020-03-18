package cn.xiaows.rpc.akka.demo.spark

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

class SparkWorker(masterUrl: String) extends Actor {

	//	master的actorRef
	var masterProxy: ActorSelection = _
	val workerId = UUID.randomUUID().toString

	override def preStart(): Unit = {
		masterProxy = context.actorSelection(masterUrl)
	}

	override def receive: Receive = {
		case "started" => {// 自己已就绪
			// 向master注册自己的信息，id core mem
			masterProxy ! RegisterWorkerInfo(workerId, 4, 32 * 1024)// 此时master会收到
		}
		case RegisteredWorkerInfo => {// master发送给自己的注册成功信息
			import context.dispatcher
			// worker启动一个定时器，定时向master发送心跳
			context.system.scheduler.schedule(0 millis, 1500 millis, self, SendHeartBeat)
		}
		case SendHeartBeat => {
			println(s"---------- $workerId 发送心跳 ----------")
			// 开始向master发送心跳
			masterProxy ! HeartBeat(workerId)// 此时master将会收到心跳信息
		}
	}
}

object SparkWorker {
	def main(args: Array[String]): Unit = {
		// 校验参数
		if (args.length != 4) {
			println(
				"""
				  |请输入参数 <host> <port> <workName> <masterUrl>
				""".stripMargin)
			sys.exit()
		}

		val host = args(0)
		val port = args(1)
		val workName = args(2)
		val masterUrl = args(3)

		val config = ConfigFactory.parseString(
			s"""
			   |akka.actor.provider="akka.remote.RemoteActorRefProvider"
			   |akka.remote.netty.tcp.hostname=$host
			   |akka.remote.netty.tcp.port=$port
			 """.stripMargin)
		val actorSystem = ActorSystem("spark-worker", config)

		// 创建自己的actorRef
		val workerActorRef = actorSystem.actorOf(Props(new SparkWorker(masterUrl)), workName)
		// 给自己发送一个已启动的消息 表示自己已经就绪了
		workerActorRef ! "started"
	}
}