package com.jcatalyst.elements;

public class ConsoleMessageJsonAdapter {

	public String type = "console";
	public String name;
	public Object value;
	
	public ConsoleMessageJsonAdapter(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
}
