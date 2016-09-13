package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainMenuPage{

	VerticalPanel mainPanel = new VerticalPanel();
	HorizontalPanel selectionPanel = new HorizontalPanel();
	Button monitorButton = new Button("Monitoring Page");
	Button scheduleButton = new Button("Schedule Page");
	Button logoutButton = new Button("Logout");
	String selectTitle = "<h1>Please select the page that you want to enter</h1>";
	
	public MainMenuPage(){
		setHandlers();
		selectionPanel.add(monitorButton);
		selectionPanel.add(scheduleButton);
		selectionPanel.setSpacing(10);
		
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(new HTML(selectTitle));
		mainPanel.add(selectionPanel);
		mainPanel.add(logoutButton);
	}
	
	public static VerticalPanel start(){
		MainMenuPage temp = new MainMenuPage();
		return temp.mainPanel;
	}
	
	public void setHandlers(){
		monitorButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterMonitoringPage();
				};
			});
		scheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterSchedulePage();
				};
			});
		logoutButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterLoginPage();
				};
			});
	}
}
