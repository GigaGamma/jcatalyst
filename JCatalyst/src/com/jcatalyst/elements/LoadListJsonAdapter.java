package com.jcatalyst.elements;

public class LoadListJsonAdapter {
	
	public String type = "load-list";
	public String name;
	public Object[] value;
	
	public LoadListJsonAdapter(String name, Object[] value) {
		this.name = name;
		this.value = value;
	}
	
}
