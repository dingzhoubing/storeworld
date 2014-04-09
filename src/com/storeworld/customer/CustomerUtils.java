package com.storeworld.customer;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.storeworld.common.DataInTable;
import com.storeworld.utils.Utils;

/**
 * the utils class of customer page
 * @author dingyuanxiong
 *
 */
public class CustomerUtils {
	
	private static final String AREA = "area";
	private static final String FIRSTNAME = "firstname";
	//when initial the table, set the new line id
	private static String newLineID = "";
	private static CustomerFilter cf = new CustomerFilter();
	
	/**
	 * 
	 * @author dingyuanxiong
	 *
	 */
	static class CheckBoxFilterAdapter extends SelectionAdapter{
		
		Button button;//which button 
		String type; //area or firstname
		CustomerFilter cf;//filter type
		TableViewer tv; //tableviewer
		
		CheckBoxFilterAdapter(Button b, String type, CustomerFilter cf, TableViewer tv){
			this.button = b;
			this.type = type;
			this.cf = cf;
			this.tv = tv;
		}
		
		@Override
    	public void widgetSelected(SelectionEvent e) {

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
//    			tv.removeFilter(cf);
    		}
    		Utils.refreshTable(tv.getTable());
    	}
	}

	
	//check box list of area
	private static HashSet<String> areas = new HashSet<String>();
	//check box list of firstname
	private static HashSet<String> firstnames = new HashSet<String>();
	
	//if the we change the table, we change the filter
	private static HashSet<String> areas_current = new HashSet<String>();
	//if the we change the table, we change the filter
	private static HashSet<String> firstnames_current = new HashSet<String>();
		
	//check box list of area
	private static HashSet<Button> areaButtons = new HashSet<Button>();
	//check box list of firstname
	private static HashSet<Button> firstnameButtons = new HashSet<Button>();
	
	//the class is Damn now, a mass....
	private static Composite composite_ar_record; 
	private static int width_record;
	private static ScrolledComposite composite_scrollarea_record; 
	
	private static Composite composite_fm_record; 	
	private static ScrolledComposite composite_scrollfm_record; 
	
	private static TableViewer tv_record;
	private static Color base_record;
	private static boolean firstTime = true;
	
	/**
	 * fill the areas_current, and call the showXX
	 */
	public static void refreshAreas_FirstName(){
		areas_current.clear();
		firstnames_current.clear();
		for(DataInTable dt : CustomerList.getCustomerList()){
			Customer cus = (Customer)dt;
			//there is an empty row,
			//TODO:if the user change the row to make the row illegal, prevent the action
			if(!CustomerValidator.checkID(cus.getID())){
				//initial the two set
				areas_current.add(cus.getArea());
				String fm = cus.getName().substring(0, 1);
				firstnames_current.add(fm);
			}
		}
		showAreaCheckBoxes(composite_ar_record,width_record,composite_scrollarea_record,tv_record,base_record);
		showFirstNameCheckBoxes(composite_fm_record,width_record,composite_scrollfm_record,tv_record,base_record);
	}
	
	/**
	 * need a cache or not? determine this by processing time
	 */
	private static void getAreasFromDataBase(){
		//call the database to full fill the areas list
//		areas.add("八里街");
//		areas.add("安陆");
		for(DataInTable dt : CustomerList.getCustomerList()){
			Customer cus = (Customer)dt;
			//there is an empty row
			if(CustomerValidator.rowLegal(cus))
				areas.add(cus.getArea());
		}
	}
	
	//get the customers of a specified area
	private static ArrayList<String> customersOfArea(String area){
		ArrayList<String> customers = new ArrayList<String>();
		return customers;
	}
	
	private static void getFirstNamesFromDataBase(){
		//call the database to full fill the firstname list
//		firstnames.add("老");
//		firstnames.add("小");
//		firstnames.add("胡");
//		firstnames.add("李");
		for(DataInTable dt : CustomerList.getCustomerList()){
			Customer cus = (Customer)dt;
			//the fist character, and remove the new line
			if(CustomerValidator.rowLegal(cus)){
				String fm = cus.getName().substring(0, 1);
				firstnames.add(fm);
			}
		}
	}
	
	/**
	 * show the area check boxes
	 * @param composite_ar
	 * @param width (int)(4*w/5/10)
	 * @param composite_scrollarea
	 */
	public static void showAreaCheckBoxes(Composite composite_ar, int width, ScrolledComposite composite_scrollarea, TableViewer tv, Color base){
		if(firstTime){
			composite_ar_record = composite_ar;
			width_record = width;
			composite_scrollarea_record = composite_scrollarea;
			tv_record = tv;
			base_record = base;
//			firstTime = false;
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
		
		System.out.println("areas: "+areas);
	}
	
	/**
	 * show the firstname check boxes
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
		System.out.println("firstnames: "+firstnames);
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
	

}
