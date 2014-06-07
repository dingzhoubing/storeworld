package com.storeworld.stock;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import com.storeworld.extenddialog.ConfirmEdit;
import com.storeworld.extenddialog.IndeedKeyBoard;
import com.storeworld.extenddialog.SoftKeyBoard;
import com.storeworld.mainui.ContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.utils.ComboUtils;
import com.storeworld.utils.Constants;
import com.storeworld.utils.GeneralCCombo;
import com.storeworld.utils.GeneralComboCellEditor;
import com.storeworld.utils.Utils;

/**
 * the main part of the stock page
 * @author dingyuanxiong
 *
 */
public class StockContentPart extends ContentPart{
	
	private static Table table;
	private static TableViewer tv;
	//the product list
	private static StockList stocklist = new StockList();
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
	private int sub_brandColumn = 2;
	private int brandColumn = 1;
	
	private static int deleteButtonColumn = 7;
	private static GeneralComboCellEditor<String> comboboxCellEditor = null;//brand
	private static GeneralComboCellEditor<String> comboboxCellEditor2 = null;//sub_brand
	private static GeneralComboCellEditor<String> comboboxCellEditor3 = null;//size
	private static Text total_val=null;
	private static Text indeed_val=null;
	private static DateTime dateTime_stock = null;
	private static int rowCurrent = -1;
	private static ArrayList<Integer> tpShift = new ArrayList<Integer>();
	
