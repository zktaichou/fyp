package com.google.gwt.sample.stockwatcher.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.crypto.SealedObject;

import com.google.gwt.sample.stockwatcher.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	
	public String[][] greetServer(String sn, Date sd, Date ed, Boolean predictionIsEnabled) throws IllegalArgumentException {
		try {
			Socket sc=new Socket(Utility.serverIP,getNewPortNumber());
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("37");
			toSend.add(sn);
			toSend.add(Utility.dateToLocalDateTime(sd));
			toSend.add(Utility.dateToLocalDateTime(ed));
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			oos.writeObject(Utility.encryptMsg(toSend));
			
			@SuppressWarnings("unchecked")
			ArrayList<Object []> data=Utility.decryptData(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			String[][] result = Utility.DataToString(data);
			
			if(predictionIsEnabled)
			{
				result = Weka.predict(result);
			}
			
			return result;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.requestError)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			
			return null;}
	}
	
	public boolean userLogin(String username, String password) throws IllegalArgumentException {
		try {
			
			String hashedPassword = Utility.hashSHA1CharAry(password);
			Socket sc=new Socket(Utility.serverIP,getNewPortNumber());
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("2");
			toSend.add(username);
			toSend.add(hashedPassword);
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			@SuppressWarnings("unchecked")
			String data=Utility.decryptLogin(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data.equals("ACTIVATED");
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userLoginError)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return false;}
	}
	
	public String[][] getSiteList() throws IllegalArgumentException {
		try {
			Socket sc=new Socket(Utility.serverIP,getNewPortNumber());
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("3");
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			@SuppressWarnings("unchecked")
			ArrayList<Object []> data=Utility.decryptData(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getSiteList)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getControllerList(String siteName) throws IllegalArgumentException {
		try {
			Socket sc=new Socket(Utility.serverIP,getNewPortNumber());
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("4");
			toSend.add(siteName);
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			@SuppressWarnings("unchecked")
			ArrayList<Object []> data=Utility.decryptData(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getControllerList)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getSensorList(String controllerName) throws IllegalArgumentException {
		try {
			Socket sc=new Socket(Utility.serverIP,getNewPortNumber());
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("5");
			toSend.add(controllerName);
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			@SuppressWarnings("unchecked")
			ArrayList<Object []> data=Utility.decryptData(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getSensorList)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getActuatorList(String controllerName) throws IllegalArgumentException {
		try {
			Socket sc=new Socket(Utility.serverIP,getNewPortNumber());
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("6");
			toSend.add(controllerName);
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			@SuppressWarnings("unchecked")
			ArrayList<Object []> data=Utility.decryptData(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();

			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getActuatorList)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
		}
		return null;
	}
	
	public String actuatorSetStatus(String actuator, String status) throws IllegalArgumentException {
		try {
			Socket sc=new Socket(Utility.serverIP,getNewPortNumber());
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("45");
			toSend.add(actuator);
			toSend.add(status);
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			@SuppressWarnings("unchecked")
			String data=Utility.decryptString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.actuatorSetStatus)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public int getNewPortNumber() throws IllegalArgumentException {
		return Utility.portNumber;
	}
	
}
