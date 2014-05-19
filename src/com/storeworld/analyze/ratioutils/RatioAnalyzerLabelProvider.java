package com.storeworld.analyze.ratioutils;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * the label provider	
 */
public class RatioAnalyzerLabelProvider extends LabelProvider  implements ITableLabelProvider {
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof RatioAnalyzer){
			RatioAnalyzer c = (RatioAnalyzer)element;
			if(columnIndex == 0){
				return c.getCol1();
			}else if(columnIndex == 1){
				return c.getCol2();
			}else if(columnIndex == 2){
				return c.getCol3();
			}
		}
		return null;
	}
	//set the image of a table colomn
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
}