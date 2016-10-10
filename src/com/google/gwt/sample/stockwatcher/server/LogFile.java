package com.google.gwt.sample.stockwatcher.server;

import java.io.File;
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
	static String getRegularSchedules = fileName("getRegularSchedules");
	static String getRegularScheduleByName = fileName("getRegularScheduleByName");
	static String getSpecialSchedules = fileName("getSpecialSchedules");
	static String getSpecialScheduleByName = fileName("getSpecialScheduleByName");
	static String getActuatorRegularSchedule = fileName("getActuatorRegularSchedule");
	static String getActuatorSpecialSchedule = fileName("getActuatorSpecialSchedule");
	
	public static String fileName(String name){
		try{
		File file = new File("log");
		} catch (Exception e){}
		
		return "log/"+name+"_"+timeStamp+".txt";
	}
	
}
