package com.google.gwt.sample.stockwatcher.server;

import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.stockwatcher.client.GreetingService;
import com.google.gwt.sample.stockwatcher.client.GreetingServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class Utility{
	
	//the request queue
	private List queuedRequests = new ArrayList(); 

	//tracking to provide serial behavior 
	private boolean insideRequest = false;
	
	private List args = new ArrayList();
	private String method;
	private String service;
	private AsyncCallback callback;
	
//	public void executeNextRPC() {
//	    if (insideRequest || queuedRequests.isEmpty()) {
//	        //if we're in the middle of a request or the queue is empty
//	        return;
//	    }
//	    insideRequest = true;
//	    QueuedRPCRequest request = queuedRequests.remove(queuedRequests.size() - 1);
//	    if (request.getService().equals(QueuedRPCRequest.LOGIN_SERVICE)) {
//	        executeLoginService(request);
//	    } 
//	} 
//
//	public void addRequestToQueue(QueuedRPCRequest request) {
//	    queuedRequests.add(request);
//	    executeNextRPC();
//	} 
//
//	private void executeLoginService(QueuedRPCRequest request) {
//	    LoginServiceGWTAsync loginService =
//	                              (LoginServiceGWTAsync) GWT.create(LoginServiceGWT.class);
//	    ((ServiceDefTarget) loginService).setRpcRequestBuilder(
//	                    new RpcRequestBuilderNowWithTimeout());
//	    if (request.getMethod().equals(QueuedRPCRequest.LOGIN)) {
//	        loginService.login((String) request.getArgs().get(0),
//	                    (String) request.getArgs().get(1), request.getCallback());
//	    }
//	}
	
	static int portNumber = 40001;
	static String serverIP = "10.100.2.56";
	static String fsktmServerIP = "103.18.2.222";
	
	public static String hashSHA1CharAry (String c) {
    	try {
    		MessageDigest md=MessageDigest.getInstance("SHA-1");
    		md.reset();
    		md.update(c.getBytes());
    		return new String(md.digest());
    	} catch (Exception e) {}
    	return "";
    }
	
	public static SecretKey getKey(){
		try {
			final byte[] salt = "chichilol".getBytes();
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec("chi-admin".toCharArray(), salt, 1024, 128);
			SecretKey secret=new SecretKeySpec(factory.generateSecret(spec).getEncoded(),"AES");
		
			return secret;
		} catch (Exception e) {return null;}
	}

	public static SealedObject encryptMsg(ArrayList<Object> msg){
		try {
			Cipher encrypter=Cipher.getInstance("AES");
	        encrypter.init(Cipher.ENCRYPT_MODE,getKey());
			return new SealedObject(msg,encrypter);
		} catch (Exception e) {return null;}
	}
	
	public static String decryptMsg(ObjectInputStream ois){
		try {
	        Cipher decrypter=Cipher.getInstance("AES");
	        decrypter.init(Cipher.DECRYPT_MODE,getKey());
	        
			return (String)(((SealedObject) ois.readObject()).getObject(decrypter));
		} catch (Exception e) {return null;}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Object []> decryptData(ObjectInputStream ois){
		try {
	        Cipher decrypter=Cipher.getInstance("AES");
	        decrypter.init(Cipher.DECRYPT_MODE,getKey());
	        
			return (ArrayList<Object []>)(((SealedObject) ois.readObject()).getObject(decrypter));
		} catch (Exception e) {return null;}
	}
	
	public static String[][] DataToString(ArrayList<Object []> input){
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String [][] data = new String[input.size()][input.get(0).length];

			for(int i=0;i<input.size();i++)
			{
				for (int i2=0;i2<input.get(i).length;i2++) {
					if (input.get(i)[i2] instanceof LocalDateTime) {
				    	data[i][i2]=df.format(((LocalDateTime)input.get(i)[i2]).plusHours(8));
					} else {
				    	data[i][i2]=input.get(i)[i2].toString();
					}
				}
			}
			
			return data;
		} catch (Exception e) {return null;}
	}
	
	public static long localDateTimeToLong (LocalDateTime dt) {
    	return dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    public static java.sql.Date localDateTimeToSQLDate (LocalDateTime dt) {
        return new java.sql.Date(localDateTimeToLong(dt));
    }
    
    public static java.util.Date localDateTimeToUtilDate (LocalDateTime dt) {
        return new java.util.Date(localDateTimeToLong(dt));
    }
    
    public static LocalDateTime dateToLocalDateTime (java.sql.Date d) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(d.getTime()), ZoneOffset.systemDefault());
    }
    
    public static LocalDateTime dateToLocalDateTime (java.util.Date d) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(d.getTime()), ZoneOffset.systemDefault());
    }
	
}