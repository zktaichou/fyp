package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;

public class SitePage extends Composite{

	ContentPanel cPanel = new ContentPanel();
	
	VerticalPanel parameterPanel = new VerticalPanel();
	VerticalPanel selectionPanel = new VerticalPanel();
	VerticalPanel controllerPanel = new VerticalPanel();
	VerticalPanel buttonPanel = new VerticalPanel();

	ListBox siteListBox = new ListBox();
	
	static Image sitePic = new Image();
	
	static ArrayList<PopupPanel> controllerIcons = new ArrayList<>();
	static ArrayList<PopupPanel> sensorIcons = new ArrayList<>();
	static ArrayList<PopupPanel> actuatorIcons = new ArrayList<>();

	HashMap <String, ArrayList<PopupPanel>> siteControllerPopupList = new HashMap<>();
	HashMap <String, ArrayList<PopupPanel>> controllerSensorPopupList = new HashMap<>();
	HashMap <String, ArrayList<PopupPanel>> controllerActuatorPopupList = new HashMap<>();
	
	public SitePage(){
		
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.add(new HTML("<h2>Selection Menu</h2></br>"));
		wrapper.add(new HTML("Please select site(s):"));
		wrapper.add(siteListBox);
		wrapper.add(selectionPanel);
		wrapper.setSpacing(10);
		  
		parameterPanel.clear();
		parameterPanel.add(wrapper);
		parameterPanel.setHeight("100%");
		parameterPanel.setStyleName("parameterPanel");
		
		cPanel.clear();
		cPanel.addLeft(parameterPanel);
		cPanel.add(sitePic);

		setHandlers();
		
		initWidget(cPanel); 
		}
	
	public void renderSiteListBox(){
		siteListBox.clear();
		int count=0;
		for(String siteName : Data.siteList.keySet())
		{
			siteListBox.addItem(siteName);
			siteListBox.setValue(count++, Data.siteList.get(siteName));
		}
		
		siteListBox.setSelectedIndex(0);

		getPic(siteListBox.getSelectedItemText());
	}
	
	public void setHandlers(){

		renderSiteListBox();

		siteListBox.addChangeHandler(new ChangeHandler(){
		      public void onChange(ChangeEvent event) {
		    	  
		  		hideAllIcons();
		  		
		  		getPic(siteListBox.getSelectedItemText());

				renderControllerPopups();
				renderSensorPopups();
				renderActuatorPopups();
				
		  		popupControllers(siteListBox.getSelectedItemText());
		      }
		});
	}
	
	public void getPic(String siteSelected){
		String url = Data.siteList.get(siteSelected);
		sitePic.setUrl(url);
		double width=sitePic.getWidth();
		double height=sitePic.getHeight();
		double screenWidth=Window.getClientWidth()*0.6;
		double screenHeight=Window.getClientHeight()*0.6;
		double resizeFactor=Math.min((screenWidth+0.0)/width,(screenHeight+0.0)/height);
		int newW=(int)(width*resizeFactor); int newH=(int)(height*resizeFactor);
		sitePic.setSize(newW+"px",newH+"px");	
		sitePic.setVisible(true);
	}
	
