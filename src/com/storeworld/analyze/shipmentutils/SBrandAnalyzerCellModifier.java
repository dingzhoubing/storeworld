package com.storeworld.analyze.shipmentutils;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;

import com.storeworld.stock.Stock;

/**
 * cannot edit the table
 */
 public class SBrandAnalyzerCellModifier implements ICellModifier{
		private TableViewer tv;//just in case
		private SBrandResultList resultlist;	
		
		public SBrandAnalyzerCellModifier(TableViewer tv, SBrandResultList resultlist) {
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
		SBrandAnalyzer s = (SBrandAnalyzer) element;		
		if(property.equals("sub_brand")){
			return String.valueOf(s.getSubBrand());
		} else if (property.equals("shipment")) {
			return String.valueOf(s.getShipment());
		}else if (property.equals("ratio")) {
			return String.valueOf(s.getRatio());
		}
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {					
	}		 
 }