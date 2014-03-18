package com.storeworld.stock;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class StockTestTextCellEditor extends TextCellEditor {
	
	public StockTestTextCellEditor(Composite parent, int style){
		super(parent, SWT.WRAP);
	}

	public int getStyle() {
		return SWT.WRAP;
	}
	
}
