package com.storeworld.analyze.trendutils;

import com.storeworld.analyze.AnalyzerUtils.KIND;
import com.storeworld.analyze.AnalyzerUtils.TYPE;

public class TrendDataSet {

	/**
	 * to determine the type to analyze is: shipment or profit
	 * true: shipment
	 * false: profit
	 */
	private KIND kind;
	private TYPE type;
	
	public TrendDataSet(){
		
	}
	
	public void setKind(KIND kind){
		this.kind = kind;
	}
	public KIND getKind(){
		return this.kind;
	}
	
	public void setType(TYPE type){
		this.type = type;
	}
	public TYPE getType(){
		return this.type;
	}
}
