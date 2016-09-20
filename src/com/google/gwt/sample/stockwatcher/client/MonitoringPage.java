package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.i18n.client.*;

import java.time.LocalDateTime;
import java.util.Date;

import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.labels.*;
import org.moxieapps.gwt.highcharts.client.plotOptions.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MonitoringPage  {
	
	VerticalPanel mainPanel = new VerticalPanel();
	HorizontalPanel chartPanel = new HorizontalPanel();
	HorizontalPanel buttonPanel = new HorizontalPanel();
	FlexTable table = new FlexTable();
	StockChart chart = new StockChart();
	Button backButton = new Button("Back");
	Button addPrediction = new Button("Add Prediction");
	Button viewLiveChart = new Button("Sample Live Chart");
	Button viewStaticChart = new Button("Sample Static Chart");
	
	public MonitoringPage(){
		setHandlers();
		
		buttonPanel.setSpacing(10);
		buttonPanel.add(viewLiveChart);
		buttonPanel.add(viewStaticChart);
		buttonPanel.add(addPrediction);
		buttonPanel.add(backButton);
		
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(buttonPanel);
		mainPanel.add(chartPanel);
		}

	public static VerticalPanel start(){
		MonitoringPage temp = new MonitoringPage();
		return temp.mainPanel;
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
				mainPanel.add(ChartUtilities.addTimer());
				chartPanel.clear();
				chartPanel.add(ChartUtilities.realTimeUpdatesChart());
				};
			});
		viewStaticChart.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				mainPanel.add(ChartUtilities.addTimer());
				//Adds chart to chart panel in base page
				ChartUtilities.getData("testTemp",ChartUtilities.stringToDate("01", "01","2016"),new java.sql.Date(System.currentTimeMillis()));
				};
			});
	}
}
