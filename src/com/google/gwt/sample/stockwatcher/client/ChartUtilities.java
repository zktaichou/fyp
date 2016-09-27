package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.i18n.client.*;

import java.util.Date;
import java.util.Random;

import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.events.PointClickEvent;
import org.moxieapps.gwt.highcharts.client.labels.*;
import org.moxieapps.gwt.highcharts.client.plotOptions.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.*;

public class ChartUtilities{

PopupPanel noDataAvailablePopup = new PopupPanel();
PopupPanel filterPopup = new PopupPanel();

ListBox startMonthListBox;
ListBox startYearListBox;
ListBox endMonthListBox;
ListBox endYearListBox;

ListBox reportTypeListBox = new ListBox();
ListBox reportDataTypeListBox = new ListBox();
ListBox reportMetricTypeListBox = new ListBox();
ListBox reportSelectionListBox = new ListBox();
ListBox groupListBox = new ListBox();

RadioButton monthlyReportRadioButton = new RadioButton("Monthly");
RadioButton yearlyReportRadioButton = new RadioButton("Yearly");
RadioButton quarterlyReportRadioButton = new RadioButton("Quarterly");

String[][]phpData;
String dateFormat;
String startMonth;
String startYear;
String endMonth;
String endYear;
String startDay;
String endDay;
String getTimeFormat;


int latestRequestID, showDetailsLatestRequestID;

HorizontalPanel titlePanel = new HorizontalPanel();
HorizontalPanel loadingPopupPanel = new HorizontalPanel();
static final HorizontalPanel chartPanel = new HorizontalPanel();

static final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");

static long getTime(String date) {
    return dateTimeFormat.parse(date).getTime();  
}  



	//Object returned here --------------vvvvv
	public static void getData(String sn, final java.sql.Date sd, java.sql.Date ed){
		Data.latestRequestID++;
		final int currRequestID=Data.latestRequestID;
		chartPanel.clear();
		if(!chartPanel.isAttached())
		BasePage.contentPanel.add(chartPanel);
		
		Utility.newRequestObj().greetServer(sn,sd,ed, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(String[][] result) {
				if(currRequestID==Data.latestRequestID)
				{
				Number [][] data = formatData(result);
				chartPanel.add(createChart(data,"Chart with live temperature updates"));
				//BasePage.panel.add(createFlexTable(result));
				}
			}
		});
	}
	
	public static void getAppendData(final Series s, String sn, java.sql.Date sd, java.sql.Date ed){
		
		Utility.newRequestObj().greetServer(sn,sd,ed, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data append fail");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(String[][] result) {
				
				Number [][] data = formatData(result);
				for(int i=0;i<data.length;i++)
				{
					s.addPoint(data[i][0],data[i][1], true, true, true);
				}
			}
		});
	}
	
	public static Number[][] formatData(String [][] input){
		Number [][] data = new Number[input.length][2];
		
		for(int i=input.length-1;i>=0;i--)
		{
			data[input.length-i-1][0]=DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").parse(input[i][0]).getTime();
			data[input.length-i-1][1]=convertToNumber(input[i][1]);
		}
		return data;
	}
	
	public static StockChart createChart(Number[][] data, String title){
		
		Utility.hideTimer();
		
		final StockChart chart = new StockChart();
		chart
		.setType(Series.Type.SPLINE)  
        .setMarginRight(10)  
        .setBarPlotOptions(new BarPlotOptions()  
                .setDataLabels(new DataLabels()  
                    .setEnabled(true)  
                )  
            )
        .setChartTitleText(title)
	    .setLegend(new Legend().setEnabled(false))  
	    .setCredits(new Credits().setEnabled(false))
	    .setSplinePlotOptions(new SplinePlotOptions()  
                .setMarker(new Marker()  
                    .setEnabled(true).setRadius(3)  
                )  
        )
	    ;
		
		chart.getXAxis().setDateTimeLabelFormats(
				new DateTimeLabelFormats()
				    .setSecond("%l:%M:%S %p"));
		
		chart.getYAxis()  
        .setAxisTitleText("Value")  
        .setPlotLines(  
            chart.getYAxis().createPlotLine()  
                .setValue(0)  
                .setWidth(1)  
                .setColor("#808080")  
        );  
		
		final Series series = chart.createSeries();  
	    chart.addSeries(series.setName("data")); 
	    
	    for(int i=0;i<data.length;i++)
		{
	    	series.addPoint(data[i][0],data[i][1]);
	    
		}
	    
	    final Timer tempTimer = new Timer() {
	    	java.sql.Date lastRequestTime = new java.sql.Date(System.currentTimeMillis());
            @Override  
            public void run() {
            	if (chart.isAttached()) {
            		if (chart.isRendered()) {
	                	long currTime=System.currentTimeMillis();
	                	getAppendData(series,"testTemp",lastRequestTime,new java.sql.Date(currTime));
	                    lastRequestTime=new java.sql.Date(currTime+10);
            		}
                    schedule(2000);
            	}

            }  
        };
        tempTimer.schedule(0);
        
        chart.setSize(Window.getClientWidth()*2/3, Window.getClientHeight()*2/3);
	    
		return chart;
	}
	
	public static java.sql.Date stringToDate(String day, String month, String year){
		String stringDate = year+"-"+month+"-"+day+" 00:00:00";
		return new java.sql.Date(dateTimeFormat.parse(stringDate).getTime());
	}
	
    public static Chart realTimeUpdatesChart() {
	
    final Chart chart = new Chart()  
        .setType(Series.Type.SPLINE)  
        .setMarginRight(10)  
        .setChartTitleText("Live random data (for now...)")  
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

    final Series series = chart.createSeries();  
    chart.addSeries(series  
        .setName("Random data")  
    );  

    // Generate an array of random data  
    long time = new Date().getTime();  
    for(int i = -19; i <= 0; i++) {  
        series.addPoint(time + i * 1000, com.google.gwt.user.client.Random.nextDouble());  
    }  

    Timer tempTimer = new Timer() {  
        @Override  
        public void run() {  
            series.addPoint(  
                new Date().getTime(),  
                com.google.gwt.user.client.Random.nextDouble(),  
                true, true, true  
            );  
        }  
    };  
    tempTimer.scheduleRepeating(1000);  

    return chart;  
}

