package com.storeworld.pojo.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.storeworld.framework.ObjectExternalizable;

public class GoodsBatchInfoDTO extends ObjectExternalizable{
	private String brand;
	private String sub_brand;
	private Float unit_price;
	private String quantity;
	private String standard;
	private String batchNo;
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
		this.brand = readUTF(in);
		this.sub_brand = readUTF(in);
		this.unit_price = readFloat(in);
		this.batchNo = readUTF(in);
		this.quantity = readUTF(in);
		this.standard = readUTF(in);
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
		writeUTF(out, this.brand);
		writeUTF(out, this.sub_brand);
		writeFloat(out, this.unit_price);
		writeUTF(out, this.batchNo);
		writeUTF(out, this.quantity);
		writeUTF(out, this.standard);
		writeUTF(out, this.reserve1);
		writeUTF(out, this.reserve2);
		writeUTF(out, this.reserve3);
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
