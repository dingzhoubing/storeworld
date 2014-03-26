package com.storeworld.utils;

import java.awt.Toolkit;

/**we use kind of ratio to show the UI, make it flexible
 * now make it fixed number
 * 
 * @author dingyuanxiong
 *
 */

public class Constants {
	public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	public static final int LOGIN_WIDTH = 600;
	public static final int LOGIN_HEIGHT = 500;
	
	public static final String CLOSE_IMAGE = "closeImage";
	public static final String CLOSE_OVER_IMAGE = "closeOverImage";
	public static final String CLOSE_DOWN_IMAGE = "closeDownImage";
	
	public static final String MIN_IMAGE = "minImage";
	public static final String MIN_OVER_IMAGE = "minOverImage";
	public static final String MIN_DOWN_IMAGE = "minDownImage";
	
	public static final String USERNAME = "root";//”√ªß√˚
	public static final String PASSWORD = "";//√‹¬Î
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String URL="jdbc:mysql://127.0.0.1:3306/storeworld";
	//login
    public enum LOGIN_TYPE {
    	LOGIN_BOTTOM,  LOGIN_INPUT; 
    };
    
	//tools column
    public enum NORTH_TYPE {
    	NORTH_BOTTOM,  NORTH_INDEX; 
    };
	
    //content column
    public enum CONTENT_TYPE {
    	CONTENT_BOTTOM,  CONTENT_STOCK, CONTENT_DELIVER, CONTENT_ANALYZE, CONTENT_CUSTOMER, CONTENT_PRODUCT, CONTENT_MAIN; 
    };
    //function type
    public enum FUNCTION {
    	STOCK, DELIVER, ANALYZE, CUSTOMER, PRODUCT, NONE;
    };
	
}
