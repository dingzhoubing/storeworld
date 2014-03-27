package com.storeworld.stock;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolTip;

public class StockTextCellEditor extends TextCellEditor {
	protected ToolTip tip;
	private static final int ADJUST_X = 30;
	private static final int ADJUST_Y = 20;
	// the column width, to determin the tooltip location
	private int width;
	//the column of the table, to get the validator type and determin the tooltip location
	private int col;
	public StockTextCellEditor(Composite parent, int width, int col) {
        super(parent, 0);
        this.width = width;
        this.col = col;
        tip = new ToolTip(parent.getParent().getShell(), SWT.BALLOON);
        text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	tip.setVisible(false);
            	StockTextCellEditor.this.focusLost();
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
		if(this.col == 3){
			valid = StockValidator.validateSize(val);
			if(!valid){
			Point loc = text.getParent().getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong size, the size should obey....");
        	tip.setVisible(true);
			}
		}else if(this.col ==4){
			valid = StockValidator.validateUnit(val);
			if(!valid){
			Point loc = text.getParent().getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong unit, the unit should obey....");
        	tip.setVisible(true);
			}
		}else if(this.col ==5){
			valid = StockValidator.validatePrice(val);
			if(!valid){
			Point loc = text.getParent().getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong price, the price should obey....");
        	tip.setVisible(true);
        	}
		}
		else if(this.col ==6){
			valid = StockValidator.validateNumber(val);
			if(!valid){
			Point loc = text.getParent().getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong number, the number should obey....");
        	tip.setVisible(true);
			}
		}
		
	}

}
