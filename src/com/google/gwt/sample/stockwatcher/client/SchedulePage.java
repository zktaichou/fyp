package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SchedulePage extends Composite{
	
	ContentPanel cPanel = new ContentPanel();
	
	VerticalPanel leftPanel = new VerticalPanel();
	VerticalPanel rightPanel = new VerticalPanel();
	
	FilterMenu scheduleMenu = new FilterMenu();
	
	Button goButton = new Button("Go");
	Button newScheduleButton = new Button("New Schedule");
	Button createScheduleButton = new Button("Create");
	Button cancelScheduleButton = new Button("Cancel");

	ListBox actuatorLB = new ListBox();
	ListBox scheduleType = new ListBox();
	
	String scheduleName;
	String actuatorName;
	int dayMask;
	String rule;
	boolean actuatorStatus;
	int priority;
	boolean scheduleEnabled;
	
	TextBox scheduleNameTB = new TextBox();
	TextBox ruleTB = new TextBox();
	ListBox actuatorStatusLB = new ListBox();
	ListBox priorityLB = new ListBox();
	ListBox scheduleEnabledLB = new ListBox();
	
	CheckBox monday = new CheckBox("Monday");
	CheckBox tuesday = new CheckBox("Tuesday");
	CheckBox wednesday = new CheckBox("Wednesday");
	CheckBox thursday = new CheckBox("Thursday");
	CheckBox friday = new CheckBox("Friday");
	CheckBox saturday = new CheckBox("Saturday");
	CheckBox sunday = new CheckBox("Sunday");
	
	PopupPanel createSchedulePopup = new PopupPanel();
	
	public SchedulePage(){
		
		setWidgets();
		setHandlers();
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(newScheduleButton);
		buttonPanel.add(goButton);
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(10);
		vPanel.add(new HTML("Please select an actuator:"));
		vPanel.add(actuatorLB);
		vPanel.add(new HTML("Please select a schedule type:"));
		vPanel.add(scheduleType);
		vPanel.add(buttonPanel);
		
		leftPanel.clear();
		leftPanel.setStyleName("parameterPanel");
		leftPanel.add(vPanel);
		
		rightPanel.clear();
		rightPanel.setStyleName("mainStyle");
		rightPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		
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
		
		actuatorStatusLB.clear();
		actuatorStatusLB.addItem("true");
		actuatorStatusLB.addItem("false");

		priorityLB.clear();
		for(int i=0;i<=10;i++)
		{
			priorityLB.addItem(""+i);
		}
		
		scheduleEnabledLB.clear();
		scheduleEnabledLB.addItem("true");
		scheduleEnabledLB.addItem("false");
		
		scheduleMenu.clear();
		scheduleMenu.addLabel("Input schedule name");
		scheduleMenu.addItem(scheduleNameTB);
		scheduleMenu.addLabel("Input schedule rule(s)");
		scheduleMenu.addItem(ruleTB);
		scheduleMenu.addLabel("Set actuator status");
		scheduleMenu.addItem(actuatorStatusLB);
		scheduleMenu.addLabel("Set schedule priority");
		scheduleMenu.addItem(priorityLB);
		scheduleMenu.addLabel("Schedule enabled?");
		scheduleMenu.addItem(scheduleEnabledLB);
		scheduleMenu.addNewRow(createScheduleButton);
		scheduleMenu.addItem(cancelScheduleButton);
		
		createSchedulePopup.setVisible(false);
		createSchedulePopup.setGlassEnabled(true);
		createSchedulePopup.add(scheduleMenu);
	}
	
	private void setHandlers(){
		goButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
			updateTable(actuatorLB.getSelectedItemText(),scheduleType.getSelectedItemText());	
			}
		});
		newScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				scheduleMenu.clear();
				scheduleMenu.addLabel("Input schedule name");
				scheduleMenu.addItem(scheduleNameTB);
				scheduleMenu.addLabel("Input schedule rule(s)");
				scheduleMenu.addItem(ruleTB);
				scheduleMenu.addLabel("Set actuator status");
				scheduleMenu.addItem(actuatorStatusLB);
				scheduleMenu.addLabel("Set schedule priority");
				scheduleMenu.addItem(priorityLB);
				scheduleMenu.addLabel("Schedule enabled?");
				scheduleMenu.addItem(scheduleEnabledLB);
				scheduleMenu.addItem("button",createScheduleButton);
				scheduleMenu.addItem("button",cancelScheduleButton);
				
				createSchedulePopup.setVisible(true);
				createSchedulePopup.center();
				}
			});
		createScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				scheduleName=scheduleNameTB.getText();
				actuatorName=actuatorLB.getSelectedItemText();
				rule=ruleTB.getText();
				dayMask=getSelectedDays();
				actuatorStatus=Boolean.parseBoolean(actuatorStatusLB.getSelectedItemText());
				priority=Integer.parseInt(priorityLB.getSelectedItemText());
				scheduleEnabled=Boolean.parseBoolean(scheduleEnabledLB.getSelectedItemText());
				
				if(scheduleType.getSelectedItemText().equals("Regular Schedule"))
				{
					Utility.newRequestObj().createRegularSchedule(scheduleName, actuatorName, dayMask, rule, actuatorStatus, priority, scheduleEnabled, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							Window.alert("Unable to create regular schedule");
						}
						
						public void onSuccess(final String result) {
							Window.alert(result);
						}
					});
				}
				else if(scheduleType.getSelectedItemText().equals("Special Schedule"))
				{
					Utility.newRequestObj().createSpecialSchedule(scheduleName, actuatorName, dayMask, rule, actuatorStatus, priority, scheduleEnabled, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							Window.alert("Unable to create special schedule");
						}
						
						public void onSuccess(final String result) {
							Window.alert(result);
						}
					});
				}
				else
					Window.alert("schedule type bug");
			}
		});
		cancelScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				createSchedulePopup.setVisible(false);
			}
		});
	}
	
	private int getSelectedDays(){
		String selectedBits="";
		
		selectedBits=
				getBit(sunday.getValue())+
				getBit(saturday.getValue())+
				getBit(friday.getValue())+
				getBit(thursday.getValue())+
				getBit(wednesday.getValue())+
				getBit(tuesday.getValue())+
				getBit(monday.getValue());
		
		return Integer.parseInt(selectedBits);
	};
	
	private String getBit(Boolean checked){
		if(checked)
		{
			return 1+"";
		}
		return 0+"";
	}
	
	private void updateTable(String aName, String scheduleType){String[][]data;
		
		if(scheduleType.equalsIgnoreCase("Regular Schedule"))
		{
			ArrayList<String> rSchedules = Data.actuatorRegularScheduleList.get(aName);
			data = new String[rSchedules.size()][Data.regularScheduleAttributeSize];
			for(int i=0; i<rSchedules.size();i++)
			{
				ArrayList<String> scheduleAttribute = Data.regularScheduleAttributesList.get(rSchedules.get(i));
				for(int j=0; j<scheduleAttribute.size();j++)
				{
					data[i][j]=scheduleAttribute.get(j);
				}
			}
		}
		else
		{
			ArrayList<String> sSchedules = Data.actuatorSpecialScheduleList.get(aName);
			data = new String[sSchedules.size()][Data.specialScheduleAttributeSize];
			for(int i=0; i<sSchedules.size();i++)
			{
				ArrayList<String> scheduleAttribute = Data.specialScheduleAttributesList.get(sSchedules.get(i));
				for(int j=0; j<scheduleAttribute.size();j++)
				{
					data[i][j]=scheduleAttribute.get(j);
				}
			}
		}
		rightPanel.clear();
		rightPanel.add(ChartUtilities.createFlexTable(data));
	}
	
	
}
