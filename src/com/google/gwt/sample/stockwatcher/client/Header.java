package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Header extends Composite{
	
	public static HorizontalPanel basePanel;
	public static HorizontalPanel leftPanel;
	public static HorizontalPanel rightPanel;
	public static final int HEIGHT = 30;
	
	public Header(){
		this(null);
	}
	
	public Header(String msg){
		basePanel = new HorizontalPanel();
		leftPanel = new HorizontalPanel();
		rightPanel = new HorizontalPanel();
		
		if(msg==null){
			msg=Messages.WELCOME;
		} 

		leftPanel.clear();
		leftPanel.setStyleName("header-panel");
		leftPanel.add(new HTML(msg));

		rightPanel.clear();
		rightPanel.add(new HTML(Images.getImage(Images.BACKGROUND_LOAD,30)));
		rightPanel.setVisible(false);
		
		basePanel.clear();
		basePanel.add(leftPanel);
		basePanel.add(rightPanel);
		
		basePanel.setCellHorizontalAlignment(leftPanel, HasHorizontalAlignment.ALIGN_LEFT);
		basePanel.setCellHorizontalAlignment(rightPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		basePanel.setCellVerticalAlignment(leftPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		basePanel.setCellVerticalAlignment(rightPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		initWidget(basePanel);
	}
	
	public static void clear(String msg){
		leftPanel.clear();
	}
	
	public static void setHeaderTitle(String msg){
		leftPanel.clear();
		leftPanel.setStyleName("header-panel");
		leftPanel.add(new HTML(msg));
	}
	
	public static void showLoading(){
		rightPanel.setVisible(true);
	}
	
	public static void hideLoading(){
		rightPanel.setVisible(false);
	}

}
