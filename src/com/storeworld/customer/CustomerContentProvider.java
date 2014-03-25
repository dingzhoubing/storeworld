package com.storeworld.customer;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;

/**
 * content provider of the customer page
 * @author dingyuanxiong
 *
 */
public class CustomerContentProvider implements IStructuredContentProvider, IDataListViewer{
		
		private CustomerList productlist;
		private TableViewer tableviewer;
		public CustomerContentProvider(TableViewer tableviewer, CustomerList productlist){
			this.tableviewer = tableviewer;
			this.productlist = productlist;
		}
		public Object[] getElements(Object inputElement) {
//			System.out.println("call here");
			return productlist.getCustomers().toArray();
		}
		
		public void dispose() {
			productlist.removeChangeListener(this);
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			 if (newInput != null)
	                ((CustomerList) newInput).addChangeListener(this);
	            if (oldInput != null)
	                ((CustomerList) oldInput).removeChangeListener(this);
		}
		@Override
		public void add(DataInTable customer) {
			tableviewer.add(customer);
//			MessageBox messageBox =   
//					   new MessageBox(new Shell(),   					     
//					    SWT.ICON_WARNING);   
//			messageBox.setMessage("add customer: "+customer);   
//			messageBox.open(); 
//			System.out.println(product);
			
		}
		@Override
		public void remove(DataInTable customer) {
			tableviewer.remove(customer);
			//no use later
//			MessageBox messageBox =   
//					   new MessageBox(new Shell(),   					     
//					    SWT.ICON_WARNING);   
//			messageBox.setMessage("remove customer: "+customer);   
//			messageBox.open(); 
			
		}
		@Override
		public void update(DataInTable customer) {
			tableviewer.update(customer, null);			
			
		}
	}