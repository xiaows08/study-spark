package cn.xiaows.rpc.nio.netty

/**
 *
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/18 15:47
 * @version: v1.0
 */
case object Message

case class HeartBeat(id: String)

case class RegisteWorker(id: String, core: Int, memory: Long)

case object RegistedWorker