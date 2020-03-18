package com.xiaows.rpc.nio;

// import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 10:10
 * @version: v1.0
 */
// @Slf4j
public class NioClient {
	public static void main(String[] args) throws Exception {
		//得到一个网络通道
		SocketChannel socketChannel = SocketChannel.open();
		// 设置非阻塞
		socketChannel.configureBlocking(false);
		// 提供服务端的ip/port
		InetSocketAddress address = new InetSocketAddress("192.168.88.190", 6666);
		// 连接服务器
		if (!socketChannel.connect(address)) {
			while (!socketChannel.finishConnect()) {
				long time = System.currentTimeMillis();
				System.out.println(time + "由于连接服务端须要时间, 客户端不会阻塞,可以做其他工作...");
				Thread.sleep(2);
			}
		}
		System.out.println("连接服务端成功,发送数据 ...");
		String msg = UUID.randomUUID().toString();
		ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
		socketChannel.write(buffer);
		socketChannel.close();
		// System.out.println("按Enter键退出");
		// System.in.read();
	}
}
