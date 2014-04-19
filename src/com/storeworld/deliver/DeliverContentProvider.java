package com.storeworld.deliver;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
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
		
		/**
		 * get the elements of the deliver table initial/refresh 
		 */
		public Object[] getElements(Object inputElement) {
			return DeliverList.getDelivers().toArray();
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
			
		}
		@Override
		public void remove(DataInTable deliver) {
			tableviewer.remove(deliver);
			
		}
		@Override
		public void update(DataInTable deliver) {
			tableviewer.update(deliver, null);			
			
		}
	}