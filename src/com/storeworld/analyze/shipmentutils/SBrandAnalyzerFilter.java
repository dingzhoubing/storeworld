package com.storeworld.analyze.shipmentutils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;


/**
 * SBrandAnalyzer filter	
 */
public class SBrandAnalyzerFilter extends ViewerFilter {
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return true;
		}
	}