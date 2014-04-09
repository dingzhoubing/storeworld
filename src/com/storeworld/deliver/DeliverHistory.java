package com.storeworld.deliver;

import com.storeworld.common.History;

public class DeliverHistory extends History {
	String title;
	String time;
	String time_show;
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
	//change here
	public String getTimeShow(){
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		if(month.startsWith("0"))
			month = month.substring(1);
		if(day.startsWith("0"))
			day = day.substring(1);
		return month+"ÔÂ  " + day + "ÈÕ";			
	}

}
