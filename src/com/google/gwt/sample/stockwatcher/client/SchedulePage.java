package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
import com.google.gwt.user.datepicker.client.DateBox;

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
	
	Button updateEditScheduleButton = new Button("Update");
	Button cancelEditScheduleButton = new Button("Cancel");
	Button deleteEditScheduleButton = new Button("Delete");
	
	Button updateEditRuleButton = new Button("Update");
	Button cancelEditRuleButton = new Button("Cancel");
	Button deleteEditRuleButton = new Button("Delete");
	
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
	
	int year;
	int month;
	int day;
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

    DateBox dateBox = new DateBox();
    DateBox formatHolder = new DateBox();

	String currentActuatorView;
	String currentScheduleView;
	String currentSelectedScheduleName;
	String currentSelectedRuleName;
	String dayGroup;
	String sDayGroup;
	
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
	CheckBox [] chkbx={monday,tuesday,wednesday,thursday,friday,saturday,sunday};
	
	String [] DaysShort={"Mo","Tu","We","Th","Fr","Sa","Su"};
	String [] monthName={"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	PopupPanel schedulePopup = new PopupPanel();
	PopupPanel rulePopup = new PopupPanel();
	
	FlexTable ruleTable = new FlexTable();
	FlexTable scheduleTable = new FlexTable();
	
	ArrayList<Object> scheduleAttributeList = new ArrayList<>();
	ArrayList<Object> ruleAttributeList = new ArrayList<>();
	
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
	
	private void refreshRuleTable(){
		VerticalPanel v2Panel = new VerticalPanel();
		v2Panel.setSpacing(10);
		v2Panel.add(new HTML("Rules"));
		v2Panel.add(ruleTable);
		v2Panel.add(newRuleButton);
		
		rightPanel.clear();
		rightPanel.setStyleName("ruleMenu");
		rightPanel.add(v2Panel);
	}
	
	private void setWidgets(){
		initializeScheduleMenu();
		initializeRuleMenu();
		initializeRuleTable();

		scheduleType.setSelectedIndex(0);
		
		Date today = new Date();
		
		formatHolder.setFormat(new DateBox.DefaultFormat(ChartUtilities.requestFormat));

		dateBox.setFormat(new DateBox.DefaultFormat(ChartUtilities.calendarFormat));
		dateBox.getDatePicker().setYearArrowsVisible(true);
		dateBox.setValue(today);
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
	
	private void refreshScheduleMenuWidgets(){
		scheduleNameTB.setText("");
		actuatorLB.clear();
		scheduleActuatorLB.clear();
		
		for(String aName: Data.actuatorAttributeList.keySet())
		{
			actuatorLB.addItem(aName);
			scheduleActuatorLB.addItem(aName);
		}
		
		for(CheckBox chk: chkbx)
		{
			chk.setValue(false);
		}
		
		int selectedSchedule = scheduleType.getSelectedIndex();
		
		scheduleType.clear();
		scheduleType.addItem("Regular Schedule");
		scheduleType.addItem("Special Schedule");
		scheduleType.setSelectedIndex(selectedSchedule);
		
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
	}
	
	private void refreshRuleMenuWidgets(){
		ruleNameTB.setText("");
		sHLB.setSelectedIndex(0);
		sMLB.setSelectedIndex(0);
		eHLB.setSelectedIndex(0);
		eMLB.setSelectedIndex(0);
	};
	
	private void refreshRuleLB(){
		ruleLB.clear();
		for(String ruleName: Data.dayScheduleRuleAttributeList.keySet())
		{
			ruleLB.addItem(ruleName);
		}
	}
	
	private void setHandlers(){
		goButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				middlePanel.clear();
				middlePanel.add(Utility.addTimer());
				String actuator=actuatorLB.getSelectedItemText();
				if(scheduleType.getSelectedItemText().equals("Regular Schedule"))
				{
					getActuatorRegularSchedule(actuator);
				}
				else if(scheduleType.getSelectedItemText().equals("Special Schedule"))
				{
					getActuatorSpecialSchedule(actuator);
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
				
				createRule();
			}
		});
		
		cancelRuleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				rulePopup.setVisible(false);
			}
		});
		
		cancelEditRuleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				rulePopup.setVisible(false);
			}
		});
		updateEditRuleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				sendUpdateRuleRequest();
			}
		});
		deleteEditRuleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				deleteRule();
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
				setCreateScheduleParam();
				sendCreateScheduleRequest();
			}
		});
		
		cancelScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				schedulePopup.setVisible(false);
			}
		});
		
		cancelEditScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				schedulePopup.setVisible(false);
			}
		});
		updateEditScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				sendUpdateScheduleRequest();
			}
		});
		deleteEditScheduleButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				sendDeleteScheduleRequest();
			}
		});
		
		createScheduleType.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent evenet){
				if(createScheduleType.getSelectedItemText().equals("Regular Schedule"))
				{
					scheduleMenu.showGroup(dayGroup);
					scheduleMenu.hideGroup(sDayGroup);
				}
				else if(createScheduleType.getSelectedItemText().equals("Special Schedule"))
				{
					scheduleMenu.showGroup(sDayGroup);
					scheduleMenu.hideGroup(dayGroup);
					
				}
				schedulePopup.center();
			}
		});
	}
	
	private void localAppendRuleTable(FlexTable flexTable){
		ruleAttributeList.clear();
		ruleAttributeList.add(ruleName);
		ruleAttributeList.add(sH);
		ruleAttributeList.add(sM);
		ruleAttributeList.add(eH);
		ruleAttributeList.add(eM);
		
		int numRows = flexTable.getRowCount();
		for(int i=0;i<ruleAttributeList.size();i++)
		{
			flexTable.setText(numRows, i, String.valueOf(ruleAttributeList.get(i)));
		}
		addEditRuleColumnLatestRow(flexTable);
	}
	
	private void setRegularScheduleHeaders(FlexTable ft){
		String[] header = {"Schedule","Actuator","Day Enabled","Rule","On Start","On End","Lock?","Priority","Schedule Enabled?"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}
	
	private void setSpecialScheduleHeaders(FlexTable ft){
		String[] header = {"Schedule","Actuator","Year","Month","Day","Rule","On Start","On End","Lock?","Priority","Schedule Enabled?"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}
	
	private void setRuleHeaders(FlexTable ft){
		String[] header = {"Rule","Start Hour","Start Minute","End Hour","End Minute"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}
	
	private void initializeScheduleMenu(){

		refreshScheduleMenuWidgets();
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(cancelScheduleButton);
		buttonPanel.add(createScheduleButton);
		
		dayGroup = "Day Group";
		sDayGroup = "Special Date Group";
		
		VerticalPanel dayCheckBoxes = new VerticalPanel();
		dayCheckBoxes.add(sunday);
		dayCheckBoxes.add(monday);
		dayCheckBoxes.add(tuesday);
		dayCheckBoxes.add(wednesday);
		dayCheckBoxes.add(thursday);
		dayCheckBoxes.add(friday);
		dayCheckBoxes.add(saturday);
		
		scheduleMenu.clear();
		scheduleMenu.addLabel("Select an actuator");
		scheduleMenu.addItem(scheduleActuatorLB);
		scheduleMenu.addLabel("Input schedule name");
		scheduleMenu.addItem(scheduleNameTB);
		scheduleMenu.addLabel("Select schedule type");
		scheduleMenu.addItem(createScheduleType);
		scheduleMenu.addLabel("Select rule to be applied");
		scheduleMenu.addItem(ruleLB);
		scheduleMenu.addLabel(dayGroup,"Select days");
		scheduleMenu.addItem(dayGroup,dayCheckBoxes);
		scheduleMenu.addLabel(sDayGroup,"Scheduled date");
		scheduleMenu.addItem(sDayGroup,dateBox);
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
		
		scheduleMenu.hideGroup(sDayGroup);
	}
	
	private void initializeEditRegularScheduleMenu(String schedule){
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(cancelEditScheduleButton);
		buttonPanel.add(updateEditScheduleButton);
		buttonPanel.add(deleteEditScheduleButton);
		
		scheduleMenu.clear();
		scheduleMenu.addLabel("Schedule name");
		scheduleMenu.addItem(scheduleNameTB);
		scheduleMenu.addLabel("Rule");
		scheduleMenu.addItem(ruleLB);
		scheduleMenu.addLabel("Select days");
		scheduleMenu.addItem(sunday);
		scheduleMenu.addItem(monday);
		scheduleMenu.addItem(tuesday);
		scheduleMenu.addItem(wednesday);
		scheduleMenu.addItem(thursday);
		scheduleMenu.addItem(friday);
		scheduleMenu.addItem(saturday);
		scheduleMenu.addLabel("Actuator starting status");
		scheduleMenu.addItem(actuatorOnStartLB);
		scheduleMenu.addLabel("Actuator ending status");
		scheduleMenu.addItem(actuatorOnEndLB);
		scheduleMenu.addLabel("Set schedule priority");
		scheduleMenu.addItem(priorityLB);
		scheduleMenu.addLabel("Lock enabled?");
		scheduleMenu.addItem(lockEnabledLB);
		scheduleMenu.addLabel("Schedule enabled?");
		scheduleMenu.addItem(scheduleEnabledLB);
		scheduleMenu.addItem(buttonPanel);
		
		ArrayList<String> scheduleParam = Data.regularScheduleAttributeList.get(schedule);
		scheduleNameTB.setText(scheduleParam.get(0));
		setSelectedDays(scheduleParam.get(2));
		ruleLB.setSelectedIndex(getIndexOfTextInWidget(ruleLB,scheduleParam.get(3)));
		actuatorOnStartLB.setSelectedIndex(getIndexOfTextInWidget(actuatorOnStartLB,scheduleParam.get(4)));
		actuatorOnEndLB.setSelectedIndex(getIndexOfTextInWidget(actuatorOnEndLB,scheduleParam.get(5)));
		lockEnabledLB.setSelectedIndex(getIndexOfTextInWidget(lockEnabledLB,scheduleParam.get(6)));
		priorityLB.setSelectedIndex(getIndexOfTextInWidget(priorityLB,scheduleParam.get(7)));
		scheduleEnabledLB.setSelectedIndex(getIndexOfTextInWidget(scheduleEnabledLB,scheduleParam.get(8)));
		
	}
	
	private void initializeEditSpecialScheduleMenu(String schedule){
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(cancelEditScheduleButton);
		buttonPanel.add(updateEditScheduleButton);
		buttonPanel.add(deleteEditScheduleButton);
		
		scheduleMenu.clear();
		scheduleMenu.addLabel("Schedule name");
		scheduleMenu.addItem(scheduleNameTB);
		scheduleMenu.addLabel("Rule");
		scheduleMenu.addItem(ruleLB);
		scheduleMenu.addLabel("Scheduled date");
		scheduleMenu.addItem(dateBox);
		scheduleMenu.addLabel("Actuator starting status");
		scheduleMenu.addItem(actuatorOnStartLB);
		scheduleMenu.addLabel("Actuator ending status");
		scheduleMenu.addItem(actuatorOnEndLB);
		scheduleMenu.addLabel("Set schedule priority");
		scheduleMenu.addItem(priorityLB);
		scheduleMenu.addLabel("Lock enabled?");
		scheduleMenu.addItem(lockEnabledLB);
		scheduleMenu.addLabel("Schedule enabled?");
		scheduleMenu.addItem(scheduleEnabledLB);
		scheduleMenu.addItem(buttonPanel);
		
		ArrayList<String> scheduleParam = Data.specialScheduleAttributeList.get(schedule);
		scheduleNameTB.setText(scheduleParam.get(0));
		ruleLB.setSelectedIndex(getIndexOfTextInWidget(ruleLB,scheduleParam.get(5)));
		actuatorOnStartLB.setSelectedIndex(getIndexOfTextInWidget(actuatorOnStartLB,scheduleParam.get(6)));
		actuatorOnEndLB.setSelectedIndex(getIndexOfTextInWidget(actuatorOnEndLB,scheduleParam.get(7)));
		lockEnabledLB.setSelectedIndex(getIndexOfTextInWidget(lockEnabledLB,scheduleParam.get(8)));
		priorityLB.setSelectedIndex(getIndexOfTextInWidget(priorityLB,scheduleParam.get(9)));
		scheduleEnabledLB.setSelectedIndex(getIndexOfTextInWidget(scheduleEnabledLB,scheduleParam.get(10)));
		
		year=Integer.parseInt(scheduleParam.get(2));
		month=Integer.parseInt(scheduleParam.get(3));
		day=Integer.parseInt(scheduleParam.get(4));

		Date date = new Date();
		date.setYear(year-1900);
		date.setMonth(month-1);
		date.setDate(day);

		dateBox.setValue(date);
	}
	
	private int getIndexOfTextInWidget(ListBox lb, String text){
		for(int i=0; i<lb.getItemCount();i++)
		{
			if(text.equals(lb.getItemText(i)))
			{
				return i;
			}
		}
		return 0;
	}
	
	private void initializeRuleMenu(){

		refreshRuleMenuWidgets();
		
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
	
	private void initializeEditRuleMenu(String rule){
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(cancelEditRuleButton);
		buttonPanel.add(updateEditRuleButton);
		buttonPanel.add(deleteEditRuleButton);
		
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
		
		ArrayList<String> scheduleParam = Data.dayScheduleRuleAttributeList.get(rule);
		ruleNameTB.setText(scheduleParam.get(0));
		sHLB.setSelectedIndex(getIndexOfTextInWidget(sHLB,scheduleParam.get(1)));
		sMLB.setSelectedIndex(getIndexOfTextInWidget(sMLB,scheduleParam.get(2)));
		eHLB.setSelectedIndex(getIndexOfTextInWidget(eHLB,scheduleParam.get(3)));
		eMLB.setSelectedIndex(getIndexOfTextInWidget(eMLB,scheduleParam.get(4)));
	}
	
	private void initializeRuleTable(){
		rulePopup.setVisible(false);
		
		FlexTable ft = new FlexTable();
		setRuleHeaders(ft);
		
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
		ruleTable = ChartUtilities.appendFlexTable(ft,data);
		addEditRuleColumnAllRow(ruleTable);
		
		refreshRuleTable();
	}
	
	private void addRuleData(String name, int a, int b, int c, int d){
		ArrayList<String> list = new ArrayList<>();
		list.add(ruleName);
		list.add(Integer.toString(a));
		list.add(Integer.toString(b));
		list.add(Integer.toString(c));
		list.add(Integer.toString(d));
		Data.dayScheduleRuleAttributeList.put(ruleName, list);
		
		refreshRuleLB();
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
	
	private void updateRegularScheduleTable(FlexTable ft, String[][] result){
		schedulePopup.setVisible(false);
		
		ft.setStyleName("fancyTable");
		
		FlexTable newTable = new FlexTable();
		setRegularScheduleHeaders(newTable);
		ft = ChartUtilities.appendFlexTable(newTable, result);
		addEditRegularScheduleColumnAllRow(ft);
		convertDayMaskToString(ft);
		
		middlePanel.clear();
		middlePanel.add(ft);
		
		scheduleType.setSelectedIndex(0);
		currentScheduleView=scheduleType.getSelectedItemText();
	}
	
	private void updateSpecialScheduleTable(FlexTable ft, String[][] result){
		schedulePopup.setVisible(false);
		
		ft.setStyleName("fancyTable");
		
		FlexTable newTable = new FlexTable();
		setSpecialScheduleHeaders(newTable);
		ft = ChartUtilities.appendFlexTable(newTable, result);
		addEditSpecialScheduleColumnAllRow(ft);
		formatMonth(ft);
		
		middlePanel.clear();
		middlePanel.add(ft);
		
		scheduleType.setSelectedIndex(1);
		currentScheduleView=scheduleType.getSelectedItemText();
	}
	
	private void addEditRegularScheduleColumnAllRow(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			Anchor edit = new Anchor("Edit");
			ft.setWidget(i, ft.getCellCount(i), edit);
			edit.setName(ft.getText(i, 0));
			setEditRegularScheduleClickHandler(edit);
		}
	}
	
	private void addEditSpecialScheduleColumnAllRow(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			Anchor edit = new Anchor("Edit");
			ft.setWidget(i, ft.getCellCount(i), edit);
			edit.setName(ft.getText(i, 0));
			setEditSpecialScheduleClickHandler(edit);
		}
	}
	
	private void setEditRegularScheduleClickHandler(final Anchor anchor){
		anchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				currentSelectedScheduleName=anchor.getName();
				initializeEditRegularScheduleMenu(anchor.getName());
				
				schedulePopup.setVisible(true);
				schedulePopup.center();
				}
			});
	}
	
	private void setEditSpecialScheduleClickHandler(final Anchor anchor){
		anchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				currentSelectedScheduleName=anchor.getName();
				initializeEditSpecialScheduleMenu(anchor.getName());
				
				schedulePopup.setVisible(true);
				schedulePopup.center();
				}
			});
	}
	
	private void setEditRuleClickHandler(final Anchor anchor){
		anchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				currentSelectedRuleName=anchor.getName();
				initializeEditRuleMenu(anchor.getName());
				
				rulePopup.setVisible(true);
				rulePopup.center();
				}
			});
	}

	private void addEditRuleColumnAllRow(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			final Anchor edit = new Anchor("Edit");
			ft.setWidget(i, ft.getCellCount(i), edit);
			edit.setName(ft.getText(i, 0));
			setEditRuleClickHandler(edit);
		}
	}
	
	private void addEditRuleColumnLatestRow(FlexTable ft){
		
		int lastRow = ft.getRowCount()-1;
		
		final Anchor edit = new Anchor("Edit");
		ft.setWidget(lastRow, ft.getCellCount(lastRow), edit);
		edit.setName(ft.getText(lastRow, 0));
		setEditRuleClickHandler(edit);
		}
	
	private void convertDayMaskToString(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			StringBuilder sb=new StringBuilder();
			String dayMask = ft.getText(i, 2);
			int dayMaskInt = Integer.parseInt(dayMask);
			
			for (int j=1;j<=7;j++) { //1 = Monday, 7 = Sunday
				if ((dayMaskInt & (1 << j))!=0) {
					sb.append(DaysShort[j-1]);
					sb.append(',');
				}
			}
			String s=sb.toString();
			ft.setText(i, 2, s.substring(0,s.length()-1));
		}
	}
	
	private void formatMonth(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			
			int monthInt = Integer.parseInt(ft.getText(i, 3));
			ft.setText(i, 3, monthName[monthInt-1]);
		}
	}
	
	private int getSelectedDays(){
		int mask=0;
		for (int i=0;i<chkbx.length;i++) {
			if (chkbx[i].getValue()) mask=(mask | (1 << (i+1)));
		}
		return mask;
	};
	
	private void setSelectedDays(String mask){
		int dayMaskInt = Integer.parseInt(mask);
		for (int i=1;i<=7;i++) { //1 = Monday, 7 = Sunday
			chkbx[i-1].setValue((dayMaskInt & (1 << i))!=0);
		}
	};
	
	private void extractEditRuleData(){
		ruleName = ruleNameTB.getText();
		sH = Integer.parseInt(sHLB.getSelectedItemText());
		sM = Integer.parseInt(sMLB.getSelectedItemText());
		eH = Integer.parseInt(eHLB.getSelectedItemText());
		eM = Integer.parseInt(eMLB.getSelectedItemText());
	}
	
	private void sendUpdateRuleRequest(){
		extractEditRuleData();
		updateRule();
	}
	
	private void createRule(){
		Utility.newRequestObj().createDayScheduleRule(ruleName, sH, sM, eH, eM, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to create rule");
			}
			
			public void onSuccess(String result) {
				Window.alert("Creating rule: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					addRuleData(ruleName, sH, sM, eH, eM);
					localAppendRuleTable(ruleTable);
					rulePopup.setVisible(false);
				}
			}
		});
	}
	
	private void deleteRule(){
		Utility.newRequestObj().deleteDayScheduleRule(currentSelectedRuleName, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to delete rule");
			}
			
			public void onSuccess(String result) {
				Window.alert("Deleting rule: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					Data.dayScheduleRuleAttributeList.remove(currentSelectedRuleName);
					initializeRuleTable();
				}
			}
		});
	}
	
	private void updateRule(){
		Utility.newRequestObj().updateDayScheduleRule(currentSelectedRuleName, ruleName, sH, sM, eH, eM, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update rule");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Updating rule: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					ArrayList<String> temp = new ArrayList<>();
					temp.add(ruleName);
					temp.add(String.valueOf(sH));
					temp.add(String.valueOf(sM));
					temp.add(String.valueOf(eH));
					temp.add(String.valueOf(eM));
					Data.dayScheduleRuleAttributeList.remove(currentSelectedRuleName);
					Data.dayScheduleRuleAttributeList.put(ruleName, temp);

					initializeRuleTable();
				}
			}
		});
	}
	
	private String getSpecialDate(){
		return (formatHolder.getFormat().format(dateBox, dateBox.getValue())+"");
	}
	
	//-------------------------------------------------Scheduling Methods------------------------------------------------------//
	
	private void setCreateScheduleParam(){
		scheduleName=scheduleNameTB.getText();
		actuatorName=actuatorLB.getSelectedItemText();
		rule=ruleLB.getSelectedItemText();
		onStart=actuatorOnStartLB.getSelectedItemText();
		onEnd=actuatorOnEndLB.getSelectedItemText();
		lock = Boolean.parseBoolean(lockEnabledLB.getSelectedItemText());
		priority=Integer.parseInt(priorityLB.getSelectedItemText());
		scheduleEnabled=Boolean.parseBoolean(scheduleEnabledLB.getSelectedItemText());
		
		if(createScheduleType.getSelectedItemText().equals("Regular Schedule")){
			dayMask=getSelectedDays();
		}
		else if(createScheduleType.getSelectedItemText().equals("Special Schedule")){
			String[] temp = getSpecialDate().split("-");
			year = Integer.parseInt(temp[0]);
			month = Integer.parseInt(temp[1]);
			day = Integer.parseInt(temp[2]);
		}
	}
	
	private void sendUpdateScheduleRequest(){
		extractEditScheduleData(currentScheduleView);
		sendUpdateScheduleRequest(currentScheduleView);
//		sendEditScheduleRequest(currentScheduleView);
	}
	
	private void sendDeleteScheduleRequest(){
		if(currentScheduleView.equals("Regular Schedule"))
		{
			deleteRegularSchedule(currentSelectedScheduleName);
		}
		else if(currentScheduleView.equals("Special Schedule"))
		{
			deleteSpecialSchedule(currentSelectedScheduleName);
		}
	}
	
	private void extractEditScheduleData(String scheduleType){
		scheduleName=scheduleNameTB.getText();
		actuatorName=currentActuatorView;
		rule=ruleLB.getSelectedItemText();
		onStart=actuatorOnStartLB.getSelectedItemText();
		onEnd=actuatorOnEndLB.getSelectedItemText();
		lock = Boolean.parseBoolean(lockEnabledLB.getSelectedItemText());
		priority=Integer.parseInt(priorityLB.getSelectedItemText());
		scheduleEnabled=Boolean.parseBoolean(scheduleEnabledLB.getSelectedItemText());
		
		if(scheduleType.equals("Regular Schedule")){
			dayMask=getSelectedDays();
		}
		else if(scheduleType.equals("Special Schedule")){
			String[] temp = getSpecialDate().split("-");
			year = Integer.parseInt(temp[0]);
			month = Integer.parseInt(temp[1]);
			day = Integer.parseInt(temp[2]);
		}
		
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
	}

	private void sendUpdateScheduleRequest(String scheduleType){
		if(scheduleType.equals("Regular Schedule"))
		{
			updateRegularSchedule();
		}
		else if(scheduleType.equals("Special Schedule"))
		{
			updateSpecialSchedule();
		}
	}
	
	private void sendCreateScheduleRequest(){
		if(createScheduleType.getSelectedItemText().equals("Regular Schedule"))
		{
			createRegularSchedule();
		}
		else if(createScheduleType.getSelectedItemText().equals("Special Schedule"))
		{
			createSpecialSchedule();
		}
		else
			Window.alert("schedule type bug");
	}
	
	private void createRegularSchedule(){
		Utility.newRequestObj().createRegularSchedule(scheduleName, actuatorName, dayMask, rule, onStart, onEnd, lock, priority, scheduleEnabled, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to create regular schedule");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Creating regular schedule: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					getActuatorRegularSchedule(actuatorName);
				}
			}
		});
	}
	
	private void createSpecialSchedule(){
		Utility.newRequestObj().createSpecialSchedule(scheduleName, actuatorName, year, month, day, rule, onStart, onEnd, lock, priority, scheduleEnabled, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to create special schedule");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Creating special schedule: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					getActuatorSpecialSchedule(actuatorName);
				}
			}
		}); 
	}
	
	private void updateRegularSchedule(){
		Utility.newRequestObj().updateRegularSchedule(currentSelectedScheduleName ,scheduleName, actuatorName, dayMask, rule, onStart, onEnd, lock, priority, scheduleEnabled, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to create regular schedule");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Updating regular schedule: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					getActuatorRegularSchedule(actuatorName);
				}
			}
		});
	}
	
	private void updateSpecialSchedule(){
		Utility.newRequestObj().updateSpecialSchedule(currentSelectedScheduleName, scheduleName, actuatorName, year, month, day, rule, onStart, onEnd, lock, priority, scheduleEnabled, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update special schedule");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Updating special schedule: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					getActuatorSpecialSchedule(actuatorName);
				}
			}
		}); 
	}
	
	private void deleteRegularSchedule(final String scheduleName){
		Utility.newRequestObj().deleteRegularSchedule(scheduleName, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to delete regular schedule");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Deletion: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					ArrayList<String> data = Data.actuatorRegularScheduleList.get(actuatorName);
					data.remove(scheduleName);
					
					Data.actuatorRegularScheduleList.remove(actuatorName);
					Data.actuatorRegularScheduleList.put(actuatorName, data);
					
					Data.regularScheduleAttributeList.remove(scheduleName);
					
					updateRegularScheduleTable(scheduleTable,regularScheduleCache());
				}
			}
		});
	}
	
	private void deleteSpecialSchedule(final String scheduleName){
		Utility.newRequestObj().deleteSpecialSchedule(scheduleName, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to delete special schedule");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Deletion: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
					ArrayList<String> data = Data.actuatorSpecialScheduleList.get(actuatorName);
					data.remove(scheduleName);
					
					Data.actuatorSpecialScheduleList.remove(actuatorName);
					Data.actuatorSpecialScheduleList.put(actuatorName, data);
					
					Data.specialScheduleAttributeList.remove(scheduleName);
					
					updateSpecialScheduleTable(scheduleTable,specialScheduleCache());
				}
			}
		});
	}
	
	private void getActuatorRegularSchedule(final String actuator){
		Utility.newRequestObj().getActuatorRegularSchedule(actuator, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get regular schedule");
				Utility.hideTimer();
			}
			
			public void onSuccess(String[][] result) {
				if(Utility.isNull(result))
				{
					Window.alert("No regular schedule found");
					Utility.hideTimer();
				}
				else
				{
				currentActuatorView=actuatorLB.getSelectedItemText();
				refreshRegularScheduleData(actuator, result);
				Utility.hideTimer();
				updateRegularScheduleTable(scheduleTable,result);
				}
			}
		});
	}
	
	private void getActuatorSpecialSchedule(final String actuator){
		Utility.newRequestObj().getActuatorSpecialSchedule(actuator, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get special schedule");
				Utility.hideTimer();
			}
			
			public void onSuccess(String[][] result) {
				if(Utility.isNull(result))
				{
					Window.alert("No special schedule found");
					Utility.hideTimer();
				}
				else
				{
					currentActuatorView=actuatorLB.getSelectedItemText();
					refreshSpecialScheduleData(actuator, result);
					Utility.hideTimer();
					updateSpecialScheduleTable(scheduleTable,result);
				}
			}
		});
	}
	
	public String[][] regularScheduleCache(){
		String[][] temp = new String[Data.regularScheduleAttributeList.size()][Data.regularScheduleAttributeSize];
		int row=0;
		for(String schedule: Data.regularScheduleAttributeList.keySet())
		{
			ArrayList<String> attributes = Data.regularScheduleAttributeList.get(schedule);
			for(int i=0; i<attributes.size();i++)
			{
				temp[row][i]=attributes.get(i);
			}
			row++;
		}
		return temp;
	}
	
	public String[][] specialScheduleCache(){
		String[][] temp = new String[Data.specialScheduleAttributeList.size()][Data.specialScheduleAttributeSize];
		int row=0;
		for(String schedule: Data.specialScheduleAttributeList.keySet())
		{
			ArrayList<String> attributes = Data.specialScheduleAttributeList.get(schedule);
			for(int i=0; i<attributes.size();i++)
			{
				temp[row][i]=attributes.get(i);
			}
			row++;
		}
		return temp;
	}
	
