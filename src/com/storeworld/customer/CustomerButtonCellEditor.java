package com.storeworld.customer;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CustomerButtonCellEditor extends CellEditor {

    protected Button button;
    protected Table table;
    protected CustomerList productlist;
    protected int rowHeight = 0;
    
//    private static final int defaultStyle = SWT.SINGLE;

    
    public CustomerButtonCellEditor() {
        setStyle(0);
    }

    
    public CustomerButtonCellEditor(Composite parent, CustomerList productlist, int rowHeight) {
        this(parent, 0);
        this.table = (Table)parent;
        this.productlist = productlist;
        this.rowHeight = rowHeight;
    }

    public CustomerButtonCellEditor(Composite parent, int style) {
        super(parent, 0);
    }
    
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, 0);
        
        
        button.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	button.setVisible(false);
            	CustomerButtonCellEditor.this.focusLost();
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
//						Customer c = (Customer)(table.getItem(index).getData());		
//						productlist.removeCustomer(c);
						MessageBox messageBox =   
								   new MessageBox(new Shell(),   					     
								    SWT.ICON_WARNING);   
						messageBox.setMessage("跳转进货页面");   
						messageBox.open(); 
						button.setVisible(false);
						CustomerContentPart.refreshTable();												
								 
						break;
					}
				}
			}
		});
        button.setFont(parent.getFont());
        button.setText("送貨");
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
