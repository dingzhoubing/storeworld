package com.storeworld.deliver;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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

import com.mysql.jdbc.Connection;
import com.storeworld.common.DataInTable;
import com.storeworld.common.NumberConverter;
import com.storeworld.customer.Customer;
import com.storeworld.customer.CustomerContentPart;
import com.storeworld.customer.CustomerList;
import com.storeworld.database.BaseAction;
import com.storeworld.extenddialog.ConfirmEdit;
import com.storeworld.extenddialog.IndeedKeyBoard;
import com.storeworld.extenddialog.SoftKeyBoard;
import com.storeworld.mainui.ContentPart;
import com.storeworld.mainui.CoolBarPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.printer.PrintHandler;
import com.storeworld.product.Product;
import com.storeworld.product.ProductCellModifier;
import com.storeworld.product.ProductUtils;
import com.storeworld.pub.service.CustomerInfoService;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.returndeliver.ReturnComposite;
import com.storeworld.returndeliver.ReturnItemComposite;
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
 * search the return case
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
	private static Button btn_save;
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
	
//	private static Pattern pattern_indeed_val = Pattern.compile("\\d+|^\\d+.\\d{0,2}");
	private static Pattern pattern_indeed_val = Pattern.compile("^([1-9][0-9]*(\\.[0-9]{1,2})?|0\\.(?!0+$)[0-9]{1,2})$");
	private static DecimalFormat df = new DecimalFormat("0.00");
	private static ReturnComposite composite_return = null;
	private static ArrayList<Integer> tpShift = new ArrayList<Integer>();
	private static Button button_swkb = null;
	private static CCombo area = null;
	private static CCombo cus = null;
	private static final BaseAction baseAction =  new BaseAction();
	private static ArrayList<Product> products = new ArrayList<Product>();
	private static final DeliverInfoService deliverinfo = new DeliverInfoService();
	
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
	
	public static Button getButtonSWKB(){
		return button_swkb;
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
		btn_save.setVisible(true);
	}
	public static void enableEditContent(){
		gc.setEnabled(true);
		gcName.setEnabled(true);
		text_phone.setEnabled(true);
		text_address.setEnabled(true);
		table.setEnabled(true);
	}
	
	public static void afterPrintFinished(){
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
		//show the save button?
		btn_save.setVisible(true);
	}
	
	
	
	public static void disableEditContent(){
		//clear the context 
		gc.clearSelection();
		gcName.clearSelection();
		gc.setText("");
		gcName.setText("");//??
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
		indeed_val.setEnabled(false);
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
	
	
	public static void doSearch() throws Exception{
		if (DeliverUtils.getDetailTimer()) {//dateTime.isVisible()
			String year = String.valueOf(dateTime.getYear());
			int mon = dateTime.getMonth() + 1;
			String month = String.valueOf(mon);
			int d = dateTime.getDay();
			String day = String.valueOf(d);
			if (mon < 10)
				month = "0" + month;
			if (d < 10)
				day = "0" + day;
			// date to search
			String dateSearch = year + month + day;
			try {
				DeliverUtils.showSearchHistory(dateSearch, "", "");
			} catch (Exception e) {
				throw e;
			}
		}else{
			String year = String.valueOf(dateTime2.getYear());
			int mon = dateTime2.getMonth() + 1;
			String month = String.valueOf(mon);					
			if (mon < 10)
				month = "0" + month;
			// date to search
			String dateSearch = year + month;
			String str_area = area.getText();
			String str_cus = cus.getText();
			try {
				DeliverUtils.showSearchHistory(dateSearch, str_area, str_cus);
			} catch (Exception e) {
				throw e;
			}
		}
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
					//if using the software keyboard
					if(Utils.getUseSoftKeyBoard()){
						if(colCurrent == priceColumn || colCurrent == numberColumn){
							editorEdit.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
							Text text = (Text)(editorEdit.getEditor());	
							callKeyBoard(text);
							
							if(colCurrent == priceColumn){
								if(Utils.getClickButton() && Utils.getInputNeedChange()){
									DeliverCellModifier.staticModify(table.getItem(rowCurrent), "price", Utils.getInput());
									//initial the next click
									Utils.setClickButton(false);
								}
							}else if(colCurrent == numberColumn){
								if(Utils.getClickButton() && Utils.getInputNeedChange()){
									DeliverCellModifier.staticModify(table.getItem(rowCurrent), "number", Utils.getInput());
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
							DeliverCellModifier.staticModify(table.getItem(rowCurrent), "sub_brand", "");	
						}else{
							List<String> list = Utils.getSub_Brands(current_brand);
							//set data into objects
							comboboxCellEditor2.setObjects(list);
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
	 * when update the product table we reset the deliver table
	 * @throws Exception 
	 */
	public static void reNewDeliver() throws Exception{	
		
		try {
			DeliverUtils.setOrderNumber();
		} catch (Exception e) {
//			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
//			mbox.setMessage("重置送货单号失败，请重试");
//			mbox.open();
//			return;
			throw e;
		}
	
		
		DeliverUtils.setStatus("NEW");
		DeliverUtils.leaveEditMode();
		DeliverUtils.leaveReturnMode();

		clearContent();
		enableEditContent();
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
		indeed_val.setEnabled(true);
	}
	
	/**
	 * re-search the the history
	 * @throws Exception 
	 */
	public static void reNewDeliverHistory() throws Exception{
		doSearch();
	}
	
	/**
	 * initialize the table elements
	 */
	public void initialization(){
		final int w = current.getBounds().width;//960
		final int h = current.getBounds().height;//570
		composite.setBounds(0, 0, w, h);
		
		//left side navigate
		final Composite composite_left = new Composite(composite, SWT.NONE);
		final Color base = new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa);
		composite_left.setBackground(base);
		composite_left.setBounds(0, 0, 200, 570);
		 
	    //right part		
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		composite_right.setBounds(200, 0, 760, 570);
		composite_shift = 200;
		
		//define a table
		final TableViewer tableViewer = new TableViewer(composite_right, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL);//shell, SWT.CHECK
		btnNewButton = new Button(composite_left, SWT.NONE);
		btnNewButton.setBounds(12, 12, 176, 36);
		btnNewButton.setText("创建送货单");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				
					ArrayList<Product> products_changed = new ArrayList<Product>();
					//if there are items in the table, we need to delete the info from the database
					if(DeliverUtils.getStatus().equals("NEW") && !text_serial.getText().equals("")){
						if(DeliverList.getDelivers().size() > 1){
							try {
								products_changed = DeliverList.deleteDeliversUseLess(getOrderNumber());
							} catch (Exception e1) {								
								MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
								mbox.setMessage("删除废弃送货信息失败");
								mbox.open();
								return;
							}							
						}						
					}
					try {
						DeliverUtils.setOrderNumber();//set the order number for the deliver table
					} catch (Exception e1) {
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
						mbox.setMessage("计算单号失败，请重试");
						mbox.open();	
						return;
					}
					//update the product table UI side
					DeliverList.relatedProductChange(products_changed, false);
					
					DeliverUtils.setStatus("NEW");
					DeliverUtils.leaveEditMode();
					DeliverUtils.leaveReturnMode();

				
					clearContent();
					enableEditContent();
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
					indeed_val.setEnabled(true);
			}
		});
		
		Label label = new Label(composite_left, SWT.NONE);
		label.setText(" 当月历史记录(在下方设置条件进行搜索)");
		label.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
		label.setBackground(new Color(composite_left.getDisplay(), 0xe1, 0xe3, 0xe6));
		label.setBounds(0, 62, 200, 18);
				
		//parent of scroll composite
		Composite composite_2 = new Composite(composite_left, SWT.NONE);
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
		composite_fn.setBackground(new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa));
		GridLayout layout = new GridLayout(1, false);  
        layout.numColumns = 1;  
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite_fn.setLayout(layout);
        composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        Color comp_color = new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa);
        //show history
        DeliverUtils.showHistoryPanel(composite_scroll, composite_fn, comp_color, 186, 50);
        composite_2.layout();
        
        //search option for area
        area = new CCombo(composite_left, SWT.BORDER|SWT.READ_ONLY);
        area.setBounds(12, 492, 86, 25);
        area.setVisibleItemCount(5);
        area.setText("全部片区");

        cus = new CCombo(composite_left, SWT.BORDER|SWT.READ_ONLY);
        cus.setBounds(102, 492, 86, 25);
        cus.setVisibleItemCount(5);
        cus.setText("全部客户");
        
        //listener for area combo box
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
					DeliverUtils.setDetailTimer(true);
				}
			}
		});
        
        //listener for customer combo box
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
					DeliverUtils.setDetailTimer(true);
				}				
			}
		});
        
        cus.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				if(!cus.getText().equals("全部客户")){
					dateTime.setVisible(false);
					dateTime2.setVisible(true);
					DeliverUtils.setDetailTimer(false);
				}else{
					dateTime.setVisible(true);
					dateTime2.setVisible(false);
					DeliverUtils.setDetailTimer(true);
				}
			}
		});
        
        
        //date picker default
        dateTime = new DateTime(composite_left, SWT.BORDER | SWT.MEDIUM);
        dateTime.setBounds(12, 520, 98, 30);
        
        //date picker when search the detail of one customer
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
				try {
					doSearch();
				} catch (Exception e1) {
					MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
					mbox.setMessage("搜索历史记录失败，请重试");
					mbox.open();	
				}
			}
		});
		
		//================================================================
		//quick search for customer, to make a deliver
		final Button btn_quick = new Button(composite_right, SWT.NONE);		
		btn_quick.setBounds(172, 114, 72, 22);
		btn_quick.setText("快速查找");
		btn_quick.setVisible(false);
		btn_quick.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
				
				//remove current history from database
				try {
					DeliverList.removeCurrentHistory();
				} catch (Exception e1) {
					MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
					mbox.setMessage("删除送货信息失败");
					mbox.open();	
					return;
				}
				
