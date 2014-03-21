package com.storeworld.utils;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.FUNCTION;
import com.storeworld.utils.Constants.LOGIN_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;

/**
 * the Utils class of the whole project
 * @author dingyuanxiong
 *
 */
public class Utils {
	
	//soft key board
	private static String inputRecord = "";
	private static boolean inputNeedChange = false;
	private static boolean clickButton = false;
	private static boolean useSoftKeyBoard = true;
	
	//gray image
	private static ImageLoader imageLoader = new ImageLoader();
	
	//map the layers in StackLayout
	private static HashMap<NORTH_TYPE, Composite> northComps = new HashMap<NORTH_TYPE, Composite>();
	private static HashMap<CONTENT_TYPE, Composite> contentComps = new HashMap<CONTENT_TYPE, Composite>();
	private static HashMap<LOGIN_TYPE, Composite> loginComps = new HashMap<LOGIN_TYPE, Composite>();
	private static boolean login_unlock = false;//false means login, true means unlock 	
	
	//record the current function and last function
	private static FUNCTION func = FUNCTION.NONE;
	private static FUNCTION func_last = FUNCTION.NONE;
	//map the function with the button ,to get the current disable button
	private static HashMap<FUNCTION, Button> func_button = new HashMap<FUNCTION, Button>();
	
	//get the brands and sub_brands for ccomboBox
	private static List<String> brands;
	private static List<String> sub_brands;
	
	private static String comboValue = "";
	
	
	public static HashMap<FUNCTION, Button> getFunc_Button(){
		return func_button;
	}
	
	public static void grayButton(Button button_current, Button button_last){
		//make the image in the button be gray
		if(button_current != null)
			button_current.setEnabled(false);
		if(button_last != null && button_last != button_current)// avoid click index directly
			button_last.setEnabled(true);
	}
	
	//record the comboValue
	public static void setComboValue(String src){
		comboValue = src;
	}
	public static String getComboValue(){
		return comboValue;
	}
	
	//get the brands
	public static List<String> getBrands(){
		//get from database
//		brands = new String[]{"五得利","五联","金龙"};
		brands = new ArrayList<String>();
		brands.add("五得利");
		brands.add("五联");
		brands.add("金龙");
		return brands;
	}
	//to check if it's a new brand
	public static boolean checkBrand(String brand){
		for(int i=0;i<brands.size();i++){
			if(brands.get(i).equals(brand)){
				return true;
			}
		}
		return false;
	}
	//return an empty sub brands list
	public static List<String> getSub_Brands(){
		return sub_brands = new ArrayList<String>();
	}
	
	//get the sub brands
	public static List<String> getSub_Brands(String brand){
		sub_brands  = new ArrayList<String>();
		if(brand.equals("五得利")){
			sub_brands.add("特精");
			sub_brands.add("包子粉");
			sub_brands.add("精一");
			sub_brands.add("普粉");
		}else{
			sub_brands.add("精粉");
			sub_brands.add("馒头粉");
			sub_brands.add("精二");
			sub_brands.add("普粉");	
		}
		return sub_brands;
	}
	
	
	
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
	public static void setFunctinLast(FUNCTION type){
		func_last = type;
	}
	public static FUNCTION getFunctionLast(){
		return func_last;
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
	
	public static void refreshTable(Table table){
		Color color1 = new Color(table.getDisplay(), 255, 245, 238);
		Color color2 = new Color(table.getDisplay(), 255, 250, 250);
		for(int i=0;i<table.getItemCount();i++){
			TableItem item = table.getItem(i);
			if(i%2 == 0){
				item.setBackground(color1);
			}else{
				item.setBackground(color2);
			}
		}
		table.redraw();
	}
	
	public static boolean isNotNull(String str) {
		if (str != null && str.trim().length() > 0
				&& !("null".equals(str.trim().toLowerCase())))
			return true;
		return false;
	}
	
}
