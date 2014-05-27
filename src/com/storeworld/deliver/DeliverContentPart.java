package com.storeworld.deliver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.common.NumberConverter;
import com.storeworld.customer.CustomerContentPart;
import com.storeworld.extenddialog.ConfirmEdit;
import com.storeworld.extenddialog.SoftKeyBoard;
import com.storeworld.mainui.ContentPart;
import com.storeworld.mainui.CoolBarPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.CustomerInfoService;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.utils.ComboUtils;
import com.storeworld.utils.Constants;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.FUNCTION;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.GeneralCCombo;
import com.storeworld.utils.GeneralComboCellEditor;
import com.storeworld.utils.Utils;

/**
 * the main class of the deliver page
 * TODO: there are too many static method, not a good way, using a return Object or some else
 * to simplify it
 * @author dingyuanxiong
 *
 */
public class DeliverContentPart extends ContentPart{
	
	private static Table table;
	private static TableViewer tv;
	//the product list
	private static DeliverList deliverlist = new DeliverList();
	//the row height of the table
	final int rowHeight = 30;
	//define the cell Editor of each column
	private static CellEditor[] cellEditor = new CellEditor[8];
	private static TableEditor editor = null;
	private static TableEditor editorEdit = null;//software number keyboard
	private static TableEditor editorCombo = null;//sub brand list
	
	private Composite current = null;
	private Composite composite = null;
	//record the last hover on row number
	private static int visibleButton_last = -1;
	private int composite_shift = 0;
	private int composite_updown = 0;
	
	private int sizeColumn = 3;
	private int priceColumn = 5;
	private int numberColumn = 6;
	private int sub_brandColomn = 2;
	private static int deleteButtonColumn = 7;
	private int brandColomn = 1;
	private static GeneralComboCellEditor<String> comboboxCellEditor = null;
	private static GeneralComboCellEditor<String> comboboxCellEditor2 = null;
	private static GeneralComboCellEditor<String> comboboxCellEditor3 = null;//size
	
	private static Button btnNewButton;
	private static Button btn_delete;
	private static Button btn_edit;//修改
	
	private static Button btn_return;//退货
	
	private static GeneralCCombo gc;
	private static GeneralCCombo gcName;
	private static Text text_phone;
	private static Text text_address;
	private static Text text_serial;
	private static Text text_time;
	private static Text total_val=null;
	private static Text indeed_val = null;
	private static Text total_big = null;
	private static Text indeed_big=null;
	
	private static Text text_title = null;
	private static Label indeed = null;
	private static Label indeed_lbl = null;
	
	private static DateTime dateTime = null;
	private static DateTime dateTime2 = null;
	
	private static ArrayList<Integer> tpShift = new ArrayList<Integer>();
	public static ArrayList<Integer> getTpShift(){
		return tpShift;
	}
	
