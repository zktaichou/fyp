package com.google.gwt.sample.stockwatcher.client;

import java.sql.Date;
import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;

public class Menu extends Composite {
	// Menu Height
	public static final int HEIGHT = 50;
	private static final String SYSTEM_TITLE = "Utility Monitoring System";

	// Main Menu Panel/Bar
	private VerticalPanel mainMenuPanel = new VerticalPanel();
	private static VerticalPanel optionsPanel = new VerticalPanel();
	private static HorizontalPanel selectionPanel = new HorizontalPanel();
	
	// Main Menu Bar GWT Components
	private Anchor fsktmLogo = new Anchor();
	private Label systemTitleLabel = new Label("");

	// Main Menu Bar Containers
	private VerticalPanel settingsModule = new VerticalPanel();
	private VerticalPanel monitoringModule = new VerticalPanel();
	private VerticalPanel planningModule = new VerticalPanel();
	private VerticalPanel optionsModule = new VerticalPanel();
	
	// Main Menu Submenu Popups
	ArrayList<PopupPanel> popupList = new ArrayList<PopupPanel>();
	private PopupPanel settingsPopupMenu = new PopupPanel();
	private PopupPanel monitoringPopupMenu = new PopupPanel();
	private PopupPanel planningPopupMenu = new PopupPanel();
	private PopupPanel optionsPopupMenu = new PopupPanel();
	
	// Menu Items
	Anchor logoutAnchor = new Anchor("logout");
	Anchor logicAnchor = new Anchor("Sensor/Actuator Logic");
	Anchor userNotificationAnchor = new Anchor("User Notification");
	Anchor actuatorAnchor = new Anchor("Actuator control");
	Anchor scheduleAnchor = new Anchor("Scheduling");
	Anchor liveAnchor = new Anchor("Live Updates");
	Anchor historicalAnchor = new Anchor("Historical");
	static Anchor cNotificationAnchor = new Anchor(" ");
	static Anchor sNotificationAnchor = new Anchor(" ");
	static Anchor aNotificationAnchor = new Anchor(" ");
	
	String verticalLine = "<hr width=\"1\" size=\"30\">";
	
	public static void standby(){
		selectionPanel.setVisible(false);
		optionsPanel.setVisible(false);
		cNotificationAnchor.setVisible(false);
		sNotificationAnchor.setVisible(false);
		aNotificationAnchor.setVisible(false);
	}
	
	public static void start(){
		selectionPanel.setVisible(true);
		optionsPanel.setVisible(true);
		NotificationServer.start();
	}
	
