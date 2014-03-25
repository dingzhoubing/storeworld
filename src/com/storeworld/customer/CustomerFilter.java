package com.storeworld.customer;

import java.util.HashSet;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class CustomerFilter extends ViewerFilter {
	
//	private static ArrayList<String> areas = new ArrayList<String>();
//	private static ArrayList<String> firstname = new ArrayList<String>();
//			
//	public static void setAreasFilter(ArrayList<String> filters){
//		areas.addAll(filters);
//	}
//	public static void setAreasFilter(String filter){
//		areas.add(filter);
//	}
//	public static ArrayList<String> getAreasFilter(){
//		return areas;
//	}
//	
//	public static void setFirstNameFilter(ArrayList<String> filters){
//		firstname.addAll(filters);
//	}
//	public static void setFirstNameFilter(String filter){
//		firstname.add(filter);
//	}
//	public static ArrayList<String> getFirstNameFilter(){
//		return firstname;
//	}
//	
//	private boolean filterArea(String area){
//		for(int i=0;i<areas.size();i++){
//			if(areas.get(i).contains(area))
//				return true;
//		}
//		return false;
//	}
//	
//	private boolean filterFirstName(String fm){
//		for(int i=0;i<firstname.size();i++){
//			if(firstname.get(i).startsWith(fm))
//				return true;
//		}
//		return false;
//	}
	
	private static String area="";
	private static String firstname="";
	private static HashSet<Customer> filterout = new HashSet<Customer>();
	
	/**
	 * set the area filter
	 * @param ar
	 */
	public static void setArea(String ar){
		area = ar;
	}
	/**
	 * set the first name filter
	 * @param fm
	 */
	public static void setFirstName(String fm){
		firstname = fm;
	}
	/**
	 * clear the elements meet requirements
	 */
	public static void clearFilterOut(){
		filterout.clear();
	}
	
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Customer cus = (Customer)element;
		if(cus != null && cus.getArea()!=null && cus.getName()!=null){
			if(cus.getArea().contains(area)){				
				return true;
			}
			else{
				return false;
			}
		}else{
			return true;
		}
	}
}