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
		Header.setHeaderTitle("");
		setHandlers();
		
		msgPanel.clear();
		msgPanel.addStyleName("mainStyle");
		msgPanel.add(new HTML(Messages.WELCOME));
		
		linksPanel.clear();
		linksPanel.addStyleName("mainStyle");
		linksPanel.setSpacing(5);
		linksPanel.add(forgotPassword);
		linksPanel.add(makeNewAccount);
		
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		wrapper.setSpacing(10);
		wrapper.add(msgPanel);
		wrapper.add(new HTML(enterUsernameMsg));
		wrapper.add(usernameTB);
		wrapper.add(new HTML(enterPasswordMsg));
		wrapper.add(passwordTB);
		wrapper.add(loginButton);
		
		mainPanel.clear();
		mainPanel.addStyleName("mainStyle");
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(wrapper);

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
			ResourcePreload.preloadData();
			
//			Pages.enterMainMenuPage();
//			Menu.start();
			
			Utility.newRequestObj().userLogin(usernameTB.getText(),passwordTB.getText(), new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					Window.alert("Unable to connect: Error Mesage - "+caught.getMessage());
				}
				
				//Remember to use Object[] input to get the rest of the information for chart display
				public void onSuccess(Boolean result) {
					if(result)
					{
						Data.currentUser = usernameTB.getText();
						
						msgPanel.clear();
						msgPanel.add(new HTML(Images.getImage(Images.LOADING_FLASK,Window.getClientWidth(),Window.getClientHeight()-Menu.HEIGHT-Footer.HEIGHT-50)));
//						msgPanel.add(new HTML(Images.getImage(Images.LOADING_EPIC,Window.getClientHeight()-Menu.HEIGHT-Footer.HEIGHT)));
						
						mainPanel.clear();
						mainPanel.getElement().getStyle().setBackgroundColor("black");
						mainPanel.setSize("100%", "100%");
						mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						mainPanel.add(msgPanel);
						
//						Pages.enterMainMenuPage();
//						Menu.start();
						
						Timer t = new Timer() {
						      @Override
						      public void run() {
						    	  new Utility.ElementFader().fade(mainPanel.getElement(), 1, 0, 1000);
									Pages.enterMainMenuPage();
									Menu.start();
						      }
						    };

						// Schedule the timer to run once in 2-4 seconds.
						t.schedule(new Random().nextInt(2001)+2000);
					}
					else
					{
						Window.alert("No such username/password combination found");
					}
				}
			});
		}
	}
}


