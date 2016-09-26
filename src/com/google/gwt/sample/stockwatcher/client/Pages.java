package com.google.gwt.sample.stockwatcher.client;

public class Pages{
	
	public static void enterLoginPage(){
		LoginPage loginPage = new LoginPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(loginPage);
	}

	public static void enterMainMenuPage(){
		MainMenuPage mainMenuPage = new MainMenuPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(mainMenuPage);
	}

	public static void enterMonitoringPage(){
		MonitoringPage monitoringPage = new MonitoringPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(monitoringPage);
	}

	public static void enterControlPage(){
		ControlPage controlPage = new ControlPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(controlPage);
	}
	
	public static void enterSensorPage(String ImageURL){
		SensorPage sensorPage = new SensorPage(ImageURL);
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(sensorPage);
	}
}
