package com.storeworld.customer;

import java.util.HashSet;

import org.eclipse.jface.viewers.CellEditor;
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
import org.eclipse.swt.widgets.Table;

import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.Utils;

/**
 * the Util class of customer page
 * @author dingyuanxiong
 *
 */
public class CustomerUtils {
	
	private static final String AREA = "area";
	private static final String FIRSTNAME = "firstname";
	//when initial the table, set the new line id
	private static String newLineID = "";
	private static CustomerFilter cf = new CustomerFilter();
	private static boolean firstTime = true;
	//record if clicked the search button
	private static boolean searchButtonClicked = false;

	/**
	 * inner class to add a listener for each checkbox of customer left navigator
	 */
	static class CheckBoxFilterAdapter extends SelectionAdapter{
		
		Button button;    //which button 
		String type;      //area or firstname
		CustomerFilter cf;//filter type
		TableViewer tv;   //tableviewer
		
		CheckBoxFilterAdapter(Button b, String type, CustomerFilter cf, TableViewer tv){
			this.button = b;
			this.type = type;
			this.cf = cf;
			this.tv = tv;
		}
		
		@Override
    	public void widgetSelected(SelectionEvent e) {
//			resetVisibleLine();
    		if(button.getSelection()){					
					if (type.equals(AREA)) {
						CustomerFilter.setArea(button.getText(), AREA);
					} else if (type.equals(FIRSTNAME)) {
						CustomerFilter
								.setFirstName(button.getText(), FIRSTNAME);
					}
					tv.addFilter(cf);
    		}
    		else{
    			if(type.equals(AREA)){
    				CustomerFilter.removeArea(button.getText(), AREA);
    			}else if(type.equals(FIRSTNAME)){
    				CustomerFilter.removeFirstName(button.getText(), FIRSTNAME);
    			}
    			tv.addFilter(cf);
    		}

    		TableEditor editor = CustomerContentPart.getEditor();
    		TableEditor editorDel = CustomerContentPart.getEditorDel();
    		if(editor!=null && editor.getEditor()!=null){
    			if(!editor.getEditor().isDisposed()){
    				editor.getEditor().setVisible(false);
    			}
    		}
    		if(editorDel!=null && editorDel.getEditor()!=null){
    			if(!editorDel.getEditor().isDisposed()){
    				editorDel.getEditor().setVisible(false);
    			}
    		}
    		Utils.refreshTable(tv.getTable());
    	}
	}

	
	//record the areas of customers info at first time initial the page
	private static HashSet<String> areas = new HashSet<String>();
	//record the first names of customers info at first time initial the page
	private static HashSet<String> firstnames = new HashSet<String>();
	
	//if the we change the table, we count the new areas list
	private static HashSet<String> areas_current = new HashSet<String>();
	//if the we change the table, we count the new first name list
	private static HashSet<String> firstnames_current = new HashSet<String>();
		
	//check box list of area
	private static HashSet<Button> areaButtons = new HashSet<Button>();
	//check box list of firstname
	private static HashSet<Button> firstnameButtons = new HashSet<Button>();
	
	//record the useful component in customer page
	private static Composite composite_ar_record; 
	private static int width_record;
	private static ScrolledComposite composite_scrollarea_record; 	
	private static Composite composite_fm_record; 	
	private static ScrolledComposite composite_scrollfm_record; 	
	private static TableViewer tv_record;
	private static Color base_record;

	public static void setSearchButtonClicked(boolean clicked){
		searchButtonClicked = clicked;
	}
	public static boolean getSearchButtonClicked(){
		return searchButtonClicked;
	}	
	/**
	 * refresh the left navigator of customer page
	 * 1. reset the values in areas list and first name list
	 * 2. refresh the left navigator panel
	 */
	public static void refreshAreas_FirstName(){
		
		areas_current.clear();
		firstnames_current.clear();
		//1. we can parse the data from customer list
//		for(DataInTable dt : CustomerList.getCustomers()){
//			Customer cus = (Customer)dt;
//			//there is an empty row,
//			//TODO:if the user change the row to make the row illegal, prevent the action
//			if(!CustomerValidator.checkID(cus.getID())){
//				//initial the two set
//				areas_current.add(cus.getArea());
//				String fm = cus.getName().substring(0, 1);
//				firstnames_current.add(fm);
//			}
//		}
		//2. directly get it from the DataCachePool
		//by this way, we do not need to judge if the row is empty, all data in cache is valid
		areas_current.addAll(DataCachePool.getArea2Names().keySet());
		for(String area: areas_current){
			HashSet<String> names = DataCachePool.getArea2Names().get(area);
			for(String name : names)
				firstnames_current.add(name.substring(0, 1));
		}
		
		showAreaCheckBoxes(composite_ar_record,width_record,composite_scrollarea_record,tv_record,base_record);
		showFirstNameCheckBoxes(composite_fm_record,width_record,composite_scrollfm_record,tv_record,base_record);
		
	}	
	
