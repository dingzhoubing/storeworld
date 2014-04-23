package com.storeworld.stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.product.ProductContentPart;
import com.storeworld.product.ProductFilter;
import com.storeworld.pub.service.StockInfoService;
import com.storeworld.utils.ItemComposite;
import com.storeworld.utils.Utils;

public class StockUtils {
	
	private static String newLineID = "";
	private static int currentBrandLine=0;
	private static String current_sub_brand = "";
	//the time when click add a new stock
	private static String time_record = "";
	private static boolean firstTime = true;
	private static StockFilter sf = new StockFilter();
	
	/**
	 *  record the composite or property for refreshing the left navigator
	 */
	private static ScrolledComposite composite_scroll_record;
	private static Composite composite_fn_record;
	private static Color color_record;
	private static int width_record;
	private static int height_record;
	
	//item composite
	private static ItemComposite ic_record;
	//true: table&time etc. are in edit mode, false: normal mode|view mode
	private static boolean editMode = false;
	/**
	 * 1. if add a new stock record, time_record = ""
	 * 2. if change the table, set the time_record="yyyyMMddHHmmss"
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
	
	public static String getTime(){
		return time_record;
	}
	
	public static void recordItemComposite(ItemComposite ic){
		ic_record = ic;
	}
	public static ItemComposite getItemCompositeRecord(){
		return ic_record;
	}
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
	 * the item composite list
	 */
	private static ArrayList<ItemComposite> itemList = new ArrayList<ItemComposite>();
	
	/**
	 * the item content is the history panel
	 */
	private static ArrayList<StockHistory> historyList = new ArrayList<StockHistory>();
	
	public static ArrayList<ItemComposite> getItemList(){
		return itemList;
	}
	public static ArrayList<StockHistory> getHistoryList(){
		return historyList;
	}
	
	/**
	 * update history, the stocks contains the empty one
	 * so: we use stocks.size()-2 to ignore the last one
	 * @param stocks
	 */
	public static void updateHistory(ArrayList<DataInTable> stocks){
		
		String title = "";
		double total = 0.000;
		String time_tmp = "";
		for(int i=0;i<stocks.size()-2;i++){
			Stock st = (Stock)stocks.get(i);
			title+=(st.getBrand()+",");
			time_tmp = st.getTime();
			String price = st.getPrice();
			String number = st.getNumber();
			double p = Double.valueOf(price);
			int n = Integer.valueOf(number);
			total+=(p * n);	
		}
		Stock st = (Stock)stocks.get(stocks.size()-2);
		String price = st.getPrice();
		String number = st.getNumber();
		time_tmp = st.getTime();
		double p = Double.valueOf(price);
		int n = Integer.valueOf(number);
		total+=(p * n);	
		
		title+=(st.getBrand());//title
		
		String number_total = String.valueOf(total);
		StockHistory shis = (StockHistory)ic_record.getHistory();
		shis.setTitle(title);
		shis.setTime(time_tmp);
		shis.setNumber(number_total);
		
		ic_record.setValue(shis.getTitle(), shis.getTimeShow(), shis.getNumber());
		
	}
	
	/**
	 * make multi-stocks into a history item
	 * @param stocks
	 */
	public static void addToHistory(HashMap<String, ArrayList<Stock>> stockmap){
		
		for(String key : stockmap.keySet()){
			ArrayList<Stock> stocks = stockmap.get(key);
			
			String title = "";
			double total = 0.000;
			String time_tmp = "";
			for(int i=0;i<stocks.size()-1;i++){
				Stock st = stocks.get(i);
				title+=(st.getBrand()+",");
				time_tmp = st.getTime();
				String price = st.getPrice();
				String number = st.getNumber();
				double p = Double.valueOf(price);
				int n = Integer.valueOf(number);
				total+=(p * n);	
			}
			Stock st = stocks.get(stocks.size()-1);
			String price = st.getPrice();
			String number = st.getNumber();
			time_tmp = st.getTime();
			double p = Double.valueOf(price);
			int n = Integer.valueOf(number);
			total+=(p * n);	
			
			title+=(st.getBrand());//title
	
			String number_total = String.valueOf(total);
			
			StockHistory his = new StockHistory(title,time_tmp,number_total);
			historyList.add(his);	
		}
		
			
	}
	
	private static void addIntoStocks(Stock st, HashMap<String, ArrayList<Stock>> stocks){
		String time = st.getTime();
		if(stocks.containsKey(time)){
			stocks.get(time).add(st);
		}else{
			ArrayList<Stock> stlist = new ArrayList<Stock>();
			stlist.add(st);
			stocks.put(time, stlist);
		}
	}
	
