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

	public static void enterReportingPage(){
		ReportingPage reportingPage = new ReportingPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(reportingPage);
	}

	public static void enterSitePage(){
		SitePage sitePage = new SitePage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(sitePage);
	}
	
	public static void enterSchedulePage(){
		SchedulePage schedulePage = new SchedulePage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(schedulePage);
	}
	
	public static void enterUserNotificationPage(){
		UserNotificationPage userNotificationPage = new UserNotificationPage(Data.currentUser);
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(userNotificationPage);
	}
	
	public static void enterSensorActuatorResponsePage(){
		SensorActuatorResponsePage sensorActuatorResponsePage = new SensorActuatorResponsePage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(sensorActuatorResponsePage);
	}
}
