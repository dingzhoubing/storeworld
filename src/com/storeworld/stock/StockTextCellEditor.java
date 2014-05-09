package com.storeworld.stock;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolTip;

import com.storeworld.utils.GeneralCCombo;

public class StockTextCellEditor extends TextCellEditor {
	protected ToolTip tip;
	private static final int ADJUST_X = 30;
	private static final int ADJUST_Y = 20;
	// the column width, to determine the tooltip location
	private int width;
	//the column of the table, to get the validator type and determine the tooltip location
	private int col;
	
	private static final int SIZE_COLUMN = 3;
	private static final int UNIT_COLUMN = 4;
	private static final int PRICE_COLUMN = 5;
	private static final int NUMBER_COLUMN = 6;
	
	private static final String SIZE_MESSAGE="规格应该以数字开头以中文或字母结尾";
	private static final String UNIT_MESSAGE="单位应该为 1至5中文字串";
	private static final String PRICE_MESSAGE="单价应该为整数或小数，精确到分";
	private static final String NUMBER_MESSAGE="数量应该为正数";
	
	public StockTextCellEditor(Composite parent, int width, int col) {
        super(parent, 0);
        this.width = width;
        this.col = col;
        tip = new ToolTip(parent.getParent().getShell(), SWT.BALLOON);
        text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	if(tip!=null && !tip.isDisposed())
            		tip.setVisible(false);
            	StockTextCellEditor.this.focusLost();
            }
        });
    }
	

	private int getShiftWidth(int col){
		ArrayList<Integer> tpShift = StockContentPart.getTpShift();
		int ret = 0;
		for(int i=0; i<(col-1); i++)
			ret+=tpShift.get(i);
		return ret;		
	}
	
	@Override
	protected void editOccured(ModifyEvent e) {		
		// TODO Auto-generated method stub
		super.editOccured(e);
		//in CustomerCellModifier, we have computed the result, can be optimized
		//bu we assume this is not time consuming, so let it go
		String val = (String)text.getText();
		boolean valid = false;
		if(GeneralCCombo.getToolTip()!=null)
			GeneralCCombo.getToolTip().setVisible(false);
//		if(this.col == SIZE_COLUMN){
//			valid = StockValidator.validateSize(val);
//			if(!valid){
//			Point loc = text.getParent().getParent().toDisplay(text.getParent().getLocation());
//            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage(SIZE_MESSAGE);
//        	tip.setVisible(true);
//			}
//		}else 
		if(this.col == UNIT_COLUMN){
			valid = StockValidator.validateUnit(val);
			if(!valid){
			Point loc = text.getParent().getParent().toDisplay(text.getParent().getLocation());
//            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
			tip.setLocation(loc.x+ADJUST_X+getShiftWidth(col), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage(UNIT_MESSAGE);
        	tip.setVisible(true);
			}
		}else if(this.col == PRICE_COLUMN){
			valid = StockValidator.validatePrice(val);
			if(!valid){
			Point loc = text.getParent().getParent().toDisplay(text.getParent().getLocation());
//            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
			tip.setLocation(loc.x+ADJUST_X+getShiftWidth(col), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage(PRICE_MESSAGE);
        	tip.setVisible(true);
        	}
		}
		else if(this.col == NUMBER_COLUMN){
			valid = StockValidator.validateNumber(val);
			if(!valid){
			Point loc = text.getParent().getParent().toDisplay(text.getParent().getLocation());
//            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
			tip.setLocation(loc.x+ADJUST_X+getShiftWidth(col), loc.y+text.getLocation().y+ADJUST_Y);//
        	tip.setMessage(NUMBER_MESSAGE);
        	tip.setVisible(true);
			}
		}
		
	}

}