//	    public void getTrainingChart(){
//	        chart.setType(Series.Type.LINE)  
//	        .setChartTitleText("Sample household electric consumption")  
//	        .setBarPlotOptions(new BarPlotOptions()  
//	            .setDataLabels(new DataLabels()  
//	                .setEnabled(true)  
//	            )  
//	        )   
//	        .setLegend(new Legend()  
//	            .setEnabled(false)  
//	        )  
//	        .setCredits(new Credits()  
//	            .setEnabled(false)  
//	        )  
//	        .setToolTip(new ToolTip()  
//	            .setFormatter(new ToolTipFormatter() {  
//	                public String format(ToolTipData toolTipData) {  
//	                    return "<b>" + toolTipData.getSeriesName() + "</b><br/>" +  
//	                        DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss")  
//	                            .format(new Date(toolTipData.getXAsLong())) + "<br/>" +  
//	                        NumberFormat.getFormat("0.00").format(toolTipData.getYAsDouble());  
//	                }  
//	            })  
//	        );  
//	
//	    chart.getXAxis()  
//	        .setType(Axis.Type.DATE_TIME)  
//	        .setTickPixelInterval(150);  
//	
//	    chart.getYAxis()  
//	        .setAxisTitleText("Value")  
//	        .setPlotLines(  
//	            chart.getYAxis().createPlotLine()  
//	                .setValue(0)  
//	                .setWidth(1)  
//	                .setColor("#808080")  
//	        );  
//	
//		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "sample.txt" );
//	
//	    try {
//	        requestBuilder.sendRequest( null, new RequestCallback(){
//	            public void onError(Request request, Throwable exception) {
//	            	Window.alert("File not found");
//	                GWT.log( "failed file reading", exception );
//	            }
//	
//	            public void onResponseReceived(Request request, Response response) {
//	            	chartPanel.setWidth(Integer.toString(Window.getClientWidth()-15)+"px");
//	            	chartPanel.setHeight(Integer.toString(Window.getClientHeight()-100)+"px");
//	            	chartPanel.add(createChart(response.getText()));
//	            	while(true)
//	            	{
//	            		if(chart.isRendered())
//	            		{
//	                	ChartUtilities.hideTimer();
//	                	break;
//	            		}
//	            	}
//	            }} );
//	    } catch (RequestException e) {
//	    	Window.alert("Request exception");
//	        GWT.log( "failed file reading", e );
//	    }
//	}

