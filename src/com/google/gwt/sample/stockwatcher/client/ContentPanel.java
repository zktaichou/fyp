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
	HorizontalPanel middlePanel = new HorizontalPanel();
	HorizontalPanel rightPanel = new HorizontalPanel();
	
	public ContentPanel(){
		wholePanel.add(leftPanel);
		wholePanel.add(middlePanel);
		wholePanel.add(rightPanel);
		wholePanel.setSize("100%", BasePage.contentPanelHeight+"px");
		wholePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		wholePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		wholePanel.setCellHorizontalAlignment(leftPanel, HasHorizontalAlignment.ALIGN_LEFT);
		wholePanel.setCellHorizontalAlignment(middlePanel, HasHorizontalAlignment.ALIGN_CENTER);
		wholePanel.setCellHorizontalAlignment(rightPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		wholePanel.setCellVerticalAlignment(middlePanel, HasVerticalAlignment.ALIGN_TOP);
		
		leftPanel.setHeight("100%");
		middlePanel.setSize("100%", "100%");
		rightPanel.setHeight("100%");
		
		initWidget(wholePanel);
	}
	
	public void add(Widget widget){
		middlePanel.add(widget);
		middlePanel.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_CENTER);
		middlePanel.setCellVerticalAlignment(widget, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void addRight(Widget widget){
		rightPanel.add(widget);
		rightPanel.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_CENTER);
		rightPanel.setCellVerticalAlignment(widget, HasVerticalAlignment.ALIGN_TOP);
	}
	
	public void add(HorizontalPanel panel){
		middlePanel.add(panel);
		middlePanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		middlePanel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void add(VerticalPanel panel){
		middlePanel.add(panel);
		middlePanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		middlePanel.setCellVerticalAlignment(panel, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void addLeft(VerticalPanel panel){
		leftPanel.add(panel);
		leftPanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	public void clear(){
		SitePage.hideAllIcons(); //Manual workaround, may need to change
		leftPanel.clear();
		middlePanel.clear();
		rightPanel.clear();
	}
	
	public void alignMiddlePanelVTop(){
		middlePanel.setCellVerticalAlignment(middlePanel.getWidget(0), HasVerticalAlignment.ALIGN_TOP);
	}
	
	public void alignMiddlePanelVMiddle(){
		middlePanel.setCellVerticalAlignment(middlePanel.getWidget(0), HasVerticalAlignment.ALIGN_MIDDLE);
	}
}
