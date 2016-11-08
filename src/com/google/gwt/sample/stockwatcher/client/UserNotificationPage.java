package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserNotificationPage extends Composite{
	
	ContentPanel cPanel = new ContentPanel();
	
	public UserNotificationPage(){
		
		String message = "Welcome "+Data.currentUser;
			
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setStyleName("mainStyle");
		wrapper.add(new HTML("<h1>"+message+"!!!</h1></br>"));
			
		cPanel.clear();
		cPanel.add(wrapper);
		cPanel.alignMiddlePanelVTop();

		initWidget(cPanel); 
		}
}
