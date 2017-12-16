package com.jcatalyst.elements;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class LiveServlet extends WebSocketServlet {

	@Override
	public void configure(WebSocketServletFactory f) {
		f.register(LiveSocket.class);
	}
	
}
