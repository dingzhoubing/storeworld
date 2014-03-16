package com.storeworld.customer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class CustomerList {
	
	private ArrayList<Customer> customerList = new ArrayList<Customer>();
	//hash set, so make it only has one of one kind
	private Set<ICustomerListViewer> changeListeners = new HashSet<ICustomerListViewer>();

	
	public CustomerList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
		String name = "老刘";
		String area = "八里街";
		String phone = "13991945478";
		String address = "74号";
		Customer cus = new Customer("1",name, area, phone, address);
		customerList.add(cus);
		
		String name2 = "小胡";
		String area2 = "八里街";
		String phone2 = "13761945478";
		String address2 = "201号";
		Customer cus2 = new Customer("2",name2, area2, phone2, address2);
		customerList.add(cus2);
		
		String name3 = "张三丰";
		String area3 = "三叉";
		String phone3 = "13501945478";
		String address3 = "";
		Customer cus3 = new Customer("3",name3, area3, phone3, address3);
		customerList.add(cus3);
		
		String name4 = "胡景涛";
		String area4 = "安陆";
		String phone4 = "1323445478";
		String address4 = "宝光路14号";
		Customer cus4 = new Customer("4",name4, area4, phone4, address4);
		customerList.add(cus4);
		
	}
	
	public ArrayList<Customer> getCustomers() {
		return this.customerList;
	}
	
	/**
	 * add a product
	 */
	public void addCustomer() {
		Customer customer = new Customer();
		this.customerList.add(customer);
		Iterator<ICustomerListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).addCustomer(customer);
	}

	/**
	 * @param remove a product
	 */
	public void removeCustomer(Customer customer) {
		this.customerList.remove(customer);
		Iterator<ICustomerListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).removeCustomer(customer);
	}

	/**
	 * @param update a product
	 */
	public void customerChanged(Customer customer) {
		Iterator<ICustomerListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).updateCustomer(customer);
	}

	/**
	 * @param may multi contentprovider?， one remove
	 */
	public void removeChangeListener(ICustomerListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param may multi contentprovider? one add
	 * viewer is a content provider
	 */
	public void addChangeListener(ICustomerListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.customerList.toString();
	}
	
}
