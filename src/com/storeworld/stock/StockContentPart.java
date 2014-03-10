package com.storeworld.stock;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.mainui.ContentPart;

public class StockContentPart extends ContentPart{
	
	private static Table table;
	private static Text txtTotal;
	private static Table table_1;
	
	public static void refreshTable(){
		Color color1 = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		for(int i=0;i<table.getItemCount();i++){
			if(i%2 == 0){
				table.getItems()[i].setBackground(color1);
			}
		}
		table.redraw();
	}
	
	private int FONT_SIZE = 12;
	private Composite current = null;
	private Composite base = null;  
	public StockContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		base = new Composite(this, SWT.NONE);
		current = parent;		
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;
		base.setBounds(0, 0, w, h);
		Composite composite_left = new Composite(base, SWT.NONE);
		composite_left.setBackground(SWTResourceManager.getColor(255, 248, 220));
		composite_left.setBounds(0, 0, (int)(w/4), h);
		
		Button new_in = new Button(composite_left, SWT.NONE);
		new_in.setBounds((int)(w/4/10), (int)(w/4/20/2), (int)(w/5), (int)(3*w/4/20));
		new_in.setText("+ \u65B0\u589E\u8FDB\u8D27");
		
		TableViewer tableViewer_1 = new TableViewer(composite_left, SWT.FULL_SELECTION);
		table_1 = tableViewer_1.getTable();
		table_1.setBackground(SWTResourceManager.getColor(255, 248, 220));
		table_1.setBounds(0, (int)(w/20), (int)(w/4), (int)(h-w/20));
		
		Composite composite  = new Composite(base, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(255, 250, 250));
		composite.setBounds((int)(w/4), 0, (int)(3*w/4), h);
		DateTime dateTime = new DateTime(composite, SWT.BORDER);
		dateTime.setBounds((int)(2*w/4/30), (int)(h/10/10), (int)(5*3*w/4/30), (int)(8*h/10/10));
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setBounds((int)(29*w/60+5*3*w/4/30), (int)(h/10/10), (int)(4*3*w/4/30), (int)(8*h/10/10));
		btnNewButton.setText("\u5220\u9664");
		
		final TableViewer tableViewer = new TableViewer(composite, SWT.FULL_SELECTION |SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK
		
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);
		
		table.setBounds(0, (int)(h/10), (int)(3*w/4), (int)(7*h/10));
		int columnWidth = (int)((3*w/4)/7);
		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(columnWidth);
		newColumnTableColumn.setText("品牌 ");
//		newColumnTableColumn.setImage(new Image(display,"title.png"));
		//加入事件监听器
		newColumnTableColumn.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.BRAND_ASC:Sorter.BRAND_DESC);
				asc = !asc;				
				refreshTable();
			}
		});

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(columnWidth);
		newColumnTableColumn_1.setText("子品牌");
//		加入事件监听器
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.SUB_BRAND_ASC:Sorter.SUB_BRAND_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(columnWidth);
		newColumnTableColumn_2.setText("规格");
//		加入事件监听器
		newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.SIZE_ASC:Sorter.SIZE_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(columnWidth);
		newColumnTableColumn_3.setText("单位");
//		加入事件监听器
		newColumnTableColumn_3.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.UNIT_ASC:Sorter.UNIT_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setWidth(columnWidth);
		newColumnTableColumn_4.setText("平均进价");
//		加入事件监听器
		newColumnTableColumn_4.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.AVG_IN_ASC:Sorter.AVG_IN_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setWidth(columnWidth);
		newColumnTableColumn_5.setText("平均售价");
//		加入事件监听器
		newColumnTableColumn_5.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.AVG_OUT_ASC:Sorter.AVG_OUT_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setWidth(columnWidth);
		newColumnTableColumn_6.setText("库存");
//		加入事件监听器
		newColumnTableColumn_6.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?Sorter.REPOSITORY_ASC:Sorter.REPOSITORY_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setInput(Product.getProduct());
		
		tableViewer.setColumnProperties(new String[]{"brand","sub_brand","size","unit","avg_in","avg_out","repository"});
		CellEditor[] cellEditor = new CellEditor[7];
		cellEditor[0] = new TextCellEditor(tableViewer.getTable());
		cellEditor[1] = new TextCellEditor(tableViewer.getTable());
		cellEditor[2] = new TextCellEditor(tableViewer.getTable());
		cellEditor[3] = new TextCellEditor(tableViewer.getTable());
		cellEditor[4] = new TextCellEditor(tableViewer.getTable());
		cellEditor[5] = new TextCellEditor(tableViewer.getTable());
		cellEditor[6] = new TextCellEditor(tableViewer.getTable());
		tableViewer.setCellEditors(cellEditor);
		ICellModifier modifier = new MyCellModifier(tableViewer);
		tableViewer.setCellModifier(modifier);
		
		txtTotal = new Text(composite, SWT.BORDER);
		txtTotal.setText("total");
		txtTotal.setBounds(0, (int)(8*h/10), (int)(3*w/4), (int)(h/10));						

		Text text = (Text)cellEditor[3].getControl();
//		text.addMouseListener(null);//pop up the keyboard
//		cellEditor[0] = null;
//		cellEditor[1] = new ComboBoxCellEditor(tableViewer.getTable(),MyCellModifier.NAMES,SWT.READ_ONLY);
//		cellEditor[2] = new CheckboxCellEditor(tableViewer.getTable());
//		cellEditor[3] = new TextCellEditor(tableViewer.getTable());
//		cellEditor[4] = null;
//		tableViewer.setCellEditors(cellEditor);
//		ICellModifier modifier = new MyCellModifier(tableViewer);
//		tableViewer.setCellModifier(modifier);
//		Text text = (Text)cellEditor[3].getControl();
//		text.addVerifyListener(new VerifyListener(){
//			public void verifyText(VerifyEvent e){
//				String inStr = e.text;
//				if (inStr.length() > 0){
//					try{
//						Integer.parseInt(inStr);
//						e.doit = true;
//					}catch(Exception ep){
//						e.doit = false;
//					}
//				}
//			}
//		});
//		tableViewer.addFilter(new MyFilter());
		refreshTable();
//		composite.setLayout(new FillLayout());
//		composite_left.setLayout(new FillLayout());
//		composite.pack();
//		composite_left.pack();
//		base.setLayout(new FillLayout());
						
	}
	
	
}
