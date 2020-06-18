package com.wjf.github.future;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class TsetFuture {
	public static void main(String[] args) throws ExecutionException, InterruptedException {

		try {
			TsetFuture future = new TsetFuture();
			TestThread thread = new TestThread(Thread.currentThread());
			Thread t = new Thread(thread);
			t.start();
			t.interrupt();
			System.out.println(future.test());
			System.out.println(Thread.currentThread().isInterrupted());
			System.out.println(Thread.currentThread().isInterrupted());
			throw new RuntimeException();
		}finally {
			System.out.println("aaaa");
		}

//		ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
//		FutureTask
//		final Future<Object> submit = executor.submit(new TestCallBack());
//		final Future future = executor.submit(new TestCallBack());
//		final Future future1 = executor.submit(new TestCallBack());
//		final Future future2 = executor.submit(new TestCallBack());
//		final Future future3 = executor.submit(new TestCallBack());
//		final Future future4 = executor.submit(new TestCallBack());
//		System.out.println(submit.get());
//		System.out.println(future.get());
//		System.out.println(future1.get());
//		System.out.println(future2.get());
//		System.out.println(future3.get());
//		System.out.println(future4.get());
//		executor.shutdown();
	}

	public boolean test(){
		LockSupport.park(this);
		return Thread.interrupted();
	}

}
