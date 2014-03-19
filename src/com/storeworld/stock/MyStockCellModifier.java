package com.storeworld.stock;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.product.Product;
import com.storeworld.utils.Utils;


public class MyStockCellModifier implements ICellModifier {
	private TableViewer tv;//just in case
	private StockList stocklist;	
	private static String tmp_brand = "";
	private static String tmp_sub_brand = "";
	public MyStockCellModifier(TableViewer tv, StockList stocklist) {
		this.tv = tv;
		this.stocklist = stocklist;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}
//	private int getBrandIndex(String brand){
//		for(int i=0;i<Utils.getBrands().length;i++){
//			if(Utils.getBrands()[i].equals(brand)){
//				return i;
//			}
//		}
//		return -1;
//	}
//	//here use a const brand to get all the sub brands
//	private int getSubBrandIndex(String subbrand, String brand){
//		for(int i=0;i<Utils.getSub_Brands("五得利").length;i++){//Utils.getSub_Brands(brand)
//			if(Utils.getSub_Brands("五得利")[i].equals(subbrand)){
//				return i;
//			}
//		}
//		return -1;
//	}
	
	//when initial the table data
	public Object getValue(Object element, String property) {
		Stock s = (Stock) element;		
		if(property.equals("id")){
			return String.valueOf(s.getID());
		}else if (property.equals("brand")) {
//			int ret = getBrandIndex(s.getBrand()); 
//			if(ret == -1)
//				return s.getBrand()+"A";
//			else
//				return ret+"B";
//			tmp_brand = s.getBrand();
//			return new Integer(getBrandIndex(s.getBrand()));//String.valueOf(s.getBrand());
			return String.valueOf(s.getBrand());
		} else if (property.equals("sub_brand")) {
//			int ret = getSubBrandIndex(s.getSubBrand(), s.getBrand());
//			if(ret == -1){
//				return s.getSubBrand()+"A";
//			}else{
//				return ret+"B";
//			}			
//			tmp_sub_brand = s.getSubBrand();
//			return new Integer(getSubBrandIndex(s.getSubBrand(), s.getBrand()));//String.valueOf(s.getSubBrand());
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
//			Integer comboIndex = (Integer)value;
//			if(comboIndex.intValue() == -1){
////				s.setBrand(String.valueOf(value));
////				System.out.println("brand value: "+String.valueOf(value));
//				s.setBrand(Utils.getComboValue());
//			}else{
//				String newValue = Utils.getBrands()[comboIndex.intValue()];
//				s.setBrand(newValue);
//			}
//			String newValue = (String) value;
//			newValue = newValue.substring(0, newValue.length());
//			s.setBrand(newValue);
		} else if (property.equals("sub_brand")) {
			String newValue = (String) value;			
			if (newValue.equals("")) {
				return;
			}			
			s.setSubBrand(newValue);
//			Integer comboIndex = (Integer)value;
//			if(comboIndex.intValue() == -1){
////				s.setBrand(String.valueOf(value));
////				System.out.println("subbrand value: "+String.valueOf(value));
//				s.setSubBrand(Utils.getComboValue());
//			}else{
//			String newValue = Utils.getSub_Brands(s.getBrand())[comboIndex.intValue()];
//			s.setSubBrand(newValue);
//			}
//			String newValue = (String) value;
//			newValue = newValue.substring(0, newValue.length());
//			s.setSubBrand(newValue);
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
//			throw new RuntimeException("错误列名:" + property);
		}
//		System.out.println("change?");
		stocklist.stockChanged(s);
	}

}