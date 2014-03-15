package com.storeworld.product;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.storeworld.mainui.ContentPart;
import com.storeworld.softwarekeyboard.SoftKeyBoard;
import com.storeworld.softwarekeyboard.SoftKeyBoard;
import com.storeworld.utils.Utils;
public class ProductContentPart extends ContentPart{
	
	private static Table table;
	//the product list
	private static ProductList productlist = new ProductList();
	//the row height of the table
	final int rowHeight = 30;
	//define the cell Editor of each column
	private static CellEditor[] cellEditor = new CellEditor[7];
	private static TableEditor editor = null;
	private static TableEditor editorEdit = null;//software number keyboard
	
	private Composite current = null;
	private Composite composite = null;
	//record the last hover on row number
	private static int visibleButton_last = -1;
	
	private int sizeColumn = 3;
	private int repColumn = 5;
	public ProductContentPart(Composite parent, int style, Image image, Color color) {
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
		SoftKeyBoard skb = new SoftKeyBoard(text, table.getParent().getShell(), 0);
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
					if(colCurrent ==sizeColumn || colCurrent == repColumn){
					//cannot reuse the editor, make cause unstable
					editorEdit.setEditor(cellEditor[colCurrent].getControl(), table.getItem(rowCurrent), colCurrent);
					Text text = (Text)(editorEdit.getEditor());	
					callKeyBoard(text);
					Product p = (Product)(table.getItem(rowCurrent).getData());
					if(colCurrent == sizeColumn){
						if(Utils.getClickButton() && Utils.getInputNeedChange())
							p.setSize(Utils.getInput()+"kg");
					}else if(colCurrent == repColumn){
						if(Utils.getClickButton() && Utils.getInputNeedChange())
							p.setRepository(Integer.valueOf(Utils.getInput()));
					}
					if(Utils.getClickButton() && Utils.getInputNeedChange()){
						productlist.productChanged(p);		
						//initial the next click
						Utils.setClickButton(false);
					}
					//add message, no use later
					MessageBox messageBox =   
							   new MessageBox(new Shell(),   					     
							    SWT.ICON_WARNING);   
					messageBox.setMessage("change product: "+p);   
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
					editor.setEditor(cellEditor[6].getControl(), table.getItem(row), 6);
					if(!editor.getEditor().isDisposed())
						editor.getEditor().setVisible(true);
				}else{
					if(visibleButton_last >= 0 && visibleButton_last < table.getItemCount()){
						editor.setEditor(cellEditor[6].getControl(), table.getItem(visibleButton_last), 6);
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
		Text text = (Text)cellEditor[4].getControl();
		text.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e){
				String inStr = e.text;
				if (inStr.length() > 0){
					try{
						if(inStr.equals("包"))
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
		int w = current.getBounds().width;
		int h = current.getBounds().height;
		composite.setBounds(0, 0, w, h);
		
		//define a table
		final TableViewer tableViewer = new TableViewer(composite, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK		
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(0, 0, w, h);
		
		//set the columns of the table
		int columnWidth = (int)(9*w/50);		
		final TableColumn newColumnTableColumn_ID = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_ID.setWidth(0);
		newColumnTableColumn_ID.setMoveable(false);
		newColumnTableColumn_ID.setResizable(false);
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(columnWidth);
		newColumnTableColumn_1.setMoveable(false);
		newColumnTableColumn_1.setResizable(false);
		newColumnTableColumn_1.setText("品牌 ");
//		newColumnTableColumn.setImage(new Image(display,"title.png"));
		//add listener
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.BRAND_ASC:Sorter.BRAND_DESC);
				asc = !asc;				
				refreshTable();
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
				tableViewer.setSorter(asc?Sorter.SUB_BRAND_ASC:Sorter.SUB_BRAND_DESC);
				asc = !asc;
				refreshTable();
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
				tableViewer.setSorter(asc?Sorter.SIZE_ASC:Sorter.SIZE_DESC);
				asc = !asc;
				refreshTable();
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
				tableViewer.setSorter(asc?Sorter.UNIT_ASC:Sorter.UNIT_DESC);
				asc = !asc;
				refreshTable();
			}
		});
			
		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth(columnWidth);
		newColumnTableColumn_5.setMoveable(false);
		newColumnTableColumn_5.setResizable(false);
		newColumnTableColumn_5.setText("库存");
		newColumnTableColumn_5.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.REPOSITORY_ASC:Sorter.REPOSITORY_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setWidth((int)(columnWidth*5/9)-2);//columnWidth*5/9)
//		buttonWidth = (int)(columnWidth*5/9/2);
		newColumnTableColumn_6.setText("");
		newColumnTableColumn_6.setMoveable(false);
		newColumnTableColumn_6.setResizable(false);		
		
		
		//set the editor of the table columns
		tableViewer.setContentProvider(new ContentProvider(tableViewer, productlist));
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up
		tableViewer.setInput(productlist);		
		tableViewer.setColumnProperties(new String[]{"id","brand","sub_brand","size","unit","repository","operation"});		
		cellEditor = new CellEditor[7];
		cellEditor[0] = null;//ID
		cellEditor[1] = new TextCellEditor(tableViewer.getTable());		
		cellEditor[2] = new TextCellEditor(tableViewer.getTable());
		cellEditor[3] = new TextCellEditor(tableViewer.getTable());
		cellEditor[4] = new TextCellEditor(tableViewer.getTable());
		cellEditor[5] = new TextCellEditor(tableViewer.getTable());
		cellEditor[6] = new ButtonCellEditor(tableViewer.getTable(), productlist, rowHeight);//ButtonCellEditor
		tableViewer.setCellEditors(cellEditor);
		
		//initial the editor for hover and set the cell modifier
		editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.CENTER;
	    editor.grabHorizontal = true;		
		editorEdit = new TableEditor(table);
		editorEdit.horizontalAlignment = SWT.CENTER;
		editorEdit.grabHorizontal = true;	

		ICellModifier modifier = new MyCellModifier(tableViewer, productlist);
		tableViewer.setCellModifier(modifier);
		
		//add Filter, no use now
		tableViewer.addFilter(new MyFilter());
		
		refreshTable();
		composite.setLayout(new FillLayout());
		
	}
}
