package com.storeworld.stock;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.mainui.MainUI;
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
	private static DecimalFormat df = new DecimalFormat("0.00");
	private static StockCellModifier smodifier = null;
	
	public StockCellModifier(TableViewer tv_tmp, StockList stocklist_tmp) {
		tv = tv_tmp;
		stocklist = stocklist_tmp;		
	}

	private static StockCellModifier getInstance(){
		if(smodifier == null){
			smodifier = new StockCellModifier(tv, stocklist);
			return smodifier;
		}else{
			//nothing
			return smodifier;
		}
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
	
	@Deprecated
	public static Stock getLastStock(){
		return stock_backup;
	}
	
	/**
	 * add a new row in table UI
	 * @param stock
	 * @throws Exception 
	 */
	public static void addNewTableRow(Stock stock) throws Exception {
		int new_id = Integer.valueOf(stock.getID()) + 1;
		StockUtils.setNewLineID(String.valueOf(new_id));
		Stock stock_new = new Stock(String.valueOf(new_id));
		try {
			stocklist.addStock(stock_new);
		} catch (Exception e) {
			System.out.println("add new table row failed");
			throw e;
		}
		
		Utils.refreshTable(tv.getTable());
	}
		
	/**
	 * when initial the table value
	 */
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
				if(s.getPrice().equals(""))
					return "";
				else
					return String.valueOf(df.format(Double.valueOf(s.getPrice())));
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
	}

	public static void staticModify(Object element, String property, Object value){
		getInstance().modify(element, property, value);
	}

	/**
	 * when modify the table value
	 */
	public void modify(Object element, String property, Object value) {
		
		StockUtils.setSizeUnitChanged(false);//initial
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
			if(s.getPrice().equals(""))
				pricelast = "";
			else
				pricelast = df.format(Double.valueOf(s.getPrice()));
			String newValue = "";
			if(!value.equals("")){
				try{
					newValue = df.format(Double.valueOf((String) value));
				}catch(NumberFormatException e){
					newValue = (String) value;
				}
			}
			if (newValue.equals("")) {
				return;
			}
			if (pricelast != null) {
				if (!pricelast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			if(newValue.equals(""))
				s.setPrice("");
			else
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
		}
		//validate if the change of the table is valid, if not, do not update the 
		//table UI & database
		boolean valid = false;
		if (hasBeenChanged) {

			if (property.equals("brand")) {
				valid = StockValidator.validateBrand(s.getBrand()); 
				if (!valid) {
					s.setBrand(brandlast);
				}

			} else if (property.equals("sub_brand")) {
				valid = StockValidator.validateSub_Brand(s.getSubBrand()); 
				if (!valid) {
					s.setSubBrand(sub_brandlast);
				}
				
			} else if (property.equals("size")) {
				StockUtils.setSizeUnitChanged(true);
				valid = StockValidator.validateSize(s.getSize()); 
				if (!valid) {
					s.setSize(sizelast);
				}
			} else if (property.equals("unit")) {
				StockUtils.setSizeUnitChanged(true);
				valid = StockValidator.validateUnit(s.getUnit()); 
				if (!valid) {
					s.setUnit(unitlast);
				}				
			} else if (property.equals("price")) {
				valid = StockValidator.validatePrice(s.getPrice()); 
				if (!valid) {
					s.setPrice(pricelast);
				}				
			} else if (property.equals("number")) {
				valid = StockValidator.validateNumber(s.getNumber()); 
				if (!valid) {
					s.setNumber(numberlast);
				}				
			}
			if (valid) {
				//if the change of the table item is valid, we make the change valid in UI and database
				try {
					stocklist.stockChanged(s);
				} catch (Exception e) {
					//set back the value
					if (property.equals("brand")) {
						s.setBrand(brandlast);
					} else if (property.equals("sub_brand")) {						
						s.setSubBrand(sub_brandlast);						
					} else if (property.equals("size")) {						
						s.setSize(sizelast);						
					} else if (property.equals("unit")) {						
						s.setUnit(unitlast);									
					} else if (property.equals("price")) {						
						s.setPrice(pricelast);									
					} else if (property.equals("number")) {						
						s.setNumber(numberlast);									
					}
					
					MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
					mbox.setMessage("更新进货信息表失败");
					mbox.open();
				}
				
			}
		}
	}

}