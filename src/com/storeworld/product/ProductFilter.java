package com.storeworld.product;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ProductFilter extends ViewerFilter {
	
	private static String filterword = "";
	private static boolean isFirst = true;
	
	public static void resetIsFirst(){
		isFirst = true;
	}
	
	private static void setFilter(String fw){
		filterword = fw;
	}
	
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		Product prod = (Product)element;
		String brand = prod.getBrand();
		if(isFirst){
			setFilter(brand);
			isFirst = false;
		}
		if(prod.getBrand().equals("") || prod.getSubBrand().equals("") || prod.getSize().equals(""))
			return true;
		if(prod.getBrand().contains(filterword))
			return true;
		
		return false;
	}
}