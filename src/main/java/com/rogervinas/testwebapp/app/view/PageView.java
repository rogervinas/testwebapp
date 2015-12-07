package com.rogervinas.testwebapp.app.view;

import java.io.IOException;

public class PageView extends TemplateView
{
	private PageView() throws IOException {
		super("template/page.html");
	}
	
	private static PageView view = null;
	
	public static PageView get() throws IOException {
		if(view == null) {
			view = new PageView();
		}
		return view;		
	}
}
