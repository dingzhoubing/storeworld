package com.storeworld.deliver;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.product.Product;


public class MyDeliverCellModifier implements ICellModifier {
	private TableViewer tv;//just in case
	private DeliverList deliverlist;

	public MyDeliverCellModifier(TableViewer tv, DeliverList deliverlist) {
		this.tv = tv;
		this.deliverlist = deliverlist;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	//when initial the table data
	public Object getValue(Object element, String property) {
		Deliver s = (Deliver) element;
		if(property.equals("id")){
			return String.valueOf(s.getID());
		}else if (property.equals("brand")) {
			return String.valueOf(s.getBrand());
		} else if (property.equals("sub_brand")) {
			return String.valueOf(s.getSubBrand());
		} else if (property.equals("size")) {
			return String.valueOf(s.getSize());
		}else if (property.equals("unit")) {
			return String.valueOf(s.getUnit());
		}else if (property.equals("price")) {
			return String.valueOf(s.getPrice());
		}else if (property.equals("number")) {
			return String.valueOf(s.getNumber());
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
		Deliver s = (Deliver) item.getData();		
		if (property.equals("brand")) {
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}			
			s.setBrand(newValue);
		} else if (property.equals("sub_brand")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			s.setSubBrand(newValue);
		} else if (property.equals("size")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			s.setSize(newValue);
		} else if (property.equals("unit")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			s.setUnit(newValue);
		} else if (property.equals("price")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			s.setPrice(Double.valueOf(newValue).doubleValue());
		} else if (property.equals("number")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			s.setNumber(Integer.valueOf(newValue).intValue());
		}else {
			return;//just return, do nothing
//			throw new RuntimeException("´íÎóÁÐÃû:" + property);
		}
//		System.out.println("change?");
		deliverlist.deliverChanged(s);
	}

}