	public DeliverContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);	
		composite = new Composite(this, SWT.NONE);	
		current = parent;		
		initialization();
		addListenerForTable();
	}
	
	/**
	 * kinds of method to control the page of deliver out of deliver page
	 * @param ordernumber
	 */
	public static void setTextOrderNumber(String ordernumber){
		text_serial.setText("");
		text_serial.setText(ordernumber);
	}
	public static String getOrderNumber(){
		return text_serial.getText().trim();
	}
	public static void setTime(String time){
		text_time.setText("");
		text_time.setText(time);
	}
	
	public static void setCommon(String area, String name, String phone, String addr){
		gc.setText(area);
		gcName.setText(name);
		text_phone.setText(phone);
		text_address.setText(addr);
	}
	
	public static void clearContent(){
		gc.clearSelection();
//		gc.removeAll();
		gcName.clearSelection();
		gc.setText("");
		gcName.setText("");
		text_phone.setText("");
		text_address.setText("");
		text_serial.setText("");
		text_time.setText("");
		total_val.setText("");
		total_big.setText("");
		indeed_val.setText("");
		indeed_big.setText("");
		//make the delete button visible = false
		for (int index=0; index < table.getItemCount(); index++) {
			editor.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(index), deleteButtonColumn);
			if(!editor.getEditor().isDisposed())
				editor.getEditor().setVisible(false);
		}
	}
	
	public static void resetInfo(){
		text_title.setText("进货单");
		indeed.setText("实收(大写)");
		indeed_lbl.setText("实收(小写)");
		
	}
	public static void enableEditContent(){
		gc.setEnabled(true);
		gcName.setEnabled(true);
		text_phone.setEnabled(true);
		text_address.setEnabled(true);
		table.setEnabled(true);
	}
	
	public static void disableEditContent(){
		//clear the context 
		gc.setText("");
		gcName.setText("");
		text_phone.setText("");
		text_address.setText("");
		text_serial.setText("");
		text_time.setText("");
		total_val.setText("");
		total_big.setText("");
		indeed_val.setText("");
		indeed_big.setText("");
		
		gc.setEnabled(false);
		gcName.setEnabled(false);
		text_phone.setEnabled(false);
		text_address.setEnabled(false);
		table.setEnabled(false);
		
		//make the delete button visible = false
		for (int index=0; index < table.getItemCount(); index++) {
			editor.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(index), deleteButtonColumn);
			if(!editor.getEditor().isDisposed())
				editor.getEditor().setVisible(false);
		}
		btnNewButton.forceFocus();
	}

	public static TableViewer getTableViewer(){
		return tv;
	}
	public static DeliverList getDeliverList(){
		return deliverlist;
	}
	
	/**
	 * get|set the total field
	 * @param total
	 */
	public static void setTotal(String total){
		total_val.setText(total);
		total_big.setText(NumberConverter.getInstance().number2CNMontrayUnit(total));
	}
	public static String getTotal(){
		return total_val.getText();
	}
	
	public static void setIndeed(String indeed){
		indeed_val.setText(indeed);
		indeed_big.setText(NumberConverter.getInstance().number2CNMontrayUnit(indeed));
	}
	public static String getIndeed(){
		return indeed_val.getText();
	}
	
	public static String getArea(){
		return gc.getText();
	}
	public static String getName(){
		return gcName.getText();
	}
	
	/**
	 * make the history editable|un-editable
	 */
	public static void makeHistoryEditable(){
		btn_delete.setVisible(false);
		btn_edit.setVisible(true);
		btn_return.setVisible(true);
	}
	public static void makeHistoryUnEditable(){
		btn_delete.setVisible(false);
		btn_edit.setVisible(false);
		btn_return.setVisible(false);
	}
	/**
	 * make the table & time picker unable to edit
	 */
	public static void makeEnable(){
		table.setEnabled(true);		
	}
	public static void makeDisable(){
		//make the delete button visible = false
		for (int index=0; index < table.getItemCount(); index++) {
			editor.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(index), deleteButtonColumn);
			if(!editor.getEditor().isDisposed())
				editor.getEditor().setVisible(false);
		}
		gc.setEnabled(false);
		gcName.setEnabled(false);
		text_phone.setEnabled(false);
		text_address.setEnabled(false);
		table.setEnabled(false);
	}
	
	public static void setCommonInfo(String area, String name, String phone, String addr, String order, String time){
		gc.setText(area);
		gcName.setText(name);
		text_phone.setText(phone);
		text_address.setText(addr);
		text_serial.setText(order);
		String year = time.substring(0, 4);
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		String hour = time.substring(8, 10);
		String min = time.substring(10, 12);
		time = year+"-"+month+"-"+day+" "+hour+":"+min;
		text_time.setText(time);
	}
	
	

	
	/**
	 * call the software keyboard
	 */
	public void callKeyBoard(Text text){
		SoftKeyBoard skb = new SoftKeyBoard(text, table.getParent().getShell(), 0, composite_shift, composite_updown);
		skb.open();
	}
	/**
	 * add all kinds of listener of the table
	 * @param event
	 */
	public void addListenerForTable(){
		
		table.addListener(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(Event event) {
				
				Point pt = new Point(event.x, event.y);
				int rowCount = table.getItemCount();
				int colCount = table.getColumnCount();
				int index = table.getTopIndex();	
				int colCurrent = -1;
				int rowCurrent = -1;
				boolean found = false;
				for (; index < rowCount; index++) {
					TableItem item = table.getItem(index);
					for(int col=0; col<colCount; col++){
						Rectangle rect = item.getBounds(col);
						if(rect.contains(pt)){	
							rowCurrent = index;
							colCurrent = col;
							found = true;
							break;
						}
					}
					if(found){
						break;
					}
				}	
				
				if(found){
					if(Utils.getUseSoftKeyBoard()){
						if(colCurrent == priceColumn || colCurrent == numberColumn){
							//cannot reuse the editor, make cause unstable
							editorEdit.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
							Text text = (Text)(editorEdit.getEditor());	
							callKeyBoard(text);
							Deliver c = (Deliver)(table.getItem(rowCurrent).getData());	
							if(colCurrent == priceColumn){
								String pricelast = c.getPrice();
								if(Utils.getClickButton() && Utils.getInputNeedChange()){
									c.setPrice(Utils.getInput());
									text.setText(c.getPrice());//validate the text
									if(DeliverValidator.validatePrice(c.getPrice()))//table, table.getItem(rowCurrent), colCurrent, 
									{
										deliverlist.deliverChanged(c);	
//										text.setText(c.getPrice());
									}else{
										c.setPrice(pricelast);
									}
									//initial the next click
									Utils.setClickButton(false);
								}
							}else if(colCurrent == numberColumn){
								String numberlast = c.getNumber();
								if(Utils.getClickButton() && Utils.getInputNeedChange()){
									c.setNumber(Utils.getInput());
									text.setText(c.getNumber());//validate the text
									if(DeliverValidator.validateNumber(c.getNumber()))//table, table.getItem(rowCurrent), colCurrent, 
									{
										deliverlist.deliverChanged(c);	
//										text.setText(c.getNumber());
									}else{
										c.setNumber(numberlast);
									}
									//initial the next click
									Utils.setClickButton(false);
								}
							}

							}
					}else{
						//do nothing
					}
					if(colCurrent == brandColomn){//sub_brand column, then fill the combox
						editorCombo.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
						GeneralCCombo combo = (GeneralCCombo)(editorCombo.getEditor());	
						DeliverUtils.setCurrentLine(rowCurrent);
						Deliver c = (Deliver)(table.getItem(rowCurrent).getData());
						DeliverUtils.setCurrentSub_Brand(c.getSubBrand());
						List<String> list = Utils.getBrands();
						//set data into objects
						comboboxCellEditor.setObjects(list);
						combo.setItems(list.toArray(new String[list.size()]));
					}
					else if(colCurrent == sub_brandColomn){//sub_brand column, then fill the combox
						editorCombo.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
						GeneralCCombo combo = (GeneralCCombo)(editorCombo.getEditor());	
						Deliver c = (Deliver)(table.getItem(rowCurrent).getData());
						String current_brand = c.getBrand();
						if( current_brand == null ||current_brand.equals("") || !Utils.checkBrand(current_brand)){
							combo.removeAll();
							c.setSubBrand("");
							deliverlist.deliverChanged(c);	
						}else{
							List<String> list = Utils.getSub_Brands(current_brand);
							//set data into objects
							comboboxCellEditor2.setObjects(list);
							combo.setItems(list.toArray(new String[list.size()]));
						}
					}else if(colCurrent == sizeColumn){
						editorCombo.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
						GeneralCCombo combo = (GeneralCCombo)(editorCombo.getEditor());	
						Deliver c = (Deliver)(table.getItem(rowCurrent).getData());
						String current_brand = c.getBrand();
						String current_sub = c.getSubBrand();
						//all this will make the size column with empty
						if(current_brand == null || current_brand.equals("") || current_sub==null || current_sub.equals("")
								|| !Utils.checkBrand(current_brand) || !Utils.checkSubBrand(current_sub)){
							combo.removeAll();
							c.setSize("");
							deliverlist.deliverChanged(c);	
						}else{
							//query the database to get the available size
							List<String> list = Utils.getSizes(current_brand, current_sub);
							//set data into objects
							comboboxCellEditor3.setObjects(list);
							combo.setItems(list.toArray(new String[list.size()]));
						}
													
					}
					else if(colCurrent == deleteButtonColumn){
						if(rowCurrent == table.getItemCount()-1){
							editor.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(rowCurrent), deleteButtonColumn);
							if(!editor.getEditor().isDisposed())
								editor.getEditor().setVisible(false);
						}
					}
					
					
				}
			}
			
		});
		
		
		//hover to show the delete button
		table.addListener(SWT.MouseHover, new Listener() {
			public void handleEvent(Event event) {
				int ptY = event.y;
				int index = table.getTopIndex();
				int row = -1;
				for (; index < table.getItemCount()-1; index++) {
					final TableItem item = table.getItem(index);
					//the width of the line maybe 0
					int rowY = item.getBounds().y;					
					if (rowY <= ptY && ptY <= (rowY+rowHeight)) {
						row = index;
						visibleButton_last = row;
						break;
					}
				}								
				if(row >= 0){			
					editor.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(row), deleteButtonColumn);
					if(!editor.getEditor().isDisposed())
						editor.getEditor().setVisible(true);
				}else{
					if(visibleButton_last >= 0 && visibleButton_last < table.getItemCount()){
						editor.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(visibleButton_last), deleteButtonColumn);
						if(!editor.getEditor().isDisposed())
							editor.getEditor().setVisible(false);
					}
				}
			}
		});
		//control the line height
		table.addListener(SWT.MeasureItem, new Listener() {
		    public void handleEvent(Event event) {
		        event.height =rowHeight;
		    }
		});
	}
	
	
	/**
	 * initialize the table elements
	 */
	public void initialization(){
		final int w = current.getBounds().width;
		final int h = current.getBounds().height;
		composite.setBounds(0, 0, w, h);
		
		//left side navigate
		final Composite composite_left = new Composite(composite, SWT.NONE);
//		final Color base = new Color(composite.getDisplay(), 255,240,245);
		final Color base = new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa);//??
		composite_left.setBackground(base);
