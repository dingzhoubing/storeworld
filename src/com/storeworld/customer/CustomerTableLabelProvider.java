package com.storeworld.customer;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * get the label content of the customer table
 * @author dingyuanxiong
 *
 */
public class CustomerTableLabelProvider extends LabelProvider  implements ITableLabelProvider {
	
	private static final int ID_COLUMN = 0;
	private static final int NAME_COLUMN = 2;
	private static final int AREA_COLUMN = 3;
	private static final int PHONE_COLUMN = 4;
	private static final int ADDRESS_COLUMN = 5;
	
		
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Customer){
				Customer c = (Customer)element;
				if(columnIndex == ID_COLUMN){
					return c.getID();//hide this column
				}else if(columnIndex == NAME_COLUMN){
					return c.getName();
				}else if(columnIndex == AREA_COLUMN){
					return c.getArea();
				}else if (columnIndex == PHONE_COLUMN){
					return c.getPhone();
				}else if (columnIndex == ADDRESS_COLUMN){
					return c.getAddress();
				}
			}
			return null;
		}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}