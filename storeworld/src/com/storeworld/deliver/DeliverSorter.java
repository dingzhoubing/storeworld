package com.storeworld.deliver;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * make the deliver table sortable
 * @author dingyuanxiong
 *
 */
public class DeliverSorter extends ViewerSorter {
	private static final int BRAND = 1;
	private static final int SUB_BRAND = 2;
	private static final int SIZE = 3;
	private static final int UNIT = 4;
	private static final int PRICE = 5;
	private static final int NUMBER = 6;
	
	public static final DeliverSorter BRAND_ASC = new DeliverSorter(BRAND);
	public static final DeliverSorter BRAND_DESC = new DeliverSorter(-BRAND);
	public static final DeliverSorter SUB_BRAND_ASC = new DeliverSorter(SUB_BRAND);
	public static final DeliverSorter SUB_BRAND_DESC = new DeliverSorter(-SUB_BRAND);
	public static final DeliverSorter SIZE_ASC = new DeliverSorter(SIZE);
	public static final DeliverSorter SIZE_DESC = new DeliverSorter(-SIZE);
	public static final DeliverSorter UNIT_ASC = new DeliverSorter(UNIT);
	public static final DeliverSorter UNIT_DESC = new DeliverSorter(-UNIT);
	public static final DeliverSorter PRICE_ASC = new DeliverSorter(PRICE);
	public static final DeliverSorter PRICE_DESC = new DeliverSorter(-PRICE);
	public static final DeliverSorter NUMBER_ASC = new DeliverSorter(NUMBER);
	public static final DeliverSorter NUMBER_DESC = new DeliverSorter(-NUMBER);
	
	private int sortType ;
	private DeliverSorter(int sortType){
		this.sortType = sortType;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		Deliver p1 = (Deliver)e1;
		Deliver p2 = (Deliver)e2;
		switch(sortType){
			case BRAND:{
				String l1 = p1.getBrand();
				String l2 = p2.getBrand();
				return l1.compareTo(l2);
			}
			case -BRAND:{
				String l1 = p1.getBrand();
				String l2 = p2.getBrand();
				return l2.compareTo(l1);
			}
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
			case SIZE:{
				String s1 = p1.getSize();
				String s2 = p2.getSize();
				return s1.compareTo(s2);
			}
			case -SIZE:{
				String s1 = p1.getSize();
				String s2 = p2.getSize();
				return s2.compareTo(s1);
			}
			case UNIT:{
				String i1 = p1.getUnit();
				String i2 = p2.getUnit();
				return i1.compareTo(i2);
			}
			case -UNIT:{
				String i1 = p1.getUnit();
				String i2 = p2.getUnit();
				return i2.compareTo(i1);
			}
			case PRICE:{
				double i1 = Double.valueOf(p1.getPrice());
				double i2 = Double.valueOf(p2.getPrice());
				return Double.valueOf(i1).compareTo(Double.valueOf(i2));
			}
			case -PRICE:{
				double i1 = Double.valueOf(p1.getPrice());
				double i2 = Double.valueOf(p2.getPrice());
				return Double.valueOf(i2).compareTo(Double.valueOf(i1));
			}
			case NUMBER:{
				int i1 = Integer.valueOf(p1.getNumber());
				int i2 = Integer.valueOf(p2.getNumber());
				return Integer.valueOf(i1).compareTo(Integer.valueOf(i2));		
			}
			case -NUMBER:{
				int i1 = Integer.valueOf(p1.getNumber());
				int i2 = Integer.valueOf(p2.getNumber());
				return Integer.valueOf(i2).compareTo(Integer.valueOf(i1));				
			}

		}
		return 0;
	}
	}