package com.storeworld.customer;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.storeworld.common.DataInTable;

public class CustomerFilter extends ViewerFilter {
		
	private static String area="";
	private static String firstname="";
	private static String type = "";
	private static HashSet<Customer> filterout = new HashSet<Customer>();
	
	
	//this is a cache of the chooser
	private static HashSet<String> areas = new HashSet<String>();
	private static HashSet<String> firstnames = new HashSet<String>();
	
	
	
	
	public static void initialFilterOut(ArrayList<DataInTable> list){
		for(int i=0;i<list.size();i++){
			filterout.add((Customer)list.get(i));
		}
		
	}
	/**
	 * set the area filter
	 * @param ar
	 */
	public static void setArea(String ar, String tp){
		type = tp;
		areas.add(ar);
	}
	/**
	 * set the first name filter
	 * @param fm
	 */
	public static void setFirstName(String fm, String tp){
		type = tp;
		firstnames.add(fm);
	}
	
	public static void removeArea(String ar, String tp){
		type = tp;
		areas.remove(ar);
	}
	/**
	 * set the first name filter
	 * @param fm
	 */
	public static void removeFirstName(String fm, String tp){
		type = tp;
		firstnames.remove(fm);
	}
	
	/**
	 * clear the elements meet requirements
	 */
	public static void clearFilterOut(){
		filterout.clear();
	}
	
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Customer cus = (Customer)element;
		//empty row always return
		if(cus.getArea() == null || cus.getArea().equals(""))
			return true;
		if(cus.getName()==null || cus.getName().equals(""))
			return true;
		//no check box selected
		if(areas.isEmpty() && firstnames.isEmpty())
			return true;
		else{
			//selected area check box
			if(areas.contains(cus.getArea()))
				return true;
			//selected firstname check box
			if(firstnames.contains(cus.getName().substring(0, 1)))
				return true;
			return false;
		}
		
	}
}