package com.storeworld.product;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ProductFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return true;
	}
}