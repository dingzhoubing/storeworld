package com.storeworld.product;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.customer.Customer;
import com.storeworld.customer.CustomerValidator;
import com.storeworld.utils.Utils;

/**
 * make the product table editable
 * @author dingyuanxiong
 *
 */
public class ProductCellModifier implements ICellModifier {
	private static TableViewer tv;//just in case
	private static ProductList productlist;

	public ProductCellModifier(TableViewer tv_tmp, ProductList productlist_tmp) {
		tv = tv_tmp;
		productlist = productlist_tmp;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	public static void addNewTableRow(Product product){
//		if (CustomerValidator.checkID(c.getID()) && CustomerValidator.rowLegal(c)) {
			int new_id = Integer.valueOf(product.getID()) + 1;
			ProductValidator.setNewID(String.valueOf(new_id));
			Product prod_new = new Product(String.valueOf(new_id));
			productlist.addProduct(prod_new);
			Utils.refreshTable(tv.getTable());
//		}
	}
	
	//when initial the table data
	public Object getValue(Object element, String property) {
		Product p = (Product) element;
		if(property.equals("id")){
			return String.valueOf(p.getID());
		}else if (property.equals("brand")) {
			if(p.getBrand() != null)
				return String.valueOf(p.getBrand());
			else
				return String.valueOf("");
		} else if (property.equals("sub_brand")) {
			if(p.getSubBrand() != null)
				return String.valueOf(p.getSubBrand());
			else
				return String.valueOf("");
		} else if (property.equals("size")) {
			if(p.getSize() != null)
				return String.valueOf(p.getSize());
			else
				return String.valueOf("");
		}else if (property.equals("unit")) {
			if(p.getUnit() != null)
				return String.valueOf(p.getUnit());
			else
				return String.valueOf("");
		}else if (property.equals("repository")) {
			if(p.getRepository() != null)
				return String.valueOf(p.getRepository());
			else
				return String.valueOf("");
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
		String brandlast = "";
		String sub_brandlast = "";
		String sizelast = "";
		String unitlast = "";
		String repolast = "";
		boolean hasBeenChanged = false;
		if (property.equals("brand")) {
			String newValue = (String) value;
			if (newValue.equals("")) {
				return;
			}			
			brandlast = p.getBrand();
			if (brandlast != null) {
				if (!brandlast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			p.setBrand(newValue);
		} else if (property.equals("sub_brand")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			
			sub_brandlast = p.getSubBrand();
			if (sub_brandlast != null) {
				if (!sub_brandlast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			p.setSubBrand(newValue);
		} else if (property.equals("size")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			sizelast = p.getSize();
			if (sizelast != null) {
				if (!sizelast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			p.setSize(newValue);
		} else if (property.equals("unit")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			
			unitlast = p.getUnit();
			if (unitlast != null) {
				if (!unitlast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			p.setUnit(newValue);
		} else if (property.equals("repository")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}	
			
			repolast = p.getRepository();
			if (repolast != null) {
				if (!repolast.equals(newValue))
					hasBeenChanged = true;
			} else {
				hasBeenChanged = true;
			}
			
			p.setRepository(newValue);
		} else {
			return;//just return, do nothing
//			throw new RuntimeException("´íÎóÁÐÃû:" + property);
		}

		boolean valid = false;
		if (hasBeenChanged) {

			if (property.equals("brand")) {
				valid = ProductValidator.validateBrand(p.getBrand());//tv.getTable(), item, 1, 
				if (!valid) {
					p.setBrand(brandlast);
				}

			} else if (property.equals("sub_brand")) {
				valid = ProductValidator.validateSub_Brand(p.getSubBrand());//tv.getTable(), item, 2, 
				if (!valid) {
					p.setSubBrand(sub_brandlast);
				}

			} else if (property.equals("size")) {
				valid = ProductValidator.validateSize(p.getSize());//tv.getTable(), item, 3, 
				if (!valid) {
					p.setSize(sizelast);
				}
			} else if (property.equals("unit")) {
				valid = ProductValidator.validateUnit(p.getUnit());//tv.getTable(), item, 4, 
				if (!valid) {
					p.setUnit(unitlast);
				}
			}else if (property.equals("repository")) {
				valid = ProductValidator.validateRepository(p.getRepository());//tv.getTable(), item, 5, 
				if (!valid) {
					p.setUnit(unitlast);
				}
			}
			if (valid) {
				productlist.productChanged(p);
				
			}
		}
	}

}