package com.xiaows.rpc.nio.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 13:36
 * @version: v1.0
 */
@Slf4j
public class ChatServer {
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private static final int port = 6667;

	public ChatServer() {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(port));
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() {
		try {
			while (true) {
				int count = selector.select(2000);
				if (count > 0) {
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
					while (iterator.hasNext()) {
						SelectionKey key = iterator.next();

						if (key.isAcceptable()) {
							SocketChannel socketChannel = serverSocketChannel.accept();
							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ);
							log.info("{} 上线", socketChannel.getRemoteAddress().toString().substring(1));
						}
						if (key.isReadable()) {
							readData(key);
						}
						iterator.remove();
					}
				} else {
					// log.info("等待 ...");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readData(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int count = channel.read(buffer);
			if (count > 0) {
				String msg = new String(buffer.array());
				log.info("{}", msg);
				sendInfo2OtherClients(msg, channel);
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				log.warn("{} 离线了", channel.getRemoteAddress());
				key.cancel();
				channel.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 转发数据给其他客户端
	 */
	private void sendInfo2OtherClients(String msg, SocketChannel self) {
		for (SelectionKey key : selector.keys()) {
			SelectableChannel channel = key.channel();
			if (channel instanceof SocketChannel && channel != self) {
				SocketChannel dest = (SocketChannel) channel;
				ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
				try {
					dest.write(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.listen();
	}
}
