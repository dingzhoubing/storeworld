package com.storeworld.customer;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;

/**
 * content provider of the customer page
 * @author dingyuanxiong
 *
 */
public class CustomerContentProvider implements IStructuredContentProvider, IDataListViewer{
		
		private CustomerList customerlist;
		private TableViewer tableviewer;
		public CustomerContentProvider(TableViewer tableviewer, CustomerList customerlist){
			this.tableviewer = tableviewer;
			this.customerlist = customerlist;
		}
		public Object[] getElements(Object inputElement) {
//			System.out.println("call here");
			return CustomerList.getCustomers().toArray();
		}
		
		public void dispose() {
			customerlist.removeChangeListener(this);
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
			
		}
		@Override
		public void remove(DataInTable customer) {
			tableviewer.remove(customer);
	
		}
		@Override
		public void update(DataInTable customer) {
			tableviewer.update(customer, null);			
			
		}
	}