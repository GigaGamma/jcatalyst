package com.jcatalyst.app;

import com.jcatalyst.elements.LiveSocket;
import com.jcatalyst.elements.LiveValueProcessor;

public class DefaultProcessor extends LiveValueProcessor {

	public DefaultProcessor(LiveSocket socket) {
		super(socket);
	}

	@Override
	public Object set(String name, Object value) {
		return value;
	}
	
	@Override
	public Object[] getList(String name) {
		return new String[] {};
	}

	@Override
	public Object[] setList(String name, Object[] value) {
		System.out.println(value.length);
		return value;
	}

}
