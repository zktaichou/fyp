package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String sn, Date sd, Date ed, Boolean predictionIsEnabled, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void userLogin(String username, String password, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void getRegularSchedules(AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getRegularScheduleByName(String scheduleName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getActuatorRegularSchedule(String aName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getSpecialSchedules(AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getSpecialScheduleByName(String scheduleName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getActuatorSpecialSchedule(String aName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getSiteList(AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getControllerList(String siteName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getSensorList(String controllerName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getActuatorList(String controllerName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void createRegularSchedule(String rScheduleName, String actuatorName, int dayMask, String rule, String onStart, String onEnd, boolean lock, int priority, boolean scheduleEnabled, AsyncCallback<String> callback) throws IllegalArgumentException;
	void createSpecialSchedule(String sScheduleName, String actuatorName, int year, int month, int day, String rule, String onStart, String onEnd, boolean lock, int priority, boolean scheduleEnabled, AsyncCallback<String> callback) throws IllegalArgumentException;
	void createDayScheduleRule(String rName, int sH, int sM, int eH, int eM, AsyncCallback<String> callback) throws IllegalArgumentException;
	void actuatorSetStatus(String actuator, String status, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getDayScheduleRuleAll(AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getDayScheduleRuleByName(String ruleName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void deleteRegularSchedule(String rScheduleName, AsyncCallback<String> callback) throws IllegalArgumentException;
	void deleteSpecialSchedule(String sScheduleName, AsyncCallback<String> callback) throws IllegalArgumentException;
	void updateRegularSchedule(String rScheduleOldName, String rScheduleNewName, String actuatorName, int dayMask, String rule, String onStart, String onEnd, boolean lock, int priority, boolean scheduleEnabled, AsyncCallback<String> callback) throws IllegalArgumentException;
	void updateSpecialSchedule(String sScheduleOldName, String sScheduleNewName, String actuatorName, int year, int month, int day, String rule, String onStart, String onEnd, boolean lock, int priority, boolean scheduleEnabled,  AsyncCallback<String> callback) throws IllegalArgumentException;
	void deleteDayScheduleRule(String rName, AsyncCallback<String> callback) throws IllegalArgumentException;
	void updateDayScheduleRule(String rOldName, String rNewName, int sH, int sM, int eH, int eM, AsyncCallback<String> callback) throws IllegalArgumentException;
	void sensorActuatorResponseGetAll(AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void sensorActuatorResponseCreate(String actuator, String onTrigger, String onNotTrigger, String expression, boolean enabled, int timeout, AsyncCallback<String> callback) throws IllegalArgumentException;
	void sensorActuatorResponseUpdate(int id, String actuator, String onTrigger, String onNotTrigger, String expression, boolean enabled, int timeout, AsyncCallback<String> callback) throws IllegalArgumentException;
	void sensorActuatorResponseDelete(int id, AsyncCallback<String> callback) throws IllegalArgumentException;
}
