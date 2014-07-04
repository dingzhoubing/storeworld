package com.storeworld.product;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductValidator {

//	private static String current_id = "";
//	
//	public static void setNewID(String id){
//		current_id = id;
//	}
//	public static String getNewID(){
//		return current_id;
//	}
	
	//brand or sub_brand
	private static Pattern pattern_brand = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
	//start with number, end with(out) Chinese character or 26 English character
	private static Pattern pattern_size = Pattern.compile("^\\d+[\\u4E00-\\u9FA5\\uF900-\\uFA2DA-Za-z]*$");
	private static Pattern pattern_unit = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]{1,5}$");
	private static Pattern pattern_repository = Pattern.compile("^([1-9][0-9]*)$");
//	private static Pattern pattern_price = Pattern.compile("^([1-9][0-9]*(\\.[0-9]{1,2})?|0\\.(?!0+$)[0-9]{1,2})$");
//	private static Pattern pattern_number = Pattern.compile("^([1-9][0-9]*$");
	
	/**
	 * validate the brand of the product
	 * @param brand
	 * @return
	 */
	public static boolean validateBrand(String brand){//Table table, TableItem item, int col, 
		Matcher matcher = pattern_brand.matcher(brand); 
		
		if(brand != null && !brand.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	/**
	 * validate the sub_brand of the product
	 * @param sub_brand
	 * @return
	 */
	public static boolean validateSub_Brand(String sub_brand){//Table table, TableItem item, int col,
		
		Matcher matcher = pattern_brand.matcher(sub_brand); 
		
		if(sub_brand != null && !sub_brand.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	
	/**
	 * validate the size of the product
	 * @param size
	 * @return
	 */
	public static boolean validateSize(String size){//Table table, TableItem item, int col,
				
		Matcher matcher = pattern_size.matcher(size); 
		
		if(size != null && !size.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	
	/**
	 * validate the unit of the product
	 * @param unit
	 * @return
	 */
	public static boolean validateUnit(String unit){//Table table, TableItem item, int col,
		Matcher matcher = pattern_unit.matcher(unit); 
		
		if(unit != null && !unit.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}

	/**
	 * validate the repository of the product
	 * @param repo
	 * @return
	 */
	public static boolean validateRepository(String repo){//Table table, TableItem item, int col,
		
		Matcher matcher = pattern_repository.matcher(repo); 
		
		if(repo != null && (repo.equals("") || matcher.matches())){

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
		if(id.equals(ProductUtils.getNewLineID()))
			return true;
		else
			return false;
	}
	/**
	 * check if the current row is legal
	 * @param p
	 * @return
	 */
	public static boolean rowLegal(Product p){
		if(p.getBrand() !=null && p.getSubBrand()!=null){//no use, never null
			
			if(!p.getBrand().equals("") && !p.getSubBrand().equals(""))//&& !p.getSize().equals("")
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
}
