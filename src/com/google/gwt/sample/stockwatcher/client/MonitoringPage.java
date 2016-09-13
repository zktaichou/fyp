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
	FlexTable table = new FlexTable();
	StockChart chart = new StockChart();
	Button backButton = new Button("Back");
	Button addPrediction = new Button("Add Prediction");
	Button viewLiveChart = new Button("Sample Live Chart");
	Button viewStaticChart = new Button("Sample Static Chart");
	
	public MonitoringPage(){
		setHandlers();
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(viewLiveChart);
		mainPanel.add(viewStaticChart);
		mainPanel.add(addPrediction);
		mainPanel.add(backButton);
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
				ChartUtilities.getData("testTemp",null,null);
				};
			});
	}
	
	/////////////////////////////////////////////////////////////////////
	
	
	public void getChart(){
        chart.setType(Series.Type.LINE)  
        .setChartTitleText("Sample household electric consumption")  
        .setBarPlotOptions(new BarPlotOptions()  
            .setDataLabels(new DataLabels()  
                .setEnabled(true)  
            )  
        )  
        .setLegend(new Legend()  
            .setEnabled(false)  
        )  
        .setCredits(new Credits()  
            .setEnabled(false)  
        )  
        .setToolTip(new ToolTip()  
            .setFormatter(new ToolTipFormatter() {  
                public String format(ToolTipData toolTipData) {  
                    return "<b>" + toolTipData.getSeriesName() + "</b><br/>" +  
                        DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss")  
                            .format(new Date(toolTipData.getXAsLong())) + "<br/>" +  
                        NumberFormat.getFormat("0.00").format(toolTipData.getYAsDouble());  
                }  
            })  
        );  

    chart.getXAxis()  
        .setType(Axis.Type.DATE_TIME)  
        .setTickPixelInterval(150);  

    chart.getYAxis()  
        .setAxisTitleText("Value")  
        .setPlotLines(  
            chart.getYAxis().createPlotLine()  
                .setValue(0)  
                .setWidth(1)  
                .setColor("#808080")  
        );  

	RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "sample.txt" );

    try {
        requestBuilder.sendRequest( null, new RequestCallback(){
            public void onError(Request request, Throwable exception) {
            	Window.alert("File not found");
                GWT.log( "failed file reading", exception );
            }

            public void onResponseReceived(Request request, Response response) {
            	chartPanel.setWidth(Integer.toString(Window.getClientWidth()-15)+"px");
            	chartPanel.setHeight(Integer.toString(Window.getClientHeight()-100)+"px");
            	chartPanel.add(createChart(response.getText()));
            	while(true)
            	{
            		if(chart.isRendered())
            		{
                	ChartUtilities.hideTimer();
                	break;
            		}
            	}
            }} );
    } catch (RequestException e) {
    	Window.alert("Request exception");
        GWT.log( "failed file reading", e );
    }
}

public StockChart createChart(String response){
try{
	String[] currentLine = response.split("\\r\\n|\\n|\\r");
	int row=0;
	while (currentLine[row+1] != null) {
		String[] splitLines = currentLine[row+1].split(";");
			for(int column=0; column<splitLines.length; column++)
			{
				table.setText(row, column, splitLines[column]);
			}
		row++;
    }

	}catch(Exception e){e.printStackTrace();}

Series series = chart.createSeries(); 
series.setOption("turboThreshold", 0);

for(int row=0; row<table.getRowCount();row++)
{ 
	String[] dateContent = table.getText(row, 0).split("/");
	String d=dateContent[0];
	String m=dateContent[1];
	String y=dateContent[2];
	
	String[] timeContent = table.getText(row, 1).split(":");
	String hour=timeContent[0];
	String minute=timeContent[1];
	String seconds=timeContent[2];
	
    Number date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").parse(y + "-" + m + "-" + d + " "+hour+":"+minute+":"+seconds).getTime();
    Number value = NumberFormat.getDecimalFormat().parse(table.getText(row, 5));

    //Number date = ChartUtilities.getTime(object.getObjectArray(0));
    //Number date = ChartUtilities.getTime("2016-09-07 12:12:12");
    //Number value = NumberFormat.getDecimalFormat().parse(object.getObjectArray(1));
    //Number value = NumberFormat.getDecimalFormat().parse(15);
    
    series.addPoint(date, value);
}

chart.addSeries(series.setName("Voltage"));
chart.setZoomType(BaseChart.ZoomType.X);
chart.setSize("100%", "100%");
chart.reflow();
return chart;
}
	
}