//	public StockChart createChart(String response){
//	try{
//		String[] currentLine = response.split("\\r\\n|\\n|\\r");
//		int row=0;
//		while (currentLine[row+1] != null) {
//			String[] splitLines = currentLine[row+1].split(";");
//				for(int column=0; column<splitLines.length; column++)
//				{
//					table.setText(row, column, splitLines[column]);
//				}
//			row++;
//	    }
//	
//		}catch(Exception e){e.printStackTrace();}
//	
//	Series series = chart.createSeries(); 
//	series.setOption("turboThreshold", 0);
//	
//	for(int row=0; row<table.getRowCount();row++)
//	{ 
//		String[] dateContent = table.getText(row, 0).split("/");
//		String d=dateContent[0];
//		String m=dateContent[1];
//		String y=dateContent[2];
//		
//		String[] timeContent = table.getText(row, 1).split(":");
//		String hour=timeContent[0];
//		String minute=timeContent[1];
//		String seconds=timeContent[2];
//		
//	    Number date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").parse(y + "-" + m + "-" + d + " "+hour+":"+minute+":"+seconds).getTime();
//	    Number value = NumberFormat.getDecimalFormat().parse(table.getText(row, 5));
//	
//	    //Number date = ChartUtilities.getTime(object.getObjectArray(0));
//	    //Number date = ChartUtilities.getTime("2016-09-07 12:12:12");
//	    //Number value = NumberFormat.getDecimalFormat().parse(object.getObjectArray(1));
//	    //Number value = NumberFormat.getDecimalFormat().parse(15);
//	    
//	    series.addPoint(date, value);
//	}
//	
//	chart.addSeries(series.setName("Voltage"));
//	chart.setZoomType(BaseChart.ZoomType.X);
//	chart.setSize("100%", "100%");
//	chart.reflow();
//	return chart;
//	}
    
//****************************EVERYTHING BELOW IS DONE DURING INTERN****************************

//Methods for automating the setting of a chart's parameters
public Chart setChartParam(String chartType, String chartTitle, String xAxisLabel, String yAxisLabel){
	
	Chart chart = new Chart().setCredits(new Credits().setEnabled(false));

	switch(chartType)
	{
	case "column" : chart.setType(Series.Type.COLUMN)
						 .setColumnPlotOptions(new ColumnPlotOptions()
								 .setCursor(PlotOptions.Cursor.POINTER));
		break;
	case "line" : chart.setType(Series.Type.LINE);
		break;
	case "pie" : chart.setType(Series.Type.PIE)
					  .setPiePlotOptions(new PiePlotOptions()  
					  	.setShowInLegend(true)  
					  	.setCursor(PlotOptions.Cursor.POINTER)
				    );
		break;
	default: 
		break;
	}
		
	chart.setChartTitleText(chartTitle);
	chart.getXAxis().setAxisTitleText(xAxisLabel);
	chart.getYAxis().setAxisTitleText(yAxisLabel);
	
	//Check if there's only one point for every series. If yes, create a category and put all series in to avoid highcharts from displaying
	//other months/years although they contain no points (weird stuff)
	if (getReportFormat().equals("none"))
	{
		chart.getXAxis().setCategories("Total To Date");
	}
//	else if(yearlyReportRadioButton.getValue() && sameYearIsSelected())
//	{
//		chart.getXAxis().setCategories(startYearListBox.getSelectedItemText());
//	}
//	else if(monthlyReportRadioButton.getValue() && sameMonthIsSelected() && sameYearIsSelected() )
//	{
//		chart.getXAxis().setCategories(startMonthListBox.getSelectedItemText());
//	}
	
	//Else, set the x-axis time format in milliseconds
	else
	{
	chart.getXAxis().setType(Axis.Type.DATE_TIME)
						  .setMaxZoom(14 * 24 * 3600000);
	}
	
	return chart;
}

public Chart setChartCustomXAxisLabelling(Chart chart){
//	if(sameYearIsSelected())
//	{
//		if(monthlyReportRadioButton.getValue())
//		{
//			//Set x-axis labelling to display only month
//			chart.getXAxis().setDateTimeLabelFormats(
//			new DateTimeLabelFormats()
//				.setMonth("%B"));
//		}
//	}
//	else
	{
		//Display both month and year on x-axis
		chart.getXAxis().setDateTimeLabelFormats(
			new DateTimeLabelFormats()
			    .setMonth("%B %Y"));
	}
	return chart;
}