	public void renderControllerPopups(){
		for(String site: Data.siteControllerList.keySet())
		{
			
			ArrayList<PopupPanel> popups = new ArrayList<>();
			
			for(String controller: Data.siteControllerList.get(site))
			{
				ArrayList<String> attributes = Data.controllerAttributeList.get(controller);
				
				final String name = attributes.get(0);
				final double x = Double.parseDouble(attributes.get(2));
				final double y = Double.parseDouble(attributes.get(3));
				
				Controller cToggle = new Controller(controller);
				
				
				final PopupPanel container = new PopupPanel();

				controllerIcons.add(container);
				
				container.add(cToggle);
				container.getElement().getStyle().setBackgroundColor("rgba(255,0,0,0.0)");
				container.getElement().getStyle().setBorderWidth(0,Unit.PX);
				container.setTitle(name);
				
				container.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
				         public void setPosition(int offsetWidth, int offsetHeight) {
				              int left = sitePic.getAbsoluteLeft()+(int)(x*(double)sitePic.getWidth());
				              int top = sitePic.getAbsoluteTop()+(int)(y*(double)sitePic.getHeight());
				              container.setPopupPosition(left, top);
				            }
				          });
				container.setVisible(false);
				popups.add(container);
			}
			siteControllerPopupList.put(site, popups);
		}
	}
	
	public void renderSensorPopups(){
		for(String controller: Data.controllerSensorList.keySet())
		{
			
			ArrayList<PopupPanel> popups = new ArrayList<>();
			
			for(String sensors: Data.controllerSensorList.get(controller))
			{
				ArrayList<String> attributes = Data.sensorAttributeList.get(sensors);
				
				String name = attributes.get(0);
				String type = attributes.get(1);
				final double x = Double.parseDouble(attributes.get(9));
				final double y = Double.parseDouble(attributes.get(10));
				String icon = setSensorIcon(type);
				
				Sensor temp = new Sensor(icon,name);
				
				final PopupPanel container = new PopupPanel();
				
				sensorIcons.add(container);
				
				container.add(temp);
				container.getElement().getStyle().setBackgroundColor("rgba(255,0,0,0.0)");
				container.getElement().getStyle().setBorderWidth(0,Unit.PX);
				container.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
		            public void setPosition(int offsetWidth, int offsetHeight) {
		              int left = sitePic.getAbsoluteLeft()+(int)(x*(double)sitePic.getWidth());
		              int top = sitePic.getAbsoluteTop()+(int)(y*(double)sitePic.getHeight());
		              container.setPopupPosition(left, top);
		            }
		          });
				container.setVisible(false);
				popups.add(container);
			}
			controllerSensorPopupList.put(controller, popups);
		}
	}
	
	public void popupControllers(String siteName){
		
		for(PopupPanel popup: siteControllerPopupList.get(siteName)){
			popup.setAnimationEnabled(true);
			popup.setAnimationType(AnimationType.CENTER);
			popup.setVisible(true);
		}
	}
	
	public void popupSensors(String controllerName, Boolean state){
		
		for(PopupPanel popup: controllerSensorPopupList.get(controllerName)){
			popup.setAnimationEnabled(true);
			popup.setAnimationType(AnimationType.CENTER);
			popup.setVisible(state);
		}
	}
	
	public void renderActuatorPopups(){
		for(String controller: Data.controllerActuatorList.keySet())
		{
			ArrayList<PopupPanel> popups = new ArrayList<>();
			
				for(String actuators: Data.controllerActuatorList.get(controller))
				{
					ArrayList<String> attributes = Data.actuatorAttributeList.get(actuators);
					String name = attributes.get(0);
					String status = attributes.get(2);
					final double x = Double.parseDouble(attributes.get(3));
					final double y = Double.parseDouble(attributes.get(4));
					
					String icon = Images.getImage(Images.ACTUATOR_CURRENT_ICON,25);
					
					Actuator temp = new Actuator(icon, name, status);
					
					final PopupPanel container = new PopupPanel();
					
					actuatorIcons.add(container);
					
					container.add(temp);
					container.getElement().getStyle().setBackgroundColor("rgba(255,0,0,0.0)");
					container.getElement().getStyle().setBorderWidth(0,Unit.PX);
					container.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			            public void setPosition(int offsetWidth, int offsetHeight) {
			            	int left = sitePic.getAbsoluteLeft()+(int)(x*(double)sitePic.getWidth());
				            int top = sitePic.getAbsoluteTop()+(int)(y*(double)sitePic.getHeight());
			              container.setPopupPosition(left, top);
			            }
			          });
					
					container.setVisible(false);
					popups.add(container);
				}
				controllerActuatorPopupList.put(controller, popups);
		}	
	}
	
	public void popupActuators(String controllerName, Boolean state){
		for(PopupPanel popup: controllerActuatorPopupList.get(controllerName)){
			popup.setAnimationEnabled(true);
			popup.setAnimationType(AnimationType.CENTER);
			popup.setVisible(state);
		}
	}
	
	public String setSensorIcon(String type){
		switch(type){
			case "Current":{
				return Images.getImage(Images.CURRENT_ICON,30);
			}
			case "Water":{
				return Images.getImage(Images.WATER_ICON,30);
			}
			case "Light":{
				return Images.getImage(Images.LIGHT_ICON,30);
			}
			case "Temperature":{
				return Images.getImage(Images.TEMPERATURE_ICON,30);
			}
		}
		return "";
	}
	
	public String setActuatorIcon(String type){
		switch(type){
			case "Current":{
				return Images.getImage(Images.ACTUATOR_CURRENT_ICON,30);
			}
			case "Water":{
				return Images.getImage(Images.ACTUATOR_WATER_ICON,30);
			}
		}
		return "";
	}
	
	public static void hideAllIcons(){
		for(PopupPanel panels: controllerIcons){
			panels.hide();
		}
		
		for(PopupPanel panels: sensorIcons){
			panels.hide();
		}
		
		for(PopupPanel panels: actuatorIcons){
			panels.hide();
		}
		controllerIcons.clear();
		sensorIcons.clear();
		actuatorIcons.clear();
		sitePic.setVisible(false);
	}
	
	public class Sensor extends Composite{
			
			public Sensor(String icon, String name){

				final HashMap<Boolean,String> toggle=new HashMap<>();
				toggle.put(Boolean.TRUE,Images.getImage(Images.ON, 30));
				toggle.put(Boolean.FALSE,Images.getImage(Images.OFF, 30));
				
				HorizontalPanel hPanel = new HorizontalPanel();
				hPanel.setStyleName("rounded");
				hPanel.getElement().getStyle().setBackgroundColor("white");
				hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				hPanel.setSpacing(3);
				hPanel.add(new HTML(icon));
				hPanel.add(new HTML(name));
				
				VerticalPanel vPanel = new VerticalPanel();
				vPanel.setSpacing(5);
				vPanel.add(hPanel);
				
				initWidget(vPanel);
			}
	}
	
	public static class Actuator extends Composite{
		public static HashMap<Anchor,Boolean> state=new HashMap<>();
		
		public Actuator(String icon, final String name, String status){
			final HashMap<Boolean,String> toggle=new HashMap<>();
			toggle.put(Boolean.TRUE,Images.getImage(Images.ON, 30));
			toggle.put(Boolean.FALSE,Images.getImage(Images.OFF, 30));
			
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setStyleName("rounded");
			hPanel.getElement().getStyle().setBackgroundColor("white");
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hPanel.setSpacing(3);
			hPanel.add(new HTML(icon));
			hPanel.add(new HTML(name));

			final Anchor temp = new Anchor(" ");
			//Enable click
			temp.setName("true");
			
			//Initial status from db
			if(status.equals("ON")) state.put(temp,true);
			else state.put(temp,false); 
			
			temp.setHTML(toggle.get(state.get(temp)));
			
			temp.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event){
					//if clickable
					if(temp.getName().equalsIgnoreCase("TRUE"))
					{
						//disable click until request finishes
						temp.setName("false");
						
						temp.setHTML(Images.getImage(Images.LOADING3, 25));
	
						String newStatus = "";
						
						if(!state.get(temp)){
							newStatus="ON";
						}
						else
						{
							newStatus="OFF";
						}
						
						Utility.newRequestObj().actuatorSetStatus(name, newStatus, new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								Window.alert("Unable to update actuator status");
								temp.setName("true");
								temp.setHTML(toggle.get(state.get(temp)));
							} 
				 
							public void onSuccess(final String reply) {
								if(reply.equals("OK"))
								{
									state.put(temp,!state.get(temp));
								}
								else
								{
									Window.alert("Error in updating");
								}
								temp.setName("true");
								temp.setHTML(toggle.get(state.get(temp)));
							}
						});
					}
				}
			});
			
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setSpacing(5);
			vPanel.add(hPanel);
			vPanel.add(temp);
			vPanel.setCellHorizontalAlignment(temp, HasHorizontalAlignment.ALIGN_CENTER);
			
			initWidget(vPanel);
		}
	}
	
	public String toggle(String cStatus){
		if(cStatus.equalsIgnoreCase("true"))
		{
			return "false";
		}
		else
		{
			return "true";
		}
	}
	
	public class Controller extends Composite{
			
			public Controller(final String name){

				final HashMap<Boolean,String> toggle=new HashMap<>();
				toggle.put(Boolean.TRUE,Images.getImage(Images.SELECTED, 30));
				toggle.put(Boolean.FALSE,Images.getImage(Images.MICROCONTROLLER_ICON, 30));
				
				final Anchor temp = new Anchor(" ");
				temp.getElement().getStyle().setProperty("toggleStatus","FALSE");
				temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
				temp.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event){
						temp.getElement().getStyle().setProperty("toggleStatus",String.valueOf(!Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
						temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
						popupSensors(name,Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus")));
						popupActuators(name,Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus")));
						}
				});
				
				HorizontalPanel hPanel = new HorizontalPanel();
				hPanel.setStyleName("rounded");
				hPanel.getElement().getStyle().setBackgroundColor("white");
				hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				hPanel.setSpacing(3);
				hPanel.add(new HTML(name));
				
				VerticalPanel vPanel = new VerticalPanel();
				vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				vPanel.setSpacing(5);
				vPanel.add(hPanel);
				vPanel.add(temp);
				
				initWidget(vPanel);
			}
	}
}
