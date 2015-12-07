package com.rogervinas.testwebapp.app.view;

import java.io.IOException;

public class LoginView extends TemplateView
{
	private LoginView() throws IOException {
		super("template/login.html");
	}
	
	private static LoginView view = null;
	
	public static LoginView get() throws IOException {
		if(view == null) {
			view = new LoginView();
		}
		return view;		
	}
}
