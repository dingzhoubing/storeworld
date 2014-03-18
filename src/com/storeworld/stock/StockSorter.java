package com.storeworld.stock;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class StockSorter extends ViewerSorter {
	private static final int BRAND = 1;
	private static final int SUB_BRAND = 2;
	private static final int SIZE = 3;
	private static final int UNIT = 4;
	private static final int PRICE = 5;
	private static final int NUMBER = 6;
	
	public static final StockSorter BRAND_ASC = new StockSorter(BRAND);
	public static final StockSorter BRAND_DESC = new StockSorter(-BRAND);
	public static final StockSorter SUB_BRAND_ASC = new StockSorter(SUB_BRAND);
	public static final StockSorter SUB_BRAND_DESC = new StockSorter(-SUB_BRAND);
	public static final StockSorter SIZE_ASC = new StockSorter(SIZE);
	public static final StockSorter SIZE_DESC = new StockSorter(-SIZE);
	public static final StockSorter UNIT_ASC = new StockSorter(UNIT);
	public static final StockSorter UNIT_DESC = new StockSorter(-UNIT);
	public static final StockSorter PRICE_ASC = new StockSorter(PRICE);
	public static final StockSorter PRICE_DESC = new StockSorter(-PRICE);
	public static final StockSorter NUMBER_ASC = new StockSorter(NUMBER);
	public static final StockSorter NUMBER_DESC = new StockSorter(-NUMBER);
	
	private int sortType ;
	private StockSorter(int sortType){
		this.sortType = sortType;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		Stock p1 = (Stock)e1;
		Stock p2 = (Stock)e2;
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
				double i1 = p1.getPrice();
				double i2 = p2.getPrice();
				return Double.valueOf(i1).compareTo(Double.valueOf(i2));
			}
			case -PRICE:{
				double i1 = p1.getPrice();
				double i2 = p2.getPrice();
				return Double.valueOf(i2).compareTo(Double.valueOf(i1));
			}
			case NUMBER:{
				int i1 = p1.getNumber();
				int i2 = p2.getNumber();
				return Integer.valueOf(i1).compareTo(Integer.valueOf(i2));		
			}
			case -NUMBER:{
				int i1 = p1.getNumber();
				int i2 = p2.getNumber();
				return Integer.valueOf(i2).compareTo(Integer.valueOf(i1));				
			}

		}
		return 0;
	}
	}