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
						getSiteControllerList(siteResult[i][0]);
					}
				}
			}
		});
	}
	
	public static void getSiteControllerList(final String siteName){
		Utility.newRequestObj().getSiteControllerList(siteName, new AsyncCallback<String[][]>() {
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
						getControllerSensorList(controllerResult[i][0]);
					}
				}
				Data.siteControllerList.put(siteName, controller);
			}
		});
	}
	
	public static void getControllerSensorList(final String controllerName){
		Utility.newRequestObj().getControllerSensorList(controllerName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) 
			{
				Window.alert("Unable to get site list");
			}
			public void onSuccess(String[][] sensorResult)
			{
				ArrayList<String> sensors = new ArrayList<>();
				if (sensorResult!=null) {
					for(int i=0; i<sensorResult.length;i++)
					{
						sensors.add(sensorResult[i][0]);
					}
				}
				Data.controllerSensorList.put(controllerName, sensors);
			}
		});
	}
}
