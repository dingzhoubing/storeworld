package com.storeworld.product;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.extenddialog.SoftKeyBoard;
import com.storeworld.mainui.ContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Utils;

/**
 * the main part of the product page
 * @author dingyuanxiong
 *
 */
public class ProductContentPart extends ContentPart{
	
	private static Table table;
	private static TableViewer tv;
	//the product list
	private static ProductList productlist = new ProductList();
	//the row height of the table
	final int rowHeight = 30;
	//define the cell Editor of each column
	private static CellEditor[] cellEditor = new CellEditor[7];
	private static TableEditor editor = null;
	private static TableEditor editorEdit = null;
	private static TableEditor editorDel = null;
	
	private Composite current = null;
	private Composite composite = null;
	//record the last hover on row number
	private static int visibleButton_last = -1;
	
	//record the column number of specified property
	private int sizeColumn = 3;
	private int repColumn = 5;
	private int deleteButtonColumn = 6;
	
	private int composite_shift = 0;
	private int composite_updown = 38;
	
	private static Button button_swkb = null;
	
	public static TableEditor getEditorDel(){
		return editorDel;
	}
	
	public ProductContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);	
		composite = new Composite(this, SWT.NONE);	
		current = parent;		
		initialization();
		addListenerForTable();
	}
	
	public static TableViewer getTableViewer(){
		return tv;
	}
		
	public static Button getButtonSWKB(){
		return button_swkb;
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
				
			if(Utils.getUseSoftKeyBoard()){									
				if(found){
					if(colCurrent == repColumn){//colCurrent ==sizeColumn || 
						//cannot reuse the editor, make cause unstable
						editorEdit.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
						Text text = (Text)(editorEdit.getEditor());	
						callKeyBoard(text);
						if(colCurrent == repColumn){
							if(Utils.getClickButton() && Utils.getInputNeedChange()){
								ProductCellModifier.staticModify(table.getItem(rowCurrent), "repository", Utils.getInput());
								//initial the next click
								Utils.setClickButton(false);
							}
						}
					}else if(colCurrent == deleteButtonColumn){
						if(rowCurrent == table.getItemCount()-1){
							editorDel.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(rowCurrent), deleteButtonColumn);
							if(!editorDel.getEditor().isDisposed())
								editorDel.getEditor().setVisible(false);
						}
					}
				}
		  }else{
				if(found){
					//if the deliver Button column, we disable the click
					if(colCurrent == deleteButtonColumn){
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
					editorDel.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(row), deleteButtonColumn);
					if(!editorDel.getEditor().isDisposed())
						editorDel.getEditor().setVisible(true);
				}else{
					if(visibleButton_last >= 0 && visibleButton_last < table.getItemCount()){
						editorDel.setEditor(cellEditor[deleteButtonColumn].getControl(), table.getItem(visibleButton_last), deleteButtonColumn);
						if(!editorDel.getEditor().isDisposed())
							editorDel.getEditor().setVisible(false);
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
		int w = current.getBounds().width;//960
		int h = current.getBounds().height;//570
		composite.setBounds(0, 0, w, h);
		
		 //right part		
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
		composite_right.setBounds(200, 0, 760, 570);
		composite_shift = 200;		
		//left side navigate
		Composite composite_left = new Composite(composite, SWT.NONE);
		final Color base = new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa);//??
		composite_left.setBackground(base);
		composite_left.setBounds(0, 0, 200, 570);

		//search button
		Button btnNewButton = new Button(composite_left, SWT.NONE);
		btnNewButton.setBounds(10, 10, 38, 19);
		btnNewButton.setText("��");
	
		//search text
		final Text text = new Text(composite_left, SWT.BORDER);
		text.setBounds(48, 10, 96, 19);		
		//click search button
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			
				ProductFilter.setKeyword(text.getText());
				//set clicked
				ProductUtils.setSearchButtonClicked(true);
				//add filter
				ProductUtils.showSearchedProducts();
			}
		});	
		
		//area label		
		Label lblNewLabel = new Label(composite_left, SWT.NONE);
		lblNewLabel.setBounds(10, 38, 38, 19);
		lblNewLabel.setFont(SWTResourceManager.getFont("΢���ź�", 12, SWT.NORMAL));
		lblNewLabel.setBackground(base);
		lblNewLabel.setText("Ʒ��");
		
		Link link_1 = new Link(composite_left, 0);
		link_1.setBounds(154, 38, 38, 19);
		link_1.setText("<a>ȫ��</a>");
		link_1.setFont(SWTResourceManager.getFont("΢���ź�", 11, SWT.NORMAL));
		link_1.setBackground(base);
		link_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					ProductUtils.showAllProducts();
				}
		});		
		
		//area base composite
		Composite composite_brand = new Composite(composite_left, SWT.NONE);
		composite_brand.setBounds(10, 58, 193, 400);
		composite_brand.setBackground(base);
		composite_brand.setLayout(new FillLayout());
		
		//area scroll composite
		final ScrolledComposite composite_scrollbrand = new ScrolledComposite(composite_brand,  SWT.H_SCROLL | SWT.V_SCROLL);
		composite_scrollbrand.setExpandHorizontal(true);  
		composite_scrollbrand.setExpandVertical(true);  
		final Composite composite_br = new Composite(composite_scrollbrand, SWT.NONE);
		composite_scrollbrand.setContent(composite_br);
		composite_br.setBackground(base);
		GridLayout layout = new GridLayout();  
        layout.numColumns = 2;  
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        //set the size of the gridlayout
        composite_br.setLayout(layout);  
        composite_scrollbrand.setMinSize(composite_br.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        composite_brand.layout();
		
		
		//whether to use the software keyboard
		button_swkb = new Button(composite_left, SWT.CHECK);
		button_swkb.setBounds(0, 545, 100, 20);
		button_swkb.setText("�������ּ���");
		button_swkb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(button_swkb.getSelection()){
					Utils.settUseSoftKeyBoard(true);
				}
				else{
					ProductUtils.refreshTableData();
					Utils.settUseSoftKeyBoard(false);
				}
			}
		});
		
		//define a table
		final TableViewer tableViewer = new TableViewer(composite_right, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK		
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(0, 0, 760, 570);
		tv = tableViewer;
		//set the columns of the table

		int columnWidth = 132;
		final TableColumn newColumnTableColumn_ID = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_ID.setWidth(0);
		newColumnTableColumn_ID.setMoveable(false);
		newColumnTableColumn_ID.setResizable(false);
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(columnWidth);
		newColumnTableColumn_1.setMoveable(false);
		newColumnTableColumn_1.setResizable(false);
		newColumnTableColumn_1.setText("Ʒ�� ");
		//add listener
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?ProductSorter.BRAND_ASC:ProductSorter.BRAND_DESC);
				asc = !asc;				
				Utils.refreshTable(table);
			}
		});

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(columnWidth);
		newColumnTableColumn_2.setMoveable(false);
		newColumnTableColumn_2.setResizable(false);
		newColumnTableColumn_2.setText("��Ʒ��");
		newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?ProductSorter.SUB_BRAND_ASC:ProductSorter.SUB_BRAND_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		
		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(columnWidth);
		newColumnTableColumn_3.setMoveable(false);
		newColumnTableColumn_3.setResizable(false);
		newColumnTableColumn_3.setText("���");
		newColumnTableColumn_3.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?ProductSorter.SIZE_ASC:ProductSorter.SIZE_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		
		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(columnWidth);
		newColumnTableColumn_4.setMoveable(false);
		newColumnTableColumn_4.setResizable(false);
		newColumnTableColumn_4.setText("��λ");
		newColumnTableColumn_4.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?ProductSorter.UNIT_ASC:ProductSorter.UNIT_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
			
		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth(columnWidth);
		newColumnTableColumn_5.setMoveable(false);
		newColumnTableColumn_5.setResizable(false);
		newColumnTableColumn_5.setText("���");
		newColumnTableColumn_5.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?ProductSorter.REPOSITORY_ASC:ProductSorter.REPOSITORY_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		
		final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setWidth(85);
		newColumnTableColumn_6.setText("");
		newColumnTableColumn_6.setMoveable(false);
		newColumnTableColumn_6.setResizable(false);		
		
		//set the editor of the table columns
		tableViewer.setContentProvider(new ProductContentProvider(tableViewer, productlist));
		tableViewer.setLabelProvider(new ProductTableLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up		
		
		Product prod_new = new Product(ProductUtils.getNewLineID());//dynamic from the database
		productlist.addProduct(prod_new);
		
		tableViewer.setInput(productlist);		
		tableViewer.setColumnProperties(new String[]{"id","brand","sub_brand","size","unit","repository","operation"});		
		cellEditor = new CellEditor[7];
		cellEditor[0] = null;//ID
		cellEditor[1] = new ProductTextCellEditor(tableViewer.getTable(), columnWidth, 1);		
		cellEditor[2] = new ProductTextCellEditor(tableViewer.getTable(), columnWidth, 2);
		cellEditor[3] = new ProductTextCellEditor(tableViewer.getTable(), columnWidth, 3);
		cellEditor[4] = new ProductTextCellEditor(tableViewer.getTable(), columnWidth, 4);
		cellEditor[5] = new ProductTextCellEditor(tableViewer.getTable(), columnWidth, 5);
		cellEditor[6] = new ProductButtonCellEditor(tableViewer.getTable(), productlist, rowHeight);//ButtonCellEditor
		tableViewer.setCellEditors(cellEditor);
		
		//initial the editor for hover and set the cell modifier
		editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.CENTER;
	    editor.grabHorizontal = true;		
		editorEdit = new TableEditor(table);
		editorEdit.horizontalAlignment = SWT.CENTER;
		editorEdit.grabHorizontal = true;	

		editorDel = new TableEditor(table);
	    editorDel.horizontalAlignment = SWT.CENTER;
	    editorDel.grabHorizontal = true;
	    
		ICellModifier modifier = new ProductCellModifier(tableViewer, productlist);
		tableViewer.setCellModifier(modifier);
		
		ProductUtils.showBrandCheckBoxes(composite_br, 77, composite_scrollbrand, tableViewer, base);
		
		Utils.refreshTable(table);
		composite.setLayout(new FillLayout());
		
	}
}
