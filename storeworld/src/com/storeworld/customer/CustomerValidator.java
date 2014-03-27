package com.storeworld.customer;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CustomerValidator {

	private static String current_id = "";
	
	public static void setNewID(String id){
		current_id = id;
	}
	public static String getNewID(){
		return current_id;
	}
	/**
	 * validate the name of the customer
	 * @param table
	 * @param item
	 * @param col
	 * @param name
	 * @return
	 */
	public static boolean validateName(String name){//Table table, TableItem item, int col, 
		if(name != null && !name.equals("") && !name.equals("123")){

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
		if(area != null && !area.equals("") && !area.equals("123")){

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

//		return true;//now, always return true
//		return false;
		if(phone != null && !phone.equals("") && !phone.equals("123")){

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

		return true;//now, always return true
//		return false;
	}
		
	/**
	 * check if the current edit row is the new row
	 * @param id
	 * @return
	 */
	public static boolean checkID(String id){
		if(id.equals(getNewID()))//dynamic to the newest ID
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
			if(!c.getName().equals("") && !c.getArea().equals(""))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
}
