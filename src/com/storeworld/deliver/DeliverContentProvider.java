package com.storeworld.deliver;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;

/**
 * content & operations on deliver table
 * @author dingyuanxiong
 *
 */
public class DeliverContentProvider implements IStructuredContentProvider, IDataListViewer{
		
		private DeliverList deliverlist;
		private TableViewer tableviewer;
		public DeliverContentProvider(TableViewer tableviewer, DeliverList deliverlist){
			this.tableviewer = tableviewer;
			this.deliverlist = deliverlist;
		}
		public Object[] getElements(Object inputElement) {
			return deliverlist.getDelivers().toArray();
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
		public void add(DataInTable deliver) {
			tableviewer.add(deliver);
			MessageBox messageBox =   
					   new MessageBox(new Shell(),   					     
					    SWT.ICON_WARNING);   
			messageBox.setMessage("add deliver: "+deliver);   
			messageBox.open(); 
//			System.out.println(deliver);
			
		}
		@Override
		public void remove(DataInTable deliver) {
			tableviewer.remove(deliver);
			//no use later
			MessageBox messageBox =   
					   new MessageBox(new Shell(),   					     
					    SWT.ICON_WARNING);   
			messageBox.setMessage("remove deliver: "+deliver);   
			messageBox.open(); 
			
		}
		@Override
		public void update(DataInTable deliver) {
			tableviewer.update(deliver, null);			
			
		}
	}