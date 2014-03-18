package com.storeworld.stock;
public class Stock{
	private String id;//the identifier of the record
	private String brand;
	private String sub_brand;
	private String size;
	private String unit;
	private double price;
	private int number;
	
	public Stock(){
		
	}
	public Stock(String brand,String sub_brand,String size,String unit, double price, int number){
		this(null, brand,sub_brand,size,unit,price, number);
	}
	public Stock(String ID, String brand,String sub_brand,String size,String unit, double price, int number){
		if(ID != null)		
			this.id = ID;
		this.brand = brand;
		this.sub_brand = sub_brand;
		this.size = size;
		this.unit = unit;
		this.price = price;
		this.number = number;
	}
	public String getID(){
		return this.id;
	}
	public void setID(String ID){
		this.id = ID;
	}
	public String getBrand() {
		return this.brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSubBrand() {
		return this.sub_brand;
	}
	public void setSubBrand(String sub_brand) {
		this.sub_brand = sub_brand;
	}
	public String getSize() {
		return this.size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getUnit() {
		return this.unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getPrice(){
		return this.price;
	}
	public void setPrice(double price){
		this.price = price;
	}
	public int getNumber(){
		return this.number;
	}
	public void setNumber(int number){
		this.number = number;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.id+" ");
		sb.append(this.brand+" ");
		sb.append(this.sub_brand+" ");
		sb.append(this.size+" ");
		sb.append(this.unit+" ");
		sb.append(this.price+" ");
		sb.append(this.number);
		return sb.toString();
	}
	
}