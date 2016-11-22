package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
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
	
	static ArrayList<Boolean> actuatorIconVisibility = new ArrayList<>();
	static ArrayList<Boolean> sensorIconVisibility = new ArrayList<>();
	static ArrayList<Boolean> controllerIconVisibility = new ArrayList<>();

	static HashMap <String, ArrayList<PopupPanel>> siteControllerPopupList = new HashMap<>();
	static HashMap <String, ArrayList<PopupPanel>> controllerSensorPopupList = new HashMap<>();
	static HashMap <String, ArrayList<PopupPanel>> controllerActuatorPopupList = new HashMap<>();

	static boolean actuatorFirstTime = true;
	static boolean sensorFirstTime = true;
	static boolean controllerFirstTime = true;
	
	public SitePage(){
		actuatorFirstTime = true;
		
		controllerIcons.clear();
		sensorIcons.clear();
		actuatorIcons.clear();
		
		periodicUpdate();
		Header.setHeaderTitle("Main Menu > Planning > Actuator Control");
		renderSiteListBox();
		
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setStyleName("mainStyle");
		wrapper.add(new HTML("<h2>Selection Menu</h2></br>"));
		wrapper.add(new HTML("Please select site:"));
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
	
	private void renderSiteListBox(){
		siteListBox.clear();
		siteListBox.addItem("Select an item below");
		int count=0;
		for(String siteName : Data.siteList.keySet())
		{
			siteListBox.addItem(siteName);
			siteListBox.setValue(count++, Data.siteList.get(siteName));
		}
		
		siteListBox.setSelectedIndex(0);
	}
	
	private static void renderControlTypeListBox(ListBox lb){
		lb.clear();
		
		lb.addItem("Manual");
		lb.addItem("Scheduled");
		lb.addItem("Sensor Response");
	}
	
	public void setHandlers(){
		getPic(siteListBox.getItemText(1));

		siteListBox.addChangeHandler(new ChangeHandler(){
		      public void onChange(ChangeEvent event) {
		    	  if(siteListBox.getSelectedIndex()!=0)
		    	  {
			  		hideAllIcons();
			  		
			  		getPic(siteListBox.getSelectedItemText());
	
					renderControllerPopups();
					renderSensorPopups();
					renderActuatorPopups();
					
			  		popupControllers(siteListBox.getSelectedItemText());
		    	  }
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
		
		sitePic.removeFromParent();
		cPanel.add(sitePic);
	}
	
	static Timer t = new Timer() {
	      @Override
	      public void run() {
	  		ResourcePreload.getSiteList();
			
			renderControllerPopups();
			renderSensorPopups();
			renderActuatorPopups();
	      }
	    };
	
	private void periodicUpdate(){
		t.scheduleRepeating(15000);
	}
	
	private static void renderActuatorPopups(){
		actuatorIconVisibility.clear();
		
		if(!actuatorFirstTime)
		{
			for(int i=0; i<actuatorIcons.size();i++)
			{
				actuatorIconVisibility.add(actuatorIcons.get(i).isVisible());
			}
			actuatorIcons.clear();
		}
		hideActuators();
		for(String controller: Data.controllerActuatorList.keySet())
		{
			ArrayList<PopupPanel> popups = new ArrayList<>();
			int i=0;
				for(String actuators: Data.controllerActuatorList.get(controller))
				{
					ArrayList<String> attributes = Data.actuatorAttributeList.get(actuators);
					String name = attributes.get(0);
					String status = attributes.get(2);
					final double x = Double.parseDouble(attributes.get(3));
					final double y = Double.parseDouble(attributes.get(4));
					String controlType = attributes.get(5);
					
					String icon = Images.getImage(Images.ACTUATOR_CURRENT_ICON,25);
					
					Actuator temp = new Actuator(icon, name, status, controlType);
					
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
					
					if(actuatorFirstTime)
					{
						container.setVisible(false);
					}
					else
					{
						container.setVisible(actuatorIconVisibility.get(i++));
					}
					popups.add(container);
				}
				controllerActuatorPopupList.put(controller, popups);
		}	
		if(actuatorFirstTime)
		{
			actuatorFirstTime=false;
		}
	}
	
	private static void renderControllerPopups(){
		hideControllers();
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
	
	private static void renderSensorPopups(){
		hideSensors();
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
	
	private static void popupControllers(String siteName){
		
		for(PopupPanel popup: siteControllerPopupList.get(siteName)){
			popup.setAnimationEnabled(true);
			popup.setAnimationType(AnimationType.CENTER);
			popup.setVisible(true);
		}
	}
	
	private static void popupSensors(String controllerName, Boolean state){
		
		for(PopupPanel popup: controllerSensorPopupList.get(controllerName)){
			popup.setAnimationEnabled(true);
			popup.setAnimationType(AnimationType.CENTER);
			popup.setVisible(state);
		}
	}
	
	private static void popupActuators(String controllerName, Boolean state){
		for(PopupPanel popup: controllerActuatorPopupList.get(controllerName)){
			popup.setAnimationEnabled(true);
			popup.setAnimationType(AnimationType.CENTER);
			popup.setVisible(state);
		}
	}
	
	private static String setSensorIcon(String type){
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
	
	private static String setActuatorIcon(String type){
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
	
	public static void hideControllers(){
		for(PopupPanel panels: controllerIcons){
			panels.hide();
		}
		controllerIcons.clear();
	}
	
	public static void hideSensors(){
		for(PopupPanel panels: sensorIcons){
			panels.hide();
		}
		sensorIcons.clear();
	}
	
	public static void hideActuators(){
		for(PopupPanel panels: actuatorIcons){
			panels.hide();
		}
		actuatorIcons.clear();
	}
	
	public static class Sensor extends Composite{
			
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
				
//				FocusPanel wrapper = new FocusPanel();
//				wrapper.add(vPanel);
//				wrapper.addClickHandler(new ClickHandler() {
//				  @Override
//				  public void onClick(ClickEvent event) {
//					  for(String controller: controllerSensorPopupList.keySet())
//					  {
//						  for(PopupPanel popup: controllerSensorPopupList.get(controller))
//						  {
//							  popup.getElement().getStyle().setZIndex(0);
//						  }
//					  }
//					  event.getRelativeElement().getStyle().setZIndex(1000);
//				  }
//				});
				
				initWidget(vPanel);
			}
	}
	
	public static class Actuator extends Composite{
		public static HashMap<Anchor,Boolean> state=new HashMap<>();
		
		public Actuator(String icon, final String name, String status, String controlType){
			final HashMap<Boolean,String> toggle=new HashMap<>();
			toggle.put(Boolean.TRUE,Images.getImage(Images.ON, 30));
			toggle.put(Boolean.FALSE,Images.getImage(Images.OFF, 30));

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
									Window.alert("Node communication error. Please try again.");
								}
								temp.setName("true");
								temp.setHTML(toggle.get(state.get(temp)));
							}
						});
					}
				}
			});

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setStyleName("rounded");
			hPanel.getElement().getStyle().setBackgroundColor("white");
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hPanel.setSpacing(3);
			hPanel.add(new HTML(icon));
			hPanel.add(new HTML(name));
			hPanel.add(temp);
			
			ListBox lb = new ListBox();
			renderControlTypeListBox(lb);
			lb.setSelectedIndex(getIndexOfTextInWidget(lb,controlType)); 
			lb.setTitle(name);
			lb.setName(lb.getSelectedIndex()+"");
			setControlTypeLBChangeHandlers(lb);
			
			VerticalPanel selectablesPanel = new VerticalPanel();
			selectablesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			selectablesPanel.setSpacing(5);
			selectablesPanel.add(lb);
			
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			vPanel.setSpacing(5);
			vPanel.add(hPanel);
			vPanel.add(selectablesPanel);
			
//			FocusPanel wrapper = new FocusPanel();
//			wrapper.add(vPanel);
//			wrapper.addClickHandler(new ClickHandler() {
//			  @Override
//			  public void onClick(ClickEvent event) {
//				  for(String controller: controllerActuatorPopupList.keySet())
//				  {
//					  for(PopupPanel popup: controllerActuatorPopupList.get(controller))
//					  {
//						  popup.getElement().getStyle().setZIndex(0);
//					  }
//				  }
//				  event.getRelativeElement().getStyle().setZIndex(1000);
//			  }
//			});
			
			PopupPanel lol = new PopupPanel();
			lol.setStyleName("rounded");
			lol.getElement().getStyle().setBackgroundColor("white");
			lol.add(vPanel);
			
			initWidget(lol);
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
	
	public static class Controller extends Composite{
			
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
	
	private static int getIndexOfTextInWidget(ListBox lb, String text){
		for(int i=0; i<lb.getItemCount();i++)
		{
			if(text.equals(lb.getItemText(i)))
			{
				return i;
			}
		}
		return 0;
	}
	
	private static void setControlTypeLBChangeHandlers(final ListBox lb){
		lb.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent evenet){
				lb.setEnabled(false);
				
				Utility.newRequestObj().actuatorSetControlType(lb.getTitle(), lb.getSelectedItemText(), new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert("Unable to update control type");
						lb.setSelectedIndex(Integer.parseInt(lb.getName()));
						lb.setEnabled(true);
					}
					
					public void onSuccess(String result) {
						Window.alert("Update control type: "+result);
						lb.setName(String.valueOf(lb.getSelectedIndex()));
						lb.setEnabled(true);
					}
				});
			}
		});
	}
}
