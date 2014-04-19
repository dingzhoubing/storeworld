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
	
	/**
	 * just as other pages
	 *  we need to check if the row is the last row
	 */
	public int compare(Viewer viewer, Object e1, Object e2) {
		Deliver p1 = (Deliver)e1;
		Deliver p2 = (Deliver)e2;
		switch(sortType){
		case BRAND:{
			if(!p1.getBrand().equals("")  && !p2.getBrand().equals("")){
				String l1 = p1.getBrand();
				String l2 = p2.getBrand();
				return l1.compareTo(l2);
				}else{
					if(p1.getBrand().equals("")){
						return 1;
					}else{
						return -1;
					}
				}
		}
		case -BRAND:{
			if(!p1.getBrand().equals("")  && !p2.getBrand().equals("")){
				String l1 = p1.getBrand();
				String l2 = p2.getBrand();
				return l2.compareTo(l1);
				}else{
					if(p1.getBrand().equals("")){
						return 1;
					}else{
						return -1;
					}
				}
		}
		case SUB_BRAND:{
			if(!p1.getSubBrand().equals("")  && !p2.getSubBrand().equals("")){
				String l1 = p1.getSubBrand();
				String l2 = p2.getSubBrand();
				return l1.compareTo(l2);
				}else{
					if(p1.getSubBrand().equals("")){
						return 1;
					}else{
						return -1;
					}
				}
		}
		case -SUB_BRAND:{
			if(!p1.getSubBrand().equals("")  && !p2.getSubBrand().equals("")){
				String l1 = p1.getSubBrand();
				String l2 = p2.getSubBrand();
				return l2.compareTo(l1);
				}else{
					if(p1.getSubBrand().equals("")){
						return 1;
					}else{
						return -1;
					}
				}	
		}
		case SIZE:{
			if(!p1.getSize().equals("")  && !p2.getSize().equals("")){
				String l1 = p1.getSize();
				String l2 = p2.getSize();
				return l1.compareTo(l2);
				}else{
					if(p1.getSize().equals("")){
						return 1;
					}else{
						return -1;
					}
				}	
		}
		case -SIZE:{
			if(!p1.getSize().equals("")  && !p2.getSize().equals("")){
				String l1 = p1.getSize();
				String l2 = p2.getSize();
				return l2.compareTo(l1);
				}else{
					if(p1.getSize().equals("")){
						return 1;
					}else{
						return -1;
					}
				}	
		}
		case UNIT:{
			if(!p1.getUnit().equals("")  && !p2.getUnit().equals("")){
				String l1 = p1.getUnit();
				String l2 = p2.getUnit();
				return l1.compareTo(l2);
				}else{
					if(p1.getUnit().equals("")){
						return 1;
					}else{
						return -1;
					}
				}	
		}
		case -UNIT:{
			if(!p1.getUnit().equals("")  && !p2.getUnit().equals("")){
				String l1 = p1.getUnit();
				String l2 = p2.getUnit();
				return l2.compareTo(l1);
				}else{
					if(p1.getUnit().equals("")){
						return 1;
					}else{
						return -1;
					}
				}	
		}
		case PRICE:{
			if(!p1.getPrice().equals("") && !p2.getPrice().equals("")){
				double i1 = Double.valueOf(p1.getPrice());
				double i2 = Double.valueOf(p2.getPrice());
				return Double.valueOf(i1).compareTo(Double.valueOf(i2));
				}else{
					if(p1.getPrice().equals("")){
						return 1;
					}else{
						return -1;
					}
				}					
		}
		case -PRICE:{
			if(!p1.getPrice().equals("") && !p2.getPrice().equals("")){
				double i1 = Double.valueOf(p1.getPrice());
				double i2 = Double.valueOf(p2.getPrice());
				return Double.valueOf(i2).compareTo(Double.valueOf(i1));
				}else{
					if(p1.getPrice().equals("")){
						return 1;
					}else{
						return -1;
					}
				}	
		}
		case NUMBER:{
			if(!p1.getNumber().equals("") && !p2.getNumber().equals("")){
				int i1 = Integer.valueOf(p1.getNumber());
				int i2 = Integer.valueOf(p2.getNumber());
				return Integer.valueOf(i1).compareTo(Integer.valueOf(i2));
				}else{
					if(p1.getNumber().equals("")){
						return 1;
					}else{
						return -1;
					}
				}						
		}
		case -NUMBER:{
			if(!p1.getNumber().equals("") && !p2.getNumber().equals("")){
				int i1 = Integer.valueOf(p1.getNumber());
				int i2 = Integer.valueOf(p2.getNumber());
				return Integer.valueOf(i2).compareTo(Integer.valueOf(i1));
				}else{
					if(p1.getNumber().equals("")){
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