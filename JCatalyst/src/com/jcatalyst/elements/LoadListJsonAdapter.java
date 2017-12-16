package com.jcatalyst.elements;

public class LoadListJsonAdapter {
	
	public String type = "load-list";
	public String name;
	public String[] value;
	
	public LoadListJsonAdapter(String name, String[] value) {
		this.name = name;
		this.value = value;
	}
	
}
