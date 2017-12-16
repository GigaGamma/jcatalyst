package com.jcatalyst.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.jcatalyst.JCatalyst;

@WebSocket
public class LiveSocket extends WebSocketAdapter {
	
	public static LiveValueFactory factory;
	public List<LiveValueProcessor> processors = new ArrayList<>();
	public Session session;
	
	public static void create(LiveValueFactory f) {
		factory = f;
	}
	
	@Override
	public void onWebSocketConnect(Session sess) {
		this.session = sess;
		factory.setSocket(this);
		factory.run();
	}
	
	@Override
	public void onWebSocketText(String message) {
		SocketJsonAdapter s = new Gson().fromJson(message, SocketJsonAdapter.class);
		if (s.type.equals("value-change")) {
			ValueChangeJsonAdapter a = new Gson().fromJson(message, ValueChangeJsonAdapter.class);
			for (LiveValueProcessor v : processors) {
				a.value = v.set(a.name, a.value);
			}
			try {
				session.getRemote().sendString(new Gson().toJson(new ValueChangeJsonAdapter(a.name, a.value)));
			} catch (Exception e) {
				JCatalyst.LOGGER.log(Level.WARNING, e.getMessage(), e.getCause());
			}
		} else if (s.type.equals("load-list")) {
			LoadListJsonAdapter a = new Gson().fromJson(message, LoadListJsonAdapter.class);
			for (LiveValueProcessor v : processors) {
				//a.value = v.setList(name, value)
				a.value = (String[]) v.getList(a.name);
			}
			try {
				session.getRemote().sendString(new Gson().toJson(new LoadListJsonAdapter(a.name, a.value)));
			} catch (Exception e) {
				JCatalyst.LOGGER.log(Level.WARNING, e.getMessage(), e.getCause());
			}
		}
	}
	
}
