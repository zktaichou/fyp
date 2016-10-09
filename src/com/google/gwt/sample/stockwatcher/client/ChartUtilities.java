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
static final DateTimeFormat calendarFormat = DateTimeFormat.getFormat("d MMMM yyyy");
static final DateTimeFormat requestFormat = DateTimeFormat.getFormat("dd-MM-yyyy");

static long getTime(String date) {
    return dateTimeFormat.parse(date).getTime();  
}  



	private static String updateTitle(String name, java.sql.Date sd, java.sql.Date ed){
		return name+" reading from "+sd+" to "+ed;
	}
	
	public static java.sql.Date stringToDate(String date){
		String stringDate = date+" 00:00:00";
		return new java.sql.Date(dateTimeFormat.parse(stringDate).getTime());
	}
	
	//Object returned here --------------vvvvv
	public static void getData(final String sn, final java.sql.Date sd, final java.sql.Date ed, final boolean predictionIsEnabled){
		Data.latestRequestID++;
		final int currRequestID=Data.latestRequestID;
		
		chartPanel.clear();
		if(!chartPanel.isAttached())
		BasePage.contentPanel.add(chartPanel);
		
		Utility.newRequestObj().greetServer(sn,sd,ed,predictionIsEnabled, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(String[][] result) {
				if(currRequestID==Data.latestRequestID)
				{
				Number [][] data = formatData(result);
				StockChart chart = createChart(data,updateTitle(sn,sd,ed), predictionIsEnabled);
				if(predictionIsEnabled)
				{
//					chart.addSeries(series);
				}
				
				chartPanel.add(chart);
				//BasePage.panel.add(createFlexTable(result));
				}
			}
		});
	}
	
	public static void getAppendData(final Series s, String sn, java.sql.Date sd, java.sql.Date ed, Boolean prediction){
		
		Utility.newRequestObj().greetServer(sn,sd,ed,prediction,new AsyncCallback<String[][]>() {
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
		int dataCount = input[0].length;
		Number [][] data = new Number[input.length][dataCount];
		
		for(int i=input.length-1;i>=0;i--)
		{
			data[input.length-i-1][0]=DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").parse(input[i][0]).getTime();
			for(int j=1; j<dataCount;j++)
			{
			data[input.length-i-1][j]=convertToNumber(input[i][j]);
			}
		}
		return data;
	}
	
	public static StockChart createChart(Number[][] data, String title, final Boolean predictionIsEnabled){
		
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
	                	getAppendData(series,"testTemp",lastRequestTime,new java.sql.Date(currTime),predictionIsEnabled);
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
	
    public static Chart realTimeUpdatesChart() {
	
    final Chart chart = new Chart()  
        .setType(Series.Type.SPLINE)  
        .setMarginRight(10)  
        .setChartTitleText("Just some sample chart~")  
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
        .setOption("/exporting/enabled", false)
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

//Methods that's made just to improve readability

private static Number convertToNumber(String data){
	Number value ;
	try	{
		value = Integer.parseInt(data);
	} catch (NumberFormatException e) { 
		value = NumberFormat.getDecimalFormat().parse(data);} //assign value
	return value;
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
	}
}
