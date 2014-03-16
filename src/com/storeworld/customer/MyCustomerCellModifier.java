package com.storeworld.customer;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;


public class MyCustomerCellModifier implements ICellModifier {
	private TableViewer tv;//just in case
	private CustomerList customerlist;

	public MyCustomerCellModifier(TableViewer tv, CustomerList customerlist) {
		this.tv = tv;
		this.customerlist = customerlist;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	//when initial the table data
	public Object getValue(Object element, String property) {
		Customer c = (Customer) element;
		if(property.equals("id")){
			return String.valueOf(c.getID());
		}else if (property.equals("name")) {
			return String.valueOf(c.getName());
		} else if (property.equals("area")) {
			return String.valueOf(c.getArea());
		} else if (property.equals("phone")) {
			return String.valueOf(c.getPhone());
		}else if (property.equals("address")) {
			return String.valueOf(c.getAddress());
		}else if(property.equals("operation")){
//			return String.valueOf("1");
			return null;// show the operation button
		}
		return null;
//		throw new RuntimeException("error column name : " + property);
	}

	//when modify the table
	public void modify(Object element, String property, Object value) {
		TableItem item = (TableItem) element;
		Customer c = (Customer) item.getData();		
		if (property.equals("name")) {
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}			
			c.setName(newValue);
		} else if (property.equals("area")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			c.setArea(newValue);
		} else if (property.equals("phone")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			c.setPhone(newValue);
		} else if (property.equals("address")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			c.setAddress(newValue);
		} else {
			return;//just return, do nothing
//			throw new RuntimeException("´íÎóÁÐÃû:" + property);
		}
//		System.out.println("change?");
		customerlist.customerChanged(c);
	}

}