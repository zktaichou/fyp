package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainMenuPage extends Composite{

	VerticalPanel mainPanel = new VerticalPanel();
	VerticalPanel msgPanel = new VerticalPanel();
	HorizontalPanel selectionPanel = new HorizontalPanel();
	Button monitorButton = new Button("Monitoring Page");
	Button controlButton = new Button("Control Page");
	Button logoutButton = new Button("Logout");
	String selectTitle = "<h1>Please select the page that you want to enter</h1>";
	
	public MainMenuPage(){
		setHandlers();
		
		msgPanel.add(new HTML(Messages.MAIN_MENU));

		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(msgPanel);
		

		initWidget(mainPanel);
	}
	
	public void setHandlers(){
		monitorButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterMonitoringPage();
				};
			});
		controlButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterSitePage();
				};
			});
		logoutButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterLoginPage();
				};
			});
	}
}
