package com.storeworld.customer;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.storeworld.common.DataInTable;

public class CustomerFilter extends ViewerFilter {
		
//	private static String area="";
//	private static String firstname="";
//	private static String type = "";
	private static HashSet<Customer> filterout = new HashSet<Customer>();
	
	
	//this is a cache of the chooser
	private static HashSet<String> areas = new HashSet<String>();
	private static HashSet<String> firstnames = new HashSet<String>();
	private static String keyword = "";//the search world
	
	public static void setKeyword(String kw){
		keyword = kw;
	}
	
	public static void initialFilterOut(ArrayList<DataInTable> list){
		for(int i=0;i<list.size();i++){
			filterout.add((Customer)list.get(i));
		}
		
	}
	/**
	 * set the area filter
	 * @param ar
	 * @param tp: do not use now
	 */
	public static void setArea(String ar, String tp){
//		type = tp;
		areas.add(ar);
	}
	
	/**
	 * set the first name filter
	 * @param fm
	 */
	public static void setFirstName(String fm, String tp){
//		type = tp;
		firstnames.add(fm);
	}
	
	/**
	 * remove an area from filter
	 * @param ar
	 * @param tp
	 */
	public static void removeArea(String ar, String tp){
//		type = tp;
		areas.remove(ar);
	}
	
	/**
	 * remove the first name filter
	 * @param fm
	 */
	public static void removeFirstName(String fm, String tp){
//		type = tp;
		firstnames.remove(fm);
	}
	
	/**
	 * clear the elements meet requirements
	 */
	public static void clearFilterOut(){
		filterout.clear();
	}
	
	/**
	 * is this customer should be returned when user click the search button
	 * @param cus
	 * @return
	 */
	private boolean isPassed(Customer cus){
		String name = cus.getName();
		String area = cus.getArea();
		String phone = cus.getPhone();
		String addr = cus.getAddress();
		String tmp = keyword.trim();
		//simple search method
		if(cus.getName() == null || cus.getArea()==null || cus.getName().equals("") || cus.getArea().equals(""))
			return true;
		if(tmp.equals("") || name.contains(tmp) || area.contains(tmp) || addr.contains(tmp) || phone.contains(tmp))
			return true;
		else
			return false;
	}
		
	/**
	 * filter the data in table 
	 * return true: show this row
	 * return false: do not show this row
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Customer cus = (Customer)element;
		
		//if user click the search button
		if(CustomerUtils.getSearchButtonClicked()){
			if(isPassed(cus)){
//				CustomerUtils.increaseVisibleLine();
				return true;
			}
			else{
				return false;
			}
		}
		else{
			//empty row always return
			if(cus.getArea() == null || cus.getArea().equals("")){
//				CustomerUtils.increaseVisibleLine();
				return true;
			}
			if(cus.getName()==null || cus.getName().equals("")){
//				CustomerUtils.increaseVisibleLine();
				return true;
			}
			//no check box selected
			if(areas.isEmpty() && firstnames.isEmpty())
			{
//				CustomerUtils.increaseVisibleLine();
				return true;
			}
			else{
				//selected area check box
				if(areas.contains(cus.getArea())){
//					CustomerUtils.increaseVisibleLine();
					return true;
				}
				//selected firstname check box
				if(firstnames.contains(cus.getName().substring(0, 1))){
//					CustomerUtils.increaseVisibleLine();
					return true;
				}
				return false;
			}
		}
		
	}
}