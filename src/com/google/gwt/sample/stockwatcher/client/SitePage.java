package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class SitePage extends Composite {

	ContentPanel cPanel = new ContentPanel();
	
	VerticalPanel parameterPanel = new VerticalPanel();

	ListBox siteListBox = new ListBox();
	
	Button backButton = new Button("Back");
	
	static Image sitePic = new Image();
	
	static ArrayList<PopupPanel> sensorIcons = new ArrayList<>();
	
	public SitePage(){
		
		setHandlers();
		
		VerticalPanel selectionPanel = new VerticalPanel();

		selectionPanel.add(new HTML("<h2>Selection Menu</h2></br>"));
		selectionPanel.add(new HTML("Please select site(s):"));
		selectionPanel.add(siteListBox);
		selectionPanel.setSpacing(10);
		
		parameterPanel.clear();
		parameterPanel.add(selectionPanel);
		parameterPanel.setHeight("100%");
		parameterPanel.setStyleName("parameterPanel");
		
		cPanel.clear();
		cPanel.addLeft(parameterPanel);
		cPanel.add(sitePic);
		
		initWidget(cPanel);
		}
	
	public void renderSiteListBox(){
		siteListBox.clear();
		int count=0;
		for(String siteName : Data.siteList.keySet())
		{
			siteListBox.addItem(siteName);
			siteListBox.setValue(count++, Data.siteList.get(siteName));
		}
	}
	
	public void setHandlers(){

		renderSiteListBox();
		
		for (int i=0;i<siteListBox.getItemCount();i++) {
			siteListBox.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event){
					cPanel.add(Utility.addTimer());
					hideAllIcons();
					updateIcons();
				};
			});
		}
	}
	
	public void getPic(){
		sitePic.setUrl(siteListBox.getSelectedValue());
		double width=sitePic.getWidth();
		double height=sitePic.getHeight();
		double screenWidth=Window.getClientWidth()*0.5;
		double screenHeight=Window.getClientHeight()*0.5;
		double resizeFactor=Math.min((screenWidth+0.0)/width,(screenHeight+0.0)/height);
		int newW=(int)(width*resizeFactor); int newH=(int)(height*resizeFactor);
		sitePic.setSize(newW+"px",newH+"px");	
		sitePic.setVisible(true);
	}
	
	public void updateIcons(){
		Utility.newRequestObj().getSiteControllerList(siteListBox.getSelectedItemText(), new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get site list");
			}
			
			//Remember to use Object[] input to get the rest of the information for chart display
			public void onSuccess(final String[][] result) {
				Utility.hideTimer();

				getPic();
				
				final HashMap<Boolean,String> toggle=new HashMap<>();
				toggle.put(Boolean.TRUE,Images.getImage(Images.ON, 25));
				toggle.put(Boolean.FALSE,Images.getImage(Images.OFF, 25));
				for(int i=0; i<result.length;i++)
				{
					final Anchor temp = new Anchor(" ");
					temp.getElement().getStyle().setProperty("toggleStatus","ON");
					temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));

					final PopupPanel container = new PopupPanel();
					
					sensorIcons.add(container);
					
					container.add(temp);
					container.getElement().getStyle().setBackgroundColor("rgba(255,0,0,0.0)");
					container.getElement().getStyle().setBorderWidth(0,Unit.PX);
					container.setTitle(result[i][0]+"");
					//container.setPopupPosition(sitePic.getAbsoluteLeft()+(int)(Double.parseDouble(result[i][2])*sitePic.getOffsetWidth()), sitePic.getAbsoluteTop()+(int)(Double.parseDouble(result[i][3])*sitePic.getOffsetHeight()));
					final int zzz=i;
					container.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			            public void setPosition(int offsetWidth, int offsetHeight) {
			              int left = sitePic.getAbsoluteLeft()+(int)(Double.parseDouble(result[zzz][2])*sitePic.getWidth());
			              int top = sitePic.getAbsoluteTop()+(int)(Double.parseDouble(result[zzz][3])*sitePic.getHeight());
			              container.setPopupPosition(left, top);
			            }
			          });
					
					temp.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event){
							temp.getElement().getStyle().setProperty("toggleStatus",String.valueOf(!Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
							temp.setHTML(toggle.get(Boolean.parseBoolean(temp.getElement().getStyle().getProperty("toggleStatus"))));
							
						}
					});
				}
			}
		});
	}
	
	public static void hideAllIcons(){
		for(PopupPanel panels: sensorIcons){
			panels.hide();
		}
		sensorIcons.clear();
		sitePic.setVisible(false);
	}
	
}
