package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import org.moxieapps.gwt.highcharts.client.*;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class MonitoringPage extends Composite {

	ContentPanel contentPanel = new ContentPanel();
	VerticalPanel parameterPanel = new VerticalPanel();
	
	public static HorizontalPanel chartPanel = new HorizontalPanel();
	HorizontalPanel buttonPanel = new HorizontalPanel();
	HorizontalPanel selectionPanel = new HorizontalPanel();
	
	ListBox siteListBox = new ListBox();
	ListBox siteControllerListBox = new ListBox();
	ListBox controllerSensorListBox = new ListBox();
	
	FlexTable table = new FlexTable();
	StockChart chart = new StockChart();
	
	Button backButton = new Button("Back");
	Button goButton = new Button("Go");
	
    DateBox sDateBox = new DateBox();
    DateBox eDateBox = new DateBox();
    DateBox formatHolder = new DateBox();
    
    String sDay;
    String sMonth;
    String sYear;
    String eDay;
    String eMonth;
    String eYear;
    
	public MonitoringPage(){
		Header.setHeaderTitle("Main Menu > Monitoring > Live Update Readings");
		setHandlers();
		setWidgetContent();
		
		VerticalPanel selectionPanel = new VerticalPanel();
		selectionPanel.setStyleName("mainStyle");
		selectionPanel.setSpacing(10);
		selectionPanel.add(new HTML("<h2>Selection Menu</h2></br>"));
		selectionPanel.add(new HTML("Please select site:"));
		selectionPanel.add(siteListBox);
		selectionPanel.add(new HTML("Please select controller:"));
		selectionPanel.add(siteControllerListBox);
		selectionPanel.add(new HTML("Please select sensor:"));
		selectionPanel.add(controllerSensorListBox);
		selectionPanel.add(goButton);
		
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
//		contentPanel.addRight(filterPanel);
		
		initWidget(contentPanel);
		}
	
	private Boolean validInputFields(){
		if(controllerSensorListBox.getItemCount()==0)
		{
			Window.alert("No sensor is selected");
			return false;
		}
		return true;
	}
	
	public void setHandlers(){
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
				sendDataToServer();
			};
		});
	}
	
	private void sendDataToServer(){
		if(validInputFields())
		{
		chartPanel.clear();
		chartPanel.add(Utility.addTimer());
		ChartUtilities.getData(controllerSensorListBox.getSelectedItemText(),ChartUtilities.stringToStartDate(getSDate()),ChartUtilities.stringToEndDate(getEDate()),false,true,1);
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
		
		Date today = new Date();
		Date startOfYear = new Date();
		startOfYear.setDate(1);
		startOfYear.setMonth(0);
		
		formatHolder.setFormat(new DateBox.DefaultFormat(ChartUtilities.requestFormat));

		sDateBox.setFormat(new DateBox.DefaultFormat(ChartUtilities.calendarFormat));
		sDateBox.getDatePicker().setYearArrowsVisible(true);
		sDateBox.setValue(today);
	    
		eDateBox.setFormat(new DateBox.DefaultFormat(ChartUtilities.calendarFormat));
		eDateBox.getDatePicker().setYearArrowsVisible(true);
		eDateBox.setValue(today);
		
		siteListBox.clear();
		siteControllerListBox.clear();
		
		for(String siteName : Data.siteList.keySet())
		{
			siteListBox.addItem(siteName);
		}
		
		showControllers(siteControllerListBox, 0);
		showSensors(controllerSensorListBox, 0);
	}
	
	private void showControllers(ListBox listBox, int category) {
		showControllers(listBox, siteListBox.getSelectedItemText());
	  }
	
	private void showControllers(ListBox listBox, String category) {
		siteControllerListBox.clear();
		for(String controllerName : Data.siteControllerList.get(category))
		{
		    	siteControllerListBox.addItem(controllerName);
		}
	}
	
	private void showSensors(ListBox listBox, int category) {
		showSensors(listBox, siteControllerListBox.getSelectedItemText());
	  }
	
	private void showSensors(ListBox listBox, String category) {
		controllerSensorListBox.clear();
	    for(String sensorName : Data.controllerSensorList.get(category))
		{
	    	controllerSensorListBox.addItem(sensorName);
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
