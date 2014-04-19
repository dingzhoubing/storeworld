package com.storeworld.deliver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.storeworld.common.DataInTable;
import com.storeworld.stock.StockContentPart;
import com.storeworld.stock.StockFilter;
import com.storeworld.utils.ItemComposite;
import com.storeworld.utils.Utils;

/**
 * almost the same as StockUtils, we may need to abstract this
 * and give up the "static" access way
 * @author dingyuanxiong
 *
 */
public class DeliverUtils {
	
	private static String newLineID = "";
	private static int currentBrandLine=0;
	private static String current_sub_brand = "";
	
	private static String time_record = "";	
	private static String orderNumber = "";
	
	private static boolean firstTime = true;
	private static DeliverFilter df = new DeliverFilter();
	/**
	 * record the composite or property for refreshing the left navigator
	 */
	private static ScrolledComposite composite_scroll_record;
	private static Composite composite_fn_record;
	private static Color color_record;
	private static int width_record;
	private static int height_record;
	
	//item composite
	private static ItemComposite ic_record;
	
	/**
	 * record the time when user click add a new deliver record(table)	
	 * @param time
	 */
	public static void setTime(String time){
		if(time==null){
			//return the data as 2014 03 05 16 36 24
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			time_record = formatter.format(new Date());
		}else{
			time_record = "";//initial
		}
	}
	/**
	 * get the time 
	 * @return
	 */
	public static String getTime(){
		return time_record;
	}
	
	/**
	 * record the current history item user selected
	 * 
	 * @param ic
	 */
	public static void recordItemComposite(ItemComposite ic){
		ic_record = ic;
	}
	public static ItemComposite getItemCompositeRecord(){
		return ic_record;
	}
	
	/**
	 * set the current order number, if first in deliver page, get the number from database
	 * else +1 based on current order number  
	 */
	public static void setOrderNumber(){
		if(!orderNumber.equals("")){
			String prefix = orderNumber.substring(0, 8);
			String tmp = orderNumber.substring(8);//yyyyMMddXX, tmp is the current ordernumber
			orderNumber = prefix+(Integer.valueOf(tmp)+1);//add 1
		}else{
			//query the database to get the order number, by time prefix
			orderNumber = "2014041020";//
			//get time of today, 20140417
			//query the order in database, to get the number
			
		}
	}
	public static String getOrderNumber(){
		return orderNumber;
	}
	
	/**
	 * the item composite list
	 */
	private static ArrayList<ItemComposite> itemList = new ArrayList<ItemComposite>();
	
	/**
	 * the item content is the history panel
	 */
	private static ArrayList<DeliverHistory> historyList = new ArrayList<DeliverHistory>();	
	
	/**
	 * when first in deliver page, initialize the history page
	 * get the data from database
	 * @param option : the time threshold or something else
	 */
	private static void getHistoryFromDataBase(String option){
		
	}
	
	/**
	 * make the current deliver record into history
	 * include the common info & table info
	 */
	public static void addToHistory(){
		//need to change as the UI design
		ArrayList<DataInTable> deliverList = DeliverList.getDelivers();
		String title = "";
		for(int i=0;i<deliverList.size()-2;i++){
			Deliver st = (Deliver)deliverList.get(i);
			title+=(st.getBrand()+",");
		}
		Deliver st = (Deliver)deliverList.get(deliverList.size()-2);
		title+=(st.getBrand());//title
		String time_tmp = getTime();
//		int num = queryItemsFromDataBase(time_tmp);
		String number="50000";//fake data
		
		DeliverHistory his = new DeliverHistory(title,time_tmp,number);
		
		ItemComposite ic = new ItemComposite(composite_fn_record, color_record, width_record, height_record, his);
		ic.setValue(his.getTitle(), his.getTimeShow(), his.getNumber());
		itemList.add(ic);
		composite_scroll_record.setMinSize(composite_fn_record.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		composite_fn_record.layout();
	}
	
	/**
	 * first into the deliver page, show the history from history
	 * @param composite_scroll
	 * @param composite_fn
	 * @param color
	 * @param width
	 * @param height
	 */
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
			DeliverHistory his = historyList.get(i);
			ItemComposite ic = new ItemComposite(composite_fn, color, width, height, his);
			ic.setValue(his.getTitle(), his.getTime(), his.getNumber());
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
	 * refresh the table if user select do not use the soft keyboard
	 */
	public static void refreshTableData(){
		
		DeliverContentPart.getTableViewer().addFilter(df);
		DeliverContentPart.getTableViewer().removeFilter(df);
		Utils.refreshTable(DeliverContentPart.getTableViewer().getTable());
		DeliverFilter.resetIsFirst();
	}
	
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
