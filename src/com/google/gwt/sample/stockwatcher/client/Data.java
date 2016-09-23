package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.HashMap;

public class Data{
	
	static int latestRequestID;
	static HashMap<String, String> siteList = new HashMap<>();
	static HashMap<String, ArrayList<String>> siteControllerList = new HashMap<>();
	static HashMap<String, ArrayList<String>> controllerSensorList = new HashMap<>();
}
