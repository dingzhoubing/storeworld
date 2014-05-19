package com.storeworld.analyze;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.storeworld.analyze.AnalyzerUtils.KIND;
import com.storeworld.analyze.ratioutils.RatioAnalyzerCellModifier;
import com.storeworld.analyze.ratioutils.RatioAnalyzerContentProvider;
import com.storeworld.analyze.ratioutils.RatioAnalyzerLabelProvider;
import com.storeworld.analyze.ratioutils.RatioResultList;
import com.storeworld.analyze.ratioutils.RatioSorter;
import com.storeworld.analyze.ratioutils.RatioBlock;
import com.storeworld.stock.StockFilter;
import com.storeworld.utils.Utils;

/**
 * class of shipment ratio and profit ratio table & graph
 * we have two steps to make a needed ratio composite
 * 1. make an RatioBlock, to specify the type, shipment or profit
 * 2. make a RatioResultList to specify the data type, shipment or profit, or what you need
 * @author dingyuanxiong
 *
 */
public class RatioComposite extends Composite implements AnalyzerBase{

	//the resutl table
	private static Table table;	
	//the content of the table
	private RatioResultList resultlist = null;
	//used to pass the arguments between caller and the composite
	private RatioBlock args= null;
	
	private String str_col1 = "";
	private String str_col2 = "";
	private String str_col3 = "";
	
	
	/**
	 * 
	 * @param parent
	 * @param style
	 * @param width  composite width
	 * @param height composite height
	 * 
	 */
	public RatioComposite(Composite parent, int style, RatioBlock args, RatioResultList resultlist) {
		super(parent, style);
		this.args = args;
		this.resultlist = resultlist;
		
//		resultlist = args.getRatioResultList();
		showComposite();
//		this.layout();
	}	
	
	public void showComposite(){
//		int width= args.getWidth();
//		int height = args.getHeight();
		
		int width= 881;
		int height = 558;		
		this.setSize(width, height);
		
		//table title
		Label lbl_title = new Label(this, SWT.BORDER);
		lbl_title.setBounds(0, 0, (int)(width/10), (int)(height/20));
		if(args.getBrand_area()){
			if(args.getBrand_sub()){
				lbl_title.setText("各品牌占比");
				str_col1 = "品牌";
			}
			else{
				lbl_title.setText("各子品牌占比");
				str_col1 = "子品牌";
			}
		}else{
			if(args.getArea_customer()){
				lbl_title.setText("各片区占比");
				str_col1 = "片区";
			}
			else{
				lbl_title.setText("客户占比");
				str_col1 = "客户";
			}
		}
		
		
		//table to show the analyzed result
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
		newColumnTableColumn_1.setText(str_col1);
		newColumnTableColumn_1.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?RatioSorter.COL1_ASC:RatioSorter.COL1_DESC);
				asc = !asc;				
				Utils.refreshTable(table);
			}
		});

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(columnWidth);
		newColumnTableColumn_2.setMoveable(false);
		newColumnTableColumn_2.setResizable(false);
		if(args.getKind().equals(KIND.SHIPMENT))
			newColumnTableColumn_2.setText("出货量");
		else
			newColumnTableColumn_2.setText("利润");
		newColumnTableColumn_2.addSelectionListener(new SelectionAdapter(){
			boolean asc = true;
			public void widgetSelected(SelectionEvent e){
				tableViewer.setSorter(asc?RatioSorter.COL2_ASC:RatioSorter.COL2_DESC);
				asc = !asc;
				Utils.refreshTable(table);
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
				tableViewer.setSorter(asc?RatioSorter.RATIO_ASC:RatioSorter.RATIO_DESC);
				asc = !asc;
				Utils.refreshTable(table);
			}
		});
		//set the table property
		tableViewer.setContentProvider(new RatioAnalyzerContentProvider(tableViewer, resultlist));
		tableViewer.setLabelProvider(new RatioAnalyzerLabelProvider());
		tableViewer.setUseHashlookup(true);//spead up
		tableViewer.setInput(resultlist);		
		//have no use for the table which cannot be modified, just in case
//		if(args.getKind().equals(KIND.SHIPMENT))
//			tableViewer.setColumnProperties(new String[]{"col1","shipment","ratio"});
//		else
		tableViewer.setColumnProperties(new String[]{"col2","col2","ratio"});
		ICellModifier modifier = new RatioAnalyzerCellModifier(tableViewer, resultlist);
		tableViewer.setCellModifier(modifier);		
		//add Filter, no use now
//		tableViewer.addFilter(new StockFilter());
		
		Utils.refreshTable(table);	
		Composite composite_image  = new Composite(this, SWT.BORDER);
		composite_image.setBackground(new Color(this.getDisplay(), 204, 250, 204));
		composite_image.setBounds((int)(3*width/5), (int)(height/20), (int)(2*width/5), (int)(5*height/10));				
		
	}	
	/**
	 * release the source
	 */
	public void remove(){
		this.dispose();
	}
	
}
