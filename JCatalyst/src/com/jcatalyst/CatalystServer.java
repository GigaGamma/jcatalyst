package com.jcatalyst;

import java.util.Properties;
import java.util.logging.Level;

import javax.websocket.server.ServerContainer;

import org.apache.velocity.app.Velocity;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.jcatalyst.app.DefaultProcessor;
import com.jcatalyst.elements.LiveServlet;
import com.jcatalyst.elements.LiveSocket;
import com.jcatalyst.elements.LiveValueFactory;
import com.jcatalyst.filter.FilterHandler;

public class CatalystServer {
	
	public Server server;
	
	public void start() {
		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(p);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/events/");
		
		ServletHolder ho = new ServletHolder("ws-events", LiveServlet.class);
		context.addServlet(ho, "/live/");
		
		LiveSocket.create(new LiveValueFactory() {
			
			@Override
			public void run() {
				getSocket().processors.add(new DefaultProcessor(getSocket()));
			}
			
		});
		
		try {
			ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
	        wscontainer.addEndpoint(LiveSocket.class);
		} catch (Exception e) {
			JCatalyst.LOGGER.log(Level.WARNING, e.getMessage(), e.getCause());
		}
		
		HandlerList handlers = new HandlerList();
	    handlers.setHandlers(new Handler[] {new FilterHandler(), context});
		
		server = new Server(80);
		server.setHandler(handlers);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			JCatalyst.LOGGER.log(Level.WARNING, e.getMessage(), e.getCause());
		}
	}
	
}
