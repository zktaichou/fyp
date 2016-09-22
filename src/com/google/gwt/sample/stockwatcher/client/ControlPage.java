package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class ControlPage  {
	
	VerticalPanel mainPanel = new VerticalPanel();
	VerticalPanel photoPanel = new VerticalPanel();
	Button backButton = new Button("Back");
	ArrayList<Anchor> photos = new ArrayList<Anchor>();
	static ListBox siteLB = new ListBox();
	Anchor pic1 = new Anchor("  ");
	Anchor pic2 = new Anchor("  ");
	Anchor pic3 = new Anchor("  ");
	Image sitePic = new Image();
	ArrayList<PopupPanel> sensorIcons = new ArrayList<>();
	
	public ControlPage(){
		setPhotosURL();
		setHandlers();
		
		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(20);
		mainPanel.add(backButton);
		mainPanel.add(siteLB);
		mainPanel.add(sitePic);
		}

	public static VerticalPanel start(){
		ControlPage temp = new ControlPage();
		return temp.mainPanel;
	}
	
	public void setHandlers(){
		backButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				for(PopupPanel panels: sensorIcons){
					panels.hide();
				}
				Pages.enterMainMenuPage();
				};
			});
		
		for (Anchor pics : photos) {
			final String URL = pics.getHTML();
			pics.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event){
					Pages.enterSensorPage(URL);
					};
				});
		}
		
		for (int i=0;i<siteLB.getItemCount();i++) {
			siteLB.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event){
					for(PopupPanel panels: sensorIcons){
						panels.hide();
					}
					
					sensorIcons.clear();
					
					sitePic.setUrl(siteLB.getSelectedValue());
					double width=sitePic.getWidth();
					double height=sitePic.getHeight();
					double screenWidth=Window.getClientWidth()*0.5;
					double screenHeight=Window.getClientHeight()*0.5;
					double resizeFactor=Math.min((screenWidth+0.0)/width,(screenHeight+0.0)/height);
					int newW=(int)(width*resizeFactor); int newH=(int)(height*resizeFactor);
					sitePic.setSize(newW+"px",newH+"px");	
					
					Utility.newRequestObj().getSiteControllerList(siteLB.getSelectedItemText(), new AsyncCallback<String[][]>() {
						public void onFailure(Throwable caught) {
							Window.alert("Unable to get site list");
						}
						
						//Remember to use Object[] input to get the rest of the information for chart display
						public void onSuccess(final String[][] result) {
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
				};
			});
		}
	}
	
	public void setPhotosURL(){
		photoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		photoPanel.setSpacing(20);
		
		pic1.setHTML(Images.getImage(Images.SAMPLE_1, Window.getClientHeight()/3));
		pic2.setHTML(Images.getImage(Images.SAMPLE_2, Window.getClientHeight()/3));
		pic3.setHTML(Images.getImage(Images.SAMPLE_3, Window.getClientHeight()/3));
		
		photos.add(pic1);
		photos.add(pic2);
		photos.add(pic3);
		
		for (Anchor pics : photos) {
			photoPanel.add(pics);
		}
	}
	
}
