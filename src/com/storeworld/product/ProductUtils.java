package com.storeworld.product;

public class ProductUtils {
	private static String newLineID = "";
	
	
	/**
	 * set the initial new ID of the table
	 * @param id
	 */
	public static void setNewLineID(String id){
		newLineID = id;
	}
	
	public static String getNewLineID(){
		return newLineID;
	}
}
