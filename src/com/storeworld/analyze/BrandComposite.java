package com.storeworld.analyze;

import java.util.HashMap;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.storeworld.analyze.shipmentutils.SBrandAnalyzerCellModifier;
import com.storeworld.analyze.shipmentutils.SBrandAnalyzerContentProvider;
import com.storeworld.analyze.shipmentutils.SBrandAnalyzerLabelProvider;
import com.storeworld.analyze.shipmentutils.SBrandResultList;
import com.storeworld.analyze.shipmentutils.SBrandSorter;
import com.storeworld.analyze.shipmentutils.ShipmentBlock;
import com.storeworld.stock.MyStockFilter;

public class BrandComposite extends Composite {

	//inner class to define the table of the composite, to make the code flexible, we don't abstract 
	//these common parts with the area table
	
	private static Table table;	
	private static SBrandResultList resultlist = new SBrandResultList(); 
	private ShipmentBlock args;
	
	/**
	 * 
	 * @param parent
	 * @param style
	 * @param width  composite width
	 * @param height composite height
	 * 
	 */
	public BrandComposite(Composite parent, int style, ShipmentBlock args) {
		super(parent, style);
		this.args = args;

		showComposite();
	}
	
	public void refreshTable(){
		Color color1 = new Color(table.getDisplay(), 255, 245, 238);
		Color color2 = new Color(table.getDisplay(), 255, 250, 250);
		for(int i=0;i<table.getItemCount();i++){
			TableItem item = table.getItem(i);
			if(i%2 == 0){
				item.setBackground(color1);
			}else{
				item.setBackground(color2);
			}
		}
		table.redraw();
	}
	
	private void showComposite(){
//		int width= args.getWidth();
//		int height = args.getHeight();
		
		int width= 881;
		int height = 558;		
		this.setSize(width, height);
		
		
		Label lbl_title = new Label(this, SWT.BORDER);
		lbl_title.setBounds(0, 0, (int)(width/10), (int)(height/20));
		lbl_title.setText("各子品牌占比");
		
		final TableViewer tableViewer = new TableViewer(this, SWT.BORDER |SWT.FULL_SELECTION |SWT.V_SCROLL|SWT.H_SCROLL);//shell, SWT.CHECK		
		table = tableViewer.getTable();
		table.setLinesVisible(false);
		table.setHeaderVisible(true);		
		table.setBounds(0, (int)(height/20), (int)(3*width/5), (int)(5*height/10));
		table.setBackground(new Color(this.getDisplay(), 255, 250, 240));
		int columnWidth = (int)(3*width/5/4);
		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(columnWidth*2 -5);
		newColumnTableColumn_1.setMoveable(false);
		newColumnTableColumn_1.setResizable(false);
		newColumnTableColumn_1.setText("子品牌");
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?SBrandSorter.SUB_BRAND_ASC:SBrandSorter.SUB_BRAND_DESC);
				asc = !asc;				
				refreshTable();
			}
		});

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(columnWidth);
		newColumnTableColumn_2.setMoveable(false);
		newColumnTableColumn_2.setResizable(false);
		newColumnTableColumn_2.setText("出货量");
		newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?SBrandSorter.SHIPMENT_ASC:SBrandSorter.SHIPMENT_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(columnWidth);
		newColumnTableColumn_3.setMoveable(false);
		newColumnTableColumn_3.setResizable(false);
		newColumnTableColumn_3.setText("占比");
		newColumnTableColumn_3.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?SBrandSorter.RATIO_ASC:SBrandSorter.RATIO_DESC);
				asc = !asc;
				refreshTable();
			}
		});
		
		tableViewer.setContentProvider(new SBrandAnalyzerContentProvider(tableViewer, resultlist));
		tableViewer.setLabelProvider(new SBrandAnalyzerLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up
		tableViewer.setInput(resultlist);		
		//have no use for the table which cannot be modified, just in case
		tableViewer.setColumnProperties(new String[]{"sub_brand","shipment","ratio"});	
		
		ICellModifier modifier = new SBrandAnalyzerCellModifier(tableViewer, resultlist);
		tableViewer.setCellModifier(modifier);		
		//add Filter, no use now
		tableViewer.addFilter(new MyStockFilter());
		
		refreshTable();		
		Composite composite_image  = new Composite(this, SWT.BORDER);
		composite_image.setBackground(new Color(this.getDisplay(), 204, 250, 204));
		composite_image.setBounds((int)(3*width/5), (int)(height/20), (int)(2*width/5), (int)(5*height/10));		
		
		
		
		
	}	
	
}
