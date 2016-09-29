package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentPanel extends Composite{

	HorizontalPanel wholePanel = new HorizontalPanel();
	HorizontalPanel leftPanel = new HorizontalPanel();
	HorizontalPanel rightPanel = new HorizontalPanel();
	
	public ContentPanel(){
		wholePanel.add(leftPanel);
		wholePanel.add(rightPanel);
		wholePanel.setSize("100%", BasePage.contentPanelHeight+"px");
		wholePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		wholePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		wholePanel.setCellHorizontalAlignment(leftPanel, HasHorizontalAlignment.ALIGN_LEFT);
		wholePanel.setCellHorizontalAlignment(rightPanel, HasHorizontalAlignment.ALIGN_CENTER);
		wholePanel.setCellVerticalAlignment(rightPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		leftPanel.setHeight("100%");
		rightPanel.setSize("100%", "100%");
		
		initWidget(wholePanel);
	}
	
	public void add(Widget widget){
		rightPanel.add(widget);
		rightPanel.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_CENTER);
		rightPanel.setCellVerticalAlignment(widget, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void add(HorizontalPanel panel){
		rightPanel.add(panel);
		rightPanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		rightPanel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void add(VerticalPanel panel){
		rightPanel.add(panel);
		rightPanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		rightPanel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void addLeft(VerticalPanel panel){
		leftPanel.add(panel);
		leftPanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	public void clear(){
		SitePage.hideAllIcons(); //Manual workaround, may need to change
		leftPanel.clear();
		rightPanel.clear();
	}
}
