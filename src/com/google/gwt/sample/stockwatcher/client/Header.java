package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Header extends Composite{
	
	private HorizontalPanel basePanel = new HorizontalPanel();
	
	public Header(){
		this(null);
	}
	
	public Header(String msg){
		if(msg==null){
			msg=Messages.WELCOME;
		}
		
		basePanel.clear();
		basePanel.setWidth("100%");
		basePanel.add(new HTML(msg));
		
		initWidget(basePanel);
	}
	
	public static void clear(){
		BasePage.headerContainer.clear();
	}
	
	public void setTitle(String msg){
		
	}

}
