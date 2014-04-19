package com.storeworld.stock;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * make the stock table sortable
 * @author dingyuanxiong
 *
 */
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
	
	/**
	 * if the row is the last row in table, always be the last one 
	 * for user to input new record 
	 */
	public int compare(Viewer viewer, Object e1, Object e2) {
		Stock p1 = (Stock)e1;
		Stock p2 = (Stock)e2;
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