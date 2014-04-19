package com.storeworld.stock;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.utils.Utils;

/**
 * make the stock table editable
 * @author dingyuanxiong
 *
 */
public class StockCellModifier implements ICellModifier {
	private static TableViewer tv;//just in case
	private static StockList stocklist;	
	private static Stock stock_backup = new Stock();
	
	public StockCellModifier(TableViewer tv_tmp, StockList stocklist_tmp) {
		tv = tv_tmp;
		stocklist = stocklist_tmp;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}
	
	/**
	 * get the class instance StockList, not the stocks
	 * @return
	 */
	public static StockList getStockList(){
		return stocklist;
	}
	
	/**
	 * get the table viewer, to operate the table
	 * @return
	 */
	public static TableViewer getTableViewer(){
		return tv;
	}
	//no use now
	public static Stock getLastStock(){
		return stock_backup;
	}
	
	/**
	 * 1. add a new row in table UI
	 * 2. compute the total value here
	 * @param stock
	 */
	public static void addNewTableRow(Stock stock){
//		if (CustomerValidator.checkID(c.getID()) && CustomerValidator.rowLegal(c)) {
			int new_id = Integer.valueOf(stock.getID()) + 1;
//			StockValidator.setNewID(String.valueOf(new_id));
			StockUtils.setNewLineID(String.valueOf(new_id));
			Stock stock_new = new Stock(String.valueOf(new_id));
			stocklist.addStock(stock_new);
			Utils.refreshTable(tv.getTable());
//		}
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
		//validate if the change of the table is valid, if not, do not update the 
		//table UI & database
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
				
			}
		}
	}

}