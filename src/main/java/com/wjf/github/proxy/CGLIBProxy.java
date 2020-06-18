package com.wjf.github.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * CGLIB动态代理，要求被代理类不能是final修饰的类
 */
public class CGLIBProxy implements MethodInterceptor {

	public <T> T createProxyObj(Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setCallback(this);
		enhancer.setSuperclass(clazz);
		return (T) enhancer.create();
	}

	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		System.out.println("代理---");
		return methodProxy.invokeSuper(o, objects);
	}

	public static void main(String[] args) {
		Thread.yield();
		Node node1 = new Node(1);
		Node node2 = new Node(2);
		node1.setNext(node2);
		Node node3 = new Node(3);
		AtomicReference<Node> reference = new AtomicReference<>(node1);
		reference.compareAndSet(node3.next = node1, node3);
		System.out.println(node1);
		System.out.println(node3);
	}

	private static class Node {
		private Node next;
		private Integer key;

		private Node(Node next, Integer key) {
			this.next = next;
			this.key = key;
		}

		private Node(Integer key) {
			this(null, key);
		}

		public Node() {
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public Integer getKey() {
			return key;
		}

		public void setKey(Integer key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return "Node{" +
					"next=" + next +
					", key=" + key +
					'}';
		}
	}
}
