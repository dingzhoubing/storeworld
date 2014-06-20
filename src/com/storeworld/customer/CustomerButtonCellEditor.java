package com.storeworld.customer;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
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
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.CustomerInfoService;
import com.storeworld.utils.Utils;
/**
 * the remove button in customer page
 * @author dingyuanxiong
 *
 */
public class CustomerButtonCellEditor extends CellEditor {

    protected Button button;
    protected Table table;
    protected CustomerList customerlist;
    protected int rowHeight = 0;
    private static TableEditor editor = null;
    private static final int deliverButtonColumn = 1;
    private static CustomerInfoService customerinfo = new CustomerInfoService();
    public CustomerButtonCellEditor() {
        setStyle(0);
    }

    
    public CustomerButtonCellEditor(Composite parent, CustomerList customerlist, int rowHeight) {
        this(parent, 0);
        this.table = (Table)parent;
        this.customerlist = customerlist;
        this.rowHeight = rowHeight;
        editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.CENTER;
	    editor.grabHorizontal = true;	
    }

    public CustomerButtonCellEditor(Composite parent, int style) {
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
						Customer c = (Customer)(table.getItem(index).getData());	
						
						ReturnObject ret = customerinfo.queryCustomerInfoByID(c.getID());
						Pagination page = (Pagination) ret.getReturnDTO();
						List<Object> list = page.getItems();
						CustomerInfoDTO cDTO = (CustomerInfoDTO) list.get(0);
						String old_area = cDTO.getCustomer_area();
						String old_name = cDTO.getCustomer_name();
						String old_tele = cDTO.getTelephone();
						String old_addr = cDTO.getCustomer_addr();
												
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
				    	messageBox.setMessage(String.format("片区:%s， 客户:%s 将不在统计中出现，同时将不出现于送货表中，确定删除？",
				    			old_area, old_name));		    		    	
				    	if (messageBox.open() == SWT.OK){
						CellEditor[] cellEditor = CustomerContentPart.getCellEditor();
						editor.setEditor(cellEditor[deliverButtonColumn].getControl(), table.getItem(index), deliverButtonColumn);
						if(!editor.getEditor().isDisposed()){
							editor.getEditor().setVisible(false);//false
						}
						customerlist.removeCustomer(c);
						button.setVisible(false);
						Utils.refreshTable(table);											
								 
						break;
				    	}else{
				    		//reset the customer info
				    		Customer cus = new Customer();
				    		cus.setID(c.getID());//new row in fact
				    		cus.setArea(old_area);
				    		cus.setName(old_name);
				    		cus.setPhone(old_tele);
				    		cus.setAddress(old_addr);
	    					CustomerCellModifier.getCustomerList().customerChangedThree(cus);
				    	}
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
