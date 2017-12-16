package com.jcatalyst.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.jcatalyst.JCatalyst;

public abstract class LiveValueProcessor {
	
	private LiveSocket socket;
	
	public LiveValueProcessor(LiveSocket socket) {
		this.socket = socket;
	}
	
	public abstract Object set(String name, Object value);
	public abstract Object[] getList(String name);
	public abstract Object[] setList(String name, Object[] value);
	
	public LiveSocket getSocket() {
		return socket;
	}

	public void setSocket(LiveSocket socket) {
		this.socket = socket;
	}
	
}
