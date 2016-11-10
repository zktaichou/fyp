package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PopupPanel;

public class Debug{
	static PopupPanel popup = new PopupPanel();
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
		popup.setVisible(false);
		popup.center();
		
//	    timer.schedule(5000);
	}
	
	public static void stop(){
		popup.setVisible(false);
//		timer.cancel();
	}
	
}

