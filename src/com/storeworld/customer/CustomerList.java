package com.storeworld.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Table;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.CustomerInfoService;
import com.storeworld.stock.StockCellModifier;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.Utils;

/**
 * the class to get all the customer data in table
 * @author dingyuanxiong
 *
 */
public class CustomerList{
	
	private static ArrayList<DataInTable> customerList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();
	private static final CustomerInfoService cusinfo = new CustomerInfoService();
	
	
	public CustomerList() {
		
	}
	public CustomerList(Table table){
		super();		
		initial();
		
	}

	/**
	 * initial the data of the table from database
	 * when system start up, do this once
	 */
	public static void initial(){		
		
		String newID = "";
		try {
			ReturnObject ret = cusinfo.queryCustomerInfoAll();
			newID = String.valueOf(cusinfo.getNextCustomerID());
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			for(int i=0;i<list.size();i++){
				CustomerInfoDTO cDTO = (CustomerInfoDTO) list.get(i);
				Customer cus = new Customer();
//				newID = cDTO.getId();
				cus.setID(cDTO.getId());
				cus.setName(cDTO.getCustomer_name());
				cus.setArea(cDTO.getCustomer_area());
				cus.setPhone(cDTO.getTelephone());
				cus.setAddress(cDTO.getCustomer_addr());
				//cache the data
				DataCachePool.addArea2Names(cDTO.getCustomer_area(), cDTO.getCustomer_name());
				customerList.add(cus);
			}
		} catch (Exception e) {
			System.out.println("failed");
		}
		//-1 means no record now, get null by show table status where Name="?"
		//no record
		if(newID.equals("-1"))
			newID="1";//empty
		//by the list of Customer from database
		CustomerUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)));//no need to +1
		
	}
	
	/**
	 * get the customers set from database
	 * @return
	 */
	public static ArrayList<DataInTable> getCustomers() {
		return customerList;
	}
	
	/**
	 * 1. add a customer in the table, and refresh the table filter
	 * 2. do not do anything in database
	 * @param customer
	 */
	public void addCustomer(Customer customer) {
//		Customer customer = new Customer();
		customerList.add(customer);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).add(customer);
//			CustomerUtils.refreshAreas_FirstName();			
		}
	}

	/**
	 * 1. delete a customer from database, refresh the table filter
	 * 2. update he DataCachePool
	 * @param customer
	 */
	public void removeCustomer(Customer customer) {
		customerList.remove(customer);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).remove(customer);
			//delete the customer
			try {
				cusinfo.deleteCustomerInfo(customer.getID());
			} catch (Exception e) {
				System.out.println("delete customer failed");
			}

			//remove the info from DataCachePool
			DataCachePool.removeCustomerInfoOfCache(customer.getArea(), customer.getName());	
			//refresh the table filter
			CustomerUtils.refreshAreas_FirstName();
		}
	}
	
	/**
	 * update the info of a customer
	 * @param customer
	 */
	public void customerChanged(Customer customer) {
		// no matter valid or not, we should update the table
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()) {
			(iterator.next()).update(customer);
			//not the new row, we update, or we do not update, just update he table
			Map<String, Object> cus = new HashMap<String ,Object>();
			cus.put("id", customer.getID());
			cus.put("customer_area", customer.getArea());
			cus.put("customer_name", customer.getName());
			cus.put("telephone", customer.getPhone());
			cus.put("customer_addr", customer.getAddress());
			if(!CustomerValidator.checkID(customer.getID())){
				//update the database here				
				try {
					//before update the database, record the old area/name, for updating the cache
					Map<String, Object> cus_old = new HashMap<String, Object>();
					cus_old.put("id", customer.getID());
					ReturnObject ret = cusinfo.queryCustomerInfo(cus_old);
					Pagination page = (Pagination) ret.getReturnDTO();
					List<Object> list = page.getItems();
					String old_area="";
					String old_name="";
					//it should contains only one element, or something wrong
					if(!list.isEmpty()){
						CustomerInfoDTO cDTO = (CustomerInfoDTO) list.get(0);
						old_area = cDTO.getCustomer_area();
						old_name = cDTO.getCustomer_name();
					}else{
						System.out.println("query a customer with an exist ID returns empty");
					}
					
					cusinfo.updateCustomerInfo(customer.getID(), cus);

					//old area, old name, new area, new name
					DataCachePool.updateCustomerInfoOfCache(old_area, old_name, customer.getArea(), customer.getName());	
					//need??
					CustomerUtils.refreshAreas_FirstName();
				} catch (Exception e) {
					System.out.println("update customer failed");
				}
				
			}
			if(CustomerValidator.checkID(customer.getID()) && CustomerValidator.rowLegal(customer)){				
				try {
					cusinfo.addCustomerInfo(cus);
					CustomerCellModifier.addNewTableRow(customer);
					//add the new info to the cache
					DataCachePool.addArea2Names(customer.getArea(), customer.getName());
					
					CustomerUtils.refreshAreas_FirstName();
				} catch (Exception e) {
					System.out.println("add customer failed");
				}
			}
			
		}
		
	}

	/**
	 * may multi content provider
	 * @param viewer
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}
	
	/**
	 * @param viewer
	 */
	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return customerList.toString();
	}
	
}
