package com.storeworld.pojo.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.storeworld.framework.ObjectExternalizable;

public class DeliverCommonInfoDTO extends ObjectExternalizable{
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
	
	/**
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.id = readUTF(in);
		this.customer_area = readUTF(in);
		this.customer_name = readUTF(in);
		this.deliver_addr = readUTF(in);
		this.total_price = readFloat(in);
		this.real_price = readFloat(in);
		this.deliver_time = readUTF(in);
		this.is_print = readUTF(in);
		this.telephone = readUTF(in);
		this.reserve1 = readUTF(in);
		this.reserve2 = readUTF(in);
		this.reserve3 = readUTF(in);
	}

	/**
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		writeUTF(out, this.id);
		writeUTF(out, this.customer_area);
		writeUTF(out, this.customer_name);
		writeUTF(out, this.deliver_addr);
		writeFloat(out, this.total_price);
		writeFloat(out, this.real_price);
		writeUTF(out, this.deliver_time);
		writeUTF(out, this.is_print);
		writeUTF(out, this.telephone);
		writeUTF(out, this.reserve1);
		writeUTF(out, this.reserve2);
		writeUTF(out, this.reserve3);
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
