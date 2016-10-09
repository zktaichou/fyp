package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FilterMenu extends Composite{
	
	int count = 0;
	
	VerticalPanel mainPanel = new VerticalPanel();
	
	public FilterMenu(){
		mainPanel.setSpacing(5);
		
		initWidget(mainPanel);
	}
	
	public void addLabel(String string){
		Label label = new Label(string);
		mainPanel.add(label);
		count++;
		if(count>0)
		{
			try{
				HorizontalPanel check = (HorizontalPanel) getPanel(count-1).asWidget();
				count++;
			}catch(Exception e){
			}
		}
	}
	
	public void addLabel(String title, String string){
		Label label = new Label(string);
		label.setTitle(title);
		mainPanel.add(label);
		count++;
		if(count>0)
		{
			try{
				HorizontalPanel check = (HorizontalPanel) getPanel(count-1).asWidget();
				count++;
			}catch(Exception e){
			}
		}
	}
	
	public void addNewRow(Widget widget){
		HorizontalPanel panel = new HorizontalPanel();
		panel.getElement().getStyle().setPaddingTop(8, Unit.PX);
		panel.getElement().getStyle().setPaddingLeft(2.5, Unit.PX);
		panel.getElement().getStyle().setPaddingRight(2.5, Unit.PX);
		panel.add(widget);
		mainPanel.add(panel);
		count++;
	}
	
	public void addItem(Widget widget){
		if(groupExist(count))
		{
			getPanel(count).add(widget);
		}
		else
		{
			HorizontalPanel panel = new HorizontalPanel();
			panel.getElement().getStyle().setPaddingLeft(2.5, Unit.PX);
			panel.getElement().getStyle().setPaddingRight(2.5, Unit.PX);
			panel.add(widget);
			mainPanel.add(panel);
		}
	}
	
	public void addItem(String title, Widget widget){
		if(groupExist(title))
		{
			getPanel(title).add(widget);
		}
		else
		{
			HorizontalPanel panel = new HorizontalPanel();
			panel.getElement().getStyle().setPaddingLeft(2.5, Unit.PX);
			panel.getElement().getStyle().setPaddingRight(2.5, Unit.PX);
			panel.setTitle(title);
			panel.add(widget);
			mainPanel.add(panel);
		}
		
	}
	
	public void addSeparator(){
		mainPanel.add(new HTML("<hr />"));
		count++;
		count++;
	}
	
	public void addSeparator(String title){
		HorizontalPanel panel = new HorizontalPanel();
		panel.setBorderWidth(0);
		panel.setWidth("100%");
		panel.setTitle(title);
		panel.add(new HTML("<hr />"));
		mainPanel.add(panel);
		count++;
		count++;
	}
	
	public void setAlignment(Widget widget, HorizontalAlignmentConstant align){
		mainPanel.setCellHorizontalAlignment(widget, align);
	}
	
	public void clear(){
		mainPanel.clear();
	}
	
	public void hideGroup(String title){
		for(int i=0; i<mainPanel.getWidgetCount();i++)
		{
			if(mainPanel.getWidget(i).getTitle().equals(title))
				mainPanel.getWidget(i).setVisible(false);
		}
	}
	
	public void showGroup(String title){
		for(int i=0; i<mainPanel.getWidgetCount();i++)
		{
			if(mainPanel.getWidget(i).getTitle().equals(title))
				mainPanel.getWidget(i).setVisible(true);
		}
	}
	
	public boolean groupExist(int count){
		try {
				if(mainPanel.getWidget(count)!=null)
				{
					return true;
				}
			}   catch(Exception e){
					return false;
		}
		return false;
	}
	
	public boolean groupExist(String title){
		for(int i=0; i<mainPanel.getWidgetCount();i++)
		{
			if(mainPanel.getWidget(i).getTitle().equals(title))
			{
				try {
						if(mainPanel.getWidget(i+1)!=null)
						{
							return true;
						}
					}   catch(Exception e){
							return false;
				}
			}
		}
		return false;
	}
	
	public HorizontalPanel getPanel(int count){
			return (HorizontalPanel) mainPanel.getWidget(count);
	}
	
	public HorizontalPanel getPanel(String title){
		for(int i=0; i<mainPanel.getWidgetCount();i++)
		{
			if(mainPanel.getWidget(i).getTitle().equals(title))
			{
				return (HorizontalPanel) mainPanel.getWidget(i+1);
			}
		}
		return null;
	}
	
	public VerticalPanel asVerticalPanel(){
		return mainPanel;
	}
}