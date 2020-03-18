package cn.xiaows.rpc.akka.demo

import akka.actor.{Actor, ActorRef, ActorSystem, Props}


class HelloActor extends Actor {
	override def receive: Receive = {
		case "666" => println("666")
		case "123" => println("123")
		case "stop" => {
			sender()
			context.stop(self)
			context.system.terminate()
		}
	}
}

object HelloActor {
	private val helloFactory = ActorSystem("HelloFactory")
	private val helloActorRef: ActorRef = helloFactory.actorOf(Props[HelloActor], "helloActor")

	def main(args: Array[String]): Unit = {
		helloActorRef ! "666"
		helloActorRef ! "123"
		helloActorRef ! "stop"
	}
}
