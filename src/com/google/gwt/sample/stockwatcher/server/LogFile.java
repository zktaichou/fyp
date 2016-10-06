package com.google.gwt.sample.stockwatcher.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile{
	static String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	static String getSiteList = fileName("getSiteList");
	static String getControllerList = fileName("getControllerList");
	static String getSensorList = fileName("getSensorList");
	static String getActuatorList = fileName("getActuatorList");
	static String failedEcryption = fileName("failedEcryption");
	static String failedDecryption = fileName("failedDecryption");
	static String failedStringDecryption = fileName("failedStringDecryption");
	static String failedLoginDecryption = fileName("failedLoginDecryption");
	static String requestError = fileName("requestError");
	static String userLoginError = fileName("userLoginError");
	static String actuatorSetStatus = fileName("actuatorSetStatus");
	static String dataToString = fileName("dataToString");
	static String getKey = fileName("getKey");
	
	public static String fileName(String name){
		return "log/"+name+"_"+timeStamp+".txt";
	}
	
}
