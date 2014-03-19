package com.storeworld.analyze.shipmentutils;

import java.util.ArrayList;

/**
 * provide the data, connect the database
 * @author dingyuanxiong
 *
 */
public class SBrandResultList {
	
	private ArrayList<SBrandAnalyzer> resultList = new ArrayList<SBrandAnalyzer>();
	
	//to get the data of which brand
	private String brand;
	//to get the shipment or get the profit
	private boolean shipment_profit;
	
	public SBrandResultList() {
		super();
		this.initial();
	}
	
	public void setSearchFactor(String brand, boolean type){
		this.brand = brand;
		this.shipment_profit = type;
	}
	
	//initial data, later, in database
	public void initial(){		
		
		String subbrand = "精一";
		String shipment = "3000";
		String ratio = "0.2";			
		SBrandAnalyzer sba = new SBrandAnalyzer(subbrand, shipment, ratio);
		resultList.add(sba);
		
		String subbrand2 = "特精";
		String shipment2 = "9000";
		String ratio2 = "0.6";			
		SBrandAnalyzer sba2 = new SBrandAnalyzer(subbrand2, shipment2, ratio2);
		resultList.add(sba2);
		
		String subbrand3 = "包子粉";
		String shipment3 = "3000";
		String ratio3 = "0.2";			
		SBrandAnalyzer sba3 = new SBrandAnalyzer(subbrand3, shipment3, ratio3);
		resultList.add(sba3);
		
	}	
	
	public ArrayList<SBrandAnalyzer> getResults() {
		return this.resultList;
	}
	public String toString(){
		return this.resultList.toString();
	}		
}