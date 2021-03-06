package com.storeworld.product;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;

/**
 * content provider & table operations on product page
 * @author dingyuanxiong
 *
 */
public class ProductContentProvider implements IStructuredContentProvider, IDataListViewer{
		
		private ProductList productlist;
		private TableViewer tableviewer;
		public ProductContentProvider(TableViewer tableviewer, ProductList productlist){
			this.tableviewer = tableviewer;
			this.productlist = productlist;
		}
		public Object[] getElements(Object inputElement) {
			return productlist.getProducts().toArray();
		}
		
		public void dispose() {
			productlist.removeChangeListener(this);
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			 if (newInput != null)
	                ((ProductList) newInput).addChangeListener(this);
	            if (oldInput != null)
	                ((ProductList) oldInput).removeChangeListener(this);
		}
		@Override
		public void add(DataInTable product) {
			tableviewer.add(product);
			
		}
		@Override
		public void remove(DataInTable product) {
			tableviewer.remove(product);
			
		}
		@Override
		public void update(DataInTable product) {
			tableviewer.update(product, null);			
			
		}
	}