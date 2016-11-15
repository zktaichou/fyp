package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;

import org.moxieapps.gwt.highcharts.client.Chart;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChartCreationHandler{

	public static void acceptParam(String sensor, Date sd, Date ed, Boolean isPredictionEnabled, int steps, String reportSort, String reportView, Boolean isLiveUpdate){
		if(reportSort.equals("Average"))
		{
			if(reportView.equals("Daily"))
			{
				getAverageReadingByDay(sensor,isPredictionEnabled,steps);
			}
			else if(reportView.equals("Monthly"))
			{
				getAverageReadingByMonth(sensor,isPredictionEnabled,steps);
			}
			else if(reportView.equals("Yearly"))
			{
				getAverageReadingByYear(sensor,isPredictionEnabled,steps);
			}
		}
		else if(reportSort.equals("Sum"))
		{
			if(reportView.equals("Daily"))
			{
				getTotalReadingByDay(sensor,isPredictionEnabled,steps);
			}
			else if(reportView.equals("Monthly"))
			{
				getTotalReadingByMonth(sensor,isPredictionEnabled,steps);
			}
			else if(reportView.equals("Yearly"))
			{
				getTotalReadingByYear(sensor,isPredictionEnabled,steps);
			}
		}
		else if(reportSort.equals("Cumulative Sum"))
		{
			if(reportView.equals("Daily"))
			{
				getCulmulativeReadingByDay(sensor,isPredictionEnabled,steps);
			}
			else if(reportView.equals("Monthly"))
			{
				getCulmulativeReadingByMonth(sensor,isPredictionEnabled,steps);
			}
			else if(reportView.equals("Yearly"))
			{
				getCulmulativeReadingByYear(sensor,isPredictionEnabled,steps);
			}
		}
	}
	
	private static void getTotalReadingByDay(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getTotalReadingGroupByDay(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				String [][] result2 = combineDate(result);
				Number[][] data = ChartUtilities.formatDaily(result2);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Sum of Daily Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static void getTotalReadingByMonth(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getTotalReadingGroupByMonth(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				String [][] result2 = combineDate(result);
				Number[][] data = ChartUtilities.formatMonthly(result2);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Sum of Monthly Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static void getTotalReadingByYear(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getTotalReadingGroupByYear(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				Number[][] data = ChartUtilities.formatYearly(result);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Sum of Yearly Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static void getAverageReadingByDay(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getAverageReadingGroupByDay(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				String [][] result2 = combineDate(result);
				Number[][] data = ChartUtilities.formatDaily(result2);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Average Daily Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static void getAverageReadingByMonth(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getAverageReadingGroupByMonth(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				String [][] result2 = combineDate(result);
				Number[][] data = ChartUtilities.formatMonthly(result2);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Average Monthly Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static void getAverageReadingByYear(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getAverageReadingGroupByYear(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				Number[][] data = ChartUtilities.formatYearly(result);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Average Yearly Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static void getCulmulativeReadingByDay(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getCulmulativeReadingGroupByDay(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				String [][] result2 = combineDate(result);
				Number[][] data = ChartUtilities.formatDaily(result2);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Daily Cumulative Sum Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static void getCulmulativeReadingByMonth(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getCulmulativeReadingGroupByMonth(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				String [][] result2 = combineDate(result);
				Number[][] data = ChartUtilities.formatMonthly(result2);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Monthly Cumulative Sum Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static void getCulmulativeReadingByYear(final String sensor, final Boolean isPredictionIsEnabled, final int steps){
		Utility.newRequestObj().getCulmulativeReadingGroupByYear(sensor,new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Data request failed");
			}
			public void onSuccess(String[][] result) {
				Number[][] data = ChartUtilities.formatYearly(result);
				Chart chart = ChartUtilities.createReportChart(sensor, data, "Yearly Cumulative Sum Readings of "+sensor, isPredictionIsEnabled, steps);
				ReportingPage.chartPanel.add(chart);
			}
		});
	}
	
	private static String[][] combineDate(String[][] data){
		String[][] newData = new String[data.length][2];
		if(data[0].length==4) //have year, month, day
		{
			for(int i=0; i<data.length; i++)
			{
				String date = data[i][0]+"-"+data[i][1]+"-"+data[i][2];
				newData[i][0]=date;
				newData[i][1]=data[i][3];
			}
			return newData;
		}
		else if(data[0].length==3) //have year, month
		{
			for(int i=0; i<data.length; i++)
			{
				String date = data[i][0]+"-"+data[i][1];
				newData[i][0]=date;
				newData[i][1]=data[i][2];
			}
			return newData;
		}
		return null;
	}
}