//		composite_left.setBounds(0, 0, (int)(w/5), h);
		composite_left.setBounds(0, 0, 200, h);
		 
	    //right part		
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
//		composite_right.setBounds((int)(w/5), 0, (int)(4*w/5), h);
		composite_right.setBounds(200, 0, 760, h);
//		composite_shift = (int)(w/5);
		composite_shift = 200;
		
		//define a table
		final TableViewer tableViewer = new TableViewer(composite_right, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL);//shell, SWT.CHECK
		//add a new deliver table
		btnNewButton = new Button(composite_left, SWT.NONE);
//		btnNewButton.setBounds((int)(2*w/5/10), (int)(w/5/10/2), (int)(2*3*w/5/10), (int)(2*w/5/10));
		btnNewButton.setBounds(12, 12, 176, 36);
		btnNewButton.setText("创建送货单");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				
					DeliverUtils.setStatus("NEW");
					DeliverUtils.leaveEditMode();
//				if(!gc.getText().equals("") && !gcName.equals("")){
					DeliverUtils.setOrderNumber();//set the order number for the deliver table
				
					clearContent();
					enableEditContent();
//					disableEditContent();
					DeliverUtils.setTime(null);					
				
					table.removeAll();
					DeliverList.removeAllDelivers();
					
					text_serial.setText(DeliverUtils.getOrderNumber());
					String time = DeliverUtils.getTime();
					String year = time.substring(0, 4);
					String month = time.substring(4, 6);
					String day = time.substring(6, 8);
					String hour = time.substring(8, 10);
					String min = time.substring(10, 12);
					time = year+"-"+month+"-"+day+" "+hour+":"+min;
					text_time.setText(time);
					btn_edit.setVisible(false);
					btn_return.setVisible(false);
//				}else{
//					
//				}
			}
		});
		
		Label label = new Label(composite_left, SWT.NONE);
		label.setText(" 当月历史记录(在下方设置条件进行搜索)");
		label.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
//		label.setBackground(new Color(composite_left.getDisplay(), 240, 255, 255));
		label.setBackground(new Color(composite_left.getDisplay(), 0xe1, 0xe3, 0xe6));
