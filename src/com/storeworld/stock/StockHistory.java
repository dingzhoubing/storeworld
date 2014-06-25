package com.storeworld.stock;

import com.storeworld.common.History;

/**
 * the sub-class of History, to show the stock history of stock page
 * left navigator
 * @author dingyuanxiong
 *
 */
public class StockHistory extends History {
	
	String title = ""; //title
	String time = ""; //time in String like "20140618105823"
	String time_show = ""; //time in String like "8ÔÂ14"
	String number = ""; //number of total
	String indeed = ""; //number of indeed
	
	
	StockHistory(String title, String time, String number, String indeed){
		this.title = title;
		this.time = time;
		this.number = number;
		this.indeed = indeed;
	}
	
	//getters and setters
	public void setTitle(String title){
		this.title = title;
	}
	public void setTime(String time){
		this.time = time;
	}
	public void setNumber(String number){
		this.number = number;
	}
	public void setIndeed(String indeed){
		this.indeed = indeed;
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
	public String getIndeed(){
		return this.indeed;
	}
	
	/**
	 * get time show by time value 
	 */
	public String getTimeShow(){
		if(!time.equals("")){
			String month = time.substring(4, 6);
			String day = time.substring(6, 8);
			if(month.startsWith("0"))
				month = month.substring(1);
			if(day.startsWith("0"))
				day = day.substring(1);
			return month+"ÔÂ  " + day + "ÈÕ";
		}else{
			return "";
		}
		
	}

	/**
	 * get value show by number and indeed
	 * @return
	 */
	public String getValueShow(){
		//this means the indeed is the same as number
		if(indeed.equals(""))
			return number;
		else
			return indeed;
	}
	
	@Override
	public boolean equals(Object obj) {
		StockHistory his = (StockHistory)obj;
		if(this.getTime().equals(his.getTime()))
			return true;
		else
			return false;
	}
	
}