	private static Button btn_edit = null;
	private static Button btn_delete = null;
	private static DecimalFormat df = new DecimalFormat("0.00");
	private static Pattern pattern_indeed_val = Pattern.compile("\\d+|^\\d+.\\d{0,2}");
	private static Button btnNewButton = null;
	private static Button button_swkb = null;
	public StockContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);	
		composite = new Composite(this, SWT.NONE);	
		current = parent;		
		initialization();
		addListenerForTable();
	}
	
	/**
	 * getters: to get the basic info of the table
	 * @return
	 */
	public static CellEditor[] getCellEditors(){
		return cellEditor;
	}
	public static TableEditor getEditor(){
		return editorEdit;
	}
	public static int getRowCurrent(){
		return rowCurrent;
	}
	public static Table getTable(){
		return table;
	}
	public static TableViewer getTableViewer(){
		return tv;
	}
	public static StockList getStockList(){
		return stocklist;
	}
	public static void setTotal(String total){
		total_val.setText(total);//+Constants.SPACE
	}
	public static String getTotal(){
		return total_val.getText();
	}
	public static void setIndeed(String indeed){
		indeed_val.setText(indeed);//+Constants.SPACE
	}
	public static String getIndeed(){
		return indeed_val.getText();
	}
	
	public static ArrayList<Integer> getTpShift(){
		return tpShift;
	}
	
	public static Button getButtonSWKB(){
		return button_swkb;
	}
	/**
	 * get/set the timer value when edit the history	
	 */
	public static void setStockTimer(int year, int month, int day, int hour, int min, int sec){
		dateTime_stock.setDate(year, month, day);
		dateTime_stock.setHours(hour);
		dateTime_stock.setMinutes(min);
		dateTime_stock.setSeconds(sec);
//		System.out.println("set time: "+ year+""+month+""+day+""+hour+""+min+""+sec);
	}
	public static void initialTimer(){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time = formatter.format(new Date());
		int year = Integer.valueOf(current_time.substring(0, 4));
		int month = Integer.valueOf(current_time.substring(4, 6));
		int day = Integer.valueOf(current_time.substring(6, 8));
		dateTime_stock.setDate(year, month-1, day);
	}
	public static String getStockTimer(){
		String year = String.valueOf(dateTime_stock.getYear());
		String mon = String.valueOf(dateTime_stock.getMonth()+1);
		String day = String.valueOf(dateTime_stock.getDay());
		String hour = String.valueOf(dateTime_stock.getHours());
		String min = String.valueOf(dateTime_stock.getMinutes());
		String sec = String.valueOf(dateTime_stock.getSeconds());				
		if(mon.length()<2)
			mon = "0"+mon;
		if(day.length()<2)
			day = "0"+day;
		if(hour.length()<2)
			hour = "0"+hour;
		if(min.length()<2)
			min = "0"+min;
		if(sec.length()<2)
			sec = "0"+sec;
		String time = year+mon+day+hour+min+sec+"";
		return time;
//		System.out.println("current time is: "+time);
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
		dateTime_stock.setEnabled(true);
		table.setEnabled(true);		
	}
	public static void makeDisable(){
		//make the delete button visible = false
		for (int index=0; index < table.getItemCount(); index++) {
			editor.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(index), deleteButtonColumn);
			if(!editor.getEditor().isDisposed())
				editor.getEditor().setVisible(false);
		}
		
		dateTime_stock.setEnabled(false);
		table.setEnabled(false);		
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
		//refactor
		
		table.addListener(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(Event event) {

				Point pt = new Point(event.x, event.y);
				int rowCount = table.getItemCount();
				int colCount = table.getColumnCount();
				int index = table.getTopIndex();	
				int colCurrent = -1;
//				int rowCurrent = -1;
				rowCurrent = -1;
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
							Stock c = (Stock)(table.getItem(rowCurrent).getData());	
							if(colCurrent == priceColumn){
								String pricelast = c.getPrice();
								if(Utils.getClickButton() && Utils.getInputNeedChange()){
									c.setPrice(Utils.getInput());
									text.setText(c.getPrice());//validate the text
									if(StockValidator.validatePrice(c.getPrice())){
										stocklist.stockChanged(c);	
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
									if(StockValidator.validateNumber(c.getNumber())){
										stocklist.stockChanged(c);	
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
					if(colCurrent == brandColumn){//sub_brand column, then fill the combox
						editorCombo.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
						GeneralCCombo combo = (GeneralCCombo)(editorCombo.getEditor());	
						StockUtils.setCurrentLine(rowCurrent);
						Stock c = (Stock)(table.getItem(rowCurrent).getData());
						StockUtils.setCurrentSub_Brand(c.getSubBrand());
						List<String> list = Utils.getBrands();
						//set data into objects
						comboboxCellEditor.setObjects(list);
						combo.setItems(list.toArray(new String[list.size()]));
					}else if(colCurrent == sub_brandColumn){//sub_brand column, then fill the combox
						editorCombo.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
						GeneralCCombo combo = (GeneralCCombo)(editorCombo.getEditor());	
						Stock c = (Stock)(table.getItem(rowCurrent).getData());
						String current_brand = c.getBrand();
						if( current_brand == null ||current_brand.equals("") || !Utils.checkBrand(current_brand)){
							combo.removeAll();
							c.setSubBrand("");
							stocklist.stockChanged(c);	
						}else{
							List<String> list = Utils.getSub_Brands(current_brand);
							//set data into objects
							comboboxCellEditor2.setObjects(list);
							combo.setItems(list.toArray(new String[list.size()]));
						}
					}else if(colCurrent == sizeColumn){
						editorCombo.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
						GeneralCCombo combo = (GeneralCCombo)(editorCombo.getEditor());	
						Stock c = (Stock)(table.getItem(rowCurrent).getData());
						String current_brand = c.getBrand();
						String current_sub = c.getSubBrand();
						//all this will make the size column with empty
						if(current_brand == null || current_brand.equals("") || current_sub==null || current_sub.equals("")
								|| !Utils.checkBrand(current_brand) || !Utils.checkSubBrand(current_sub)){
							combo.removeAll();
							c.setSize("");
							stocklist.stockChanged(c);	
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
		
//		table.addPaintListener(new PaintListener(){
//
//			@Override
//			public void paintControl(PaintEvent e) {
//				System.out.println("paint");
//				
//			}
//			
//		});
		
	}
	
	
	/**
	 * initialize the table elements
	 */
	public void initialization(){
		final int w = current.getBounds().width;//960
		final int h = current.getBounds().height;//570
		composite.setBounds(0, 0, w, h);
		
		//left side navigate
		Composite composite_left = new Composite(composite, SWT.NONE);
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
		
		//add a new stock table
		btnNewButton = new Button(composite_left, SWT.NONE);
//		btnNewButton.setBounds((int)(2*w/5/10), (int)(w/5/10/2), (int)(2*3*w/5/10), (int)(2*w/5/10));
		btnNewButton.setBounds(12, 12, 176, 36);
		btnNewButton.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		btnNewButton.setText("+   新增进货");
		//if click, record the time, put the current into history(if exist), clear all the stock table
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				
				StockUtils.setStatus("NEW");
//				StockUtils.setTime();//record the time
				//TODO: check if has history
				if((StockList.getStocks().size() > 1)){
					
					//only records when the time is not empty
					if(!StockUtils.getEditMode()){
						if(!StockUtils.getTime().equals("")){
							StockUtils.addToHistory();
						}
					}
					//clear table
					//and add a new line
					table.removeAll();
					StockList.removeAllStocks();
					setTotal("0.00");
					setIndeed("0.00");
				}
				//after we add to history, initial the time
				initialTimer();
				StockUtils.setTime("");//initial
				makeHistoryUnEditable();
				makeEnable();
				
				//if entered to add a new stock, leave the edit mode
				StockUtils.leaveEditMode();
//				StockUtils.setStatus("");//no status??
			}
		});
		
		
		//label to show the tips info
		Label label = new Label(composite_left, SWT.NONE);
		label.setText(" 当月历史记录(在下方设置日期进行搜索)");
		label.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
//		label.setBackground(new Color(composite_left.getDisplay(), 240, 255, 255));
		label.setBackground(new Color(composite_left.getDisplay(), 0xe1, 0xe3, 0xe6));
//		label.setBounds(0, (int)(2*w/5/10/2)+(int)(2*w/5/10), (int)(w/5), (int)(w/5/10));
		label.setBounds(0, 62, 200, 18);
		
		//the base composite of the left navigator
		final Composite composite_2 = new Composite(composite_left, SWT.NONE);
//        composite_2.setBounds(0, (int)(2*w/5/10)+(int)(2*w/5/10), (int)(w/5), (int)(4*(h-2*w/25)/5));
//        composite_2.setBackground(new Color(composite.getDisplay(), 255,240,245));
		composite_2.setBounds(0, 80, 200, 428);
        composite_2.setBackground(new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa));
        composite_2.setLayout(new FillLayout());
        
        //left history part
		final ScrolledComposite composite_scroll = new ScrolledComposite(composite_2,  SWT.NONE|SWT.V_SCROLL);//
		composite_scroll.setVisible(true);
		composite_scroll.setExpandHorizontal(true);  
		composite_scroll.setExpandVertical(true);  
		//make the scroll bar move as the mouse wheel
		composite_scroll.addListener(SWT.Activate, new Listener(){    
			public void handleEvent(Event e){      
				composite_scroll.forceFocus();   
				}
		}); 
		//the composite on scroll part
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
        composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        //show the history panel
//        Color comp_color = new Color(composite_fn.getDisplay(), 204, 255, 204);
        Color comp_color = new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa);
//        StockUtils.showHistoryPanel(composite_scroll, composite_fn, comp_color,(int)(9*w/5/9), (int)(4*(h-2*w/25)/5/9));
        StockUtils.showHistoryPanel(composite_scroll, composite_fn, comp_color, 186, 50);//width ??
        composite_2.layout();
        //date picker
        final DateTime dateTime = new DateTime(composite_left, SWT.BORDER | SWT.SHORT);
        dateTime.setBounds(12, 520, 98, 30);
		Button btnSearch = new Button(composite_left, SWT.NONE);
		//search the history
		btnSearch.setBounds(114, 520, 74, 30);
		btnSearch.setText("查找");
		btnSearch.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				String year = String.valueOf(dateTime.getYear());
				int mon = dateTime.getMonth()+1;
				String month = String.valueOf(mon);
				if(mon<10)
					month = "0"+month;
				//date to search
				String dateSearch = year+month;
				StockUtils.showSearchHistory(dateSearch);
			}
		});

		//show time in the right cmposite
		dateTime_stock = new DateTime(composite_right, SWT.BORDER);
