package com.jcatalyst.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Filter<T> {
	
	T handle(HttpServletRequest req, HttpServletResponse res);
	
}
