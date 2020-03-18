package com.xiaows.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/12 09:33
 * @version: v1.0
 */
@Slf4j
public class ThreadPoolTest {
	public static void main(String[] args) throws InterruptedException {
		// ExecutorService pool = Executors.newSingleThreadExecutor();
		AtomicInteger poolNum = new AtomicInteger(1);
		AtomicInteger threadNum = new AtomicInteger(1);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 2, TimeUnit.SECONDS, new LinkedBlockingDeque<>(3), r -> {
			Thread thread = new Thread(r);
			thread.setName("pool-" + poolNum.getAndIncrement() +"-thread-" + threadNum.getAndIncrement());
			log.info("创建线程 {} 成功", thread.getName());
			return thread;
		}, (r, e) -> {
			//设置(覆盖)拒接策略,重试提交任务
			log.info("task {} rejected by {}, retry ...", r, e);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			e.execute(r);
		});


		for (int i = 0; i < 20; i++) {
			pool.execute(new Task(i));
			log.info("提交任务 {} 成功", i);
		}
		Thread.sleep(3000);

		for (int i = 20; i < 40; i++) {
			pool.execute(new Task(i));
			log.info("提交任务 {} 成功", i);
		}
		pool.shutdown();
		log.info("主线程结束");
	}

	public static class Task implements Runnable {
		private int i;

		public Task(int i) {
			this.i = i;
		}

		@Override
		public void run() {
			log.info("任务 {} 开始 ...", i);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public String toString() {
			return "Task{" +
					"i=" + i +
					'}';
		}
	}
}