	/**
	 * if click the all link, show all the customers
	 */
	public static void showAllCustomers(){
		for(Button btn : areaButtons){
			btn.setSelection(false);
			CustomerFilter.removeArea(btn.getText(), AREA);
		}
		for(Button btn : firstnameButtons){
			btn.setSelection(false);
			CustomerFilter.removeFirstName(btn.getText(), FIRSTNAME);
		}
		tv_record.addFilter(cf);
		Utils.refreshTable(tv_record.getTable());
	}
	
	/**
	 * show the result if user click the search button
	 */
	public static void showSearchedCustomers(){

		for(Button btn : areaButtons){
			btn.setSelection(false);
		}
		for(Button btn : firstnameButtons){
			btn.setSelection(false);
		}
		tv_record.addFilter(cf);
		Utils.refreshTable(tv_record.getTable());
		//after show the searched result, make the seatch button clear 
		setSearchButtonClicked(false);		
		
	}
	
	/**
	 * if the user select do not use the soft keyboard, we need to do this
	 * to make the text cell editor works better
	 */
	public static void refreshTableData(){
		for(Button btn : areaButtons){
			CustomerFilter.setArea(btn.getText(), AREA);
			CustomerContentPart.getTableViewer().addFilter(cf);
			CustomerFilter.removeArea(btn.getText(), AREA);
			CustomerContentPart.getTableViewer().addFilter(cf);
			Utils.refreshTable(CustomerContentPart.getTableViewer().getTable());
			break;
		}		
	}
	
	/**
	 * get the areas from the cache
	 */
	private static void getAreasFromDataBase(){
		//just get if from cache
		//1. parse data from customer list		
//		for(DataInTable dt : CustomerList.getCustomers()){
//			Customer cus = (Customer)dt;
//			//there is an empty row
//			if(CustomerValidator.rowLegal(cus))
//				areas.add(cus.getArea());
//		}
		//2. directly get it from cache
		areas.addAll(DataCachePool.getArea2Names().keySet());
	}
	

	/**
	 * get all the first names from cache
	 */
	private static void getFirstNamesFromDataBase(){
		//just get it from cache
		//1. parse the data from customer list
//		for(DataInTable dt : CustomerList.getCustomers()){
//			Customer cus = (Customer)dt;
//			//the fist character, and remove the new line
//			if(CustomerValidator.rowLegal(cus)){
//				String fm = cus.getName().substring(0, 1);
//				firstnames.add(fm);
//			}
//		}
		//2. directly get it from cache
		for(String area: DataCachePool.getArea2Names().keySet()){
			HashSet<String> names = DataCachePool.getArea2Names().get(area);
			for(String name : names)
				firstnames.add(name.substring(0, 1));
		}
	}
	
