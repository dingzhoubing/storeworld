package com.storeworld.product;

import java.util.List;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.GoodsInfoService;
import com.storeworld.utils.Utils;

/**
 * the remove button on product table
 * once hover on a row of the table, show the button
 * @author dingyuanxiong
 *
 */
public class ProductButtonCellEditor extends CellEditor {

    protected Button button;
    protected Table table;
    protected ProductList productlist;
    protected int rowHeight = 0;
    private static GoodsInfoService goodsinfo = new GoodsInfoService();
    
    public ProductButtonCellEditor() {
        setStyle(0);
    }

    
    public ProductButtonCellEditor(Composite parent, ProductList productlist, int rowHeight) {
        this(parent, 0);
        this.table = (Table)parent;
        this.productlist = productlist;
        this.rowHeight = rowHeight;
    }

    public ProductButtonCellEditor(Composite parent, int style) {
        super(parent, 0);
    }
    
	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, 0);
        
        
        button.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	button.setVisible(false);
            	ProductButtonCellEditor.this.focusLost();
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
						Product p = (Product)(table.getItem(index).getData());	
						
						//we query if we met some blank item
						ReturnObject ret = goodsinfo.queryProductInfoByID(p.getID());
						Pagination page = (Pagination) ret.getReturnDTO();
						List<Object> list = page.getItems();
						GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(0);
						String old_brand = cDTO.getBrand();
						String old_sub = cDTO.getSub_brand();
						String old_size = cDTO.getStandard();
						String old_unit = cDTO.getUnit();
						String old_repo = cDTO.getRepertory();
						
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
				    	messageBox.setMessage(String.format("品牌:%s， 子品牌:%s, 规格:%s 将不在统计中出现，同时将不出现于进货，送货表中，确定删除？",
				    			old_brand, old_sub, old_size));		    		    	
				    	if (messageBox.open() == SWT.OK){ 
				    		productlist.removeProduct(p);
				    		button.setVisible(false);
				    		Utils.refreshTable(table);																			 
				    		break;
				    	}else{
				    		Product s = new Product();
	    					s.setID(p.getID());//new row in fact
	    					s.setBrand(old_brand);
	    					s.setSubBrand(old_sub);
	    					s.setSize(old_size);
	    					s.setUnit(old_unit);	
	    					s.setRepository(old_repo);
//	    					s.setTime(time);time and indeed?
	    					ProductCellModifier.getProductList().productChangedThree(s);
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
