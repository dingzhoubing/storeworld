package com.storeworld.customer;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
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
import com.storeworld.utils.Utils;
public class CustomerContentPart extends ContentPart{
	
	private static Table table;
	//the product list
	private static CustomerList customerlist = new CustomerList();
	//the row height of the table
	final int rowHeight = 30;
	//define the cell Editor of each column
	private static CellEditor[] cellEditor = new CellEditor[6];
	private static TableEditor editor = null;
	private static TableEditor editorEdit = null;//software number keyboard
	
	private Composite current = null;
	private Composite composite = null;
	//record the last hover on row number
	private static int visibleButton_last = -1;
	private int composite_shift = 0;
	
	private int phoneColumn = 3;
	
	public CustomerContentPart(Composite parent, int style, Image image, Color color) {
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
		SoftKeyBoard skb = new SoftKeyBoard(text, table.getParent().getShell(), 0, composite_shift, 0);
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
					if(colCurrent == phoneColumn){
					//cannot reuse the editor, make cause unstable
					editorEdit.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
					Text text = (Text)(editorEdit.getEditor());	
					callKeyBoard(text);
					Customer c = (Customer)(table.getItem(rowCurrent).getData());					
					if(Utils.getClickButton() && Utils.getInputNeedChange())
						c.setPhone(Utils.getInput());
					
					if(Utils.getClickButton() && Utils.getInputNeedChange()){
						customerlist.customerChanged(c);		
						//initial the next click
						Utils.setClickButton(false);
					}
					//add message, no use later
					MessageBox messageBox =   
							   new MessageBox(new Shell(),   					     
							    SWT.ICON_WARNING);   
					messageBox.setMessage("change product: "+c);   
					messageBox.open(); 
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
					editor.setEditor(cellEditor[5].getControl(), table.getItem(row), 5);
					if(!editor.getEditor().isDisposed())
						editor.getEditor().setVisible(true);
				}else{
					if(visibleButton_last >= 0 && visibleButton_last < table.getItemCount()){
						editor.setEditor(cellEditor[5].getControl(), table.getItem(visibleButton_last), 5);
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
	 * after sort of the table column, refresh the table to show it
	 */
	public static void refreshTable(){
//		System.out.println("table size: "+table.getItemCount());
		Color color1 = new Color(table.getDisplay(), 255, 245, 238);
		Color color2 = new Color(table.getDisplay(), 255, 250, 250);
		for(int i=0;i<table.getItemCount();i++){
			TableItem item = table.getItem(i);
			if(i%2 == 0){
//				System.out.println("set row: "+i);
				item.setBackground(color1);
			}else{
				item.setBackground(color2);
			}
		}
		table.redraw();
	}
	
	/**
	 * initialize the table elements
	 */
	public void initialization(){
		final int w = current.getBounds().width;
		int h = current.getBounds().height;
		composite.setBounds(0, 0, w, h);
		
		//left side navigate
		Composite composite_left = new Composite(composite, SWT.NONE);
		final Color base = new Color(composite.getDisplay(), 255,240,245);
		composite_left.setBackground(base);
		composite_left.setBounds(0, 0, (int)(w/5), h);

		//search button
		Button btnNewButton = new Button(composite_left, SWT.NONE);
		btnNewButton.setBounds((int)(w/5/20), (int)(w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		btnNewButton.setText("搜");
		//search text
		Text text = new Text(composite_left, SWT.BORDER);
		text.setBounds((int)(w/5/20)+(int)(2*3*w/5/10/3), (int)(w/5/10/2), (int)(5*w/5/10), (int)(2*w/5/10/2));		
		//area label		
		Label lblNewLabel = new Label(composite_left, SWT.NONE);
		lblNewLabel.setBounds((int)(w/5/20), (int)(2*w/5/10/2)+(int)(2*w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lblNewLabel.setBackground(base);
		lblNewLabel.setText("片区");
		
		Link link_1 = new Link(composite_left, 0);
		link_1.setBounds((int)(w/5)-(int)(4*w/5/20), (int)(2*w/5/10/2)+(int)(2*w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		link_1.setText("<a>全部</a>");
		link_1.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		link_1.setBackground(base);
		link_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox =   
						new MessageBox(new Shell(),   					     
							   SWT.ICON_WARNING);   
						messageBox.setMessage("显示所有客户"); 
						messageBox.open(); 
				}
		});		
		
		//area base composite
		Composite composite_area = new Composite(composite_left, SWT.NONE);
		composite_area.setBounds((int)(w/5/20), (int)(3*w/5/10), (int)(9*w/5/10), (int)(2*(h-3*w/50)/5));
		composite_area.setBackground(base);
		composite_area.setLayout(new FillLayout());
		
		//area scroll composite
		final ScrolledComposite composite_scrollarea = new ScrolledComposite(composite_area,  SWT.H_SCROLL | SWT.V_SCROLL);
		composite_scrollarea.setExpandHorizontal(true);  
		composite_scrollarea.setExpandVertical(true);  
		final Composite composite_ar = new Composite(composite_scrollarea, SWT.NONE);
		composite_scrollarea.setContent(composite_ar);
		composite_ar.setBackground(base);
		GridLayout layout = new GridLayout();  
        layout.numColumns = 2;  
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        
        composite_ar.setLayout(layout);  
        composite_scrollarea.setMinSize(composite_ar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        Button b1 = new Button(composite_ar, SWT.CHECK);
        GridData gd_b = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_b.widthHint = (int)(4*w/5/10);
		b1.setLayoutData(gd_b);
        b1.setText("八里街");
        b1.setBackground(base);

		Button b2 = new Button(composite_ar, SWT.CHECK);
        GridData gd_b2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_b2.widthHint = (int)(4*w/5/10);
		b2.setLayoutData(gd_b2);
		b2.setText("安陆");//安陆这是一个很长的片区
		b2.setBackground(base);
		composite_scrollarea.setMinSize(composite_ar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite_ar.layout();
		
		//first name label
		Label label = new Label(composite_left, SWT.NONE);
		label.setBounds((int)(w/5/20), (int)(3*w/5/10)+(int)(2*(h-3*w/50)/5)+(int)(w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		label.setText("姓氏");
		label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		label.setBackground(base);
		//show all of the customer
		Link link = new Link(composite_left, 0);
		link.setText("<a>全部</a>");
		link.setBounds((int)(w/5)-(int)(4*w/5/20), (int)(3*w/5/10)+(int)(2*(h-3*w/50)/5)+(int)(w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		link.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
		link.setBackground(base);   
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox =   
						   new MessageBox(new Shell(),   					     
						    SWT.ICON_WARNING);   
				messageBox.setMessage("显示所有客户"); 
				messageBox.open(); 
			}
		});
		
		//first name scroll composite
        Composite composite_firstname = new Composite(composite_left, SWT.NONE);
        composite_firstname.setBounds((int)(w/5/20), (int)(4*w/5/10)+(int)(2*(h-3*w/50)/5)+(int)(w/5/10/2), (int)(9*w/5/10), (int)((h-3*w/50)/2));
        composite_firstname.setLayout(new FillLayout());
        composite_firstname.setBackground(base);
        
		final ScrolledComposite composite_scroll = new ScrolledComposite(composite_firstname,  SWT.H_SCROLL | SWT.V_SCROLL);
		composite_scroll.setExpandHorizontal(true);  
		composite_scroll.setExpandVertical(true);  
		final Composite composite_fn = new Composite(composite_scroll, SWT.NONE);
		composite_scroll.setContent(composite_fn);
		composite_fn.setBackground(base);
		
		GridLayout layout2 = new GridLayout();  
        layout2.numColumns = 2;  
        layout2.horizontalSpacing = 0;
        layout2.verticalSpacing = 0;
        layout2.marginHeight = 0;
        layout2.marginWidth = 0;
        composite_fn.setLayout(layout2);  
        composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		 btnNewButton.addListener(SWT.Selection, new Listener() {  
	            public void handleEvent(Event e) {  	             
	                Button button = new Button(composite_fn, SWT.CHECK);  
	                GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
	                gd.widthHint = (int)(4*w/5/10);
	                button.setLayoutData(gd);
	                button.setText("姓");  
	                button.setBackground(base);
	                composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
	                composite_fn.layout();  
	            }  
	        });	
		
		Button btnCheckButton = new Button(composite_fn, SWT.CHECK);
		btnCheckButton.setText("欧阳测试");
		btnCheckButton.setBackground(base);

		Button button = new Button(composite_fn, SWT.CHECK);
		GridData gd_b3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_b3.widthHint = (int)(4*w/5/10);
		button.setLayoutData(gd_b3);
		button.setText("钱");
		button.setBackground(base);
		
		Button button_1 = new Button(composite_fn, SWT.CHECK);
		GridData gd_b4 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_b4.widthHint = (int)(4*w/5/10);
		button_1.setLayoutData(gd_b4);
		button_1.setText("孙");
		button_1.setBackground(base);
		
		Button button_2 = new Button(composite_fn, SWT.CHECK);
		GridData gd_b5 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_b5.widthHint = (int)(4*w/5/10);
		button_2.setLayoutData(gd_b5);
		button_2.setText("李");
		button_2.setBackground(base);
		
		Button button_3 = new Button(composite_fn, SWT.CHECK);
		GridData gd_b6 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_b6.widthHint = (int)(4*w/5/10);
		button_3.setLayoutData(gd_b6);
		button_3.setText("多");//次点搜，有垂直滚动条
		button_3.setBackground(base);
		
		composite_scroll.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
        composite_fn.layout();  
		
	   composite_area.layout();
	   composite_firstname.layout();
		

	 //right part		
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		composite_right.setBounds((int)(w/5), 0, (int)(4*w/5), h);		
		composite_shift = (int)(w/5);
		
		//define a table
		final TableViewer tableViewer = new TableViewer(composite_right, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK		
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(0, 0, (int)(4*w/5), h);
		
		//set the columns of the table
		int columnWidth = (int)(4*9*w/40/5);		
		final TableColumn newColumnTableColumn_ID = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_ID.setWidth(0);
		newColumnTableColumn_ID.setMoveable(false);
		newColumnTableColumn_ID.setResizable(false);
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(columnWidth);
		newColumnTableColumn_1.setMoveable(false);
		newColumnTableColumn_1.setResizable(false);
		newColumnTableColumn_1.setText("姓名");
//		newColumnTableColumn.setImage(new Image(display,"title.png"));
		//add listener
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?CustomerSorter.NAME_ASC:CustomerSorter.NAME_DESC);
				asc = !asc;				
				refreshTable();
			}
		});

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(columnWidth);
		newColumnTableColumn_2.setMoveable(false);
		newColumnTableColumn_2.setResizable(false);
		newColumnTableColumn_2.setText("片区");
		newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?CustomerSorter.AREA_ASC:CustomerSorter.AREA_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(columnWidth);
		newColumnTableColumn_3.setMoveable(false);
		newColumnTableColumn_3.setResizable(false);
		newColumnTableColumn_3.setText("电话");
		newColumnTableColumn_3.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?CustomerSorter.PHONE_ASC:CustomerSorter.PHONE_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(columnWidth);
		newColumnTableColumn_4.setMoveable(false);
		newColumnTableColumn_4.setResizable(false);
		newColumnTableColumn_4.setText("地址");
		newColumnTableColumn_4.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?CustomerSorter.ADDRESS_ASC:CustomerSorter.ADDRESS_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		
		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth((int)(columnWidth*4/9)-3);//columnWidth*5/9)
//		buttonWidth = (int)(columnWidth*5/9/2);
		newColumnTableColumn_5.setText("");
		newColumnTableColumn_5.setMoveable(false);
		newColumnTableColumn_5.setResizable(false);		
		
		
		//set the editor of the table columns
		tableViewer.setContentProvider(new CustomerContentProvider(tableViewer, customerlist));
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up
		tableViewer.setInput(customerlist);		
		tableViewer.setColumnProperties(new String[]{"id","name","area","phone","address","operation"});		
		cellEditor = new CellEditor[6];
		cellEditor[0] = null;//ID
		cellEditor[1] = new TextCellEditor(tableViewer.getTable());		
		cellEditor[2] = new TextCellEditor(tableViewer.getTable());
		cellEditor[3] = new TextCellEditor(tableViewer.getTable());
		cellEditor[4] = new TextCellEditor(tableViewer.getTable());
		cellEditor[5] = new CustomerButtonCellEditor(tableViewer.getTable(), customerlist, rowHeight);//ButtonCellEditor
		tableViewer.setCellEditors(cellEditor);
		
		//initial the editor for hover and set the cell modifier
		editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.CENTER;
	    editor.grabHorizontal = true;		
		editorEdit = new TableEditor(table);
		editorEdit.horizontalAlignment = SWT.CENTER;
		editorEdit.grabHorizontal = true;	

		ICellModifier modifier = new MyCustomerCellModifier(tableViewer, customerlist);
		tableViewer.setCellModifier(modifier);
		
		//add Filter, no use now
		tableViewer.addFilter(new MyCustomerFilter());
		
		refreshTable();
		composite_right.setLayout(new FillLayout());
		
	}
}