//	private void localAppendScheduleTable(FlexTable flexTable){
//		scheduleAttributeList.clear();
//		scheduleAttributeList.add(scheduleName);
//		scheduleAttributeList.add(actuatorName);
//		scheduleAttributeList.add(dayMask);
//		scheduleAttributeList.add(rule);
//		scheduleAttributeList.add(actuatorOnStartLB.getSelectedItemText());
//		scheduleAttributeList.add(actuatorOnEndLB.getSelectedItemText());
//		scheduleAttributeList.add(lock);
//		scheduleAttributeList.add(priority);
//		scheduleAttributeList.add(scheduleEnabled);
//		
//		int numRows = flexTable.getRowCount();
//		for(int i=0;i<scheduleAttributeList.size();i++)
//		{
//			flexTable.setText(numRows, i, String.valueOf(scheduleAttributeList.get(i)));
//		}
//		addEditScheduleColumnLatestRow(flexTable);
//		convertDayMaskToString(flexTable);
//	}
	
//	private void addEditScheduleColumnLatestRow(FlexTable ft){
//		int lastRow = ft.getRowCount()-1;
//		
//		Anchor edit = new Anchor("Edit");
//		ft.setWidget(lastRow, ft.getCellCount(lastRow), edit);
//		edit.setName(ft.getText(lastRow, 0));
//		setEditScheduleClickHandler(edit);
//		}
	
}
