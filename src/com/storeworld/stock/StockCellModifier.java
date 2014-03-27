package com.storeworld.stock;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.customer.Customer;
import com.storeworld.customer.CustomerValidator;
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
			if(s.getBrand() != null)
				return String.valueOf(s.getBrand());
			else
				return String.valueOf("");			
		} else if (property.equals("sub_brand")) {
			if(s.getSubBrand() != null)
				return String.valueOf(s.getSubBrand());
			else
				return String.valueOf("");			
		} else if (property.equals("size")) {			
			if(s.getSize() != null)
				return String.valueOf(s.getSize());
			else
				return String.valueOf("");			
		}else if (property.equals("unit")) {			
			if(s.getUnit() != null)
				return String.valueOf(s.getUnit());
			else
				return String.valueOf("");			
		}else if (property.equals("price")) {			
			if(s.getPrice() != null)
				return String.valueOf(s.getPrice());
			else
				return String.valueOf("");			
		}else if (property.equals("number")) {			
			if(s.getNumber() != null)
				return String.valueOf(s.getNumber());
			else
				return String.valueOf("");			
		}else if(property.equals("operation")){
			return null;// show the operation button
		}
		return null;
//		throw new RuntimeException("error column name : " + property);
	}

	//when modify the table
	public void modify(Object element, String property, Object value) {
		TableItem item = (TableItem) element;		
		Stock s = (Stock) item.getData();	
		String brandlast = "";
		String sub_brandlast = "";
		String sizelast = "";
		String unitlast = "";
		String pricelast = "";
		String numberlast = "";
		boolean hasBeenChanged = false;
		if (property.equals("brand")) {
				
			brandlast = s.getBrand();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			if (brandlast != null) {
				if (!brandlast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			s.setBrand(newValue);
			
		} else if (property.equals("sub_brand")) {
			sub_brandlast = s.getSubBrand();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			if (sub_brandlast != null) {
				if (!sub_brandlast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			s.setSubBrand(newValue);

		} else if (property.equals("size")) {
			sizelast = s.getSize();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			if (sizelast != null) {
				if (!sizelast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			s.setSize(newValue);
		} else if (property.equals("unit")) {
			unitlast = s.getUnit();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			if (unitlast != null) {
				if (!unitlast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			s.setUnit(newValue);
		} else if (property.equals("price")) {
			pricelast = s.getPrice();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			if (pricelast != null) {
				if (!pricelast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			s.setPrice(newValue);
		} else if (property.equals("number")) {
			numberlast = s.getNumber();
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}
			if (numberlast != null) {
				if (!numberlast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			s.setNumber(newValue);
		}else {
			return;//just return, do nothing
//			throw new RuntimeException("´íÎóÁÐÃû:" + property);
		}

		boolean valid = false;
		if (hasBeenChanged) {

			if (property.equals("brand")) {
				valid = StockValidator.validateBrand(s.getBrand());//tv.getTable(), item, 1, 
				if (!valid) {
					s.setBrand(brandlast);
				}

			} else if (property.equals("sub_brand")) {
				valid = StockValidator.validateSub_Brand(s.getSubBrand());//tv.getTable(), item, 2, 
				if (!valid) {
					s.setSubBrand(sub_brandlast);
				}

			} else if (property.equals("size")) {
				valid = StockValidator.validateSize(s.getSize());//tv.getTable(), item, 3, 
				if (!valid) {
					s.setSize(sizelast);
				}
			} else if (property.equals("unit")) {
				valid = StockValidator.validateUnit(s.getUnit());//tv.getTable(), item, 4, 
				if (!valid) {
					s.setUnit(unitlast);
				}				
			} else if (property.equals("price")) {
				valid = StockValidator.validatePrice(s.getPrice());//tv.getTable(), item, 4, 
				if (!valid) {
					s.setPrice(pricelast);
				}				
			} else if (property.equals("number")) {
				valid = StockValidator.validateNumber(s.getNumber());//tv.getTable(), item, 4, 
				if (!valid) {
					s.setNumber(numberlast);
				}				
			}
			if (valid) {
				stocklist.stockChanged(s);
				if (StockValidator.checkID(s.getID()) && StockValidator.rowLegal(s)) {
					int new_id = Integer.valueOf(s.getID()) + 1;
					StockValidator.setNewID(String.valueOf(new_id));
					Stock stock_new = new Stock(String.valueOf(new_id));
					stocklist.addStock(stock_new);
					Utils.refreshTable(tv.getTable());
				}
			}
		}
	}

}