//		dateTime_stock.setBounds((int)(4*w/5/100), (int)(4*w/5/100), (int)(6*4*w/5/50), (int)(h/20));
		dateTime_stock.setBounds(12, 12, 98, 30);
		dateTime_stock.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				dateTime_stock.setEnabled(false);
				//update all the time value in table & update the history panel
				StockList.changeStocksTime();
				dateTime_stock.setEnabled(true);
			}
		});		
		
		//delete or clear the table
		btn_delete = new Button(composite_right, SWT.NONE);
//		btn_delete.setBounds((int)(364*w/500), (int)(4*w/5/100), (int)(3*4*w/5/50), (int)(h/20));
		btn_delete.setBounds(672, 12, 76, 30);
		btn_delete.setText("删除");
		//while editing the table, we make the delete button in-visible
		btn_delete.setVisible(false);
		btn_delete.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				//first leave the edit mode, avoid update the table
				StockUtils.leaveEditMode();
				//clear table
				table.removeAll();
				StockList.removeAllStocks();
				//remove current history from database
				StockList.removeCurrentHistory();
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//				String current_time = formatter.format(new Date());
//				int year = Integer.valueOf(current_time.substring(0, 4));
//				int month = Integer.valueOf(current_time.substring(4, 6));
//				int day = Integer.valueOf(current_time.substring(6, 8));
//				dateTime_stock.setDate(year, month-1, day);
				initialTimer();
				//will not show delete button anymore
				total_val.setText("0.00");//+Constants.SPACE
				indeed_val.setText("0.00");//+Constants.SPACE
				btn_delete.setVisible(false);
			}
		});
				
		composite_updown = 30+24+37;
		
		//=======================================================================================
		//sum composite
		Composite composite_sum = new Composite(composite_right, SWT.BORDER);
		composite_sum.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