//Methods for automating adding points to a series
public Series newSeries(Chart chart, String seriesName, String data[][], int columnIndexInDatabaseQuery){
	
	int m=1, y=2012;

	Number value;
	
	Series series = chart.createSeries().setName(seriesName);

	//logic for plotting points and adding lines for the chart
	for (int i = 0;i<data.length;i++)
	{		
		
		if (!getReportFormat().equals("none"))
		{
			//check if only year field is present
			if(data[i][0].length()==4) 
			{
				y=Integer.parseInt(data[i][0]); 
			}
			
			//otherwise, month field is also present
			else 
			{
				m=Integer.parseInt(data[i][0].substring(0,2));
				y=Integer.parseInt(data[i][0].substring(2));
			}
		}
			
		//This different way of adding the data is for setting the name for the points that will be used for drilldown later
		//The names are parameters that will be accepted by SQL's stored procedures
//		if( onlyOneYearIsSelectedOnYearlyReport() || getReportFormat().equals("none") )
		{
			value = convertToNumber(data[i][columnIndexInDatabaseQuery]);
			
			Point p = new Point();
			p.update(value);
			
			if (getReportFormat().equals("none"))
			{
				p.setName("none");
			}
			else
			{
//				p.setName(startYearListBox.getSelectedItemText());
			}
				
			series.addPoint(p);
			series.setPlotOptions(new ColumnPlotOptions().setPointWidth(70).setPointPadding(5));
//			break;
		}
			
//			if(monthlyReportRadioButton.getValue())
			{
				if(m==1)
				{
					y-=1;
					m=12;
				}
				else
				{
					m-=1;
				}
			}
			
				Number date = DateTimeFormat.getFormat("yyyy-MM-dd").parse(y + "-" + (m + 1) + "-" + 1).getTime();
				value = convertToNumber(data[i][columnIndexInDatabaseQuery]);
				
				//add the date and value to the series' point
				series.addPoint(date, value); 
	}
	
	return series;
}

//Methods to create a flex table from an input 2D String array
private static FlexTable createFlexTable(String data[][])
{

	FlexTable breakdownTable = new FlexTable();
	breakdownTable.setBorderWidth(1);
	breakdownTable.setCellPadding(5);
	
	for(int i=0;i<data.length;i++)
	{
		for(int j=0;j<data[i].length;j++)
		{
			breakdownTable.setText(i, j, data[i][j]);
		}
	}
	
	return breakdownTable;
}

//Methods to reset/initialise widget values on an event (such as page load or user selection on UI)

