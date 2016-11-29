package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.i18n.client.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.labels.*;
import org.moxieapps.gwt.highcharts.client.plotOptions.*;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
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
static final DateTimeFormat requestFormat = DateTimeFormat.getFormat("yyyy-MM-dd");

static long getTime(String date) {
    return dateTimeFormat.parse(date).getTime();  
}  

	public static String updateReportingTitle(String name, java.sql.Date sd, java.sql.Date ed){
		return name+" readings from "+calendarFormat.format(sd)+" to "+calendarFormat.format(ed);
		//return name+" reading from "+sd+" to "+ed;
	}
	
	public static String updateLiveUpdateTitle(String name, java.sql.Date sd, java.sql.Date ed){
		return "Today's live updates for "+name;
		//return name+" reading from "+sd+" to "+ed;
	}
	
	public static java.sql.Date stringToStartDate(String date){
		String stringDate = date+" 00:00:00";
		return new java.sql.Date(dateTimeFormat.parse(stringDate).getTime());
	}
	
	public static java.sql.Date stringToEndDate(String date){
		String stringDate = date+" 23:59:59";
		return new java.sql.Date(dateTimeFormat.parse(stringDate).getTime());
	}
	
	//Object returned here --------------vvvvv
	public static void getData(final String sn, final java.sql.Date sd, final java.sql.Date ed, final boolean predictionIsEnabled, final boolean isLiveUpdate, final int steps){
		Data.latestRequestID++;
		final int currRequestID=Data.latestRequestID;
		
		Utility.newRequestObj().greetServer(sn,sd,ed,predictionIsEnabled, steps, false, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(String[][] result) {
				if(Utility.isNull(result))
				{
					Utility.setMsg(Messages.NO_DATA);
				}
				else if(currRequestID==Data.latestRequestID)
				{
				result=filterData(result);
				Number [][] data = formatData(result);
				Utility.hideTimer();
				StockChart chart = createLiveChart(sn, data,updateLiveUpdateTitle(sn,sd,ed), predictionIsEnabled, isLiveUpdate, steps);
				MonitoringPage.chartPanel.clear();
				MonitoringPage.chartPanel.add(chart);
				//BasePage.panel.add(createFlexTable(result));
				}
			}
		});
	}
	
	public static void getAppendData(final Series series, String sn, java.sql.Date sd, java.sql.Date ed, final Boolean prediction, final Series predictionSeries){
		
		Utility.newRequestObj().greetServer(sn,sd,ed,prediction,1,true, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Data append fail");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(String[][] result) {
				
				Number [][] data = formatData(result);

			    int lastRow = data.length;
			    if(prediction)
			    {
				    predictionSeries.addPoint(data[lastRow][0],data[lastRow][1], true, true, true);
				    lastRow-=1;
			    }
				for(int i=0;i<lastRow;i++)
				{
					series.addPoint(data[i][0],data[i][1], true, true, true);
				}
			}
		});
	}
	
	private static String [][] filterData(String[][] result){
		if(result.length<=500) return result;
		else {
			int factor=result.length/500;
			int count=result.length;
			int resultxCount=0;
			String [][] resultx=new String[500][result[0].length];
			for (int i=0;i<result.length;i++) {
				if (count>500) {
					if (i%factor==0) resultx[resultxCount++]=result[i];
					else count--;
				} else {
					resultx[resultxCount++]=result[i];
				}
			}
			return resultx;
		}
	}
	
	public static Number[][] formatData(String [][] input){
		int dataCount = input[0].length;
		Number [][] data = new Number[input.length][dataCount];
		
		for(int i=0;i<input.length;i++)
		{
			data[i][0]=DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").parse(input[i][0]).getTime();
			for(int j=1; j<dataCount;j++)
			{
				data[i][j]=convertToNumber(input[i][j]);
			}
		}
		return data;
	}
	
	public static Number[][] formatDaily(String [][] input){
		int dataCount = input[0].length;
		Number [][] data = new Number[input.length][dataCount];
		
		for(int i=0;i<input.length;i++)
		{
			data[i][0]=DateTimeFormat.getFormat("yyyy-MM-dd").parse(input[i][0]).getTime();
			for(int j=1; j<dataCount;j++)
			{
				data[i][j]=convertToNumber(input[i][j]);
			}
		}
		return data;
	}
	
	public static Number[][] formatMonthly(String [][] input){
		int dataCount = input[0].length;
		Number [][] data = new Number[input.length][dataCount];
		
		for(int i=0;i<input.length;i++)
		{
			data[i][0]=DateTimeFormat.getFormat("yyyy-MM").parse(input[i][0]).getTime();
			for(int j=1; j<dataCount;j++)
			{
				data[i][j]=convertToNumber(input[i][j]);
			}
		}
		return data;
	}
	
	public static Number[][] formatYearly(String [][] input){
		int dataCount = input[0].length;
		Number [][] data = new Number[input.length][dataCount];
		
		for(int i=0;i<input.length;i++)
		{
			data[i][0]=DateTimeFormat.getFormat("yyyy").parse(input[i][0]).getTime();
			for(int j=1; j<dataCount;j++)
			{
				data[i][j]=convertToNumber(input[i][j]);
			}
		}
		return data;
	}
	
	public static StockChart createLiveChart(final String sensorName, Number[][] data, String title, final Boolean predictionIsEnabled, final Boolean isLiveUpdate, int steps){
		
		Utility.hideTimer();
		
		final StockChart chart = new StockChart();
		chart
		.setType(Series.Type.SPLINE)  
        .setMarginRight(30)  
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
		chart.setBackgroundColor(new Color()
				   .setLinearGradient(0.0, 0.0, 1.0, 1.0)
				   .addColorStop(0, 0, 0, 0, 1)
				   .addColorStop(0, 0, 0, 0, 0)
				 );
		
		chart.getXAxis().setDateTimeLabelFormats(
				new DateTimeLabelFormats()
				    .setSecond("%l:%M:%S %p"));
		
		ArrayList<String> attributes = Data.sensorAttributeList.get(sensorName);
		String unit = attributes.get(5);
		
		chart.getYAxis()
        .setAxisTitleText(unit)
        .setPlotLines(  
            chart.getYAxis().createPlotLine()  
                .setValue(0)  
                .setWidth(1)  
                .setColor("#808080")  
        );
		
		final Series series = chart.createSeries();  
	    chart.addSeries(series.setName("Data")); 
	    
	    final Series predictionSeries = chart.createSeries();
	    int lastRow = data.length-1;
	    
	    if(predictionIsEnabled)
	    {
		    lastRow-=steps;
		    chart.addSeries(predictionSeries.setName("Prediction")); 
		    for(int i=lastRow;i<data.length;i++)
		    {
		    	predictionSeries.addPoint(data[i][0],data[i][1]);
		    }
	    }
	    
	    for(int i=0;i<lastRow;i++)
		{
	    	series.addPoint(data[i][0],data[i][1]);
		}
	    
	    if(isLiveUpdate)
	    {
		    final Timer tempTimer = new Timer() {
		    	java.sql.Date lastRequestTime = new java.sql.Date(System.currentTimeMillis());
	            @Override  
	            public void run() {
	            	if (chart.isAttached()) {
	            		if (chart.isRendered()) {
		                	long currTime=System.currentTimeMillis();
		                	getAppendData(series,sensorName,lastRequestTime,new java.sql.Date(currTime),predictionIsEnabled, predictionSeries);
		                    lastRequestTime=new java.sql.Date(currTime+10);
	            		}
	                    schedule(2000);
	            	}
	
	            }  
	        };
	        tempTimer.schedule(0);
	    }
        
        chart.setSize(Window.getClientWidth()*2/3, Window.getClientHeight()*2/3);
	    
		return chart;
	}
	
	public static Chart createReportChart(final String sensorName, Number[][] data, String title, final Boolean predictionIsEnabled, int steps){
		
		Utility.hideTimer();
		
		final Chart chart = new Chart();
		chart
		.setType(Series.Type.COLUMN)  
        .setChartTitleText(title)
        .setOptions3D(new Options3D()  
                .setEnabled(true)  
                .setAlpha(15)  
                .setBeta(15)  
                .setViewDistance(25)  
                .setDepth(40)  
            ) 
	    .setLegend(new Legend().setEnabled(false))  
	    .setCredits(new Credits().setEnabled(false))
	    ;
		chart.setBackgroundColor(new Color()
				   .setLinearGradient(0.0, 0.0, 1.0, 1.0)
				   .addColorStop(0, 0, 0, 0, 1)
				   .addColorStop(0, 0, 0, 0, 0)
				 );

		String[] dateContents = title.split(" ");

		chart.getXAxis().setType(Axis.Type.DATE_TIME).setMaxZoom(14 * 24 * 3600000);
		
		for(int i=0;i<dateContents.length;i++)
		{
			if(dateContents[i].equals("Daily"))
			{
				chart.getXAxis().setDateTimeLabelFormats(new DateTimeLabelFormats().setDay("%e %B %Y"));
				break;
			}
			if(dateContents[i].equals("Monthly"))
			{
				chart.getXAxis().setDateTimeLabelFormats(new DateTimeLabelFormats().setMonth("%B %Y"));
				break;
			}
			if(dateContents[i].equals("Yearly"))
			{
				chart.getXAxis().setDateTimeLabelFormats(new DateTimeLabelFormats().setYear("%Y"));
				break;
			}
		}
		
		chart.getYAxis()
        .setPlotLines(  
            chart.getYAxis().createPlotLine()  
                .setValue(0)  
                .setWidth(2)  
                .setColor("#808080")  
        );
		
		final Series series = chart.createSeries();  
	    chart.addSeries(series.setName("Data")); 
	    
	    final Series predictionSeries = chart.createSeries();
	    int lastRow = data.length;
	    
	    if(predictionIsEnabled && data.length>1)
	    {
		    lastRow-=steps;
		    chart.addSeries(predictionSeries.setName("Prediction")); 
		    for(int i=lastRow;i<data.length;i++)
		    {
		    	predictionSeries.addPoint(data[i][0],data[i][1]);
		    }
	    }
	    
	    for(int i=0;i<lastRow;i++)
		{
	    	series.addPoint(data[i][0],data[i][1]);
		}
        
        chart.setSize(Window.getClientWidth()*2/3, Window.getClientHeight()*2/3);
	    
		return chart;
	}
	
	public static HorizontalPanel createGaugeChart(final String name, final int size){
		final HorizontalPanel lol = new HorizontalPanel();
		final Chart chart = new Chart();
		chart
		.setType(Series.Type.SOLID_GAUGE)  
	    .setLegend(new Legend().setEnabled(false))  
	    .setCredits(new Credits().setEnabled(false))
	    .setAlignTicks(false)
        .setPlotBackgroundImage(null)
        .setBorderWidth(0)
        .setPlotShadow(false)
        .setExporting(new Exporting().setEnabled(false))
        .setChartTitle(new ChartTitle()
                .setText("Live reading of "+name)
        )
        .setPane(new Pane()
                .setStartAngle(-90)
                .setEndAngle(90)
                .setCenter("50%", "85%")
                .setBackground(new PaneBackground()
                		.setInnerRadius("60%")
                		.setOuterRadius("100%")
                		.setShape(PaneBackground.Shape.ARC)
                		.setBackgroundColor("rgba(0,0,0,0)")
                		)
        )
        .setSolidGaugePlotOptions(new SolidGaugePlotOptions()
                .setAnimation(false))
        ;
		
		
		final ArrayList<String> attributes =  Data.sensorAttributeList.get(name);
		
		final Number maxTreshold = (Number)Double.parseDouble(attributes.get(8));
	    chart.getYAxis().setMin(0);
	    chart.getYAxis().setMax(maxTreshold);
	    chart.getYAxis().setLineColor("gray");
		
		chart.setBackgroundColor(new Color()
				   .setLinearGradient(0.0, 0.0, 1.0, 1.0)
				   .addColorStop(0, 0, 0, 0, 1)
				   .addColorStop(0, 0, 0, 0, 0)
				 );
		
        chart.getYAxis()
                .setTickPosition(Axis.TickPosition.INSIDE)
                .setMinorTickPosition(Axis.TickPosition.INSIDE)
                .setLineColor("white")
                .setTickColor("white")
                .setMinorTickColor("white")
                .setLineWidth(2)
                .setEndOnTick(true)
                .setLabels(
                        new YAxisLabels()
                                // There is no documented "distance" option for gauge chart axes
                                .setOption("distance", -20)
                )
        ;
		
		final Series series = chart.createSeries();
        chart.addSeries(series
                .setName("Reading")
                .addPoint(0)
        );
        final Timer tempTimer = new Timer() {
            @Override
            public void run() {
            	if(MainMenuPage.mainPanel.isVisible())
            	Utility.newRequestObj().getLatestReading(name, new AsyncCallback<Double>(){
        			public void onFailure(Throwable caught) {
//        				Window.alert("Unable to get "+Data.currentUser+"'s sensor subscription");
        			} 
         
        			public void onSuccess(Double reply) {
//        				double temp = Utility.round(new Random().nextDouble()*Double.parseDouble(attributes.get(8)));
        				double temp = Utility.round(reply);
        				series.getPoints()[0].update(temp);
        				chart.setColors(updateColor(temp,maxTreshold));
        				lol.add(chart);
        			}
        		});
            }
        };

        tempTimer.scheduleRepeating(2000);
        
        chart.setSize(Window.getClientWidth()*1/size, Window.getClientHeight()*1/size);
        if(size>4)
        {
        chart.setSize(Window.getClientWidth()*1/4, Window.getClientHeight()*1/4);
        }
        lol.add(chart);
		return lol;
	}
	
	private static void doFadeOut(HorizontalPanel panel) {
		final Element element = panel.getElement();
	    final double opacity = Double.parseDouble(element.getStyle().getOpacity());
	    final Timer tempTimer = new Timer() {
            @Override
            public void run() {
            	if(opacity==0)
            	{
            		cancel();
            	}
        	    element.getStyle().setOpacity(opacity - 0.1);
            }
        };
        tempTimer.scheduleRepeating(10);
	}

	private static void doFadeIn(HorizontalPanel panel) {
		final Element element = panel.getElement();
	    final double opacity = Double.parseDouble(element.getStyle().getOpacity());
	    final Timer tempTimer = new Timer() {
            @Override
            public void run() {
            	if(opacity==1)
            	{
            		cancel();
            	}
        	    element.getStyle().setOpacity(opacity + 0.1);
            }
        };
        tempTimer.scheduleRepeating(10);
	}
	
	private static String updateColor(double input,Number maxTreshold){
		double ratio = input/maxTreshold.doubleValue();
		int value = (int)(ratio*10);
		switch(value)
		{
		case 0: return "#00FF00";
		case 1: return "#33FF00";
		case 2: return "#66FF00";
		case 3: return "#99FF00";
		case 4: return "#CCFF00";
		case 5: return "#FFFF00";
		case 6: return "#FFCC00";
		case 7: return "#FF9900";
		case 8: return "#FF6600";
		case 9: return "#FF3300";
		case 10: return "#FF0000";
		}
		return null;
	}

	//Methods to create a flex table from an input 2D String array
	public static FlexTable createFlexTable(String data[][])
	{

	FlexTable table = new FlexTable();
	table.setBorderWidth(1);
	table.setCellPadding(5);
	table.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
	
	for(int i=table.getRowCount();i<data.length;i++)
	{
		for(int j=0;j<data[i].length;j++)
		{
			table.setText(i, j, data[i][j]);
		}
	}
	
	return table;
	}
	
	public static FlexTable appendFlexTable(FlexTable ft, String data[][])
	{
	ft.setBorderWidth(1);
	ft.setCellPadding(5);
	ft.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
	
	int rowCount = ft.getRowCount();
	
	for(int i=0;i<data.length;i++)
	{
		for(int j=0;j<data[i].length;j++)
		{
			ft.setText(i+rowCount, j, data[i][j]);
		}
	}
	
	return ft;
	}
	
	//Methods that's made just to improve readability
	private static Number convertToNumber(String unformattedData){
		String data ="";
		if (unformattedData.contains(".")) data=unformattedData.substring(0,Math.min(unformattedData.indexOf('.')+3,unformattedData.length()));
		else data=unformattedData; 
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
					@SuppressWarnings("unused")
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
					@SuppressWarnings("unused")
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
