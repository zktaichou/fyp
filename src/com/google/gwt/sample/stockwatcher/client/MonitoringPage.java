package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import org.moxieapps.gwt.highcharts.client.*;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MonitoringPage extends Composite {

	ContentPanel contentPanel = new ContentPanel();
	VerticalPanel mainPanel = new VerticalPanel();
	VerticalPanel parameterPanel = new VerticalPanel();
	
	HorizontalPanel chartPanel = new HorizontalPanel();
	HorizontalPanel buttonPanel = new HorizontalPanel();
	HorizontalPanel selectionPanel = new HorizontalPanel();

	ListBox siteListBox = new ListBox();
	ListBox siteControllerListBox = new ListBox();
	ListBox controllerSensorListBox = new ListBox();
	
	FlexTable table = new FlexTable();
	StockChart chart = new StockChart();
	Button backButton = new Button("Back");
	Button goButton = new Button("Go");
	Button addPrediction = new Button("Add Prediction");
	Button viewLiveChart = new Button("Sample Live Chart");
	Button viewStaticChart = new Button("View sensor data");
	
	public MonitoringPage(){
		
		setHandlers();
		setWidgetContent();
		
		VerticalPanel selectionPanel = new VerticalPanel();

		selectionPanel.add(new HTML("<h2>Selection Menu</h2></br>"));
		selectionPanel.add(new HTML("Please select site(s):"));
		selectionPanel.add(siteListBox);
		selectionPanel.add(new HTML("Please select controller(s):"));
		selectionPanel.add(siteControllerListBox);
		selectionPanel.add(new HTML("Please select sensor(s):"));
		selectionPanel.add(controllerSensorListBox);
		selectionPanel.add(goButton);
		selectionPanel.setSpacing(10);
		
		parameterPanel.clear();
		parameterPanel.add(selectionPanel);
		parameterPanel.setHeight("100%");
		parameterPanel.setStyleName("parameterPanel");
		
		contentPanel.clear();
		contentPanel.addLeft(parameterPanel);
		contentPanel.add(chartPanel);
		
		initWidget(contentPanel);
		}
	
	public void setHandlers(){
		backButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterMainMenuPage();
				};
			});
		addPrediction.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Window.alert("Prediction code work in progress");
				};
			});
		viewLiveChart.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				chartPanel.add(Utility.addTimer());
				chartPanel.clear();
				chartPanel.add(ChartUtilities.realTimeUpdatesChart());
				};
			});
		viewStaticChart.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				chartPanel.add(Utility.addTimer());
				//Adds chart to chart panel in base page
				ChartUtilities.getData("testTemp",ChartUtilities.stringToDate("01", "01","2016"),new java.sql.Date(System.currentTimeMillis()));
				};
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
				chartPanel.add(Utility.addTimer());
				ChartUtilities.getData(controllerSensorListBox.getSelectedItemText(),ChartUtilities.stringToDate("01", "01","2016"),new java.sql.Date(System.currentTimeMillis()));
				};
			});
	}
	
	public void setWidgetContent(){
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
