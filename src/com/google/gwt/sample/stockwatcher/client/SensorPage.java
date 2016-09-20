package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class SensorPage  {
	
	VerticalPanel mainPanel = new VerticalPanel();
	Button backButton = new Button("Back");
	Anchor pic = new Anchor("   ");
	
	public SensorPage(String ImageURL){
		setHandlers();
		setImage(ImageURL);
		
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(backButton);
		mainPanel.add(pic);
		}

	public static VerticalPanel start(String ImageURL){
		SensorPage temp = new SensorPage(ImageURL);
		return temp.mainPanel;
	}
	
	public void setHandlers(){
		backButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterControlPage();
				};
			});
		pic.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event){
					Window.alert("X: "+event.getX()+", Y: "+event.getY());
					};
				});
		}
	
	public void setImage(String ImageURL){
		pic.setHTML(Images.getImage(ImageURL));
	}
	
}
