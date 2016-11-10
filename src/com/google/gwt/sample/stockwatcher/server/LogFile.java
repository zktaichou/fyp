package com.google.gwt.sample.stockwatcher.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile{
	public static String getSiteList(){
		return fileName("getSiteList");
	}
	public static String getControllerList(){
		return fileName("getControllerList");
	}
	public static String getSensorList(){
		return fileName("getSensorList");
	}
	public static String getActuatorList(){
		return fileName("getActuatorList");
	}
	public static String failedEcryption(){
		return fileName("failedEcryption");
	}
	public static String failedDecryption(){
		return fileName("failedDecryption");
	}
	public static String failedStringDecryption(){
		return fileName("failedStringDecryption");
	}
	public static String failedLoginDecryption(){
		return fileName("failedLoginDecryption");
	}
	public static String failedLocalDateTimeDecryption(){
		return fileName("failedLocalDateTimeDecryption");
	}
	public static String failedArrayListStringDecryption(){
		return fileName("failedArrayListStringDecryption");
	}
	public static String requestError(){
		return fileName("requestError");
	}
	public static String userLoginError(){
		return fileName("userLoginError");
	}
	public static String actuatorSetStatus(){
		return fileName("actuatorSetStatus");
	}
	public static String dataToString(){
		return fileName("dataToString");
	}
	public static String getKey(){
		return fileName("getKey");
	}
	public static String getRegularSchedules(){
		return fileName("getRegularSchedules");
	}
	public static String getRegularScheduleByName(){
		return fileName("getRegularScheduleByName");
	}
	public static String getSpecialSchedules(){
		return fileName("getSpecialSchedules");
	}
	public static String getSpecialScheduleByName(){
		return fileName("getSpecialScheduleByName");
	}
	public static String getActuatorRegularSchedule(){
		return fileName("getActuatorRegularSchedule");
	}
	public static String getActuatorSpecialSchedule(){
		return fileName("getActuatorSpecialSchedule");
	}
	public static String createRegularSchedule(){
		return fileName("createRegularSchedule");
	}
	public static String deleteRegularSchedule(){
		return fileName("deleteRegularSchedule");
	}
	public static String createSpecialSchedule(){
		return fileName("createSpecialSchedule");
	}
	public static String deleteSpecialSchedule(){
		return fileName("deleteSpecialSchedule");
	}
	public static String getDayScheduleRuleAll(){
		return fileName("getDayScheduleRuleAll");
	}
	public static String getDayScheduleRuleByName(){
		return fileName("getDayScheduleRuleByName");
	}
	public static String createDayScheduleRule(){
		return fileName("createDayScheduleRule");
	}
	public static String updateRegularSchedule(){
		return fileName("updateRegularSchedule");
	}
	public static String updateSpecialSchedule(){
		return fileName("updateSpecialSchedule");
	}
	public static String updateDayScheduleRule(){
		return fileName("updateDayScheduleRule");
	}
	public static String deleteDayScheduleRule(){
		return fileName("deleteDayScheduleRule");
	}
	public static String sensorActuatorResponseGetAll(){
		return fileName("sensorActuatorResponseGetAll");
	}
	public static String sensorActuatorResponseCreate(){
		return fileName("sensorActuatorResponseCreate");
	}
	public static String sensorActuatorResponseUpdate(){
		return fileName("sensorActuatorResponseUpdate");
	}
	public static String sensorActuatorResponseDelete(){
		return fileName("sensorActuatorResponseDelete");
	}
	public static String getOngoingSchedulesAll(){
		return fileName("getOngoingSchedulesAll");
	}
	public static String actuatorSetControlType(){
		return fileName("actuatorSetControlType");
	}
	public static String localDateTimeToString(){
		return fileName("localDateTimeToString");
	}
	public static String userGetControllerNotificationLastReadTime(){
		return fileName("userGetControllerNotificationLastReadTime");
	}
	public static String userGetSensorNotificationLastReadTime(){
		return fileName("userGetSensorNotificationLastReadTime");
	}
	public static String userGetActuatorNotificationLastReadTime(){
		return fileName("userGetActuatorNotificationLastReadTime");
	}
	public static String userSubscribeControllerNotification(){
		return fileName("userSubscribeControllerNotification");
	}
	public static String userUnsubscribeControllerNotification(){
		return fileName("userUnsubscribeControllerNotification");
	}
	public static String userSubscribeSensorNotification(){
		return fileName("userSubscribeSensorNotification");
	}
	public static String userUnsubscribeSensorNotification(){
		return fileName("userUnsubscribeSensorNotification");
	}
	public static String userSubscribeActuatorNotification(){
		return fileName("userSubscribeActuatorNotification");
	}
	public static String userUnsubscribeActuatorNotification(){
		return fileName("userUnsubscribeActuatorNotification");
	}
	public static String userUpdateControllerNotificationLastReadTime(){
		return fileName("userUpdateControllerNotificationLastReadTime");
	}
	public static String userUpdateSensorNotificationLastReadTime(){
		return fileName("userUpdateSensorNotificationLastReadTime");
	}
	public static String userUpdateActuatorNotificationLastReadTime(){
		return fileName("userUpdateActuatorNotificationLastReadTime");
	}
	public static String userGetSubscribedControllers(){
		return fileName("userGetSubscribedControllers");
	}
	public static String userGetSubscribedSensors(){
		return fileName("userGetSubscribedSensors");
	}
	public static String userGetSubscribedActuators(){
		return fileName("userGetSubscribedActuators");
	}
	public static String controllerEventGetBetweenTime(){
		return fileName("controllerEventGetBetweenTime");
	}
	public static String sensorEventGetBetweenTime(){
		return fileName("sensorEventGetBetweenTime");
	}
	public static String actuatorEventGetBetweenTime(){
		return fileName("actuatorEventGetBetweenTime");
	}
	
	public static String fileName(String name){
		try{
		File file=new File("log/"+timestamp());
		file.mkdirs();
		} catch (Exception e){}
		
		return "log/"+timestamp()+"/"+name+".txt";
	}
	
	private static String timestamp(){
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}
	
}
