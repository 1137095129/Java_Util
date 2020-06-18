package com.wjf.github.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Graph<T> {

	private final List<Node<T>> order = new ArrayList<>();
	private final List<Node<T>> reverse = new ArrayList<>();
	private final List<T> list = new ArrayList<>();

	private final Lock lock = new ReentrantLock();

	public boolean addOrderNode(T t, int index) {
		if (t == null || list.get(index) == null)
			throw new NullPointerException();

		lock.lock();
		try {
			Node<T> node1 = order.get(index);
			if (node1 == null) {
				order.set(index, (node1 = new Node<>()));
			}
			Node<T> node = new Node<>();
			node.setT(t);
			node1.setNext(node);
			node.setLast(node1);
		} finally {
			lock.unlock();
		}

		return true;
	}

	public boolean addLastNode(T t, int index) {
		if (t == null || list.get(index) == null)
			throw new NullPointerException();

		lock.lock();
		try {
			Node<T> node1 = reverse.get(index);
			if (node1 == null) {
				reverse.set(index, (node1 = new Node<>()));
			}
			Node<T> node = new Node<>();
			node.setT(t);
			node1.setNext(node);
			node.setLast(node1);
		} finally {
			lock.unlock();
		}

		return true;
	}

	public boolean addListData(T t) {
		if (t == null)
			throw new NullPointerException();
		lock.lock();
		try {
			list.add(t);
			order.add(new Node<>());
			reverse.add(new Node<>());
		} finally {
			lock.unlock();
		}
		return true;
	}

	public List<T> getListInfo() {
		return Collections.unmodifiableList(list);
	}

	private static class Node<T> {
		private volatile Node next = null;
		private volatile Node last = null;
		private volatile T t = null;

		public Node() {
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public Node getLast() {
			return last;
		}

		public void setLast(Node last) {
			this.last = last;
		}

		public T getT() {
			return t;
		}

		public void setT(T t) {
			this.t = t;
		}
	}

}
