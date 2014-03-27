package com.storeworld.stock;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.product.Product;
import com.storeworld.utils.Utils;

/**
 * make the stock table editable
 * @author dingyuanxiong
 *
 */
public class StockCellModifier implements ICellModifier {
	private TableViewer tv;//just in case
	private StockList stocklist;	

	public StockCellModifier(TableViewer tv, StockList stocklist) {
		this.tv = tv;
		this.stocklist = stocklist;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}
	
	//when initial the table data
	public Object getValue(Object element, String property) {
		Stock s = (Stock) element;		
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
		Stock s = (Stock) item.getData();		
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
			s.setPrice(newValue);
		} else if (property.equals("number")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			s.setNumber(newValue);
		}else {
			return;//just return, do nothing
//			throw new RuntimeException("´íÎóÁÐÃû:" + property);
		}
//		System.out.println("change?");
		stocklist.stockChanged(s);
	}

}