package com.storeworld.utils;


/**
 * has no use now
 * @author dingyuanxiong
 *
 */
public class ComboUtils {
	/**
	 * in GeneralComboCellEditor, in constructor, we call super
	 * which make the createContrl called before left part of the constructor
	 * but we want to pass the args
	 * in_width: the width of the row column
	 * in_coL: the row number
	 * type: stock or deliver
	 */
	private static int in_width_stock_brand;
	private static int in_col_stock_brand;
	private static int in_width_stock_sub_brand;
	private static int in_col_stock_sub_brand;
	
	private static int in_width_deliver_brand;
	private static int in_col_deliver_brand;
	private static int in_width_deliver_sub_brand;
	private static int in_col_deliver_sub_brand;
	
	private static String in_type;
	
	public static void setWidth_Col(int width, int col, String type){
		if(type.equals(Constants.STOCK_TYPE_BRAND)){
			in_width_stock_brand = width;
			in_col_stock_brand = col;
		}else if(type.equals(Constants.STOCK_TYPE_SUB_BRAND)){
			in_width_stock_sub_brand = width;
			in_col_stock_sub_brand = col;
		}else if(type.equals(Constants.DELIVER_TYPE_BRAND)){
			in_width_deliver_brand = width;
			in_col_deliver_brand = col;
		}else if(type.equals(Constants.DELIVER_TYPE_SUB_BRAND)){
			in_width_deliver_sub_brand = width;
			in_col_deliver_sub_brand = col;
		} 
		
		in_type = type;
	}
	
	public static String getType(){
		return in_type;
	}
	public static int getWidth_Stock_Brand(){
		return in_width_stock_brand;
	}
	
	public static int getCol_Stock_Brand(){
		return in_col_stock_brand;
	}
	
	public static int getWidth_Stock_Sub_Brand(){
		return in_width_stock_sub_brand;
	}
	
	public static int getCol_Stock_Sub_Brand(){
		return in_col_stock_sub_brand;
	}
	
	public static int getWidth_Deliver_Brand(){
		return in_width_deliver_brand;
	}
	
	public static int getCol_Deliver_Brand(){
		return in_col_deliver_brand;
	}
	
	public static int getWidth_Deliver_Sub_Brand(){
		return in_width_deliver_sub_brand;
	}
	
	public static int getCol_Deliver_Sub_Brand(){
		return in_col_deliver_sub_brand;
	}

}
