package com.storeworld.product;

import java.util.HashSet;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.storeworld.customer.Customer;
import com.storeworld.customer.CustomerUtils;

public class ProductFilter extends ViewerFilter {
	
	private static String filterword = "";
	private static boolean isFirst = true;
	
	//this is a cache of the chooser
	private static HashSet<String> brands = new HashSet<String>();
	private static String keyword = "";//the search world
	
	public static void setKeyword(String kw){
		keyword = kw;
	}
	
	public static void resetIsFirst(){
		isFirst = true;
	}
	
	private static void setFilter(String fw){
		filterword = fw;
	}

	public static void setBrand(String br, String tp){
		brands.add(br);		
	}
	public static void removeBrand(String br, String tp){
		brands.remove(br);
	}
	
	private boolean isPassed(Product prod){
		String brand = prod.getBrand();
		String sub = prod.getSubBrand();
		String size = prod.getSize();
		String unit = prod.getUnit();
		String repo = prod.getRepository();
		String tmp = keyword.trim();
		//simple search method
		if(prod.getBrand() == null || prod.getBrand().equals(""))
			return true;
		if(tmp.equals("") || brand.contains(tmp) || sub.contains(tmp) || size.contains(tmp) || unit.contains(tmp) ||repo.contains(tmp))
			return true;
		else
			return false;
	}
	
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		Product prod = (Product)element;	
		//if user click the search button
		if(ProductUtils.getSearchButtonClicked()){
			if(isPassed(prod)){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			//empty row always return
			if(prod.getBrand() == null || prod.getBrand().equals("")){
				return true;
			}			
			//no check box selected
			if(brands.isEmpty())
			{
				return true;
			}
			else{
				//selected area check box
				if(brands.contains(prod.getBrand())){
					return true;
				}				
				return false;
			}
		}
	}
}