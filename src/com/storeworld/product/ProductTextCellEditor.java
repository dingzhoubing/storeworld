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
	private int col;
	private int width;
	
	private static final int BRAND_COLUMN = 1;
	private static final int SUB_BRAND_COLUMN = 2;
	private static final int SIZE_COLUMN = 3;
	private static final int UNIT_COLUMN = 4;
	private static final int REPOSITORY_COLUMN = 5;
	
	private static final String BRAND_MESSAGE="品牌应该为中文字串";
	private static final String SUB_BRAND_MESSAGE="子品牌应该为中文字串";
	private static final String SIZE_MESSAGE="规格应该以数字开头以中文或字母结尾";
	private static final String UNIT_MESSAGE="单位应该为 1至5中文字串";
	private static final String REPOSITORY_MESSAGE="库存应该为正数";
	
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
		if(this.col == BRAND_COLUMN){
			valid = ProductValidator.validateBrand(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong brand name, the name should obey....");
            tip.setMessage(BRAND_MESSAGE);
        	tip.setVisible(true);
			}
		}else if(this.col == SUB_BRAND_COLUMN){
			valid = ProductValidator.validateSub_Brand(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong sub brand name, the name should obey....");
            tip.setMessage(SUB_BRAND_MESSAGE);
        	tip.setVisible(true);
			}
		}else if(this.col == SIZE_COLUMN){
			valid = ProductValidator.validateSize(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong size, the size should obey....");
            tip.setMessage(SIZE_MESSAGE);
        	tip.setVisible(true);
        	}
		}
		else if(this.col == UNIT_COLUMN){
			valid = ProductValidator.validateUnit(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong unit, the unit should obey....");
            tip.setMessage(UNIT_MESSAGE);
        	tip.setVisible(true);
			}
		}
		else if(this.col == REPOSITORY_COLUMN){
			valid = ProductValidator.validateRepository(val);
			if(!valid){
			Point loc = text.getParent().toDisplay(text.getParent().getLocation());
            tip.setLocation(loc.x+ADJUST_X+width*(col-1), loc.y+text.getLocation().y+ADJUST_Y);//
//        	tip.setMessage("wrong repository, the repository should obey....");
            tip.setMessage(REPOSITORY_MESSAGE);
        	tip.setVisible(true);
			}
		}
	}

}
