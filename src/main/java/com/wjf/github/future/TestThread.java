package com.wjf.github.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class TestThread implements Runnable {

	private final Thread thread;

	public TestThread(Thread thread) {
		this.thread = thread;
	}

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LockSupport.unpark(thread);
	}
}
