package com.google.gwt.sample.stockwatcher.client;

import java.awt.Graphics;
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
	
	Button backButton = new Button("Back");
	Button goButton = new Button("Go");
	
	static Image sitePic = new Image();
	
	static ArrayList<PopupPanel> controllerIcons = new ArrayList<>();
	static ArrayList<PopupPanel> sensorIcons = new ArrayList<>();
	static ArrayList<PopupPanel> actuatorIcons = new ArrayList<>();
	
	HashMap <String, ArrayList<PopupPanel>> controllerSensorPopupList = new HashMap<>();
	HashMap <String, ArrayList<PopupPanel>> controllerActuatorPopupList = new HashMap<>();
	
	public SitePage(){
		setHandlers();
		
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.add(new HTML("<h2>Selection Menu</h2></br>"));
		wrapper.add(new HTML("Please select site(s):"));
		wrapper.add(siteListBox);
		wrapper.add(selectionPanel);
		wrapper.add(goButton);
		wrapper.setSpacing(10);
		
		parameterPanel.clear();
		parameterPanel.add(wrapper);
		parameterPanel.setHeight("100%");
		parameterPanel.setStyleName("parameterPanel");
		
		cPanel.clear();
		cPanel.addLeft(parameterPanel);
		cPanel.add(sitePic);
		
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
	}
	
	public void setHandlers(){

		renderSiteListBox();
//		renderSensorPopups();
//		renderActuatorPopups();
		
		goButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		          fireRequest();
		      }
		});
	}
	
	public void fireRequest(){
		cPanel.add(Utility.addTimer());
		hideAllIcons();
		getControllers(siteListBox.getSelectedItemText());
	}
	
	public void getPic(String siteSelected){
		sitePic.setUrl(siteSelected);
		double width=sitePic.getWidth();
		double height=sitePic.getHeight();
		double screenWidth=Window.getClientWidth()*0.5;
		double screenHeight=Window.getClientHeight()*0.5;
		double resizeFactor=Math.min((screenWidth+0.0)/width,(screenHeight+0.0)/height);
		int newW=(int)(width*resizeFactor); int newH=(int)(height*resizeFactor);
		sitePic.setSize(newW+"px",newH+"px");	
		sitePic.setVisible(true);
	}
	
	public void getControllers(String site){
		Utility.newRequestObj().getControllerList(site, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get sensor list");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(final String[][] controllerResult) {
				Utility.hideTimer();

				getPic(siteListBox.getSelectedValue());
				
				renderSensorPopups();
				renderActuatorPopups();
				
				final HashMap<Boolean,String> toggle=new HashMap<>();
				toggle.put(Boolean.TRUE,Images.getImage(Images.SELECTED, 30));
				toggle.put(Boolean.FALSE,Images.getImage(Images.MICROCONTROLLER_ICON, 30));
				
				for(int i=0; i<controllerResult.length;i++)
				{
					final String controllerName = controllerResult[i][0];
					
					final Anchor temp = new Anchor(" ");
					temp.getElement().getStyle().setProperty("toggleStatus","FALSE");
					temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
					
					HorizontalPanel vPanel = new HorizontalPanel();
					vPanel.setSpacing(5);
					vPanel.add(temp);
					vPanel.add(new HTML(controllerName));
					
					final PopupPanel container = new PopupPanel();
					
					controllerIcons.add(container);
					
					container.add(temp);
					container.getElement().getStyle().setBackgroundColor("rgba(255,0,0,0.0)");
					container.getElement().getStyle().setBorderWidth(0,Unit.PX);
					container.setTitle(controllerName);
					final int zzz=i;
					container.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			            public void setPosition(int offsetWidth, int offsetHeight) {
			              int left = sitePic.getAbsoluteLeft()+(int)(Double.parseDouble(controllerResult[zzz][2])*sitePic.getWidth());
			              int top = sitePic.getAbsoluteTop()+(int)(Double.parseDouble(controllerResult[zzz][3])*sitePic.getHeight());
			              container.setPopupPosition(left, top);
			            }
			          });
					
					temp.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event){
							temp.getElement().getStyle().setProperty("toggleStatus",String.valueOf(!Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
							temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
							popupSensors(controllerName,Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus")));
						}
					});
				}
			}
		});
	}
	
	public void renderSensorPopups(){
		for(String controller: Data.controllerSensorList.keySet())
		{
			final HashMap<Boolean,String> toggle=new HashMap<>();
			toggle.put(Boolean.TRUE,Images.getImage(Images.ON, 30));
			toggle.put(Boolean.FALSE,Images.getImage(Images.OFF, 30));
			
			ArrayList<PopupPanel> popups = new ArrayList<>();
			
			for(String sensors: Data.controllerSensorList.get(controller))
			{
				ArrayList<String> attributes = Data.sensorAttributeList.get(sensors);
				
				String name = attributes.get(0);
				String type = attributes.get(1);
				final double x = Double.parseDouble(attributes.get(9));
				final double y = Double.parseDouble(attributes.get(10));
				String icon = setSensorIcon(type);
				PowerToggle temp = new PowerToggle(icon,name,toggle);
				
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
	
	public void popupSensors(String controllerName, Boolean state){
		
		for(PopupPanel popup: controllerSensorPopupList.get(controllerName)){
			popup.setAnimationEnabled(true);
			popup.setAnimationType(AnimationType.CENTER);
			popup.setVisible(state);
//			ArrayList<String> controllerAttributes = Data.controllerAttributeList.get(controllerName);
//
//			Graphics g;
//			g.drawLine(popup.getPopupLeft(), popup.getPopupTop(), Integer.parseInt(controllerAttributes.get(2)), Integer.parseInt(controllerAttributes.get(3)));
		}
	}
	
	public void renderActuatorPopups(){
		for(String controller: Data.controllerActuatorList.keySet())
		{
			final HashMap<Boolean,String> toggle=new HashMap<>();
			toggle.put(Boolean.TRUE,Images.getImage(Images.ON, 30));
			toggle.put(Boolean.FALSE,Images.getImage(Images.OFF, 30));
			
			ArrayList<PopupPanel> popups = new ArrayList<>();
			
			for(String actuators: Data.controllerActuatorList.get(controller))
			{
				ArrayList<String> attributes = Data.actuatorAttributeList.get(actuators);
				String name = attributes.get(0);
				final double x = Double.parseDouble(attributes.get(3));
				final double y = Double.parseDouble(attributes.get(4));
				Window.alert(name+" "+x+" "+y);
				String icon = Images.getImage(Images.ACTUATOR_CURRENT_ICON,30);
				PowerToggle temp = new PowerToggle(icon, name, toggle);
				
				final PopupPanel container = new PopupPanel();
				
				actuatorIcons.add(container);
				
				container.add(temp);
				container.setVisible(false);
				container.getElement().getStyle().setBackgroundColor("rgba(255,0,0,0.0)");
				container.getElement().getStyle().setBorderWidth(0,Unit.PX);
				container.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
		            public void setPosition(int offsetWidth, int offsetHeight) {
		            	int left = sitePic.getAbsoluteLeft()+(int)(x*(double)sitePic.getWidth());
			            int top = sitePic.getAbsoluteTop()+(int)(y*(double)sitePic.getHeight());
		              container.setPopupPosition(left, top);
		            }
		          });
				
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
	
	public class PowerToggle extends Composite{
		
		public PowerToggle(String icon, String name, final HashMap<Boolean,String> toggle){
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setStyleName("rounded");
			hPanel.getElement().getStyle().setBackgroundColor("#BBEAF1");
			hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hPanel.add(new HTML(icon));
			hPanel.add(new HTML(name));
			
			final Anchor temp = new Anchor(" ");
			temp.getElement().getStyle().setProperty("toggleStatus","FALSE");
			temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
			
			temp.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event){
					temp.getElement().getStyle().setProperty("toggleStatus",String.valueOf(!Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
					temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
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
	
}
