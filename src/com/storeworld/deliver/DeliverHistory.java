package com.storeworld.deliver;

import com.storeworld.common.History;

/**
 * 
 * @author dingyuanxiong
 * store and change the value of history in deliver table history panel
 */
public class DeliverHistory extends History {
	String title;
	String time;      //the time like "yyyyMMddhhmmss"
	String time_show; //the time showed like "m ‘¬  d»’"
	String number;
	String ordernumber;
	
	DeliverHistory(String title, String time, String number, String ordernumber){
		this.title = title;
		this.time = time;
		this.number = number;
		this.ordernumber = ordernumber;
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

}
