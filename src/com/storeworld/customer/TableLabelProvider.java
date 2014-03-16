package com.storeworld.customer;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableLabelProvider extends LabelProvider  implements ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Customer){
				Customer c = (Customer)element;
				if(columnIndex == 0){
					return c.getID();//hide this column
				}else if(columnIndex == 1){
					return c.getName();
				}else if(columnIndex == 2){
					return c.getArea();
				}else if (columnIndex ==3){
					return c.getPhone();
				}else if (columnIndex == 4){
					return c.getAddress();
				}
			}
			return null;
		}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}