package com.storeworld.product;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;


public class TestTableViewer {
	private static Table table;
	/**
	 * Launch the application
	 * @param args
	 */
	
	public static void refreshTable(){
		Color color1 = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		for(int i=0;i<table.getItemCount();i++){
			if(i%2 == 0){
				table.getItems()[i].setBackground(color1);
			}
		}
		table.redraw();
	}
	public static void main(String[] args) {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.setSize(500, 375);
		shell.setText("SWT Application");

		int w = shell.getBounds().width;
		int h = shell.getBounds().height;
		Composite composite  = new Composite(shell, SWT.NONE);
//		composite.setBounds(0, 0, w, h);
		final TableViewer tableViewer = new TableViewer(composite, SWT.FULL_SELECTION | SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK
		
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);
		
		table.setBounds(0, 0, w, h);
		int columnWidth = (int)(w/7);
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
		composite.setLayout(new FillLayout());
		composite.pack();
		shell.setLayout(new FillLayout());
		shell.open();		
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}