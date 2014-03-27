package com.storeworld.pojo.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.storeworld.framework.ObjectExternalizable;

public class CustomerInfoDTO extends ObjectExternalizable{
	private String id;
	private String customer_area;
	private String customer_name;
	private String telephone;
	private String customer_addr;
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
		this.customer_area = readUTF(in);
		this.customer_name = readUTF(in);
		this.telephone = readUTF(in);
		this.customer_addr = readUTF(in);
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

		writeUTF(out, this.customer_area);
		writeUTF(out, this.customer_name);
		writeUTF(out, this.telephone);
		writeUTF(out, this.customer_addr);
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
