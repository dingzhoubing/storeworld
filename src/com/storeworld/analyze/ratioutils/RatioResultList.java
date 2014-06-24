package com.storeworld.analyze.ratioutils;

import java.util.ArrayList;
import java.util.List;

import com.storeworld.pojo.dto.AnalysticDTO;

/**
 * provide the data, connect the database
 * @author dingyuanxiong
 *
 */
public class RatioResultList {
	
	private ArrayList<RatioAnalyzer> resultList = new ArrayList<RatioAnalyzer>();
	
	//to get the data of which brand
	private String brand;
	//to get the shipment or get the profit
	private boolean shipment_profit;
	
	
	public RatioResultList() {
////		super();		
//		this.initial();
	}
	
	public void setSearchFactor(String brand, boolean type){
		this.brand = brand;
		this.shipment_profit = type;
	}
	
	//initial data, later, in database
	public void initial(){		
		
//		String subbrand = "精一";
//		String shipment = "3000";
//		String ratio = "0.2";			
//		RatioAnalyzer sba = new RatioAnalyzer(subbrand, shipment, ratio);
//		resultList.add(sba);
//		
//		String subbrand2 = "特精";
//		String shipment2 = "9000";
//		String ratio2 = "0.6";			
//		RatioAnalyzer sba2 = new RatioAnalyzer(subbrand2, shipment2, ratio2);
//		resultList.add(sba2);
//		
//		String subbrand3 = "包子粉";
//		String shipment3 = "3000";
//		String ratio3 = "0.2";			
//		RatioAnalyzer sba3 = new RatioAnalyzer(subbrand3, shipment3, ratio3);
//		resultList.add(sba3);
		
	}		
	
	//args
	public void initilByQuery(List<Object> ds, int type){
		
		
		for(int i=0;i<ds.size();i++){
			AnalysticDTO item = (AnalysticDTO)ds.get(i);
			String f1 = item.getField1();
			String f2 = item.getField2();
			double f3 = Double.valueOf(item.getField3());
			String ratio = (f3*100)+"%";
			
			RatioAnalyzer sba = new RatioAnalyzer(f1, f2, ratio);
			resultList.add(sba);
		}
				
	}
	/**
	 * where we get the data in the table
	 * currently, we get it from initial, later from db here
	 * all logics are here
	 * @return
	 */
	public ArrayList<RatioAnalyzer> getResults() {
		return this.resultList;
	}
	public String toString(){
		return this.resultList.toString();
	}		
}