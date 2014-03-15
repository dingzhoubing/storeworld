package com.storeworld.product;

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

public class ButtonCellEditor extends CellEditor {

    protected Button button;
    protected Table table;
    protected ProductList productlist;
    protected int rowHeight = 0;
    
//    private static final int defaultStyle = SWT.SINGLE;

    
    public ButtonCellEditor() {
        setStyle(0);
    }

    
    public ButtonCellEditor(Composite parent, ProductList productlist, int rowHeight) {
        this(parent, 0);
        this.table = (Table)parent;
        this.productlist = productlist;
        this.rowHeight = rowHeight;
    }

    public ButtonCellEditor(Composite parent, int style) {
        super(parent, 0);
    }
    
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, 0);
        
        
        button.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	button.setVisible(false);
            	ButtonCellEditor.this.focusLost();
            }
        });
        button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int ptY = button.getBounds().y+1;//make it always not equals the up one
//				System.out.println("button y: "+ptY);
				int rowCount = table.getItemCount();				
				int index = table.getTopIndex();	
//				System.out.println("topIndex: "+index);
				for (; index < rowCount; index++) {
					TableItem item = table.getItem(index);
					int rowY = item.getBounds().y;						
					if (rowY <= ptY && ptY <= (rowY+rowHeight)) {//ptY <= (rowY+rowHeight) no use now
						Product p = (Product)(table.getItem(index).getData());		
//						System.out.println(p.toString());
//						System.out.println("row y: "+ptY);
//						System.out.println("row: "+(index));
						productlist.removeProduct(p);
						button.setVisible(false);
						ProductContentPart.refreshTable();												
								 
						break;
					}
				}
			}
		});
        button.setFont(parent.getFont());
        button.setText("ɾ��");
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