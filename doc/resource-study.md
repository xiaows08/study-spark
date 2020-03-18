
1.调用SparkSubmit类，内部执行submit --> doRunMain -> 通过反射获取应用程序的主类对象 --> 执行主类的main方法。
2.构建SparkConf和SparkContext对象，在SparkContext入口做了三件事，创建了SparkEnv对象（创建了ActorSystem对象），TaskScheduler（用来生成并发送task给Executor），DAGScheduler（用来划分Stage）。
3.ClientActor将任务信息封装到ApplicationDescription对象里并且提交给Master。
4.Master收到ClientActor提交的任务信息后，把任务信息存在内存中，然后又将任务信息放到队列中。
5.当开始执行这个任务信息的时候，调用scheduler方法，进行资源的调度。
6.将调度好的资源封装到LaunchExecutor并发送给对应的Worker。
7.Worker接收到Master发送过来的调度信息（LaunchExecutor）后，将信息封装成一个ExecutorRunner对象。
8.封装成ExecutorRunner后，调用ExecutorRunner的start方法，开始启动 CoarseGrainedExecutorBackend对象。
9.Executor启动后向DriverActor进行反向注册。
10.与DriverActor注册成功后，创建一个线程池（ThreadPool），用来执行任务。
11.当所有的Executor注册完成后，意味着作业环境准备好了，Driver端会结束与SparkContext对象的初始化。
12.当Driver初始化完成后（创建了sc实例），会继续执行我们提交的App的代码，当触发了Action的RDD算子时，就触发了一个job，这时就会调用DAGScheduler对象进行Stage划分。
13.DAGScheduler开始进行Stage划分。
14.将划分好的Stage按照区域生成一个一个的task，并且封装到TaskSet对象，然后TaskSet提交到TaskScheduler。
15.TaskScheduler接收到提交过来的TaskSet，拿到一个序列化器，对TaskSet序列化，将序列化好的TaskSet封装到LaunchExecutor并提交到DriverActor。
16.把LaunchExecutor发送到Executor上。
17.Executor接收到DriverActor发送过来的任务（LaunchExecutor），会将其封装成TaskRunner，然后从线程池中获取线程来执行TaskRunner。
18.TaskRunner拿到反序列化器，反序列化TaskSet，然后执行App代码，也就是对RDD分区上执行的算子和自定义函数



SparkSubmit.main() -> doRunMain() -> 反射调用App.main()
new SparkContext()



```text
    1-课程说明和要求  
    
    2-Netty是什么  
    
    3-应用场景和学习资料  
    
    4-IO模型  
    
    5-BIO 介绍说明  
    
    6-BIO实例及分析  
    
    7-BIO内容梳理小结  
    
    8-NIO介绍说明  
    
    9-NIO的Buffer基本使用 
    
    10-NIO三大核心组件关系 
    
    11-Buffer的机制及子类 
    
    12-Channel基本介绍 
    
    13-Channel应用实例1 
    
    14-Channel应用实例2 
    
    15-Channel应用实例3 
    
    16-Channel拷贝文件 
    
    17-Buffer类型化和只读 
    
    18-MappedByteBuffer使用 
    
    19-Buffer的分散和聚集 
    
    20-Channel和Buffer梳理 
    
    21-Selector介绍和原理 
    
    22-Selector API介绍 
    
    23-SelectionKey在NIO体系 
    
    24-NIO快速入门(1) 
    
    25-NIO快速入门(2) 
    
    26-NIO快速入门小结 
    
    27-SelectionKey API 
    
    28-SocketChannel API 
    
    29-NIO 群聊系统(1) 
    
    30-NIO 群聊系统(2) 
    
    31-NIO 群聊系统(3) 
    
    32-NIO 群聊系统(4) 
    
    33-零拷贝原理剖析 
    
    34-零拷贝应用实例 
    
    35-零拷贝AIO内容梳理 
    
    36-Netty概述 
    
    37-线程模型概述 
    
    38-Reactor模式图解剖析 
    
    39-单Reactor单线程模式 
    
    40-单Reactor多线程模式 
    
    41-主从Reactor模式 
    
    42-Netty模型-通俗版 
    
    43-Netty模型-详细版 
    
    44-Netty入门-服务端1 
    
    45-Netty入门-服务端2 
    
    46-Netty入门-客户端 
    
    47-Netty案例源码分析 
    
    48-Netty模型梳理 
    
    49-taskQueue自定义任务 
    
    50-scheduledTaskQueue 
    
    51-异步模型原理剖析 
    
    52-FutureListener机制 
    
    53-Http服务程序实例 
    
    54-Http服务过滤资源 
    
    55-阶段内容梳理 
    
    56-Netty核心模块(1) 
    
    57-Netty核心模块(2) 
    
    58-pipeline组件剖析 
    
    59-Netty核心模块梳理 
    
    60-EventLoop组件 
    
    61-Unpooled应用实例1 
    
    62-Unpooled应用实例2 
    
    63-Netty群聊系统服务端 
    
    64-Netty群聊系统客户端 
    
    65-Netty私聊实现思路 
    
    66-Netty心跳机制实例 
    
    67-Netty心跳处理器 
    
    68-WebSocket长连接开发1 
    
    69-WebSocket长连接开发2 
    
    70-WebSocket长连接开发3 
    
    71-WebSocket长连接开发4 
    
    72-核心模块内容梳理 
    
    73-netty编解码器机制简述 
    
    74-ProtoBuf机制简述 
    
    75-ProtoBuf实例-生成类 
    
    76-ProtoBuf实例Codec使用 
    
    77-ProtoBuf传输多种类型 
    
    78-ProtoBuf内容小结 
    
    79-Netty入站与出站机制 
    
    80-Handler链调用机制实例1 
    
    81-Handler链调用机制实例2 
    
    82-Handler链调用机制实例3 
    
    83-Handler链调用机制实例4 
    
    84-Netty其它常用编解码器 
    
    85-Log4j 整合到Netty 
    
    86-编解码器和处理器链梳理 
    
    87-Tcp粘包拆包原理 
    
    88-Tcp粘包拆包实例演示 
    
    89-自定义协议解决TCP粘包拆包1 
    
    90-自定义协议解决TCP粘包拆包2 
    
    91-TCP粘包拆包内容梳理 
    
    92-Netty服务器启动源码剖析1 
    
    93-Netty服务器启动源码剖析2 
    
    94-Netty服务器启动源码剖析3 
    
    95-Netty接收请求源码剖析1 
    
    96-Netty接收请求源码剖析2 
    
    97-Netty接收请求源码剖析3 
    
    98-Pipeline源码剖析 
    
    99-ChannelHandler源码剖析
    
    100-管道 处理器 上下文创建源码剖析
    
    101-Pipeline调用Handler源码剖析
    
    102-三大核心组件剖析梳理
    
    103-Netty心跳源码剖析1
    
    104-Netty心跳源码剖析2
    
    105-EventLoop源码剖析1
    
    106-EventLoop源码剖析2
    
    107-任务加入异步线程池源码剖析1
    
    108-任务加入异步线程池源码剖析2
    
    109-任务加入异步线程池源码剖析3
    
    110-RPC调用流程分析
    
    111-用Netty实现DubboRPC-1
    
    112-用Netty实现DubboRPC-2
    
    113-用Netty实现DubboRPC-3
    
    114-用Netty实现DubboRPC-4
    
    115-用Netty实现DubboRPC-5
    
    116-用Netty实现DubboRPC-6
``` 