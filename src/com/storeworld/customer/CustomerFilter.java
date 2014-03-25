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
		area = ar;
	}
	/**
	 * set the first name filter
	 * @param fm
	 */
	public static void setFirstName(String fm, String tp){
		type = tp;
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
		if(type.equals("area")){
			if(cus != null && cus.getArea()!=null && cus.getName()!=null){
				if(cus.getArea().contains(area)){// && filterout.contains(cus)				
					//if matched, remove it from the filterout
//					filterout.remove(cus);
					return true;
					
				}
				else{
					return false;
				}
			}else{
				//always keep this line
				return true;
			}
		}else{
			if(cus != null && cus.getArea()!=null && cus.getName()!=null){
				if(cus.getName().startsWith(firstname)){	
//					filterout.remove(cus);
					return true;
				}
				else{
					return false;
				}
			}else{
				//always keep the blank line
				return true;
			}
		}
	}
}