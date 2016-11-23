package com.google.gwt.sample.stockwatcher.client;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Utility{
	
	private static final LinkedList<GreetingServiceAsync> request = new LinkedList<>();
	private static HorizontalPanel loadingPanel = new HorizontalPanel();
	private static String defaultMsg = "Loading data...";
	private static String msg = "Loading data...";
	
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
		if(!msg.equals(defaultMsg))
			msg=defaultMsg;
		
		timerCount = 0;
		loadingTimer.scheduleRepeating(1000);
		
		Anchor pic = new Anchor(" ");
		pic.setHTML(Images.getImage(Images.LOADING2, 20));
		
		loadingPanel.clear();
		loadingPanel.setSpacing(5);
		loadingPanel.getElement().getStyle().setColor("black");
		loadingPanel.add(pic);
		loadingPanel.add(new HTML(msg));

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
	
	public static Boolean isNull(String[][] result){
		return result==null;
	}
	
	public static Boolean isNull(String result){
		return result==null;
	}
	
	public static boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	} 
	
	public static void setMsg(String myMessage){
		loadingPanel.clear();
		loadingPanel.add(new HTML(myMessage));
	}
	
	public static class ElementFader {

	    private int stepCount;

	    public ElementFader() {
	        this.stepCount = 0;
	    }

	    private void incrementStep() {
	        stepCount++;
	    }

	    private int getStepCount() {
	        return stepCount;
	    }

	    public void fade(final Element element, final float startOpacity, final float endOpacity, int totalTimeMillis) {
	        final int numberOfSteps = 30;
	        int stepLengthMillis = totalTimeMillis / numberOfSteps;

	        stepCount = 0;

	        final float deltaOpacity = (float) (endOpacity - startOpacity) / numberOfSteps;

	        Timer timer = new Timer() {

	            @Override
	            public void run() {
	                float opacity = startOpacity + (getStepCount() * deltaOpacity);
	                DOM.setStyleAttribute(element, "opacity", Float.toString(opacity));

	                incrementStep();
	                if (getStepCount() == numberOfSteps) {
	                    DOM.setStyleAttribute(element, "opacity", Float.toString(endOpacity));
	                    this.cancel();
	                }
	            }
	        };

	        timer.scheduleRepeating(stepLengthMillis);
	    }
	}
	
}
