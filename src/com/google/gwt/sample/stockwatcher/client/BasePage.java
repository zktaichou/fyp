package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BasePage implements EntryPoint {

	static VerticalPanel panel = new VerticalPanel();
	
	public void onModuleLoad() {
		ResourcePreload.getSiteList();
		
		RootPanel.get("mainContainer").add(panel);
		panel.setSize("100%", "100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(LoginPage.start());
	}
}
