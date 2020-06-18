package com.wjf.github.proxy;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class TestInterfaceImpl implements TestInterface {
	@Override
	public String get() {
		return "aaaaa";
	}

	private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
	private static final int COUNT_BITS = Integer.SIZE - 3;
	private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

	public AtomicInteger getCtl() {
		return ctl;
	}

	// runState is stored in the high-order bits
	private static final int RUNNING    = -1 << COUNT_BITS;
	private static final int SHUTDOWN   =  0 << COUNT_BITS;
	private static final int STOP       =  1 << COUNT_BITS;
	private static final int TIDYING    =  2 << COUNT_BITS;
	private static final int TERMINATED =  3 << COUNT_BITS;

	// Packing and unpacking ctl
	private static int runStateOf(int c)     { return c & ~CAPACITY; }
	private static int workerCountOf(int c)  { return c & CAPACITY; }
	private static int ctlOf(int rs, int wc) { return rs | wc; }

	public static void main(String[] args) {

		AbstractExecutorService executorService = new ThreadPoolExecutor(10,20,6000L, TimeUnit.NANOSECONDS,new ArrayBlockingQueue<>(100));

		Future<Integer> submit = executorService.submit(() -> {
			LockSupport.parkNanos(new Object(),10000000000L);
			return 20;
		});



		try {
			System.out.println(submit.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		executorService.shutdownNow();

//		System.out.println(RUNNING);
//		System.out.println(SHUTDOWN);
//		System.out.println(STOP);
//		System.out.println(TIDYING);
//		System.out.println(TERMINATED);

//		TestInterface anInterface = new TestInterfaceImpl();
//		InvocationHandler handler = new JDKProxy<>(anInterface);
//		TestInterface o = (TestInterface) Proxy.newProxyInstance(handler.getClass().getClassLoader(), anInterface.getClass().getInterfaces(), handler);
//		System.out.println(o.get());
	}
}
