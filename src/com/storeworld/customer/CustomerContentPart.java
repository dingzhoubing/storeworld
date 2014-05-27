package com.storeworld.customer;

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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.extenddialog.SoftKeyBoard;
import com.storeworld.mainui.ContentPart;
import com.storeworld.utils.Utils;

/**
 * the main of the customer page
 * @author dingyuanxiong
 * need to do: dispose control, color control
 *
 */
public class CustomerContentPart extends ContentPart{
	
	private static Table table;
	private static TableViewer tv;
	//the product list
	private static CustomerList customerlist = new CustomerList();
	//the row height of the table
	final int rowHeight = 30;
	//define the cell Editor of each column
	private static CellEditor[] cellEditor = new CellEditor[7];
	private static TableEditor editor = null;
	private static TableEditor editorDel = null;
	private static TableEditor editorEdit = null;//software number keyboard	
	
	private Composite current = null;
	private Composite composite = null;
	//record the last hover on row number
	private static int visibleButton_last = -1;
	
	//the shift width of the table, determine the location of the soft keyboard
	private int composite_shift = 0;
	
	//since there is a column: deliver button, the tooltip need to shift
	private int column_shift = 0;
	
	private int phoneColumn = 4;
	private int deliverButtonColumn = 1;
	private int deleteButtonColumn = 6;
	
