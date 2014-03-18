package com.storeworld.deliver;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class DeliverContentProvider implements IStructuredContentProvider, IDeliverListViewer{
		
		private DeliverList deliverlist;
		private TableViewer tableviewer;
		public DeliverContentProvider(TableViewer tableviewer, DeliverList deliverlist){
			this.tableviewer = tableviewer;
			this.deliverlist = deliverlist;
		}
		public Object[] getElements(Object inputElement) {
			return deliverlist.getStocks().toArray();
		}
		
		public void dispose() {
			deliverlist.removeChangeListener(this);
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			 if (newInput != null)
	                ((DeliverList) newInput).addChangeListener(this);
	            if (oldInput != null)
	                ((DeliverList) oldInput).removeChangeListener(this);
		}
		@Override
		public void addStock(Deliver deliver) {
			tableviewer.add(deliver);
			MessageBox messageBox =   
					   new MessageBox(new Shell(),   					     
					    SWT.ICON_WARNING);   
			messageBox.setMessage("add deliver: "+deliver);   
			messageBox.open(); 
//			System.out.println(deliver);
			
		}
		@Override
		public void removeStock(Deliver deliver) {
			tableviewer.remove(deliver);
			//no use later
			MessageBox messageBox =   
					   new MessageBox(new Shell(),   					     
					    SWT.ICON_WARNING);   
			messageBox.setMessage("remove deliver: "+deliver);   
			messageBox.open(); 
			
		}
		@Override
		public void updateStock(Deliver deliver) {
			tableviewer.update(deliver, null);			
			
		}
	}