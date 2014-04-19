package com.storeworld.utils;

import com.storeworld.customer.Customer;

/**
 * 
 * @author dingyuanxiong
 * using this class to record the data between different pages
 */
public class UIDataConnector {
	
	//record if user click deliver in customer page
	private static boolean fromCustomer = false;
	private static Customer customer = null;
	
	/**
	 * record the flag of "click deliver in customer page"
	 * @param fromcustomer
	 */
	public static void setFromCustomer(boolean fromcustomer){
		fromCustomer = fromcustomer;
	}
	
	/**
	 * get the flag of "click deliver in customer page" 
	 * @return
	 */
	public static boolean getFromCustomer(){
		return fromCustomer;
	}
	
	/**
	 * record the customer if "click deliver in customer page"
	 * @param cus
	 */
	public static void setCustomerRecord(Customer cus){
		customer = cus;
	}
	
	/**
	 * get the record customer
	 * @return
	 */
	public static Customer getCustomerRecord(){
		return customer;
	}
	
	
}
