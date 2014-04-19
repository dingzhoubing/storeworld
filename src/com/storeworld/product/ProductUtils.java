package com.storeworld.product;

import com.storeworld.utils.Utils;

public class ProductUtils {
	private static String newLineID = "";
	private static ProductFilter pf = new ProductFilter();
	
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
	
	public static void refreshTableData(){
		
		ProductContentPart.getTableViewer().addFilter(pf);
		ProductContentPart.getTableViewer().removeFilter(pf);
		Utils.refreshTable(ProductContentPart.getTableViewer().getTable());
		ProductFilter.resetIsFirst();
	}
}
