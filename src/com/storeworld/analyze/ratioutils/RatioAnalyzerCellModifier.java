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
		if(property.equals("col1")){
			return String.valueOf(s.getCol1());
		} else if (property.equals("col2")) {			
			return String.valueOf(s.getCol2());
		}else if (property.equals("ratio")) {
			return String.valueOf(s.getCol3());
		}
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {					
	}		 
 }