//private void updateDateListBoxesSelection(int value){
//	endDay=getCurrentDay();
//	
//	date = new Date();
//	CalendarUtil.addDaysToDate(date, value);
//	int newCurrentMonthInt = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format(date));
//	String newCurrentYearString = DateTimeFormat.getFormat( "yyyy" ).format(date);
//	startDay = DateTimeFormat.getFormat( "d" ).format(date);
//	
//	startMonthListBox.setSelectedIndex(newCurrentMonthInt-1);
//	
//	for(int i=0;i<startYearListBox.getItemCount();i++)
//	{
//		//set new selected year in listbox
//		if(startYearListBox.getValue(i).equals(newCurrentYearString))
//		{
//			startYearListBox.setSelectedIndex(i);
//			
//			//reupdate the month list box if new current year == current year
//			
//			int startMonthSelectedIndex = startMonthListBox.getSelectedIndex();
//			startMonthListBox.clear();
//			for(int j=0;j<months.length;j++)
//			{
//				startMonthListBox.addItem(months[j]);
//				startMonthListBox.setValue(j, monthListBoxValue[j]);
//			}
//			startMonthListBox.setSelectedIndex(startMonthSelectedIndex);
//			
//			if(startYearListBox.getValue(i).equals(getCurrentYearString()))
//			{
//				startMonthSelectedIndex = startMonthListBox.getSelectedIndex();
//				startMonthListBox.clear();
//				if (startYearListBox.getSelectedIndex()==startYearListBox.getItemCount()-1)
//				{
//					for(int j=0;j<getCurrentMonthInt();j++)
//					{
//						startMonthListBox.addItem(months[j]);
//						startMonthListBox.setValue(j, monthListBoxValue[j]);
//					}
//					startMonthListBox.setSelectedIndex(startMonthSelectedIndex);
//					if(startMonthListBox.getItemCount()<=startMonthSelectedIndex)
//					{
//						startMonthListBox.setSelectedIndex(startMonthListBox.getItemCount()-1);
//					}
//				}
//			}
//				
//			break;
//		}
//	}
//	
//	endMonthListBox.setSelectedIndex(endMonthListBox.getItemCount()-1);
//	endYearListBox.setSelectedIndex(endYearListBox.getItemCount()-1);
//}
//
//private void updateShowDetailsDateListBoxesSelection(int value){
//	endDay=getCurrentDay();
//	
//	date = new Date();
//	CalendarUtil.addDaysToDate(date, value);
//	int newCurrentMonthInt = Integer.parseInt(DateTimeFormat.getFormat( "M" ).format(date));
//	String newCurrentYearString = DateTimeFormat.getFormat( "yyyy" ).format(date);
//	startDay = DateTimeFormat.getFormat( "d" ).format(date);
//	
//	subStartMonthListBox.setSelectedIndex(newCurrentMonthInt-1);
//	
//	for(int i=0;i<subStartYearListBox.getItemCount();i++)
//	{
//		//set new selected year in listbox
//		if(subStartYearListBox.getValue(i).equals(newCurrentYearString))
//		{
//			subStartYearListBox.setSelectedIndex(i);
//			
//			//reupdate the month list box if new current year == current year
//			
//			int startMonthSelectedIndex = subStartMonthListBox.getSelectedIndex();
//			subStartMonthListBox.clear();
//			for(int j=0;j<months.length;j++)
//			{
//				subStartMonthListBox.addItem(months[j]);
//				subStartMonthListBox.setValue(j, monthListBoxValue[j]);
//			}
//			subStartMonthListBox.setSelectedIndex(startMonthSelectedIndex);
//			
//			if(subStartYearListBox.getValue(i).equals(getCurrentYearString()))
//			{
//				startMonthSelectedIndex = subStartMonthListBox.getSelectedIndex();
//				subStartMonthListBox.clear();
//				if (subStartYearListBox.getSelectedIndex()==subStartYearListBox.getItemCount()-1)
//				{
//					for(int j=0;j<getCurrentMonthInt();j++)
//					{
//						subStartMonthListBox.addItem(months[j]);
//						subStartMonthListBox.setValue(j, monthListBoxValue[j]);
//					}
//					subStartMonthListBox.setSelectedIndex(startMonthSelectedIndex);
//					if(subStartMonthListBox.getItemCount()<=startMonthSelectedIndex)
//					{
//						subStartMonthListBox.setSelectedIndex(subStartMonthListBox.getItemCount()-1);
//					}
//				}
//			}
//				
//			break;
//		}
//	}
//	
//	subEndMonthListBox.setSelectedIndex(subEndMonthListBox.getItemCount()-1);
//	subEndYearListBox.setSelectedIndex(subEndYearListBox.getItemCount()-1);
//}

//Methods to control display of popups
private void hideLoading(){
	loadingPopupPanel.setVisible(false);
}

private void showLoading(){
	loadingPopupPanel.setVisible(true);
}

//The main methods where a request for data is sent, and the chart is created from the returned data
private void getPHPData(String url, final String yourChart, final String chartLevel)
{
	hideChartPanels();
	showLoading();
	selectChartTitle(yourChart);
	
	if(chartLevel.equals("main"))
	{
		clearChartPanels();
	}
	
	Random r = new Random();
	
	final int requestID = r.nextInt(1000000);
	latestRequestID = requestID;
	
//	GIORequest gioRequest = new GIORequest(new GIORequestHandler() {
//		@Override
//		public void onResponseReceived(GIORequestEvent _event) {
//			if (_event.getResponseData().isEmpty())
//			{
//			hideLoading();
//			showNoDataPopup();
//			Window.alert("No data is available for selected items");
//			}
//			else
//			{
//				if(latestRequestID == requestID)
//				{
//					createChartOfChoice(chartLevel, yourChart,_event.getResponseData());
//				}
//			}
//			
//		}
//
//		@Override
//		public void onRequestError(GIORequestEvent _event) {
//			Window.alert("Connection error");
//		}
//	});
//	
//	gioRequest.setUrl(url);
//	gioRequest.initiateRequest();
	
}

