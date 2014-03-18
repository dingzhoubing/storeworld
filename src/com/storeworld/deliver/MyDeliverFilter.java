package com.storeworld.deliver;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class MyDeliverFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		return true;
	}
}