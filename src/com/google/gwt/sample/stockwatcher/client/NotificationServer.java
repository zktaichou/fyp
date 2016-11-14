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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NotificationServer{
	static ArrayList<PopupPanel> popupList = new ArrayList<>();
	
	static PopupPanel cPopup= new PopupPanel();
	static PopupPanel sPopup= new PopupPanel();
	static PopupPanel aPopup= new PopupPanel();
	
	static VerticalPanel cPanel = new VerticalPanel();
	static VerticalPanel sPanel = new VerticalPanel();
	static VerticalPanel aPanel = new VerticalPanel();
	
	static ScrollPanel cScrollpanel = new ScrollPanel();
	static ScrollPanel sScrollpanel = new ScrollPanel();
	static ScrollPanel aScrollpanel = new ScrollPanel();
	
	static FlexTable cTable = new FlexTable();
	static FlexTable sTable = new FlexTable();
	static FlexTable aTable = new FlexTable();
	
	static boolean cFlag = false;
	static boolean sFlag = false;
	static boolean aFlag = false;

	static boolean cFirstRequest = true;
	static boolean sFirstRequest = true;
	static boolean aFirstRequest = true;
	
	static Date cLastSent;
	static Date sLastSent;
	static Date aLastSent;
	
	static Button cCloseButton = new Button("Close");
	static Button sCloseButton = new Button("Close");
	static Button aCloseButton = new Button("Close");
	
	static Timer t = new Timer() {
	      @Override
	      public void run() {
	    	  isThereNotification();
	    	  getUpdates();
	      }
	    };
	
	public static void start(){
		cFirstRequest = true;
		sFirstRequest = true;
		aFirstRequest = true;
		
		setHandlers();
		resetNotificationTables();
  	  	getUpdates();

  	  	popupList.clear();
  	  	renderPopup("Controller Notifications",cPopup,cScrollpanel,cTable,cCloseButton);
  	  	renderPopup("Sensor Notifications",sPopup,sScrollpanel,sTable,sCloseButton);
  	  	renderPopup("Actuator Notifications",aPopup,aScrollpanel,aTable,aCloseButton);

		t.scheduleRepeating(10000);
	}
	
	public static void stop(){
		hideAllPopup();
		t.cancel();
	}
	
	private static void renderPopup(String title, PopupPanel popup,ScrollPanel scrollPanel, FlexTable table, Button closeButton){
		scrollPanel.clear();
		scrollPanel.setHeight("400px");
		scrollPanel.add(table);
		
  	  	VerticalPanel lol = new VerticalPanel();
  	  	lol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
  	  	lol.setSpacing(10);
  	  	lol.add(new HTML(title));
  	  	lol.add(scrollPanel);
  	  	lol.add(closeButton);
  	  
		popup.clear();
		popup.setVisible(true);
		popup.add(lol);
		popup.show();
		popup.center();
		popup.setVisible(false);
		
  	  	popupList.add(popup);
	}
	
	private static void hideAllPopup(){
		for(PopupPanel popup: popupList){
			popup.setVisible(false);
		}
	}
	
	private static void setHeaderC(FlexTable ft){
		String[] header = {"Controller","Timestamp","Event Type","Notification Info"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}
	
	private static void setHeaderS(FlexTable ft){
		String[] header = {"Sensor","Timestamp","Notification Info","Value"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}
	
	private static void setHeaderA(FlexTable ft){
		String[] header = {"Actuator","Timestamp","Event Type","Notification Info"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
		
	}
	
	public static void isThereNotification(){
		if(!cPopup.isVisible())
		{
			if(cFlag){
				Menu.cNotificationAnchor.setVisible(true);
			}
		}
		if(!sPopup.isVisible())
		{
			if(sFlag){
				Menu.sNotificationAnchor.setVisible(true);
			}
		}
		if(!aPopup.isVisible())
		{
			if(aFlag){
				Menu.aNotificationAnchor.setVisible(true);
			}
		}
	}
	
	public static void resetNotificationTables(){
		cTable = new FlexTable();
		sTable = new FlexTable();
		aTable = new FlexTable();
		
		setHeaderC(cTable);
		setHeaderS(sTable);
		setHeaderA(aTable);
	}
	
	private static void getUpdates(){
		getSubscribedController(Data.currentUser);
		getSubscribedSensor(Data.currentUser);
		getSubscribedActuator(Data.currentUser);
	}
	
	private static void setHandlers(){
		cCloseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				setHeaderC(cTable);
				cPopup.setVisible(false);
				};
			});
		sCloseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				setHeaderS(sTable);
				sPopup.setVisible(false);
				};
			});
		aCloseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				setHeaderA(aTable);
				aPopup.setVisible(false);
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

						if(cFirstRequest)
						{
							if(start.after(new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime())))
							start = new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime());
						}
					}

					if(!cFirstRequest)
					{
						start = cLastSent;
					}
					
					cFirstRequest=false;
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
						
						if(sFirstRequest)
						{
							if(start.after(new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime())))
							start = new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime());
						}
					}

					if(!sFirstRequest)
					{
						start = sLastSent;
					}
					
					sFirstRequest=false;
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

						if(aFirstRequest)
						{
							if(start.after(new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime())))
							start = new java.sql.Date(ChartUtilities.dateTimeFormat.parse(reply[i][2]).getTime());
						}
					}

					if(!aFirstRequest)
					{
						start = aLastSent;
					}
					
					aFirstRequest=false;
					getActuatorEventBetweenTime(Data.subscribedActuatorList,start,now);
				}
			}
		});
	}
	
	private static void getControllerEventBetweenTime(ArrayList<String> controllerList, Date start, Date end){
		cLastSent=new Date(System.currentTimeMillis()+1);
		Utility.newRequestObj().controllerEventGetBetweenTime(controllerList, start, end, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get controller events between time");
			} 
 
			public void onSuccess(String[][] reply) {
				if(reply.length!=0)
				{
					cFlag=true;
					cTable=ChartUtilities.appendFlexTable(cTable, reply);
				}
				if(cPopup.isVisible())
				{
					for(int i=0; i<Data.subscribedControllerList.size(); i++)
					{
						updateControllerLastReadTime(Data.currentUser, Data.subscribedControllerList.get(i));
					}
				}
			}
		});
	}
	
	private static void getSensorEventBetweenTime(ArrayList<String> sensorList, Date start, Date end){
		sLastSent=new Date(System.currentTimeMillis()+1);
		Utility.newRequestObj().sensorEventGetBetweenTime(sensorList, start, end, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get sensor events between time");
			}
 
			public void onSuccess(String[][] reply) {
				if(reply.length!=0)
				{
					sFlag=true;
					sTable=ChartUtilities.appendFlexTable(sTable, reply);
				}
				if(sPopup.isVisible())
				{
					for(int i=0; i<Data.subscribedSensorList.size(); i++)
					{
						updateSensorLastReadTime(Data.currentUser, Data.subscribedSensorList.get(i));
					}
				}
			}
		});
	}
	
	private static void getActuatorEventBetweenTime(ArrayList<String> actuatorList, Date start, Date end){
		aLastSent=new Date(System.currentTimeMillis()+1);
		Utility.newRequestObj().actuatorEventGetBetweenTime(actuatorList, start, end, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get actuator events between time");
			} 
 
			public void onSuccess(String[][] reply) {
				if(reply.length!=0)
				{
					aFlag=true;
					aTable=ChartUtilities.appendFlexTable(aTable, reply);
				}
				if(aPopup.isVisible())
				{
					for(int i=0; i<Data.subscribedActuatorList.size(); i++)
					{
						updateActuatorLastReadTime(Data.currentUser, Data.subscribedSensorList.get(i));
					}
				}
			}
		});
	}
	
	public static void updateControllerLastReadTime(final String user, final String controller){
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, controller, new Date(System.currentTimeMillis()), new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to update controller last read time");
			} 
 
			public void onSuccess(String reply) {
//				Window.alert("updated controller last read time!");
			}
		});
	}
	
	public static void updateSensorLastReadTime(final String user, final String sensor){
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, sensor, new Date(System.currentTimeMillis()), new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to update sensor last read time");
			} 
 
			public void onSuccess(String reply) {

			}
		});
	}
	
	public static void updateActuatorLastReadTime(final String user, final String actuator){
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, actuator, new Date(System.currentTimeMillis()), new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to update actuator last read time");
			} 
 
			public void onSuccess(String reply) {

			}
		});
	}
}
