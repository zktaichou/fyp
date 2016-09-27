package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Utility{
	
	private static final LinkedList<GreetingServiceAsync> request = new LinkedList<>();
	
	public static GreetingServiceAsync newRequestObj () {
		if (request.size()==0) return GWT.create(GreetingService.class);
		else return request.removeFirst();
	}
	
	static int timerCount=0;
	static DialogBox loadingPopup= new DialogBox();
	
	static Timer loadingTimer = new Timer() {
		  public void run() {
			loadingPopup.setPopupPosition(Window.getClientWidth()/2,Window.getClientHeight()/2);
			timerCount++;
		  }
	};

	public static DialogBox addTimer(){
		timerCount = 0;
		loadingTimer.scheduleRepeating(1000);
		
		Anchor pic = new Anchor(" ");
		pic.setHTML(Images.getImage(Images.LOADING2, 20));

		HorizontalPanel loadingPanel = new HorizontalPanel();
		loadingPanel.setSpacing(5);
		loadingPanel.add(pic);
		loadingPanel.add(new HTML("Loading data..."));

		loadingPopup.clear();
		loadingPopup.getElement().getStyle().setBorderWidth(0, Unit.PX);
		loadingPopup.add(loadingPanel);
		return loadingPopup;
	}

	public static void hideTimer(){
		timerCount = 0;
		loadingPopup.removeFromParent();
		loadingTimer.cancel();
	}
	
}
