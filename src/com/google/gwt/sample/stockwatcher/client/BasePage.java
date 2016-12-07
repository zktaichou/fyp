package com.google.gwt.sample.stockwatcher.client;

import org.moxieapps.gwt.highcharts.client.Global;
import org.moxieapps.gwt.highcharts.client.Highcharts;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BasePage implements EntryPoint {
	static VerticalPanel menuContainer;
	static VerticalPanel headerContainer;
	static VerticalPanel contentContainer;
	static VerticalPanel footerContainer;
	static ContentPanel contentPanel;
	
	static int contentPanelHeight = Window.getClientHeight()-Menu.HEIGHT-Footer.HEIGHT-Header.HEIGHT;
	
	public void onModuleLoad() {
		Highcharts.setOptions(
			     new Highcharts.Options().setGlobal(
			         new Global()
			           .setUseUTC(false)
			   ));
		
		ResourcePreload.preloadData();
		
		menuContainer = new VerticalPanel();
		headerContainer = new VerticalPanel();
		contentContainer = new VerticalPanel();
		footerContainer = new VerticalPanel();
		contentPanel = new ContentPanel();
		
		RootPanel.get("menuContainer").add(menuContainer);
		RootPanel.get("headerContainer").add(headerContainer);
		RootPanel.get("contentContainer").add(contentContainer);
		RootPanel.get("footerContainer").add(footerContainer);

		RootPanel.getBodyElement().getStyle().setBackgroundImage("url(http://cdn.wallpapersafari.com/93/52/UQNku3.jpg)");
		// https://s-media-cache-ak0.pinimg.com/originals/a4/e9/ca/a4e9caa0026c329b946b089f7657b053.jpg
		
		Menu menu = new Menu();
		menuContainer.add(menu);
		menuContainer.setWidth("100%");

		Header header = new Header();
		headerContainer.add(header);
		headerContainer.setWidth("100%");
		headerContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

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
