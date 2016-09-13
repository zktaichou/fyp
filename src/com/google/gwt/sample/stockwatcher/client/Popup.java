package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Popup{

	static HTML forgotPasswordMsg = new HTML("Please enter your username and e-mail address to reset your password");
	static VerticalPanel panel = new VerticalPanel();
	static TextBox usernameBox = new TextBox();
	static TextBox emailBox = new TextBox();
	static PasswordTextBox passwordBox = new PasswordTextBox();
	static DialogBox passwordDB = new DialogBox();
	static DialogBox newAccountDB = new DialogBox();
	
	public static void forgotPassword(){
		final Button cancel = new Button("Cancel");
		final Button accept = new Button("Accept");
		
		cancel.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				passwordDB.setVisible(false);
				};
			});
		
		accept.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				passwordDB.setVisible(false);
				};
			});
		
		if(!passwordDB.isAttached())
		{
		BasePage.panel.add(passwordDB);
		}
		
		if(!passwordDB.isVisible())
		{
			passwordDB.setVisible(true);
		}
		
		HorizontalPanel usernamePanel = new HorizontalPanel();
		usernamePanel.add(new HTML("Username: "));
		usernamePanel.add(usernameBox);
		
		HorizontalPanel emailPanel = new HorizontalPanel();
		emailPanel.add(new HTML("Email: "));
		emailPanel.add(emailBox);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(cancel);
		buttonPanel.add(accept);

		panel.clear();
		panel.add(forgotPasswordMsg);
		panel.add(usernamePanel);
		panel.add(emailPanel);
		panel.add(buttonPanel);

		passwordDB.clear();
		passwordDB.add(panel);
	}

	public static void makeNewAccount(){
		final Button cancel = new Button("Cancel");
		final Button accept = new Button("Accept");
		
		cancel.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				newAccountDB.setVisible(false);
				};
			});
		
		accept.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				newAccountDB.setVisible(false);
				};
			});
		
		if(!newAccountDB.isAttached())
		{
		BasePage.panel.add(newAccountDB);
		}
		
		if(!newAccountDB.isVisible())
		{
			newAccountDB.setVisible(true);
		}
		
		HorizontalPanel usernamePanel = new HorizontalPanel();
		usernamePanel.add(new HTML("New Username: "));
		usernamePanel.add(usernameBox);
		
		HorizontalPanel passwordPanel = new HorizontalPanel();
		passwordPanel.add(new HTML("New Password: "));
		passwordPanel.add(passwordBox);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(cancel);
		buttonPanel.add(accept);

		panel.clear();
		panel.add(usernamePanel);
		panel.add(passwordPanel);
		panel.add(buttonPanel);

		newAccountDB.clear();
		newAccountDB.add(panel);
	}

}
