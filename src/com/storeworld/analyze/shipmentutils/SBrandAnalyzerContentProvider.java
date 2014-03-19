package com.storeworld.analyze.shipmentutils;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class SBrandAnalyzerContentProvider implements IStructuredContentProvider{
	
	private SBrandResultList resultlist;
	private TableViewer tableviewer;
	public SBrandAnalyzerContentProvider(TableViewer tableviewer, SBrandResultList resultlist){
		this.tableviewer = tableviewer;
		this.resultlist = resultlist;
	}
	public Object[] getElements(Object inputElement) {
		return resultlist.getResults().toArray();
	}
	@Override
	public void dispose() {
		
	}
	@Override
	public void inputChanged(Viewer viewer, Object oldInput,
			Object newInput) {			
		
	}
	
}

