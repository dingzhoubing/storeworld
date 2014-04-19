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
	String time_show; //the time showed like "m 月  d日"
	String number;
	
	DeliverHistory(String title, String time, String number){
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

	/**
	 * get the format we need by time
	 */
	@Override
	public String getTimeShow(){
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		if(month.startsWith("0"))
			month = month.substring(1);
		if(day.startsWith("0"))
			day = day.substring(1);
		return month+"月  " + day + "日";			
	}

}
