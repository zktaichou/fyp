package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserNotificationPage extends Composite{
	
	ContentPanel cPanel = new ContentPanel();
	
	HorizontalPanel subscriptionPanel = new HorizontalPanel();
	
	VerticalPanel controllerSubscribedPanel = new VerticalPanel();
	VerticalPanel sensorSubscribedPanel = new VerticalPanel();
	VerticalPanel actuatorSubscribedPanel = new VerticalPanel();

	HashMap<String, String> controllerSubcriptionList = new HashMap<>();
	HashMap<String, String> sensorSubcriptionList = new HashMap<>();
	HashMap<String, String> actuatorSubcriptionList = new HashMap<>();
	
	ListBox controllerLB = new ListBox();
	ListBox sensorLB = new ListBox();
	ListBox actuatorLB = new ListBox();
	
	Button subscribeControllerButton = new Button("Subscribe Controller");
	Button subscribeSensorButton = new Button("Subscribe Sensor");
	Button subscribeActuatorButton = new Button("Subscribe Actuator");
	
	Button cancelButton = new Button("Cancel");
	Button closeButton = new Button("Close");
	
	Button okSubscribeControllerButton = new Button("OK");
	Button okSubscribeSensorButton = new Button("OK");
	Button okSubscribeActuatorButton = new Button("OK");
	
	Button okUnsubscribeControllerButton = new Button("Unsubscribe");
	Button okUnsubscribeSensorButton = new Button("Unsubscribe");
	Button okUnsubscribeActuatorButton = new Button("Unsubscribe");
	
	PopupPanel subscribeSelectionPopup = new PopupPanel();
	
	FlexTable cTable = new FlexTable();
	FlexTable sTable = new FlexTable();
	FlexTable aTable = new FlexTable();
	
	String user;
	String noSubscription = "No subscription";
	
	public UserNotificationPage(String user){
		Header.setHeaderTitle("Main Menu > Settings > User Notification Subscription");
		this.user=user;
		
		getUserSubscription(this.user);
		setHandlers();
		
		String message = "Welcome "+Data.currentUser;
		
		subscriptionPanel.clear();
		subscriptionPanel.setStyleName("fancyTable");
		subscriptionPanel.setSpacing(20);
		subscriptionPanel.add(controllerSubscribedPanel);
		subscriptionPanel.add(sensorSubscribedPanel);
		subscriptionPanel.add(actuatorSubscribedPanel);
			
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.add(new HTML("<h1>"+message+"!</h1></br>"));
		wrapper.add(subscriptionPanel);
			
		cPanel.clear();
		cPanel.add(wrapper);
		cPanel.alignMiddlePanelVTop();

		initWidget(cPanel); 
	}
	
	private void setHandlers(){
		cancelButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				  subscribeSelectionPopup.setVisible(false);
			  }
		});
		closeButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				  subscribeSelectionPopup.setVisible(false);
			  }
		});
		
		subscribeControllerButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				  HorizontalPanel buttonPanel = new HorizontalPanel();
				  buttonPanel.setSpacing(10);

				  VerticalPanel wrapper = new VerticalPanel();
				  wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				  wrapper.setSpacing(10);
				  wrapper.add(new HTML("<h2>Controller Subscription</h2></br>"));
				  
				  if(controllerLB.getItemCount()==0)
				  {
					  wrapper.add(new HTML("No controller left to subscribe!"));
					  
					  buttonPanel.add(closeButton);
				  }
				  else
				  {
					  buttonPanel.add(cancelButton);
					  buttonPanel.add(okSubscribeControllerButton);
					  
					  wrapper.add(controllerLB);
				  }
				  wrapper.add(buttonPanel);
				  
				  subscribeSelectionPopup.clear();
				  subscribeSelectionPopup.add(wrapper);
				  subscribeSelectionPopup.setVisible(true);
				  subscribeSelectionPopup.center();
			  }
		});
		
		subscribeSensorButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				  HorizontalPanel buttonPanel = new HorizontalPanel();
				  buttonPanel.setSpacing(10);

				  VerticalPanel wrapper = new VerticalPanel();
				  wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				  wrapper.setSpacing(10);
				  wrapper.add(new HTML("<h2>Sensor Subscription</h2></br>"));
				  
				  if(sensorLB.getItemCount()==0)
				  {
					  wrapper.add(new HTML("No sensor left to subscribe!"));
					  
					  buttonPanel.add(closeButton);
				  }
				  else
				  {
					  buttonPanel.add(cancelButton);
					  buttonPanel.add(okSubscribeSensorButton);
					  
					  wrapper.add(sensorLB);
				  }
				  wrapper.add(buttonPanel);
				  
				  subscribeSelectionPopup.clear();
				  subscribeSelectionPopup.add(wrapper);
				  subscribeSelectionPopup.setVisible(true);
				  subscribeSelectionPopup.center();
			  }
		});
		
		subscribeActuatorButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				  HorizontalPanel buttonPanel = new HorizontalPanel();
				  buttonPanel.setSpacing(10);

				  VerticalPanel wrapper = new VerticalPanel();
				  wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				  wrapper.setSpacing(10);
				  wrapper.add(new HTML("<h2>Actuator Subscription</h2></br>"));
				  
				  if(actuatorLB.getItemCount()==0)
				  {
					  wrapper.add(new HTML("No actuator left to subscribe!"));
					  
					  buttonPanel.add(closeButton);
				  }
				  else
				  {
					  buttonPanel.add(cancelButton);
					  buttonPanel.add(okSubscribeActuatorButton);
					  
					  wrapper.add(actuatorLB);
				  }
				  wrapper.add(buttonPanel);
				  
				  subscribeSelectionPopup.clear();
				  subscribeSelectionPopup.add(wrapper);
				  subscribeSelectionPopup.setVisible(true);
				  subscribeSelectionPopup.center();
			  }
		});
		
		okSubscribeControllerButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				  subscribeController(user, controllerLB.getSelectedItemText());
			  }
		});
		okSubscribeSensorButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				  subscribeSensor(user, sensorLB.getSelectedItemText());
				  }
		});
		okSubscribeActuatorButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				  subscribeActuator(user, actuatorLB.getSelectedItemText());
			  }
		});
	}
	private void renderControllerLB(){
		controllerLB.clear();
		controllerLB.setVisibleItemCount(5);
		for(String controller: Data.controllerAttributeList.keySet())
		{
			if(!controllerSubcriptionList.containsKey(controller))
			controllerLB.addItem(controller);
		}
	}
	
	private void renderSensorLB(){
		sensorLB.clear();
		sensorLB.setVisibleItemCount(5);
		for(String sensor: Data.sensorAttributeList.keySet())
		{
			if(!sensorSubcriptionList.containsKey(sensor))
			sensorLB.addItem(sensor);
		}
	}
	
	private void renderActuatorLB(){
		actuatorLB.clear();
		actuatorLB.setVisibleItemCount(5);
		for(String actuator: Data.actuatorAttributeList.keySet())
		{
			if(!actuatorSubcriptionList.containsKey(actuator))
			actuatorLB.addItem(actuator);
		}
	}
	
	private void getUserSubscription(String user){
		getSubscribedController(user);
		getSubscribedSensor(user);
		getSubscribedActuator(user);
	}
	
	private void renderControllerSubcriptionPanel(){
		FlexTable wrapper = new FlexTable();
		setHeaders(wrapper);
		for(String controller: controllerSubcriptionList.keySet())
		{
			int row = wrapper.getRowCount();
			wrapper.setText(row, 0, controller);
			wrapper.setText(row, 1, controllerSubcriptionList.get(controller));
		}
		addUnsubscribeControllerColumnAllRow(wrapper);
		cTable=wrapper;
		
		controllerSubscribedPanel.clear();
		controllerSubscribedPanel.setStyleName("mainStyle");
		controllerSubscribedPanel.setSpacing(10);
		controllerSubscribedPanel.add(new HTML("Subscribed Controllers"));
		controllerSubscribedPanel.add(cTable);
		controllerSubscribedPanel.add(subscribeControllerButton);
	}
	
	private void renderSensorSubcriptionPanel(){
		FlexTable wrapper = new FlexTable();
		setHeaders(wrapper);
		for(String sensor: sensorSubcriptionList.keySet())
		{
			int row = wrapper.getRowCount();
			wrapper.setText(row, 0, sensor);
			wrapper.setText(row, 1, sensorSubcriptionList.get(sensor));
		}
		addUnsubscribeSensorColumnAllRow(wrapper);
		sTable=wrapper;

		sensorSubscribedPanel.clear();
		sensorSubscribedPanel.setStyleName("mainStyle");
		sensorSubscribedPanel.setSpacing(10);
		sensorSubscribedPanel.add(new HTML("Subscribed Sensors"));
		sensorSubscribedPanel.add(sTable);
		sensorSubscribedPanel.add(subscribeSensorButton);
	}
	
	private void renderActuatorSubcriptionPanel(){
		FlexTable wrapper = new FlexTable();
		setHeaders(wrapper);
		for(String actuator: actuatorSubcriptionList.keySet())
		{
			int row = wrapper.getRowCount();
			wrapper.setText(row, 0, actuator);
			wrapper.setText(row, 1, actuatorSubcriptionList.get(actuator));
		}
		addUnsubscribeActuatorColumnAllRow(wrapper);
		aTable=wrapper;

		actuatorSubscribedPanel.clear();
		actuatorSubscribedPanel.setStyleName("mainStyle");
		actuatorSubscribedPanel.setSpacing(10);
		actuatorSubscribedPanel.add(new HTML("Subscribed Actuators"));
		actuatorSubscribedPanel.add(aTable);
		actuatorSubscribedPanel.add(subscribeActuatorButton);
	}
	
	private void getSubscribedController(String user){
		Utility.newRequestObj().userGetSubscribedControllers(user, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get "+Data.currentUser+"'s controller subscription");
			}
 
			public void onSuccess(String[][] reply) {
				if(reply==null)
				{
					controllerSubcriptionList.clear();
					controllerSubcriptionList.put(noSubscription, noSubscription);
				}
				else
				{
					Data.subscribedControllerList.clear();
					controllerSubcriptionList.clear();
					for(int i=0;i<reply.length;i++)
					{
						Data.subscribedControllerList.add(reply[i][1]);
						controllerSubcriptionList.put(reply[i][1], checkIfLastReadTimeNull(reply[i][2]));
					}
				}
				renderControllerSubcriptionPanel();
				renderControllerLB();
			}
		});
	}
	
	private void getSubscribedSensor(String user){
		Utility.newRequestObj().userGetSubscribedSensors(user, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get "+Data.currentUser+"'s sensor subscription");
			} 
 
			public void onSuccess(String[][] reply) {
				if(reply==null)
				{
					sensorSubcriptionList.clear();
					sensorSubcriptionList.put(noSubscription, noSubscription);
				}
				else
				{
					Data.subscribedSensorList.clear();
					sensorSubcriptionList.clear();
					for(int i=0;i<reply.length;i++)
					{
						Data.subscribedSensorList.add(reply[i][1]);
						sensorSubcriptionList.put(reply[i][1], checkIfLastReadTimeNull(reply[i][2]));
					}
				}
				renderSensorSubcriptionPanel();
				renderSensorLB();
			}
		});
	}
	
	private void getSubscribedActuator(String user){
		Utility.newRequestObj().userGetSubscribedActuators(user, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get "+Data.currentUser+"'s actuator subscription");
			} 
 
			public void onSuccess(String[][] reply) {
				if(reply==null)
				{
					actuatorSubcriptionList.clear();
					actuatorSubcriptionList.put(noSubscription, noSubscription);
				}
				else
				{
					Data.subscribedActuatorList.clear();
					actuatorSubcriptionList.clear();
					for(int i=0;i<reply.length;i++)
					{
						Data.subscribedActuatorList.add(reply[i][1]);
						actuatorSubcriptionList.put(reply[i][1], checkIfLastReadTimeNull(reply[i][2]));
					}
				}
				renderActuatorSubcriptionPanel();
				renderActuatorLB();
			}
		});
	}
	
	private void subscribeController(final String user, final String controller){
		Utility.newRequestObj().userSubscribeControllerNotification(user, controller, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to subscribe to "+controller);
			} 
 
			public void onSuccess(String reply) {
				Window.alert("Controller Subscription: "+reply);
				if(success(reply))
				{
//					cTable.setText(cTable.getRowCount(), 0, controller); //Local Update
					getSubscribedController(user);
					removeItemFromListBox(controllerLB, controller);
					subscribeSelectionPopup.setVisible(false);
					updateControllerLastReadTime(user,controller);
				}
			}
		});
	}
	
	private void subscribeSensor(final String user, final String sensor){
		Utility.newRequestObj().userSubscribeSensorNotification(user, sensor, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to subscribe to "+sensor);
			} 
 
			public void onSuccess(String reply) {
				Window.alert("Sensor Subscription: "+reply);
				if(success(reply))
				{
//					sTable.setText(sTable.getRowCount(), 0, sensor);
					getSubscribedSensor(user);
					removeItemFromListBox(sensorLB, sensor);
					subscribeSelectionPopup.setVisible(false);
					updateSensorLastReadTime(user,sensor);
				}
			}
		});
	}
	
	private void subscribeActuator(final String user, final String actuator){
		Utility.newRequestObj().userSubscribeActuatorNotification(user, actuator, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to subscribe to "+actuator);
			} 
 
			public void onSuccess(String reply) {
				Window.alert("Actuator Subscription: "+reply);
				if(success(reply))
				{
//					aTable.setText(aTable.getRowCount(), 0, actuator);
					getSubscribedActuator(user);
					removeItemFromListBox(actuatorLB, actuator);
					subscribeSelectionPopup.setVisible(false);
					updateActuatorLastReadTime(user,actuator);
				}
			}
		});
	}
	
	private void unSubscribeController(final String user, final String controller){
		Utility.newRequestObj().userUnsubscribeControllerNotification(user, controller, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to unsubscribe "+controller);
			} 
 
			public void onSuccess(String reply) {
				Window.alert("Controller Unsubscription: "+reply);
				if(success(reply))
				{
					addItemToListBox(controllerLB, controller);
					getSubscribedController(user);
				}
			}
		});
	}
	
	private void unSubscribeSensor(final String user, final String sensor){
		Utility.newRequestObj().userUnsubscribeSensorNotification(user, sensor, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to unsubscribe "+sensor);
			} 
 
			public void onSuccess(String reply) {
				Window.alert("Sensor Unsubscription: "+reply);
				if(success(reply))
				{
					addItemToListBox(sensorLB, sensor);
					getSubscribedSensor(user);
				}
			}
		});
	}
	
	private void unSubscribeActuator(final String user, final String actuator){
		Utility.newRequestObj().userUnsubscribeActuatorNotification(user, actuator, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to unsubscribe "+actuator);
			} 
 
			public void onSuccess(String reply) {
				Window.alert("Actuator Unsubscription: "+reply);
				if(success(reply))
				{
					addItemToListBox(actuatorLB, actuator);
					getSubscribedActuator(user);
				}
			}
		});
	}
	
	private void updateControllerLastReadTime(final String user, final String controller){
		Date date = new Date(System.currentTimeMillis());
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, controller, date, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update controller last read time");
			} 
 
			public void onSuccess(String reply) {
				Window.alert(reply);
			}
		});
	} 
	
	private void updateSensorLastReadTime(final String user, final String sensor){
		Date date = new Date(System.currentTimeMillis());
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, sensor, date, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update sensor last read time");
			} 
 
			public void onSuccess(String reply) {

			}
		});
	}
	
	private void updateActuatorLastReadTime(final String user, final String actuator){
		Date date = new Date(System.currentTimeMillis());
		Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(user, actuator, date, new AsyncCallback<String>(){
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update actuator last read time");
			} 
 
			public void onSuccess(String reply) {

			}
		});
	}
	
	private void removeItemFromListBox(ListBox lb, String item){
		for(int i=0; i<lb.getItemCount(); i++)
		{
			if(lb.getItemText(i).equals(item))
			{
				lb.removeItem(i);
				break;
			}
		}
	}
	
	private void addItemToListBox(ListBox lb, String item){
		lb.addItem(item);
	}
	
	private boolean success(String reply){
		return reply.equals("OK");
	}
	
	private void addUnsubscribeControllerColumnAllRow(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			if(ft.getText(i, 0).equals(noSubscription))
			{
				break;
			}
			Anchor Unsubscribe = new Anchor("Unsubscribe");
			ft.setWidget(i, ft.getCellCount(i), Unsubscribe);
			Unsubscribe.setName(ft.getText(i, 0));
			setUnsubscribeControllerClickHandler(Unsubscribe);
		}
	}
	
	private void setUnsubscribeControllerClickHandler(final Anchor anchor){
		anchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				 unSubscribeController(user, anchor.getName());
			}
		});
	}
	
	private void addUnsubscribeSensorColumnAllRow(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			if(ft.getText(i, 0).equals(noSubscription))
			{
				break;
			}
			Anchor Unsubscribe = new Anchor("Unsubscribe");
			ft.setWidget(i, ft.getCellCount(i), Unsubscribe);
			Unsubscribe.setName(ft.getText(i, 0));
			setUnsubscribeSensorClickHandler(Unsubscribe);
		}
	}
	
	private void setUnsubscribeSensorClickHandler(final Anchor anchor){
		anchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				  unSubscribeSensor(user, anchor.getName());
			}
		});
	}
	
	private void addUnsubscribeActuatorColumnAllRow(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			if(ft.getText(i, 0).equals(noSubscription))
			{
				break;
			}
			Anchor Unsubscribe = new Anchor("Unsubscribe");
			ft.setWidget(i, ft.getCellCount(i), Unsubscribe);
			Unsubscribe.setName(ft.getText(i, 0));
			setUnsubscribeActuatorClickHandler(Unsubscribe);
		}
	}
	
	private void setUnsubscribeActuatorClickHandler(final Anchor anchor){
		anchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				  unSubscribeActuator(user, anchor.getName());
			}
		});
	}
	
	private void setHeaders(FlexTable ft){
		String[] header = {"Device","Last Read Time"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}
	
	private String checkIfLastReadTimeNull(String data){
		if(data==null)
			return "No Access History";
		
		return data;
	}
}
