package com.storeworld.utils;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.GoodsInfoService;
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
	
	//indeed key board
	private static String indeedRecord = "";
	private static boolean indeedNeedChange = false;
	private static boolean indeedClickButton = false;
	
	//return key board, no use now
	private static String returnRecord = "";
	private static boolean returnNeedChange = false;
	private static boolean returnClickButton = false;
	
	private static boolean useSoftKeyBoard = true;
	private static GoodsInfoService goodsinfo = new GoodsInfoService();
//	private static int waitForListener = 0;
	
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
		List<String> brands = new ArrayList<String>();
		//get the data from cache
		brands.addAll(DataCachePool.getBrand2Sub().keySet());
		return brands;
	}
	//to check if it's a new brand
	public static boolean checkBrand(String brand){	
		Set<String> brands = DataCachePool.getBrand2Sub().keySet();		
		for(String bd: brands){
			if(bd.equals(brand)){
				return true;
			}
		}
		return false;		
	}
	//to check if it's new sub
	public static boolean checkSubBrand(String subbrand){	
		Set<String> brands = DataCachePool.getBrand2Sub().keySet();		
		for(String bd: brands){
			for(String sub: DataCachePool.getBrand2Sub().get(bd)){
				if(sub.equals(subbrand)){
					return true;
				}	
			}			
		}
		return false;		
	}
	
	//return an empty sub brands list
	public static List<String> getSub_Brands(){
		return new ArrayList<String>();
	}
	//return an empty size list
	public static List<String> getSizes(){
		return new ArrayList<String>();
	}	
	
	//get the sub brands by brand from cache
	public static List<String> getSub_Brands(String brand){
		//always need to new, if not, cause wrong
		List<String> sub_brands  = new ArrayList<String>();
		if(DataCachePool.getBrand2Sub().get(brand)!=null)
			sub_brands.addAll(DataCachePool.getBrand2Sub().get(brand));
		
		return sub_brands;
	}
	
	//get the sizes by brand & sub from cache
	public static List<String> getSizes(String brand, String sub){
		//always need to new, if not, cause wrong
		List<String> sizes  = new ArrayList<String>();
		//now there is no cache, just get it from database
		Map<String, Object> prod = new HashMap<String, Object>();
		prod.put("brand", brand);
		prod.put("sub_brand", sub);
		ReturnObject ret;
		try {
			ret = goodsinfo.queryGoodsInfo(prod);
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			for(int i=0;i<list.size();i++){
				GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(i);
				sizes.add(cDTO.getStandard());
				break;//one is ok
			}			
		} catch (Exception e) {
			System.out.println("query the sizes with brand&sub failed");
		}
		return sizes;		
	}
	
	
	
	/**
	 * if click the button of  software keyboard
	 * @return
	 */
	public static boolean getUseSoftKeyBoard(){
		return useSoftKeyBoard;
//		return false;
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
	 * if click the button of  indeed keyboard
	 * @return
	 */
	public static boolean getIndeedClickButton(){
		return indeedClickButton;
	}
	public static void setIndeedClickButton(boolean click){
		indeedClickButton = click;
	}
	
	public static boolean getReturnClickButton(){
		return returnClickButton;
	}
	public static void setReturnClickButton(boolean click){
		returnClickButton = click;
	}
	
	/**
	 * whether enter the edit mode or not
	 */
	private static boolean enter = false;
	public static boolean getEnter(){
		return enter;
	}
	public static void setEnter(boolean ent){
		enter = ent;
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
	 * if the record need change after the  indeed keyboard
	 * @return
	 */
	public static boolean getIndeedNeedChange(){
		return indeedNeedChange;
	}
	public static void setIndeedNeedChange(boolean ret){
		indeedNeedChange = ret;
	}
	
	public static boolean getReturnNeedChange(){
		return returnNeedChange;
	}
	public static void setReturnNeedChange(boolean ret){
		returnNeedChange = ret;
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
	 * record and read the text of the indeed keyboard
	 * @return
	 */
	public static String getIndeed(){
		return indeedRecord;
	}
	public static void setIndeed(String input){
		indeedRecord = input;
	}
	
	public static String getReturn(){
		return returnRecord;
	}
	public static void setReturn(String input){
		returnRecord = input;
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
	
	/**
	 * refresh the table data, to make the table data with different color items
	 * @param table
	 */
	public static void refreshTable(Table table){
		Color color1 = new Color(table.getDisplay(), 255, 245, 238);
		Color color2 = new Color(table.getDisplay(), 255, 250, 250);
		Color color3 = new Color(table.getDisplay(), 230, 230, 230);
		for(int i=0;i<table.getItemCount()-1;i++){
			TableItem item = table.getItem(i);
			if(i%2 == 0){
				item.setBackground(color1);
			}else{
				item.setBackground(color2);
			}
		}
		TableItem item = table.getItem(table.getItemCount()-1);
		item.setBackground(color3);
		table.redraw();
	}
	
	public static boolean isNotNull(String str) {
		if (str != null && str.trim().length() > 0
				&& !("null".equals(str.trim().toLowerCase())))
			return true;
		return false;
	}
	
}
