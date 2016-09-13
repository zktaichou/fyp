package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class SchedulePage  {
	
	VerticalPanel mainPanel = new VerticalPanel();
	VerticalPanel photoPanel = new VerticalPanel();
	Button backButton = new Button("Back");
	ArrayList<Anchor> photos = new ArrayList<Anchor>();
	Anchor pic1 = new Anchor("  ");
	Anchor pic2 = new Anchor("  ");
	Anchor pic3 = new Anchor("  ");
	
	public SchedulePage(){
		setPhotosURL();
		setHandlers();
		
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(backButton);
		mainPanel.add(photoPanel);
		}

	public static VerticalPanel start(){
		SchedulePage temp = new SchedulePage();
		return temp.mainPanel;
	}
	
	public void setHandlers(){
		backButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterMainMenuPage();
				};
			});
		
		for (Anchor pics : photos) {
			final String URL = pics.getHTML();
			pics.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event){
					Pages.enterSensorPage(URL);
					};
				});
		}
	}
	
	public void setPhotosURL(){
		photoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		photoPanel.setSpacing(20);
		
		pic1.setHTML(Images.getImage(Images.SAMPLE_1, Window.getClientHeight()/3));
		pic2.setHTML(Images.getImage(Images.SAMPLE_2, Window.getClientHeight()/3));
		pic3.setHTML(Images.getImage(Images.SAMPLE_3, Window.getClientHeight()/3));
		
		photos.add(pic1);
		photos.add(pic2);
		photos.add(pic3);
		
		for (Anchor pics : photos) {
			photoPanel.add(pics);
		}
	}
	
}
