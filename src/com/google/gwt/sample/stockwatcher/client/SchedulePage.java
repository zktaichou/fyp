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
	FilterMenu ruleMenu = new FilterMenu();
	
	Button goButton = new Button("Go");
	Button newScheduleButton = new Button("New Schedule");
	Button createScheduleButton = new Button("Create");
	Button cancelScheduleButton = new Button("Cancel");
	Button newRuleButton = new Button("New Rule");
	Button createRuleButton = new Button("Create");
	Button cancelRuleButton = new Button("Cancel");

	ListBox sHLB = new ListBox();
	ListBox sMLB = new ListBox();
	ListBox eHLB = new ListBox();
	ListBox eMLB = new ListBox();
	ListBox actuatorLB = new ListBox();
	ListBox createScheduleType = new ListBox();
	ListBox scheduleType = new ListBox();
	ListBox ruleLB = new ListBox();
	
	int sH;
	int sM;
	int eH;
	int eM;
	String ruleName;
	String scheduleName;
	String actuatorName;
	int dayMask;
	String rule;
	String onStart;
	String onEnd;
	int priority;
	boolean scheduleEnabled;
	Boolean lock;
	
	TextBox ruleNameTB = new TextBox();
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
	
	PopupPanel schedulePopup = new PopupPanel();
	PopupPanel rulePopup = new PopupPanel();
	
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
		
		createScheduleType.clear();
		createScheduleType.addItem("Regular Schedule");
		createScheduleType.addItem("Special Schedule");
		
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
		for(int i=1;i<=10;i++)
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
		initializeRuleMenu();
		initializeRuleTable();
		
		schedulePopup.setVisible(false);
		schedulePopup.setGlassEnabled(true);
		schedulePopup.add(scheduleMenu);

		for(int i=1; i<=24; i++)
		{
			sHLB.addItem(Integer.toString(i));
			eHLB.addItem(Integer.toString(i));
		}
		
		for(int i=0; i<=55; i+=5)
		{
			sMLB.addItem(Integer.toString(i));
			eMLB.addItem(Integer.toString(i));
		}
		
		rulePopup.setVisible(false);
		rulePopup.setGlassEnabled(true);
		rulePopup.add(ruleMenu);
		
	}
	
	private void setHandlers(){
		goButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				middlePanel.clear();
				middlePanel.add(Utility.addTimer());
				final String actuator=actuatorLB.getSelectedItemText();
				if(scheduleType.getSelectedItemText().equals("Regular Schedule"))
				{
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
				else if(scheduleType.getSelectedItemText().equals("Special Schedule"))
				{
					Utility.newRequestObj().getActuatorSpecialSchedule(actuator, new AsyncCallback<String[][]>() {
						public void onFailure(Throwable caught) {
							Window.alert("Unable to get special schedule");
							Utility.hideTimer();
						}
						
						public void onSuccess(String[][] result) {
							refreshSpecialScheduleData(actuator, result);
							Utility.hideTimer();
							updateTable(scheduleTable,result);
						}
					});
				}
			}
		});
		newRuleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				initializeRuleMenu();
				
				rulePopup.setVisible(true);
				rulePopup.center();
				}
			});
		createRuleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				ruleName = ruleNameTB.getText();
				sH = Integer.parseInt(sHLB.getSelectedItemText());
				sM = Integer.parseInt(sMLB.getSelectedItemText());
				eH = Integer.parseInt(eHLB.getSelectedItemText());
				eM = Integer.parseInt(eMLB.getSelectedItemText());
				
				Utility.newRequestObj().createDayScheduleRule(ruleName, sH, sM, eH, eM, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert("Unable to create rule");
					}
					
					public void onSuccess(final String result) {
						Window.alert("Response: "+result);
						if(result.equalsIgnoreCase("OK"))
						{
							refreshRuleData(ruleName, sH, sM, eH, eM);
							localAppendTable(ruleTable);
							rulePopup.setVisible(false);
						}
					}
				});
				
			}
		});
		cancelRuleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				rulePopup.setVisible(false);
			}
		});
		newScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				initializeScheduleMenu();
				
				schedulePopup.setVisible(true);
				schedulePopup.center();
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
				
				if(createScheduleType.getSelectedItemText().equals("Regular Schedule"))
				{
					Utility.newRequestObj().createRegularSchedule(scheduleName, actuatorName, dayMask, rule, onStart, onEnd, lock, priority, scheduleEnabled, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							Window.alert("Unable to create regular schedule");
						}
						
						public void onSuccess(final String result) {
							Window.alert("Response: "+result);
							if(result.equalsIgnoreCase("OK"))
							{
								localAppendTable(scheduleTable);
								schedulePopup.setVisible(false);
							}
						}
					});
				}
				else if(createScheduleType.getSelectedItemText().equals("Special Schedule"))
				{
					Utility.newRequestObj().createSpecialSchedule(scheduleName, actuatorName, dayMask, rule, onStart, onEnd, lock, priority, scheduleEnabled, new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							Window.alert("Unable to create special schedule");
						}
						
						public void onSuccess(final String result) {
							Window.alert("Response: "+result);
							if(result.equalsIgnoreCase("OK"))
							{
								localAppendTable(scheduleTable);
								schedulePopup.setVisible(false);
							}
						}
					}); 
				}
				else
					Window.alert("schedule type bug");
			}
		});
		cancelScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				schedulePopup.setVisible(false);
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
	
	private void setScheduleHeaders(FlexTable ft){
		String[] header = {"Schedule Name","Actuator Name","DayMask","Rule","On Start","On End","Lock?","Priority","Schedule Enabled?"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}
	
	private void setRuleHeaders(FlexTable ft){
		String[] header = {"Rule Name","Start Hour","Start Minute","End Hour","End Minute"};
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
		scheduleMenu.addLabel("Select schedule type");
		scheduleMenu.addItem(createScheduleType);
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
	
	private void initializeRuleMenu(){
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(cancelRuleButton);
		buttonPanel.add(createRuleButton);
		
		VerticalPanel hourPanel = new VerticalPanel();
		hourPanel.setSpacing(10);
		hourPanel.add(new HTML("Set start hour"));
		hourPanel.add(sHLB);
		hourPanel.add(new HTML("Set end hour"));
		hourPanel.add(eHLB);
		
		VerticalPanel minutePanel = new VerticalPanel();
		minutePanel.setSpacing(10);
		minutePanel.add(new HTML("Set start minute"));
		minutePanel.add(sMLB);
		minutePanel.add(new HTML("Set end minute"));
		minutePanel.add(eMLB);
		
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.add(hourPanel);
		wrapper.add(minutePanel);
		
		ruleMenu.clear();
		ruleMenu.addLabel("Input rule name");
		ruleMenu.addItem(ruleNameTB);
		ruleMenu.addSeparator();
		ruleMenu.addNewRow(wrapper);
		ruleMenu.addSeparator();
		ruleMenu.addNewRow(buttonPanel);
	}
	
	private void initializeRuleTable(){
		ruleTable.clear();
		setRuleHeaders(ruleTable);
		
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
			row++;
		}
		ChartUtilities.appendFlexTable(ruleTable,data);
	}
	
	private int getSelectedDays(){
		CheckBox [] chkbx={monday,tuesday,wednesday,thursday,friday,saturday,sunday};
		int mask=0;
		for (int i=0;i<chkbx.length;i++) {
			if (chkbx[i].getValue()) mask=(mask | (1 << (i+1)));
		}
		return mask;
	};
	
	private void refreshRuleData(String name, int a, int b, int c, int d){
		ArrayList<String> list = new ArrayList<>();
		list.add(ruleName);
		list.add(Integer.toString(a));
		list.add(Integer.toString(b));
		list.add(Integer.toString(c));
		list.add(Integer.toString(d));
		Data.dayScheduleRuleAttributeList.put(ruleName, list);
	}
	
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
	
	private void refreshSpecialScheduleData(String aName, String[][] result){
		ArrayList<String> rSchedules = new ArrayList<>();
		for(int i=0; i<result.length;i++)
		{
			rSchedules.add(result[i][0]);
			ArrayList<String> rScheduleAttributes = new ArrayList<>();
			for(int j=0; j<result[i].length;j++)
			{
				rScheduleAttributes.add(result[i][j]);
			}
			Data.specialScheduleAttributeList.remove(result[i][0]);
			Data.specialScheduleAttributeList.put(result[i][0], rScheduleAttributes);
		}
		Data.actuatorSpecialScheduleList.remove(aName);
		Data.actuatorSpecialScheduleList.put(aName, rSchedules);
	}
	
	private void updateTable(FlexTable ft, String[][] result){
		scheduleTable.clear();
		setScheduleHeaders(scheduleTable);
		ChartUtilities.appendFlexTable(scheduleTable,result);
		middlePanel.clear();
		middlePanel.add(scheduleTable);
	}
	
	
}
