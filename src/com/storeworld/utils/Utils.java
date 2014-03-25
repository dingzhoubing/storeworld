package com.storeworld.utils;

import java.awt.Toolkit;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.FUNCTION;
import com.storeworld.utils.Constants.LOGIN_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;

public class Utils {
	
	//soft key board
	private static String inputRecord = "";
	private static boolean inputNeedChange = false;
	private static boolean clickButton = false;
	private static boolean useSoftKeyBoard = true;
	
	private static ImageLoader imageLoader = new ImageLoader();
	private static HashMap<NORTH_TYPE, Composite> northComps = new HashMap<NORTH_TYPE, Composite>();
	private static HashMap<CONTENT_TYPE, Composite> contentComps = new HashMap<CONTENT_TYPE, Composite>();
	private static HashMap<LOGIN_TYPE, Composite> loginComps = new HashMap<LOGIN_TYPE, Composite>();
	private static boolean login_unlock = false;//false means login, true means unlock 	
	private static FUNCTION func = FUNCTION.NONE;
	
	
	/**
	 * if click the button of  software keyboard
	 * @return
	 */
	public static boolean getUseSoftKeyBoard(){
		return useSoftKeyBoard;
	}
	public static void settUseSoftKeyBoard(boolean use){
		useSoftKeyBoard = use;
	}
	
	/**
	 * if click the button of  software keyboard
	 * @return
	 */
	public static boolean getClickButton(){
		return clickButton;
	}
	public static void setClickButton(boolean click){
		clickButton = click;
	}
	
	/**
	 * if the record need change after the  software keyboard
	 * @return
	 */
	public static boolean getInputNeedChange(){
		return inputNeedChange;
	}
	public static void setInputNeedChange(boolean need){
		inputNeedChange = need;
	}
	
	/**
	 * record and read the text of the software keyboard
	 * @return
	 */
	public static String getInput(){
		return inputRecord;
	}
	public static void setInput(String input){
		inputRecord = input;
	}
	/**
	 * to judge it's lock or unlock, and change the status 
	 */
	public static boolean getStatus(){
		return login_unlock;
	}
	public static void changeStatus(){
		if(!login_unlock)
			login_unlock = true;
		else
			login_unlock = false;
	}
	
	/**
	 * set and get the current function, to make the corresponding functin button gray 
	 * @param type
	 */	
	//determin the button to gray in the tools bar
	public static void setFunctin(FUNCTION type){
		func = type;
	}
	public static FUNCTION getFunction(){
		return func;
	}
	
	/**
	 * make the shell in the middle of the screen
	 * @param shell
	 */
	public static void center(Shell shell) {

		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;

		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;

		if (shellH > screenH)
			shellH = screenH;

		if (shellW > screenW)
			shellW = screenW;

		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2));
	}
	
	/**
	 * get the north part composite by type
	 * @param type
	 * @return
	 */
	public static Composite getNorthPartComposites(NORTH_TYPE type){
		Composite comp = null;
		switch (type) {
		case NORTH_BOTTOM:
		case NORTH_INDEX:
			comp = northComps.get(type);
			break;
		default:
			break;
		}
		return comp;		
	}
	/**
	 * get the content part composite by type
	 * @param type
	 * @return
	 */
	public static Composite getContentPartComposites(CONTENT_TYPE type){
		Composite comp = null;
		switch (type) {
		case CONTENT_BOTTOM:
		case CONTENT_CUSTOMER:
		case CONTENT_DELIVER:
		case CONTENT_PRODUCT:
		case CONTENT_STOCK:
		case CONTENT_MAIN:
		case CONTENT_ANALYZE:
			comp = contentComps.get(type);
			break;
		default:
			break;
		}
		return comp;
	}
	
	/**
	 * get the login part of the login compiste(the content part as the mainui)
	 * @param type
	 * @return
	 */
	public static Composite getLoginPartComposites(LOGIN_TYPE type){
		Composite comp = null;
		switch (type) {
		case LOGIN_BOTTOM:
		case LOGIN_INPUT:	
			comp = loginComps.get(type);
			break;
		default:
			break;
		}
		return comp;
	}
	/**
	 * set the parts
	 * @param comp
	 * @param type
	 */
	public static void setNorthPartComposite(Composite comp, NORTH_TYPE type){
		northComps.put(type, comp);
	}
	public static void setContentPartComposite(Composite comp, CONTENT_TYPE type){
		contentComps.put(type, comp);
	}
	public static void setLoginPartComposite(Composite comp, LOGIN_TYPE type){
		loginComps.put(type, comp);
	}
	
	
	/**
	 * get a scaled image
	 * @param imagepath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image getScaledImage(String imagepath, int width, int height){
		ImageData[] data = imageLoader.load(imagepath);
		Image imageScale = new Image(null, data[0].scaledTo(width, height));
		return imageScale;
	}
	/**
	 * get a gray image
	 * @param image
	 * @return
	 */
	public static Image getGrayImage(Image image){
		return new Image(null,image, SWT.IMAGE_GRAY);
	}
	
	public static boolean isNotNull(String str) {
		if (str != null && str.trim().length() > 0
				&& !("null".equals(str.trim().toLowerCase())))
			return true;
		return false;
	}
	
}
