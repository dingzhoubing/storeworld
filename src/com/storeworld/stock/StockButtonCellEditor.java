package com.storeworld.stock;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Utils;

/**
 * the remove button on stock table
 * once mouse hover on a row of the table, the button appears
 * @author dingyuanxiong
 *
 */
public class StockButtonCellEditor extends CellEditor {

    protected Button button; //the remove button
    protected Table table; //the parent table
    protected StockList stocklist; //the data set in the parent table
    protected int rowHeight = 0; //the row height of the row, no use now

    public StockButtonCellEditor() {
        setStyle(0);
    }

    
    public StockButtonCellEditor(Composite parent, StockList productlist, int rowHeight) {
        this(parent, 0);
        this.table = (Table)parent;
        this.stocklist = productlist;
        this.rowHeight = rowHeight;//not been used now
//        button = new Button(parent, 0);
    }

    public StockButtonCellEditor(Composite parent, int style) {
        super(parent, 0);
    }
    
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, 0);
                
        button.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	if(button!=null)
            		button.setVisible(false);
            	StockButtonCellEditor.this.focusLost();
            }
        });
        button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int ptY = button.getBounds().y+1;//make it always not equals the up one
				int rowCount = table.getItemCount();				
				int index = table.getTopIndex();	
				for (; index < rowCount; index++) {
					TableItem item = table.getItem(index);
					int rowY = item.getBounds().y;						
					if (rowY <= ptY && ptY <= (rowY+rowHeight)) {//ptY <= (rowY+rowHeight) no use now
						Stock c = (Stock)(table.getItem(index).getData());		
						//remove the stock item
						try {
							stocklist.removeStock(c);
						} catch (Exception e1) {
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("删除该条进货记录失败，请重试");
							mbox.open();
							return;
						}	
						
						//only when delete succeed
						button.setVisible(false);
						//refresh the stock table
						Utils.refreshTable(table);												
								 
						break;
					}
				}
			}
		});
        button.setFont(parent.getFont());
        button.setText("删除");
        return button;
	}

	@Override
	protected Object doGetValue() {
		return null;
	}

	@Override
	protected void doSetFocus() {
		 if (button != null) {
	         button.setFocus();
			 button.setVisible(true);
	        }
	}

	@Override
	protected void doSetValue(Object value) {
	}

}
