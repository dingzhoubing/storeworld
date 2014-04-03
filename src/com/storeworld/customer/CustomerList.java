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
import com.storeworld.utils.Utils;

/**
 * the class to get all the customer data in table
 * @author dingyuanxiong
 *
 */
public class CustomerList{
	
	private static ArrayList<DataInTable> customerList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
//	private Set<ICustomerListViewer> changeListeners = new HashSet<ICustomerListViewer>();
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();
	private static final CustomerInfoService cusinfo = new CustomerInfoService();
	
	
	public CustomerList() {
//		super();
//		this.initial();
	}
	public CustomerList(Table table){
		super();
		
		this.initial();
		
	}
	public static ArrayList<DataInTable> getCustomerList(){
		return customerList;
	}
	
	//initial data, later, in database
	public void initial(){		
//		String name = "";
//		String area = ";
//		String phone = "13991945478";
//		String address = ";
//		Customer cus = new Customer("1",name, area, phone, address);
//		customerList.add(cus);
//		
//		String name2 = ;
//		String area2 = ";
//		String phone2 = "13761945478";
//		String address2 = "";
//		Customer cus2 = new Customer("2",name2, area2, phone2, address2);
//		customerList.add(cus2);
//		
//		String name3 = "
//		String area3 = ";
//		String phone3 = "13501945478";
//		String address3 = "";
//		Customer cus3 = new Customer("3",name3, area3, phone3, address3);
//		customerList.add(cus3);
//		
//		String name4 = ";
//		String area4 = "
//		String phone4 = "1323445478";
//		String address4 = 
//		Customer cus4 = new Customer("4",name4, area4, phone4, address4);
//		customerList.add(cus4);
		
		
//		Map<String, Object> cus = new HashMap<String ,Object>();
//		cus.put("customer_area", "八里街");
//		cus.put("customer_name", "老刘");
//		cus.put("telephone", "13991945478");
//		cus.put("customer_addr", "74号");
//		
//		Map<String, Object> cus2 = new HashMap<String ,Object>();
//		cus2.put("customer_area", "八里街");
//		cus2.put("customer_name", "老李");
//		cus2.put("telephone", "13992145478");
//		cus2.put("customer_addr", "78号");
//		
//		Map<String, Object> cus3 = new HashMap<String ,Object>();
//		cus3.put("customer_area", "安陆");
//		cus3.put("customer_name", "胡海");
//		cus3.put("telephone", "1352345478");
//		cus3.put("customer_addr", "小街");
//		
//		try {
//			cusinfo.addCustomerInfo(cus2);
//			cusinfo.addCustomerInfo(cus3);
//		} catch (Exception e1) {
//			System.out.println("failed");
//		}

		/**
		 * initial the data of the table
		 */
		String newID = "";
		try {
			ReturnObject ret = cusinfo.queryCustomerInfoAll();
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			for(int i=0;i<list.size();i++){
				CustomerInfoDTO cDTO = (CustomerInfoDTO) list.get(i);
				Customer cus = new Customer();
				newID = cDTO.getId();
				cus.setID(cDTO.getId());
				cus.setName(cDTO.getCustomer_name());
				cus.setArea(cDTO.getCustomer_area());
				cus.setPhone(cDTO.getTelephone());
				cus.setAddress(cDTO.getCustomer_addr());
				customerList.add(cus);
//				System.out.println("name: "+cDTO.getCustomer_name());
			}
		} catch (Exception e) {
			System.out.println("failed");
		}
		//no record
		if(newID.equals(""))
			newID="0";//empty
		//by the list of Customer from database
		CustomerUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)+1));
		
	}
	
	public ArrayList<DataInTable> getCustomers() {

//		CustomerFilter.initialFilterOut(customerList);
//		System.out.println("here");
		return this.customerList;
	}
	
	
	
	/**
	 * add a product
	 */
	public void addCustomer(Customer customer) {
//		Customer customer = new Customer();
		customerList.add(customer);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).add(customer);
			CustomerUtils.refreshAreas_FirstName();
		}
	}

	/**
	 * @param remove a product
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
			CustomerUtils.refreshAreas_FirstName();
		}
	}

	/**
	 * @param update a product
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
					cusinfo.updateCustomerInfo(customer.getID(), cus);
				} catch (Exception e) {
					System.out.println("update customer failed");
				}
			}
			if(CustomerValidator.checkID(customer.getID()) && CustomerValidator.rowLegal(customer)){				
				try {
					cusinfo.addCustomerInfo(cus);
					CustomerCellModifier.addNewTableRow(customer);
				} catch (Exception e) {
					System.out.println("add customer failed");
				}
			}
			//need?
			CustomerUtils.refreshAreas_FirstName();
		}
		
	}

	/**
	 * @param may multi contentprovider?
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
		return customerList.toString();
	}
	
}
