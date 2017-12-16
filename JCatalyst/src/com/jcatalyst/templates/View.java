package com.jcatalyst.templates;

import java.util.HashMap;
import java.util.Map;

public class View {
	
	private String name;
	private Map<String, Object> map = new HashMap<>();
	
	public View(String name) {
		this.setName(name);
	}
	
	public View(String name, Map<String, Object> map) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
}
