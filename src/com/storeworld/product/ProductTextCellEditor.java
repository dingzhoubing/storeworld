package com.storeworld.product;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolTip;

public class ProductTextCellEditor extends TextCellEditor {
	protected ToolTip tip;
	private static final int ADJUST_X = 30;
	private static final int ADJUST_Y = 20;
//	protected int col;
	public ProductTextCellEditor(Composite parent) {
        super(parent, 0);
//        this.col = col;
        tip = new ToolTip(parent.getParent().getShell(), SWT.BALLOON);
        text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	tip.setVisible(false);
            	ProductTextCellEditor.this.focusLost();
            }
        });
    }
	

	
	@Override
	protected void editOccured(ModifyEvent e) {
		// TODO Auto-generated method stub
		super.editOccured(e);
		String val = (String)text.getText();
        if(val.equals("123")){
        	Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X, loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong");
        	tip.setVisible(true);
        }
	}

}
