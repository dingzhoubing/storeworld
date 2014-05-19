package com.storeworld.analyze.ratioutils;

import com.storeworld.analyze.AnalyzerUtils.KIND;

/**
 * using this class to pass all the need args to the RatioComposite
 * @author dingyuanxiong
 *
 */
public class RatioBlock {
	
	private int rowHeight;//=30
	private int width;
	private int height;

	
	/**
	 * to determine the type to analyze is: shipment or profit
	 * true: shipment
	 * false: profit
	 */
	private KIND kind;
	
	/**
	 * if get the result of all brands or sub of a specified brand
	 * true: all brands
	 * false: sub of a specified brand
	 */
	private boolean brand_sub = false;
	
	/**
	 * if get the result of all area or customers of a specified area
	 * true: all areas
	 * false: customer of a specified area
	 */
	private boolean area_customer = false;
	
	private String brand = "";//if brand_sub is false, set the brand
	private String area = "";//if area_customers is false, set the area
	
	/**
	 * true: brand, sub
	 * false: area, customer
	 */
	private boolean brand_area = false;
	/**
	 * the data to be shown in table
	 */	
//	private RatioResultList resultlist;
	
	public RatioBlock(){}
	
	
//	public void setRatioResultList(RatioResultList resultlist){
//		this.resultlist = resultlist;
//	}
//	public RatioResultList getRatioResultList(){
//		return this.resultlist;
//	}	
	
	public void setRowHeight(int height){
		this.rowHeight = height;
	}
	public int getRowHeight(){
		return this.rowHeight;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	public int getWidth(){
		return this.width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	public int getHeight(){
		return this.height;
	}
	
	public void setKind(KIND kind){
		this.kind = kind;
	}
	public KIND getKind(){
		return this.kind;
	}
	
	public void setBrand_sub(boolean brand_sub){
		this.brand_sub = brand_sub;
	}
	public boolean getBrand_sub(){
		return this.brand_sub;
	}
	
	public void setArea_customer(boolean area_cus){
		this.area_customer = area_cus;
	}
	public boolean getArea_customer(){
		return this.area_customer;
	}
	
	public void setBrand(String brand){
		this.brand = brand;
	}
	public String getBrand(){
		return this.brand;
	}
	
	public void setArea(String area){
		this.area = area;
	}
	public String getArea(){
		return this.area;
	}
	
	public void setBrand_area(boolean ba){
		this.brand_area = ba;
	}
	public boolean getBrand_area(){
		return this.brand_area;
	}
	
	
	
	
}
