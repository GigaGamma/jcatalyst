package com.jcatalyst.app;

import java.util.ArrayList;
import java.util.List;

import com.jcatalyst.elements.LiveSocket;
import com.jcatalyst.elements.LiveValueProcessor;
import com.jcatalyst.elements.LoadListJsonAdapter;

public class DefaultProcessor extends LiveValueProcessor {
	
	public List<String> vlist = new ArrayList<>();
	
	public DefaultProcessor(LiveSocket socket) {
		super(socket);
		vlist.add("Clean my laundry");
		vlist.add("Buy some eggs");
		getSocket().updateList(new LoadListJsonAdapter("todolist", getList("todolist")));
	}

	@Override
	public Object set(String name, Object value) {
		//vlist.add(new SampleValue((String) value, 2));
		//getSocket().updateList(new LoadListJsonAdapter("samplelist", getList("samplelist")));
		return value;
	}
	
	@Override
	public Object submit(String name, Object value) {
		vlist.add(((String) value).replace("(important)", "(!!!)"));
		getSocket().updateList(new LoadListJsonAdapter("todolist", getList("todolist")));
		return "";
	}
	
	@Override
	public Object[] getList(String name) {
		return vlist.toArray();
	}

	@Override
	public Object[] setList(String name, Object[] value) {
		System.out.println(value.length);
		return vlist.toArray();
	}
	
	public static class SampleValue {
		public String name;
		public float price;
		
		public SampleValue(String name, float price) {
			this.name = name;
			this.price = price;
		}
	}

}
