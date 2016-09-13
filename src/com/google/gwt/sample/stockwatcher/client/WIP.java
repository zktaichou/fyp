package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WIP{
	
	VerticalPanel mainPanel = new VerticalPanel();
	
	public WIP(){
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		}
	
	public static VerticalPanel start(){
		SchedulePage temp = new SchedulePage();
		return temp.mainPanel;
	}

}
