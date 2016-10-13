package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SchedulePage extends Composite{
	
	ContentPanel cPanel = new ContentPanel();
	
	VerticalPanel leftPanel = new VerticalPanel();
	VerticalPanel middlePanel = new VerticalPanel();
	VerticalPanel rightPanel = new VerticalPanel();
	
	FilterMenu scheduleMenu = new FilterMenu();
	
	Button goButton = new Button("Go");
	Button newScheduleButton = new Button("New Schedule");
	Button createScheduleButton = new Button("Create");
	Button cancelScheduleButton = new Button("Cancel");
	Button newRuleButton = new Button("New Rule");

	ListBox actuatorLB = new ListBox();
	ListBox scheduleType = new ListBox();
	ListBox ruleLB = new ListBox();
	
	String scheduleName;
	String actuatorName;
	int dayMask;
	String rule;
	String onStart;
	String onEnd;
	int priority;
	boolean scheduleEnabled;
	Boolean lock;
	
	TextBox scheduleNameTB = new TextBox();
	ListBox scheduleActuatorLB = new ListBox();
	ListBox actuatorOnStartLB = new ListBox();
	ListBox actuatorOnEndLB = new ListBox();
	ListBox priorityLB = new ListBox();
	ListBox lockEnabledLB = new ListBox();
	ListBox scheduleEnabledLB = new ListBox();
	
	CheckBox monday = new CheckBox("Monday");
	CheckBox tuesday = new CheckBox("Tuesday");
	CheckBox wednesday = new CheckBox("Wednesday");
	CheckBox thursday = new CheckBox("Thursday");
	CheckBox friday = new CheckBox("Friday");
	CheckBox saturday = new CheckBox("Saturday");
	CheckBox sunday = new CheckBox("Sunday");
	
	PopupPanel createSchedulePopup = new PopupPanel();
	
	FlexTable ruleTable = new FlexTable();
	FlexTable scheduleTable = new FlexTable();
	ArrayList<Object> scheduleAttributeList = new ArrayList<>();
	
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
		
		middlePanel.clear();
		middlePanel.setStyleName("mainStyle");
		middlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		middlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		middlePanel.setSpacing(10);

		ruleTable=ruleTable();
		
		VerticalPanel v2Panel = new VerticalPanel();
		v2Panel.setSpacing(10);
		v2Panel.add(new HTML("Rules"));
		v2Panel.add(ruleTable);
		v2Panel.add(newRuleButton);
		
		rightPanel.clear();
		rightPanel.setStyleName("ruleMenu");
		rightPanel.add(v2Panel);
		
		cPanel.clear();
		cPanel.addLeft(leftPanel);
		cPanel.add(middlePanel);
		cPanel.addRight(rightPanel);
		cPanel.alignMiddlePanelVTop();
		
		initWidget(cPanel);
	}
	
	private void setWidgets(){
		
		actuatorLB.clear();
		scheduleActuatorLB.clear();
		for(String aName: Data.actuatorAttributeList.keySet())
		{
			actuatorLB.addItem(aName);
			scheduleActuatorLB.addItem(aName);
		}
		
		scheduleType.clear();
		scheduleType.addItem("Regular Schedule");
		scheduleType.addItem("Special Schedule");
		
		actuatorOnStartLB.clear();
		actuatorOnStartLB.addItem("ON");
		actuatorOnStartLB.addItem("OFF");
		
		actuatorOnEndLB.clear();
		actuatorOnEndLB.addItem("ON");
		actuatorOnEndLB.addItem("OFF");
		
		ruleLB.clear();
		for(String ruleName: Data.dayScheduleRuleAttributeList.keySet())
		{
			ruleLB.addItem(ruleName);
		}

		priorityLB.clear();
		for(int i=0;i<=10;i++)
		{
			priorityLB.addItem(""+i);
		}
		
		lockEnabledLB.clear();
		lockEnabledLB.addItem("true");
		lockEnabledLB.addItem("false");
		
		scheduleEnabledLB.clear();
		scheduleEnabledLB.addItem("true");
		scheduleEnabledLB.addItem("false");
		
		initializeScheduleMenu();
		
		createSchedulePopup.setVisible(false);
		createSchedulePopup.setGlassEnabled(true);
		createSchedulePopup.add(scheduleMenu);
		
	}
	
	private void setHandlers(){
		goButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				middlePanel.clear();
				middlePanel.add(Utility.addTimer());
				final String actuator=actuatorLB.getSelectedItemText();
				
				Utility.newRequestObj().getActuatorRegularSchedule(actuator, new AsyncCallback<String[][]>() {
					public void onFailure(Throwable caught) {
						Window.alert("Unable to get regular schedule");
						Utility.hideTimer();
					}
					
					public void onSuccess(String[][] result) {
						refreshRegularScheduleData(actuator, result);
						Utility.hideTimer();
						updateTable(scheduleTable,result);
					}
				});
			}
		});
		newScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				initializeScheduleMenu();
				
				createSchedulePopup.setVisible(true);
				createSchedulePopup.center();
				}
			});
		createScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				scheduleName=scheduleNameTB.getText();
				actuatorName=actuatorLB.getSelectedItemText();
				rule=ruleLB.getSelectedItemText();
				dayMask=getSelectedDays();
				onStart=actuatorOnStartLB.getSelectedItemText();
				onEnd=actuatorOnEndLB.getSelectedItemText();
				lock = Boolean.parseBoolean(lockEnabledLB.getSelectedItemText());
				priority=Integer.parseInt(priorityLB.getSelectedItemText());
				scheduleEnabled=Boolean.parseBoolean(scheduleEnabledLB.getSelectedItemText());
				
				if(scheduleType.getSelectedItemText().equals("Regular Schedule"))
				{
					Utility.newRequestObj().createRegularSchedule(scheduleName, actuatorName, dayMask, rule, onStart, onEnd, lock, priority, scheduleEnabled, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							Window.alert("Unable to create regular schedule");
						}
						
						public void onSuccess(final String result) {
							Window.alert("Response: "+result);
							localAppendTable(scheduleTable);
							createSchedulePopup.setVisible(false);
						}
					});
				}
				else if(scheduleType.getSelectedItemText().equals("Special Schedule"))
				{
					Utility.newRequestObj().createSpecialSchedule(scheduleName, actuatorName, dayMask, rule, onStart, onEnd, lock, priority, scheduleEnabled, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							Window.alert("Unable to create special schedule");
						}
						
						public void onSuccess(final String result) {
							Window.alert("Response: "+result);
							localAppendTable(scheduleTable);
							createSchedulePopup.setVisible(false);
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
	
	private void localAppendTable(FlexTable flexTable){
		scheduleAttributeList.clear();
		scheduleAttributeList.add(scheduleName);
		scheduleAttributeList.add(actuatorName);
		scheduleAttributeList.add(dayMask);
		scheduleAttributeList.add(rule);
		scheduleAttributeList.add(actuatorOnStartLB.getSelectedItemText());
		scheduleAttributeList.add(actuatorOnEndLB.getSelectedItemText());
		scheduleAttributeList.add(lock);
		scheduleAttributeList.add(priority);
		scheduleAttributeList.add(scheduleEnabled);
		
		int numRows = flexTable.getRowCount();
		for(int i=0;i<9;i++)
		{
	    flexTable.setText(numRows, i, String.valueOf(scheduleAttributeList.get(i)));
		}
	}
	
	private void setHeaders(FlexTable ft){
		String[] header = {"Schedule Name","Actuator Name","DayMask","Rule","On Start","On End","Lock?","Priority","Schedule Enabled?"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}
	
	private void initializeScheduleMenu(){
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(cancelScheduleButton);
		buttonPanel.add(createScheduleButton);
		
		scheduleMenu.clear();
		scheduleMenu.addLabel("Select an actuator");
		scheduleMenu.addItem(scheduleActuatorLB);
		scheduleMenu.addLabel("Input schedule name");
		scheduleMenu.addItem(scheduleNameTB);
		scheduleMenu.addLabel("Select rule to be applied");
		scheduleMenu.addItem(ruleLB);
		scheduleMenu.addLabel("Select days");
		scheduleMenu.addItem(sunday);
		scheduleMenu.addItem(monday);
		scheduleMenu.addItem(tuesday);
		scheduleMenu.addItem(wednesday);
		scheduleMenu.addItem(thursday);
		scheduleMenu.addItem(friday);
		scheduleMenu.addItem(saturday);
		scheduleMenu.addLabel("Set actuator starting status");
		scheduleMenu.addItem(actuatorOnStartLB);
		scheduleMenu.addLabel("Set actuator ending status");
		scheduleMenu.addItem(actuatorOnEndLB);
		scheduleMenu.addLabel("Set schedule priority");
		scheduleMenu.addItem(priorityLB);
		scheduleMenu.addLabel("Lock enabled?");
		scheduleMenu.addItem(lockEnabledLB);
		scheduleMenu.addLabel("Schedule enabled?");
		scheduleMenu.addItem(scheduleEnabledLB);
		scheduleMenu.addItem(buttonPanel);
	}
	
	private FlexTable ruleTable(){
		String[][] data = new String[Data.dayScheduleRuleAttributeList.size()][Data.ruleAttributeSize];
		int row=0;
		for(String ruleName: Data.dayScheduleRuleAttributeList.keySet())
		{
			int column=0;
			for(String rAttributes: Data.dayScheduleRuleAttributeList.get(ruleName))
			{
				data[row][column]=rAttributes;
				column++;
			}
		}
		return ChartUtilities.createFlexTable(data);
	}
	
	private int getSelectedDays(){
		CheckBox [] chkbx={monday,tuesday,wednesday,thursday,friday,saturday,sunday};
		int mask=0;
		for (int i=0;i<chkbx.length;i++) {
			if (chkbx[i].getValue()) mask=(mask | (1 << (i+1)));
		}
		return mask;
	};
	
	private void refreshRegularScheduleData(String aName, String[][] result){
		ArrayList<String> rSchedules = new ArrayList<>();
		for(int i=0; i<result.length;i++)
		{
			rSchedules.add(result[i][0]);
			ArrayList<String> rScheduleAttributes = new ArrayList<>();
			for(int j=0; j<result[i].length;j++)
			{
				rScheduleAttributes.add(result[i][j]);
			}
			Data.regularScheduleAttributeList.remove(result[i][0]);
			Data.regularScheduleAttributeList.put(result[i][0], rScheduleAttributes);
		}
		Data.actuatorRegularScheduleList.remove(aName);
		Data.actuatorRegularScheduleList.put(aName, rSchedules);
	}
	
	private void updateTable(FlexTable ft, String[][] result){
		scheduleTable.clear();
		setHeaders(scheduleTable);
		ChartUtilities.appendFlexTable(scheduleTable,result);
		middlePanel.clear();
		middlePanel.add(scheduleTable);
	}
	
	
}
