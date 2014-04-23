package com.storeworld.deliver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
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

import com.storeworld.customer.CustomerContentPart;
import com.storeworld.mainui.ContentPart;
import com.storeworld.mainui.CoolBarPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.CustomerInfoService;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.softwarekeyboard.SoftKeyBoard;
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
	private static Button btn_edit;
	private static GeneralCCombo gc;
	private static GeneralCCombo gcName;
	private static Text text_phone;
	private static Text text_address;
	private static Text text_serial;
	private static Text text_time;
	private static Text total_val=null;
	private static String TOTAL_VAL = "总计(小写):";
	
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
		total_val.setText(TOTAL_VAL+Constants.SPACE);
		//make the delete button visible = false
		for (int index=0; index < table.getItemCount(); index++) {
			editor.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(index), deleteButtonColumn);
			if(!editor.getEditor().isDisposed())
				editor.getEditor().setVisible(false);
		}
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
		total_val.setText(TOTAL_VAL+Constants.SPACE);
		
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
		total_val.setText(TOTAL_VAL+total+Constants.SPACE);
	}
	public static String getTotal(){
		return total_val.getText();
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
	}
	public static void makeHistoryUnEditable(){
		btn_delete.setVisible(false);
		btn_edit.setVisible(false);
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
		Composite composite_left = new Composite(composite, SWT.NONE);
		final Color base = new Color(composite.getDisplay(), 255,240,245);
		composite_left.setBackground(base);
		composite_left.setBounds(0, 0, (int)(w/5), h);
		 
	    //right part		
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		composite_right.setBounds((int)(w/5), 0, (int)(4*w/5), h);		
		composite_shift = (int)(w/5);
		//define a table
		final TableViewer tableViewer = new TableViewer(composite_right, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK
		//add a new deliver table
		btnNewButton = new Button(composite_left, SWT.NONE);
		btnNewButton.setBounds((int)(2*w/5/10), (int)(w/5/10/2), (int)(2*3*w/5/10), (int)(2*w/5/10));
		btnNewButton.setText("创建送货单");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				
//				if(!gc.getText().equals("") && !gcName.equals("")){
					DeliverUtils.setOrderNumber();//set the order number for the deliver table
				
					clearContent();
					enableEditContent();
//					disableEditContent();
					DeliverUtils.setTime(null);
					DeliverUtils.setStatus("NEW");
				
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
//				}else{
//					
//				}
			}
		});
		
		Label label = new Label(composite_left, SWT.NONE);
		label.setText(" 当月历史记录(在下方设置日期进行搜索)");
		label.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
		label.setBackground(new Color(composite_left.getDisplay(), 240, 255, 255));
		label.setBounds(0, (int)(2*w/5/10/2)+(int)(2*w/5/10), (int)(w/5), (int)(w/5/10));
		
		
		Composite composite_2 = new Composite(composite_left, SWT.NONE);
        composite_2.setBounds(0, (int)(2*w/5/10)+(int)(2*w/5/10), (int)(w/5), (int)(4*(h-2*w/25)/5));
        composite_2.setBackground(new Color(composite_left.getDisplay(), 255,240,245));
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
		composite_fn.setBackground(new Color(composite.getDisplay(), 255,240,245));
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
        Color comp_color = new Color(composite_fn.getDisplay(), 204, 255, 204);
        DeliverUtils.showHistoryPanel(composite_scroll, composite_fn, comp_color,(int)(9*w/5/9), (int)(4*(h-2*w/25)/5/9));
        composite_2.layout();
        //date picker
        final DateTime dateTime = new DateTime(composite_left, SWT.BORDER | SWT.MEDIUM);
		dateTime.setBounds((int)(w/5/10/2), (int)(h-3*w/5/10), (int)(2*3*w/5/10), (int)(2*w/5/10));
		//search button, search the deliver history
		Button btnSearch = new Button(composite_left, SWT.NONE);
		btnSearch.setBounds((int)(w/5/10/2 + 2*3*w/5/10), (int)(h-3*w/5/10), (int)(3*w/5/10), (int)(2*w/5/10));
		btnSearch.setText("查找");   
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				String year = String.valueOf(dateTime.getYear());
				int mon = dateTime.getMonth()+1;
				String month = String.valueOf(mon);
				int d = dateTime.getDay();
				String day = String.valueOf(d);