//		label.setBounds(0, (int)(2*w/5/10/2)+(int)(2*w/5/10), (int)(w/5), (int)(w/5/10));
		label.setBounds(0, 62, 200, 18);
		
		
		Composite composite_2 = new Composite(composite_left, SWT.NONE);
//        composite_2.setBounds(0, (int)(2*w/5/10)+(int)(2*w/5/10), (int)(w/5), (int)(4*(h-2*w/25)/5));
//        composite_2.setBackground(new Color(composite_left.getDisplay(), 255,240,245));
		composite_2.setBounds(0, 80, 200, 410);
        composite_2.setBackground(new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa));
        composite_2.setLayout(new FillLayout());
        
		final ScrolledComposite composite_scroll = new ScrolledComposite(composite_2, SWT.V_SCROLL);//	
		composite_scroll.setExpandHorizontal(true);  
		composite_scroll.setExpandVertical(true);  
		composite_scroll.addListener(SWT.Activate, new Listener(){    
			public void handleEvent(Event e){     
				composite_scroll.forceFocus();
				}
		}); 
		final Composite composite_fn = new Composite(composite_scroll, SWT.NONE);
		composite_scroll.setContent(composite_fn);
//		composite_fn.setBackground(new Color(composite.getDisplay(), 255,240,245));
		composite_fn.setBackground(new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa));
		GridLayout layout = new GridLayout(1, false);  
        layout.numColumns = 1;  
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite_fn.setLayout(layout);
//        final ArrayList<ItemComposite> itemList = new ArrayList<ItemComposite>();
        composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        //show the deliver history
//        Color comp_color = new Color(composite_fn.getDisplay(), 204, 255, 204);
        Color comp_color = new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa);
//        DeliverUtils.showHistoryPanel(composite_scroll, composite_fn, comp_color,(int)(9*w/5/9), (int)(4*(h-2*w/25)/5/9));
        DeliverUtils.showHistoryPanel(composite_scroll, composite_fn, comp_color, 186, 50);
        composite_2.layout();
        
        
        final CCombo area = new CCombo(composite_left, SWT.BORDER|SWT.READ_ONLY);
        area.setBounds(12, 492, 86, 25);
        area.setVisibleItemCount(5);
        area.setText("全部片区");

        final CCombo cus = new CCombo(composite_left, SWT.BORDER|SWT.READ_ONLY);
        cus.setBounds(102, 492, 86, 25);
        cus.setVisibleItemCount(5);
        cus.setText("全部客户");
        
        area.addListener(SWT.MouseDown, new Listener() {
			@Override
        	public void handleEvent(Event event)  {
				area.setItems(DataCachePool.getCustomerAreas());
				area.add("全部片区");
			}
		});
        area.addListener(SWT.MouseUp, new Listener() {
			@Override
        	public void handleEvent(Event event)  {
				if(area.getText().equals("")){
					area.setText("全部片区");
					cus.setText("全部客户");
					dateTime.setVisible(true);
					dateTime2.setVisible(false);
				}
			}
		});
        

        
        cus.addListener(SWT.MouseDown, new Listener() {
			@Override
        	public void handleEvent(Event event) {
				String ar = area.getText();
				String[] names = DataCachePool.getCustomerNames(ar);
				if(names.length != 0){//no such areas
					cus.setItems(names);
					cus.add("全部客户");
				}
				else{//the result is empty, we should remove the old items
					String[] items = new String[0];
					cus.setItems(items);
					cus.add("全部客户");
				}
			}
		});
        cus.addListener(SWT.MouseUp, new Listener() {
			@Override
        	public void handleEvent(Event event) {
				if(cus.getText().equals("")){
					cus.setText("全部客户");
					dateTime.setVisible(true);
					dateTime2.setVisible(false);
				}				
			}
		});
        
        cus.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				if(!cus.getText().equals("全部客户")){
					dateTime.setVisible(false);
					dateTime2.setVisible(true);
				}else{
					dateTime.setVisible(true);
					dateTime2.setVisible(false);
				}
			}
		});
        
        
        //date picker
        dateTime = new DateTime(composite_left, SWT.BORDER | SWT.MEDIUM);
        dateTime.setBounds(12, 520, 98, 30);
        
        dateTime2 = new DateTime(composite_left, SWT.BORDER | SWT.SHORT);
        dateTime2.setBounds(12, 520, 98, 30);
        dateTime2.setVisible(false);
        
		//search button, search the deliver history
		Button btnSearch = new Button(composite_left, SWT.NONE);
		btnSearch.setBounds(114, 520, 74, 30);
		btnSearch.setText("查找");   
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				String year = String.valueOf(dateTime.getYear());
				int mon = dateTime.getMonth()+1;
				String month = String.valueOf(mon);
				int d = dateTime.getDay();
				String day = String.valueOf(d);
				if(mon<10)
					month = "0"+month;
				if(d<10)
					day="0"+day;
				//date to search
				String dateSearch = year+month+day;
				DeliverUtils.showSearchHistory(dateSearch);
			}
		});
		
		//================================================================
		//quick search for customer, to make a deliver
		final Button btn_quick = new Button(composite_right, SWT.NONE);
		btn_quick.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("jump into customer page, can filter the customer by already exist options(todo)");
				//jump into the customer page
				Utils.setFunctin(FUNCTION.CUSTOMER);
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new CoolBarPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_CUSTOMER) == null)
					shell.setContentPart(new CustomerContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_CUSTOMER);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.CUSTOMER);
				shell.show_Content_customer();
			}
		});
		btn_quick.setBounds(172, 114, 72, 22);
		btn_quick.setText("快速查找");
		btn_quick.setVisible(false);
		//show & hide the quick search button
		composite_right.addListener(SWT.MouseEnter, new Listener(){
			@Override
			public void handleEvent(Event event) {
				btn_quick.setVisible(false);				
			}			
		});
		btn_quick.addListener(SWT.MouseExit, new Listener(){
			@Override
			public void handleEvent(Event event) {				
				btn_quick.setVisible(false);				
			}			
		});
		
		//title fo the table
		text_title = new Text(composite_right, SWT.CENTER);
		text_title.setFont(SWTResourceManager.getFont("微软雅黑", 21, SWT.NORMAL));
		text_title.setBounds(305, 22, 150, 36);
		text_title.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		text_title.setText("送货单");	
		text_title.setEditable(false);
		
		//delete button to delete the list or clear the current list
		btn_delete = new Button(composite_right, SWT.NONE);
		btn_delete.setBounds(672, 12, 76, 30);
		btn_delete.setText("删除");
		btn_delete.setVisible(false);
		btn_delete.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
