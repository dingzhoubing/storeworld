package com.storeworld.customer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * make the customer table sortable
 * @author dingyuanxiong
 *
 */
public class CustomerSorter extends ViewerSorter {
		private static final int NAME = 1;
		private static final int AREA = 2;
		private static final int PHONE = 3;
		private static final int ADDRESS = 4;

		
		public static final CustomerSorter NAME_ASC = new CustomerSorter(NAME);
		public static final CustomerSorter NAME_DESC = new CustomerSorter(-NAME);
		public static final CustomerSorter AREA_ASC = new CustomerSorter(AREA);
		public static final CustomerSorter AREA_DESC = new CustomerSorter(-AREA);
		public static final CustomerSorter PHONE_ASC = new CustomerSorter(PHONE);
		public static final CustomerSorter PHONE_DESC = new CustomerSorter(-PHONE);
		public static final CustomerSorter ADDRESS_ASC = new CustomerSorter(ADDRESS);
		public static final CustomerSorter ADDRESS_DESC = new CustomerSorter(-ADDRESS);
		
		private int sortType ;
		private CustomerSorter(int sortType){
			this.sortType = sortType;
		}
		//is null, then the largest, cus of the new row
		public int compare(Viewer viewer, Object e1, Object e2) {
			Customer p1 = (Customer)e1;
			Customer p2 = (Customer)e2;
			switch(sortType){
				case NAME:{
					if(p1.getName() !=null  && p2.getName() != null){
					String l1 = p1.getName();
					String l2 = p2.getName();
					return l1.compareTo(l2);
					}else{
						if(p1.getName() == null){
							return 1;
						}else{
							return -1;
						}
					}
				}
				case -NAME:{
					if(p1.getName() != null && p2.getName() != null){
					String l1 = p1.getName();
					String l2 = p2.getName();
					return l2.compareTo(l1);
					}else{
						if(p1.getName() == null){
							return 1;
						}else{
							return -1;
						}
					}
				}
				case AREA:{
					if(p1.getArea()!=null && p2.getArea()!=null){
					String s1 = p1.getArea();
					String s2 = p2.getArea();					
					return s1.compareTo(s2);
					}else{
						if(p1.getArea() == null){
							return 1;
						}else{
							return -1;
						}
					}
				}
				case -AREA:{
					if(p1.getArea()!=null && p2.getArea()!=null){
					String s1 = p1.getArea();
					String s2 = p2.getArea();
					return s2.compareTo(s1);
					}else{
						if(p1.getArea() == null){
							return 1;
						}else{
							return -1;
						}
					}
				}
				case PHONE:{
					if(p1.getPhone()!=null && p2.getPhone()!=null){
					String s1 = p1.getPhone();
					String s2 = p2.getPhone();
					return s1.compareTo(s2);
					}else{
						if(p1.getPhone() == null){
							return 1;
						}else{
							return -1;
						}
					}
				}
				case -PHONE:{
					if(p1.getPhone()!=null && p2.getPhone()!=null){
					String s1 = p1.getPhone();
					String s2 = p2.getPhone();
					return s2.compareTo(s1);
					}else{
						if(p1.getPhone() == null){
							return 1;
						}else{
							return -1;
						}
					}
				}
				case ADDRESS:{
					if(p1.getAddress()!=null && p2.getAddress()!=null){
					String i1 = p1.getAddress();
					String i2 = p2.getAddress();
					return i1.compareTo(i2);
					}else{
						if(p1.getAddress() == null){
							return 1;
						}else{
							return -1;
						}
					}
				}
				case -ADDRESS:{
					if(p1.getAddress()!=null && p2.getAddress()!=null){
					String i1 = p1.getAddress();
					String i2 = p2.getAddress();
					return i2.compareTo(i1);
					}else{
						if(p1.getAddress() == null){
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