//				int hour = dateTime.getHours()+1;
//				int min = dateTime.getMinutes()+1;
//				int sec = dateTime.getSeconds()+1;
				if(mon<10)
					month = "0"+month;
				if(d<10)
					day="0"+day;
				//date to search
				String dateSearch = year+month+day;
				DeliverUtils.showSearchHistory(dateSearch);
			}
		});
		
		
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
		btn_quick.setBounds((int)(4*w/5/25)+(int)(24*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(6*w/5/25), (int)(3*h/9/2/4));
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
		Text text_title = new Text(composite_right, SWT.CENTER);
		text_title.setFont(SWTResourceManager.getFont("微软雅黑", 30, SWT.NORMAL));
		text_title.setBounds((int)(2*4*w/5/5), (int)(4*w/5/100+h/40), (int)(4*w/5/5), (int)(3*h/20/2));
		text_title.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		text_title.setText("送货单");		
		//delete button to delete the list or clear the current list
		btn_delete = new Button(composite_right, SWT.NONE);
		btn_delete.setBounds((int)(364*w/500), (int)(4*w/5/100), (int)(3*4*w/5/50), (int)(h/20));
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
		btn_edit.setBounds((int)(12*w/500), (int)(4*w/5/100), (int)(3*4*w/5/50), (int)(h/20));
		btn_edit.setText("修改");
		btn_edit.setVisible(false);
		btn_edit.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL); 
				messageBox.setMessage("点击确定进入编辑模式");
				if (messageBox.open() == SWT.OK){ 
					
					enableEditContent();
					btn_edit.setVisible(false);
					btn_delete.setVisible(true);
					DeliverUtils.enterEditMode();
				}
			}
		});
		
		
		composite_updown = (int)(h/3);
		//area
		Label lbl_area = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_area.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_area.setAlignment(SWT.CENTER);
		lbl_area.setText("片区:");
		lbl_area.setBounds(0, (int)(2*h/9), (int)(4*w/5/25), (int)(h/9/2));
		//area combo
		
		gc = new GeneralCCombo(composite_right, SWT.NONE, 0, -1, Constants.DELIVER_TYPE);
		gc.setBounds((int)(4*w/5/25), (int)(2*h/9), (int)(24*w/5/25), (int)(h/9/2));
		gc.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		gc.setVisibleItemCount(5);
		gc.setBackground(new Color(composite.getDisplay(), 204, 255, 204));
		//mouse down listener
		gc.addListener(SWT.MouseDown, new Listener() {
			@Override
        	public void handleEvent(Event event)  {
				gc.setItems(DataCachePool.getCustomerAreas());
			}
		});
	
		//customer
		Label lbl_cusname = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_cusname.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_cusname.setAlignment(SWT.CENTER);
		lbl_cusname.setText("客户:");
		lbl_cusname.setBounds(0, (int)(5*h/18), (int)(4*w/5/25), (int)(h/9/2));

		//customer name
		gcName = new GeneralCCombo(composite_right, SWT.NONE, 0, -2, Constants.DELIVER_TYPE);
		gcName.setBounds((int)(4*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(24*w/5/25), (int)(h/9/2));
		gcName.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		gcName.setVisibleItemCount(5);
		gcName.setBackground(new Color(composite.getDisplay(), 204, 255, 204));
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
		lbl_phone.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_phone.setAlignment(SWT.CENTER);
		lbl_phone.setText("电话:");
		lbl_phone.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(8*w/5/25), (int)(2*h/9), (int)(4*w/5/25), (int)(h/9/2));
		text_phone = new Text(composite_right, SWT.NONE);
		text_phone.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(12*w/5/25), (int)(2*h/9), (int)(24*w/5/25), (int)(h/9/2));		
		//customer address
		Label lbl_address = new Label(composite_right, SWT.CENTER);
		lbl_address.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_address.setAlignment(SWT.CENTER);
		lbl_address.setText("地址:");
		lbl_address.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(8*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(4*w/5/25), (int)(h/9/2));
		text_address = new Text(composite_right, SWT.NONE);
		text_address.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(12*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(24*w/5/25), (int)(h/9/2));		
		//serial number
		Label lbl_serial = new Label(composite_right, SWT.CENTER);
		lbl_serial.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_serial.setAlignment(SWT.CENTER);
		lbl_serial.setText("单号:");
		lbl_serial.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(42*w/5/25), (int)(2*h/9), (int)(4*w/5/25), (int)(h/9/2));
		text_serial = new Text(composite_right, SWT.NONE);
		text_serial.setEnabled(false);
		text_serial.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(46*w/5/25), (int)(2*h/9), (int)(24*w/5/25), (int)(h/9/2));		
		//time
		Label lbl_time = new Label(composite_right, SWT.CENTER);
		lbl_time.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_time.setAlignment(SWT.CENTER);
		lbl_time.setText("时间:");
		lbl_time.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(42*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(4*w/5/25), (int)(h/9/2));
		text_time = new Text(composite_right, SWT.NONE);
		text_time.setEnabled(false);
		text_time.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(46*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(24*w/5/25), (int)(h/9/2));
		
		
		//sum composite
		Composite composite_sum = new Composite(composite_right, SWT.NONE);
		composite_sum.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		composite_sum.setBounds(0, (int)(2*h/3), (int)(4*w/5), (int)(5*h/10/4));//(int)(13*h/20)+(int)(2*4*w/5/100)		
		GridLayout gd = new GridLayout(2, true);
		gd.horizontalSpacing = 0;
		gd.verticalSpacing = 0;
		gd.marginWidth = 0;
		gd.marginHeight = 0;
		composite_sum.setLayout(gd);	
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = (int)((int)(4*w/5/2));
		gd_text.heightHint = (int)(5*h/10/3/4);		
		GridData gd_text2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text2.widthHint = (int)((int)(4*w/5/2));
		gd_text2.heightHint = (int)(5*h/10/3/4);		
		GridData gd_text3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text3.widthHint = (int)((int)(4*w/5/2));
		gd_text3.heightHint = (int)(5*h/10/3/4);		
		GridData gd_text4 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text4.widthHint = (int)((int)(4*w/5/2));
		gd_text4.heightHint = (int)(5*h/10/3/4);		
		GridData gd_text5 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text5.widthHint = (int)((int)(4*w/5/2));
		gd_text5.heightHint = (int)(5*h/10/3/4);
		//text total 1						
		Text total = new Text(composite_sum, SWT.NONE);
		total.setEnabled(false);
		total.setText("总计(大写):");
		total.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total.setLayoutData(gd_text);
		//text total 2
		total_val = new Text(composite_sum, SWT.RIGHT|SWT.NONE);
		total_val.setEnabled(false);
		total_val.setText("总计(小写):"+Constants.SPACE+Constants.SPACE);
		total_val.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total_val.setLayoutData(gd_text2);
		//indeed 1
		Text indeed = new Text(composite_sum, SWT.NONE);
		indeed.setEnabled(false);
		indeed.setText("实收(大写):");
		indeed.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed.setLayoutData(gd_text3);		
		//indeed 2
		Text indeed_val = new Text(composite_sum, SWT.RIGHT|SWT.NONE);
		indeed_val.setEnabled(false);
		indeed_val.setText("实收(小写):               ");
		indeed_val.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed_val.setLayoutData(gd_text4);		
		//sign
		Text indeed_sign = new Text(composite_sum, SWT.NONE);
		indeed_sign.setEnabled(false);
		indeed_sign.setText("收货人签字(盖章):");
		indeed_sign.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed_sign.setLayoutData(gd_text5);
		new Label(composite_sum, SWT.NONE);		
		composite_sum.layout();
		
		//button print
		Button btn_print = new Button(composite_right, SWT.NONE);
		btn_print.setText("打印");
		btn_print.setBounds((int)(17*w/50), (int)(h-4*w/50), (int)(6*w/50), (int)(2*w/50));
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
						
						total_val.setText(TOTAL_VAL+Constants.SPACE+Constants.SPACE);
						btn_edit.setVisible(false);
						
					}else{
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("收货人片区和姓名需填写完整");
						mbox.open();						
					}
				}
				
			}
		});
		//define a table				
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(0, (int)(h/3), (int)(4*w/5), (int)(h/3));
		tv = tableViewer;
		//set the columns of the table
		int columnWidth = (int)(4*9*w/60/5);		
		final TableColumn newColumnTableColumn_ID = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_ID.setWidth(0);
		newColumnTableColumn_ID.setMoveable(false);
		newColumnTableColumn_ID.setResizable(false);
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(columnWidth);
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

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(columnWidth);
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
		
		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(columnWidth);
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
		
		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(columnWidth);
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
		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth(columnWidth);
		newColumnTableColumn_5.setMoveable(false);
		newColumnTableColumn_5.setResizable(false);
		newColumnTableColumn_5.setText("单价");
		newColumnTableColumn_5.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?DeliverSorter.PRICE_ASC:DeliverSorter.PRICE_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setWidth(columnWidth);
		newColumnTableColumn_6.setMoveable(false);
		newColumnTableColumn_6.setResizable(false);
		newColumnTableColumn_6.setText("数量");
		newColumnTableColumn_6.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?DeliverSorter.NUMBER_ASC:DeliverSorter.NUMBER_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		
		
		final TableColumn newColumnTableColumn_7 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_7.setWidth((int)(columnWidth*6/9)-3);//columnWidth*5/9)
//		buttonWidth = (int)(columnWidth*5/9/2);
		newColumnTableColumn_7.setText("");
		newColumnTableColumn_7.setMoveable(false);
		newColumnTableColumn_7.setResizable(false);		
		
		
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
