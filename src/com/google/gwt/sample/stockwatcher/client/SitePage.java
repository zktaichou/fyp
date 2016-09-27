package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class SitePage extends Composite {

	ContentPanel cPanel = new ContentPanel();
	
	VerticalPanel parameterPanel = new VerticalPanel();
	VerticalPanel selectionPanel = new VerticalPanel();
	VerticalPanel controllerPanel = new VerticalPanel();
	VerticalPanel buttonPanel = new VerticalPanel();

	ListBox siteListBox = new ListBox();
	ListBox controllerListBox = new ListBox();
	ListBox optionListBox = new ListBox();
	
	Button backButton = new Button("Back");
	Button goButton = new Button("Go");
	
	static Image sitePic = new Image();
	
	static ArrayList<PopupPanel> sensorIcons = new ArrayList<>();
	
	public SitePage(){
		
		setHandlers();
		
		controllerPanel.clear();
		controllerPanel.add(new HTML("Please select controller(s):"));
		controllerPanel.add(controllerListBox);
		controllerPanel.setSpacing(10);

		selectionPanel.clear();
		selectionPanel.add(new HTML("<h2>Selection Menu</h2></br>"));
		selectionPanel.add(new HTML("Please select site(s):"));
		selectionPanel.add(siteListBox);
		selectionPanel.add(new HTML("Please select view:"));
		selectionPanel.add(optionListBox);
		selectionPanel.setSpacing(10);
		
		buttonPanel.add(goButton);
		buttonPanel.setSpacing(10);
		
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.add(selectionPanel);
		wrapper.add(controllerPanel);
		wrapper.add(buttonPanel);
		
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
		showControllers(controllerListBox, 0);
	}
	
	public void renderControllerListBox(){
		siteListBox.clear();
		int count=0;
		for(String siteName : Data.siteList.keySet())
		{
			siteListBox.addItem(siteName);
			siteListBox.setValue(count++, Data.siteList.get(siteName));
		}
	}
	
	private void showControllers(ListBox listBox, int category) {
		showControllers(listBox, siteListBox.getSelectedItemText());
	  }
	
	private void showControllers(ListBox listBox, String category) {
		controllerListBox.clear();
		for(String controllerName : Data.siteControllerList.get(category))
		{
		    	controllerListBox.addItem(controllerName);
		}
	}
	
	public void renderOptionListBox(){
		optionListBox.clear();
		optionListBox.addItem("Controller");
		optionListBox.addItem("Sensor");
		optionListBox.addItem("Actuator");
	}
	
	public void setHandlers(){

		renderSiteListBox();
		renderOptionListBox();
		
		for (int i=0;i<siteListBox.getItemCount();i++) {
			siteListBox.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event){
//					cPanel.add(Utility.addTimer());
//					hideAllIcons();
//					getControllers(siteListBox.getSelectedItemText());
				};
			});
		}
		
		siteListBox.addChangeHandler(new ChangeHandler() {
		      public void onChange(ChangeEvent event) {
		          showControllers(controllerListBox, siteListBox.getSelectedItemText());
		        }
		      });
		
		optionListBox.addChangeHandler(new ChangeHandler() {
		      public void onChange(ChangeEvent event) {
		          if(optionListBox.getSelectedItemText().equals("Controller")){
		        	  controllerPanel.setVisible(true);
		          }
		          else
		          {
		        	  controllerPanel.setVisible(false);
		          }
		        }
		      });
		
		goButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		          fireRequest();
		        }
		      });
	}
	
	public void fireRequest(){
		String site = siteListBox.getSelectedItemText();
		String controller = controllerListBox.getSelectedItemText();
		String view = optionListBox.getSelectedItemText();
		
		cPanel.add(Utility.addTimer());
		hideAllIcons();
		
		switch(view){
			case "Controller":
			{
				getControllers(site);
				break;
			}
			case "Sensor":
			{
				getSensors(controller);
				break;
			}
			case "Actuator":
			{
				getActuators(controller);
				break;
			}
			default: break;
		}
	}
	
	public void getPic(){
		sitePic.setUrl(siteListBox.getSelectedValue());
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
			public void onSuccess(final String[][] sensorResult) {
				Utility.hideTimer();

				getPic();
				
				final HashMap<Boolean,String> toggle=new HashMap<>();
				toggle.put(Boolean.TRUE,Images.getImage(Images.ON, 25));
				toggle.put(Boolean.FALSE,Images.getImage(Images.OFF, 25));
				
				for(int i=0; i<sensorResult.length;i++)
				{
					final Anchor temp = new Anchor(" ");
					temp.getElement().getStyle().setProperty("toggleStatus","ON");
					temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
					
					HorizontalPanel vPanel = new HorizontalPanel();
					vPanel.setSpacing(5);
					vPanel.add(temp);
					vPanel.add(new HTML(sensorResult[i][0]));
					
					final PopupPanel container = new PopupPanel();
					
					sensorIcons.add(container);
					
					container.add(temp);
					container.getElement().getStyle().setBackgroundColor("rgba(255,0,0,0.0)");
					container.getElement().getStyle().setBorderWidth(0,Unit.PX);
					container.setTitle(sensorResult[i][0]+"");
					//container.setPopupPosition(sitePic.getAbsoluteLeft()+(int)(Double.parseDouble(result[i][2])*sitePic.getOffsetWidth()), sitePic.getAbsoluteTop()+(int)(Double.parseDouble(result[i][3])*sitePic.getOffsetHeight()));
					final int zzz=i;
					container.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			            public void setPosition(int offsetWidth, int offsetHeight) {
			              int left = sitePic.getAbsoluteLeft()+(int)(Double.parseDouble(sensorResult[zzz][2])*sitePic.getWidth());
			              int top = sitePic.getAbsoluteTop()+(int)(Double.parseDouble(sensorResult[zzz][3])*sitePic.getHeight());
			              container.setPopupPosition(left, top);
			            }
			          });
					
					temp.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event){
							temp.getElement().getStyle().setProperty("toggleStatus",String.valueOf(!Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
							temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
						}
					});
				}
			}
		});
	}
	
	public void getSensors(String controller){
		Utility.newRequestObj().getSensorList(controller, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get sensor list");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(final String[][] actuatorResult) {
				
			}
		});
	}
	
	public void getActuators(String controller){
		Utility.newRequestObj().getActuatorList(controller, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get actuator list");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(final String[][] actuatorResult) {
				
			}
		});
	}
	
	public String setIcon(String type){
		switch(type){
			case "Current":{
				return Images.getImage(Images.CURRENT_ICON,100);
			}
			case "Water":{
				return Images.getImage(Images.WATER_ICON,25);
			}
			case "Light":{
				return Images.getImage(Images.LIGHT_ICON,25);
			}
		}
		return "";
	}
	
	public static void hideAllIcons(){
		for(PopupPanel panels: sensorIcons){
			panels.hide();
		}
		sensorIcons.clear();
		sitePic.setVisible(false);
	}
	
}
