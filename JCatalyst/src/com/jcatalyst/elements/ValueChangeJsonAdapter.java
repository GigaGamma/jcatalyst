package com.jcatalyst.elements;

public class ValueChangeJsonAdapter {
	
	public String type = "value-change";
	public String name;
	public Object value;
	
	public ValueChangeJsonAdapter(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
}
