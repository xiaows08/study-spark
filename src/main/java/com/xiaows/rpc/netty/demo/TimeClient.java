package com.xiaows.rpc.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/12 16:57
 * @version: v1.0
 */
public class TimeClient {

	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 8080;
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap(); // (1)
			b.group(workerGroup); // (2)
			b.channel(NioSocketChannel.class); // (3)
			b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) {
							ByteBuf m = (ByteBuf) msg; // (1)
							try {
								long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
								System.out.println(new Date(currentTimeMillis));
								// ctx.close();
							} finally {
								m.release();
							}
						}

						@Override
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
							cause.printStackTrace();
							ctx.close();
						}
					});
				}
			});

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync(); // (5)

			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
