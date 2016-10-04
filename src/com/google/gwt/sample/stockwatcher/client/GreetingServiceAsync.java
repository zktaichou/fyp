package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String sn, Date sd, Date ed, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void userLogin(String username, String password, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void getSiteList(AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getControllerList(String siteName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getSensorList(String controllerName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void getActuatorList(String controllerName, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void actuatorSetStatus(String status, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
}