//				//first leave the edit mode, avoid update the table
				DeliverUtils.leaveEditMode();
				//clear table
				table.removeAll();
				DeliverList.removeAllDelivers();
				//remove current history from database
				DeliverList.removeCurrentHistory();
				//will not show delete button anymore
				btn_delete.setVisible(false);
			}
		});
		
		btn_edit = new Button(composite_right, SWT.NONE);
//		btn_edit.setBounds((int)(12*w/500), (int)(4*w/5/100), (int)(3*4*w/5/50), (int)(h/20));
		btn_edit.setBounds(12, 12, 76, 30);
		btn_edit.setText("修改");
		btn_edit.setVisible(false);
		btn_edit.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				ConfirmEdit ce = new ConfirmEdit(table.getParent().getShell(), 0);
				ce.open();
				if(Utils.getEnter()){
					enableEditContent();
					btn_edit.setVisible(false);
					btn_delete.setVisible(true);
					text_title.setText("进货单");
					indeed.setText("实收(大写)");
					indeed_lbl.setText("实收(小写)");
					gc.setEnabled(true);
					gcName.setEnabled(true);
					text_phone.setEnabled(true);
					text_address.setEnabled(true);
					
					btn_return.setVisible(false);
					DeliverUtils.enterEditMode();
				}
			}
		});
		
		btn_return = new Button(composite_right, SWT.NONE);
		btn_return.setBounds(90, 12, 76, 30);
		btn_return.setVisible(false);
		btn_return.setText("退货");
		btn_return.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				ConfirmEdit ce = new ConfirmEdit(table.getParent().getShell(), 0);
				ce.open();
				if(Utils.getEnter()){
					enableEditContent();
					btn_return.setVisible(false);
					btn_edit.setVisible(false);
					DeliverUtils.enterEditMode();
					
					text_title.setText("退货单");
					indeed.setText("实退(大写)");
					indeed_lbl.setText("实退(小写)");
					gc.setEnabled(false);
					gcName.setEnabled(false);
					text_phone.setEnabled(false);
					text_address.setEnabled(false);
					
					//make a return mode, then, in this mode,
					//we need to check and make sure:
					//1. all goods are subsets of this deliver
					//2. based on 1, on new goods, and goods number are no bigger than this deliver
					
				}
			}
		});
		
		composite_updown = (int)(h/3);//wait for adjust
		//area
		Label lbl_area = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_area.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		lbl_area.setForeground(new Color(composite.getDisplay(), 0x80, 0x80, 0x80));
		lbl_area.setAlignment(SWT.CENTER);
		lbl_area.setText("片区:");
//		lbl_area.setBounds(0, (int)(2*h/9), (int)(4*w/5/25), (int)(h/9/2));
		lbl_area.setBounds(12, 82, 32, 22);
		//area combo
		
		gc = new GeneralCCombo(composite_right, SWT.BORDER, 0, -1, Constants.DELIVER_TYPE);
//		gc.setBounds((int)(4*w/5/25), (int)(2*h/9), (int)(24*w/5/25), (int)(h/9/2));
		gc.setBounds(60, 82, 112, 22);
		gc.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		gc.setForeground(new Color(composite.getDisplay(), 0x00, 0x00, 0x00));
		gc.setVisibleItemCount(5);
//		gc.setBackground(new Color(composite.getDisplay(), 204, 255, 204));
		//mouse down listener
		gc.addListener(SWT.MouseDown, new Listener() {
			@Override
        	public void handleEvent(Event event)  {
				gc.setItems(DataCachePool.getCustomerAreas());
			}
		});
	
		//customer
		Label lbl_cusname = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_cusname.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		lbl_cusname.setForeground(new Color(composite.getDisplay(), 0x80, 0x80, 0x80));
		lbl_cusname.setAlignment(SWT.CENTER);
		lbl_cusname.setText("客户:");
		lbl_cusname.setBounds(12, 114, 32, 22);

		//customer name
		gcName = new GeneralCCombo(composite_right, SWT.BORDER, 0, -2, Constants.DELIVER_TYPE);
		gcName.setBounds(60, 114, 112, 22);
		gcName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		gcName.setVisibleItemCount(5);
