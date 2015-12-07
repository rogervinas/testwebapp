package com.rogervinas.testwebapp.app.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;

public class TemplateView
{
	private final StringBuffer template;
	private final Pattern pattern;

	public TemplateView(String templateName) throws IOException {
		template = load(templateName);
		pattern = Pattern.compile("\\{\\{(\\w+)\\}\\}");
	}
	
	private StringBuffer load(String templateName) throws IOException {
		StringBuffer buffer = new StringBuffer();
		String line;
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(templateName)))) {
			do {
				line = reader.readLine();
				if(line != null) {
					buffer.append(line + "\n");
				}
			} while(line != null);
		}
		return buffer;
	}
	
	public String render(TemplateValues values) {	
		StringBuffer html = new StringBuffer(template);
		while(true) {
			Matcher matcher = pattern.matcher(html);
			if(matcher.find()) {
				String match = matcher.group(1);
				String str = values.get(match);
				html.replace(matcher.start(), matcher.end(), str == null ? "" : str);
			} else {
				return html.toString();
			}
		}
	}
	
	public void render(int status, HttpExchange exchange, TemplateValues values) throws IOException {
		byte[] content = render(values).getBytes();
		exchange.getResponseHeaders().add("Content-Type", "text/html");		
		exchange.sendResponseHeaders(status, content.length);
		OutputStream stream = exchange.getResponseBody();
		stream.write(content);
		stream.close();
	}
}
