package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ResourcePreload{
	
	public static void getSiteList(){
		
		Utility.newRequestObj().getSiteList(new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get site list");
			}
			
			public void onSuccess(final String[][] siteResult) {
				if (siteResult!=null) {
					for(int i=0; i<siteResult.length;i++)
					{
						Data.siteList.put(siteResult[i][0], siteResult[i][1]);
						getControllerList(siteResult[i][0]);
					}
				}
			}
		});
	}
	
	public static void getControllerList(final String siteName){
		Utility.newRequestObj().getControllerList(siteName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) 
			{
				Window.alert("Unable to get site list");
			}
			public void onSuccess(String[][] controllerResult)
			{
				ArrayList<String> controller = new ArrayList<>();
				if (controllerResult!=null) {
					for(int i=0; i<controllerResult.length;i++)
					{
						controller.add(controllerResult[i][0]);
						ArrayList<String> controllerAttributes = new ArrayList<>();
						for(int j=0; j<controllerResult.length;j++)
						{
							controllerAttributes.add(controllerResult[i][j]);
						}
						Data.controllerAttributeList.put(controllerResult[i][0], controllerAttributes);

						getSensorList(controllerResult[i][0]);
						getActuatorList(controllerResult[i][0]);
					}
				}
				Data.siteControllerList.put(siteName, controller);
			}
		});
	}
	
	public static void getSensorList(final String controllerName){
		Utility.newRequestObj().getSensorList(controllerName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) 
			{
				Window.alert("Unable to get sensor list");
			}
			public void onSuccess(String[][] sensorResult)
			{
				ArrayList<String> sensors = new ArrayList<>();
				if (sensorResult!=null) {
					for(int i=0; i<sensorResult.length;i++)
					{
						sensors.add(sensorResult[i][0]);
						ArrayList<String> sensorAttributes = new ArrayList<>();
						for(int j=0; j<sensorResult[i].length;j++)
						{
//							Window.alert("sensor: "+sensorResult[i][j]);
							sensorAttributes.add(sensorResult[i][j]);
						}
						Data.sensorAttributeList.put(sensorResult[i][0], sensorAttributes);
					}
				}
				Data.controllerSensorList.put(controllerName, sensors);
			}
		});
	}
	
	public static void getActuatorList(final String controllerName){
		Utility.newRequestObj().getActuatorList(controllerName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) 
			{
				Window.alert("Unable to get actuator list");
			}
			public void onSuccess(String[][] actuatorResult)
			{
				Window.alert(actuatorResult.length+"");
				ArrayList<String> actuators = new ArrayList<>();
				if (actuatorResult!=null) {
					for(int i=0; i<actuatorResult[i].length;i++)
					{
						actuators.add(actuatorResult[i][0]);
						ArrayList<String> actuatorAttributes = new ArrayList<>();
						for(int j=0; j<actuatorResult.length;j++)
						{
							Window.alert("actuator: "+actuatorResult[i][j]);
							actuatorAttributes.add(actuatorResult[i][j]);
						}
						Data.actuatorAttributeList.put(actuatorResult[i][0], actuatorAttributes);
					}
				}
				Data.controllerActuatorList.put(controllerName, actuators);
			}
		});
	}
}
