package com.storeworld.stock;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class MyFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Product p = (Product) element;
		return p.getBrand().startsWith("Îå");
	}
}