//		gcName.setBackground(new Color(composite.getDisplay(), 204, 255, 204));
		gcName.addListener(SWT.MouseEnter, new Listener(){
			@Override
			public void handleEvent(Event event) {
				btn_quick.setVisible(true);				
			}			
		});
		gcName.addListener(SWT.MouseDown, new Listener() {
			@Override
        	public void handleEvent(Event event) {
				String area = gc.getText();
//				System.out.println("area: "+area);
				String[] names = DataCachePool.getCustomerNames(area);
				if(names.length != 0){//no such areas
					gcName.setItems(names);
				}
			}
		});
		
		gcName.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
//				System.out.println("selected");
				String area = gc.getText();
				String name = gcName.getText();
				try {
					//		
					CustomerInfoService cusinfo = new CustomerInfoService();		
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("customer_area", area);
					map.put("customer_name", name);
					ReturnObject ret = cusinfo.queryCustomerInfo(map);
					Pagination page = (Pagination) ret.getReturnDTO();
					List<Object> list = page.getItems();
					if(list.size() > 0){
						CustomerInfoDTO cDTO = (CustomerInfoDTO) list.get(0);
						text_phone.setText(cDTO.getTelephone());
						text_address.setText(cDTO.getCustomer_addr());
					}					
				} catch (Exception ex) {
					System.out.println("get customer names of a specfied area & name failed");
				}
				
			}
		});
//		gc.setItems(items);		
		//add listener for customer names
		
		//customer phone
		Label lbl_phone = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_phone.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		lbl_phone.setForeground(new Color(composite.getDisplay(), 0x80, 0x80, 0x80));
		lbl_phone.setAlignment(SWT.CENTER);
		lbl_phone.setText("电话:");
		lbl_phone.setBounds(252, 82, 32, 22);
		
		text_phone = new Text(composite_right, SWT.BORDER);
		text_phone.setBounds(300, 82, 162, 22);
		text_phone.setForeground(new Color(composite.getDisplay(), 0x00, 0x00, 0x00));
		text_phone.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		
		//customer address
		Label lbl_address = new Label(composite_right, SWT.CENTER);
		lbl_address.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		lbl_address.setForeground(new Color(composite.getDisplay(), 0x80, 0x80, 0x80));
		lbl_address.setAlignment(SWT.CENTER);
		lbl_address.setText("地址:");
		lbl_address.setBounds(252, 114, 32, 22);
		
		text_address = new Text(composite_right, SWT.BORDER);
		text_address.setBounds(300, 114, 162, 22);
		text_address.setForeground(new Color(composite.getDisplay(), 0x00, 0x00, 0x00));
		text_address.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		
		
		//serial number
		Label lbl_serial = new Label(composite_right, SWT.CENTER);
		lbl_serial.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		lbl_serial.setForeground(new Color(composite.getDisplay(), 0x80, 0x80, 0x80));
		lbl_serial.setAlignment(SWT.CENTER);
		lbl_serial.setText("单号:");
		lbl_serial.setBounds(542, 82, 32, 22);
		
		text_serial = new Text(composite_right, SWT.BORDER);
		text_serial.setEnabled(false);
		text_serial.setBounds(590, 82, 158, 22);
		text_serial.setForeground(new Color(composite.getDisplay(), 0x00, 0x00, 0x00));
		text_serial.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		
		//time
		Label lbl_time = new Label(composite_right, SWT.CENTER);
		lbl_time.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		lbl_time.setForeground(new Color(composite.getDisplay(), 0x80, 0x80, 0x80));
		lbl_time.setAlignment(SWT.CENTER);
		lbl_time.setText("时间:");
		lbl_time.setBounds(542, 114, 32, 22);
		
		text_time = new Text(composite_right, SWT.BORDER);
		text_time.setEnabled(false);
		text_time.setBounds(590, 114, 158, 22);
		text_time.setForeground(new Color(composite.getDisplay(), 0x00, 0x00, 0x00));
		text_time.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		
		//===================================================================================================
		//sum composite
		Composite composite_sum = new Composite(composite_right, SWT.BORDER);
		composite_sum.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		composite_sum.setBounds(24, 392, 712, 52);
		
		Label total = new Label(composite_sum, SWT.NONE);
		total.setEnabled(false);
		total.setText("总计(大写):");
		total.setForeground(new Color(composite.getDisplay(), 0x80, 0x80, 0x80));
		total.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		total.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total.setBounds(0, 0, 72, 15);
		
		total_big = new Text(composite_sum, SWT.LEFT|SWT.NONE);
		total_big.setEnabled(false);
		total_big.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		total_big.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total_big.setBounds(74, 0, 252, 15);
		
		//label of total_val
		Label total_lbl = new Label(composite_sum, SWT.LEFT|SWT.NONE);
		total_lbl.setEnabled(false);
		total_lbl.setText("总计(小写):");
		total_lbl.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		total_lbl.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total_lbl.setBounds(412, 0, 72, 15);
		
		total_val = new Text(composite_sum, SWT.RIGHT|SWT.NONE);
		total_val.setEnabled(false);
		total_val.setText("");
		total_val.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		total_val.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total_val.setBounds(484, 0, 225, 15);
		
		indeed = new Label(composite_sum, SWT.NONE);
		indeed.setEnabled(false);
		indeed.setText("实收(大写):");
		indeed.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		indeed.setBackground(new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa));
		indeed.setBounds(0, 27, 72, 15);
		
		indeed_big = new Text(composite_sum, SWT.LEFT|SWT.NONE);
		indeed_big.setEnabled(false);
		indeed_big.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		indeed_big.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed_big.setBounds(74, 27, 252, 15);
		
		indeed_lbl = new Label(composite_sum, SWT.LEFT|SWT.NONE);
		indeed_lbl.setEnabled(false);
		indeed_lbl.setText("实收(小写):");
		indeed_lbl.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		indeed_lbl.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed_lbl.setBounds(412, 27, 72, 15);
		
		indeed_val = new Text(composite_sum, SWT.RIGHT|SWT.NONE);
		indeed_val.setEnabled(true);
		indeed_val.setText("");
		indeed_val.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		indeed_val.setBackground(new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa));
		indeed_val.setBounds(484, 27, 225, 15);
		
		Text indeed_sign = new Text(composite_right, SWT.NONE);
		indeed_sign.setEnabled(false);
		indeed_sign.setText("收货人签字(盖章):");
		indeed_sign.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		indeed_sign.setBackground(new Color(composite.getDisplay(), 255, 250, 250));		
		indeed_sign.setBounds(12, 464, 150, 15);
		
		indeed_val.addFocusListener(new FocusAdapter(){

			@Override
			public void focusLost(FocusEvent e) {
				indeed_big.setText(NumberConverter.getInstance().number2CNMontrayUnit(indeed_val.getText()));
				//change the database
			}
			
		});
		
		composite_sum.layout();
		
		//button print
		Button btn_print = new Button(composite_right, SWT.NONE);
		btn_print.setText("打印");
		btn_print.setBounds(293, 500, 85, 40);
		btn_print.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
