package com.storeworld.stock;

import java.util.Date;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class Sorter extends ViewerSorter {
		private static final int BRAND = 1;
		private static final int SUB_BRAND = 2;
		private static final int SIZE = 3;
		private static final int UNIT = 4;
		private static final int AVG_IN = 5;
		private static final int AVG_OUT = 6;
		private static final int REPOSITORY = 7;
		
		public static final Sorter BRAND_ASC = new Sorter(BRAND);
		public static final Sorter BRAND_DESC = new Sorter(-BRAND);
		public static final Sorter SUB_BRAND_ASC = new Sorter(SUB_BRAND);
		public static final Sorter SUB_BRAND_DESC = new Sorter(-SUB_BRAND);
		public static final Sorter SIZE_ASC = new Sorter(SIZE);
		public static final Sorter SIZE_DESC = new Sorter(-SIZE);
		public static final Sorter UNIT_ASC = new Sorter(UNIT);
		public static final Sorter UNIT_DESC = new Sorter(-UNIT);
		public static final Sorter AVG_IN_ASC = new Sorter(AVG_IN);
		public static final Sorter AVG_IN_DESC = new Sorter(-AVG_IN);
		public static final Sorter AVG_OUT_ASC = new Sorter(AVG_OUT);
		public static final Sorter AVG_OUT_DESC = new Sorter(-AVG_OUT);
		public static final Sorter REPOSITORY_ASC = new Sorter(REPOSITORY);
		public static final Sorter REPOSITORY_DESC = new Sorter(-REPOSITORY);
		
		private int sortType ;
		private Sorter(int sortType){
			this.sortType = sortType;
		}
		public int compare(Viewer viewer, Object e1, Object e2) {
			Product p1 = (Product)e1;
			Product p2 = (Product)e2;
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
				case AVG_IN:{
					double d1 = p1.getAvgStockPrice();
					double d2 = p2.getAvgStockPrice();
					return Double.valueOf(d1).compareTo(Double.valueOf(d2));
				}
				case -AVG_IN:{
					double d1 = p1.getAvgStockPrice();
					double d2 = p2.getAvgStockPrice();
					return Double.valueOf(d2).compareTo(Double.valueOf(d1));
				}
				case AVG_OUT:{
					double d1 = p1.getAvgDeliverPrice();
					double d2 = p2.getAvgDeliverPrice();
					return Double.valueOf(d1).compareTo(Double.valueOf(d2));
				}
				case -AVG_OUT:{
					double d1 = p1.getAvgDeliverPrice();
					double d2 = p2.getAvgDeliverPrice();
					return Double.valueOf(d2).compareTo(Double.valueOf(d1));
				}
				case REPOSITORY:{
					int d1 = p1.getRepository();
					int d2 = p2.getRepository();
					return Double.valueOf(d1).compareTo(Double.valueOf(d2));
				}
				case -REPOSITORY:{
					int d1 = p1.getRepository();
					int d2 = p2.getRepository();
					return Double.valueOf(d2).compareTo(Double.valueOf(d1));
				}
			}
			return 0;
		}
	}