package com.xiaows.thread;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/16 09:26
 * @version: v1.0
 */
public class SonThreadExceptionTest {
	public static void main(String[] args) {
		new Thread(() -> {
			throw new RuntimeException("son thread exception");
		}).start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("end");

	}
}
