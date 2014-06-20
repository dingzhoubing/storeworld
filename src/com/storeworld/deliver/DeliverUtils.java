package com.storeworld.deliver;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.storeworld.common.DataInTable;
import com.storeworld.pojo.dto.DeliverInfoAllDTO;
import com.storeworld.pojo.dto.DeliverInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.stock.StockHistory;
import com.storeworld.utils.ItemComposite;
import com.storeworld.utils.Utils;

/**
 * almost the same as StockUtils, we may need to abstract this
 * and give up the "static" access way
 * @author dingyuanxiong
 *
 */
public class DeliverUtils {
	
	private static boolean detailTimer = true;
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
	private static DecimalFormat dataformat = new DecimalFormat("0.00");
	//item composite
	private static ItemComposite ic_record;
	//the table is in edit mode or not
	private static boolean editMode = false;
	private static boolean returnMode = false;
	private static String status = "";
	
	//NEW, HISTORY, EMPTY
	public static void setStatus(String sta){
		status = sta;
	}
	public static String getStatus(){
		return status;
	} 
	
	/**
	 * edit mode, if we click edit
	 */
	public static void enterEditMode(){
		editMode = true;
	}
	public static void leaveEditMode(){
		editMode = false;
	}
	public static boolean getEditMode(){
		return editMode;
	}
	
	/**
	 * return mode, if we click return
	 */
	public static void enterReturnMode(){
		returnMode = true;
	}
	public static void leaveReturnMode(){
		returnMode = false;
	}
	public static boolean getReturnMode(){
		return returnMode;
	}
	
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
		
		if(DeliverUtils.getEditMode()){
			DeliverHistory shis = (DeliverHistory)ic_record.getHistory();
			time_record = shis.getTime();
		}
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
	
	public static ArrayList<ItemComposite> getItemList(){
		return itemList;
	}
	public static ArrayList<DeliverHistory> getHistoryList(){
		return historyList;
	}
	
	/**
	 * set the current order number, if first in deliver page, get the number from database
	 * else +1 based on current order number  
	 */
	public static void setOrderNumber(){
		//mock data
//		if(!orderNumber.equals("")){
//			String prefix = orderNumber.substring(0, 8);
//			String tmp = orderNumber.substring(8);//yyyyMMddXX, tmp is the current ordernumber
//			orderNumber = prefix+(Integer.valueOf(tmp)+1);//add 1
//		}else{
//			//query the database to get the order number, by time prefix
//			orderNumber = "2014041020";//
//			//get time of today, 20140417
//			//query the order in database, to get the number
//			
//		}
		
		//query the database to query how many order number here
		DeliverInfoService deliverinfo = new DeliverInfoService();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");		
		String time_current = formatter.format(new Date());
		String prefix = time_current.substring(0, 8);//order number is: 20140420xxxx
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("deliver_time", time_current);
		HashSet<String> orderset = new HashSet<String>();
		try {
			ReturnObject ret = deliverinfo.queryDeliverInfoByDefaultDelivertime(map);
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
//			String last_order = "";
	
			if(list.size()>0){
				
				int count = 1;
				for(int i=0;i<list.size();i++){
					DeliverInfoAllDTO dto = (DeliverInfoAllDTO)list.get(i);
					orderset.add(dto.getOrder_num());
				}
				count = orderset.size();			
				if(count<10)
					orderNumber = prefix+"000"+(count+1);
				else if(count<100)
					orderNumber = prefix+"00"+(count+1);
				else if(count<1000)
					orderNumber = prefix+"0"+(count+1);
				else
					orderNumber = prefix+(count+1);
				
			}else{
				//if today still have no deliver, by default, it's 0001
				orderNumber = prefix+"0001";				
			}
			
			
		} catch (Exception e) {
			System.out.println("query deliver info by default deliver time failed");
		}
	}
	
