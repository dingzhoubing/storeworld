package com.storeworld.analyze.ratioutils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;


/**
 * RatioAnalyzer filter	
 */
public class RatioAnalyzerFilter extends ViewerFilter {
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return true;
		}
	}