private void createChartOfChoice(String chartLevel, String yourChart, String data){
	
//	phpData = decode_from_php_num(data);
//	
//	Chart chart = new Chart();
//	FlexTable extraTable = new FlexTable();
//	
//	setChartCustomXAxisLabelling(chart);
//	
//	if(yourChart.equals("test_executed"))
//	{
//		if (timeTakenIsSelected())
//		{
//			chart = createColumnChartTestExecuted("", phpData, legendHelpText, "Total Elapsed Time ("+reportMetricTypeListBox.getSelectedItemText()+")");
//			populateExtraCharts("Total Elapsed Time ("+reportMetricTypeListBox.getSelectedItemText()+")");
//		}
//		else
//		{
//			chart = createColumnChartTestExecuted("", phpData, legendHelpText, "Number of Test Executed");
//			populateExtraCharts("Number of Test Executed");
//		}
//	}
//	else if(yourChart.equals("test_executed_pie"))
//	{
//	chart = createComponentPieChartTestExecuted("", phpData, legendHelpText, "Number of Test Executed");
//	extraTable = createFlexTableLegacy(phpData,"Breakdown",dataHeaderName);
//	}
//	else if(yourChart.equals("test_case_trend_time"))
//	{
//	chart = createTimeChartTestCaseTrend("", phpData, legendHelpText, "Number of Test Cases created");
//	}
//	else if(yourChart.equals("test_case_trend_component"))
//	{
//	chart = createComponentChartTestCaseTrend("", phpData, legendHelpText, "Number of Test Cases created");
//	}
//	else if(yourChart.equals("test_case_trend_pie"))
//	{
//	chart = createComponentPieChartTestCaseTrend("", phpData, legendHelpText, "Number of Test Cases created");
//	extraTable = createFlexTableLegacy(phpData,"Breakdown","Test Cases Created");
//	}
//	else if(yourChart.equals("user_activity"))
//	{
//	chart = createLineChartUserActivity("", phpData, "Time Period", "Number of Users");
//	extraTable = userActivityMainTable;
//	}
//
//	setChartSize(chart, chartLevel);
//	
//	if(chartLevel.equals("drilldown"))
//	{
//		makeDrilldownChart(chart, extraTable);
//	}
//	else if(chartLevel.equals("main"))
//	{
//		chartPanel.setVisible(true);
//		chartPanel.add(chart);
//	}
	
	hideLoading();
}

//Methods that does misc stuff (like updating main title/display or hide panels) when a chart is created
private void selectChartTitle(String yourChart){
//	if(yourChart.equals("test_executed"))
//	{
//		if (getComponent.equals("hsd_exposure") || getComponent.equals("hsd_exposure_cumulative"))
//		{
//			mainChartTitle = getChartTitle("Total Defects Detected");
//			updateTitlePanel(getChartTitle("Total Defects Detected"));
//		}
//		else if (timeTakenIsSelected())
//		{
//			mainChartTitle = getChartTitle("Total Elapsed Time ("+reportMetricTypeListBox.getSelectedItemText()+")");
//			updateTitlePanel(getChartTitle("Total Elapsed Time ("+reportMetricTypeListBox.getSelectedItemText()+")"));
//		}
//		else
//		{
//			mainChartTitle = getChartTitle("Total Test Executed");
//			updateTitlePanel(getChartTitle("Total Test Executed"));
//		}
//	}
//	else if(yourChart.equals("test_case_trend_time"))
//	{
//		mainChartTitle = getChartTitle("Total Test Cases Created");
//		updateTitlePanel(mainChartTitle);
//	}
//	else if(yourChart.equals("test_case_trend_component"))
//	{
//		mainChartTitle = getChartTitle("Total Test Cases Created under "+groupListBox.getItemText(groupListBox.getSelectedIndex()));
//		updateTitlePanel(mainChartTitle);
//	}
//	else if(yourChart.equals("user_activity"))
//	{
//		mainChartTitle = getChartTitle("Number of Users");
//		updateTitlePanel(mainChartTitle);
//	}
}

private void updateTitlePanel(String title){
	titlePanel.clear();
	titlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
	titlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	titlePanel.setTitle(title);
	titlePanel.add(new HTML("<h2>"+title+"</h2>"));
} 

//private void updateShowDetailsDateSelectionPanel(){
//	subStartMonthListBox.setSelectedIndex(startMonthListBox.getSelectedIndex());
//	subStartYearListBox.setSelectedIndex(startYearListBox.getSelectedIndex());
//	subEndMonthListBox.setSelectedIndex(endMonthListBox.getSelectedIndex());
//	subEndYearListBox.setSelectedIndex(endYearListBox.getSelectedIndex());
//}

private int contentPanelHeight(){
	return Window.getClientHeight()-241;
}

private void clearWidgets(HorizontalPanel panel){
	while(panel.getWidgetCount()!=0)
	{
		panel.remove(0);
	}
	panel.setVisible(true);
}

private void clearWidgets(VerticalPanel panel){
	while(panel.getWidgetCount()!=0)
	{
		panel.remove(0);
	}
	panel.setVisible(true);
}

private void clearWidgets(FilterMenu panel){
	panel.clearWidgets();
	panel.count = 0;
}

