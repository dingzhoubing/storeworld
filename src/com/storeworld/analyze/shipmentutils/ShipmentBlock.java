package com.storeworld.analyze.shipmentutils;

/**
 * using this class to pass all the need args to the BrandComposite
 * @author dingyuanxiong
 *
 */
public class ShipmentBlock {
	
	private int rowHeight;//=30
	private int width;
	private int height;

	private String brand;
	/**
	 * to determin the type to analyze is: shipment or profit
	 * true: shipment
	 * false: profit
	 */
	private boolean shipment_profit;
	/**
	 * to determin the type to analyze is: sub_brand or area
	 * true: sub_brand
	 * false: area
	 */
	private boolean sbrand_area;
	
	public ShipmentBlock(){}
	public ShipmentBlock(int rowHeight, int width, int height,String brand, boolean shipment_profit, 
			boolean sbrand_area){
		this.rowHeight = rowHeight;
		this.width = width;
		this.height = height;
		this.brand = brand;
		this.shipment_profit = shipment_profit;		
		this.sbrand_area = sbrand_area;
	}
	
	public void setSbrand_Area(boolean type){
		this.sbrand_area = type;
	}
	public boolean getSbrand_Area(){
		return this.sbrand_area;
	}
	
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
	
	public void setBrand(String brand){
		this.brand = brand;
	}
	public String getBrand(){
		return this.brand;
	}
	
	public void setShipment_Profit(boolean type){
		this.shipment_profit = type;
	}
	public boolean getShipment_Profit(){
		return this.shipment_profit;
	}
	
	
}
