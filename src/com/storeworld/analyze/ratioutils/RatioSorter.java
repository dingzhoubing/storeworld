package com.storeworld.analyze.ratioutils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
/**
 * we need to make the table sortable for users
 */
public class RatioSorter extends ViewerSorter {
	private static final int SUB_BRAND = 1;
	private static final int SHIPMENT_PROFIT = 2;
	private static final int RATIO = 3;
	
	public static final RatioSorter SUB_BRAND_ASC = new RatioSorter(SUB_BRAND);
	public static final RatioSorter SUB_BRAND_DESC = new RatioSorter(-SUB_BRAND);
	public static final RatioSorter SHIPMENT_PROFIT_ASC = new RatioSorter(SHIPMENT_PROFIT);
	public static final RatioSorter SHIPMENT_PROFIT_DESC = new RatioSorter(-SHIPMENT_PROFIT);
	public static final RatioSorter RATIO_ASC = new RatioSorter(RATIO);
	public static final RatioSorter RATIO_DESC = new RatioSorter(-RATIO);
	
	
	private int sortType ;
	private RatioSorter(int sortType){
		this.sortType = sortType;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		RatioAnalyzer p1 = (RatioAnalyzer)e1;
		RatioAnalyzer p2 = (RatioAnalyzer)e2;
		switch(sortType){				
			case SUB_BRAND:{
				String s1 = p1.getSubBrand();
				String s2 = p2.getSubBrand();
				return s1.compareTo(s2);
			}
			case -SUB_BRAND:{
				String s1 = p1.getSubBrand();
				String s2 = p2.getSubBrand();
				return s2.compareTo(s1);
			}
			case SHIPMENT_PROFIT:{
				String s1 = p1.getShipment_Profit();
				String s2 = p2.getShipment_Profit();
				return s1.compareTo(s2);
			}
			case -SHIPMENT_PROFIT:{
				String s1 = p1.getShipment_Profit();
				String s2 = p2.getShipment_Profit();
				return s2.compareTo(s1);
			}
			case RATIO:{
				String i1 = p1.getRatio();
				String i2 = p2.getRatio();
				return i1.compareTo(i2);
			}
			case -RATIO:{
				String i1 = p1.getRatio();
				String i2 = p2.getRatio();
				return i2.compareTo(i1);
			}			

		}
		return 0;
	}
}