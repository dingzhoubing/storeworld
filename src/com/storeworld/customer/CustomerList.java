package com.storeworld.customer;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;

import com.mysql.jdbc.Connection;
import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.database.BaseAction;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.product.Product;
import com.storeworld.product.ProductCellModifier;
import com.storeworld.product.ProductUtils;
import com.storeworld.pub.service.CustomerInfoService;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.pub.service.StockInfoService;
import com.storeworld.stock.StockCellModifier;
import com.storeworld.stock.StockContentPart;
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
	private static final DeliverInfoService deliverinfo = new DeliverInfoService();
	private static final BaseAction baseAction = new BaseAction();
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
			newID = String.valueOf(cusinfo.getNextCustomerID());
		} catch (Exception e) {
			MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK);						
	    	messageBox.setMessage("初始化数据库失败，请重新启动"); 	
	    	if (messageBox.open() == SWT.OK){	    			    	
	    		MainUI.getMainUI_Instance(Display.getDefault()).dispose();
	    		System.exit(0);
	    		return;
	    	}
		}
				
		//no record
		if(newID.equals("-1") || newID.equals(""))//is this right?
			newID="1";//empty
		//by the list of Customer from database
		CustomerUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)));
		
		try {
			DataCachePool.cacheCustomerInfo2(customerList);
		} catch (Exception e) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("缓存客户信息失败");
			mbox.open();
		}		
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
	 * @throws Exception 
	 */
	public void removeCustomer(Customer customer) throws Exception {
		
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}
		

		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).remove(customer);
			//delete the customer
			try {
				conn.setAutoCommit(false);
				cusinfo.deleteCustomerInfo(conn, customer.getID());
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}finally{
				conn.close();
			}

			//remove the info from DataCachePool
			DataCachePool.removeCustomerInfoOfCache(customer.getArea(), customer.getName());	
			//refresh the table filter
			CustomerUtils.refreshAreas_FirstName();
			customerList.remove(customer);
		}
	}
	
	public void customerChangedThree(Customer customer) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(customer);	
			
			for(int i=0;i<customerList.size();i++){
				Customer c = (Customer)(customerList.get(i));
				if(c.getID().equals(customer.getID())){
					c.setArea(customer.getArea());
					c.setName(customer.getName());
					c.setPhone(customer.getPhone());
					c.setAddress(customer.getAddress());					
					break;
				}				
			}
			
			CustomerUtils.refreshTableData();
			
		}
	}
	
	public void customerChangedTwo(Customer customer) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(customer);
			Customer c = (Customer)(customerList.get(customerList.size()-1));			
			c.setArea(customer.getArea());
			c.setName(customer.getName());
			c.setPhone(customer.getPhone());
			c.setAddress(customer.getAddress());
			
			CustomerCellModifier.addNewTableRow(customer);
			DataCachePool.addArea2Names(customer.getArea(), customer.getName());
			CustomerUtils.refreshAreas_FirstName();			
			CustomerUtils.refreshTableData();
			
		}
	}
	
	
	private boolean checkSameCustomer(Customer customer){
		boolean ret = false;
		for(int i=0;i<customerList.size()-1;i++){
			Customer stmp = (Customer)customerList.get(i);
			//a different stock
			if(!stmp.getID().equals(customer.getID())){
				if(stmp.getArea().equals(customer.getArea()) && stmp.getName().equals(customer.getName())){
					ret = true;
					break;
				}				
			}			
		}		
		return ret;
//		return DataCachePool.ifExistAreaName(customer.getArea(), customer.getName());
	}
	
	public static void relatedCustomerChange(ArrayList<Customer> custsomers_changed){

		String current = CustomerUtils.getNewLineID();
		int dup = 0;
		for(Customer p : custsomers_changed){//update product table UI
			if(p.getID().equals(current)){
				int tid = Integer.valueOf(p.getID());
				int nid = tid + dup;//new id
				p.setID(String.valueOf(nid));
				//already do the cache
				CustomerCellModifier.getCustomerList().customerChangedTwo(p);
				dup++;									
			}else{
				CustomerCellModifier.getCustomerList().customerChangedThree(p);
			}								
		}	
//		return info;
	}
	
	/**
	 * update the info of a customer
	 * @param customer
	 * @throws Exception 
	 */
	public void customerChanged(final Customer customer) throws Exception {
		
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}
		
		// no matter valid or not, we should update the table
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()) {
			(iterator.next()).update(customer);
			//not the new row, we update, or we do not update, just update he table
			final Map<String, Object> cus = new HashMap<String ,Object>();
			cus.put("id", customer.getID());
			cus.put("customer_area", customer.getArea());
			cus.put("customer_name", customer.getName());
			cus.put("telephone", customer.getPhone());
			cus.put("customer_addr", customer.getAddress());
			
			try {
				conn.setAutoCommit(false);
				if(!CustomerValidator.checkID(customer.getID()) && CustomerValidator.rowLegal(customer)){
				//update the database here				
				
					if(checkSameCustomer(customer)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的客户在客户表中，请重新填写！"));		    		    	
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Customer c = new Customer();
	    		    		c.setID(customer.getID());
	    		    		c.setArea("");
	    		    		c.setName("");
	    		    		c.setPhone(customer.getPhone());
	    		    		c.setAddress(customer.getAddress());	    					
	    					CustomerCellModifier.getCustomerList().customerChangedThree(c);
	    		    	}						
					}else{
						
						//before update the database, record the old area/name, for updating the cache
						Map<String, Object> cus_old = new HashMap<String, Object>();
						cus_old.put("id", customer.getID());
						ReturnObject ret = cusinfo.queryCustomerInfo(conn, cus_old);
						Pagination page = (Pagination) ret.getReturnDTO();
						List<Object> list = page.getItems();
						String old_area="";
						String old_name="";
						String old_tele = "";
						String old_addr = "";
						final Map<String, Object> cus_rec = new HashMap<String, Object>();
						//it should contains only one element, or something wrong
						if(!list.isEmpty()){
							CustomerInfoDTO cDTO = (CustomerInfoDTO) list.get(0);
							old_area = cDTO.getCustomer_area();
							old_name = cDTO.getCustomer_name();
							old_tele = cDTO.getTelephone();
							old_addr = cDTO.getCustomer_addr();
							cus_rec.put("customer_area", old_area);
							cus_rec.put("customer_name", old_name);
							cus_rec.put("telephone", old_tele);
							cus_rec.put("customer_addr", old_addr);
						}else{
							System.out.println("query a customer with an exist ID returns empty");
						}
						
						if(customer.getArea().equals(old_area) && customer.getName().equals(old_name) 
								&& customer.getPhone().equals(old_tele) && customer.getAddress().equals(old_addr)){
							//do nothing?
							//not the same as product, product has the property quantity
						}else{
							final String old_area_final = old_area;
							final String old_name_final = old_name;
							
							boolean contain = deliverinfo.queryCommonInfo(conn, old_area, old_name);
							if(!contain){//means no such deliver related to this customer, we can just change it
								//just update it
								cusinfo.updateCustomerInfo(conn, customer.getID(), cus);
								conn.commit();
								
								//old area, old name, new area, new name
		    					DataCachePool.updateCustomerInfoOfCache(old_area_final, old_name_final, customer.getArea(), customer.getName());	
		    					CustomerUtils.refreshAreas_FirstName();
							}else{
							
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);						
		    		    	messageBox.setMessage(String.format("片区:%s，客户:%s 的信息将被修改，送货表中将会发生相应更新，确认要操作吗？", old_area, old_name));		    		    	
		    		    	if (messageBox.open() == SWT.OK){	
		    		    		
		    		    		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
		    		    		IRunnableWithProgress runnable = new IRunnableWithProgress() {  
		    		    		    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {  
		    		    		    	
		    		    		    Display.getDefault().asyncExec(new Runnable() {  
		    		    		                public void run() {  
		    		    		        monitor.beginTask("正在进行更新，请勿关闭系统...", 100);  
		    		    		        
										Connection connin; 
										try {
											connin = baseAction.getConnection();
										} catch (Exception e1) {
											MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
											mbox.setMessage("连接数据库失败");
											mbox.open();
											return;
										}
										
		    		    		        try {
		    		    		        	connin.setAutoCommit(false);
											cusinfo.updateCustomerInfo(connin, customer.getID(), cus);
				 
				    					monitor.worked(20);  
	    		    		            monitor.subTask("更新客戶表");
	    		    		            
				    					//update all deliver info and current deliver table
										DeliverInfoService deliverinfo = new DeliverInfoService();
										deliverinfo.updateAllDeliverInfoForCustomerChanged(connin, cus, cus_rec);
										
										monitor.worked(60);  
	    		    		            monitor.subTask("更新送货表");
										
										DeliverContentPart.reNewDeliver();
										DeliverContentPart.reNewDeliverHistory();
										
				    					//old area, old name, new area, new name
				    					DataCachePool.updateCustomerInfoOfCache(old_area_final, old_name_final, customer.getArea(), customer.getName());	
				    					CustomerUtils.refreshAreas_FirstName();
				    						
										monitor.worked(100);  
	    		    		            monitor.subTask("更新送货界面");
	    		    		            
										connin.commit();										
										
										} catch (Exception e) {
											try {
												connin.rollback();
											} catch (SQLException e1) {
												MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
												mbox.setMessage("连接数据库异常");
												mbox.open();
											}
										}finally{
											try {
												connin.close();
											} catch (SQLException e) {
												MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
												mbox.setMessage("连接数据库异常");
												mbox.open();
											}
										}

	    		    		            
		    		    		        monitor.done();
		    		    		                }
		    		    		    	  });
		    		    		    }  
		    		    		};  
		    		    		  
		    		    		try {  
		    		    		    progressDialog.run(true,/*是否开辟另外一个线程*/  
		    		    		    false,/*是否可执行取消操作的线程*/  
		    		    		    runnable/*线程所执行的具体代码*/  
		    		    		    );  
		    		    		} catch (InvocationTargetException e) {  
		    		    			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
									mbox.setMessage("未知异常");
									mbox.open();
									return;
		    		    		} catch (InterruptedException e) {  
		    		    			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
									mbox.setMessage("未知异常");
									mbox.open();
									return;
		    		    		}  

		    		    	}else{
		    		    		Customer c = new Customer();
		    					c.setID(customer.getID());//new row in fact
		    					c.setArea(old_area);
		    					c.setName(old_name);
		    					c.setPhone(old_tele);
		    					c.setAddress(old_addr);
		    					CustomerCellModifier.getCustomerList().customerChangedThree(c);
		    		    	}
							}
						}
					}					
				
			}
			if(CustomerValidator.checkID(customer.getID()) && CustomerValidator.rowLegal(customer)){				
					if(checkSameCustomer(customer)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的客户在客户表中，请重新填写！"));		    		    	
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Customer c = new Customer();
	    		    		c.setID(customer.getID());
	    		    		c.setArea("");
	    		    		c.setName("");
	    		    		c.setPhone(customer.getPhone());
	    		    		c.setAddress(customer.getAddress());	    					
	    					CustomerCellModifier.getCustomerList().customerChangedThree(c);
	    		    	}						
					}else{
						cusinfo.addCustomerInfo(conn, cus);
						conn.commit();
						
						CustomerCellModifier.addNewTableRow(customer);
						//add the new info to the cache
						DataCachePool.addArea2Names(customer.getArea(), customer.getName());						
						CustomerUtils.refreshAreas_FirstName();
					}

			}
			} catch (Exception e) {
				System.out.println("add customer failed");
				conn.rollback();
				throw e;
			}finally{
				conn.close();
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
