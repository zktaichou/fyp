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
	String[][] getSiteControllerList(String siteName) throws IllegalArgumentException;
}