//		composite_sum.setBounds(0, (int)(13*h/20)+(int)(2*4*w/5/100), (int)(4*w/5), (int)(h/10));
		composite_sum.setBounds(24, 396, 712, 52);
		
		Text total = new Text(composite_sum, SWT.NONE);
		total.setEnabled(false);
		total.setText("总计:");
		total.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		total.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total.setBounds(0, 0, 300, 14);
		
		total_val = new Text(composite_sum, SWT.RIGHT|SWT.NONE);
		total_val.setEnabled(false);
		total_val.setText("0.00");//+Constants.SPACE
		total_val.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		total_val.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total_val.setBounds(412, 0, 298, 14);
		
		Text indeed = new Text(composite_sum, SWT.NONE);
		indeed.setEnabled(false);
		indeed.setText("实付:");
		indeed.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		indeed.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed.setBounds(0, 26, 300, 14);
		
		indeed_val = new Text(composite_sum, SWT.RIGHT|SWT.NONE);
		indeed_val.setEnabled(true);
		indeed_val.setText("0.00");//+Constants.SPACE
		indeed_val.setFont(SWTResourceManager.getFont("Arial", 9, SWT.NORMAL));
		indeed_val.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed_val.setBounds(412, 26, 298, 14);
		indeed_val.addFocusListener(new FocusAdapter(){
			
			@Override
			public void focusGained(FocusEvent e) {
				btnNewButton.forceFocus();				
				IndeedKeyBoard inkb = new IndeedKeyBoard(indeed_val, table.getParent().getShell(), 0, 0, 0);
				inkb.open();
				if(Utils.getIndeedClickButton() && Utils.getIndeedNeedChange()){
					String txt = Utils.getIndeed();
					//reasonable value
					if(pattern_indeed_val.matcher(txt).matches()){	
						String format_str = df.format(Double.valueOf(txt));
						indeed_val.setText(format_str);						
					}else{
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("数值应为整数或两位小数");
						mbox.open();
					}
					//initial the next click
					Utils.setIndeedClickButton(false);
				}
			}
			
		});
				
		composite_sum.layout();
		
		//===============================================================================
		//button edit the history
		btn_edit = new Button(composite_right, SWT.NONE);
		btn_edit.setText("修改记录");
		btn_edit.setBounds(293, 500, 174, 40);
		//at first, set in-visible
		btn_edit.setVisible(false);
		btn_edit.addSelectionListener(new SelectionAdapter() {
			@Override
        	public void widgetSelected(SelectionEvent e) {
				ConfirmEdit ce = new ConfirmEdit(table.getParent().getShell(), 0);
				ce.open();
				if(Utils.getEnter()){
					makeHistoryEditable();
					makeEnable();
					btn_edit.setVisible(false);
					btn_delete.setVisible(true);
					StockUtils.enterEditMode();
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
					StockUtils.refreshTableData();
					Utils.settUseSoftKeyBoard(false);
				}
			}
		});

		//======================================================================
		//the stock table	
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);	
		table.setBounds(12, 54, 736, 330);
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
		newColumnTableColumn_1.setAlignment(SWT.LEFT);
