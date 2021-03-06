package com.storeworld.product;

import com.storeworld.common.DataInTable;

/**
 * definition of the product
 * @author dingyuanxiong
 *
 */
public class Product extends DataInTable{
	private String id="";//the identifier of the record
	private String brand="";
	private String sub_brand="";
	private String size="";
	private String unit="";
	private String repository="";//make it easy to process
	
	public Product(){
		
	}
	public Product(String ID){
		this.id = ID;
	}
	
	public Product(String brand,String sub_brand,String size,String unit, String repository){
		this(null, brand,sub_brand,size,unit,repository);
	}
	public Product(String ID, String brand,String sub_brand,String size,String unit, String repository){
		if(ID != null)		
			this.id = ID;
		this.brand = brand;
		this.sub_brand = sub_brand;
		this.size = size;
		this.unit = unit;
		this.repository = repository;		
	}
	
	/**
	 * all getters, setters
	 * @return
	 */
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
	public String getRepository() {
		return this.repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.id+" ");
		sb.append(this.brand+" ");
		sb.append(this.sub_brand+" ");
		sb.append(this.size+" ");
		sb.append(this.unit+" ");
		sb.append(this.repository);
		return sb.toString();
	}
	
}