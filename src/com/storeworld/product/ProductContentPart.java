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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.storeworld.mainui.ContentPart;
public class ProductContentPart extends ContentPart{
	private static Table table;
	
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
	private Composite composite = null;
	public ProductContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		
		composite = new Composite(this, SWT.NONE);
		
		current = parent;		
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;
//		Composite composite  = new Composite(current, SWT.NONE);
		composite.setBounds(0, 0, w, h);
		
		final TableViewer tableViewer = new TableViewer(composite, SWT.FULL_SELECTION | SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK
		
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);
		
		table.setBounds(0, 0, w, h);
		int columnWidth = (int)(w/7);
		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(columnWidth);
		newColumnTableColumn.setText("Ʒ�� ");
//		newColumnTableColumn.setImage(new Image(display,"title.png"));
		//�����¼�������
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
		newColumnTableColumn_1.setText("��Ʒ��");
//		�����¼�������
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
		newColumnTableColumn_2.setText("���");
//		�����¼�������
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
		newColumnTableColumn_3.setText("��λ");
//		�����¼�������
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
		newColumnTableColumn_4.setText("ƽ������");
//		�����¼�������
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
		newColumnTableColumn_5.setText("ƽ���ۼ�");
//		�����¼�������
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
		newColumnTableColumn_6.setText("���");
//		�����¼�������
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
		
//		composite.setBackground(new Color(getDisplay(), 63, 63, 125));
//		composite.pack();	
						
	}
}