package com.storeworld.product;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;


public class MyCellModifier implements ICellModifier {
	private TableViewer tv;//just in case
	private ProductList productlist;

	public MyCellModifier(TableViewer tv, ProductList productlist) {
		this.tv = tv;
		this.productlist = productlist;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	//when initial the table data
	public Object getValue(Object element, String property) {
		Product p = (Product) element;
		if(property.equals("id")){
			return String.valueOf(p.getID());
		}else if (property.equals("brand")) {
			return String.valueOf(p.getBrand());
		} else if (property.equals("sub_brand")) {
			return String.valueOf(p.getSubBrand());
		} else if (property.equals("size")) {
			return String.valueOf(p.getSize());
		}else if (property.equals("unit")) {
			return String.valueOf(p.getUnit());
		}else if (property.equals("repository")) {
			return String.valueOf(p.getRepository());
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
		Product p = (Product) item.getData();		
		if (property.equals("brand")) {
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}			
			p.setBrand(newValue);
		} else if (property.equals("sub_brand")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			p.setSubBrand(newValue);
		} else if (property.equals("size")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			p.setSize(newValue);
		} else if (property.equals("unit")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			p.setUnit(newValue);
		} else if (property.equals("repository")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			p.setRepository(Integer.valueOf(newValue).intValue());
		} else {
			return;//just return, do nothing
//			throw new RuntimeException("´íÎóÁÐÃû:" + property);
		}
//		System.out.println("change?");
		productlist.productChanged(p);
	}

}