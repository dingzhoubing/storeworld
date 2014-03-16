package com.storeworld.customer;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class CustomerContentProvider implements IStructuredContentProvider, ICustomerListViewer{
		
		private CustomerList productlist;
		private TableViewer tableviewer;
		public CustomerContentProvider(TableViewer tableviewer, CustomerList productlist){
			this.tableviewer = tableviewer;
			this.productlist = productlist;
		}
		public Object[] getElements(Object inputElement) {
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
		public void addCustomer(Customer customer) {
			tableviewer.add(customer);
			MessageBox messageBox =   
					   new MessageBox(new Shell(),   					     
					    SWT.ICON_WARNING);   
			messageBox.setMessage("add customer: "+customer);   
			messageBox.open(); 
//			System.out.println(product);
			
		}
		@Override
		public void removeCustomer(Customer customer) {
			tableviewer.remove(customer);
			//no use later
			MessageBox messageBox =   
					   new MessageBox(new Shell(),   					     
					    SWT.ICON_WARNING);   
			messageBox.setMessage("remove customer: "+customer);   
			messageBox.open(); 
			
		}
		@Override
		public void updateCustomer(Customer customer) {
			tableviewer.update(customer, null);			
			
		}
	}