package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;
import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NotificationServer{
	static PopupPanel notificationPopup= new PopupPanel();
	
	static VerticalPanel cPanel = new VerticalPanel();
	static VerticalPanel sPanel = new VerticalPanel();
	static VerticalPanel aPanel = new VerticalPanel();
	
	static FlexTable cTable = new FlexTable();
	static FlexTable sTable = new FlexTable();
	static FlexTable aTable = new FlexTable();
	
	static boolean cFlag = false;
	static boolean sFlag = false;
	static boolean aFlag = false;
	
	static boolean isRead = true;
	
	static Button closeButton = new Button("Close");
	
	static Timer t = new Timer() {
	      @Override
	      public void run() {
	    	  isThereNotification();
	    	  getUpdates();
	      }
	    };
	
	public static void start(){
		setHandlers();
		resetNotificationTables();
  	  	getUpdates();
		
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setSpacing(10);
		wrapper.add(cTable);
		wrapper.add(sTable);
		wrapper.add(aTable);
  	  	
  	  	VerticalPanel lol = new VerticalPanel();
  	  	lol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
  	  	lol.setSpacing(10);
  	  	lol.add(new HTML("Notifications"));
  	  	lol.add(wrapper);
		
		notificationPopup.clear();
		notificationPopup.setVisible(true);
		notificationPopup.add(lol);
		notificationPopup.show();
		notificationPopup.center();
		notificationPopup.setVisible(false);

		
		t.scheduleRepeating(10000);
	}
	
	public static void stop(){
		notificationPopup.setVisible(false);
		t.cancel();
	}
	
	public static void isThereNotification(){
		if(cFlag && sFlag && aFlag)
		{
			Menu.notificationAnchor.setVisible(true);
		}
		
	}
	
	public static void resetNotificationTables(){
		cTable = new FlexTable();
		sTable = new FlexTable();
		aTable = new FlexTable();
	}
	
	private static void getUpdates(){
		getSubscribedController(Data.currentUser);
		getSubscribedSensor(Data.currentUser);
		getSubscribedActuator(Data.currentUser);
	}
	
	private static void setHandlers(){
		closeButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				notificationPopup.setVisible(false);
				};
			});
	}
	
	private static void getSubscribedController(String user){
		cFlag=false;
		Utility.newRequestObj().userGetSubscribedControllers(user, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get "+Data.currentUser+"'s controller subscription");
			}
 
			public void onSuccess(String[][] reply) {
				if(reply!=null)
				{
					Date start = new Date(System.currentTimeMillis());
					Date now = new Date(System.currentTimeMillis());
					
					Data.subscribedControllerList.clear();
					for(int i=0;i<reply.length;i++)
					{
						Data.subscribedControllerList.add(reply[i][1]);
						
						if(start.after(new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime())))
						start = new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime());
					}
					getControllerEventBetweenTime(Data.subscribedControllerList,start,now);
				}
			}
		});
	}
	
	private static void getSubscribedSensor(String user){
		sFlag=false;
		Utility.newRequestObj().userGetSubscribedSensors(user, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get "+Data.currentUser+"'s sensor subscription");
			} 
 
			public void onSuccess(String[][] reply) {
				if(reply!=null)
				{
					Date start = new Date(System.currentTimeMillis());
					Date now = new Date(System.currentTimeMillis());
					
					Data.subscribedSensorList.clear();
					for(int i=0;i<reply.length;i++)
					{
						Data.subscribedSensorList.add(reply[i][1]);
						
						if(start.after(new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime())))
						start = new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime());
					}
					getSensorEventBetweenTime(Data.subscribedSensorList,start,now);
				}
			}
		});
	}
	
	private static void getSubscribedActuator(String user){
		aFlag=false;
		Utility.newRequestObj().userGetSubscribedActuators(user, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get "+Data.currentUser+"'s actuator subscription");
			} 
 
			public void onSuccess(String[][] reply) {
				if(reply!=null)
				{
					Date start = new Date(System.currentTimeMillis());
					Date now = new Date(System.currentTimeMillis());
					
					Data.subscribedActuatorList.clear();
					for(int i=0;i<reply.length;i++)
					{
						Data.subscribedActuatorList.add(reply[i][1]);

						if(start.after(new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime())))
						start = new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime());
					}
					getActuatorEventBetweenTime(Data.subscribedActuatorList,start,now);
				}
			}
		});
	}
	
	private static void getControllerEventBetweenTime(ArrayList<String> controllerList, Date start, Date end){
		Utility.newRequestObj().controllerEventGetBetweenTime(controllerList, start, end, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get controller events between time");
			} 
 
			public void onSuccess(String[][] reply) {
				cFlag=true;
				if(reply.length!=0)
				cTable=ChartUtilities.appendFlexTable(cTable, reply);
				Window.alert("c done");
//				for(int i=0; i<reply.length;i++){
//					updateControllerLastReadTime(Data.currentUser, reply[i][0], reply[i][1]);
//				}
			}
		});
	}
	
	private static void getSensorEventBetweenTime(ArrayList<String> sensorList, Date start, Date end){
		Utility.newRequestObj().sensorEventGetBetweenTime(sensorList, start, end, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get sensor events between time");
			}
 
			public void onSuccess(String[][] reply) {
				sFlag=true;
				if(reply.length!=0)
				sTable=ChartUtilities.appendFlexTable(sTable, reply);
//				for(int i=0; i<reply.length;i++){
//					updateSensorLastReadTime(Data.currentUser, reply[i][0], reply[i][1]);
//				}
				Window.alert("s done");
			}
		});
	}
	
	private static void getActuatorEventBetweenTime(ArrayList<String> actuatorList, Date start, Date end){
		Utility.newRequestObj().actuatorEventGetBetweenTime(actuatorList, start, end, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get actuator events between time");
			} 
 
			public void onSuccess(String[][] reply) {
				aFlag=true;
				if(reply.length!=0)
				aTable=ChartUtilities.appendFlexTable(aTable, reply);
//				for(int i=0; i<reply.length;i++){
//					updateActuatorLastReadTime(Data.currentUser, reply[i][0], reply[i][1]);
//				}
				Window.alert("a done");
			}
		});
	}
	
	private static void updateControllerLastReadTime(final String user, final String controller, final String date){
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, controller, new java.sql.Date(ChartUtilities.dateTimeFormat.parse(date).getTime()), new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update controller last read time");
			} 
 
			public void onSuccess(String reply) {

			}
		});
	}
	
	private static void updateSensorLastReadTime(final String user, final String sensor, final String date){
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, sensor, new java.sql.Date(ChartUtilities.dateTimeFormat.parse(date).getTime()), new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update sensor last read time");
			} 
 
			public void onSuccess(String reply) {

			}
		});
	}
	
	private static void updateActuatorLastReadTime(final String user, final String actuator, final String date){
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, actuator, new java.sql.Date(ChartUtilities.dateTimeFormat.parse(date).getTime()), new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update actuator last read time");
			} 
 
			public void onSuccess(String reply) {

			}
		});
	}
}
