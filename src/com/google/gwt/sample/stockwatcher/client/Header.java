package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Header extends Composite{
	
	public static HorizontalPanel basePanel = new HorizontalPanel();
	public static final int HEIGHT = 30;
	
	public Header(){
		this(null);
	}
	
	public Header(String msg){
		if(msg==null){
			msg=Messages.WELCOME;
		} 
		
		basePanel.clear();
		basePanel.setStyleName("header-panel"); 
		basePanel.add(new HTML(msg));
		
		initWidget(basePanel);
	}
	
	public static void clear(String msg){
		basePanel.clear();
	}
	
	public static void setHeaderTitle(String msg){
		basePanel.clear();
		basePanel.setStyleName("header-panel");
		basePanel.add(new HTML(msg));
	}

}