	private static Listener listener = null;
	

	
	/**
	 * constructor
	 * @param parent
	 * @param style
	 * @param image
	 * @param color
	 */
	public CustomerContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);	
		composite = new Composite(this, SWT.NONE);	
		current = parent;		
		initialization();
		addListenerForTable();
	}
	
	public static CellEditor[] getCellEditor(){
		return cellEditor;
	}
	public static TableEditor getEditor(){
		return editor;
	}
	public static TableEditor getEditorDel(){
		return editorDel;
	}
	public static TableViewer getTableViewer(){
		return tv;
	}
	public static void removeMouseDownListener(){
		table.removeListener(SWT.MouseDown, listener);
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
		
//		click to show the soft keyboard
//		table.addListener(SWT.MouseDown, listener);
		table.addListener(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(Event event) {
				//no matter use the software keyboard or not, we need to catch the mouse point
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
				//if use the soft keyboard
				if(Utils.getUseSoftKeyBoard()){											
					if(found){
						if(colCurrent == phoneColumn){
							//cannot reuse the editor, make cause unstable
							editorEdit.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
							Text text = (Text)(editorEdit.getEditor());	
							callKeyBoard(text);
							Customer c = (Customer)(table.getItem(rowCurrent).getData());					
							String phonelast = c.getPhone();
							////
							if(Utils.getClickButton() && Utils.getInputNeedChange()){
								c.setPhone(Utils.getInput());
								text.setText(c.getPhone());//validate the text
								if(CustomerValidator.validatePhone(c.getPhone())){
									customerlist.customerChanged(c);	
									text.setText(c.getPhone());
								}else{
									c.setPhone(phonelast);
								}
								//initial the next click
								Utils.setClickButton(false);
							}
						}else if(colCurrent == deliverButtonColumn){
							if(rowCurrent == table.getItemCount()-1){
								editor.setEditor(cellEditor[deliverButtonColumn].getControl(), table.getItem(rowCurrent), deliverButtonColumn);
								if(!editor.getEditor().isDisposed()){
									editor.getEditor().setVisible(false);
								}
							}
						}else if(colCurrent == deleteButtonColumn){
							if(rowCurrent == table.getItemCount()-1){
								editorDel.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(rowCurrent), deleteButtonColumn);
								if(!editorDel.getEditor().isDisposed()){
									editorDel.getEditor().setVisible(false);
								}
							}
						}
				}
			}else{//do not use the software keyboard

				if(found){
					//if the deliver Button column, we disable the click
					if(colCurrent == deliverButtonColumn){
						if(rowCurrent == table.getItemCount()-1){
							editor.setEditor(cellEditor[deliverButtonColumn].getControl(), table.getItem(rowCurrent), deliverButtonColumn);
							if(!editor.getEditor().isDisposed()){
								editor.getEditor().setVisible(false);
							}
						}
					}else if(colCurrent == deleteButtonColumn){
						if(rowCurrent == table.getItemCount()-1){
							editorDel.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(rowCurrent), deleteButtonColumn);
							if(!editorDel.getEditor().isDisposed()){
								editorDel.getEditor().setVisible(false);
							}
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
					editor.setEditor(cellEditor[deliverButtonColumn].getControl(), table.getItem(row), deliverButtonColumn);
					if(!editor.getEditor().isDisposed()){
						editor.getEditor().setVisible(true);
					}
					
					editorDel.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(row), deleteButtonColumn);
					if(!editorDel.getEditor().isDisposed()){
						editorDel.getEditor().setVisible(true);
					}
					
				}
				else{
					if(visibleButton_last >= 0 && visibleButton_last < table.getItemCount()-1){
						editor.setEditor(cellEditor[deliverButtonColumn].getControl(), table.getItem(visibleButton_last), deliverButtonColumn);
						if(!editor.getEditor().isDisposed()){
							editor.getEditor().setVisible(false);//false
						}
						editorDel.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(visibleButton_last), deleteButtonColumn);
						if(!editorDel.getEditor().isDisposed()){
							editorDel.getEditor().setVisible(false);
						}
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
		int h = current.getBounds().height;
		composite.setBounds(0, 0, w, h);
	    //right part		
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
//		composite_right.setBounds((int)(w/5), 0, (int)(4*w/5), h);
		composite_right.setBounds(200, 0, 760, h);
		composite_shift = (int)(w/5);
		final TableViewer tableViewer = new TableViewer(composite_right, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK
		tv=tableViewer;
		//left side navigate
		Composite composite_left = new Composite(composite, SWT.NONE);
		final Color base = new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa);//??
		composite_left.setBackground(base);
		composite_left.setBounds(0, 0, 200, h);

		//search button
		Button btnNewButton = new Button(composite_left, SWT.NONE);
		btnNewButton.setBounds((int)(w/5/20), (int)(w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		btnNewButton.setText("ËÑ");
	
		//search text
		final Text text = new Text(composite_left, SWT.BORDER);
		text.setBounds((int)(w/5/20)+(int)(2*3*w/5/10/3), (int)(w/5/10/2), (int)(5*w/5/10), (int)(2*w/5/10/2));		
		//click search button
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//set key word
				CustomerFilter.setKeyword(text.getText());
				//set clicked
				CustomerUtils.setSearchButtonClicked(true);
				//add filter
				CustomerUtils.showSearchedCustomers();
			}
		});	
		
		//area label		
		Label lblNewLabel = new Label(composite_left, SWT.NONE);
		lblNewLabel.setBounds((int)(w/5/20), (int)(2*w/5/10/2)+(int)(2*w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		lblNewLabel.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		lblNewLabel.setBackground(base);
		lblNewLabel.setText("Æ¬Çø");
		
		Link link_1 = new Link(composite_left, 0);
		link_1.setBounds((int)(w/5)-(int)(4*w/5/20), (int)(2*w/5/10/2)+(int)(2*w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		link_1.setText("<a>È«²¿</a>");
		link_1.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 11, SWT.NORMAL));
		link_1.setBackground(base);
		link_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					CustomerUtils.showAllCustomers();
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
        //set the size of the gridlayout
        composite_ar.setLayout(layout);  
        composite_scrollarea.setMinSize(composite_ar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
           
		//first name label
		Label label = new Label(composite_left, SWT.NONE);
		label.setBounds((int)(w/5/20), (int)(3*w/5/10)+(int)(2*(h-3*w/50)/5)+(int)(w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		label.setText("ÐÕÊÏ");
		label.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label.setBackground(base);
		//show all of the customer
		Link link = new Link(composite_left, 0);
		link.setText("<a>È«²¿</a>");
		link.setBounds((int)(w/5)-(int)(4*w/5/20), (int)(3*w/5/10)+(int)(2*(h-3*w/50)/5)+(int)(w/5/10/2), (int)(2*3*w/5/10/3), (int)(2*w/5/10/2));
		link.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 11, SWT.NORMAL));
		link.setBackground(base);   
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {	
				CustomerUtils.showAllCustomers();
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
        composite_fn.layout();  
		
	    composite_area.layout();
	    composite_firstname.layout();
		
		//whether to use the software keyboard
		Button button_swkb = new Button(composite_left, SWT.CHECK);
		button_swkb.setBounds(0, 545, 100, 20);
		button_swkb.setText("ÆôÓÃÊý×Ö¼üÅÌ");
		
	    //==========================================================================================================
		//define a table, right part				
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(0, 0, (int)(4*w/5), h);
		
		//set the columns of the table
		int columnWidth = (int)(4*9*w/40/5- 4*w/40/5);		
		final TableColumn newColumnTableColumn_ID = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_ID.setWidth(0);
		newColumnTableColumn_ID.setMoveable(false);
		newColumnTableColumn_ID.setResizable(false);
		
		//the new column for delivering 
		final TableColumn newColumnTableColumn_Deliver = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_Deliver.setWidth((int)(4*4*w/40/5));
//		buttonWidth = (int)(columnWidth*5/9/2);
		newColumnTableColumn_Deliver.setText("");
		newColumnTableColumn_Deliver.setMoveable(false);
		newColumnTableColumn_Deliver.setResizable(false);	
		column_shift = (int)(4*4*w/40/5);
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(columnWidth);
		newColumnTableColumn_1.setMoveable(false);
		newColumnTableColumn_1.setResizable(false);
		newColumnTableColumn_1.setText("ÐÕÃû");
//		newColumnTableColumn.setImage(new Image(display,"title.png"));
		//add listener
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?CustomerSorter.NAME_ASC:CustomerSorter.NAME_DESC);
				asc = !asc;				
				Utils.refreshTable(table);
			}
		});

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(columnWidth);
		newColumnTableColumn_2.setMoveable(false);
		newColumnTableColumn_2.setResizable(false);
		newColumnTableColumn_2.setText("Æ¬Çø");
		newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?CustomerSorter.AREA_ASC:CustomerSorter.AREA_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		
		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(columnWidth);
		newColumnTableColumn_3.setMoveable(false);
		newColumnTableColumn_3.setResizable(false);
		newColumnTableColumn_3.setText("µç»°");
		newColumnTableColumn_3.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?CustomerSorter.PHONE_ASC:CustomerSorter.PHONE_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		
		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(columnWidth);
		newColumnTableColumn_4.setMoveable(false);
		newColumnTableColumn_4.setResizable(false);
		newColumnTableColumn_4.setText("µØÖ·");
		newColumnTableColumn_4.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?CustomerSorter.ADDRESS_ASC:CustomerSorter.ADDRESS_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		
		
		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth((int)((int)(4*9*w/40/5)*4/9)-3);//columnWidth*5/9)
//		buttonWidth = (int)(columnWidth*5/9/2);
		newColumnTableColumn_5.setText("");
		newColumnTableColumn_5.setMoveable(false);
		newColumnTableColumn_5.setResizable(false);		
		
		
		//set the editor of the table columns
		customerlist = new CustomerList(table);
		tableViewer.setContentProvider(new CustomerContentProvider(tableViewer, customerlist));
		tableViewer.setLabelProvider(new CustomerTableLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up
		//always get the ID from the database!
		
		Customer cus_new = new Customer(CustomerUtils.getNewLineID());//dynamic from the database
		
		//always keep a new line in the last of the table 
//		CustomerValidator.setNewID(CustomerUtils.getNewLineID());
		customerlist.addCustomer(cus_new);
		tableViewer.setInput(customerlist);		
		tableViewer.setColumnProperties(new String[]{"id","deliver","name","area","phone","address","operation"});		
		cellEditor = new CellEditor[7];
		cellEditor[0] = null;//ID
		cellEditor[1] = new CustomerDeliverButtonCellEditor(tableViewer.getTable(), customerlist, rowHeight);
		cellEditor[2] = new CustomerTextCellEditor(tableViewer.getTable(), columnWidth, 2, column_shift);		
		cellEditor[3] = new CustomerTextCellEditor(tableViewer.getTable(), columnWidth, 3, column_shift);
		cellEditor[4] = new CustomerTextCellEditor(tableViewer.getTable(), columnWidth, 4, column_shift);
		cellEditor[5] = new CustomerTextCellEditor(tableViewer.getTable(), columnWidth, 5, column_shift);
		cellEditor[6] = new CustomerButtonCellEditor(tableViewer.getTable(), customerlist, rowHeight);//ButtonCellEditor
		tableViewer.setCellEditors(cellEditor);
		
		//initial the editor for hover and set the cell modifier
		editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.CENTER;
	    editor.grabHorizontal = true;	
	    
		editorDel = new TableEditor(table);
	    editorDel.horizontalAlignment = SWT.CENTER;
	    editorDel.grabHorizontal = true;
	    
		editorEdit = new TableEditor(table);
		editorEdit.horizontalAlignment = SWT.CENTER;
		editorEdit.grabHorizontal = true;	
		
		ICellModifier modifier = new CustomerCellModifier(tableViewer, customerlist);
		tableViewer.setCellModifier(modifier);
		
		Utils.refreshTable(table);
		composite_right.setLayout(new FillLayout());
		
		//show the area checkbox button
        CustomerUtils.showAreaCheckBoxes(composite_ar, (int)(4*w/5/10), composite_scrollarea, tableViewer, base);
        //show all the firstname checkbox button
        //always under the area call
        CustomerUtils.showFirstNameCheckBoxes(composite_fn, (int)(4*w/5/10), composite_scroll, tableViewer, base);
		
		
		
	}
}
