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
		private static final int REPOSITORY = 5;
		
		public static final ProductSorter BRAND_ASC = new ProductSorter(BRAND);
		public static final ProductSorter BRAND_DESC = new ProductSorter(-BRAND);
		public static final ProductSorter SUB_BRAND_ASC = new ProductSorter(SUB_BRAND);
		public static final ProductSorter SUB_BRAND_DESC = new ProductSorter(-SUB_BRAND);
		public static final ProductSorter SIZE_ASC = new ProductSorter(SIZE);
		public static final ProductSorter SIZE_DESC = new ProductSorter(-SIZE);
		public static final ProductSorter UNIT_ASC = new ProductSorter(UNIT);
		public static final ProductSorter UNIT_DESC = new ProductSorter(-UNIT);
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
					if(p1.getBrand() !=null  && p2.getBrand() != null){
						String l1 = p1.getBrand();
						String l2 = p2.getBrand();
						return l1.compareTo(l2);
						}else{
							if(p1.getBrand() == null){
								return 1;
							}else{
								return -1;
							}
						}					
				}
				case -BRAND:{
					if(p1.getBrand() !=null  && p2.getBrand() != null){
						String l1 = p1.getBrand();
						String l2 = p2.getBrand();
						return l2.compareTo(l1);
						}else{
							if(p1.getBrand() == null){
								return 1;
							}else{
								return -1;
							}
						}
				}
				case SUB_BRAND:{
					if(p1.getSubBrand() !=null  && p2.getSubBrand() != null){
						String l1 = p1.getSubBrand();
						String l2 = p2.getSubBrand();
						return l1.compareTo(l2);
						}else{
							if(p1.getSubBrand() == null){
								return 1;
							}else{
								return -1;
							}
						}					
				}
				case -SUB_BRAND:{
					if(p1.getSubBrand() !=null  && p2.getSubBrand() != null){
						String l1 = p1.getSubBrand();
						String l2 = p2.getSubBrand();
						return l2.compareTo(l1);
						}else{
							if(p1.getSubBrand() == null){
								return 1;
							}else{
								return -1;
							}
						}	
				}
				case SIZE:{
					if(p1.getSize() !=null  && p2.getSize() != null){
						String l1 = p1.getSize();
						String l2 = p2.getSize();
						return l1.compareTo(l2);
						}else{
							if(p1.getSize() == null){
								return 1;
							}else{
								return -1;
							}
						}						
				}
				case -SIZE:{
					if(p1.getSize() !=null  && p2.getSize() != null){
						String l1 = p1.getSize();
						String l2 = p2.getSize();
						return l2.compareTo(l1);
						}else{
							if(p1.getSize() == null){
								return 1;
							}else{
								return -1;
							}
						}	
				}
				case UNIT:{
					if(p1.getUnit() !=null  && p2.getUnit() != null){
						String l1 = p1.getUnit();
						String l2 = p2.getUnit();
						return l1.compareTo(l2);
						}else{
							if(p1.getUnit() == null){
								return 1;
							}else{
								return -1;
							}
						}					
				}
				case -UNIT:{
					if(p1.getUnit() !=null  && p2.getUnit() != null){
						String l1 = p1.getUnit();
						String l2 = p2.getUnit();
						return l2.compareTo(l1);
						}else{
							if(p1.getUnit() == null){
								return 1;
							}else{
								return -1;
							}
						}	
				}

				case REPOSITORY:{
					if(p1.getRepository() !=null  && p2.getRepository() != null){
						String d1 = p1.getRepository();
						String d2 = p2.getRepository();
						int double_d1 = Integer.valueOf(d1);
						int double_d2 = Integer.valueOf(d2);
						return Integer.valueOf(double_d1).compareTo(Integer.valueOf(double_d2));
						}else{
							if(p1.getRepository() == null){
								return 1;
							}else{
								return -1;
							}
						}	
				}
				case -REPOSITORY:{
					if(p1.getRepository() !=null  && p2.getRepository() != null){
						String d1 = p1.getRepository();
						String d2 = p2.getRepository();
						int double_d1 = Integer.valueOf(d1);
						int double_d2 = Integer.valueOf(d2);
						return Integer.valueOf(double_d2).compareTo(Integer.valueOf(double_d1));
						}else{
							if(p1.getRepository() == null){
								return 1;
							}else{
								return -1;
							}
						}
				}
			}
			return 0;
		}
	}