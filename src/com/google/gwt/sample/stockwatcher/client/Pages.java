package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.Timer;

public class Pages{
	
	public static void enterLoginPage(){
		stopGaugeTimers();
		Menu.arrowAnchor.setVisible(false);
		LoginPage loginPage = new LoginPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(loginPage);
	}

	public static void enterMainMenuPage(){
		stopGaugeTimers();
		Menu.arrowAnchor.setVisible(true);
		MainMenuPage mainMenuPage = new MainMenuPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(mainMenuPage);
	}

	public static void enterMonitoringPage(){
		stopGaugeTimers();
		Menu.arrowAnchor.setVisible(false);
		MonitoringPage monitoringPage = new MonitoringPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(monitoringPage);
	}

	public static void enterReportingPage(){
		stopGaugeTimers();
		Menu.arrowAnchor.setVisible(false);
		ReportingPage reportingPage = new ReportingPage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(reportingPage);
	}

	public static void enterSitePage(){
		stopGaugeTimers();
		Menu.arrowAnchor.setVisible(false);
		SitePage sitePage = new SitePage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(sitePage);
	}
	
	public static void enterSchedulePage(){
		stopGaugeTimers();
		Menu.arrowAnchor.setVisible(false);
		SchedulePage schedulePage = new SchedulePage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(schedulePage);
	}
	
	public static void enterUserNotificationPage(){
		stopGaugeTimers();
		Menu.arrowAnchor.setVisible(false);
		UserNotificationPage userNotificationPage = new UserNotificationPage(Data.currentUser);
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(userNotificationPage);
	}
	
	public static void enterSensorActuatorResponsePage(){
		stopGaugeTimers();
		Menu.arrowAnchor.setVisible(false);
		SensorActuatorResponsePage sensorActuatorResponsePage = new SensorActuatorResponsePage();
		
		BasePage.contentPanel.clear();
		BasePage.contentPanel.add(sensorActuatorResponsePage);
	}
	
	private static void stopGaugeTimers(){
		for(Timer t: Data.gaugeTimers)
		{
			t.cancel();
		}
	}
}
