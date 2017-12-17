package com.jcatalyst;

import java.awt.image.DataBufferByte;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.jcatalyst.filter.Filter;
import com.jcatalyst.filter.ViewFilter;
import com.jcatalyst.templates.View;

public class JCatalyst {
	
	public static final Logger LOGGER = Logger.getLogger("JCatalyst");
	public static final boolean LOG_CLIENT = false;
	
	private static final CatalystServer SERVER = new CatalystServer();
	private static final Map<String, ViewFilter> VIEWS = new HashMap<String, ViewFilter>();
	private static final Map<String, Filter> FILES = new HashMap<String, Filter>();
	
	private static boolean s = false;
	
	public static void main(String[] args) {
		get("/", (req, res) -> {
			Map<String, Object> vm = new HashMap<>();
			
			return new View("com/jcatalyst/app/templates/index.html", vm);
		});
		
		file("/catalyst/.css", (req, res) -> {
			res.setContentType("text/css");
			Map<String, Object> vm = new HashMap<>();
			InputStream s = new ClasspathResourceLoader().getResourceStream("com/jcatalyst/app/resources/catalyst.css");
			byte[] data;
			try {
				data = new byte[(int) s.available()];
				s.read(data);
				s.close();
				return new String(data, "UTF-8");
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, e.getMessage(), e.getCause());
			}
			
			return "";
		});
		
		file("/catalyst/.js", (req, res) -> {
			res.setContentType("text/javascript");
			Map<String, Object> vm = new HashMap<>();
			InputStream s = new ClasspathResourceLoader().getResourceStream("com/jcatalyst/app/resources/catalyst.js");
			byte[] data;
			try {
				data = new byte[(int) s.available()];
				s.read(data);
				s.close();
				return new String(data, "UTF-8");
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, e.getMessage(), e.getCause());
			}
			
			return "";
		});
		
		file("/catalyst/icon.png", (req, res) -> {
			res.setContentType("image/png");
			Map<String, Object> vm = new HashMap<>();
			InputStream s = new ClasspathResourceLoader().getResourceStream("com/jcatalyst/app/resources/icon.png");
			byte[] data;
			try {
				/*byte[] imageBytes = ((DataBufferByte) ImageIO.read(s).getRaster().getDataBuffer()).getData();
				System.out.println(imageBytes.length);
				res.setHeader("Content-Type", "image/png");// or png or gif, etc
				res.setHeader("Content-Length", String.valueOf(imageBytes.length));
				res.getOutputStream().write(imageBytes);
				res.getOutputStream().flush();*/
				ImageIO.write(ImageIO.read(s), "png", res.getOutputStream());
				res.flushBuffer();
				//res.getOutputStream().flush();
				return null;
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, e.getMessage(), e.getCause());
			}
			
			return "";
		});
	}
	
	private static void _a() {
		if (!s) {
			s = true;
			SERVER.start();
		}
	}
	
	public static void get(String path, ViewFilter f) {
		new Thread() {
			
			@Override
			public void run() {
				_a();
			}
			
		}.start();
		VIEWS.put(path.replace("*", "[\\s\\S]*"), f);
	}
	
	public static void file(String path, Filter f) {
		new Thread() {
			
			@Override
			public void run() {
				_a();
			}
			
		}.start();
		FILES.put(path.replace("*", "[\\s\\S]*"), f);
	}
	
	public static Map<String, ViewFilter> getViews() {
		return VIEWS;
	}
	
	public static Map<String, Filter> getFiles() {
		return FILES;
	}
	
}
