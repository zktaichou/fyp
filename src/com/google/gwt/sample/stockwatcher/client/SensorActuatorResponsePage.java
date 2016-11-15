package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SensorActuatorResponsePage extends Composite {
	
	ContentPanel cPanel = new ContentPanel();
	
	VerticalPanel tablePanel = new VerticalPanel();
	
	Button newResponseButton = new Button("Add New Actuator Response");
	Button cancelCreateResponseButton = new Button("Cancel");
	Button createResponseButton = new Button("Create");
	
	Button cancelupdateResponseButton = new Button("Cancel");
	Button updateResponseButton = new Button("Update");
	Button deleteResponseButton = new Button("Delete");
	
	TextBox expressionTB = new TextBox();
	TextBox timeoutTB = new TextBox();
	TextBox valueTB = new TextBox();
	
	FlexTable responseTable = new FlexTable();
	
	ListBox actuatorLB = new ListBox();
	ListBox sensorLB = new ListBox();
	ListBox responseEnabledLB = new ListBox();
	ListBox onTriggerLB = new ListBox();
	ListBox onNotTriggerLB = new ListBox();
	ListBox opLB = new ListBox();

	FilterMenu responseMenu = new FilterMenu();
	
	PopupPanel responsePopup = new PopupPanel();
	
	String currentSelectedID = "";

	int responseID;
	String aName;
	String onTrigger;
	String onNotTrigger;
	String expression;
	Boolean enabled;
	int timeout;
	
	public SensorActuatorResponsePage(){
		Header.setHeaderTitle("Main Menu > Settings > Sensor/Actuator Logic Control");
		
		setHandlers();
		setWidgets();
		
		tablePanel.clear();
		tablePanel.setStyleName("mainStyle");
		tablePanel.add(responseTable);

		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setStyleName("mainStyle");
		wrapper.setSpacing(10);
		wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		wrapper.add(new HTML("Reponse Logic Binding List"));
		wrapper.add(tablePanel);
		wrapper.add(newResponseButton);
		
		cPanel.clear();
		cPanel.add(wrapper);
		cPanel.alignMiddlePanelVTop();
		
		initWidget(cPanel); 
	}
	
	private void setWidgets(){
		
		onTriggerLB.clear();
		onTriggerLB.addItem("ON");
		onTriggerLB.addItem("OFF");

		onNotTriggerLB.clear();
		onNotTriggerLB.addItem("ON");
		onNotTriggerLB.addItem("OFF");
		
		opLB.clear();
		opLB.addItem(">");
		opLB.addItem("<");
		
		sensorLB.clear();
		for(String sensor: Data.sensorAttributeList.keySet())
		{
			sensorLB.addItem(sensor);
		}
		
		responsePopup.setGlassEnabled(true);
		responsePopup.add(responseMenu);
		
		getLatestResponseData();

		refreshResponseMenuWidgets();

		initializeNewResponseMenu();
		
	}
	
	private void setHandlers(){
		
		newResponseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				initializeNewResponseMenu();
				responsePopup.setVisible(true);
				responsePopup.center();
			}
		});
		
		createResponseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				setParam();
				sendCreateResponseRequest();
			}
		});
		
		cancelCreateResponseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				responsePopup.setVisible(false);
			}
		});
		
		cancelupdateResponseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				responsePopup.setVisible(false);
			}
		});
		
		updateResponseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				setParam();
				sendUpdateResponseRequest();
			}
		});
		
		deleteResponseButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				sendDeleteResponseRequest();
			}
		});
	}
	
	private void setParam(){
		aName = actuatorLB.getSelectedItemText();
		onTrigger = onTriggerLB.getSelectedItemText();
		onNotTrigger = onNotTriggerLB.getSelectedItemText();
		expression = sensorLB.getSelectedItemText()+expressionTB.getText();
		enabled = Boolean.parseBoolean(responseEnabledLB.getSelectedItemText());
		timeout = Integer.parseInt(timeoutTB.getText());
	}
	
	private void sendCreateResponseRequest(){
		Utility.newRequestObj().sensorActuatorResponseCreate(aName, onTrigger, onNotTrigger, expression, enabled, timeout, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to create response binding");
			}
			
			public void onSuccess(final String result) {
				if(Integer.parseInt(result)>=0)
				{
					Window.alert("Created response binding (ID): "+result);
//					addSensorActuatorResponseAttributeList(result);
					getLatestResponseData();
				}
				else
					Window.alert(result);
			}
		});	
	}
	
	private void sendUpdateResponseRequest(){
		Utility.newRequestObj().sensorActuatorResponseUpdate(Integer.parseInt(currentSelectedID), aName, onTrigger, onNotTrigger, expression, enabled, timeout, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to update response binding");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Updating response binding: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
//					updateSensorActuatorResponseAttributeList();
					getLatestResponseData();
				}
			}
		});	
	}
	
	private void sendDeleteResponseRequest(){
		Utility.newRequestObj().sensorActuatorResponseDelete(Integer.parseInt(currentSelectedID), new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to delete response binding");
			}
			
			public void onSuccess(final String result) {
				Window.alert("Deleting response binding: "+result);
				if(result.equalsIgnoreCase("OK"))
				{
//					Data.sensorAcutatorResponseAttributeList.remove(currentSelectedID);
					getLatestResponseData();
				}
			}
		});	
	}
	
	private void getLatestResponseData(){
		tablePanel.clear();
		tablePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		tablePanel.add(Utility.addTimer());
		
		Utility.newRequestObj().sensorActuatorResponseGetAll(new AsyncCallback<String[][]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to get sensor/actuator response list");
			}
			
			public void onSuccess(String[][] responseList) {
				Utility.hideTimer();
				responseTable.setVisible(true);
				if (responseList!=null) {
//					Window.alert("Got latest response data: size "+responseList.length);
					refreshResponseCache(responseList);
					updateResponseTable(responseTable, responseList);
				}
				else
				{
					Window.alert("No data found");
					
					responsePopup.setVisible(false);
					
					FlexTable newTable = new FlexTable();
					responseTable = newTable;

					tablePanel.clear();
					tablePanel.add(responseTable);
				}
			}
		});
	}
	
	private void refreshResponseCache(String[][] result){
		Data.sensorAcutatorResponseAttributeList.clear();
		for(int i=0; i<result.length; i++)
		{
			String responseID = result[i][0];
			ArrayList<String> attributes = new ArrayList<>();
			for(int i2=0; i2<result[i].length; i2++)
			{
				attributes.add(result[i][i2]);
			}
//			Window.alert("Refreshing with "+attributes);
			Data.sensorAcutatorResponseAttributeList.put(responseID, attributes);
		}
	}
	
	private void updateResponseTable(FlexTable ft, String[][] result){
		responsePopup.setVisible(false);
		
		ft.setStyleName("fancyTable");
		
		FlexTable newTable = new FlexTable();
		setHeaders(newTable);
		ft = ChartUtilities.appendFlexTable(newTable, result);
		ft.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		addEditResponseColumnAllRow(ft);

		tablePanel.clear();
		tablePanel.add(ft);
	}
	
	private void initializeNewResponseMenu(){
		
		refreshResponseMenuWidgets();
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(cancelCreateResponseButton);
		buttonPanel.add(createResponseButton);
		
		VerticalPanel sensorPanel = new VerticalPanel();
		sensorPanel.add(new HTML("Sensor"));
		sensorPanel.add(sensorLB);
		
//		VerticalPanel opPanel = new VerticalPanel();
//		opPanel.setSpacing(10);
//		opPanel.add(new HTML("Op"));
//		opPanel.add(opLB);
		
		VerticalPanel valuePanel = new VerticalPanel();
		valuePanel.add(new HTML("Value"));
		valuePanel.add(expressionTB);

		HorizontalPanel expressionPanel = new HorizontalPanel();
		expressionPanel.add(sensorPanel);
//		expressionPanel.add(opPanel);
		expressionPanel.add(valuePanel);
		
		responseMenu.clear();
		responseMenu.addLabel("Select actuator");
		responseMenu.addItem(actuatorLB);
		responseMenu.addLabel("Input on trigger action");
		responseMenu.addItem(onTriggerLB);
		responseMenu.addLabel("Input on not trigger action");
		responseMenu.addItem(onNotTriggerLB);
		responseMenu.addLabel("Input expression command");
		responseMenu.addItem(expressionPanel);
		responseMenu.addLabel("Enable response?");
		responseMenu.addItem(responseEnabledLB);
		responseMenu.addLabel("Set timeout");
		responseMenu.addItem(timeoutTB);
		responseMenu.addItem(buttonPanel);
		
	}
	
	private void initializeEditResponseMenu(){
	
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(cancelupdateResponseButton);
		buttonPanel.add(updateResponseButton);
		buttonPanel.add(deleteResponseButton);
		
		VerticalPanel sensorPanel = new VerticalPanel();
		sensorPanel.add(new HTML("Sensor"));
		sensorPanel.add(sensorLB);
		
//		VerticalPanel opPanel = new VerticalPanel();
//		opPanel.setSpacing(10);
//		opPanel.add(new HTML("Op"));
//		opPanel.add(opLB);
		
		VerticalPanel valuePanel = new VerticalPanel();
		valuePanel.add(new HTML("Value"));
		valuePanel.add(expressionTB);

		HorizontalPanel expressionPanel = new HorizontalPanel();
		expressionPanel.add(sensorPanel);
//		expressionPanel.add(opPanel);
		expressionPanel.add(valuePanel);
		
		responseMenu.clear();
		responseMenu.addLabel("Input on trigger action");
		responseMenu.addItem(onTriggerLB);
		responseMenu.addLabel("Input on not trigger action");
		responseMenu.addItem(onNotTriggerLB);
		responseMenu.addLabel("Input expression command");
		responseMenu.addItem(expressionPanel);
		responseMenu.addLabel("Enable response?");
		responseMenu.addItem(responseEnabledLB);
		responseMenu.addLabel("Set timeout");
		responseMenu.addItem(timeoutTB);
		responseMenu.addItem(buttonPanel);
		
		ArrayList<String> responseParam = Data.sensorAcutatorResponseAttributeList.get(currentSelectedID);
		
		onTriggerLB.setSelectedIndex(getIndexOfTextInWidget(onTriggerLB,responseParam.get(2)));
		onNotTriggerLB.setSelectedIndex(getIndexOfTextInWidget(onNotTriggerLB,responseParam.get(3)));
		sensorLB.setSelectedIndex(getIndexOfTextInWidget(sensorLB,getAlphabets(responseParam.get(4))));
		expressionTB.setText(getNonAlphabets(responseParam.get(4)));
		responseEnabledLB.setSelectedIndex(getIndexOfTextInWidget(responseEnabledLB,responseParam.get(5)));
		timeoutTB.setText(responseParam.get(6));
	}
	
	private String getAlphabets(String s){
		String alphabets = "";
		for (int i = 0; i < s.length(); i++) {
	        char charAt2 = s.charAt(i);
	        if (Character.isLetter(charAt2)) {
	        	alphabets = alphabets+charAt2;
	        }
	    }
		return alphabets;
	}
	
	private String getNonAlphabets(String s){
		String nonLetters = "";
		for (int i = 0; i < s.length(); i++) {
	        char charAt2 = s.charAt(i);
	        if (!Character.isLetter(charAt2)) {
	        	nonLetters = nonLetters+charAt2;
	        }
	    }
		return nonLetters;
	}
	
	private void refreshResponseMenuWidgets(){
		actuatorLB.clear();
		for(String aName: Data.actuatorAttributeList.keySet())
		{
			actuatorLB.addItem(aName);
		}
		
		onTriggerLB.setSelectedIndex(0);
		onNotTriggerLB.setSelectedIndex(0);

		expressionTB.setText("");
		timeoutTB.setText("");

		responseEnabledLB.clear();
		responseEnabledLB.addItem("true");
		responseEnabledLB.addItem("false");
	}
	
	private void setHeaders(FlexTable ft){
		String[] header = {"Response ID","ActuatorName","OnTriggerAction","OnNotTriggerAction","Expression","Enabled","Timeout"};
		for(int i=0;i<header.length;i++)
		{
			ft.setText(0, i, header[i]);
		}
	}

	private void addEditResponseColumnAllRow(FlexTable ft){
		for(int i=1; i<ft.getRowCount(); i++)
		{
			final Anchor edit = new Anchor("Edit");
			ft.setWidget(i, ft.getCellCount(i), edit);
			edit.setName(ft.getText(i, 0));
			setEditResponseClickHandler(edit);
		}
	}
	
	private void setEditResponseClickHandler(final Anchor anchor){
		anchor.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				currentSelectedID=anchor.getName();
				initializeEditResponseMenu();
				
				responsePopup.setVisible(true);
				responsePopup.center();
				}
			});
	}
	
	private int getIndexOfTextInWidget(ListBox lb, String text){
		for(int i=0; i<lb.getItemCount();i++)
		{
			if(text.equals(lb.getItemText(i)))
			{
				return i;
			}
		}
		return 0;
	}
}
