package com.storeworld.product;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * make the product table sortable
 * @author dingyuanxiong
 *
 */
public class ProductSorter extends ViewerSorter {
		private static final int BRAND = 1;
		private static final int SUB_BRAND = 2;
		private static final int SIZE = 3;
		private static final int UNIT = 4;
//		private static final int AVG_IN = 5;
//		private static final int AVG_OUT = 6;
		private static final int REPOSITORY = 5;
		
		public static final ProductSorter BRAND_ASC = new ProductSorter(BRAND);
		public static final ProductSorter BRAND_DESC = new ProductSorter(-BRAND);
		public static final ProductSorter SUB_BRAND_ASC = new ProductSorter(SUB_BRAND);
		public static final ProductSorter SUB_BRAND_DESC = new ProductSorter(-SUB_BRAND);
		public static final ProductSorter SIZE_ASC = new ProductSorter(SIZE);
		public static final ProductSorter SIZE_DESC = new ProductSorter(-SIZE);
		public static final ProductSorter UNIT_ASC = new ProductSorter(UNIT);
		public static final ProductSorter UNIT_DESC = new ProductSorter(-UNIT);
//		public static final Sorter AVG_IN_ASC = new Sorter(AVG_IN);
//		public static final Sorter AVG_IN_DESC = new Sorter(-AVG_IN);
//		public static final Sorter AVG_OUT_ASC = new Sorter(AVG_OUT);
//		public static final Sorter AVG_OUT_DESC = new Sorter(-AVG_OUT);
		public static final ProductSorter REPOSITORY_ASC = new ProductSorter(REPOSITORY);
		public static final ProductSorter REPOSITORY_DESC = new ProductSorter(-REPOSITORY);
		
		private int sortType ;
		private ProductSorter(int sortType){
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
//				case AVG_IN:{
//					double d1 = p1.getAvgStockPrice();
//					double d2 = p2.getAvgStockPrice();
//					return Double.valueOf(d1).compareTo(Double.valueOf(d2));
//				}
//				case -AVG_IN:{
//					double d1 = p1.getAvgStockPrice();
//					double d2 = p2.getAvgStockPrice();
//					return Double.valueOf(d2).compareTo(Double.valueOf(d1));
//				}
//				case AVG_OUT:{
//					double d1 = p1.getAvgDeliverPrice();
//					double d2 = p2.getAvgDeliverPrice();
//					return Double.valueOf(d1).compareTo(Double.valueOf(d2));
//				}
//				case -AVG_OUT:{
//					double d1 = p1.getAvgDeliverPrice();
//					double d2 = p2.getAvgDeliverPrice();
//					return Double.valueOf(d2).compareTo(Double.valueOf(d1));
//				}
				case REPOSITORY:{
					String d1 = p1.getRepository();
					String d2 = p2.getRepository();
					int double_d1 = Integer.valueOf(d1);
					int double_d2 = Integer.valueOf(d2);
//					return Double.valueOf(d1).compareTo(Double.valueOf(d2));
					return Integer.valueOf(double_d1).compareTo(Integer.valueOf(double_d2));
					
				}
				case -REPOSITORY:{
					String d1 = p1.getRepository();
					String d2 = p2.getRepository();
					int double_d1 = Integer.valueOf(d1);
					int double_d2 = Integer.valueOf(d2);
//					return Double.valueOf(d1).compareTo(Double.valueOf(d2));
					return Integer.valueOf(double_d2).compareTo(Integer.valueOf(double_d1));
				}
			}
			return 0;
		}
	}