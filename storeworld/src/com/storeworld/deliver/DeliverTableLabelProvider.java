package com.storeworld.deliver;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * get the label of deliver table
 * we can also give the column an image by this
 * @author dingyuanxiong
 *
 */
public class DeliverTableLabelProvider extends LabelProvider  implements ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Deliver){
				Deliver c = (Deliver)element;
				if(columnIndex == 0){
					return c.getID();//hide this column
				}else if(columnIndex == 1){
					return c.getBrand();
				}else if(columnIndex == 2){
					return c.getSubBrand();
				}else if (columnIndex ==3){
					return c.getSize();
				}else if (columnIndex == 4){
					return c.getUnit();
				}else if (columnIndex == 5){
					return String.valueOf(c.getPrice());
				}else if (columnIndex == 6){
					return String.valueOf(c.getNumber());
				}
			}
			return null;
		}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}