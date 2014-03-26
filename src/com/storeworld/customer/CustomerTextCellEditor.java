package com.storeworld.customer;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolTip;

public class CustomerTextCellEditor extends TextCellEditor {
	protected ToolTip tip;
	private static final int ADJUST_X = 30;
	private static final int ADJUST_Y = 20;
	// the column width, to determin the tooltip location
	private int width;
	//the column of the table, to get the validator type and determin the tooltip location
	private int col;
	public CustomerTextCellEditor(Composite parent, int width, int col) {
        super(parent, 0);
        this.width = width;
        this.col = col;
        tip = new ToolTip(parent.getParent().getShell(), SWT.BALLOON);
        text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	tip.setVisible(false);
            	CustomerTextCellEditor.this.focusLost();
            }
        });
    }
	

	
	@Override
	protected void editOccured(ModifyEvent e) {
		// TODO Auto-generated method stub
		super.editOccured(e);
		String val = (String)text.getText();
        if(val.equals("123")){
//        	System.out.println("border width: "+text.getBorderWidth());
        	Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong");
        	tip.setVisible(true);
        }
	}

}
