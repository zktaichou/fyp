package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String [][] greetServer(String sn, Date sd, Date ed) throws IllegalArgumentException;
	boolean userLogin(String username, String password) throws IllegalArgumentException;
	String[][] getSiteList() throws IllegalArgumentException;
	String[][] getControllerList(String siteName) throws IllegalArgumentException;
	String[][] getSensorList(String controllerName) throws IllegalArgumentException;
	String[][] getActuatorList(String controllerName) throws IllegalArgumentException;
}
