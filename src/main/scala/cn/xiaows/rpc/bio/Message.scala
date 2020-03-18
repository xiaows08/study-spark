package cn.xiaows.rpc.bio

import Action.Action

/**
 * 客户端和服务端保持周期性的通信
 * 客户端
 * *****************************
 * Master / Worker
 * Client提交job到Master
 * Master根据job所需的资源(cores/memory),向集群申请资源
 * 	资源不够时,将job放入等待队列queue
 * 	资源足够时,分配资源(预处理)
 * Master启动Worker进程
 *
 * Master
 *
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/6 16:20
 * @version: v1.0
 */

case class HeartBeat(id: String, ts: Long)

object Action extends Enumeration {
	type Action = Value
	val Submit, Run, Stop, Kill = Value
}

case class Job(action: Action, args: String*)