//		newColumnTableColumn.setImage(new Image(display,"title.png"));
		//add listener
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?StockSorter.BRAND_ASC:StockSorter.BRAND_DESC);
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
		newColumnTableColumn_2.setAlignment(SWT.LEFT);
		newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?StockSorter.SUB_BRAND_ASC:StockSorter.SUB_BRAND_DESC);
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
		newColumnTableColumn_3.setAlignment(SWT.LEFT);
		newColumnTableColumn_3.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?StockSorter.SIZE_ASC:StockSorter.SIZE_DESC);
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
		newColumnTableColumn_4.setAlignment(SWT.LEFT);
		newColumnTableColumn_4.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?StockSorter.UNIT_ASC:StockSorter.UNIT_DESC);
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
				tableViewer.setSorter(asc?StockSorter.PRICE_ASC:StockSorter.PRICE_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		tpShift.add(98);
		
		final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setWidth(62);//132
		newColumnTableColumn_6.setMoveable(false);
		newColumnTableColumn_6.setResizable(false);
		newColumnTableColumn_6.setText("数量");
		newColumnTableColumn_6.setAlignment(SWT.CENTER);
		newColumnTableColumn_6.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?StockSorter.NUMBER_ASC:StockSorter.NUMBER_DESC);
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
		tableViewer.setContentProvider(new StockContentProvider(tableViewer, stocklist));
		tableViewer.setLabelProvider(new StockTableLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up
		Stock stock_new = new Stock(StockUtils.getNewLineID());//dynamic from the database
//		StockValidator.setNewID(StockUtils.getNewLineID());
		stocklist.addStock(stock_new);
		
		tableViewer.setInput(stocklist);		
		tableViewer.setColumnProperties(new String[]{"id","brand","sub_brand","size","unit","price", "number", "operation"});	
		cellEditor = new CellEditor[8];
		cellEditor[0] = null;//ID
		
		int columnWidth = 50;//now, since we locate the point by getText of the table cell
		ComboUtils.setWidth_Col(columnWidth, 1, Constants.STOCK_TYPE_BRAND);
//		GeneralComboCellEditor<String> comboboxCellEditor = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getBrands());
		comboboxCellEditor = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getBrands());
//		comboboxCellEditor.setActivationStyle(SWT.Expand);
		cellEditor[1] = comboboxCellEditor;

		ComboUtils.setWidth_Col(columnWidth, 2, Constants.STOCK_TYPE_SUB_BRAND);
//		GeneralComboCellEditor<String> comboboxCellEditor2 = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getSub_Brands());
		comboboxCellEditor2 = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getSub_Brands());
//		comboboxCellEditor2.setActivationStyle(SWT.Expand);
		cellEditor[2] = comboboxCellEditor2;

//		cellEditor[3] = new StockTextCellEditor(tableViewer.getTable(),columnWidth, 3);
		
		ComboUtils.setWidth_Col(columnWidth, 3, Constants.STOCK_TYPE_SUB_BRAND);
		comboboxCellEditor3 = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getSizes());
		cellEditor[3] = comboboxCellEditor3;
				
		cellEditor[4] = new StockTextCellEditor(tableViewer.getTable(),columnWidth, 4);
		cellEditor[5] = new StockTextCellEditor(tableViewer.getTable(),columnWidth, 5);
		cellEditor[6] = new StockTextCellEditor(tableViewer.getTable(),columnWidth, 6);				
		cellEditor[7] = new StockButtonCellEditor(tableViewer.getTable(), stocklist, rowHeight);//ButtonCellEditor

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

		ICellModifier modifier = new StockCellModifier(tableViewer, stocklist);
		tableViewer.setCellModifier(modifier);
		//add Filter, no use now
//		tableViewer.addFilter(new StockFilter());
		
		Utils.refreshTable(table);
		composite_right.setLayout(new FillLayout());
		
	}
}
