package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.HashMap;

import org.moxieapps.gwt.highcharts.client.StockChart;

import com.google.gwt.user.client.Timer;

public class Data{
	
	static int latestRequestID;
	static HashMap<String, String> siteList = new HashMap<>();
	static HashMap<String, ArrayList<String>> siteControllerList = new HashMap<>();
	static HashMap<String, ArrayList<String>> controllerSensorList = new HashMap<>();
	static HashMap<StockChart, Timer> chartTimerMap = new HashMap<>();
}
