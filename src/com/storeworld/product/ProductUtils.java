package com.storeworld.product;

import java.util.HashSet;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.storeworld.customer.CustomerContentPart;
import com.storeworld.customer.CustomerFilter;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.Utils;

public class ProductUtils {
	
	private static final String BRAND = "brand";
	private static String newLineID = "";
	private static ProductFilter pf = new ProductFilter();
	private static boolean firstTime = true;
	//record if clicked the search button
	private static boolean searchButtonClicked = false;
	//record the useful component in customer page
	private static Composite composite_ar_record; 
	private static int width_record;
	private static ScrolledComposite composite_scrollarea_record; 	
	private static Composite composite_fm_record; 	
	private static ScrolledComposite composite_scrollfm_record; 	
	private static TableViewer tv_record;
	private static Color base_record;
	//record the brands of products info at first time initial the page
	private static HashSet<String> brands = new HashSet<String>();
	//if the we change the table, we count the new brand list
	private static HashSet<String> brands_current = new HashSet<String>();
	//check box list of brand
	private static HashSet<Button> brandButtons = new HashSet<Button>();
	
	
	static class CheckBoxFilterAdapter extends SelectionAdapter{
		
		Button button;    //which button 
		String type;      //area or firstname
		ProductFilter pf;//filter type
		TableViewer tv;   //tableviewer
		
		CheckBoxFilterAdapter(Button b, String type, ProductFilter pf, TableViewer tv){
			this.button = b;
			this.type = type;
			this.pf = pf;
			this.tv = tv;
		}
		
		@Override
    	public void widgetSelected(SelectionEvent e) {
//			resetVisibleLine();
    		if(button.getSelection()){					
					if (type.equals(BRAND)) {
						ProductFilter.setBrand(button.getText(), BRAND);
					} 
					tv.addFilter(pf);
    		}
    		else{
    			if(type.equals(BRAND)){
    				ProductFilter.removeBrand(button.getText(), BRAND);
    			}
    			tv.addFilter(pf);
    		}
    		//set invisible of the button do not in current table
    		TableEditor editorDel = CustomerContentPart.getEditorDel();    		
    		if(editorDel!=null && editorDel.getEditor()!=null){
    			if(!editorDel.getEditor().isDisposed()){
    				editorDel.getEditor().setVisible(false);
    			}
    		}
    		Utils.refreshTable(tv.getTable());
    	}
	}

	public static void setSearchButtonClicked(boolean clicked){
		searchButtonClicked = clicked;
	}
	public static boolean getSearchButtonClicked(){
		return searchButtonClicked;
	}	
	
	
	/**
	 * set the initial new ID of the table
	 * @param id
	 */
	public static void setNewLineID(String id){
		newLineID = id;
	}
	
	public static String getNewLineID(){
		return newLineID;
	}
	
	public static void refreshTableData(){
		//if there is no filter box
//		ProductContentPart.getTableViewer().addFilter(pf);
//		ProductContentPart.getTableViewer().removeFilter(pf);
//		Utils.refreshTable(ProductContentPart.getTableViewer().getTable());
//		ProductFilter.resetIsFirst();
		for(Button btn : brandButtons){
			ProductFilter.setBrand(btn.getText(), BRAND);
			ProductContentPart.getTableViewer().addFilter(pf);
			ProductFilter.removeBrand(btn.getText(), BRAND);
			ProductContentPart.getTableViewer().addFilter(pf);
			Utils.refreshTable(ProductContentPart.getTableViewer().getTable());
			break;
		}	
	}
	
	
	public static void refreshBrands(){		
		brands_current.clear();		
		brands_current.addAll(DataCachePool.getBrand2Sub().keySet());		
		showBrandCheckBoxes(composite_ar_record,width_record,composite_scrollarea_record,tv_record,base_record);		
	}
	
	public static void showAllProducts() {
		for (Button btn : brandButtons) {
			btn.setSelection(false);
			ProductFilter.removeBrand(btn.getText(), BRAND);
		}
		
		tv_record.addFilter(pf);
		Utils.refreshTable(tv_record.getTable());
	}
	
	/**
	 * show the result if user click the search button
	 */
	public static void showSearchedProducts(){

		for(Button btn : brandButtons){
			btn.setSelection(false);
		}
		tv_record.addFilter(pf);
		Utils.refreshTable(tv_record.getTable());
		//after show the searched result, make the seatch button clear 
		setSearchButtonClicked(false);		
		
	}
	
	private static void getBrandsFromDataBase(){	
		brands.addAll(DataCachePool.getBrand2Sub().keySet());
	}
	
	public static void showBrandCheckBoxes(Composite composite_ar, int width, ScrolledComposite composite_scrollarea, TableViewer tv, Color base){
		//if first time at initial stage, show the brands of "brands"
		if(firstTime){
			composite_ar_record = composite_ar;
			width_record = width;
			composite_scrollarea_record = composite_scrollarea;
			tv_record = tv;
			base_record = base;
			brands.clear();
			firstTime = false;
			getBrandsFromDataBase();
			for (String brand : brands) {				
				Button button = new Button(composite_ar, SWT.CHECK);
				GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				gd.widthHint = width;
				button.setLayoutData(gd);
				button.setText(brand);
				button.setBackground(base);
				button.addSelectionListener(new CheckBoxFilterAdapter(button, BRAND, pf, tv)); 			
				//add the Button into the button list
				brandButtons.add(button);
				composite_scrollarea.setMinSize(composite_ar.computeSize(SWT.DEFAULT,
						SWT.DEFAULT));
				composite_ar.layout();
			}
		}
		//if not the first time, we need to check if the brands_current changed, not the same as brands
		else{
		for (String brand : brands_current) {
			//if not in brands, means new
			if (!brands.contains(brand)) {
				Button button = new Button(composite_ar, SWT.CHECK);
				GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false,
						1, 1);
				gd.widthHint = width;
				button.setLayoutData(gd);
				button.setText(brand);
				button.setBackground(base);
				button.addSelectionListener(new CheckBoxFilterAdapter(button,
						BRAND, pf, tv));
				// add the Button into the button list
				brandButtons.add(button);
				composite_scrollarea.setMinSize(composite_ar.computeSize(
						SWT.DEFAULT, SWT.DEFAULT));
				composite_ar.layout();
				brands.add(brand);
			}			
		}
		//if in brands, but do not in brands_current, means we removed the brand
		//then remove the check box
		HashSet<String> tmp_remove = new HashSet<String>();
		Button btn_tmp = null;
		for (String brand : brands) {
			//if not in brands_current, means has been removed
			if (!brands_current.contains(brand)) {
				for(Button btn : brandButtons){
					if(btn.getText().equals(brand)){						
						btn_tmp = btn;
						
						break;
					}
				}
				tmp_remove.add(brand);
				break;
			}
		}
		brands.removeAll(tmp_remove);
		if(btn_tmp != null){
			brandButtons.remove(btn_tmp);
			btn_tmp.dispose();
			composite_scrollarea.setMinSize(composite_ar.computeSize(
				SWT.DEFAULT, SWT.DEFAULT));
			composite_ar.layout();
		}
		}		
	}
	
	
}
