package cn.xiaows.rpc.akka.demo.spark

/**
  * worker -> master 注册自己（信息）
  *
  * @param id
  * @param core
  * @param ram
  */
case class RegisterWorkerInfo(id: String, core: Int, ram: Int)

/**
  * 存储worker信息类
  *
  * @param id
  * @param core
  * @param ram
  */
case class WorkerInfo(id: String, core: Int, ram: Int){
	var lastHeartBeatTime: Long = _
}

/**
  * worker -> master发送心跳信息
  * @param id
  */
case class HeartBeat(id: String)
/**
  * master -> worker发送注册成功的消息
  */
case object RegisteredWorkerInfo

/**
  * worker发送给自己的消息 告诉自己说要开始周期性的想master发送心跳消息
  */
case object SendHeartBeat

/**
  * master自己给自己发送一个检查超时worker的信息
  */
case object CheckWorkerTimeOut

/**
  * master自己给自己发送的消息，删除超时的worker，并启动一个调度器，周期性检测删除超时的worker
  */
case object RemoveTimeOutWorker