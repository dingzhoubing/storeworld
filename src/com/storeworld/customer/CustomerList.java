package com.storeworld.customer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.utils.Utils;

/**
 * the class to get all the customer data in table
 * @author dingyuanxiong
 *
 */
public class CustomerList{
	
	private ArrayList<DataInTable> customerList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
//	private Set<ICustomerListViewer> changeListeners = new HashSet<ICustomerListViewer>();
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();
	
	
	
	public CustomerList() {
//		super();
//		this.initial();
	}
	public CustomerList(Table table){
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
		
		//by the list of Customer from database
		CustomerUtils.setNewLineID("5");
		
	}
	
	public ArrayList<DataInTable> getCustomers() {

//		CustomerFilter.initialFilterOut(customerList);
		return this.customerList;
	}
	
	
	
	/**
	 * add a product
	 */
	public void addCustomer(Customer customer) {
//		Customer customer = new Customer();
		this.customerList.add(customer);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).add(customer);
	}

	/**
	 * @param remove a product
	 */
	public void removeCustomer(Customer customer) {
		this.customerList.remove(customer);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).remove(customer);
	}

	/**
	 * @param update a product
	 */
	public void customerChanged(Customer customer) {
		// no matter valid or not, we should update the table
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()) {
			(iterator.next()).update(customer);
		}
		//update the database here
	}

	/**
	 * @param may multi contentprovider?， one remove
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param may multi contentprovider? one add
	 * viewer is a content provider
	 */
	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.customerList.toString();
	}
	
}
