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

	
	/**
	 * the item composite list
	 */
	private static ArrayList<ItemComposite> itemList = new ArrayList<ItemComposite>();
	
	/**
	 * the item content is the history panel
	 */
	private static ArrayList<DeliverHistory> historyList = new ArrayList<DeliverHistory>();
//	private static ArrayList<History> areas = new ArrayList<History>();
	
	
	/**
	 * the option to search the history
	 * @param option
	 */
	private static void getHistoryFromDataBase(String option){
		
		for(int i=0;i<5;i++){
			DeliverHistory his = new DeliverHistory("123","456","789"+Constants.SPACE);
			historyList.add(his);
		}
	}
	
	private static void addToHistory(){
		
	}
	public static void showHistoryPanel(ScrolledComposite composite_scroll, Composite composite_fn, Color color, int width, int height){
		//search the database
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
			DeliverHistory his) {
		// search the database
		addToHistory();

		ItemComposite ic = new ItemComposite(composite_fn, color, width, height, his);
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
