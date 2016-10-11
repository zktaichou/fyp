package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	boolean userLogin(String username, String password) throws IllegalArgumentException;
	String[][] greetServer(String sn, Date sd, Date ed, Boolean predictionIsEnabled) throws IllegalArgumentException;
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
	String createRegularSchedule(String rScheduleName, String actuatorName, int dayMask, String rule, boolean actuatorStatus, int priority, boolean scheduleEnabled) throws IllegalArgumentException;
	String createSpecialSchedule(String sScheduleName, String actuatorName, int dayMask, String rule, boolean actuatorStatus, int priority, boolean scheduleEnabled) throws IllegalArgumentException;
	String actuatorSetStatus(String actuator, String status) throws IllegalArgumentException;
}
