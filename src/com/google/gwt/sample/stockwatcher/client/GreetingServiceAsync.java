package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String sn, Date sd, Date ed, AsyncCallback<String[][]> callback) throws IllegalArgumentException;
	void userLogin(String username, String password, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
}
