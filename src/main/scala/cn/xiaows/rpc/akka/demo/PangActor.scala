package cn.xiaows.rpc.akka.demo

import akka.actor.Actor

class PangActor extends Actor{

    override def receive: Receive = {
        case _ => print("xxxx")
    }
}
