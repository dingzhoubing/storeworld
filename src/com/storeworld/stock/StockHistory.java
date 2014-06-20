package com.storeworld.stock;

import com.storeworld.common.History;

/**
 * the sub-class of History, to show the stock history of stock page
 * left navigator
 * @author dingyuanxiong
 *
 */
public class StockHistory extends History {
	String title = "";
	String time = "";
	String time_show = "";
	String number = "";
	String indeed = "";
	
	
	StockHistory(String title, String time, String number, String indeed){
		this.title = title;
		this.time = time;
		this.number = number;
		this.indeed = indeed;
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
