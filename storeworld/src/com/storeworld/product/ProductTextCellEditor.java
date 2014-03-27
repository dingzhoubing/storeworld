package com.storeworld.product;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolTip;

import com.storeworld.customer.CustomerValidator;

public class ProductTextCellEditor extends TextCellEditor {
	protected ToolTip tip;
	private static final int ADJUST_X = 30;
	private static final int ADJUST_Y = 20;
	private int col;
	private int width;
	public ProductTextCellEditor(Composite parent, int width, int col) {
        super(parent, 0);
        this.width = width;
        this.col = col;
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
		
		boolean valid = false;
		if(this.col == 1){
			valid = ProductValidator.validateBrand(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong brand name, the name should obey....");
        	tip.setVisible(true);
			}
		}else if(this.col ==2){
			valid = ProductValidator.validateSub_Brand(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong sub brand name, the name should obey....");
        	tip.setVisible(true);
			}
		}else if(this.col ==3){
			valid = ProductValidator.validateSize(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong size, the size should obey....");
        	tip.setVisible(true);
        	}
		}
		else if(this.col ==4){
			valid = ProductValidator.validateUnit(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong unit, the unit should obey....");
        	tip.setVisible(true);
			}
		}
		else if(this.col ==5){
			valid = ProductValidator.validateRepository(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage("wrong repository, the repository should obey....");
        	tip.setVisible(true);
			}
		}
	}

}
