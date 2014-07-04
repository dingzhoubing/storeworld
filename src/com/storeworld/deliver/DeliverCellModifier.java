package com.storeworld.deliver;

import java.text.DecimalFormat;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.mainui.MainUI;
import com.storeworld.stock.StockCellModifier;
import com.storeworld.utils.Utils;

/**
 * make the deliver table editable
 * @author dingyuanxiong
 *
 */
public class DeliverCellModifier implements ICellModifier {
	private static TableViewer tv;//just in case
	private static DeliverList deliverlist;
	private static DecimalFormat df = new DecimalFormat("#.00");
	private static DeliverCellModifier smodifier = null;
	
	public DeliverCellModifier(TableViewer tv_tmp, DeliverList deliverlist_tmp) {
		tv = tv_tmp;
		deliverlist = deliverlist_tmp;
	}

	private static DeliverCellModifier getInstance(){
		if(smodifier == null){
			smodifier = new DeliverCellModifier(tv, deliverlist);
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
	 * get the DeliverList
	 * @return
	 */
	public static DeliverList getDeliverList(){
		return deliverlist;
	}
	
	/**
	 * get the TableViewer
	 * @return
	 */
	public static TableViewer getTableViewer(){
		return tv;
	}
	
	/**
	 * add a new row of deliver table 
	 * @param deliver
	 */
	public static void addNewTableRow(Deliver deliver){
//		if (CustomerValidator.checkID(c.getID()) && CustomerValidator.rowLegal(c)) {
			int new_id = Integer.valueOf(deliver.getID()) + 1;
//			DeliverValidator.setNewID(String.valueOf(new_id));
			DeliverUtils.setNewLineID(String.valueOf(new_id));
			Deliver deliver_new = new Deliver(String.valueOf(new_id));
			deliverlist.addDeliver(deliver_new);
			Utils.refreshTable(tv.getTable());
//		}
	}
	
	//when initial the table data
	@Override
	public Object getValue(Object element, String property) {
		Deliver s = (Deliver) element;
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
			if(s.getPrice() != null){
//				return String.valueOf(s.getPrice());
				if(s.getPrice().equals(""))
					return "";
				else
					return String.valueOf(df.format(Double.valueOf(s.getPrice())));
			}
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

	public static void staticModify(Object element, String property, Object value){
		getInstance().modify(element, property, value);
	}
	
	//when modify the table
	@Override
	public void modify(Object element, String property, Object value) {
		
		DeliverUtils.setSizeUnitChanged(false);		
		TableItem item = (TableItem) element;
		Deliver s = (Deliver) item.getData();		
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
//				newValue = df.format(Double.valueOf((String) value));				
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
//			s.setPrice(newValue);
			if(newValue.equals(""))
				s.setPrice("");
			else
				s.setPrice(newValue);//df.format(Double.valueOf(newValue))
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
//			throw new RuntimeException("错误列名:" + property);
		}

		boolean valid = false;
		if (hasBeenChanged) {

			if (property.equals("brand")) {
				valid = DeliverValidator.validateBrand(s.getBrand());//tv.getTable(), item, 1, 
				if (!valid) {
					s.setBrand(brandlast);
				}

			} else if (property.equals("sub_brand")) {
				valid = DeliverValidator.validateSub_Brand(s.getSubBrand());//tv.getTable(), item, 2, 
				if (!valid) {
					s.setSubBrand(sub_brandlast);
				}

			} else if (property.equals("size")) {
				DeliverUtils.setSizeUnitChanged(true);
				valid = DeliverValidator.validateSize(s.getSize());//tv.getTable(), item, 3, 
				if (!valid) {
					s.setSize(sizelast);
				}
			} else if (property.equals("unit")) {
				DeliverUtils.setSizeUnitChanged(true);
				valid = DeliverValidator.validateUnit(s.getUnit());//tv.getTable(), item, 4, 
				if (!valid) {
					s.setUnit(unitlast);
				}				
			} else if (property.equals("price")) {
				valid = DeliverValidator.validatePrice(s.getPrice());//tv.getTable(), item, 4, 
				if (!valid) {
					s.setPrice(pricelast);
				}				
			} else if (property.equals("number")) {
				valid = DeliverValidator.validateNumber(s.getNumber());//tv.getTable(), item, 4, 
				if (!valid) {
					s.setNumber(numberlast);
				}				
			}
			if (valid) {
				try {
					deliverlist.deliverChanged(s);
				} catch (Exception e) {
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
					mbox.setMessage("修改送货信息失败");
					mbox.open();
				}
			}
		}
	}

}