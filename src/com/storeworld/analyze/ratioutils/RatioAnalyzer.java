package com.storeworld.analyze.ratioutils;

import com.storeworld.common.DataInTable;

/**
 * the RatioAnalyzer
 * @author dingyuanxiong
 *
 */
public class RatioAnalyzer extends DataInTable{
	private String col1; //brand, subrand, area, customer
	private String col2; //shipment, profit
	private String col3; //ratio
	
	public RatioAnalyzer() {
		
	}

	public RatioAnalyzer(String col1, String col2,
			String col3) {
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
	}

	
	public String getCol1() {
		return this.col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return this.col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	public String getCol3() {
		return this.col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.col1 + " ");
		sb.append(this.col2 + " ");
		sb.append(this.col3);
		return sb.toString();
	}
}

