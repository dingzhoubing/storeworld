package com.storeworld.customer;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.mainui.MainUI;
import com.storeworld.stock.StockCellModifier;
import com.storeworld.utils.Utils;

/**
 * modify the data of customer table
 * @author dingyuanxiong
 *
 */
public class CustomerCellModifier implements ICellModifier {
	private static TableViewer tv;//just in case
	private static CustomerList customerlist;
	private static CustomerCellModifier smodifier = null;
	
	public CustomerCellModifier(TableViewer tv_tmp, CustomerList customerlist_tmp) {
		tv = tv_tmp;		
		customerlist = customerlist_tmp;
	}

	private static CustomerCellModifier getInstance(){
		if(smodifier == null){
			smodifier = new CustomerCellModifier(tv, customerlist);
			return smodifier;
		}else{
			//nothing
			return smodifier;
		}
	}
	
	public boolean canModify(Object element, String property) {
		return true;
	}

	public static CustomerList getCustomerList(){
		return customerlist;
	}
	//when initial the table data
	public Object getValue(Object element, String property) {
		Customer c = (Customer) element;
		if(property.equals("id")){
			return String.valueOf(c.getID());
		}else if (property.equals("name")) {
			if(c.getName() != null)
				return String.valueOf(c.getName());
			else
				return String.valueOf("");
		} else if (property.equals("area")) {
			if(c.getArea() != null)
				return String.valueOf(c.getArea());
			else
				return String.valueOf("");
		} else if (property.equals("phone")) {
			if(c.getPhone() != null)
				return String.valueOf(c.getPhone());
			else
				return String.valueOf("");
		}else if (property.equals("address")) {
			if(c.getAddress() != null)
				return String.valueOf(c.getAddress());
			else
				return String.valueOf("");
		}else if(property.equals("operation")){
//			return String.valueOf("1");
			return null;// show the operation button
		}else if(property.equals("deliver")){
			return null;
		}
		return null;
//		throw new RuntimeException("error column name : " + property);
	}

	/**
	 * add a new row in table with newest customer id
	 * @param customer
	 */
	public static void addNewTableRow(Customer customer){
//		if (CustomerValidator.checkID(c.getID()) && CustomerValidator.rowLegal(c)) {
			int new_id = Integer.valueOf(customer.getID()) + 1;
//			CustomerValidator.setNewID(String.valueOf(new_id));
			CustomerUtils.setNewLineID(String.valueOf(new_id));
			Customer cus_new = new Customer(String.valueOf(new_id));
			customerlist.addCustomer(cus_new);
			Utils.refreshTable(tv.getTable());
//		}
	}
	
	public static void staticModify(Object element, String property, Object value){
		getInstance().modify(element, property, value);
	}

	//when modify the table
	public void modify(Object element, String property, Object value) {
//		tip.setVisible(false);
		
		TableItem item = (TableItem) element;
		Customer c = (Customer) item.getData();
		String namelast = "";
		String arealast = "";
		String phonelast = "";
		String addresslast = "";
		int col = 0;//no use now
		boolean hasBeenChanged = false;
		if (property.equals("name")) {
			namelast = c.getName();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			col = 1;
			if (namelast != null) {
				if (!namelast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			c.setName(newValue);
		} else if (property.equals("area")) {
			arealast = c.getArea();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			col = 2;
			if (arealast != null) {
				if (!arealast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			c.setArea(newValue);
		} else if (property.equals("phone")) {
			phonelast = c.getPhone();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			col = 3;
			if (phonelast != null) {
				if (!phonelast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			c.setPhone(newValue);
		} else if (property.equals("address")) {
			addresslast = c.getAddress();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			col = 4;
			if (addresslast != null) {
				if (!addresslast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			c.setAddress(newValue);
		} else {
			return;// just return, do nothing
			// throw new RuntimeException("错误列名:" + property);
		}
		
		//validate the new value, update the table & database, only if the values are valid
		boolean valid = false;
		if (hasBeenChanged) {

			if (property.equals("name")) {
				valid = CustomerValidator.validateName(c.getName());//tv.getTable(), item, 1, 
				if (!valid) {
					c.setName(namelast);
				}

			} else if (property.equals("area")) {
				valid = CustomerValidator.validateArea(c.getArea());//tv.getTable(), item, 2, 
				if (!valid) {
					c.setArea(arealast);
				}

			} else if (property.equals("phone")) {
				valid = CustomerValidator.validatePhone(c.getPhone());//tv.getTable(), item, 3, 
				if (!valid) {
					c.setPhone(phonelast);
				}
			} else if (property.equals("address")) {
				valid = CustomerValidator.validateAddress(c.getAddress());//tv.getTable(), item, 4, 
				if (!valid) {
					c.setAddress(addresslast);
				}
			}
			if (valid) {				
				try {
					customerlist.customerChanged(c);
				} catch (Exception e) {
					if (property.equals("name")) {					
						c.setName(namelast);						
					} else if (property.equals("area")) {						
						c.setArea(arealast);						
					} else if (property.equals("phone")) {						
						c.setPhone(phonelast);						
					} else if (property.equals("address")) {						
						c.setAddress(addresslast);						
					}
					MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
					mbox.setMessage("更新客户表失败，请重试");
					mbox.open();
//					return;
				}
				
			}
		}
	}

}