package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SchedulePage extends Composite{
	
	ContentPanel cPanel = new ContentPanel();
	
	VerticalPanel leftPanel = new VerticalPanel();
	VerticalPanel rightPanel = new VerticalPanel();
	
	FlexTable table = new FlexTable();
	
	Button goButton = new Button("Go");

	ListBox actuatorLB = new ListBox();
	ListBox scheduleType = new ListBox();
	
	public SchedulePage(){
		
		setWidgets();
		setHandlers();
		
		leftPanel.clear();
		leftPanel.setSpacing(10);
		leftPanel.add(new HTML("Please select an actuator:"));
		leftPanel.add(actuatorLB);
		leftPanel.add(new HTML("Please select a schedule type:"));
		leftPanel.add(scheduleType);
		leftPanel.add(goButton);
		
		rightPanel.clear();
		rightPanel.add(table);
		
		cPanel.clear();
		cPanel.addLeft(leftPanel);
		cPanel.add(rightPanel);
		
		initWidget(cPanel); 
	}
	
	private void setWidgets(){
		
		actuatorLB.clear();
		for(String aName: Data.actuatorAttributeList.keySet())
		{
			actuatorLB.addItem(aName);
		}
		
		scheduleType.clear();
		scheduleType.addItem("Regular Schedule");
		scheduleType.addItem("Special Schedule");
		
		
	}
	
	private void setHandlers(){
		goButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
			updateTable(actuatorLB.getSelectedItemText(),scheduleType.getSelectedItemText());	
			}
		});
	}
	
	
	//NOT WORKING YET
	private void updateTable(String aName, String scheduleType){
		table.clear();
		int row = 0, column = 0;
		if(scheduleType.equalsIgnoreCase("Regular Schedule"))
		{
			ArrayList<String> rScheduleList = Data.actuatorRegularScheduleList.get(aName);
			for(int i=0; i<rScheduleList.size();i++)
			{
				ArrayList<String> scheduleAttribute = Data.regularScheduleAttributesList.get(rScheduleList);
				column=scheduleAttribute.size();
				String[] dummy = new String[column];
				for(int j=0; j<scheduleAttribute.size();j++)
				{
					dummy[j] = scheduleAttribute.get(j);
				}
			}
			String[][]data = new String[rScheduleList.size()][column]; //dummy initialize

			table = ChartUtilities.createFlexTable(data);
		}
		else
		{
			
		}
	}
	
}