//				System.out.println("print");
				//do more check here
				if(DeliverList.getDelivers().size() > 1){
					
					//check area & name, if both are not empty, add to history, or popup an message box
					if(!gc.getText().equals("") && !gcName.getText().equals("")){
						
						if(DeliverUtils.getStatus().equals("NEW")){
							DeliverUtils.addToHistory();
						}
						//step 1: add the deliver common info into database
						DeliverInfoService deliverinfo = new DeliverInfoService();
						Map<String,Object> commonMap = new HashMap<String,Object>();
						commonMap.put("order_num", DeliverUtils.getOrderNumber());
						commonMap.put("customer_area", gc.getText());
						commonMap.put("customer_name", gcName.getText());						
						commonMap.put("deliver_addr", text_address.getText());
						commonMap.put("deliver_time", DeliverUtils.getTime());
						commonMap.put("telephone", text_phone.getText());
						//if already exist, update it 
						deliverinfo.print_voucher(commonMap);
						
						//status: NEW, HISTORY, EMPTY
						DeliverUtils.setStatus("EMPTY");
						
						//step 2: initial the deliver page
						//clear table
						//and add a new line					
						table.clearAll();
						table.removeAll();					
						DeliverList.removeAllDelivers();
						clearContent();
						disableEditContent();	
						DeliverUtils.setTime("");
						
						total_val.setText("");
						total_big.setText("");
						btn_edit.setVisible(false);
						btn_return.setVisible(false);
						
					}else{
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("收货人片区和姓名需填写完整");
						mbox.open();						
					}
				}
				
			}
		});
		
		//button print
				Button btn_save = new Button(composite_right, SWT.NONE);
				btn_save.setText("仅保存");
				btn_save.setBounds(382, 500, 85, 40);
				btn_save.addSelectionListener(new SelectionAdapter() {
					@Override
		        	public void widgetSelected(SelectionEvent e) {
//						System.out.println("print");
						//do more check here
						if(DeliverList.getDelivers().size() > 1){
							
							//check area & name, if both are not empty, add to history, or popup an message box
							if(!gc.getText().equals("") && !gcName.getText().equals("")){
								
								if(DeliverUtils.getStatus().equals("NEW")){
									DeliverUtils.addToHistory();
								}
								//step 1: add the deliver common info into database
								DeliverInfoService deliverinfo = new DeliverInfoService();
								Map<String,Object> commonMap = new HashMap<String,Object>();
								commonMap.put("order_num", DeliverUtils.getOrderNumber());
								commonMap.put("customer_area", gc.getText());
								commonMap.put("customer_name", gcName.getText());						
								commonMap.put("deliver_addr", text_address.getText());
								commonMap.put("deliver_time", DeliverUtils.getTime());
								commonMap.put("telephone", text_phone.getText());
								//if already exist, update it 
								deliverinfo.print_voucher(commonMap);
								
								//status: NEW, HISTORY, EMPTY
								DeliverUtils.setStatus("EMPTY");
								
								//step 2: initial the deliver page
								//clear table
								//and add a new line					
								table.clearAll();
								table.removeAll();					
								DeliverList.removeAllDelivers();
								clearContent();
								disableEditContent();	
								DeliverUtils.setTime("");
								
								total_val.setText("");
								total_big.setText("");
								btn_edit.setVisible(false);
								btn_return.setVisible(false);
								
							}else{
								MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
								mbox.setMessage("收货人片区和姓名需填写完整");
								mbox.open();						
							}
						}
						
					}
				});
		
		
		
		//whether to use the software keyboard
		Button button_swkb = new Button(composite_right, SWT.CHECK);
		button_swkb.setBounds(660, 545, 100, 20);
		button_swkb.setText("启用数字键盘");
		
		//===============================================================================
		//define a table				
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(12, 150, 736, 230);
		tv = tableViewer;
		//set the columns of the table
//		int columnWidth = (int)(4*9*w/60/5);		
		final TableColumn newColumnTableColumn_ID = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_ID.setWidth(0);
		newColumnTableColumn_ID.setMoveable(false);
		newColumnTableColumn_ID.setResizable(false);
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(164);
		newColumnTableColumn_1.setMoveable(false);
		newColumnTableColumn_1.setResizable(false);
		newColumnTableColumn_1.setText("品牌");
