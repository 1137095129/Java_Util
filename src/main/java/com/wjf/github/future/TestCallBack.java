package com.wjf.github.future;

import java.security.SecureRandom;
import java.util.concurrent.Callable;

public class TestCallBack implements Callable {
	@Override
	public Object call() throws Exception {
		SecureRandom random = new SecureRandom();
		final int i = random.nextInt();
		for (int k = 0; k < 100; k++) {
			System.out.println(Thread.currentThread().getName() + "---" + k);
		}
		return i;
	}
}
