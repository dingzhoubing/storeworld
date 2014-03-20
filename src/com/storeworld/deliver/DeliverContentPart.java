package com.storeworld.deliver;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.mainui.ContentPart;
import com.storeworld.softwarekeyboard.SoftKeyBoard;
import com.storeworld.utils.GeneralComboCellEditor;
import com.storeworld.utils.ItemComposite;
import com.storeworld.utils.Utils;

/**
 * the main class of the deliver page
 * @author dingyuanxiong
 *
 */
public class DeliverContentPart extends ContentPart{
	
	private static Table table;
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
	
	public DeliverContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);	
		composite = new Composite(this, SWT.NONE);	
		current = parent;		
		initialization();
		addListenerForTable();
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
				if(Utils.getUseSoftKeyBoard()){
				Point pt = new Point(event.x, event.y);
				int rowCount = table.getItemCount();
				int colCount = table.getColumnCount();
				int index = table.getTopIndex();	
				int colCurrent = -1;
				int rowCurrent = -1;
				boolean found = false;
				for (; index < rowCount; index++) {
					TableItem item = table.getItem(index);
					for(int col=0; col<colCount-1; col++){
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
					if(colCurrent == sizeColumn || colCurrent == priceColumn || colCurrent == numberColumn){
					//cannot reuse the editor, make cause unstable
					editorEdit.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
					Text text = (Text)(editorEdit.getEditor());	
					callKeyBoard(text);
					Deliver c = (Deliver)(table.getItem(rowCurrent).getData());	
					if(colCurrent == sizeColumn){
						if(Utils.getClickButton() && Utils.getInputNeedChange())
							c.setSize(Utils.getInput()+"kg");
					}else if(colCurrent == priceColumn){
						if(Utils.getClickButton() && Utils.getInputNeedChange())
							c.setPrice(Double.valueOf(Utils.getInput()));
					}else if(colCurrent == numberColumn){
						if(Utils.getClickButton() && Utils.getInputNeedChange())
							c.setNumber(Integer.valueOf(Utils.getInput()));
					}
					
					if(Utils.getClickButton() && Utils.getInputNeedChange()){
						deliverlist.deliverChanged(c);		
						//initial the next click
						Utils.setClickButton(false);
					}
					//add message, no use later
					MessageBox messageBox =   
							   new MessageBox(new Shell(),   					     
							    SWT.ICON_WARNING);   
					messageBox.setMessage("change product: "+c);   
					messageBox.open(); 
					}else if(colCurrent == sub_brandColomn){//sub_brand column, then fill the combox
						editorCombo.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
						CCombo combo = (CCombo)(editorCombo.getEditor());	
						Deliver c = (Deliver)(table.getItem(rowCurrent).getData());
						String current_brand = c.getBrand();
						if(current_brand.equals("") || !Utils.checkBrand(current_brand)){
							combo.removeAll();
						}else{
							List<String> list = Utils.getSub_Brands(current_brand);
							combo.setItems(list.toArray(new String[list.size()]));
						}
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
				for (; index < table.getItemCount(); index++) {
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
					editor.setEditor(cellEditor[7].getControl(), table.getItem(row), 7);
					if(!editor.getEditor().isDisposed())
						editor.getEditor().setVisible(true);
				}else{
					if(visibleButton_last >= 0 && visibleButton_last < table.getItemCount()){
						editor.setEditor(cellEditor[7].getControl(), table.getItem(visibleButton_last), 7);
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
		//control the verify
		Text text = (Text)cellEditor[3].getControl();
		text.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e){
				String inStr = e.text;
				if (inStr.length() > 0){
					try{
						if(!inStr.equals(""))
							e.doit = true;
						else
							e.doit=false;
					}catch(Exception ep){
						e.doit = false;
					}
				}
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

		//add a new deliver table
		Button btnNewButton = new Button(composite_left, SWT.NONE);
		btnNewButton.setBounds((int)(2*w/5/10), (int)(w/5/10/2), (int)(2*3*w/5/10), (int)(2*w/5/10));
		btnNewButton.setText("创建送货单");
		
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
        final ArrayList<ItemComposite> itemList = new ArrayList<ItemComposite>();
        composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        btnNewButton.addListener(SWT.Selection, new Listener() {  
	            public void handleEvent(Event e) {  	  
	            	ItemComposite ic = new ItemComposite(composite_fn, 0, new Color(composite_fn.getDisplay(), 204, 255, 204),(int)(9*w/5/9), (int)(4*(h-2*w/25)/5/9));
	            	ic.setValue("123", "234", "456      ");
	            	itemList.add(ic);
	                composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
	                composite_fn.layout();  
	            }  
	        });
        composite_2.layout();
        
        DateTime dateTime = new DateTime(composite_left, SWT.BORDER | SWT.MEDIUM);
		dateTime.setBounds((int)(w/5/10/2), (int)(h-3*w/5/10), (int)(2*3*w/5/10), (int)(2*w/5/10));
		Button btnSearch = new Button(composite_left, SWT.NONE);
		btnSearch.setBounds((int)(w/5/10/2 + 2*3*w/5/10), (int)(h-3*w/5/10), (int)(3*w/5/10), (int)(2*w/5/10));
		btnSearch.setText("查找");
		btnSearch.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {  
				if (!itemList.isEmpty()) {
					itemList.get(0).dispose();
					itemList.remove(0);
					composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
	                composite_fn.layout();  
				}
            }  
        });
        
	    //right part		
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		composite_right.setBounds((int)(w/5), 0, (int)(4*w/5), h);		
		composite_shift = (int)(w/5);
		
		//quick search
		final Button btn_quick = new Button(composite_right, SWT.NONE);
		btn_quick.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox =   
						   new MessageBox(new Shell(),   					     
						    SWT.ICON_WARNING);   
				messageBox.setMessage("进入客户页面");   
				messageBox.open(); 
			}
		});
		btn_quick.setBounds((int)(4*w/5/25)+(int)(24*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(6*w/5/25), (int)(3*h/9/2/4));
		btn_quick.setText("快速查找");
		btn_quick.setVisible(false);
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
		
		
		Text text_title = new Text(composite_right, SWT.CENTER);
		text_title.setFont(SWTResourceManager.getFont("微软雅黑", 30, SWT.NORMAL));
		text_title.setBounds((int)(2*4*w/5/5), (int)(4*w/5/100+h/40), (int)(4*w/5/5), (int)(3*h/20/2));
		text_title.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		text_title.setText("送货单");
		
		Button btn_delete = new Button(composite_right, SWT.NONE);
		btn_delete.setBounds((int)(364*w/500), (int)(4*w/5/100), (int)(3*4*w/5/50), (int)(h/20));
		btn_delete.setText("删除");
		composite_updown = (int)(h/3);
		//area
		Label lbl_area = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_area.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_area.setAlignment(SWT.CENTER);
		lbl_area.setText("片区:");
		lbl_area.setBounds(0, (int)(2*h/9), (int)(4*w/5/25), (int)(h/9/2));
		//area combo
		ComboViewer comboViewer = new ComboViewer(composite_right, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setBounds((int)(4*w/5/25), (int)(2*h/9), (int)(24*w/5/25), (int)(h/9/2));
		String[] comboContext = new String[]{"八里街","安陆","云梦","A","B","C","D"};
		combo.setItems(comboContext);
		combo.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		combo.setVisibleItemCount(5);
		combo.setBackground(new Color(composite.getDisplay(), 204, 255, 204));
				
		//customer
		Label lbl_cusname = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_cusname.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_cusname.setAlignment(SWT.CENTER);
		lbl_cusname.setText("客户:");
		lbl_cusname.setBounds(0, (int)(5*h/18), (int)(4*w/5/25), (int)(h/9/2));

		//customer name
		ComboViewer comboViewerName = new ComboViewer(composite_right, SWT.NONE);
		Combo comboName = comboViewerName.getCombo();
		comboName.setBounds((int)(4*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(24*w/5/25), (int)(h/9/2));
		String[] comboContextName = new String[]{"老李","老刘","小胡","张三","李四","王五"};
		comboName.setItems(comboContextName);
		comboName.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		comboName.setVisibleItemCount(5);
		comboName.setBackground(new Color(composite.getDisplay(), 204, 255, 204));
		comboName.addListener(SWT.MouseEnter, new Listener(){
			@Override
			public void handleEvent(Event event) {
				btn_quick.setVisible(true);				
			}			
		});
		
		//customer phone
		Label lbl_phone = new Label(composite_right, SWT.CENTER|SWT.NONE);
		lbl_phone.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_phone.setAlignment(SWT.CENTER);
		lbl_phone.setText("电话:");
		lbl_phone.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(8*w/5/25), (int)(2*h/9), (int)(4*w/5/25), (int)(h/9/2));
		Text text_phone = new Text(composite_right, SWT.NONE);
		text_phone.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(12*w/5/25), (int)(2*h/9), (int)(24*w/5/25), (int)(h/9/2));
		
		//customer address
		Label lbl_address = new Label(composite_right, SWT.CENTER);
		lbl_address.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_address.setAlignment(SWT.CENTER);
		lbl_address.setText("地址:");
		lbl_address.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(8*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(4*w/5/25), (int)(h/9/2));
		Text text_address = new Text(composite_right, SWT.NONE);
		text_address.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(12*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(24*w/5/25), (int)(h/9/2));
		
		//serial number
		Label lbl_serial = new Label(composite_right, SWT.CENTER);
		lbl_serial.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_serial.setAlignment(SWT.CENTER);
		lbl_serial.setText("单号:");
		lbl_serial.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(42*w/5/25), (int)(2*h/9), (int)(4*w/5/25), (int)(h/9/2));
		Text text_serial = new Text(composite_right, SWT.NONE);
		text_serial.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(46*w/5/25), (int)(2*h/9), (int)(24*w/5/25), (int)(h/9/2));
		
		//time
		Label lbl_time = new Label(composite_right, SWT.CENTER);
		lbl_time.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lbl_time.setAlignment(SWT.CENTER);
		lbl_time.setText("单号:");
		lbl_time.setBounds((int)(4*w/5/25)+(int)(24*w/5/25)+(int)(42*w/5/25), (int)(2*h/9)+(int)(h/9/2), (int)(4*w/5/25), (int)(h/9/2));
		Text text_time = new Text(composite_right, SWT.NONE);
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
								
		Text total = new Text(composite_sum, SWT.NONE);
		total.setEnabled(false);
		total.setText("总计(大写):");
		total.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total.setLayoutData(gd_text);
		
		Text total_val = new Text(composite_sum, SWT.RIGHT|SWT.NONE);
		total_val.setEnabled(false);
		total_val.setText("总计(小写):               ");
		total_val.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		total_val.setLayoutData(gd_text2);
		
		Text indeed = new Text(composite_sum, SWT.NONE);
		indeed.setEnabled(false);
		indeed.setText("实收(大写):");
		indeed.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		indeed.setLayoutData(gd_text3);		
		
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
		
		
		
		//define a table
		final TableViewer tableViewer = new TableViewer(composite_right, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK		
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(0, (int)(h/3), (int)(4*w/5), (int)(h/3));
		
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
		tableViewer.setInput(deliverlist);		
		tableViewer.setColumnProperties(new String[]{"id","brand","sub_brand","size","unit","price", "number", "operation"});		
		cellEditor = new CellEditor[8];
		cellEditor[0] = null;//ID
//		cellEditor[1] = new TextCellEditor(tableViewer.getTable());		
//		cellEditor[2] = new TextCellEditor(tableViewer.getTable());
		GeneralComboCellEditor<String> comboboxCellEditor = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getBrands(), true);
		comboboxCellEditor.setActivationStyle(SWT.Expand);
		cellEditor[1] = comboboxCellEditor;		
		GeneralComboCellEditor<String> comboboxCellEditor2 = new GeneralComboCellEditor<String>(tableViewer.getTable(), Utils.getSub_Brands(), true);
		comboboxCellEditor2.setActivationStyle(SWT.Expand);
		cellEditor[2] = comboboxCellEditor2;
		cellEditor[3] = new TextCellEditor(tableViewer.getTable());
		cellEditor[4] = new TextCellEditor(tableViewer.getTable());
		cellEditor[5] = new TextCellEditor(tableViewer.getTable());
		cellEditor[6] = new TextCellEditor(tableViewer.getTable());				
		cellEditor[7] = new DeliverButtonCellEditor(tableViewer.getTable(), deliverlist, rowHeight);//ButtonCellEditor
//		cellEditor[7] = new ItemCompositeEditor(tableViewer.getTable(), 0, new Color(this.getDisplay(), 63,63,63),(int)(columnWidth*6/9)-3, rowHeight);//ButtonCellEditor
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
		
		//add Filter, no use now
		tableViewer.addFilter(new DeliverFilter());
		
		Utils.refreshTable(table);
		composite_right.setLayout(new FillLayout());
		

		

		

		

		

		

		
		
		
		
		
	}
}
