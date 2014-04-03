package com.storeworld.pojo.dto;

public class DeliverCommonInfo implements java.io.Serializable{
	private String id;
	private String customer_area;
	private String customer_name;
	private String deliver_addr;
	private Float total_price;
	private Float real_price;
	private String deliver_time;
	private String is_print;
	private String telephone;
	private String reserve1;
	private String reserve2;
	private String reserve3;
	
	public DeliverCommonInfo(String id,String customer_area,String customer_name,String deliver_addr,
			Float total_price,Float real_price,String deliver_time,
			String is_print,String telephone,String reserve1,String reserve2,String reserve3){
		this.customer_area=customer_area;
		this.customer_name=customer_name;
		this.deliver_addr=deliver_addr;
		this.total_price=total_price;
		this.real_price=real_price;
		this.deliver_time=deliver_time;
		this.is_print=is_print;
		this.telephone=telephone;
		this.reserve1=reserve1;
		this.reserve2=reserve2;
		this.reserve3=reserve3;

	}
	
	public DeliverCommonInfo(){
		
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

	public String getDeliver_addr() {
		return deliver_addr;
	}

	public void setDeliver_addr(String deliver_addr) {
		this.deliver_addr = deliver_addr;
	}

	public Float getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Float total_price) {
		this.total_price = total_price;
	}

	public Float getReal_price() {
		return real_price;
	}

	public void setReal_price(Float real_price) {
		this.real_price = real_price;
	}

	public String getDeliver_time() {
		return deliver_time;
	}

	public void setDeliver_time(String deliver_time) {
		this.deliver_time = deliver_time;
	}

	public String getIs_print() {
		return is_print;
	}

	public void setIs_print(String is_print) {
		this.is_print = is_print;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
