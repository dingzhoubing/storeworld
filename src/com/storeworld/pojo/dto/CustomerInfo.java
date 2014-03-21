package com.storeworld.pojo.dto;

public class CustomerInfo implements java.io.Serializable{

	private String id;
	private String customer_area;
	private String customer_name;
	private String telephone;
	private String customer_addr;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	
	public CustomerInfo(String customer_area,String customer_name,String telephone,
			String customer_addr,String reserve1,String reserve2,String reserve3){
		this.customer_area=customer_area;
		this.customer_name=customer_name;
		this.telephone=telephone;
		this.customer_addr=customer_addr;
		this.reserve1=reserve1;
		this.reserve2=reserve2;
		this.reserve3=reserve3;

	}

	public CustomerInfo(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomer_area() {
		return customer_area;
	}

	public void setCustomer_area(String customer_area) {
		this.customer_area = customer_area;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCustomer_addr() {
		return customer_addr;
	}

	public void setCustomer_addr(String customer_addr) {
		this.customer_addr = customer_addr;
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
