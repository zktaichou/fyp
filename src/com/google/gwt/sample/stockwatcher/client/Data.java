package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.Timer;

public class Data{
	
	static int latestRequestID;
	static HashMap<String, String> siteList = new HashMap<>();
	static HashMap<String, ArrayList<String>> siteControllerList = new HashMap<>();
	static HashMap<String, ArrayList<String>> controllerAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> controllerSensorList = new HashMap<>();
	static HashMap<String, ArrayList<String>> sensorAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> controllerActuatorList = new HashMap<>();
	static HashMap<String, ArrayList<String>> actuatorAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> regularScheduleActuatorList = new HashMap<>();
	static HashMap<String, ArrayList<String>> regularScheduleActuatorAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> specialScheduleActuatorList = new HashMap<>();
	static HashMap<String, ArrayList<String>> specialScheduleActuatorAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> actuatorRegularScheduleList = new HashMap<>();
	static HashMap<String, ArrayList<String>> regularScheduleAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> actuatorSpecialScheduleList = new HashMap<>();
	static HashMap<String, ArrayList<String>> specialScheduleAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> dayScheduleRuleAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> sensorAcutatorResponseAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> controllerSubscriptionAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> sensorSubscriptionAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> actuatorSubscriptionAttributeList = new HashMap<>();
	static HashMap<String, ArrayList<String>> sensorTresholdList = new HashMap<>();
	static ArrayList<String> subscribedControllerList = new ArrayList<String>();
	static ArrayList<String> subscribedSensorList = new ArrayList<String>();
	static ArrayList<String> subscribedActuatorList = new ArrayList<String>();
	static int regularScheduleAttributeSize = 0;
	static int specialScheduleAttributeSize = 0;
	static int ruleAttributeSize = 0;
	static int responseAttributeSize = 0;
	static String currentUser = "";
	static ArrayList<Timer> gaugeTimers = new ArrayList<Timer>();
}
