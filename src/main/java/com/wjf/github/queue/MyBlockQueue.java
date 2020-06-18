package com.wjf.github.queue;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockQueue<T> {
	private final AtomicInteger length = new AtomicInteger(0);
	private int start = 0;
	private int end = 0;
	private final int maxSize;
	private final T[] arr;
	private final static Lock lock = new ReentrantLock();
	private final static Condition put = lock.newCondition();
	private final static Condition get = lock.newCondition();

	public MyBlockQueue(int maxSize) {
		this.maxSize = maxSize;
		arr = (T[]) new Object[maxSize];
	}

	private int getLength() {
		return length.get();
	}

	public boolean put(T t) {
		lock.lock();
		try {
			for (; ; ) {
				//为了避免虚假唤醒的情况，应将if...else...方法替换为while(...)方法，
				// 并将Object.wait()或者Condition.await()方法放在循环中
				while (getLength() == maxSize) {
					try {
						put.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
						return false;
					}
				}
				int currentLength = getLength();
				int nextLength = currentLength + 1;
				if (length.compareAndSet(currentLength, nextLength)) {
					arr[end] = t;
					get.signal();
					if (end == maxSize - 1) {
						end = 0;
					} else {
						end++;
					}
					return true;
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public T get() {
		lock.lock();
		try {
			for (; ; ) {
				//为了避免虚假唤醒的情况，应将if...else...方法替换为while(...)方法，
				// 并将Object.wait()或者Condition.await()方法放在循环中
				while (getLength() == 0) {
					try {
						get.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				int currentLength = getLength();
				int lastLength = currentLength - 1;
				if (length.compareAndSet(currentLength, lastLength)) {
					final int index;
					final T t;
					try {
						index = start;
						t = arr[index];
						put.signal();
						if (start == maxSize - 1) {
							start = 0;
						} else {
							start++;
						}
					} finally {

					}
					return t;
				}
			}
		} finally {
			lock.unlock();
		}

	}


}
