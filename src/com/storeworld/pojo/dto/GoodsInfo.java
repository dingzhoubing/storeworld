package com.storeworld.pojo.dto;

public class GoodsInfo implements java.io.Serializable{

	private String id;
	private String brand;
	private String sub_brand;
	private Float unit_price;
	private String unit;
	private String standard;
	private String repertory; 
	private String reserve1;
	private String reserve2;
	private String reserve3;

	public GoodsInfo(String brand,String sub_brand,Float unit_price,
	 String unit,String standard,String repertory,String reserve1,
	 String reserve2,String reserve3){
		this.brand=brand;
		this.sub_brand=sub_brand;
		this.unit_price=unit_price;
		this.unit=unit;
		this.standard=standard;
		this.repertory=repertory;
		this.reserve1=reserve1;
		this.reserve2=reserve2;
		this.reserve3=reserve3;
	}

	public GoodsInfo(){

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSub_brand() {
		return sub_brand;
	}

	public void setSub_brand(String sub_brand) {
		this.sub_brand = sub_brand;
	}

	public Float getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(Float unit_price) {
		this.unit_price = unit_price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getRepertory() {
		return repertory;
	}

	public void setRepertory(String repertory) {
		this.repertory = repertory;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}


}