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
	// the column width, to determine the tooltip location
	private int width;
	//the column of the table, to get the validator type and determin the tooltip location
	private int col;
	private int column_shift;
	
	private static final int NAME_COLUMN = 2;
	private static final int AREA_COLUMN = 3;
	private static final int PHONE_COLUMN = 4;
	private static final int ADDRESS_COLUMN = 5;
	
	private static final String NAME_MESSAGE="名字应该为中文字串";
	private static final String AREA_MESSAGE="片区应该只包含中文或字母，数字";
	private static final String PHONE_MESSAGE="不是一个正确的电话格式";
	private static final String ADDRESS_MESSAGE="地址应该只包含中文或字母，数字";
	
	public CustomerTextCellEditor(Composite parent, int width, int col, int column_shift) {
        super(parent, 0);
        this.width = width;
        this.col = col;
        this.column_shift = column_shift;
        tip = new ToolTip(parent.getParent().getShell(), SWT.BALLOON);
        text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	if(tip!=null && !tip.isDisposed()){
            		tip.setVisible(false);
            	}
            	CustomerTextCellEditor.this.focusLost();
            }
        });
    }
	

	
	@Override
	protected void editOccured(ModifyEvent e) {
		// TODO Auto-generated method stub
		super.editOccured(e);
		//in CustomerCellModifier, we have computed the result, can be optimized
		//but we assume this is not time consuming, so let it go
		String val = (String)text.getText();
		boolean valid = false;
		if(this.col == NAME_COLUMN){
			valid = CustomerValidator.validateName(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-2)+column_shift, loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong name, the name should obey....");
            tip.setMessage(NAME_MESSAGE);
        	tip.setVisible(true);
			}
		}else if(this.col == AREA_COLUMN){
			valid = CustomerValidator.validateArea(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-2)+column_shift, loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong area, the area should obey....");
            tip.setMessage(AREA_MESSAGE);
        	tip.setVisible(true);
			}
		}else if(this.col == PHONE_COLUMN){
			valid = CustomerValidator.validatePhone(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-2)+column_shift, loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong phone number, the area should obey....");
            tip.setMessage(PHONE_MESSAGE);
        	tip.setVisible(true);
        	}
		}
		else if(this.col == ADDRESS_COLUMN){
			valid = CustomerValidator.validateAddress(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-2)+column_shift, loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong address, the address should obey....");
            tip.setMessage(ADDRESS_MESSAGE);
        	tip.setVisible(true);
			}
		}
		
	}

}
