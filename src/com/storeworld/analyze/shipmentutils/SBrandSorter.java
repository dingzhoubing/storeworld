package com.storeworld.analyze.shipmentutils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
/**
 * we need to make the table sortable for users
 */
public class SBrandSorter extends ViewerSorter {
	private static final int SUB_BRAND = 1;
	private static final int SHIPMENT = 2;
	private static final int RATIO = 3;
	
	public static final SBrandSorter SUB_BRAND_ASC = new SBrandSorter(SUB_BRAND);
	public static final SBrandSorter SUB_BRAND_DESC = new SBrandSorter(-SUB_BRAND);
	public static final SBrandSorter SHIPMENT_ASC = new SBrandSorter(SHIPMENT);
	public static final SBrandSorter SHIPMENT_DESC = new SBrandSorter(-SHIPMENT);
	public static final SBrandSorter RATIO_ASC = new SBrandSorter(RATIO);
	public static final SBrandSorter RATIO_DESC = new SBrandSorter(-RATIO);
	
	
	private int sortType ;
	private SBrandSorter(int sortType){
		this.sortType = sortType;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		SBrandAnalyzer p1 = (SBrandAnalyzer)e1;
		SBrandAnalyzer p2 = (SBrandAnalyzer)e2;
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
			case SHIPMENT:{
				String s1 = p1.getShipment();
				String s2 = p2.getShipment();
				return s1.compareTo(s2);
			}
			case -SHIPMENT:{
				String s1 = p1.getShipment();
				String s2 = p2.getShipment();
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