	// Menu Constructor
	public Menu() {
		// Set up menu items
		setupAnchors();
		// Render the main menu and its sub menus
		selectionPanel.add(new HTML(verticalLine));
		renderMonitoringMenu("Monitoring");
		renderPlanningMenu("Planning");
		renderSettingsMenu("Settings");
		renderOptionsMenu(Images.getImage(Images.GEAR_ICON,Menu.HEIGHT));
		renderNotificationAnchor();
		
		// Set up logo and title
		fsktmLogo.setHTML(Images.getImage(Images.FSKTM_LOGO,Menu.HEIGHT));
		systemTitleLabel.setText(SYSTEM_TITLE);
		systemTitleLabel.addStyleName("gwt-MainMenuTitle");
		
		HorizontalPanel leftMenuContainerPanel = new HorizontalPanel();
		leftMenuContainerPanel.add(new HTML(Images.getImage(Images.LOADING_EPIC2,Menu.HEIGHT)));
		leftMenuContainerPanel.add(fsktmLogo);
		leftMenuContainerPanel.add(systemTitleLabel);
		leftMenuContainerPanel.add(selectionPanel);
		leftMenuContainerPanel.setCellVerticalAlignment(fsktmLogo, HasVerticalAlignment.ALIGN_MIDDLE);
		leftMenuContainerPanel.setCellVerticalAlignment(systemTitleLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		leftMenuContainerPanel.setCellVerticalAlignment(selectionPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		HorizontalPanel rightMenuContainerPanel = new HorizontalPanel();
		rightMenuContainerPanel.add(cNotificationAnchor);
		rightMenuContainerPanel.add(sNotificationAnchor);
		rightMenuContainerPanel.add(aNotificationAnchor);
		rightMenuContainerPanel.add(optionsPanel);
		rightMenuContainerPanel.setWidth("100%");
		rightMenuContainerPanel.setCellHorizontalAlignment(cNotificationAnchor, HasHorizontalAlignment.ALIGN_RIGHT);
		rightMenuContainerPanel.setCellHorizontalAlignment(sNotificationAnchor, HasHorizontalAlignment.ALIGN_RIGHT);
		rightMenuContainerPanel.setCellHorizontalAlignment(aNotificationAnchor, HasHorizontalAlignment.ALIGN_RIGHT);
		rightMenuContainerPanel.setCellHorizontalAlignment(optionsPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		// Container Panel for Main Menu Panel
		HorizontalPanel menuContainerPanel = new HorizontalPanel();
		menuContainerPanel.add(leftMenuContainerPanel);
		menuContainerPanel.add(rightMenuContainerPanel);
		menuContainerPanel.setCellVerticalAlignment(leftMenuContainerPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		menuContainerPanel.setCellVerticalAlignment(rightMenuContainerPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		menuContainerPanel.setCellHorizontalAlignment(rightMenuContainerPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		menuContainerPanel.setWidth("100%");

		// Set up main menu panel
		mainMenuPanel.setWidth("100%");
		mainMenuPanel.getElement().getStyle().setHeight(HEIGHT, Unit.PX);
		mainMenuPanel.addStyleName("gwt-MainMenuPanel");
		mainMenuPanel.add(menuContainerPanel);
		mainMenuPanel.setCellVerticalAlignment(menuContainerPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		mainMenuPanel.setCellHeight(menuContainerPanel, "100%");
		
		initWidget(mainMenuPanel);
	}

	// Setup Menu Item Links
	private void setupAnchors() {
		actuatorAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterSitePage();
			};
		});
		scheduleAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterSchedulePage();
			};
		});
		liveAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterMonitoringPage();
			};
		});
		historicalAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterReportingPage();
			};
		});
		logoutAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				hidePopups();
				NotificationServer.stop();
				Pages.enterLoginPage();
			};
		});
		logicAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterSensorActuatorResponsePage();
			};
		});
		
		userNotificationAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Pages.enterUserNotificationPage();
			};
		});
		cNotificationAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				NotificationServer.cPopup.setVisible(true);
				NotificationServer.cPopup.center();
				cNotificationAnchor.setVisible(false);
				
				for(int i=0; i<Data.subscribedControllerList.size(); i++)
				{
					Utility.newRequestObj().userUpdateControllerNotificationLastReadTime(Data.currentUser, Data.subscribedControllerList.get(i), new Date(System.currentTimeMillis()), new AsyncCallback<String>(){
						public void onFailure(Throwable caught) {
//							Window.alert("Unable to update controller last read time");
						} 
			 
						public void onSuccess(String reply) {
//							Window.alert("updated controller last read time!");
						}
					});
				}
			};
		});
		sNotificationAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				NotificationServer.sPopup.setVisible(true);
				NotificationServer.sPopup.center();
				sNotificationAnchor.setVisible(false);
				
				for(int i=0; i<Data.subscribedSensorList.size(); i++)
				{
					Utility.newRequestObj().userUpdateSensorNotificationLastReadTime(Data.currentUser, Data.subscribedSensorList.get(i), new Date(System.currentTimeMillis()), new AsyncCallback<String>(){
						public void onFailure(Throwable caught) {
//							Window.alert("Unable to update sensor last read time");
						} 
			 
						public void onSuccess(String reply) {

						}
					});
				}
			};
		});
		aNotificationAnchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				NotificationServer.aPopup.setVisible(true);
				NotificationServer.aPopup.center();
				aNotificationAnchor.setVisible(false);
				
				for(int i=0; i<Data.subscribedActuatorList.size(); i++)
				{
					Utility.newRequestObj().userUpdateActuatorNotificationLastReadTime(Data.currentUser, Data.subscribedActuatorList.get(i), new Date(System.currentTimeMillis()), new AsyncCallback<String>(){
						public void onFailure(Throwable caught) {
//							Window.alert("Unable to update actuator last read time");
						} 
			 
						public void onSuccess(String reply) {

						}
					});
				}
			};
		});
	}
	
	private void renderSettingsMenu(String _title) {
		
		//Add new vertical sections here
		VerticalMenu reportingMenu = new VerticalMenu();
		
		reportingMenu.addMenu("Settings Menu");
		reportingMenu.addAnchor(logicAnchor);
		reportingMenu.addAnchor(userNotificationAnchor);
		
		//Add content panel to popup panel
		settingsPopupMenu.add(reportingMenu.getMenus());
		
		//Set main title and attach popup
		settingsModule = addNewModule(_title, settingsPopupMenu);
		
		selectionPanel.add(settingsModule);
		selectionPanel.setCellVerticalAlignment(settingsModule, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	private void renderMonitoringMenu(String _title) {
		
		//Add new vertical sections here
		VerticalMenu reportingMenu = new VerticalMenu();
		
		reportingMenu.addMenu("Report Menu");
		reportingMenu.addAnchor(liveAnchor);
		reportingMenu.addAnchor(historicalAnchor);
		
		//Add content panel to popup panel
		monitoringPopupMenu.add(reportingMenu.getMenus());
		
		//Set main title and attach popup
		monitoringModule = addNewModule(_title, monitoringPopupMenu);
		
		selectionPanel.add(monitoringModule);
		selectionPanel.setCellVerticalAlignment(monitoringModule, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	private void renderPlanningMenu(String _title) {
		
		//Add new vertical sections here
		VerticalMenu planningMenu = new VerticalMenu();
		
		planningMenu.addMenu("Function Menu");
		planningMenu.addAnchor(actuatorAnchor);
		planningMenu.addAnchor(scheduleAnchor);
		
		//Add content panel to popup panel
		planningPopupMenu.add(planningMenu.getMenus());
		
		//Set main title and attach popup
		planningModule = addNewModule(_title, planningPopupMenu);
		
		selectionPanel.add(planningModule);
		selectionPanel.setCellVerticalAlignment(planningModule, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	private void renderOptionsMenu(String _title) {
		
		VerticalMenu optionsMenu = new VerticalMenu();
		Anchor anchor = new Anchor("   ");
		anchor.setHTML(Images.getImage(Images.OPTIONS, 30));
		
		optionsMenu.addAnchor(logoutAnchor);
		
		//Add content panel to popup panel
		optionsPopupMenu.add(optionsMenu.getMenus());
				
		//Set main title and attach popup
		optionsModule = addNewModule(anchor, optionsPopupMenu);
				
		optionsPanel.add(optionsModule);
	}
	
	private void renderNotificationAnchor() {
		cNotificationAnchor.setHTML(Images.getImage(Images.CONTROLLER_NOTIFICATION, 30));
		sNotificationAnchor.setHTML(Images.getImage(Images.SENSOR_NOTIFICATION, 30));
		aNotificationAnchor.setHTML(Images.getImage(Images.ACTUATOR_NOTIFICATION, 30));
	}
	
	// Render Menu Bars
//		private void renderHomeMenu(String _title){
//			VerticalPanel temp = new VerticalPanel();
//			temp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//			temp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//			temp.addStyleName("gwt-NewMainMenuPanel");
//			temp.addStyleName("pointer");
//			temp.setHeight("30px");
//			temp.setWidth("60px");
//			
//			temp.addDomHandler(new MouseOverHandler() {
//			    @Override
//			    public void onMouseOver(MouseOverEvent event) {
//			    	for(int i=0; i<popupList.size();i++)
//			    	{
//			    		popupList.get(i).hide();
//			    	}
//			    	event.getRelativeElement().getStyle().setBackgroundColor("#2EBEDB");
//			    }
//			}, MouseOverEvent.getType());
//			
//			temp.addDomHandler(new MouseOutHandler() {
//			    @Override
//			    public void onMouseOut(MouseOutEvent event) {
//			    	event.getRelativeElement().getStyle().setBackgroundColor("#000000");
//			    }
//			}, MouseOutEvent.getType());
//			
//			selectionPanel.add(temp);
//		}
	
	//Custom menu class and methods
	public class VerticalMenu {
		
		int count = -1;
		
		ArrayList<VerticalPanel> panelList = new ArrayList<VerticalPanel>();
		
		public VerticalMenu(){
			
		}
		
		public void addMenu(String title){
			
			Label label = new Label(title);
			label.addStyleName("gwt-NewMainMenuHeader");
			
			VerticalPanel panel = new VerticalPanel();
			panel.addStyleName("gwt-NewSubMenuPanel");
			panel.setSpacing(10);
			panel.setTitle(title);
			panel.add(label);
			panelList.add(panel);
			
			count++;
		}
		
		public void addAnchor(Anchor anchor){
			if(count==-1){
				try	{
					panelList.get(0).add(addNewAnchor(anchor));
				} catch(Exception e){
					VerticalPanel temp = new VerticalPanel();
					temp.addStyleName("gwt-NewMainMenuLabel");
					temp.setSpacing(7);
					temp.add(addNewAnchor(anchor));
					panelList.add(temp);
				}
			}
			else
			{
			panelList.get(count).add(addNewAnchor(anchor));
			}
		}
		
		public HorizontalPanel getMenus(){
			HorizontalPanel temp = new HorizontalPanel();
			
			for(int i=0; i<panelList.size();i++)
			{
				temp.add(panelList.get(i));
			}
			
			return temp;
		}
	}
	
	public VerticalPanel addNewAnchor(Anchor anchor){
		
		VerticalPanel panel = new VerticalPanel();
		panel.addStyleName("gwt-NewSubMenuPanel");
		panel.setSpacing(5);
		panel.add(anchor);
		
		panel.addDomHandler(new MouseOverHandler() {
		    @Override
		    public void onMouseOver(MouseOverEvent event) {
		    	event.getRelativeElement().getStyle().setBackgroundColor("#15B5EA");
		    }
		}, MouseOverEvent.getType());
		
		panel.addDomHandler(new MouseOutHandler() {
		    @Override
		    public void onMouseOut(MouseOutEvent event) {
		    	event.getRelativeElement().getStyle().setBackgroundColor("#000000");
		    }
		}, MouseOutEvent.getType());

		anchor.addStyleName("gwt-NewMainMenuLabel");
		
		//
//		anchor.addMouseOverHandler(new MouseOverHandler() {
//		    @Override
//		    public void onMouseOver(MouseOverEvent event) {
//		    	event.getRelativeElement().getStyle().setColor("#15B5EA");
//		    	event.getRelativeElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
//		    }
//		});
//		
//		anchor.addMouseOutHandler(new MouseOutHandler() {
//		    @Override
//		    public void onMouseOut(MouseOutEvent event) {
//		    	event.getRelativeElement().getStyle().setColor("white");
//		    	event.getRelativeElement().getStyle().setTextDecoration(TextDecoration.NONE);
//		    }
//		});
		
		return panel;
	}
	
	public VerticalPanel addNewModule(String temp, PopupPanel popup){
		
		Label label = new Label(temp);
		label.addStyleName("gwt-NewMainMenuHeader");
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.addStyleName("gwt-NewMainMenuPanel");
		mainPanel.setHeight("30px");
		mainPanel.add(label);
		
		mainPanel.addDomHandler(addShowPopupHandler(popup), MouseOverEvent.getType());

		setPopupParam(popup, mainPanel);
		popupList.add(popup);
		
		return mainPanel;
	}
	
	public VerticalPanel addNewModule(Anchor anchor, PopupPanel popup){
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.addStyleName("gwt-NewMainMenuPanel");
		mainPanel.setHeight("30px");
		mainPanel.add(anchor);
		
		mainPanel.addDomHandler(addShowPopupHandler(popup), MouseOverEvent.getType());

		setPopupParam(popup, mainPanel);
		popupList.add(popup);
		
		return mainPanel;
	}
	
	public VerticalPanel addNewSection(String title){
		Label label = new Label(title);
		label.addStyleName("gwt-NewMainMenuHeader");
		
		VerticalPanel temp = new VerticalPanel();
		temp.addStyleName("gwt-NewSubMenuPanel");
		temp.setSpacing(5);
		temp.add(label);
		temp.add(new HTML("<hr />"));
		
		return temp;
	}
	
	public MouseOverHandler addShowPopupHandler(final PopupPanel popupPanel) {
			MouseOverHandler handler = new MouseOverHandler() {
			    @Override
			    public void onMouseOver(MouseOverEvent event) {
			    	if (!popupPanel.isShowing())
			    	{
				    	for(int i=0; i<popupList.size();i++)
				    	{
				    		popupList.get(i).hide();
				    	}
				    	event.preventDefault();
				    	event.stopPropagation();
				    	event.getRelativeElement().getStyle().setBackgroundColor("#15B5EA");
				    	popupPanel.setPopupPosition(
			            		event.getRelativeElement().getAbsoluteLeft(),
			            		event.getRelativeElement().getAbsoluteBottom());
				    	popupPanel.show();
			    	}
			    }
			};
		  return handler;
		}
	
	public void setPopupParam(PopupPanel popup, final VerticalPanel vpanel){
		popup.addStyleName("gwt-PopUpPanel");
		popup.setAnimationEnabled(true);
		popup.setAnimationType(AnimationType.ROLL_DOWN);
		popup.setAutoHideEnabled(true);
		popup.getElement().getStyle().setBorderWidth(0, Unit.PX);
		popup.getElement().getStyle().setBackgroundColor("black");
		popup.hide();

		popup.addCloseHandler(new CloseHandler<PopupPanel>(){
		    @Override
		    public void onClose(CloseEvent<PopupPanel> event) {
		    	vpanel.getElement().getStyle().setBackgroundColor("#000000");
		    }
		});
	}
	
	private void hidePopups(){
		for(int i=0; i<popupList.size();i++)
    	{
    		popupList.get(i).hide();
    	}
	}
}

