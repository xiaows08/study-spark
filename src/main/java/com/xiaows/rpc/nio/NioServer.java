package com.xiaows.rpc.nio;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 09:49
 * @version: v1.0
 */
@Slf4j
public class NioServer {
	public static void main(String[] args) throws Exception {
		// 创建ServerSocketChannel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// 得到selector对象
		Selector selector = Selector.open();
		// 绑定端口,在服务端监听
		serverSocketChannel.bind(new InetSocketAddress(6666));
		serverSocketChannel.configureBlocking(false);
		// 把serverSocketChannel注册到selector 关心OP_ACCEPT事件
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		// 循环等待客户端连接
		while (true) {
			// selector.selectNow();
			if (selector.select(5000) == 0) {
				log.debug("服务器等待了5s, 无新客户端连接 ...");
				continue;
			}
			log.info("\n");
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			log.info("{}", selectionKeys);
			// for (SelectionKey key : selectionKeys) {
			// 	if (key.isAcceptable()) {
			// 		log.info("有新的客户端连接");
			// 		// 给该客户端生成一个socketChannel
			// 		SocketChannel socketChannel = serverSocketChannel.accept();
			// 		// 将socketChannel注册到selector上,关注事件为OP_READ, 同时将socketChannel关联一个Buffer
			// 		socketChannel.configureBlocking(false);
			// 		socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
			// 	} else if (key.isReadable()) {
			// 		// 通过key反向获取对应的socketChannel
			// 		SocketChannel socketChannel = (SocketChannel) key.channel();
			// 		// 获取到该channel关联的buffer
			// 		ByteBuffer buffer = (ByteBuffer) key.attachment();
			// 		socketChannel.read(buffer);
			// 		log.info("{} : {}",socketChannel, new String(buffer.array()));
			// 		buffer.flip();
			// 		// socketChannel.close();
			// 	}
			// 	// 手动从集合中移除当前的selectionKey,防止重复操作
			// 	selectionKeys.remove(key);
			// 	log.info("{} {}", key.toString(), selectionKeys.remove(key));
			// }
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				if (key.isAcceptable()) {
					log.info("有新的客户端连接");
					// 给该客户端生成一个socketChannel
					SocketChannel socketChannel = serverSocketChannel.accept();
					// 将socketChannel注册到selector上,关注事件为OP_READ, 同时将socketChannel关联一个Buffer
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
				} else if (key.isReadable()) {
					// 通过key反向获取对应的socketChannel
					SocketChannel socketChannel = (SocketChannel) key.channel();
					// 获取到该channel关联的buffer
					ByteBuffer buffer = (ByteBuffer) key.attachment();
					socketChannel.read(buffer);
					buffer.flip();
					log.info("{} : {}  {}",socketChannel.getRemoteAddress(), new String(buffer.array()), buffer.toString());
					socketChannel.close();
				}
				// 手动从集合中移除当前的selectionKey,防止重复操作
				iterator.remove();
				log.info("{}", selectionKeys);
			}
		}

	}

}
