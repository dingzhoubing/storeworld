package com.storeworld.deliver;

import com.storeworld.common.DataInTable;

/**
 * define the deliver class
 * @author dingyuanxiong
 *
 */
public class Deliver extends DataInTable{
	private String id="";//the identifier of the record
	private String brand="";
	private String sub_brand="";
	private String size="";
	private String unit="";
	private String price="";
	private String number="";
	
	public Deliver(){
		
	}
	
	public Deliver(String id){
		this.id = id;
	}

	public Deliver(String brand,String sub_brand,String size,String unit, String price, String number){
		this(null, brand,sub_brand,size,unit,price, number);
	}
	public Deliver(String ID, String brand,String sub_brand,String size,String unit, String price, String number){
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
	public String getPrice(){
		return this.price;
	}
	public void setPrice(String price){
		this.price = price;
	}
	public String getNumber(){
		return this.number;
	}
	public void setNumber(String number){
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