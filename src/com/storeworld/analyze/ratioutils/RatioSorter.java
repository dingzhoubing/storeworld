package com.storeworld.analyze.ratioutils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
/**
 * we need to make the table sortable for users
 */
public class RatioSorter extends ViewerSorter {
	private static final int COL1 = 1;
	private static final int COL2 = 2;
	private static final int RATIO = 3;
	
	public static final RatioSorter COL1_ASC = new RatioSorter(COL1);
	public static final RatioSorter COL1_DESC = new RatioSorter(-COL1);
	public static final RatioSorter COL2_ASC = new RatioSorter(COL2);
	public static final RatioSorter COL2_DESC = new RatioSorter(-COL2);
	public static final RatioSorter RATIO_ASC = new RatioSorter(RATIO);
	public static final RatioSorter RATIO_DESC = new RatioSorter(-RATIO);
	
	
	private int sortType ;
	private RatioSorter(int sortType){
		this.sortType = sortType;
	}
	public int compare(Viewer viewer, Object e1, Object e2) {
		RatioAnalyzer p1 = (RatioAnalyzer)e1;
		RatioAnalyzer p2 = (RatioAnalyzer)e2;
		switch(sortType){				
			case COL1:{
				String s1 = p1.getCol1();
				String s2 = p2.getCol1();
				return s1.compareTo(s2);
			}
			case -COL1:{
				String s1 = p1.getCol1();
				String s2 = p2.getCol1();
				return s2.compareTo(s1);
			}
			case COL2:{
				String s1 = p1.getCol2();
				String s2 = p2.getCol2();
				return s1.compareTo(s2);
			}
			case -COL2:{
				String s1 = p1.getCol2();
				String s2 = p2.getCol2();
				return s2.compareTo(s1);
			}
			case RATIO:{
				String i1 = p1.getCol3();
				String i2 = p2.getCol3();
				return i1.compareTo(i2);
			}
			case -RATIO:{
				String i1 = p1.getCol3();
				String i2 = p2.getCol3();
				return i2.compareTo(i1);
			}			

		}
		return 0;
	}
}