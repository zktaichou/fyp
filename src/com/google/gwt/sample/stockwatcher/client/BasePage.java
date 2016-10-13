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
	static VerticalPanel menuContainer = new VerticalPanel();
	static VerticalPanel headerContainer = new VerticalPanel();
	static VerticalPanel contentContainer = new VerticalPanel();
	static VerticalPanel footerContainer = new VerticalPanel();
	static ContentPanel contentPanel = new ContentPanel();
	
	static int contentPanelHeight = Window.getClientHeight()-Menu.HEIGHT-Footer.HEIGHT;
	
	public void onModuleLoad() {
		ResourcePreload.preloadData();
		
		RootPanel.get("menuContainer").add(menuContainer);
		RootPanel.get("headerContainer").add(headerContainer);
		RootPanel.get("contentContainer").add(contentContainer);
		RootPanel.get("footerContainer").add(footerContainer);

		RootPanel.getBodyElement().getStyle().setBackgroundImage("url(http://cdn.wallpapersafari.com/93/52/UQNku3.jpg)");
		// https://s-media-cache-ak0.pinimg.com/originals/a4/e9/ca/a4e9caa0026c329b946b089f7657b053.jpg
		
		Menu menu = new Menu();
		menuContainer.add(menu);
		menuContainer.setWidth("100%");
		
//		Header header = new Header();
//		headerContainer.add(header);
//		headerContainer.setWidth("100%");
//		headerContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		LoginPage loginPage = new LoginPage();
		
		contentPanel.add(loginPage);
		contentContainer.add(contentPanel);
		contentContainer.setSize("100%", "100%");
		contentContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		Footer footer = new Footer();
		footerContainer.add(footer);
		footerContainer.setWidth("100%");
	}
}
