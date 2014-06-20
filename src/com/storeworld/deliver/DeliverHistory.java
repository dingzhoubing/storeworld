package com.storeworld.deliver;

import com.storeworld.common.History;
import com.storeworld.stock.StockHistory;

/**
 * 
 * @author dingyuanxiong
 * store and change the value of history in deliver table history panel
 */
public class DeliverHistory extends History {
	String title="";
	String time="";      //the time like "yyyyMMddhhmmss"
	String time_show=""; //the time showed like "m 月  d日"
	String number="";
	String ordernumber="";
	String indeed="";
	String is_print = "";
	
	DeliverHistory(String title, String time, String number, String ordernumber, String indeed, String isPrint){
		this.title = title;
		this.time = time;
		this.number = number;
		this.ordernumber = ordernumber;
		this.indeed = indeed;
		this.is_print = isPrint;
	}
	
	public void setIsPrint(String isprint){
		this.is_print = isprint;
	}
	public String getIsPrint(){
		return this.is_print;
	}
	
	public void setIndeed(String indeed){
		this.indeed = indeed;
	}
	public String getIndeed(){
		return this.indeed;
	}
	
	public void setOrderNumber(String order){
		this.ordernumber = order;
	}
	public String getOrderNumber(){
		return ordernumber;
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

	/**
	 * get the format we need by time
	 */
	@Override
	public String getTimeShow(){
//		String month = time.substring(4, 6);
//		String day = time.substring(6, 8);
		String hour = time.substring(8, 10);
		String min = time.substring(10, 12);
		if(hour.startsWith("0"))
			hour = hour.substring(1);
//		if(min.startsWith("0"))
//			min = min.substring(1);
		return hour+":  " + min;			
	}

	public String getTitleShow(){
		if(is_print.equals(""))
			return this.title;
		else
			return this.title+"(已打单)";
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
		DeliverHistory his = (DeliverHistory)obj;
		if(this.getTime().equals(his.getTime()))//or use order number
			return true;
		else
			return false;
	}
	
	
}
