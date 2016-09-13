package com.google.gwt.sample.stockwatcher.client;

public class Pages{
	
	public static void enterWIPPage(){
		BasePage.panel.clear();
		BasePage.panel.add(WIP.start());
	}
	
	public static void enterLoginPage(){
		BasePage.panel.clear();
		BasePage.panel.add(LoginPage.start());
	}

	public static void enterMainMenuPage(){
		BasePage.panel.clear();
		BasePage.panel.add(MainMenuPage.start());
	}

	public static void enterMonitoringPage(){
		BasePage.panel.clear();
		BasePage.panel.add(MonitoringPage.start());
	}

	public static void enterSchedulePage(){
		BasePage.panel.clear();
		BasePage.panel.add(SchedulePage.start());
	}
	
	public static void enterSensorPage(String ImageURL){
		BasePage.panel.clear();
		BasePage.panel.add(SensorPage.start(ImageURL));
	}
}
