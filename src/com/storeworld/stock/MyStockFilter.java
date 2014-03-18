package com.storeworld.stock;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class MyStockFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		return true;
	}
}