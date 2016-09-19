package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPage  { 
	
	VerticalPanel mainPanel = new VerticalPanel();
	HorizontalPanel linksPanel = new HorizontalPanel();
	TextBox usernameTB = new TextBox();
	PasswordTextBox passwordTB = new PasswordTextBox();
	String welcomeMsg = "<h1>Welcome to the Utility Monitoring via IoT Home Page!</h1>";
	String enterUsernameMsg = "Please enter your username: ";
	String enterPasswordMsg = "Please enter your password: ";
	Anchor forgotPassword = new Anchor("Forgot password");
	Anchor makeNewAccount = new Anchor("Request new account");
	Button loginButton = new Button("Login");
	public static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public LoginPage(){
		setHandlers();
		
		linksPanel.add(forgotPassword);
		linksPanel.add(makeNewAccount);
		linksPanel.setSpacing(5);
		
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(new HTML(welcomeMsg));
		mainPanel.add(new HTML(enterUsernameMsg));
		mainPanel.add(usernameTB);
		mainPanel.add(new HTML(enterPasswordMsg));
		mainPanel.add(passwordTB);
		mainPanel.add(linksPanel);
		mainPanel.add(loginButton);
	}
 
	public static VerticalPanel start(){
		LoginPage temp = new LoginPage();
		return temp.mainPanel;
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
			
			greetingService.userLogin(usernameTB.getText(),passwordTB.getText(), new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					Window.alert("Can't connect to database");
				}
				
				//Remember to use Object[] input to get the rest of the information for chart display
				public void onSuccess(Boolean result) {
					if(result)
					{
						Pages.enterMainMenuPage();
					}
					else
					{
						Window.alert("No such account exists");
					}
				}
			});
		}
		
	}
	
}


