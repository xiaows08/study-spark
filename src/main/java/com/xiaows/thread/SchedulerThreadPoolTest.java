package com.xiaows.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: XIAO_WS
 * @email: xiaows08@163.com
 * @create: 2020/3/18 09:34
 * @version: v1.0
 */
@Slf4j
public class SchedulerThreadPoolTest {

	public static void main(String[] args) throws InterruptedException {
		// ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		// executor.schedule(() -> {
		// 	log.info("{}", System.currentTimeMillis());
		// },0, TimeUnit.SECONDS);
		// executor.scheduleAtFixedRate(() -> {
		// 	log.info(Thread.currentThread().getName());
		// }, 0, 1, TimeUnit.SECONDS);
		// Thread.sleep(10000);
		// executor.shutdown();

		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(2);
		// pool.execute(()->{log.info("== {}", Thread.currentThread().getName());});

		//周期为 period与task'time的和
		pool.scheduleWithFixedDelay(() -> {
			log.info("==== {}", Thread.currentThread().getName());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, 0, 3, TimeUnit.SECONDS);

		// 周期为 period与task'time的最大值
		pool.scheduleAtFixedRate(() ->{
			log.info("---- {}", Thread.currentThread().getName());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, 0, 3, TimeUnit.SECONDS);

		Runtime.getRuntime().addShutdownHook(new Thread(() ->{
			pool.shutdown();
		}));
	}
}
