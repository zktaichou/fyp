package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ResourcePreload{
	
	public static void preloadData(){
		getSiteList();
		getDayScheduleRuleAll();
		getSensorActuatorResponseAll();
//		getRegularSchedules();
//		getSpecialSchedules();
	}
	
	private static void getSensorActuatorResponseAll(){
		
		Utility.newRequestObj().sensorActuatorResponseGetAll(new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get sensor/actuator response list");
			}
			
			public void onSuccess(String[][] reponseList) {
				if (reponseList!=null) {
					Data.responseAttributeSize=reponseList[0].length;
					for(int i=0; i<reponseList.length;i++)
					{
						ArrayList<String> attributes = new ArrayList<>();
						for(int j=0; j<reponseList[i].length;j++)
						{
							attributes.add(reponseList[i][j]);
						}
						Data.sensorAcutatorResponseAttributeList.put(reponseList[i][0], attributes);
					}
				}
			}
		});
	}
	
	private static void getDayScheduleRuleAll(){
		
		Utility.newRequestObj().getDayScheduleRuleAll(new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get schedule rule list");
			}
			
			public void onSuccess(String[][] ruleName) {
				if (ruleName!=null) {
					for(int i=0; i<ruleName.length;i++)
					{
						getDayScheduleRuleByName(ruleName[i][0]);
					}
				}
			}
		});
	}
	
	private static void getDayScheduleRuleByName(String ruleName){
		
		Utility.newRequestObj().getDayScheduleRuleAll(new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get schedule rule list");
			}
			
			public void onSuccess(String[][] rules) {
				if (rules!=null) {
					Data.ruleAttributeSize=rules[0].length;
					for(int i=0; i<rules.length;i++)
					{
						ArrayList<String> ruleAttributes = new ArrayList<>();
						for(int j=0; j<rules[i].length;j++)
						{
							ruleAttributes.add(rules[i][j]);
						}
						Data.dayScheduleRuleAttributeList.put(rules[i][0], ruleAttributes);
					}
				}
			}
		});
	}
	
	private static void getRegularScheduleByName(final String scheduleName){
		
		Utility.newRequestObj().getRegularScheduleByName(scheduleName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get actuators list for regular schedule: "+scheduleName);
			}
			
			public void onSuccess(final String[][] result) {
				if (result!=null) {
					ArrayList<String> actuators = new ArrayList<>();
					for(int i=0; i<result.length;i++)
					{
						actuators.add(result[i][0]);
						ArrayList<String> actuatorAttributes = new ArrayList<>();
						for(int j=0; j<result[i].length;j++)
						{
							actuatorAttributes.add(result[i][j]);
						}
						Data.regularScheduleActuatorAttributeList.put(result[i][0], actuatorAttributes);
					}
					Data.regularScheduleActuatorList.put(scheduleName, actuators);
				}
			}
		});
	}
	
	private static void getSpecialScheduleByName(final String scheduleName){
		
		Utility.newRequestObj().getSpecialScheduleByName(scheduleName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get actuators list for special schedule: "+scheduleName);
			}
			
			public void onSuccess(final String[][] result) {
				if (result!=null) {
					ArrayList<String> actuators = new ArrayList<>();
					for(int i=0; i<result.length;i++)
					{
						actuators.add(result[i][0]);
						ArrayList<String> actuatorAttributes = new ArrayList<>();
						for(int j=0; j<result[i].length;j++)
						{
							actuatorAttributes.add(result[i][j]);
						}
						Data.specialScheduleActuatorAttributeList.put(result[i][0], actuatorAttributes);
					}
					Data.specialScheduleActuatorList.put(scheduleName, actuators);
				}
			}
		});
	}
	
	@SuppressWarnings("unused")
	private static void getRegularSchedules(){
		
		Utility.newRequestObj().getRegularSchedules(new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get regular schedules list");
			}
			
			public void onSuccess(String[][] rSchedule) {
				if (rSchedule!=null) {
					for(int i=0; i<rSchedule.length;i++)
					{
						getRegularScheduleByName(rSchedule[i][0]);
					}
				}
			}
		});
	}

	@SuppressWarnings("unused")
	private static void getSpecialSchedules(){
		
		Utility.newRequestObj().getSpecialSchedules(new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get site list");
			}
			
			public void onSuccess(final String[][] sSchedule) {
				if (sSchedule!=null) {
					for(int i=0; i<sSchedule.length;i++)
					{
						getSpecialScheduleByName(sSchedule[i][0]);
					}
				}
				
			}
		});
	}
	
	public static void getSiteList(){
		
		Utility.newRequestObj().getSiteList(new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get site list");
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
	
	private static void getControllerList(final String siteName){
		Utility.newRequestObj().getControllerList(siteName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) 
			{
//				Window.alert("Unable to get site list");
			}
			public void onSuccess(String[][] controllerResult)
			{
//				Window.alert("controller count: "+controllerResult.length);
				ArrayList<String> controller = new ArrayList<>();
				if (controllerResult!=null) {
					for(int i=0; i<controllerResult.length;i++)
					{
						controller.add(controllerResult[i][0]);
						getSensorList(controllerResult[i][0]);
						getActuatorList(controllerResult[i][0]);
						
						ArrayList<String> controllerAttributes = new ArrayList<>();
						for(int j=0; j<controllerResult[i].length;j++)
						{
							controllerAttributes.add(controllerResult[i][j]);
						}
						Data.controllerAttributeList.put(controllerResult[i][0], controllerAttributes);
					}
					Data.siteControllerList.put(siteName, controller);
				}
			}
		});
	}
	
	private static void getSensorList(final String controllerName){
//		Window.alert("getting sensors");
		Utility.newRequestObj().getSensorList(controllerName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) 
			{
//				Window.alert("Unable to get sensor list");
			}
			public void onSuccess(String[][] sensorResult)
			{
//				Window.alert("sensor count: "+sensorResult.length);
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
					Data.controllerSensorList.put(controllerName, sensors);
				}
			}
		});
	}
	
	private static void getActuatorList(final String controllerName){
//		Window.alert("getting actuators");
		Utility.newRequestObj().getActuatorList(controllerName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) 
			{
//				Window.alert("Unable to get actuator list");
			}
			public void onSuccess(String[][] actuatorResult)
			{
//				Window.alert("actuator count: "+actuatorResult.length);
				ArrayList<String> actuators = new ArrayList<>();
				if (actuatorResult!=null) {
					for(int i=0; i<actuatorResult.length;i++)
					{
						actuators.add(actuatorResult[i][0]);
						getActuatorRegularSchedule(actuatorResult[i][0]);
						getActuatorSpecialSchedule(actuatorResult[i][0]);
						
						ArrayList<String> actuatorAttributes = new ArrayList<>();
						for(int j=0; j<actuatorResult[i].length;j++)
						{
							actuatorAttributes.add(actuatorResult[i][j]);
						}
//						Window.alert("Actuator "+actuatorResult[i][0]+"->"+actuatorResult[i][2]);
						Data.actuatorAttributeList.put(actuatorResult[i][0], actuatorAttributes);
					} 
					Data.controllerActuatorList.put(controllerName, actuators);
				}
			}
		});
	}
	
	private static void getActuatorRegularSchedule(final String aName){
		
		Utility.newRequestObj().getActuatorRegularSchedule(aName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get regular schedule(s) for "+aName);
			}
			
			public void onSuccess(final String[][] result) {
				if (result!=null) {
					Data.regularScheduleAttributeSize=result[0].length;
					ArrayList<String> rSchedules = new ArrayList<>();
					for(int i=0; i<result.length;i++)
					{
						rSchedules.add(result[i][0]);
						ArrayList<String> rScheduleAttributes = new ArrayList<>();
						for(int j=0; j<result[i].length;j++)
						{
							rScheduleAttributes.add(result[i][j]);
						}
						Data.regularScheduleAttributeList.put(result[i][0], rScheduleAttributes);
					}
					Data.actuatorRegularScheduleList.put(aName, rSchedules);
				}
			}
		});
	}
	
	private static void getActuatorSpecialSchedule(final String aName){
		
		Utility.newRequestObj().getActuatorSpecialSchedule(aName, new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get special schedule(s) for "+aName);
			}
			
			public void onSuccess(final String[][] result) {
				if (result!=null) {
					Data.specialScheduleAttributeSize=result[0].length;
					ArrayList<String> sSchedules = new ArrayList<>();
					for(int i=0; i<result.length;i++)
					{
						sSchedules.add(result[i][0]);
						ArrayList<String> sScheduleAttributes = new ArrayList<>();
						for(int j=0; j<result[i].length;j++)
						{
							sScheduleAttributes.add(result[i][j]);
						}
						Data.specialScheduleAttributeList.put(result[i][0], sScheduleAttributes);
					}
					Data.actuatorSpecialScheduleList.put(aName, sSchedules);
				}
			}
		});
	}
	
	
}
