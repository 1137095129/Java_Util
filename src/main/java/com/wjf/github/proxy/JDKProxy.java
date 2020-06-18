package com.wjf.github.proxy;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK动态代理，要求被代理类必须实现一个接口
 * @param <T>
 */
public class JDKProxy<T> implements Serializable, InvocationHandler {

	private final static Logger LOGGER = Logger.getLogger(JDKProxy.class);

	private T t;

	public JDKProxy(T t) {
		this.t = t;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("方法执行之前");
		final Object invoke = method.invoke(t, args);
		System.out.println("方法执行之后");
		return invoke;
	}
}
