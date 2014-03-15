package com.storeworld.product;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ContentProvider implements IStructuredContentProvider, IProductListViewer{
		
		private ProductList productlist;
		private TableViewer tableviewer;
		public ContentProvider(TableViewer tableviewer, ProductList productlist){
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
		public void addProduct(Product product) {
			tableviewer.add(product);
			MessageBox messageBox =   
					   new MessageBox(new Shell(),   					     
					    SWT.ICON_WARNING);   
			messageBox.setMessage("add product: "+product);   
			messageBox.open(); 
//			System.out.println(product);
			
		}
		@Override
		public void removeProduct(Product product) {
			tableviewer.remove(product);
			//no use later
			MessageBox messageBox =   
					   new MessageBox(new Shell(),   					     
					    SWT.ICON_WARNING);   
			messageBox.setMessage("remove product: "+product);   
			messageBox.open(); 
			
		}
		@Override
		public void updateProduct(Product product) {
			tableviewer.update(product, null);			
			
		}
	}