package com.storeworld.pojo.dto;

public class DeliverInfoAll implements java.io.Serializable{

	private String common_id;
	private String uni_id;
	private String customer_area;
	private String customer_name;
	private String deliver_addr;
	private String order_num;
	private String brand;
	private String sub_brand;
	private Float unit_price;
	private String unit;
	private String quantity;
	private Float total_price;
	private Float real_price;
	private String deliver_time;
	private String standard;
	private String stock_from;
	private String is_print;
	private String telephone;
	private String common_reserve1;
	private String common_reserve2;
	private String common_reserve3;
	private String uni_reserve1;
	private String uni_reserve2;
	private String uni_reserve3;

	public DeliverInfoAll(String customer_area,String customer_name,String deliver_addr,
			String order_num,String brand,String sub_brand,Float unit_price,
			String unit,String quantity,Float total_price,Float real_price,String deliver_time,
			String standard,String stock_from,String is_print,String telephone,
			String uni_reserve1,String uni_reserve2,String uni_reserve3,String common_reserve1,String common_reserve2,String common_reserve3){
		this.customer_area=customer_area;
		this.customer_name=customer_name;
		this.deliver_addr=deliver_addr;
		this.order_num=order_num;
		this.brand=brand;
		this.sub_brand=sub_brand;
		this.unit_price=unit_price;
		this.unit=unit;
		this.quantity=quantity;
		this.total_price=total_price;
		this.real_price=real_price;
		this.deliver_time=deliver_time;
		this.standard=standard;
		this.stock_from=stock_from;
		this.is_print=is_print;
		this.telephone=telephone;
		this.uni_reserve1=uni_reserve1;
		this.uni_reserve2=uni_reserve2;
		this.uni_reserve3=uni_reserve3;
		this.common_reserve1=common_reserve1;
		this.common_reserve2=common_reserve2;
		this.common_reserve3=common_reserve3;

	}

	public DeliverInfoAll(){

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

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
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

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getStock_from() {
		return stock_from;
	}

	public void setStock_from(String stock_from) {
		this.stock_from = stock_from;
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

	public String getCommon_id() {
		return common_id;
	}

	public void setCommon_id(String common_id) {
		this.common_id = common_id;
	}

	public String getUni_id() {
		return uni_id;
	}

	public void setUni_id(String uni_id) {
		this.uni_id = uni_id;
	}

	public String getCommon_reserve1() {
		return common_reserve1;
	}

	public void setCommon_reserve1(String common_reserve1) {
		this.common_reserve1 = common_reserve1;
	}

	public String getCommon_reserve2() {
		return common_reserve2;
	}

	public void setCommon_reserve2(String common_reserve2) {
		this.common_reserve2 = common_reserve2;
	}

	public String getCommon_reserve3() {
		return common_reserve3;
	}

	public void setCommon_reserve3(String common_reserve3) {
		this.common_reserve3 = common_reserve3;
	}

	public String getUni_reserve1() {
		return uni_reserve1;
	}

	public void setUni_reserve1(String uni_reserve1) {
		this.uni_reserve1 = uni_reserve1;
	}

	public String getUni_reserve2() {
		return uni_reserve2;
	}

	public void setUni_reserve2(String uni_reserve2) {
		this.uni_reserve2 = uni_reserve2;
	}

	public String getUni_reserve3() {
		return uni_reserve3;
	}

	public void setUni_reserve3(String uni_reserve3) {
		this.uni_reserve3 = uni_reserve3;
	}
	



}