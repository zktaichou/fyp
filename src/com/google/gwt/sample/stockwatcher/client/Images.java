package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.Window;

public class Images{
	// Vertical Alignment Constants
	public static enum VerticalAlignment { BASELINE, SUB, SUPER, TOP, TEXT_TOP, MIDDLE, BOTTOM, TEXT_BOTTOM }
	
	// Picture Paths
	public static final String HOME = "images/home.png";
	public static final String HOME2 = "images/home2.png";
	public static final String LEFT_ARROW = "images/left_arrow.gif";
	public static final String LEFT_ARROW2 = "images/left_arrow2.gif";
	public static final String UP_BLINKER = "images/up_blinker.gif";
	public static final String WELCOME = "images/welcome.gif";
	public static final String WELCOME2 = "images/welcome2.gif";
	public static final String WELCOME3 = "images/welcome3.gif";
	public static final String CONTROLLER_NOTIFICATION = "images/controller_notification.gif";
	public static final String ACTUATOR_NOTIFICATION = "images/actuator_notification.gif";
	public static final String SENSOR_NOTIFICATION = "images/sensor_notification.gif";
	public static final String DEBUG = "images/debug.gif";
	public static final String NOTIFICATION = "images/notification.gif";
	public static final String SAMPLE_1 = "images/sample1.png";
	public static final String SAMPLE_2 = "images/sample2.png";
	public static final String SAMPLE_3 = "images/sample3.png";
	public static final String LOADING = "images/loading.gif";
	public static final String LOADING2 = "images/loading2.gif";
	public static final String LOADING3 = "images/loading3.gif";
	public static final String LOADING_EPIC = "images/loading_epic.gif";
	public static final String LOADING_EPIC2 = "images/loading_epic2.gif";
	public static final String LOADING_EPIC3 = "images/loading_epic3.gif";
	public static final String LOADING_EPIC4 = "images/loading_epic4.gif";
	public static final String LOADING_FLASK = "images/loading_flask.gif";
	public static final String LOADING_CAT = "images/loading_cat.gif";
	public static final String BACKGROUND_LOAD = "images/background_load.gif";
	public static final String OPTIONS = "images/options.gif";
	public static final String GRAPH = "images/graph.gif";
	public static final String GRAPH2 = "images/graph2.gif";
	public static final String ON = "images/on.png";
	public static final String OFF = "images/off.png";
	public static final String SELECTED = "images/selected.png";
	public static final String FSKTM_LOGO = "images/fsktm.png";
	public static final String GEAR_ICON = "images/gear_icon.png";
	public static final String CURRENT_ICON = "images/current_icon.png";
	public static final String WATER_ICON = "images/water_icon.png";
	public static final String LIGHT_ICON = "images/light_icon.png";
	public static final String TEMPERATURE_ICON = "images/temperature_icon.png";
	public static final String ACTUATOR_CURRENT_ICON = "images/actuator_current_icon.png";
	public static final String ACTUATOR_WATER_ICON = "images/actuator_water_icon.png";
	public static final String MICROCONTROLLER_ICON = "images/microcontroller_icon.png";
	
	// Function to customize image width, height and image alignment
	public static String getImage(String _path, int _width_px, int _height_px) {
		return getImage(_path, _width_px + "px", _height_px + "px", VerticalAlignment.MIDDLE);
	}
	
	public static String getImage(String _path, int _width_px, int _height_px, VerticalAlignment _align) {
		return getImage(_path, _width_px + "px", _height_px + "px", _align);
	}
	
	public static String getImage(String _path, String _width, String _height) {
		return getImage(_path, _width, _height, VerticalAlignment.MIDDLE);
	}
	
	public static String getImage(String _path, String _width, String _height, VerticalAlignment _align) {
		return "<img src='" + _path + "' width='" + _width + "' height='" + _height + "' style='vertical-align:" + getVerticalAlign(_align) + ";' />";
	}
	
	public static String getImage(String _path, int _height_px) {
		return getImage(_path, _height_px + "px", VerticalAlignment.MIDDLE);
	}
	
	public static String getImage(String _path, int _height_px, VerticalAlignment _align) {
		return getImage(_path, _height_px + "px", _align);
	}
	
	public static String getImage(String _path, String _height) {
		return getImage(_path, _height, VerticalAlignment.MIDDLE);
	}
	
	public static String getImage(String _path, String _height, VerticalAlignment _align) {
		return "<img src='" + _path + "' height='" + _height + "' style='vertical-align:" + getVerticalAlign(_align) + ";' />";
	}
	
	public static String getImage(String _path) {
		String path1 = _path.replaceAll("<.*?\"", "");
		String path2 = path1.replaceAll("\".*?>", "");
		return "<img src='" + path2 + "' height='" + Window.getClientHeight()*0.8 + "' style='vertical-align:middle;' />";
	}
	
	// Function to return CSS equivalent of vertical align attribute
	private static String getVerticalAlign(VerticalAlignment _align) {
		switch(_align) {
			case BASELINE:
				return "baseline";
			case BOTTOM:
				return "bottom";
			case MIDDLE:
				return "middle";
			case SUB:
				return "sub";
			case SUPER:
				return "super";
			case TEXT_BOTTOM:
				return "text-bottom";
			case TEXT_TOP:
				return "text-top";
			case TOP:
				return "top";
			default:
				return "inherit";
		}
	}
}