//				//first leave the edit mode, avoid update the table
				DeliverUtils.leaveEditMode();
				DeliverUtils.leaveReturnMode();
				//clear table
				table.removeAll();
				DeliverList.removeAllDelivers();

				//will not show delete button anymore
				btn_delete.setVisible(false);
				DeliverUtils.setStatus("NEW");
			}
		});
		
		//when user click the "edit" button
		btn_edit = new Button(composite_right, SWT.NONE);
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
					indeed_val.setEnabled(true);
				}
			}
		});
		
		//the "return" mode
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

					//cannot save the return now, in the future, there may exist return module
					btn_save.setVisible(false);
					
					text_title.setText("退货单");
					indeed.setText("实退(大写)");
					indeed_lbl.setText("实退(小写)");
					gc.setEnabled(false);
					gcName.setEnabled(false);
					text_phone.setEnabled(false);
					text_address.setEnabled(false);
					
					DeliverUtils.enterReturnMode();
					composite_return.setVisible(true);
					composite_return.showDelivers(DeliverList.getDelivers());
					table.setVisible(false);
					
					//reset the summary
					total_val.setText("");
					total_big.setText("");
					indeed_val.setText("");
					indeed_big.setText("");
					indeed_val.setEnabled(true);//can be editable					
					//make a return mode, then, in this mode,
					//we need to check and make sure:
					//1. all goods are subsets of this deliver
					//2. based on 1, on new goods, and goods number are no bigger than this deliver
				}
			}
		});
		
		composite_updown = 190;//wait for adjust
		//area
		Label lbl_area = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_area.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		lbl_area.setForeground(new Color(composite.getDisplay(), 0x80, 0x80, 0x80));
		lbl_area.setAlignment(SWT.CENTER);
		lbl_area.setText("片区:");
		lbl_area.setBounds(12, 82, 32, 22);
		
		gc = new GeneralCCombo(composite_right, SWT.BORDER, 0, -2, Constants.DELIVER_TYPE);
		gc.setBounds(60, 82, 112, 22);
		gc.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		gc.setForeground(new Color(composite.getDisplay(), 0x00, 0x00, 0x00));
		gc.setVisibleItemCount(5);

		//mouse down listener
		gc.addListener(SWT.MouseDown, new Listener() {
			@Override
        	public void handleEvent(Event event)  {
				gc.setItems(DataCachePool.getCustomerAreas());
			}
		});
		
		gc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				String area = gc.getText();
				String[] names = DataCachePool.getCustomerNames(area);
				if(names.length != 0){//no such areas
					gcName.setItems(names);
				}
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
		gcName = new GeneralCCombo(composite_right, SWT.BORDER, 0, -1, Constants.DELIVER_TYPE);
		gcName.setBounds(60, 114, 112, 22);
		gcName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		gcName.setVisibleItemCount(5);
		gcName.addListener(SWT.MouseEnter, new Listener(){
			@Override
			public void handleEvent(Event event) {
				btn_quick.setVisible(true);				
			}			
		});
		gcName.addListener(SWT.MouseDown, new Listener() {
			@Override
        	public void handleEvent(Event event){
				gcName.removeAll();
				String area = gc.getText();
				String[] names = DataCachePool.getCustomerNames(area);
				if(names.length != 0){//no such areas
					gcName.setItems(names);
				}
			}
		});
		
		gcName.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				String area = gc.getText();
				String name = gcName.getText();
				Connection conn=null;
				try {
					conn = baseAction.getConnection();
				} catch (Exception e1) {
					MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
					mbox.setMessage("连接数据库失败");
					mbox.open();
					return;
				}
				
				try {
					//		
					CustomerInfoService cusinfo = new CustomerInfoService();		
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("customer_area", area);
					map.put("customer_name", name);
					conn.setAutoCommit(false);
					ReturnObject ret = cusinfo.queryCustomerInfo(conn, map);
					conn.commit();
					Pagination page = (Pagination) ret.getReturnDTO();
					List<Object> list = page.getItems();
					if(list.size() > 0){
						CustomerInfoDTO cDTO = (CustomerInfoDTO) list.get(0);
						text_phone.setText(cDTO.getTelephone());
						text_address.setText(cDTO.getCustomer_addr());
					}					
				} catch (Exception ex) {
					System.out.println("get customer names of a specfied area & name failed");
					try {
						conn.rollback();
					} catch (SQLException e1) {
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("连接数据库异常");
						mbox.open();
					}
				}finally{
					try {
						conn.close();
					} catch (SQLException e1) {
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("连接数据库异常");
						mbox.open();
					}
				}
				
			}
		});

		gcName.addKeyListener(new KeyAdapter(){

			public void keyReleased(KeyEvent e){
				if(!(e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN)){
					if(e.keyCode == SWT.CR){
						gcName.lostFocus();
						
					}else{
				String area = gc.getText();
				String[] names = DataCachePool.getCustomerNames(area);
				if (names.length > 0) {
					String index_str = gcName.getText();
					int num = gcName.getItemCount();
					gcName.remove(0, num - 1);
					for (int i = 0; i < names.length; i++) {
						gcName.add(names[i]);
						String head_str = Utils.getPinYinHeadChar(names[i]);
						if (!(head_str.contains(index_str))) {
							gcName.remove(names[i]);
						}
					}
					gcName.setListVisibleAndDoNotFocus(true);
				}
					}
				}
			}			
		});
		
		gcName.addFocusListener(new FocusAdapter(){
			@Override
			public void focusLost(FocusEvent e) {
				gcName.lostFocus();
			}
			
		});
		
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
			public void focusGained(FocusEvent e) {
				btnNewButton.forceFocus();				
				IndeedKeyBoard inkb = new IndeedKeyBoard(indeed_val, table.getParent().getShell(), 0, 0, 0);
				inkb.open();
				if(Utils.getIndeedClickButton() && Utils.getIndeedNeedChange()){
					String txt = Utils.getIndeed();
					//reasonable value
					if(DeliverList.getDelivers().size() > 1){
					if(pattern_indeed_val.matcher(txt).matches()){	
						String format_str = df.format(Double.valueOf(txt));
						indeed_val.setText(format_str);
						indeed_big.setText(NumberConverter.getInstance().number2CNMontrayUnit(format_str));
						
						indeed_val.setEnabled(false);
						//update the database, only when not in return mode
						if(!indeed_val.getText().equals(total_val.getText()) && !DeliverUtils.getReturnMode()){
							try {
								DeliverList.updateDeliversByOrderNumber(DeliverUtils.getOrderNumber(), indeed_val.getText());
							} catch (Exception e1) {
								MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
								mbox.setMessage("更新送货表实付款失败");
								mbox.open();	
							}
						}
						if(DeliverUtils.getEditMode() && DeliverUtils.getStatus().equals("HISTORY")){
							//update the history panel
							DeliverUtils.getItemCompositeRecord().setDownRight(indeed_val.getText());
							DeliverHistory dh = (DeliverHistory)DeliverUtils.getItemCompositeRecord().getHistory();
							dh.setIndeed(indeed_val.getText());
						}
						
						indeed_val.setEnabled(true);
						
					}else{
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("数值应为整数或两位小数");
						mbox.open();
					}
					}else{
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("送货列表为空，不存在实付");
						mbox.open();
					}
					//initial the next click
					Utils.setIndeedClickButton(false);
				}
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
				
				//if in return mode
				if(DeliverUtils.getReturnMode()){
					//1. print the table
					ArrayList<DataInTable> ds = new ArrayList<DataInTable>();					
					ds.clear();
					//2. update the deliver table
					ArrayList<ReturnItemComposite> items = ReturnComposite.getReturnItems();
					String indeed_u = "";
					
					Connection conn=null;
					try {
						conn = baseAction.getConnection();
					} catch (Exception e3) {
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("连接数据库失败");
						mbox.open();
					}
					DeliverHistory dh = (DeliverHistory)DeliverUtils.getItemCompositeRecord().getHistory();
					boolean existReturn = false;
					try{
					conn.setAutoCommit(false);
					for(int i=0;i<items.size();i++){
						ReturnItemComposite rc = items.get(i);
						//the number has to be changed, we update the database
						if(rc.getCheck() && !rc.getReturnNumber().equals("0")){
							existReturn = true;
							Map<String, Object> common = new HashMap<String ,Object>();
							Map<String, Object> st = new HashMap<String ,Object>();
							st.put("id", rc.getID());
							st.put("brand", rc.getBrand());
							st.put("sub_brand", rc.getSub());
							st.put("standard", rc.getProdSize());
							st.put("unit", rc.getUnit());							
							st.put("order_num", rc.getOrderNumber());
							st.put("unit_price", rc.getPrice());
							Deliver d = new Deliver(rc.getID(), rc.getBrand(), rc.getSub(), rc.getProdSize(), rc.getUnit(), rc.getPrice(), rc.getReturnNumber());
							ds.add(d);							
							int deli = Integer.valueOf(rc.getDeliverNumber());
							int ret = Integer.valueOf(rc.getReturnNumber());
							st.put("quantity", (deli-ret));

							DeliverInfoService deliverinfo = new DeliverInfoService();	
							products.clear();
							Product prec = null;
							if((deli-ret) == 0){
								prec = deliverinfo.deleteDeliverInfoAndUpdateGoods(conn, Integer.valueOf(rc.getID()), d);//deleteDeliverInfo
								if(prec!=null)
									products.add(prec);
							}else{							
								deliverinfo.updateDeliverInfo(conn, rc.getID(),st, "return", products);									
							}
						}
					}					
					if(!existReturn){
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("未选择退货商品！");
						mbox.open();
						return;
					}
					
					
					String indeed_minus = indeed_val.getText();
					
					String indeed_old = dh.getValueShow();//getIndeed();
					indeed_u = df.format(Double.valueOf(indeed_old) - Double.valueOf(indeed_minus));
					deliverinfo.updateDeliversIndeedByOrderNumber(conn, DeliverUtils.getOrderNumber(), indeed_u);
//					conn.commit();
			    	
					//how to get the indeed value
					PrintHandler ph = new PrintHandler(conn, ds, true, indeed_val.getText(), "");
					ph.setHistoryIndeed(indeed_u);
					ph.setProductsChanged(products);
					
					ph.doPrint();
					
					DeliverList.doAfterPrint(indeed_u, products, null);
					} catch (Exception e1) {
						System.out.println("remove the deliver failed");
						try {
							if(conn!=null)
								conn.rollback();
						} catch (SQLException e2) {
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("数据库异常");
							mbox.open();
						}
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("退货失败， 请重试");
						mbox.open();
						return;
					}finally{
						try {
							if(conn!=null)
								conn.close();
						} catch (SQLException e1) {
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("数据库异常");
							mbox.open();
						}
					}
//					//after return, show this
//					dh.setIndeed(indeed_u);//this is needed
//					DeliverUtils.getItemCompositeRecord().setDownRight(indeed_u);
//					//change the product table ui side
//					DeliverList.relatedProductChange(products, true);					
					
//			    	
//					//how to get the indeed value
//					PrintHandler ph = new PrintHandler(conn, ds, true, indeed_val.getText(), "");
//					ph.doPrint();
					
					//put this after the print is succeed?
//					DeliverUtils.leaveReturnMode();
//					
//					MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
//					mbox.setMessage("退货成功");
//					mbox.open();
					
					
				}else{
				//not return mode
				if(DeliverList.getDelivers().size() > 1){
					//check if all the deliver items are complete
					for(int i=0; i< DeliverList.getDelivers().size()-1;i++){
						Deliver d = (Deliver)DeliverList.getDelivers().get(i);
						if(!(DeliverValidator.rowLegal2(d) && DeliverValidator.rowComplete(d))){
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage(String.format("第 %d 条送货信息不全", i+1));
							mbox.open();
							return;//do not go to print or save
						}						
					}

					//check area & name, if both are not empty, add to history, or popup an message box
					if(!gc.getText().equals("") && !gcName.getText().equals("")){
						
						Connection conn = null;
						try {
							conn = baseAction.getConnection();
						} catch (Exception e2) {
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("连接数据库失败");
							mbox.open();
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
						ArrayList<Customer> customers = new ArrayList<Customer>();
						try{
							conn.setAutoCommit(false);
						if(DeliverUtils.getStatus().equals("NEW")){
							deliverinfo.printSaveCommonInfo(conn, commonMap, customers);
						}else{//update
							if(DeliverUtils.getEditMode()){
								deliverinfo.updateCommonInfo(conn, commonMap, customers);							
							}
						}			
						ArrayList<DataInTable> ds = new ArrayList<DataInTable>();
						ds.clear();
						//the last one will not print
						ds.addAll(DeliverList.getDelivers().subList(0, DeliverList.getDelivers().size()-1));
						//how to get the indeed value
						PrintHandler ph = new PrintHandler(conn, ds, false, indeed_val.getText(), DeliverUtils.getOrderNumber());
						ph.setCustomersChanged(customers);
						ph.doPrint();
						
						DeliverList.doAfterPrint("", null, customers);
//						conn.commit();
						} catch (Exception e1) {
							System.out.println("update common info failed");
							try {
								if(conn!=null)
									conn.rollback();
							} catch (SQLException e2) {
								MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
								mbox.setMessage("数据库异常");
								mbox.open();
							}
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("打单出错，请重试");
							mbox.open();
							return;
						}finally{
							try {
								if(conn!=null)
									conn.close();
							} catch (SQLException e1) {
								MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
								mbox.setMessage("数据库异常");
								mbox.open();
							}
						}
//						//update the customer table
//						CustomerList.relatedCustomerChange(customers);
//						
//						if(DeliverUtils.getStatus().equals("NEW")){
//							DeliverUtils.addToHistory();
//						}
//						
//						//status: NEW, HISTORY, EMPTY, empty mode is necessary?
//						DeliverUtils.setStatus("EMPTY");
//						
//						//step 2: initial the deliver page
//						//clear table
//						//and add a new line					
//						table.clearAll();
//						table.removeAll();					
//						DeliverList.removeAllDelivers();
//						clearContent();
//						disableEditContent();	
//						DeliverUtils.setTime("");
//						
//						total_val.setText("");
//						total_big.setText("");
//						btn_edit.setVisible(false);
//						btn_return.setVisible(false);
//						//show the save button?
//						btn_save.setVisible(true);
					}else{
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("收货人片区和姓名需填写完整");
						mbox.open();						
					}
				}
				}
			}
		});

		//button only save, not print
		btn_save = new Button(composite_right, SWT.NONE);
		btn_save.setText("仅保存");
		btn_save.setBounds(382, 500, 85, 40);
		btn_save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (DeliverList.getDelivers().size() > 1) {
					for (int i = 0; i < DeliverList.getDelivers().size() - 1; i++) {
						Deliver d = (Deliver) DeliverList.getDelivers().get(i);
						if (!(DeliverValidator.rowLegal2(d) && DeliverValidator.rowComplete(d))) {
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage(String.format("第 %d 条送货信息不全", i + 1));
							mbox.open();
							return;// do not go to print or save
						}
					}

					// check area & name, if both are not empty, add to history,
					// or popup an message box
					if (!gc.getText().equals("")&& !gcName.getText().equals("")) {

						Connection conn = null;
						try {
							conn = baseAction.getConnection();
						} catch (Exception e2) {
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("连接数据库失败");
							mbox.open();
						}
						
						
						// step 1: add the deliver common info into database
						DeliverInfoService deliverinfo = new DeliverInfoService();
						Map<String, Object> commonMap = new HashMap<String, Object>();
						commonMap.put("order_num",DeliverUtils.getOrderNumber());
						commonMap.put("customer_area", gc.getText());
						commonMap.put("customer_name", gcName.getText());
						commonMap.put("deliver_addr", text_address.getText());
						commonMap.put("deliver_time", DeliverUtils.getTime());
						commonMap.put("telephone", text_phone.getText());
						
						ArrayList<Customer> customers = new ArrayList<Customer>();
						// if already exist, update it
						try{
							conn.setAutoCommit(false);
						if (DeliverUtils.getStatus().equals("NEW")) {
							deliverinfo.printSaveCommonInfo(conn, commonMap, customers);
						} else {// update
							if (DeliverUtils.getEditMode()) {
								deliverinfo.updateCommonInfo(conn, commonMap, customers);								
							}
						}
						conn.commit();
						} catch (Exception e1) {
							System.out.println("update common info failed");
							try {
								conn.rollback();
							} catch (SQLException e2) {
								MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
								mbox.setMessage("数据库异常");
								mbox.open();
							}
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("保存送货信息失败");
							mbox.open();
							return;
						}finally{
							try {
								conn.close();
							} catch (SQLException e1) {
								MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
								mbox.setMessage("数据库异常");
								mbox.open();
							}
						}
						//update the customer UI table
						CustomerList.relatedCustomerChange(customers);
						
						if (DeliverUtils.getStatus().equals("NEW")) {
							DeliverUtils.addToHistory();
						}
						
						// status: NEW, HISTORY, EMPTY
						DeliverUtils.setStatus("EMPTY");
						// step 2: initial the deliver page
						// clear table
						// and add a new line
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

					} else {
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("收货人片区和姓名需填写完整");
						mbox.open();
					}
				}

			}
		});
	
		//whether to use the software keyboard
		button_swkb = new Button(composite_right, SWT.CHECK);
		button_swkb.setBounds(660, 545, 100, 20);
		button_swkb.setText("启用数字键盘");
		button_swkb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(button_swkb.getSelection()){
					Utils.settUseSoftKeyBoard(true);
				}
				else{
					DeliverUtils.refreshTableData();
					Utils.settUseSoftKeyBoard(false);
				}
			}
		});
		//define the return Composite
		composite_return = new ReturnComposite(composite_right, SWT.BORDER);		
		composite_return.setBounds(12, 150, 736, 230);
		composite_return.setVisible(false);
		
		//===============================================================================
		//define a table				
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(12, 150, 736, 230);
		tv = tableViewer;
	
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
		newColumnTableColumn_7.setWidth(58);
		newColumnTableColumn_7.setText("");
		newColumnTableColumn_7.setMoveable(false);
		newColumnTableColumn_7.setResizable(false);		
		tpShift.add(58);
		
		//set the editor of the table columns
		tableViewer.setContentProvider(new DeliverContentProvider(tableViewer, deliverlist));
		tableViewer.setLabelProvider(new DeliverTableLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up
		Deliver deliver_new = new Deliver(DeliverUtils.getNewLineID());//dynamic from the database
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
		
//		ComboUtils.setWidth_Col(columnWidth, 3, Constants.DELIVER_TYPE_SUB_BRAND);
//		comboboxCellEditor3 = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getSizes());
//		cellEditor[3] = comboboxCellEditor3;
		cellEditor[3] = new DeliverTextCellEditor(tableViewer.getTable(),columnWidth, 3);
		
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
		
		DeliverUtils.setStatus("NEW");
	}
}