	/**
	 * the option to search the history, time threshold
	 * @param option
	 */
	private static void getHistoryFromDataBase(String option){
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("stock_time", option);
		StockInfoService stockinfo = new StockInfoService();
		try {
			ReturnObject ret = stockinfo.queryStockInfoByDefaultStocktime(map);
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
//			String last_time = "";
//			ArrayList<Stock> stocks = new ArrayList<Stock>();
			HashMap<String, ArrayList<Stock>> stocks = new HashMap<String, ArrayList<Stock>>(); 
			for(int i=0;i<list.size();i++){
				StockInfoDTO cDTO_tmp = (StockInfoDTO) list.get(i);
				Stock st_tmp = new Stock();
//				String tmp_time = cDTO_tmp.getStock_time();
				st_tmp.setBrand(cDTO_tmp.getBrand());
				st_tmp.setPrice(String.valueOf(cDTO_tmp.getUnit_price()));
				st_tmp.setNumber(cDTO_tmp.getQuantity());
				st_tmp.setTime(cDTO_tmp.getStock_time());
				addIntoStocks(st_tmp,stocks);
			}
			addToHistory(stocks);
			
		} catch (Exception e) {
			System.out.println("query the stocks by default time failed");
		}	
		
		
	}
	
	/**
	 * add the current table into the history panel
	 * this happened when: there is something in stock table, and user click the 
	 * "add a new stock table" button
	 */
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
//		String number="50000";//fake data
		String number = StockContentPart.getTotal().trim();
		
		StockHistory his = new StockHistory(title,time_tmp,number);
		
		ItemComposite ic = new ItemComposite(composite_fn_record, color_record, width_record, height_record, his);
		ic.setValue(his.getTitle(), his.getTimeShow(), his.getNumber());
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
	 * show the history panel, when first into the stock panel
	 * get the data(today, this month etc.) from database
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
	
	public static void showSearchHistory(String dateSearch){
		//remove the navigator panel, clear all the result
		for(int i=0;i<itemList.size();i++)
			itemList.get(i).dispose();
		itemList.clear();
		historyList.clear();
		
		//add search result
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("stock_time", dateSearch);
		StockInfoService stockinfo = new StockInfoService();
		try {
			ReturnObject ret = stockinfo.queryStockInfoByInputStocktime(map);
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
//			String last_time = "";
//			ArrayList<Stock> stocks = new ArrayList<Stock>();
			HashMap<String, ArrayList<Stock>> stocks = new HashMap<String, ArrayList<Stock>>(); 
			for(int i=0;i<list.size();i++){
				StockInfoDTO cDTO_tmp = (StockInfoDTO) list.get(i);
				Stock st_tmp = new Stock();
//				String tmp_time = cDTO_tmp.getStock_time();
				st_tmp.setBrand(cDTO_tmp.getBrand());
				st_tmp.setPrice(String.valueOf(cDTO_tmp.getUnit_price()));
				st_tmp.setNumber(cDTO_tmp.getQuantity());
				st_tmp.setTime(cDTO_tmp.getStock_time());
				addIntoStocks(st_tmp, stocks);
			}
			
			
//			if(list.size()>0){
//				StockInfoDTO cDTO = (StockInfoDTO) list.get(0);
//				Stock stock = new Stock();
//				stock.setBrand(cDTO.getBrand());
//				stock.setPrice(String.valueOf(cDTO.getUnit_price()));
//				stock.setNumber(cDTO.getQuantity());
//				stock.setTime(cDTO.getStock_time());
//				stocks.add(stock);
//				last_time = stock.getTime();
//				for(int i=1;i<list.size();i++){
//					StockInfoDTO cDTO_tmp = (StockInfoDTO) list.get(i);
//					Stock st_tmp = new Stock();
//					String tmp_time = cDTO_tmp.getStock_time();
//					st_tmp.setBrand(cDTO_tmp.getBrand());
//					st_tmp.setPrice(String.valueOf(cDTO_tmp.getUnit_price()));
//					st_tmp.setNumber(cDTO_tmp.getQuantity());
//					st_tmp.setTime(cDTO_tmp.getStock_time());
//					if(tmp_time.equals(last_time)){						
//						stocks.add(st_tmp);//if still the same stock, add to array list
//					}else{//a new stock
//						addToHistory(stocks);
//						stocks.clear();
//						stocks.add(st_tmp);
//						last_time = tmp_time;
//					}				
//				}
//				//the last stock
//				if(!stocks.isEmpty()){
//					addToHistory(stocks);//finish
//				}
//			}
			
			//add into history
			addToHistory(stocks);
		} catch (Exception e) {
			System.out.println("query the stocks by default time failed");
		}	
		for (int i = 0; i < historyList.size(); i++) {
			StockHistory his = historyList.get(i);
			ItemComposite ic = new ItemComposite(composite_fn_record, color_record, width_record, height_record, his);
			ic.setValue(his.getTitle(), his.getTimeShow(), his.getNumber());
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
		
		StockContentPart.getTableViewer().addFilter(sf);
		StockContentPart.getTableViewer().removeFilter(sf);
		Utils.refreshTable(StockContentPart.getTableViewer().getTable());
		StockFilter.resetIsFirst();
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
