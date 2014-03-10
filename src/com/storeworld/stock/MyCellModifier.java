package com.storeworld.stock;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class MyCellModifier implements ICellModifier {
	private TableViewer tv;

//	public static String[] NAMES = { "张三", "李四", "小红", "翠花" };

	public MyCellModifier(TableViewer tv) {
		this.tv = tv;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		Product p = (Product) element;
		if (property.equals("brand")) {
			return String.valueOf(p.getBrand());
		} else if (property.equals("sub_brand")) {
			return String.valueOf(p.getSubBrand());
		} else if (property.equals("size")) {
			return String.valueOf(p.getSize());
		}else if (property.equals("unit")) {
			return String.valueOf(p.getUnit());
		}else if (property.equals("avg_in")) {
			return String.valueOf(p.getAvgStockPrice());
		}else if (property.equals("avg_out")) {
			return String.valueOf(p.getAvgDeliverPrice());
		}else if (property.equals("repository")) {
			return String.valueOf(p.getRepository());
		}
		
		throw new RuntimeException("error column name : " + property);
	}

//	private int getNameIndex(String name) {
//		for (int i = 0; i < NAMES.length; i++) {
//			if (NAMES[i].equals(name)) {
//				return i;
//			}
//		}
//		return -1;
//	}

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
		} else if (property.equals("avg_in")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			p.setAvgStockPrice(Double.valueOf(newValue).doubleValue());
		} else if (property.equals("avg_out")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			p.setAvgDeliverPrice(Double.valueOf(newValue).doubleValue());
		} else if (property.equals("repository")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			p.setRepository(Integer.valueOf(newValue).intValue());
		} else {
			throw new RuntimeException("错误列名:" + property);
		}
		tv.update(p, null);
	}

}