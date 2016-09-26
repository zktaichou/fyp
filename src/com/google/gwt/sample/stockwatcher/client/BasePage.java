package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BasePage implements EntryPoint {
	static VerticalPanel menuPanel = new VerticalPanel();
	static VerticalPanel headerPanel = new VerticalPanel();
	static VerticalPanel contentPanel = new VerticalPanel();
	static VerticalPanel footerPanel = new VerticalPanel();
	
	public void onModuleLoad() {
		ResourcePreload.getSiteList();
		
		RootPanel.get("menuContainer").add(menuPanel);
		RootPanel.get("headerContainer").add(headerPanel);
		RootPanel.get("contentContainer").add(contentPanel);
		RootPanel.get("footerContainer").add(footerPanel);

		Menu menu = new Menu();
		menuPanel.add(menu);
		menuPanel.setWidth("100%");
		
		Header header = new Header();
		headerPanel.add(header);
		headerPanel.setWidth("100%");
		headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		LoginPage loginPage = new LoginPage();
		contentPanel.add(loginPage);
		contentPanel.setSize("100%", "100%");
		contentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		Footer footer = new Footer();
		footerPanel.add(footer);
		footerPanel.setWidth("100%");
	}
}
