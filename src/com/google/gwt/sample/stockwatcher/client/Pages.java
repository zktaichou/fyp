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
}
