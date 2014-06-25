package com.storeworld.stock;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * filter of stock data table
 * @author dingyuanxiong
 *
 */
public class StockFilter extends ViewerFilter {
	private static String filterword = "";
	private static boolean isFirst = true;
	
	public static void resetIsFirst(){
		isFirst = true;
	}
	
	private static void setFilter(String fw){
		filterword = fw;
	}
	
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		Stock st = (Stock)element;
		String brand = st.getBrand();
		if(isFirst){
			setFilter(brand);
			isFirst = false;
		}
		//this blank row has to be clear and show again??
		if(st.getBrand().equals("") || st.getSubBrand().equals(""))// || st.getSize().equals("")
			return false;
		if(st.getBrand().contains(filterword))
			return false;
		
		return true;
	}
}