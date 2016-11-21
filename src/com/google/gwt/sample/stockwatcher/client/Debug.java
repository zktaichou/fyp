package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;

public class Debug{
	static DialogBox popup = new DialogBox();
	static Anchor debug = new Anchor(" ");
	
	static Timer timer = new Timer()
    {
        @Override
        public void run()
        {
    		popup.setVisible(false);
        }
    };
	
	public static void show(){
		debug.setHTML(Images.getImage(Images.DEBUG,300));
		popup.clear();
		popup.add(debug);
		popup.setVisible(true);
		popup.center();
		
//	    timer.schedule(5000);
	}
	
	public static void stop(){
		popup.clear();
		popup.setVisible(false);
//		timer.cancel();
	}
	
}

