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
import java.util.ArrayList;

import com.google.gwt.sample.stockwatcher.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	
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
	
	public String createSpecialSchedule(String sScheduleName, String actuatorName, int dayMask, String rule, String onStart, String onEnd, boolean lock, int priority, boolean actuatorEnabled) throws IllegalArgumentException {
		try {
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("32a");
			toSend.add(sScheduleName);
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
			toSend.add("45");
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
	
	public int getPortNumber() throws IllegalArgumentException {
		return Utility.portNumber;
	}
	
}
