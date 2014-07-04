package com.storeworld.deliver;

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
 * the remove button on a deliver table
 * once hover on a row of a table
 * @author dingyuanxiong
 *
 */
public class DeliverButtonCellEditor extends CellEditor {

    protected Button button;
    protected Table table;
    protected DeliverList deliverlist;
    protected int rowHeight = 0;
    
    public DeliverButtonCellEditor() {
        setStyle(0);
    }

    
    public DeliverButtonCellEditor(Composite parent, DeliverList productlist, int rowHeight) {
        this(parent, 0);
        this.table = (Table)parent;
        this.deliverlist = productlist;
        this.rowHeight = rowHeight;
    }

    public DeliverButtonCellEditor(Composite parent, int style) {
        super(parent, 0);
    }
    
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, 0);
        
        
        button.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	button.setVisible(false);
            	DeliverButtonCellEditor.this.focusLost();
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
						Deliver c = (Deliver)(table.getItem(index).getData());		
						//delete the deliver from table & database
						try {
							deliverlist.removeDeliver(c);
						} catch (Exception e1) {
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("É¾³ýËÍ»õÐÅÏ¢Ê§°Ü£¬ ÇëÖØÊÔ");
							mbox.open();
							return;
						}
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