//		newColumnTableColumn.setImage(new Image(display,"title.png"));
		//add listener
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?DeliverSorter.BRAND_ASC:DeliverSorter.BRAND_DESC);
				asc = !asc;				
				Utils.refreshTable(table);
			}
		});
		tpShift.add(164);
		
		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(212);
		newColumnTableColumn_2.setMoveable(false);
		newColumnTableColumn_2.setResizable(false);
		newColumnTableColumn_2.setText("子品牌");
		newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?DeliverSorter.SUB_BRAND_ASC:DeliverSorter.SUB_BRAND_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		tpShift.add(212);
		
		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(70);
		newColumnTableColumn_3.setMoveable(false);
		newColumnTableColumn_3.setResizable(false);
		newColumnTableColumn_3.setText("规格");
		newColumnTableColumn_3.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?DeliverSorter.SIZE_ASC:DeliverSorter.SIZE_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		tpShift.add(70);
		
		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(50);
		newColumnTableColumn_4.setMoveable(false);
		newColumnTableColumn_4.setResizable(false);
		newColumnTableColumn_4.setText("单位");
		newColumnTableColumn_4.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?DeliverSorter.UNIT_ASC:DeliverSorter.UNIT_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		tpShift.add(50);
		
		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth(98);
		newColumnTableColumn_5.setMoveable(false);
		newColumnTableColumn_5.setResizable(false);
		newColumnTableColumn_5.setText("单价");
		newColumnTableColumn_5.setAlignment(SWT.RIGHT);
		newColumnTableColumn_5.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?DeliverSorter.PRICE_ASC:DeliverSorter.PRICE_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		tpShift.add(98);
		
		final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setWidth(62);
		newColumnTableColumn_6.setMoveable(false);
		newColumnTableColumn_6.setResizable(false);
		newColumnTableColumn_6.setText("数量");
		newColumnTableColumn_6.setAlignment(SWT.CENTER);
		newColumnTableColumn_6.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?DeliverSorter.NUMBER_ASC:DeliverSorter.NUMBER_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		tpShift.add(62);
		
		final TableColumn newColumnTableColumn_7 = new TableColumn(table, SWT.NONE);
//		newColumnTableColumn_7.setWidth((int)(columnWidth*6/9)-3);//columnWidth*5/9)
		newColumnTableColumn_7.setWidth(58);//columnWidth*5/9)
//		buttonWidth = (int)(columnWidth*5/9/2);
		newColumnTableColumn_7.setText("");
		newColumnTableColumn_7.setMoveable(false);
		newColumnTableColumn_7.setResizable(false);		
		tpShift.add(58);
		
		//set the editor of the table columns
		tableViewer.setContentProvider(new DeliverContentProvider(tableViewer, deliverlist));
		tableViewer.setLabelProvider(new DeliverTableLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up
		Deliver deliver_new = new Deliver(DeliverUtils.getNewLineID());//dynamic from the database
//		DeliverValidator.setNewID(DeliverUtils.getNewLineID());
		deliverlist.addDeliver(deliver_new);
		
		tableViewer.setInput(deliverlist);		
		tableViewer.setColumnProperties(new String[]{"id","brand","sub_brand","size","unit","price", "number", "operation"});		
		cellEditor = new CellEditor[8];
		cellEditor[0] = null;//ID

		int columnWidth = 50;
		ComboUtils.setWidth_Col(columnWidth, 1, Constants.DELIVER_TYPE_BRAND);
		comboboxCellEditor = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getBrands());
//		comboboxCellEditor.setActivationStyle(SWT.Expand);
		cellEditor[1] = comboboxCellEditor;
		
		ComboUtils.setWidth_Col(columnWidth, 2, Constants.DELIVER_TYPE_SUB_BRAND);
		comboboxCellEditor2 = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getSub_Brands());
//		comboboxCellEditor2.setActivationStyle(SWT.Expand);
		cellEditor[2] = comboboxCellEditor2;
		
		ComboUtils.setWidth_Col(columnWidth, 3, Constants.DELIVER_TYPE_SUB_BRAND);
		comboboxCellEditor3 = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getSizes());
		cellEditor[3] = comboboxCellEditor3;
		
		cellEditor[4] = new DeliverTextCellEditor(tableViewer.getTable(),columnWidth, 4);
		cellEditor[5] = new DeliverTextCellEditor(tableViewer.getTable(),columnWidth, 5);
		cellEditor[6] = new DeliverTextCellEditor(tableViewer.getTable(),columnWidth, 6);	
		cellEditor[7] = new DeliverButtonCellEditor(tableViewer.getTable(), deliverlist, rowHeight);//ButtonCellEditor

		tableViewer.setCellEditors(cellEditor);
		
		//initial the editor for hover and set the cell modifier
		editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.CENTER;
	    editor.grabHorizontal = true;		
		editorEdit = new TableEditor(table);
		editorEdit.horizontalAlignment = SWT.CENTER;
		editorEdit.grabHorizontal = true;	
		editorCombo = new TableEditor(table);
		editorCombo.horizontalAlignment = SWT.CENTER;
		editorCombo.grabHorizontal = true;	
		
		ICellModifier modifier = new DeliverCellModifier(tableViewer, deliverlist);
		tableViewer.setCellModifier(modifier);
		
		Utils.refreshTable(table);
		composite_right.setLayout(new FillLayout());
		
		//initial state: disable
		disableEditContent();

	}
}
