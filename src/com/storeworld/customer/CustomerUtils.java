package com.storeworld.customer;

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
    			if(type.equals(AREA)){
    				CustomerFilter.setArea(button.getText(), AREA);
    			}else if(type.equals(FIRSTNAME)){
    				CustomerFilter.setFirstName(button.getText(), FIRSTNAME);
    			}
    			tv.addFilter(cf);
    		}
    		else{
    			if(type.equals(AREA)){
    				CustomerFilter.setArea("", AREA);
    			}else if(type.equals(FIRSTNAME)){
    				CustomerFilter.setFirstName("", FIRSTNAME);
    			}
    			tv.removeFilter(cf);
    		}
    		Utils.refreshTable(tv.getTable());
    	}
	}

	
	//check box list of area
	private static HashSet<String> areas = new HashSet<String>();
	//check box list of firstname
	private static HashSet<String> firstnames = new HashSet<String>();
	
	//check box list of area
	private static HashSet<Button> areaButtons = new HashSet<Button>();
	//check box list of firstname
	private static HashSet<Button> firstnameButtons = new HashSet<Button>();
	
		
	/**
	 * need a cache or not? determine this by processing time
	 */
	private static void getAreasFromDataBase(){
		//call the database to full fill the areas list
		areas.add("八里街");
		areas.add("安陆");
	}
	
	private static void getFirstNamesFromDataBase(){
		//call the database to full fill the firstname list
		firstnames.add("老");
		firstnames.add("小");
		firstnames.add("胡");
		firstnames.add("李");
	}
	
	/**
	 * show the area check boxes
	 * @param composite_ar
	 * @param width (int)(4*w/5/10)
	 * @param composite_scrollarea
	 */
	public static void showAreaCheckBoxes(Composite composite_ar, int width, ScrolledComposite composite_scrollarea, TableViewer tv, Color base){
		
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
	
	/**
	 * show the firstname check boxes
	 * @param composite_ar
	 * @param width (int)(4*w/5/10)
	 * @param composite_scrollarea
	 * @param base the parent color of the button
	 */
	public static void showFirstNameCheckBoxes(Composite composite_ar, int width, ScrolledComposite composite_scrollarea, TableViewer tv, Color base){
		
		firstnames.clear();
		getFirstNamesFromDataBase();
		
		for (String area : firstnames) {
			Button button = new Button(composite_ar, SWT.CHECK);
			GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd.widthHint = width;
			button.setLayoutData(gd);
			button.setText(area);
			button.setBackground(base);
			button.addSelectionListener(new CheckBoxFilterAdapter(button, FIRSTNAME, cf, tv)); 			
			//add the Button into the button list
			firstnameButtons.add(button);
			composite_scrollarea.setMinSize(composite_ar.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));
			composite_ar.layout();
		}
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
