package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class SplitPanel extends Composite{
	
	SplitLayoutPanel panel = new SplitLayoutPanel();
	
	public SplitPanel(){
		RootLayoutPanel.get().setHeight(Window.getClientHeight()-Menu.HEIGHT-Footer.HEIGHT+"px");
		RootLayoutPanel.get().setWidth(200+"px");
		RootLayoutPanel.get().getElement().getStyle().setTop(Menu.HEIGHT, Unit.PX);
		RootLayoutPanel.get().getElement().getStyle().setLeft(0, Unit.PX);
		RootLayoutPanel.get().getElement().getStyle().setBackgroundColor("rgba(255,255,255,1)");
		RootLayoutPanel.get().add(panel);
	}
}
