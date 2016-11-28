package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SubscriptionRequest{
	
	public static void getSubscribedSensor(String user){
		Utility.newRequestObj().userGetSubscribedSensors(user, new AsyncCallback<String[][]>(){
			public void onFailure(Throwable caught) {
//				Window.alert("Unable to get "+Data.currentUser+"'s sensor subscription");
			} 
 
			public void onSuccess(String[][] reply) {
				if(reply!=null)
				{
					Data.subscribedSensorList.clear();
					for(int i=0;i<reply.length;i++)
					{
						Data.subscribedSensorList.add(reply[i][1]);
					}
				}
			}
		});
	}
	
}
