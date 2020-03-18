package cn.xiaows.rpc.akka.demo.spark

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.concurrent.duration._

class SparkMaster extends Actor {
	private val log = LoggerFactory.getLogger(this.getClass)

	override def preStart(): Unit = {
	}

	val id2WorkerInfo = new mutable.HashMap[String, WorkerInfo]()

	override def receive: Receive = {
		// 收到worker注册过来的消息
		case RegisterWorkerInfo(workerId, core, ram) => {
			// 将worker的信息存储起来 HashMap
			if (!id2WorkerInfo.contains(workerId)) {
				val workerInfo = new WorkerInfo(workerId, core, ram)
				id2WorkerInfo += ((workerId, workerInfo))

				// master存储完worker注册的数据之后 要告诉worker说你已经注册成功
				sender() ! RegisteredWorkerInfo //此时worker会收到注册成功的消息

			}
		}
		case HeartBeat(workerId) => {
			// master收到worker的心跳消息之后，更新worker的上一次心跳时间
			val workerInfo = id2WorkerInfo(workerId)
			// 更改心跳时间
			workerInfo.lastHeartBeatTime = System.currentTimeMillis()
		}
		case CheckWorkerTimeOut => {
			import context.dispatcher
			context.system.scheduler.schedule(0 millis, 5000 millis, self, RemoveTimeOutWorker)
		}
		case RemoveTimeOutWorker => {
			// 将hashMap中所有的value都拿出来，查看当前时间和上一次心跳时间差 3000
			val workerInfos = id2WorkerInfo.values
			workerInfos.filter(workInfo => System.currentTimeMillis() - workInfo.lastHeartBeatTime > 3000)
				.foreach(wk => id2WorkerInfo.remove(wk.id))
			//println(s"---------- 还剩下 ${workerInfos.size} 个存活的Worker ----------")
			log.info(s"还剩下 ${workerInfos.size} 个存活的Worker")
		}
	}
}

object SparkMaster {
	def main(args: Array[String]): Unit = {
		// 校验参数
		//if (args.length != 3) {
		//    println(
		//        """
		//          |请输入参数 <host> <port> <masterName>
		//        """.stripMargin)
		//    sys.exit()
		//}

		val host = "localhost" //args(0)
		val port = 7078 //args(1)
		val masterName = "spark-master" //args(2)

		val config = ConfigFactory.parseString(
			s"""
			   |akka.actor.provider="akka.remote.RemoteActorRefProvider"
			   |akka.remote.netty.tcp.hostname=$host
			   |akka.remote.netty.tcp.port=$port
			 """.stripMargin)
		val actorSystem = ActorSystem("spark-worker", config)

		// 创建自己的actorRef
		val masterActorRef = actorSystem.actorOf(Props[SparkMaster], masterName)
		// 给自己发送一个已启动的消息 去启动一个调度器，定期检测HashMap中超时的worker
		masterActorRef ! CheckWorkerTimeOut
	}
}