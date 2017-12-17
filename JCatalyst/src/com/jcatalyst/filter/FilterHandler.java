package com.jcatalyst.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.jcatalyst.JCatalyst;
import com.jcatalyst.resources.ResourceImporter;
import com.jcatalyst.templates.View;
import com.jcatalyst.util.RandomString;

public class FilterHandler extends AbstractHandler {

	@Override
	public void handle(String target, Request basereq, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		List<String> l = new ArrayList(JCatalyst.getViews().keySet());
		ListIterator<String> a = l.listIterator(l.size());
		while (a.hasPrevious()) {
			String s = a.previous();
			if (Pattern.matches(s, target)) {
				View v = JCatalyst.getViews().get(s).handle(req, res);
				if (!v.getMap().containsKey("imports"))
					v.getMap().put("imports", new ResourceImporter());
				
				ResourceImporter ri = (ResourceImporter) v.getMap().get("imports");
				String fs = "";
				
				ri.styles.add("/catalyst/.css");
				ri.scripts.add("https://code.jquery.com/jquery-3.2.1.slim.min.js");
				ri.scripts.add("/catalyst/.js");
				
				RandomString session = new RandomString();
				
				fs += "<meta name=\"catalyst-id\" content=\"" + session.nextString() + "\">";
				
				for (String st : ri.styles) {
					fs += "<link rel=\"stylesheet\" href=\"" + st + "\">\n";
				}
				for (String st : ri.scripts) {
					fs += "<script src=\"" + st + "\"></script>\n";
				}
				
				v.getMap().remove("imports");
				v.getMap().put("imports", fs);
				
				Template template = Velocity.getTemplate(v.getName());
				OutputStreamWriter writer = new OutputStreamWriter(res.getOutputStream());
				template.merge(new VelocityContext(v.getMap()), writer);
				writer.flush();
				return;
			}
		}
		for (String s : JCatalyst.getFiles().keySet()) {
			if (Pattern.matches(s, target)) {
				Object r = JCatalyst.getFiles().get(s).handle(req, res);
				if (r != null) {
					OutputStreamWriter writer = new OutputStreamWriter(res.getOutputStream());
					writer.write((String) r);
					writer.flush();
				}
				return;
			}
		}
	}

}
