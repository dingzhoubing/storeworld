package com.storeworld.analyze.trendutils;

import com.storeworld.analyze.AnalyzerUtils.KIND;

public class TrendDataSet {

	/**
	 * to determine the type to analyze is: shipment or profit
	 * true: shipment
	 * false: profit
	 */
	private KIND kind;
	
	public TrendDataSet(){
		
	}
	
	public void setKind(KIND kind){
		this.kind = kind;
	}
	public KIND getKind(){
		return this.kind;
	}
	
}
