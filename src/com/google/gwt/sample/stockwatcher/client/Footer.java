package com.google.gwt.sample.stockwatcher.client;

import java.util.Date;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class Footer extends Composite {
	// Footer Settings
	public static final int HEIGHT = 30;
	public static final String BACKGROUND_COLOR = "#000000";
	
	// GWT Base Panels
	private HorizontalPanel basePanel = new HorizontalPanel();
	private HorizontalPanel leftAnchorPanel = new HorizontalPanel();
	private HorizontalPanel rightAnchorPanel = new HorizontalPanel();
	
	// GWT Components
	private Anchor copyrightAnchor = new Anchor();
	private Anchor dateAnchor = new Anchor();
	
	// GWT Styles
	private Style mainFooterStyle;
	
	// 
	public Footer() {
		//
		final Label dateLabel = new Label();
		final HTML yearLabel = new HTML();
		Date today = new Date();
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a (zzzzz)");
		DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
		dateLabel.setText("Today is " + dateFormat.format(today));
		yearLabel.setHTML("Copyright &copy;" + yearFormat.format(today));
		
		// 
		renderLeftAnchorPanel();
		renderRightAnchorPanel();
		setupAnchors();
		
		//
		basePanel.clear();
		basePanel.setWidth("100%");
		basePanel.add(leftAnchorPanel);
		basePanel.add(rightAnchorPanel);
		
		// 
		basePanel.setCellHorizontalAlignment(leftAnchorPanel, HasHorizontalAlignment.ALIGN_LEFT);
		basePanel.setCellHorizontalAlignment(rightAnchorPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		basePanel.setCellVerticalAlignment(leftAnchorPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		basePanel.setCellVerticalAlignment(rightAnchorPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		setupComponentStyles();
		
		initWidget(basePanel);
	}
	
	private void setupAnchors() {
		//
		Date today = new Date();
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a (zzzzz)");
		DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
		
		//
		copyrightAnchor.setHTML("Created by Titus Ong and Yap Yee King, Copyright &copy; " + yearFormat.format(today));
		copyrightAnchor.addStyleName("footer-text");
		
		//
		dateAnchor.setText("Today is " + dateFormat.format(today));
		dateAnchor.addStyleName("footer-text");
	}
	
	private void renderLeftAnchorPanel() {
		leftAnchorPanel.clear();
		leftAnchorPanel.addStyleName("footer-link-panel");
		leftAnchorPanel.add(copyrightAnchor);
	}
	
	private void renderRightAnchorPanel() {
		rightAnchorPanel.clear();
		rightAnchorPanel.addStyleName("footer-link-panel");
		rightAnchorPanel.add(dateAnchor);
	}
	
	// 
	private void setupComponentStyles() {
		mainFooterStyle = basePanel.getElement().getStyle();
		mainFooterStyle.setHeight(HEIGHT, Unit.PX);
		mainFooterStyle.setBackgroundColor(BACKGROUND_COLOR);
	}
	
	// 
	@SuppressWarnings("unused")
	private void addLinkSeparator(HorizontalPanel _anchorPanel) {
		Label linkSeparator = new Label();
		
		linkSeparator.setText("|");
		linkSeparator.addStyleName("footer-link-separator");
		
		_anchorPanel.add(linkSeparator);
		_anchorPanel.setCellVerticalAlignment(linkSeparator, HasVerticalAlignment.ALIGN_MIDDLE);
	}
}
