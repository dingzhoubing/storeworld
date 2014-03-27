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
		//in CustomerCellModifier, we have computed the result, can be optimized
		//bu we assume this is not time consuming, so let it go
		String val = (String)text.getText();
		boolean valid = false;
		if(this.col == 1){
			valid = CustomerValidator.validateName(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong name, the name should obey....");
        	tip.setVisible(true);
			}
		}else if(this.col ==2){
			valid = CustomerValidator.validateArea(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong area, the area should obey....");
        	tip.setVisible(true);
			}
		}else if(this.col ==3){
			valid = CustomerValidator.validatePhone(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong phone number, the area should obey....");
        	tip.setVisible(true);
        	}
		}
		else if(this.col ==4){
			valid = CustomerValidator.validateAddress(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong address, the address should obey....");
        	tip.setVisible(true);
			}
		}
		
	}

}
