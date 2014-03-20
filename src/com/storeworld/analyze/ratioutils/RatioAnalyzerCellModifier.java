package com.storeworld.analyze.ratioutils;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;

import com.storeworld.stock.Stock;

/**
 * cannot edit the table
 */
 public class RatioAnalyzerCellModifier implements ICellModifier{
		private TableViewer tv;//just in case
		private RatioResultList resultlist;	
		
		public RatioAnalyzerCellModifier(TableViewer tv, RatioResultList resultlist) {
			this.tv = tv;
			this.resultlist = resultlist;
		}
	//can not edit the table
	@Override
	public boolean canModify(Object element, String property) {
		return false;
	}

	@Override
	public Object getValue(Object element, String property) {
		RatioAnalyzer s = (RatioAnalyzer) element;		
		if(property.equals("sub_brand")){
			return String.valueOf(s.getSubBrand());
		} else if (property.equals("shipment")) {
			return String.valueOf(s.getShipment_Profit());
		} else if (property.equals("profit")) {
			return String.valueOf(s.getShipment_Profit());
		}else if (property.equals("ratio")) {
			return String.valueOf(s.getRatio());
		}
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {					
	}		 
 }