private void clearWidgets(DialogBox panel){
	if(panel.getWidget()!=null)
	{
		panel.remove(panel.getWidget());
	}
}

private void clearAllPanels(){

//	clearWidgets(filterPopup);

	//Hide all components that are not shared between pages
//	separator.setVisible(false);
}

private void hideChartPanels(){
//	chartPanel.setVisible(false);
}

private void clearChartPanels(){
//	clearWidgets(chartPanel);

}

private void showPopup(DialogBox widget){
	widget.getElement().getStyle().setBackgroundColor("#F5FDFD");
	widget.setAutoHideEnabled(true);
	widget.setGlassEnabled(true);
	widget.center();
}

//Methods for styling
//private void setMainContentPanelStyle(){
//	mainContentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
//	mainContentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//	mainContentPanel.setSize("100%","100%");
//}

//Methods that's made just to improve readability

private static Number convertToNumber(String data){
	Number value ;
	try	{
		value = Integer.parseInt(data);
	} catch (NumberFormatException e) { 
		value = NumberFormat.getDecimalFormat().parse(data);} //assign value
	return value;
}

private int getCurrentMonthInt(){
	return Integer.parseInt(DateTimeFormat.getFormat("M").format( new Date() ));
}

private String getCurrentYearString(){
	return DateTimeFormat.getFormat("yyyy").format( new Date() );
}

private String getCurrentDay(){
	return DateTimeFormat.getFormat("d").format( new Date() );
}

private String getPointMonth(PointClickEvent clickEvent){
	
	String month = "";
	
	if(!getReportFormat().equals("none"))
	{
		if(monthlyReportRadioButton.getValue())
		{
		month = DateTimeFormat.getFormat("MM").format(new Date(clickEvent.getXAsLong()+1));
			if(month.equals("13"))
			{
				month = "01";
			}
		}
		else
		month="";
	}
	
	return month;
}

private String getPointYear(PointClickEvent clickEvent){
	
	String month = "";
	String year = "";

	if(!getReportFormat().equals("none"))
	{
		year = DateTimeFormat.getFormat("yyyy").format(new Date(clickEvent.getXAsLong()));
		month = DateTimeFormat.getFormat("MM").format(new Date(clickEvent.getXAsLong()+1));

		if(month.equals("13"))
		{
			year = Integer.toString(Integer.parseInt(year)+1);
		}
	}
	
	return year;
}

private String getChartTitle(String chartBaseTitle){
	
	String chartTitle = "";
	String chartDateRangeTitle = "";
	
	if(endYearListBox.getSelectedItemText().equals(getCurrentYearString()) && endMonthListBox.getSelectedIndex()==getCurrentMonthInt()-1)
	{
		endDay = getCurrentDay();
	}
	
	chartDateRangeTitle ="from "+startDay+" "
								+startMonthListBox.getItemText(startMonthListBox.getSelectedIndex())+" "
								+startYearListBox.getItemText(startYearListBox.getSelectedIndex())+" to "
								+endDay+" "
								+endMonthListBox.getItemText(endMonthListBox.getSelectedIndex())+" "
								+endYearListBox.getItemText(endYearListBox.getSelectedIndex());
	
	chartTitle=(chartBaseTitle+" "+chartDateRangeTitle);
	return chartTitle;
}

private String getReportFormat(){
	
	dateFormat="none";
	
	if(monthlyReportRadioButton.getValue())
	{
		dateFormat="%m%Y";
	}
	else if(yearlyReportRadioButton.getValue())
	{
		dateFormat="%Y";
	}
	
	return dateFormat;
}

//Render popup panels
private void renderLoadingPopupPanel(){
	
	loadingPopupPanel.clear();
	loadingPopupPanel.setHeight(Window.getClientHeight()-241+"px");
	loadingPopupPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	loadingPopupPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
	//loadingPopupPanel.add(loadingPopup);
	
	//hideLoading();
	
	//loadingPopup.setLoadingTitle("Please wait for a moment...");
	//loadingPopup.hide();
}

//Custom class that is written to create a filter menu
public class FilterMenu {
	
	int count = 0;
	
	VerticalPanel mainPanel = new VerticalPanel();
	
	public FilterMenu(){
		mainPanel.setSpacing(5);
	}
	
	public void addLabel(String string){
		Label label = new Label(string);
		mainPanel.add(label);
		count++;
		if(count>0)
		{
			try{
				HorizontalPanel check = (HorizontalPanel) getPanel(count-1).asWidget();
				count++;
			}catch(Exception e){
			}
		}
	}
	
