package com.wjf.github.queue;

public class Test implements Runnable {

	private final MyBlockQueue<Integer> queue;

	public Test(MyBlockQueue<Integer> queue){
		this.queue=queue;
	}

	public static void main(String[] args) {
//		MyBlockQueue<Integer> queue = new MyBlockQueue<>(5);
//		Thread thread = new Thread(new Test(queue));
//		Thread thread1 = new Thread(new TestGet(queue));
//		thread.start();
//		thread1.start();
		System.out.println(0xf0000000);
		System.out.println(0xc0000000);
		System.out.println(0x80000000);
		System.out.println(0x00010000);
		System.out.println(0x0000ffff);
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			queue.put(i);
		}
	}
}
