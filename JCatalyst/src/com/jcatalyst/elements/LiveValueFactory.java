package com.jcatalyst.elements;

public abstract class LiveValueFactory implements Runnable {
	
	private LiveSocket socket;

	public LiveSocket getSocket() {
		return socket;
	}

	public void setSocket(LiveSocket socket) {
		this.socket = socket;
	}
	
}
