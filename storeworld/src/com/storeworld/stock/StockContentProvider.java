package com.storeworld.stock;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;

/**
 * provide data interface & table operations
 * @author dingyuanxiong
 *
 */
public class StockContentProvider implements IStructuredContentProvider, IDataListViewer{
		
		private StockList stocklist;
		private TableViewer tableviewer;
		public StockContentProvider(TableViewer tableviewer, StockList stocklist){
			this.tableviewer = tableviewer;
			this.stocklist = stocklist;
		}
		public Object[] getElements(Object inputElement) {
			return stocklist.getStocks().toArray();
		}
		
		public void dispose() {
			stocklist.removeChangeListener(this);
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			 if (newInput != null)
	                ((StockList) newInput).addChangeListener(this);
	            if (oldInput != null)
	                ((StockList) oldInput).removeChangeListener(this);
		}
		@Override
		public void add(DataInTable stock) {
			tableviewer.add(stock);
//			MessageBox messageBox =   
//					   new MessageBox(new Shell(),   					     
//					    SWT.ICON_WARNING);   
//			messageBox.setMessage("add stock: "+stock);   
//			messageBox.open(); 
//			System.out.println(stock);
			
		}
		@Override
		public void remove(DataInTable stock) {
			tableviewer.remove(stock);
//			//no use later
//			MessageBox messageBox =   
//					   new MessageBox(new Shell(),   					     
//					    SWT.ICON_WARNING);   
//			messageBox.setMessage("remove stock: "+stock);   
//			messageBox.open(); 
			
		}
		@Override
		public void update(DataInTable stock) {
			tableviewer.update(stock, null);			
			
		}
	}