package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	boolean userLogin(String username, String password) throws IllegalArgumentException;
	String[][] greetServer(String sn, Date sd, Date ed, Boolean predictionIsEnabled, int steps, Boolean isAppend) throws IllegalArgumentException;
	String[][] getRegularSchedules() throws IllegalArgumentException;
	String[][] getRegularScheduleByName(String scheduleName) throws IllegalArgumentException;
	String[][] getActuatorRegularSchedule(String aName) throws IllegalArgumentException;
	String[][] getSpecialSchedules() throws IllegalArgumentException;
	String[][] getSpecialScheduleByName(String scheduleName) throws IllegalArgumentException;
	String[][] getActuatorSpecialSchedule(String aName) throws IllegalArgumentException;
	String[][] getSiteList() throws IllegalArgumentException;
	String[][] getControllerList(String siteName) throws IllegalArgumentException;
	String[][] getSensorList(String controllerName) throws IllegalArgumentException;
	String[][] getActuatorList(String controllerName) throws IllegalArgumentException;
	String createRegularSchedule(String rScheduleName, String actuatorName, int dayMask, String rule, String onStart, String onEnd, boolean lock, int priority, boolean scheduleEnabled) throws IllegalArgumentException;
	String createSpecialSchedule(String sScheduleName, String actuatorName, int year, int month, int day, String rule, String onStart, String onEnd, boolean lock, int priority, boolean scheduleEnabled) throws IllegalArgumentException;
	String actuatorSetStatus(String actuator, String status) throws IllegalArgumentException;
	String actuatorSetControlType(String actuator, String controlType) throws IllegalArgumentException;
	String[][] getDayScheduleRuleAll() throws IllegalArgumentException;
	String[][] getDayScheduleRuleByName(String ruleName) throws IllegalArgumentException;
	String createDayScheduleRule(String rName, int sH, int sM, int eH, int eM) throws IllegalArgumentException;
	String deleteRegularSchedule(String rScheduleName) throws IllegalArgumentException;
	String deleteSpecialSchedule(String sScheduleName) throws IllegalArgumentException;
	String updateRegularSchedule(String rScheduleOldName, String rScheduleNewName, String actuatorName, int dayMask, String rule, String onStart, String onEnd, boolean lock, int priority, boolean scheduleEnabled) throws IllegalArgumentException;
	String updateSpecialSchedule(String sScheduleName, String sScheduleNewName, String actuatorName, int year, int month, int day, String rule, String onStart, String onEnd, boolean lock, int priority, boolean scheduleEnabled) throws IllegalArgumentException;
	String deleteDayScheduleRule(String rName) throws IllegalArgumentException;
	String updateDayScheduleRule(String rOldName, String rNewName, int sH, int sM, int eH, int eM) throws IllegalArgumentException;
	String[][] sensorActuatorResponseGetAll() throws IllegalArgumentException;
	String sensorActuatorResponseCreate(String actuator, String onTrigger, String onNotTrigger, String expression, boolean enabled, int timeout) throws IllegalArgumentException;
	String sensorActuatorResponseUpdate(int id, String actuator, String onTrigger, String onNotTrigger, String expression, boolean enabled, int timeout) throws IllegalArgumentException;
	String sensorActuatorResponseDelete(int id) throws IllegalArgumentException;
	String[][] getOngoingSchedulesAll() throws IllegalArgumentException;
	String userSubscribeControllerNotification(String user, String controller) throws IllegalArgumentException;
	String userUpdateControllerNotificationLastReadTime(String user, String controller, Date date) throws IllegalArgumentException;
	String userUnsubscribeControllerNotification(String user, String controller) throws IllegalArgumentException;
	String userGetControllerNotificationLastReadTime(String user, String controller) throws IllegalArgumentException;
	String userSubscribeSensorNotification(String user, String sensor) throws IllegalArgumentException;
	String userUpdateSensorNotificationLastReadTime(String user, String sensor, Date date) throws IllegalArgumentException;
	String userUnsubscribeSensorNotification(String user, String sensor) throws IllegalArgumentException;
	String userGetSensorNotificationLastReadTime(String user, String sensor) throws IllegalArgumentException;
	String userSubscribeActuatorNotification(String user, String actuator) throws IllegalArgumentException;
	String userUpdateActuatorNotificationLastReadTime(String user, String actuator, Date date) throws IllegalArgumentException;
	String userUnsubscribeActuatorNotification(String user, String actuator) throws IllegalArgumentException;
	String userGetActuatorNotificationLastReadTime(String user, String actuator) throws IllegalArgumentException;
	String[][] userGetSubscribedControllers(String user) throws IllegalArgumentException;
	String[][] userGetSubscribedSensors(String user) throws IllegalArgumentException;
	String[][] userGetSubscribedActuators(String user) throws IllegalArgumentException;
	String[][] controllerEventGetBetweenTime(ArrayList<String> controllerList, Date start, Date end) throws IllegalArgumentException;
	String[][] sensorEventGetBetweenTime(ArrayList<String> controllerList, Date start, Date end) throws IllegalArgumentException;
	String[][] actuatorEventGetBetweenTime(ArrayList<String> controllerList, Date start, Date end) throws IllegalArgumentException;
}
