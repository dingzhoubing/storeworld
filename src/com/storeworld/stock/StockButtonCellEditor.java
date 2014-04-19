package com.storeworld.stock;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.utils.Utils;

/**
 * the remove button on stock table
 * once mouse hover on a row of the table, the button appears
 * @author dingyuanxiong
 *
 */
public class StockButtonCellEditor extends CellEditor {

    protected Button button;
    protected Table table;
    protected StockList stocklist;
    protected int rowHeight = 0;

    public StockButtonCellEditor() {
        setStyle(0);
    }

    
    public StockButtonCellEditor(Composite parent, StockList productlist, int rowHeight) {
        this(parent, 0);
        this.table = (Table)parent;
        this.stocklist = productlist;
        this.rowHeight = rowHeight;
    }

    public StockButtonCellEditor(Composite parent, int style) {
        super(parent, 0);
    }
    
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, 0);
        
        
        button.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
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
						stocklist.removeStock(c);
						button.setVisible(false);
						Utils.refreshTable(table);												
								 
						break;
					}
				}
			}
		});
        button.setFont(parent.getFont());
        button.setText("É¾³ý");
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
