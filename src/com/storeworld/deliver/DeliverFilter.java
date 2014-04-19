package com.storeworld.deliver;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * filter the deliver by patterns, no use now
 * @author dingyuanxiong
 *
 */
public class DeliverFilter extends ViewerFilter {
	private static String filterword = "";
	private static boolean isFirst = true;
	
	public static void resetIsFirst(){
		isFirst = true;
	}
	
	private static void setFilter(String fw){
		filterword = fw;
	}
	
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		Deliver st = (Deliver)element;
		String brand = st.getBrand();
		if(isFirst){
			setFilter(brand);
			isFirst = false;
		}
		if(st.getBrand().equals("") || st.getSubBrand().equals("") || st.getSize().equals(""))
			return false;
		if(st.getBrand().contains(filterword))
			return false;
		
		return true;
	}
}