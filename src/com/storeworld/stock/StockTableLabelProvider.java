package com.storeworld.stock;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * get the content part of the column in stock table
 * and also the image of the column
 * @author dingyuanxiong
 *
 */
public class StockTableLabelProvider extends LabelProvider  implements ITableLabelProvider {
	
	private static final int ID_COLUMN = 0;
	private static final int BRAND_COLUMN = 1;
	private static final int SUB_BRAND_COLUMN = 2;
	private static final int SIZE_COLUMN = 3;
	private static final int UNIT_COLUMN = 4;
	private static final int PRICE_COLUMN = 5;
	private static final int NUMBER_COLUMN = 6;
	
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Stock){
				Stock c = (Stock)element;
				if(columnIndex == ID_COLUMN){
					return c.getID();//hide this column
				}else if(columnIndex == BRAND_COLUMN){
					return c.getBrand();
				}else if(columnIndex == SUB_BRAND_COLUMN){
					return c.getSubBrand();
				}else if (columnIndex == SIZE_COLUMN){
					return c.getSize();
				}else if (columnIndex == UNIT_COLUMN){
					return c.getUnit();
				}else if (columnIndex == PRICE_COLUMN){
					return String.valueOf(c.getPrice());
				}else if (columnIndex == NUMBER_COLUMN){
					return String.valueOf(c.getNumber());
				}
			}
			return null;
		}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}