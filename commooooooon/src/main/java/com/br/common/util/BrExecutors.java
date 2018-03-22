package com.br.common.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: Executors 的四种线程池不满足需求，自定义</p>
 * 
 * @Company: 百融（北京）金融信息服务股份有限公司
 * @Author: hongliang.zuo
 * @Date: 2016年11月22日
 * @version: 1.0
 */
public class BrExecutors {

	private static final Logger logger = LoggerFactory.getLogger(BrExecutors.class);

	public static ThreadPoolExecutor other = new ThreadPoolExecutor(50, 50, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50000), new CallerRunsPolicy2());

	/**
	 * 默认大小、初始化线程 100 最大 500
	 * @return
	 */
	public static ThreadPoolExecutor getThreadPool() {
		return getThreadPool(100, 500);
	}

	public static ThreadPoolExecutor getThreadPool(int initNum, int maxNum) {
		// return new ThreadPoolExecutor(initNum, maxNum,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),new
		// MyRejectedExecutionHandler());
		// CallerRunsPolicy 策略 线程调用运行该任务的 execute 本身。此策略提供简单的反馈控制机制，能够减缓新任务的提交速度
		return new ThreadPoolExecutor(initNum, maxNum, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1200), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static class MyRejectedExecutionHandler implements RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
			// new Thread(task).start();
			// if (!executor.isShutdown()) {
			// task.run();
			// }
			other.execute(task);
			logger.warn("线程池已经达到边界值，将由other线程池执行，建议加大线程池，{}", executor.toString());
			// 打印线程池的对象
			// System.out.println("The pool RejectedExecutionHandler = "+other.toString());
		}
	}

	public static class CallerRunsPolicy2 implements RejectedExecutionHandler {
		public CallerRunsPolicy2() {
		}

		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			if (!e.isShutdown()) {
				r.run();
			}
		}
	}
}
