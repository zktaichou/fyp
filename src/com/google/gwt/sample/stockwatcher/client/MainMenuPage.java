package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainMenuPage extends Composite{
	
	public static VerticalPanel mainPanel = new VerticalPanel();
	
	String selectTitle = "<h1>Please select the page that you want to enter</h1>";
	
	public MainMenuPage(){
		Header.setHeaderTitle("");
		mainPanel=new VerticalPanel();
		mainPanel.clear();
		mainPanel.setStyleName("mainStyle");
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(new HTML(Messages.MAIN_MENU));
		
		HorizontalPanel temp = new HorizontalPanel();
		Boolean flag = true;
		int count=0;
		for(String sensor: Data.subscribedSensorList)
		{
			flag=true;
			temp.add(ChartUtilities.createGaugeChart(sensor,Data.subscribedSensorList.size()-(count*4)));
			if(temp.getWidgetCount()==4)
			{
				mainPanel.add(temp);
				temp = new HorizontalPanel();
				flag=false;
				count++;
			}
		}
		if(flag)
		{
		mainPanel.add(temp);
		}
		
		initWidget(mainPanel);
	}
}
