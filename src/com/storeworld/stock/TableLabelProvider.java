package com.storeworld.stock;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableLabelProvider extends LabelProvider  implements ITableLabelProvider {
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Product){
				Product p = (Product)element;
				if(columnIndex == 0){
					return p.getBrand();
				}else if(columnIndex == 1){
					return p.getSubBrand();
				}else if (columnIndex ==2){
					return p.getSize();
				}else if (columnIndex == 3){
					return p.getUnit();
				}else if (columnIndex == 4){
					return p.getAvgStockPrice()+"";
				}else if(columnIndex == 5){
					return p.getAvgDeliverPrice() + "";
				}else if(columnIndex ==6){
					return p.getRepository()+"";
				}
			}
			return null;
		}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}