	public void addLabel(String title, String string){
		Label label = new Label(string);
		label.setTitle(title);
		mainPanel.add(label);
		count++;
		if(count>0)
		{
			try{
				HorizontalPanel check = (HorizontalPanel) getPanel(count-1).asWidget();
				count++;
			}catch(Exception e){
			}
		}
	}
	
	public void addNewRow(Widget widget){
		HorizontalPanel panel = new HorizontalPanel();
		panel.getElement().getStyle().setPaddingLeft(2.5, Unit.PX);
		panel.getElement().getStyle().setPaddingRight(2.5, Unit.PX);
		panel.add(widget);
		mainPanel.add(panel);
		count++;
	}
	
	public void addItem(Widget widget){
		if(groupExist(count))
		{
			getPanel(count).add(widget);
		}
		else
		{
			HorizontalPanel panel = new HorizontalPanel();
			panel.getElement().getStyle().setPaddingLeft(2.5, Unit.PX);
			panel.getElement().getStyle().setPaddingRight(2.5, Unit.PX);
			panel.add(widget);
			mainPanel.add(panel);
		}
	}
	
	public void addItem(String title, Widget widget){
		if(groupExist(title))
		{
			getPanel(title).add(widget);
		}
		else
		{
			HorizontalPanel panel = new HorizontalPanel();
			panel.getElement().getStyle().setPaddingLeft(2.5, Unit.PX);
			panel.getElement().getStyle().setPaddingRight(2.5, Unit.PX);
			panel.setTitle(title);
			panel.add(widget);
			mainPanel.add(panel);
		}
		
	}
	
	public void addSeparator(){
		mainPanel.add(new HTML("<hr />"));
		count++;
		count++;
	}
	
	public void addSeparator(String title){
		HorizontalPanel panel = new HorizontalPanel();
		panel.setBorderWidth(0);
		panel.setWidth("100%");
		panel.setTitle(title);
		panel.add(new HTML("<hr />"));
		mainPanel.add(panel);
		count++;
		count++;
	}
	
	public void setAlignment(Widget widget, HorizontalAlignmentConstant align){
		mainPanel.setCellHorizontalAlignment(widget, align);
	}
	
	public void clearWidgets(){
		mainPanel.clear();
	}
	
	public void hideGroup(String title){
		for(int i=0; i<mainPanel.getWidgetCount();i++)
		{
			if(mainPanel.getWidget(i).getTitle().equals(title))
				mainPanel.getWidget(i).setVisible(false);
		}
	}
	
	public void showGroup(String title){
		for(int i=0; i<mainPanel.getWidgetCount();i++)
		{
			if(mainPanel.getWidget(i).getTitle().equals(title))
				mainPanel.getWidget(i).setVisible(true);
		}
	}
	
	public boolean groupExist(int count){
		try {
				if(mainPanel.getWidget(count)!=null)
				{
					return true;
				}
			}   catch(Exception e){
					return false;
		}
		return false;
	}
	
	public boolean groupExist(String title){
		for(int i=0; i<mainPanel.getWidgetCount();i++)
		{
			if(mainPanel.getWidget(i).getTitle().equals(title))
			{
				try {
						if(mainPanel.getWidget(i+1)!=null)
						{
							return true;
						}
					}   catch(Exception e){
							return false;
				}
			}
		}
		return false;
	}
	
	public HorizontalPanel getPanel(int count){
			return (HorizontalPanel) mainPanel.getWidget(count);
	}
	
	public HorizontalPanel getPanel(String title){
		for(int i=0; i<mainPanel.getWidgetCount();i++)
		{
			if(mainPanel.getWidget(i).getTitle().equals(title))
			{
				return (HorizontalPanel) mainPanel.getWidget(i+1);
			}
		}
		return null;
	}
	
	public VerticalPanel asVerticalPanel(){
		return mainPanel;
	}
	
public String[][] decode_from_php_num(String stringData) {
		
		stringData = stringData.replace("\"][\"", "|");
		stringData = stringData.replace("[\"", "");
		
		//Some php request adds a magical space behind the data that even trim() doesn't remove. Who knows why...........
		stringData = stringData.replace("\"] ", "");
		
		stringData = stringData.replace("\"]", "");
		
		String[] rows = stringData.split("[|]");
		
		String[][] data = new String[rows.length][];

		for (int i = 0; i < rows.length; i++) 
		{
			rows[i]=rows[i].replace("\",\"", "|");
			data[i]=rows[i].split("[|]");
		}
		return data;
	}

}

}
