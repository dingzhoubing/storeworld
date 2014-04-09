package com.storeworld.stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.storeworld.common.DataInTable;
import com.storeworld.utils.Constants;
import com.storeworld.utils.ItemComposite;

public class StockUtils {
	
	private static String newLineID = "";
	private static int currentBrandLine=0;
	private static String current_sub_brand = "";
	//the time when click add a new stock
	private static String time_record = "";
	private static boolean firstTime = true;
	
	private static ScrolledComposite composite_scroll_record;
	private static Composite composite_fn_record;
	private static Color color_record;
	private static int width_record;
	private static int height_record;
	
	//item composite
	private static ItemComposite ic_record;
	
	
	private static HashMap<String, HashSet<String>> brand2sub = new HashMap<String, HashSet<String>>();
	
	public static void addBrand2Sub(String brand, String sub_brand){
		//before adding into database, we check the validation
		if(brand2sub.keySet().contains(brand)){
			if(!brand2sub.get(brand).contains(sub_brand)){
				brand2sub.get(brand).add(sub_brand);
			}
		}else{//do not contain this brand
			HashSet<String> sub_tmp = new HashSet<String>();
			sub_tmp.add(sub_brand);
			brand2sub.put(brand, sub_tmp);
		}
	}
	public static HashMap<String, HashSet<String>> getBrand2Sub(){
		return brand2sub;
	}
	//once click the add new stock table, record the time
	public static void setTime(String time){
		if(time==null){
			//return the data as 2014 03 05 16 36 24
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			time_record = formatter.format(new Date());
		}else{
			time_record = "";//initial
		}
	}
	
	public static String getTime(){
		return time_record;
	}
	
	public static void recordItemComposite(ItemComposite ic){
		ic_record = ic;
	}
	public static ItemComposite getItemCompositeRecord(){
		return ic_record;
	}
	/**
	 * the item composite list
	 */
	private static ArrayList<ItemComposite> itemList = new ArrayList<ItemComposite>();
	
	/**
	 * the item content is the history panel
	 */
	private static ArrayList<StockHistory> historyList = new ArrayList<StockHistory>();
	
	/**
	 * the option to search the history
	 * @param option
	 */
	private static void getHistoryFromDataBase(String option){
		
//		for(int i=0;i<5;i++){
//			History his = new History("123","456","789"+Constants.SPACE);
//			historyList.add(his);
//		}
	}
	
	public static void addToHistory(){
		ArrayList<DataInTable> stockList = StockList.getStocks();
		String title = "";
		for(int i=0;i<stockList.size()-2;i++){
			Stock st = (Stock)stockList.get(i);
			title+=(st.getBrand()+",");
		}
		Stock st = (Stock)stockList.get(stockList.size()-2);
		title+=(st.getBrand());//title
		String time_tmp = getTime();
//		int num = queryItemsFromDataBase(time_tmp);
		String number="50000";//fake data
		
		StockHistory his = new StockHistory(title,time_tmp,number);
		
		ItemComposite ic = new ItemComposite(composite_fn_record, color_record, width_record, height_record, his);
		ic.setValue(his.getTitle(), his.getTimeShow(), his.getNumber());
		itemList.add(ic);
		composite_scroll_record.setMinSize(composite_fn_record.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		composite_fn_record.layout();
		
	}
	public static void showHistoryPanel(ScrolledComposite composite_scroll, Composite composite_fn, Color color, int width, int height){
		//search the database
		if(firstTime){
			composite_scroll_record = composite_scroll;
			composite_fn_record = composite_fn;
			color_record = color;
			width_record = width;
			height_record = height;
		getHistoryFromDataBase("");
		
		for (int i = 0; i < historyList.size(); i++) {
			StockHistory his = historyList.get(i);
			ItemComposite ic = new ItemComposite(composite_fn, color, width, height, his);
			ic.setValue(his.getTitle(), his.getTimeShow(), his.getNumber());
			itemList.add(ic);
			composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));
			composite_fn.layout();
		}
		firstTime = false;
		}else{
			//??
		}
	}
	/**
	 * add to history, not complete yet
	 * @param composite_scroll
	 * @param composite_fn
	 * @param color
	 * @param width
	 * @param height
	 * @param his
	 */
//	public static void addToHistoryPanel(ScrolledComposite composite_scroll,
//			Composite composite_fn, Color color, int width, int height,
//			History his) {
//		// search the database
//		addToHistory();
//
//		ItemComposite ic = new ItemComposite(composite_fn, color, width, height);
//		ic.setValue(his.getTitle(), his.getTime(), his.getNumber());
//		itemList.add(ic);
//		composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT,
//				SWT.DEFAULT));
//		composite_fn.layout();
//
//	}
	
	/**
	 * dispose all the history item
	 */
	public static void clearHistoryPanel(){
		for(int i=0;i<itemList.size();i++){
			itemList.get(i).dispose();
		}
		itemList.clear();
	}
	
	/**
	 * new line of the table
	 * @param id
	 */
	public static void setNewLineID(String id){
		newLineID = id;
	}
	
	public static String getNewLineID(){
		return newLineID;
	}
	
	/**
	 * if click brand , record the current line, current sub_brand
	 * @param line
	 */
	public static void setCurrentLine(int line){
		currentBrandLine = line;
	}
	public static int getCurrentLine(){
		return currentBrandLine;
	}
	public static void setCurrentSub_Brand(String sub){
		current_sub_brand = sub;
	}
	public static String getCurrentSub_Brand(){
		return current_sub_brand;
	}
		
}