	public static void setOrderNumber(String ordernum){
		orderNumber = ordernum;
	}
	public static String getOrderNumber(){
		if(DeliverUtils.getEditMode()){
			DeliverHistory shis = (DeliverHistory)ic_record.getHistory();
			orderNumber = shis.getOrderNumber();
		}
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
	
	public static void addToHistory(HashMap<String, ArrayList<DeliverInfoAllDTO>> delivermap){

		ArrayList<String> keylist = new ArrayList<String>();
		keylist.addAll(delivermap.keySet());
		Collections.sort(keylist);
//		for(String key : delivermap.keySet()){
		for(String key : keylist){
			String title = "";
			double total = 0.000;
			String time_tmp = "";
			String indeed="";
			String is_print = "";
			ArrayList<DeliverInfoAllDTO> delivers = delivermap.get(key);
			String area = delivers.get(0).getCustomer_area();
			String name = delivers.get(0).getCustomer_name();
			String ordernumber = delivers.get(0).getOrder_num();
			indeed = delivers.get(0).getUni_reserve1();//get the indeed
			if(indeed == null)
				indeed = "";
			is_print = delivers.get(0).getIs_print();
			if(is_print == null)
				is_print = "";
			title = area+" "+name;
			double p = 0.0;
			int n = 0;
			time_tmp = delivers.get(0).getDeliver_time();
			for(int i=0;i<delivers.size();i++){
				p = Double.valueOf(delivers.get(i).getUnit_price());
				n = Integer.valueOf(delivers.get(i).getQuantity());
				total+=(p * n);	
			}
			
			DeliverHistory his = new DeliverHistory(title,time_tmp, dataformat.format(total), ordernumber, indeed, is_print);
			historyList.add(his);				
		}
	}
	
	
	/**
	 * when first in deliver page, initialize the history page
	 * get the data from database
	 * @param option : the time threshold or something else
	 */
	private static void getHistoryFromDataBase(String option){
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("deliver_time", option);
		DeliverInfoService deliverinfo = new DeliverInfoService();
		try {
			ReturnObject ret = deliverinfo.queryDeliverInfoByDefaultDelivertime(map);
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
//			String last_order = "";
			//ordernumber -- > deliver
			HashMap<String, ArrayList<DeliverInfoAllDTO>> delivers = new HashMap<String, ArrayList<DeliverInfoAllDTO>>();  
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String ordernumber = cDTO.getOrder_num();
				if(delivers.containsKey(ordernumber)){
					delivers.get(ordernumber).add(cDTO);
				}else{
					ArrayList<DeliverInfoAllDTO> delist = new ArrayList<DeliverInfoAllDTO>();
					delist.add(cDTO);
					delivers.put(ordernumber, delist);
				}			
				}
			}
			addToHistory(delivers);//finish
		} catch (Exception e) {
			System.out.println("query the delivers by default time failed");
		}	
		
		
	}
	
	//update history when change the value of item composite
	//!!but we do not change the history, so...problems
	public static void updateHistory(){
		String title = "";
		String area = DeliverContentPart.getArea();
		String name = DeliverContentPart.getName();
		title = area+" "+name;
		DeliverHistory shis = (DeliverHistory)ic_record.getHistory();
		shis.setTitle(title);		
		ic_record.setValue(shis.getTitleShow());
	}
	
	public static void updateHistory(ArrayList<DataInTable> delivers){
		
		String title = "";
		double total = 0.000;
//		String time_tmp = "";
		String area = DeliverContentPart.getArea();
		String name = DeliverContentPart.getName();
		title = area+" "+name;
		double p = 0.0;
		int n = 0;
		for(int i=0;i<delivers.size()-1;i++){
			Deliver st = (Deliver)delivers.get(i);
//			time_tmp = st.getTime();
			String price = st.getPrice();
			String number = st.getNumber();
			p = Double.valueOf(price);
			n = Integer.valueOf(number);
			total+=(p * n);	
		}
		
//		String number_total = String.valueOf(total);
		String number_total = dataformat.format(total);
		//indeed is the same as total when update the table
		DeliverHistory shis = (DeliverHistory)ic_record.getHistory();
		shis.setTitle(title);
//		shis.setTime(time_tmp);
		shis.setNumber(number_total);
		shis.setIndeed(number_total);//the same as total
		ic_record.setValue(shis.getTitleShow(), shis.getTimeShow(), shis.getValueShow());
		
	}

	/**
	 * make the current deliver record into history
	 * include the common info & table info
	 */
	public static void addToHistory(){
		//need to change as the UI design
		ArrayList<DataInTable> deliverList = DeliverList.getDelivers();
		String title = "";
		double total = 0.000;
		double p = 0.0;//price
		int n=0;//number
		
		for(int i=0;i<deliverList.size()-1;i++){
			Deliver st = (Deliver)deliverList.get(i);
			p = Double.valueOf(st.getPrice());
			n = Integer.valueOf(st.getNumber());
			total+=(p*n);			
		}

		String time_tmp = getTime();
//		int num = queryItemsFromDataBase(time_tmp);
//		String number="50000";//fake data
		String area = DeliverContentPart.getArea();
		String name = DeliverContentPart.getName();
		String ordernumber = DeliverContentPart.getOrderNumber();
		title = area+" "+name;		
		String indeed = DeliverContentPart.getIndeed().trim();
		DeliverHistory his = new DeliverHistory(title,time_tmp,dataformat.format(total), ordernumber, indeed, "");
		
		ItemComposite ic = new ItemComposite(composite_fn_record, color_record, width_record, height_record, his);
		ic.setValue(his.getTitleShow(), his.getTimeShow(), his.getValueShow());
		itemList.add(ic);
		composite_scroll_record.setMinSize(composite_fn_record.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		composite_fn_record.layout();
	}
	
	public static void layoutItemList(){
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
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String time_current = formatter.format(new Date());
		getHistoryFromDataBase(time_current);
		
		for (int i = 0; i < historyList.size(); i++) {
			DeliverHistory his = historyList.get(i);
			ItemComposite ic = new ItemComposite(composite_fn, color, width, height, his);
			ic.setValue(his.getTitleShow(), his.getTimeShow(), his.getValueShow());
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
	
	
	public static void showSearchHistory(String dateSearch, String area, String cus){
		//remove the navigator panel, clear all the result
		for(int i=0;i<itemList.size();i++)
			itemList.get(i).dispose();
		itemList.clear();
		historyList.clear();
		
		//add search result
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("deliver_time", dateSearch);
		//no customer specified, means, all customers
		if(cus.equals("")){
			map.put("customer_area", "");
			map.put("customer_name", "");
		}else{
			map.put("customer_area", area);
			map.put("customer_name", cus);
		}
		DeliverInfoService deliverinfo = new DeliverInfoService();
		try {
			ReturnObject ret = deliverinfo.queryDeliverInfoByInputDelivertime(map);
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			HashMap<String, ArrayList<DeliverInfoAllDTO>> delivers = new HashMap<String, ArrayList<DeliverInfoAllDTO>>();  
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String ordernumber = cDTO.getOrder_num();
				if(delivers.containsKey(ordernumber)){
					delivers.get(ordernumber).add(cDTO);
				}else{
					ArrayList<DeliverInfoAllDTO> delist = new ArrayList<DeliverInfoAllDTO>();
					delist.add(cDTO);
					delivers.put(ordernumber, delist);
				}				
				}
			}
			addToHistory(delivers);//finish
		} catch (Exception e) {
			System.out.println("query the delivers by default time failed");
		}	
		for (int i = 0; i < historyList.size(); i++) {
			DeliverHistory his = historyList.get(i);
			ItemComposite ic = new ItemComposite(composite_fn_record, color_record, width_record, height_record, his);
			ic.setValue(his.getTitleShow(), his.getTimeShow(), his.getValueShow());
			itemList.add(ic);
			composite_scroll_record.setMinSize(composite_fn_record.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));
			composite_fn_record.layout();
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
	
	
	public static void setDetailTimer(boolean detail){
		detailTimer = detail;
	}
	public static boolean getDetailTimer(){
		return detailTimer;
	}
		
}
