package com.storeworld.analyze.ratioutils;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * just a content provider, we cannot change the content
 * of the table, so there is no need to realize the operators
 * @author dingyuanxiong
 *
 */
public class RatioAnalyzerContentProvider implements IStructuredContentProvider{
	
	private RatioResultList resultlist;
	private TableViewer tableviewer;
	public RatioAnalyzerContentProvider(TableViewer tableviewer, RatioResultList resultlist){
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

