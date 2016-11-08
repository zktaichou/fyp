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

import com.google.gwt.sample.stockwatcher.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public boolean userLogin(String username, String password) throws IllegalArgumentException {
		try {
			String hashedPassword = Utility.hashSHA1CharAry(password);
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("2");
			toSend.add(username);
			toSend.add(hashedPassword);
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
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
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("3");
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object []> data=Utility.decryptToObjectArray(ois);
			
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
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("4");
			toSend.add(siteName);
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object []> data=Utility.decryptToObjectArray(ois);
			
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
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("5");
			toSend.add(controllerName);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object []> data=Utility.decryptToObjectArray(ois);
			
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
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("6");
			toSend.add(controllerName);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object []> data=Utility.decryptToObjectArray(ois);
			
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
	
	public String[][] getDayScheduleRuleAll() throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("7");
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object []> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getDayScheduleRuleAll)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getDayScheduleRuleByName(String rulename) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("8");
			toSend.add(rulename);
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object []> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getDayScheduleRuleByName)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String updateDayScheduleRule(String rOldName, String rNewName, int sH, int sM, int eH, int eM) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("9a");
			toSend.add(rOldName);
			toSend.add(rNewName);
			toSend.add(sH);
			toSend.add(sM);
			toSend.add(eH);
			toSend.add(eM);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.updateDayScheduleRule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String deleteDayScheduleRule(String rName) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("9b");
			toSend.add(rName);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.deleteDayScheduleRule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String createDayScheduleRule(String rName, int sH, int sM, int eH, int eM) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("10");
			toSend.add(rName);
			toSend.add(sH);
			toSend.add(sM);
			toSend.add(eH);
			toSend.add(eM);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.createDayScheduleRule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getActuatorRegularSchedule(String aName) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("11");
			toSend.add(aName);
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			ArrayList<Object[]> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getActuatorRegularSchedule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getActuatorSpecialSchedule(String aName) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("12");
			toSend.add(aName);
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			ArrayList<Object[]> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getActuatorSpecialSchedule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getRegularSchedules() throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("13");

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			ArrayList<Object[]> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getRegularSchedules)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getRegularScheduleByName(String rScheduleName) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("14");
			toSend.add(rScheduleName);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			ArrayList<Object[]> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getRegularScheduleByName)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String createRegularSchedule(String rScheduleName, String actuatorName, int dayMask, String rule, String onStart, String onEnd, boolean lock, int priority, boolean actuatorEnabled) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("22a");
			toSend.add(rScheduleName);
			toSend.add(actuatorName);
			toSend.add(dayMask);
			toSend.add(rule);
			toSend.add(onStart);
			toSend.add(onEnd);
			toSend.add(lock);
			toSend.add(priority);
			toSend.add(actuatorEnabled);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.createRegularSchedule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String deleteRegularSchedule(String rScheduleName) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("22b");
			toSend.add(rScheduleName);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.deleteRegularSchedule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String updateRegularSchedule(String rScheduleOldName, String rScheduleNewName,String actuatorName, int dayMask, String rule, String onStart, String onEnd, boolean lock, int priority, boolean actuatorEnabled) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("22c");
			toSend.add(rScheduleOldName);
			toSend.add(rScheduleNewName);
			toSend.add(actuatorName);
			toSend.add(dayMask);
			toSend.add(rule);
			toSend.add(onStart);
			toSend.add(onEnd);
			toSend.add(lock);
			toSend.add(priority);
			toSend.add(actuatorEnabled);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.updateRegularSchedule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getSpecialSchedules() throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("23");

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object[]> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getSpecialSchedules)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getSpecialScheduleByName(String sScheduleName) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("24");
			toSend.add(sScheduleName);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object[]> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getSpecialScheduleByName)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String createSpecialSchedule(String sScheduleName, String actuatorName, int year, int month, int day, String rule, String onStart, String onEnd, boolean lock, int priority, boolean actuatorEnabled) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("32a");
			toSend.add(sScheduleName);
			toSend.add(actuatorName);
			toSend.add(year);
			toSend.add(month);
			toSend.add(day);
			toSend.add(rule);
			toSend.add(onStart);
			toSend.add(onEnd);
			toSend.add(lock);
			toSend.add(priority);
			toSend.add(actuatorEnabled);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.createSpecialSchedule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String deleteSpecialSchedule(String sScheduleName) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("32b");
			toSend.add(sScheduleName);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.deleteSpecialSchedule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String updateSpecialSchedule(String sScheduleOldName, String sScheduleNewName,String actuatorName, int year, int month, int day, String rule, String onStart, String onEnd, boolean lock, int priority, boolean actuatorEnabled) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("32c");
			toSend.add(sScheduleOldName);
			toSend.add(sScheduleNewName);
			toSend.add(actuatorName);
			toSend.add(year);
			toSend.add(month);
			toSend.add(day);
			toSend.add(rule);
			toSend.add(onStart);
			toSend.add(onEnd);
			toSend.add(lock);
			toSend.add(priority);
			toSend.add(actuatorEnabled);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.updateSpecialSchedule)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] getOngoingSchedulesAll() throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("34");

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			ArrayList<Object[]> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			is.close();
			
			sc.close();
			
			return Utility.DataToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.getOngoingSchedulesAll)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] greetServer(String sn, Date sd, Date ed, Boolean predictionIsEnabled) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("37");
			toSend.add(sn);
			toSend.add(Utility.dateToLocalDateTime(sd));
			toSend.add(Utility.dateToLocalDateTime(ed));
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			oos.writeObject(Utility.encryptMsg(toSend));
			
			 
			ArrayList<Object []> data=Utility.decryptToObjectArray(ois);
			
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
	
	public String actuatorSetStatus(String actuator, String status) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("45a");
			toSend.add(actuator);
			toSend.add(status);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
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
	
	public String actuatorSetControlType(String actuator, String controlType) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("45b");
			toSend.add(actuator);
			toSend.add(controlType);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.actuatorSetControlType)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String[][] sensorActuatorResponseGetAll() throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("46");
			
			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			oos.writeObject(Utility.encryptMsg(toSend));
			
			 
			ArrayList<Object []> data=Utility.decryptToObjectArray(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			String[][] result = Utility.DataToString(data);
			
			return result;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.sensorActuatorResponseGetAll)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			
			return null;}
	}
	
	public String sensorActuatorResponseCreate(String actuator, String onTrigger, String onNotTrigger, String expression, boolean enabled, int timeout) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("48a");
			toSend.add(actuator);
			toSend.add(onTrigger);
			toSend.add(onNotTrigger);
			toSend.add(expression);
			toSend.add(enabled);
			toSend.add(timeout);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.sensorActuatorResponseCreate)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String sensorActuatorResponseUpdate(int id, String actuator, String onTrigger, String onNotTrigger, String expression, boolean enabled, int timeout) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("48b");
			toSend.add(id);
			toSend.add(actuator);
			toSend.add(onTrigger);
			toSend.add(onNotTrigger);
			toSend.add(expression);
			toSend.add(enabled);
			toSend.add(timeout);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.sensorActuatorResponseUpdate)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String sensorActuatorResponseDelete(int id) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("48c");
			toSend.add(id);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.sensorActuatorResponseDelete)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userSubscribeControllerNotification(String user, String controller) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("54");
			toSend.add(user);
			toSend.add(controller);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userSubscribeControllerNotification)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userUpdateControllerNotificationLastReadTime(String user, String controller, Date date) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("55");
			toSend.add(user);
			toSend.add(controller);
			toSend.add(Utility.dateToLocalDateTime(date));

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userUpdateControllerNotificationLastReadTime)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userUnsubscribeControllerNotification(String user, String controller) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("56");
			toSend.add(user);
			toSend.add(controller);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userUnsubscribeControllerNotification)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userGetControllerNotificationLastReadTime(String user, String controller) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("57");
			toSend.add(user);
			toSend.add(controller);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			LocalDateTime data=Utility.decryptToLocalDateTime(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.LocalDateTimeToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userGetControllerNotificationLastReadTime)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userSubscribeSensorNotification(String user, String sensor) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("58");
			toSend.add(user);
			toSend.add(sensor);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userSubscribeSensorNotification)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userUpdateSensorNotificationLastReadTime(String user, String sensor, Date date) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("59");
			toSend.add(user);
			toSend.add(sensor);
			toSend.add(Utility.dateToLocalDateTime(date));

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userUpdateSensorNotificationLastReadTime)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userUnsubscribeSensorNotification(String user, String sensor) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("60");
			toSend.add(user);
			toSend.add(sensor);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userUnsubscribeSensorNotification)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userGetSensorNotificationLastReadTime(String user, String sensor) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("61");
			toSend.add(user);
			toSend.add(sensor);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			LocalDateTime data=Utility.decryptToLocalDateTime(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.LocalDateTimeToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userGetSensorNotificationLastReadTime)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userSubscribeActuatorNotification(String user, String actuator) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("62");
			toSend.add(user);
			toSend.add(actuator);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userSubscribeActuatorNotification)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userUpdateActuatorNotificationLastReadTime(String user, String actuator, Date date) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("63");
			toSend.add(user);
			toSend.add(actuator);
			toSend.add(Utility.dateToLocalDateTime(date));

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userUpdateActuatorNotificationLastReadTime)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userUnsubscribeActuatorNotification(String user, String actuator) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("64");
			toSend.add(user);
			toSend.add(actuator);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			String data=Utility.decryptToString(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data;
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userUnsubscribeActuatorNotification)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	public String userGetActuatorNotificationLastReadTime(String user, String actuator) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("65");
			toSend.add(user);
			toSend.add(actuator);

			Socket sc=new Socket(Utility.serverIP,getPortNumber());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(Utility.encryptMsg(toSend));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			
			//Special decrypt for String-based returned response
			 
			LocalDateTime data=Utility.decryptToLocalDateTime(ois);
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return Utility.LocalDateTimeToString(data);
		} catch (Exception e) {
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile.userGetActuatorNotificationLastReadTime)));
				e.printStackTrace(pw);
				pw.close();
			} catch (Exception f) {}
			return null;}
	}
	
	
	public int getPortNumber() throws IllegalArgumentException {
		return Utility.portNumber;
	}
	
}
