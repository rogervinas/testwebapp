package com.rogervinas.testwebapp.app.view;

import java.io.IOException;

public class LogoutView extends TemplateView
{
	private LogoutView() throws IOException {
		super("template/logout.html");
	}
	
	private static LogoutView view = null;
	
	public static LogoutView get() throws IOException {
		if(view == null) {
			view = new LogoutView();
		}
		return view;		
	}
}
