package com.storeworld.customer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class MyCustomerFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		return true;
	}
}