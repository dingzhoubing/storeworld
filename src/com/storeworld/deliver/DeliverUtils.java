package com.storeworld.deliver;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.storeworld.utils.Constants;
import com.storeworld.utils.ItemComposite;

public class DeliverUtils {
	
	private static String newLineID = "";
	private static int currentBrandLine=0;
	private static String current_sub_brand = "";
	
	static class History{
		String title;
		String time;
		String number;
		
		History(String title, String time, String number){
			this.title = title;
			this.time = time;
			this.number = number;
		}
		
		public void setTitle(String title){
			this.title = title;
		}
		public void setTime(String time){
			this.time = time;
		}
		public void setNumber(String number){
			this.number = number;
		}
		public String getTitle(){
			return this.title;
		}
		public String getTime(){
			return this.time;
		}
		public String getNumber(){
			return this.number;
		}
	}
	
	/**
	 * the item composite list
	 */
	private static ArrayList<ItemComposite> itemList = new ArrayList<ItemComposite>();
	
	/**
	 * the item content is the history panel
	 */
	private static ArrayList<History> historyList = new ArrayList<History>();
//	private static ArrayList<History> areas = new ArrayList<History>();
	
	
	/**
	 * the option to search the history
	 * @param option
	 */
	private static void getHistoryFromDataBase(String option){
		
		for(int i=0;i<5;i++){
			History his = new History("123","456","789"+Constants.SPACE);
			historyList.add(his);
		}
	}
	
	private static void addToHistory(){
		
	}
	public static void showHistoryPanel(ScrolledComposite composite_scroll, Composite composite_fn, Color color, int width, int height){
		//search the database
		getHistoryFromDataBase("");
		
		for (int i = 0; i < historyList.size(); i++) {
			History his = historyList.get(i);
			ItemComposite ic = new ItemComposite(composite_fn, color, width, height);
			ic.setValue(his.getTitle(), his.getTime(), his.getNumber());
			itemList.add(ic);
			composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));
			composite_fn.layout();
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
	public static void addToHistoryPanel(ScrolledComposite composite_scroll,
			Composite composite_fn, Color color, int width, int height,
			History his) {
		// search the database
		addToHistory();

		ItemComposite ic = new ItemComposite(composite_fn, color, width, height);
		ic.setValue(his.getTitle(), his.getTime(), his.getNumber());
		itemList.add(ic);
		composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		composite_fn.layout();

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