	/**
	 * show the area check boxes
	 * @param composite_ar
	 * @param width (int)(4*w/5/10)
	 * @param composite_scrollarea
	 */
	public static void showAreaCheckBoxes(Composite composite_ar, int width, ScrolledComposite composite_scrollarea, TableViewer tv, Color base){
		//if first time at initial stage, show the areas of "areas"
		if(firstTime){
			composite_ar_record = composite_ar;
			width_record = width;
			composite_scrollarea_record = composite_scrollarea;
			tv_record = tv;
			base_record = base;
			areas.clear();
			getAreasFromDataBase();
			for (String area : areas) {				
				Button button = new Button(composite_ar, SWT.CHECK);
				GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				gd.widthHint = width;
				button.setLayoutData(gd);
				button.setText(area);
				button.setBackground(base);
				button.addSelectionListener(new CheckBoxFilterAdapter(button, AREA, cf, tv)); 			
				//add the Button into the button list
				areaButtons.add(button);
				composite_scrollarea.setMinSize(composite_ar.computeSize(SWT.DEFAULT,
						SWT.DEFAULT));
				composite_ar.layout();
			}
		}
		//if not the first time, we need to check if the areas_current changed, not the same as areas
		else{
		for (String area : areas_current) {
			//if not in areas, means new
			if (!areas.contains(area)) {
				Button button = new Button(composite_ar, SWT.CHECK);
				GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false,
						1, 1);
				gd.widthHint = width;
				button.setLayoutData(gd);
				button.setText(area);
				button.setBackground(base);
				button.addSelectionListener(new CheckBoxFilterAdapter(button,
						AREA, cf, tv));
				// add the Button into the button list
				areaButtons.add(button);
				composite_scrollarea.setMinSize(composite_ar.computeSize(
						SWT.DEFAULT, SWT.DEFAULT));
				composite_ar.layout();
				areas.add(area);
			}			
		}
		//if in areas, but do not in areas_current, means we removed the area
		//then remove the check box
		HashSet<String> tmp_remove = new HashSet<String>();
		Button btn_tmp = null;
		for (String area : areas) {
			//if not in areas_current, means has been removed
			if (!areas_current.contains(area)) {
				for(Button btn : areaButtons){
					if(btn.getText().equals(area)){						
						btn_tmp = btn;
						
						break;
					}
				}
				tmp_remove.add(area);
				break;
			}
		}
		areas.removeAll(tmp_remove);
		if(btn_tmp != null){
			areaButtons.remove(btn_tmp);
			btn_tmp.dispose();
			composite_scrollarea.setMinSize(composite_ar.computeSize(
				SWT.DEFAULT, SWT.DEFAULT));
			composite_ar.layout();
		}
		}		
//		System.out.println("areas: "+areas);
	}
	
	/**
	 * show the firstname check boxes,
	 * the logic is quite the same as the area part
	 * @param composite_ar
	 * @param width (int)(4*w/5/10)
	 * @param composite_scrollarea
	 * @param base the parent color of the button
	 */	
	public static void showFirstNameCheckBoxes(Composite composite_ar, int width, ScrolledComposite composite_scrollarea, TableViewer tv, Color base){		
		if(firstTime){
			composite_fm_record = composite_ar;
			composite_scrollfm_record = composite_scrollarea;
			firstnames.clear();
			getFirstNamesFromDataBase();
			firstTime = false;
			for (String fm : firstnames) {
				Button button = new Button(composite_ar, SWT.CHECK);
				GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				gd.widthHint = width;
				button.setLayoutData(gd);
				button.setText(fm);
				button.setBackground(base);
				button.addSelectionListener(new CheckBoxFilterAdapter(button, FIRSTNAME, cf, tv)); 			
				//add the Button into the button list
				firstnameButtons.add(button);
				composite_scrollarea.setMinSize(composite_ar.computeSize(SWT.DEFAULT,
						SWT.DEFAULT));
				composite_ar.layout();
			}
		}
		else{
			for (String fm : firstnames_current) {
				//if not in areas, means new
				if (!firstnames.contains(fm)) {
					Button button = new Button(composite_ar, SWT.CHECK);
					GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false,
							1, 1);
					gd.widthHint = width;
					button.setLayoutData(gd);
					button.setText(fm);
					button.setBackground(base);
					button.addSelectionListener(new CheckBoxFilterAdapter(button,
							FIRSTNAME, cf, tv));
					// add the Button into the button list
					firstnameButtons.add(button);
					composite_scrollarea.setMinSize(composite_ar.computeSize(
							SWT.DEFAULT, SWT.DEFAULT));
					composite_ar.layout();
					firstnames.add(fm);
				}			
			}
			HashSet<String> tmp_remove = new HashSet<String>();
			Button btn_tmp = null;
			for (String fm : firstnames) {
				//if not in areas_current, means has been removed
				if (!firstnames_current.contains(fm)) {
					for(Button btn : firstnameButtons){
						if(btn.getText().equals(fm)){							
							btn_tmp = btn;
							
							break;
						}
					}
					tmp_remove.add(fm);
					break;
				}
			}
			firstnames.removeAll(tmp_remove);
			if(btn_tmp!=null){
				firstnameButtons.remove(btn_tmp);
				btn_tmp.dispose();
				composite_scrollarea.setMinSize(composite_ar.computeSize(
					SWT.DEFAULT, SWT.DEFAULT));
				composite_ar.layout();
			}
		}
	}
	
	/**
	 * set the initial new ID of the table
	 * @param id
	 */
	public static void setNewLineID(String id){
		newLineID = id;
	}
	
	/**
	 * get the new ID of the table
	 * @return
	 */
	public static String getNewLineID(){
		return newLineID;
	}
	

}
