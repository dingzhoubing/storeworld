package com.storeworld.pojo.dto;

public class GoodsBatchInfo implements java.io.Serializable{
	
	private String brand;
	private String sub_brand;
	private Float unit_price;
	private String quantity;
	private String standard;
	private String batchNo;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	
	public GoodsBatchInfo(String brand,String sub_brand,Float unit_price,
			String batchNo,String quantity,String standard,String reserve1,String reserve2,String reserve3){

		this.brand=brand;
		this.sub_brand=sub_brand;
		this.unit_price=unit_price;
		this.batchNo=batchNo;
		this.quantity=quantity;
		this.standard=standard;
		this.reserve1=reserve1;
		this.reserve2=reserve2;
		this.reserve3=reserve3;

	}

	public GoodsBatchInfo(){

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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
