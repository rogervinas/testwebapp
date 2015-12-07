package com.rogervinas.testwebapp.app.view;

import java.io.IOException;

public class NotFoundView extends TemplateView
{
	private NotFoundView() throws IOException {
		super("template/notfound.html");
	}
	
	private static NotFoundView view = null;
	
	public static NotFoundView get() throws IOException {
		if(view == null) {
			view = new NotFoundView();
		}
		return view;		
	}
}
