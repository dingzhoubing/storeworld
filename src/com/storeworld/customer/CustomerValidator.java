package com.storeworld.customer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerValidator {

//	private static String current_id = "";
	
//	public static void setNewID(String id){
//		current_id = id;
//	}
//	public static String getNewID(){
//		return current_id;
//	}
	
	private static Pattern pattern_name = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]{1,5}$");
	private static Pattern pattern_area = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]+$");
//	private static Pattern pattern_phone = Pattern.compile("\\d{4}[-]*\\d{8}|\\d{4}[-]*\\d{7}|\\d(3)[-]*\\d(8)|^[0-9]*[-]*[1][0-9]+\\d{9}");
	private static Pattern pattern_phone = Pattern.compile("\\d+");
	private static Pattern pattern_address = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]*$");
	/**
	 * validate the name of the customer
	 * @param table
	 * @param item
	 * @param col
	 * @param name
	 * @return
	 */
	public static boolean validateName(String name){//Table table, TableItem item, int col, 
		//1~5 number of Chinese Character
//		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]{1,5}");
		Matcher matcher = pattern_name.matcher(name); 
		if(name != null && !name.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	/**
	 * validate the area of the customer
	 * @param table
	 * @param item
	 * @param col
	 * @param area
	 * @return
	 */
	public static boolean validateArea(String area){//Table table, TableItem item, int col,
		Matcher matcher = pattern_area.matcher(area); 
		if(area != null && !area.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	
	/**
	 * validate the phone of the customer
	 * @param table
	 * @param item
	 * @param col
	 * @param phone
	 * @return
	 */
	public static boolean validatePhone(String phone){//Table table, TableItem item, int col,

		Matcher matcher = pattern_phone.matcher(phone); 
		if(phone != null && !phone.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	
	/**
	 * validate the address of the customer
	 * @param table
	 * @param item
	 * @param col
	 * @param address
	 * @return
	 */
	public static boolean validateAddress(String address){//Table table, TableItem item, int col,

		Matcher matcher = pattern_address.matcher(address); 
		if(matcher.matches() || address.equals("")){

			return true;
		}
		else{

			return false;
		}
	}
		
	/**
	 * check if the current edit row is the new row
	 * @param id
	 * @return
	 */
	public static boolean checkID(String id){
//		if(id.equals(getNewID()))//dynamic to the newest ID
		if(id.equals(CustomerUtils.getNewLineID()))
			return true;
		else
			return false;
	}
	/**
	 * check if the current row is legal
	 * @param c
	 * @return
	 */
	public static boolean rowLegal(Customer c){
		if(c.getName() !=null && c.getArea()!=null){
			if(!c.getName().equals("") && !c.getArea().equals(""))//&& !c.getPhone().equals("")
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
}
