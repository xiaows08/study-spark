package com.xiaows.rpc.nio.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/13 14:02
 * @version: v1.0
 */
@Slf4j
public class ChatClient {
	private final String host = "127.0.0.1";
	private final int port = 6667;
	private Selector selector;
	private SocketChannel socketChannel;
	private String username;

	public ChatClient() {
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			username = socketChannel.getLocalAddress().toString().substring(1);
			log.info("{} is ok ...", username);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendInfo(String info) {
		info = username + " : " + info;
		try {
			socketChannel.write(ByteBuffer.wrap(info.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readInfo() {
		try {
			int count = selector.select();
			if (count > 0) {
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if (key.isReadable()) {
						SocketChannel channel = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						int read = channel.read(buffer);
						channel.finishConnect();
						log.info("{} {}", read, new String(buffer.array()));
					} else {
						log.info("没有可用的通道");
					}
					iterator.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		// 启动一个子线程,每隔三秒,读取从服务器发送的数据
		new Thread() {
			@Override
			public void run() {
				while (true) {
					client.readInfo();
					try {
						sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		// 发送数据给服务端
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			client.sendInfo(line);
		}
	}
}
