package com.storeworld.stock;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
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
			return StockList.getStocks().toArray();
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
			
		}
		@Override
		public void remove(DataInTable stock) {
			tableviewer.remove(stock);
			
		}
		@Override
		public void update(DataInTable stock) {
			tableviewer.update(stock, null);			
			
		}
	}