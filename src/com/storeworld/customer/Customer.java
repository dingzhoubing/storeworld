package com.storeworld.customer;

import com.storeworld.common.DataInTable;

/**
 * define the class Customer
 * @author dingyuanxiong
 *
 */

public class Customer extends DataInTable{
	private String id;//the identifier of the record
	private String name;
	private String area;
	private String phone;
	private String address;
	
	public Customer(){
		
	}
	public Customer(String name,String area, String phone, String address){
		this(null, name, area,  phone, address);
	}
	public Customer(String ID, String name,String area, String phone, String address){
		if(ID != null)		
			this.id = ID;
		this.name = name;
		this.area = area;
		this.phone = phone;
		this.address = address;		
	}
	/**
	 * each class should have an unique id
	 * @return
	 */
	public String getID(){
		return this.id;
	}
	public void setID(String ID){
		this.id = ID;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArea() {
		return this.area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.id+" ");
		sb.append(this.name+" ");
		sb.append(this.area+" ");
		sb.append(this.phone+" ");
		sb.append(this.address+" ");
		return sb.toString();
	}
	
}