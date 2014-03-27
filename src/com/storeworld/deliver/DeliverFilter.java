package com.storeworld.deliver;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * filter the deliver by patterns, no use now
 * @author dingyuanxiong
 *
 */
public class DeliverFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		return true;
	}
}