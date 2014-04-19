package com.storeworld.product;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * get the label of a table column & also image
 * @author dingyuanxiong
 *
 */
public class ProductTableLabelProvider extends LabelProvider  implements ITableLabelProvider {
	
	private static final int ID_COLUMN = 0;
	private static final int BRAND_COLUMN = 1;
	private static final int SUB_BRAND_COLUMN = 2;
	private static final int SIZE_COLUMN = 3;
	private static final int UNIT_COLUMN = 4;
	private static final int REPOSITORY_COLUMN = 5;
	
	
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Product){
				Product p = (Product)element;
				if(columnIndex == ID_COLUMN){
					return p.getID();//hide this column
				}else if(columnIndex == BRAND_COLUMN){
					return p.getBrand();
				}else if(columnIndex == SUB_BRAND_COLUMN){
					return p.getSubBrand();
				}else if (columnIndex == SIZE_COLUMN){
					return p.getSize();
				}else if (columnIndex == UNIT_COLUMN){
					return p.getUnit();
				}
				else if(columnIndex == REPOSITORY_COLUMN){
					return p.getRepository();
				}
			}
			return null;
		}
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}