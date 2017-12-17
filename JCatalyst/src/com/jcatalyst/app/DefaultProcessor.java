package com.jcatalyst.app;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.jcatalyst.elements.LiveSocket;
import com.jcatalyst.elements.LiveValueProcessor;
import com.jcatalyst.elements.LoadListJsonAdapter;

public class DefaultProcessor extends LiveValueProcessor {
	 
	public static List<Chat> vlist = new ArrayList<>();
	public static List<LiveSocket> sockets = new ArrayList<>();
	
	public DefaultProcessor(LiveSocket socket) {
		super(socket);
		sockets.add(socket);
		getSocket().updateList(new LoadListJsonAdapter("chat", getList("chat")));
	}

	@Override
	public Object set(String name, Object value) {
		//vlist.add(new SampleValue((String) value, 2));
		//getSocket().updateList(new LoadListJsonAdapter("samplelist", getList("samplelist")));
		return value;
	}
	
	@Override
	public Object submit(String name, Object value) {
		vlist.add(new Chat(getSocket().session.getRemoteAddress().getHostString(), (String) value));
		getSocket().updateList(new LoadListJsonAdapter("chat", getList("chat")));
		for (LiveSocket s : sockets) {
			if (s != getSocket() && s.session.isOpen())
				s.updateList(new LoadListJsonAdapter("chat", getList("chat")));
		}
		return "";
	}
	
	@Override
	public Object[] getList(String name) {
		//getSocket().updateList(new LoadListJsonAdapter("chat", getList("chat")));
		return vlist.toArray();
	}

	@Override
	public Object[] setList(String name, Object[] value) {
		return vlist.toArray();
	}
	
	public static class Chat {
		public String user;
		public String msg;
		
		public Chat(String user, String msg) {
			this.user = user;
			this.msg = msg;
		}
	}

}
