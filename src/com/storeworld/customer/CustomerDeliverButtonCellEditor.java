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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.deliver.DeliverList;
import com.storeworld.deliver.DeliverUtils;
import com.storeworld.mainui.CoolBarPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.utils.UIDataConnector;
import com.storeworld.utils.Utils;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.FUNCTION;
import com.storeworld.utils.Constants.NORTH_TYPE;
/**
 * the deliver button in customer page
 * try to abstract all the ButtonCellEditor
 * @author dingyuanxiong
 *
 */
public class CustomerDeliverButtonCellEditor extends CellEditor {

    protected Button button;
    protected Table table;
    protected CustomerList customerlist;
    protected int rowHeight = 0;
    
    public CustomerDeliverButtonCellEditor() {
        setStyle(0);
    }

    
    public CustomerDeliverButtonCellEditor(Composite parent, CustomerList customerlist, int rowHeight) {
        this(parent, 0);
        this.table = (Table)parent;
        this.customerlist = customerlist;
        this.rowHeight = rowHeight;
    }

    public CustomerDeliverButtonCellEditor(Composite parent, int style) {
        super(parent, 0);
    }
    
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, 0);
//        button.setFocus();
//        button.setVisible(true);
        
        button.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	button.setVisible(false);//false
            	CustomerDeliverButtonCellEditor.this.focusLost();
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
						//record some info
						System.out.println("jump into the deliver page");
						//record the customer info
						Customer c = (Customer)(table.getItem(index).getData());
						//current deliver list from customer page
						UIDataConnector.setFromCustomer(true);
						UIDataConnector.setCustomerRecord(c);//record the customer
						
						//jump
						Utils.setFunctin(FUNCTION.DELIVER);						
						MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
						if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)
							shell.setNorthPart(new CoolBarPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
						if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_DELIVER) == null)
							shell.setContentPart(new DeliverContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_DELIVER);
						shell.show_North_index();
						Utils.setFunctinLast(FUNCTION.DELIVER);
						shell.show_Content_deliver();
						
						//show the basic message
						DeliverUtils.setOrderNumber();//set the order number for the deliver table
						
						DeliverContentPart.clearContent();
						DeliverContentPart.getTableViewer().getTable().removeAll();
						DeliverList.removeAllDelivers();
						
						DeliverContentPart.enableEditContent();
						DeliverContentPart.makeHistoryUnEditable();
						DeliverUtils.setTime(null);
						DeliverUtils.setStatus("NEW");
						
						DeliverContentPart.setTextOrderNumber(DeliverUtils.getOrderNumber());						
						String time = DeliverUtils.getTime();
						String year = time.substring(0, 4);
						String month = time.substring(4, 6);
						String day = time.substring(6, 8);
						String hour = time.substring(8, 10);
						String min = time.substring(10, 12);
						time = year+"-"+month+"-"+day+" "+hour+":"+min;
						DeliverContentPart.setTime(time);
						DeliverContentPart.setCommon(c.getArea(), c.getName(), c.getPhone(), c.getAddress());
												
						button.setVisible(false);
//						Utils.refreshTable(table);											
								 												
						break;
					}
				}
			}
		});
        button.setFont(parent.getFont());
        button.setText("ËÍ»õ");
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
