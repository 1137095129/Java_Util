package com.wjf.github.queue;

public class TestGet implements Runnable {

	private final MyBlockQueue<Integer> queue;

	public TestGet(MyBlockQueue<Integer> queue){
		this.queue=queue;
	}

	@Override
	public void run() {
		for (int i=0;i<100;i++){
			System.out.println(queue.get());
		}
	}
}
