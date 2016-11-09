package com.google.gwt.sample.stockwatcher.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile{
	static String getSiteList = fileName("getSiteList");
	static String getControllerList = fileName("getControllerList");
	static String getSensorList = fileName("getSensorList");
	static String getActuatorList = fileName("getActuatorList");
	static String failedEcryption = fileName("failedEcryption");
	static String failedDecryption = fileName("failedDecryption");
	static String failedStringDecryption = fileName("failedStringDecryption");
	static String failedLoginDecryption = fileName("failedLoginDecryption");
	static String failedLocalDateTimeDecryption = fileName("failedLocalDateTimeDecryption");
	static String failedArrayListStringDecryption = fileName("failedArrayListStringDecryption");
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
	static String createRegularSchedule = fileName("createRegularSchedule");
	static String deleteRegularSchedule = fileName("deleteRegularSchedule");
	static String createSpecialSchedule = fileName("createSpecialSchedule");
	static String deleteSpecialSchedule = fileName("deleteSpecialSchedule");
	static String getDayScheduleRuleAll = fileName("getDayScheduleRuleAll");
	static String getDayScheduleRuleByName = fileName("getDayScheduleRuleByName");
	static String createDayScheduleRule = fileName("createDayScheduleRule");
	static String updateRegularSchedule = fileName("updateRegularSchedule");
	static String updateSpecialSchedule = fileName("updateSpecialSchedule");
	static String updateDayScheduleRule = fileName("updateDayScheduleRule");
	static String deleteDayScheduleRule = fileName("deleteDayScheduleRule");
	static String sensorActuatorResponseGetAll = fileName("sensorActuatorResponseGetAll");
	static String sensorActuatorResponseCreate = fileName("sensorActuatorResponseCreate");
	static String sensorActuatorResponseUpdate = fileName("sensorActuatorResponseUpdate");
	static String sensorActuatorResponseDelete = fileName("sensorActuatorResponseDelete");
	static String getOngoingSchedulesAll = fileName("getOngoingSchedulesAll");
	static String actuatorSetControlType = fileName("actuatorSetControlType");
	static String localDateTimeToString = fileName("localDateTimeToString");
	static String userGetControllerNotificationLastReadTime = fileName("userGetControllerNotificationLastReadTime");
	static String userGetSensorNotificationLastReadTime = fileName("userGetSensorNotificationLastReadTime");
	static String userGetActuatorNotificationLastReadTime = fileName("userGetActuatorNotificationLastReadTime");
	static String userSubscribeControllerNotification = fileName("userSubscribeControllerNotification");
	static String userUnsubscribeControllerNotification = fileName("userUnsubscribeControllerNotification");
	static String userSubscribeSensorNotification = fileName("userSubscribeSensorNotification");
	static String userUnsubscribeSensorNotification = fileName("userUnsubscribeSensorNotification");
	static String userSubscribeActuatorNotification = fileName("userSubscribeActuatorNotification");
	static String userUnsubscribeActuatorNotification = fileName("userUnsubscribeActuatorNotification");
	static String userUpdateControllerNotificationLastReadTime = fileName("userUpdateControllerNotificationLastReadTime");
	static String userUpdateSensorNotificationLastReadTime = fileName("userUpdateSensorNotificationLastReadTime");
	static String userUpdateActuatorNotificationLastReadTime = fileName("userUpdateActuatorNotificationLastReadTime");
	static String userGetSubscribedControllers = fileName("userGetSubscribedControllers");
	static String userGetSubscribedSensors = fileName("userGetSubscribedSensors");
	static String userGetSubscribedActuators = fileName("userGetSubscribedActuators");
	
	public static String fileName(String name){
		try{
		File file = new File("log_"+timestamp());
		file.mkdir();
		} catch (Exception e){}
		
		return "log/"+timestamp()+"/"+name+".txt";
	}
	
	private static String timestamp(){
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}
	
}
