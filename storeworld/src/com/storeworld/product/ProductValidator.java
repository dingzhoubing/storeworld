package com.storeworld.product;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class ProductValidator {

	private static String current_id = "";
	
	public static void setNewID(String id){
		current_id = id;
	}
	public static String getNewID(){
		return current_id;
	}
	/**
	 * validate the brand of the product
	 * @param table
	 * @param item
	 * @param col
	 * @param name
	 * @return
	 */
	public static boolean validateBrand(String brand){//Table table, TableItem item, int col, 
		if(brand != null && !brand.equals("") && !brand.equals("123")){

			return true;
		}
		else{

			return false;
		}
	}
	/**
	 * validate the sub_brand of the product
	 * @param table
	 * @param item
	 * @param col
	 * @param area
	 * @return
	 */
	public static boolean validateSub_Brand(String sub_brand){//Table table, TableItem item, int col,
		if(sub_brand != null && !sub_brand.equals("") &&!sub_brand.equals("123")){

			return true;
		}
		else{

			return false;
		}
	}
	
	/**
	 * validate the size of the product
	 * @param table
	 * @param item
	 * @param col
	 * @param phone
	 * @return
	 */
	public static boolean validateSize(String size){//Table table, TableItem item, int col,

		return true;//now, always return true
//		return false;
	}
	
	/**
	 * validate the unit of the product
	 * @param table
	 * @param item
	 * @param col
	 * @param address
	 * @return
	 */
	public static boolean validateUnit(String unit){//Table table, TableItem item, int col,

		return true;//now, always return true
//		return false;
	}
		
	public static boolean validateRepository(String repo){//Table table, TableItem item, int col,
		
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
	public static boolean rowLegal(Product p){
		if(p.getBrand() !=null && p.getSubBrand()!=null){
			
			if(!p.getBrand().equals("") && !p.getSubBrand().equals(""))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
}
