package com.google.gwt.sample.stockwatcher.client;

import java.util.Random;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPage extends Composite { 
	ContentPanel cPanel = new ContentPanel();
	VerticalPanel mainPanel = new VerticalPanel();
	VerticalPanel msgPanel = new VerticalPanel();
	HorizontalPanel linksPanel = new HorizontalPanel();
	TextBox usernameTB = new TextBox();
	PasswordTextBox passwordTB = new PasswordTextBox();
	String enterUsernameMsg = "Please enter your username: ";
	String enterPasswordMsg = "Please enter your password: ";
	Anchor forgotPassword = new Anchor("Forgot password");
	Anchor makeNewAccount = new Anchor("Request new account");
	Button loginButton = new Button("Login");
	
	public static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public LoginPage(){
		setHandlers();
		
		msgPanel.clear();
		msgPanel.addStyleName("mainStyle");
		msgPanel.add(new HTML(Messages.WELCOME));
		
		linksPanel.clear();
		linksPanel.addStyleName("mainStyle");
		linksPanel.setSpacing(5);
		linksPanel.add(forgotPassword);
		linksPanel.add(makeNewAccount);
		
		mainPanel.clear();
		mainPanel.addStyleName("mainStyle");
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setSpacing(3);
		mainPanel.add(msgPanel);
		mainPanel.add(new HTML(enterUsernameMsg));
		mainPanel.add(usernameTB);
		mainPanel.add(new HTML(enterPasswordMsg));
		mainPanel.add(passwordTB);
		mainPanel.add(linksPanel);
		mainPanel.add(loginButton);
//		mainPanel.add(ChartUtilities.realTimeUpdatesChart());
//		mainPanel.add(new HTML(Images.getImage(Images.GRAPH)));

		cPanel.clear();
		cPanel.addStyleName("mainStyle");
		cPanel.add(mainPanel);
		
		Menu.standby();
		
		initWidget(cPanel);
	}
	
	public void setHandlers(){
		MyHandler handler = new MyHandler();
		usernameTB.addKeyUpHandler(handler);
		passwordTB.addKeyUpHandler(handler);
		loginButton.addClickHandler(handler);
		
		forgotPassword.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Popup.forgotPassword();
				};
			});
		
		makeNewAccount.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Popup.makeNewAccount();
				};
			});
	}
	
	
	
	class MyHandler implements ClickHandler, KeyUpHandler {
		
		public void onClick(ClickEvent event) {
			checkLoginInfo();
		}

		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				checkLoginInfo();
			}
		}

		public void checkLoginInfo(){
			ResourcePreload.getSiteList();
			
			msgPanel.clear();
			msgPanel.add(new HTML(Images.getImage(Images.LOADING_EPIC,Window.getClientHeight()-Menu.HEIGHT-Footer.HEIGHT)));
			
			mainPanel.clear();
			mainPanel.getElement().getStyle().setBackgroundColor("black");
			mainPanel.setSize("100%", "100%");
			mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			mainPanel.add(msgPanel);
			Pages.enterMainMenuPage();
			Menu.start();
//			Utility.newRequestObj().userLogin(usernameTB.getText(),passwordTB.getText(), new AsyncCallback<Boolean>() {
//				public void onFailure(Throwable caught) {
//					Window.alert("Can't connect to database");
//				}
//				
//				//Remember to use Object[] input to get the rest of the information for chart display
//				public void onSuccess(Boolean result) {
//					if(result)
//					{
//						Timer t = new Timer() {
//						      @Override
//						      public void run() {
//									Pages.enterMainMenuPage();
//									Menu.start();
//						      }
//						    };
//
//						    // Schedule the timer to run once in 5 seconds.
//						    t.schedule(new Random().nextInt(2001)+2000);
//					}
//					else
//					{
//						Window.alert("No such username/password combination found");
//					}
//				}
//			});
		}
		
	}
	
}


