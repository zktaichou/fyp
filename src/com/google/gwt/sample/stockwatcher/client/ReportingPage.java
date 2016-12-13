package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import org.moxieapps.gwt.highcharts.client.*;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class ReportingPage extends Composite {

	ContentPanel contentPanel = new ContentPanel();
	VerticalPanel parameterPanel = new VerticalPanel();
	VerticalPanel filterPanel = new VerticalPanel();
	
	public static HorizontalPanel chartPanel = new HorizontalPanel();
	HorizontalPanel buttonPanel = new HorizontalPanel();
	HorizontalPanel selectionPanel = new HorizontalPanel();

	FilterMenu advancedFilterMenu = new FilterMenu();
	FilterMenu basicFilterMenu = new FilterMenu();
	
	CheckBox filterBox = new CheckBox("Advanced filter");
	public static CheckBox predictionBox = new CheckBox("Show prediction");
	
	ListBox siteListBox = new ListBox();
	ListBox siteControllerListBox = new ListBox();
	ListBox controllerSensorListBox = new ListBox();
	ListBox reportViewLB = new ListBox();
	ListBox reportSortLB = new ListBox();
	
	FlexTable table = new FlexTable();
	StockChart chart = new StockChart();
	
	Button backButton = new Button("Back");
	Button goButton = new Button("Go");
	Button refreshButton = new Button("Refresh chart");
	
    DateBox sDateBox = new DateBox();
    DateBox eDateBox = new DateBox();
    DateBox formatHolder = new DateBox();
    
    String sDay;
    String sMonth;
    String sYear;
    String eDay;
    String eMonth;
    String eYear;
    
    TextBox tb = new TextBox();
    
	public ReportingPage(){
		Header.setHeaderTitle("Main Menu > Monitoring > Historical");
		setHandlers();
		setWidgetContent();
		
		basicFilterMenu.clear();
		basicFilterMenu.setStyleName("mainStyle");
		basicFilterMenu.addLabel("View Reading By");
		basicFilterMenu.addItem(reportViewLB);
		basicFilterMenu.addLabel("Sort Reading By");
		basicFilterMenu.addItem(reportSortLB);
		
		advancedFilterMenu.clear();
		advancedFilterMenu.setStyleName("mainStyle");
		advancedFilterMenu.setVisible(false);
		advancedFilterMenu.addLabel("Start date");
		advancedFilterMenu.addItem(sDateBox);
		advancedFilterMenu.addLabel("End date");
		advancedFilterMenu.addItem(eDateBox);
		advancedFilterMenu.addLabel("Prediction menu");
		advancedFilterMenu.addItem(predictionBox);
		advancedFilterMenu.addLabel("Prediction", "Input steps");
		advancedFilterMenu.addItem("Prediction",tb);
		advancedFilterMenu.addNewRow(refreshButton);
		
		advancedFilterMenu.hideGroup("Prediction");

		VerticalPanel temp = new VerticalPanel();
		temp.setSpacing(10);
		temp.add(filterBox);
		temp.add(advancedFilterMenu);
		
		filterPanel.clear();
		filterPanel.add(temp);
		filterPanel.setStyleName("filterMenu");
		
		VerticalPanel selectionPanel = new VerticalPanel();
		selectionPanel.setStyleName("mainStyle");
		selectionPanel.add(new HTML("<h2>Selection Menu</h2></br>"));
		selectionPanel.add(new HTML("Please select site:"));
		selectionPanel.add(siteListBox);
		selectionPanel.add(new HTML("Please select controller:"));
		selectionPanel.add(siteControllerListBox);
		selectionPanel.add(new HTML("Please select sensor:"));
		selectionPanel.add(controllerSensorListBox);
		selectionPanel.add(new HTML("<h3>Basic Filter Menu</h3></br>"));
		selectionPanel.add(basicFilterMenu);
		selectionPanel.add(goButton);
		selectionPanel.setSpacing(10);
		
		parameterPanel.clear();
		parameterPanel.add(selectionPanel);
		parameterPanel.setHeight("100%");
		parameterPanel.setStyleName("parameterPanel");
		
		chartPanel.clear();
		chartPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		chartPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		contentPanel.clear();
		contentPanel.addLeft(parameterPanel);
		contentPanel.add(chartPanel);
		contentPanel.addRight(filterPanel);
		
		initWidget(contentPanel);
		}
	
	private Boolean isSensorSelected(){
		if(siteControllerListBox.getSelectedItemText().equals("None"))
		{
			Window.alert("No controller is selected");
			return false;
		}
		if(controllerSensorListBox.getSelectedItemText().equals("None"))
		{
			Window.alert("No sensor is selected");
			return false;
		}
		return true;
	}
	
	public void setHandlers(){
		filterBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			   @Override
			   public void onValueChange(ValueChangeEvent<Boolean> event) {
					advancedFilterMenu.setVisible(filterBox.getValue());
			  }
		});
		siteListBox.addChangeHandler(new ChangeHandler() {
		      public void onChange(ChangeEvent event) {
		          showControllers(siteControllerListBox, siteListBox.getSelectedItemText());
		          showSensors(controllerSensorListBox, siteControllerListBox.getSelectedItemText());
		      }
		});
		siteControllerListBox.addChangeHandler(new ChangeHandler() {
		      public void onChange(ChangeEvent event) {
		          showSensors(controllerSensorListBox, siteControllerListBox.getSelectedItemText());
		      }
		});
		goButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(isSensorSelected())
				{
					refreshAdvanceFilterMenu();
					filterBox.setValue(false, true);
					sendDataToServer();
				}
			};
		});
		refreshButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				sendDataToServer();
			};
		});
		predictionBox.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				advancedFilterMenu.hideGroup("Prediction");
				if(predictionBox.getValue())
				{
					advancedFilterMenu.showGroup("Prediction");
				}
			};
		});
	}
	
	private void refreshAdvanceFilterMenu(){
		predictionBox.setValue(false);
	}
	
	private void sendDataToServer(){
		if(isSensorSelected())
		{
			
			chartPanel.clear();
			chartPanel.add(Utility.addTimer());
			
				if(predictionBox.getValue())
				{
					if(Utility.isNumeric(tb.getText()))
					{
						int steps = Integer.parseInt(tb.getText());
						ChartCreationHandler.acceptParam(controllerSensorListBox.getSelectedItemText(), 
								ChartUtilities.stringToStartDate(getSDate()), 
								ChartUtilities.stringToEndDate(getEDate()), 
								predictionBox.getValue(), 
								steps,
								reportSortLB.getSelectedItemText(), 
								reportViewLB.getSelectedItemText(), 
								false); //isLiveUpdate
					}
					else
					{
						Window.alert("Invalid input for number of steps");
						Utility.hideTimer();
					}
				}
				else
				{
					ChartCreationHandler.acceptParam(controllerSensorListBox.getSelectedItemText(), 
							ChartUtilities.stringToStartDate(getSDate()), 
							ChartUtilities.stringToEndDate(getEDate()), 
							predictionBox.getValue(), 
							0,
							reportSortLB.getSelectedItemText(), 
							reportViewLB.getSelectedItemText(), 
							false); //isLiveUpdate
				}
		}
	}
	
	private String getSDate(){
		return (formatHolder.getFormat().format(sDateBox, sDateBox.getValue())+"");
	}
	
	private String getEDate(){
		return (formatHolder.getFormat().format(eDateBox, eDateBox.getValue())+"");
	}
	
	@SuppressWarnings("deprecation")
	public void setWidgetContent(){
		predictionBox.setValue(false);
		
		Date today = new Date();
		Date startOfYear = new Date();
		startOfYear.setDate(1);
		startOfYear.setMonth(0);
		
		formatHolder.setFormat(new DateBox.DefaultFormat(ChartUtilities.requestFormat));

		sDateBox.setFormat(new DateBox.DefaultFormat(ChartUtilities.calendarFormat));
		sDateBox.getDatePicker().setYearArrowsVisible(true);
		sDateBox.setValue(startOfYear);
	    
		eDateBox.setFormat(new DateBox.DefaultFormat(ChartUtilities.calendarFormat));
		eDateBox.getDatePicker().setYearArrowsVisible(true);
		eDateBox.setValue(today);
		
		siteListBox.clear();
		siteControllerListBox.clear();
		
		for(String siteName : Data.siteList.keySet())
		{
			siteListBox.addItem(siteName);
		}

		reportViewLB.clear();
		reportViewLB.addItem("Daily");
		reportViewLB.addItem("Monthly");
		reportViewLB.addItem("Yearly");
		
		reportSortLB.clear();
		reportSortLB.addItem("Average");
		reportSortLB.addItem("Sum");
		reportSortLB.addItem("Cumulative Sum");
		
		showControllers(siteControllerListBox, 0);
		showSensors(controllerSensorListBox, 0);
	}
	
	private void showControllers(ListBox listBox, int category) {
		showControllers(listBox, siteListBox.getSelectedItemText());
	  }
	
	private void showControllers(ListBox listBox, String category) {
		siteControllerListBox.clear();
		try{
			for(String controllerName : Data.siteControllerList.get(category))
			{
				siteControllerListBox.addItem(controllerName);
			}
		}catch(Exception e){
			siteControllerListBox.addItem("None");
		}
	}
	
	private void showSensors(ListBox listBox, int category) {
		showSensors(listBox, siteControllerListBox.getSelectedItemText());
	  }
	
	private void showSensors(ListBox listBox, String category) {
		controllerSensorListBox.clear();
		try{
			for(String sensorName : Data.controllerSensorList.get(category))
			{
				controllerSensorListBox.addItem(sensorName);
			}
		}catch(Exception e){
			controllerSensorListBox.addItem("None");
		}
	}
	
	public ArrayList<String> getSelectedItems() {
		ArrayList<String> selectedItems = new ArrayList<String>();
	    for (int i = 0; i < siteControllerListBox.getItemCount(); i++) {
	        if (siteControllerListBox.isItemSelected(i)) {
	            selectedItems.add(siteControllerListBox.getItemText(i));
	        }
	    }
	    return selectedItems;
	}
}
