package com.storeworld.analyze.ratioutils;

import com.storeworld.common.DataInTable;

/**
 * the RatioAnalyzer
 * @author dingyuanxiong
 *
 */
public class RatioAnalyzer extends DataInTable{
	private String sub_brand;
	//it can be shipment or profit, as the type in the AnalyzerBase defined
	private String shipment_profit;
	private String ratio;
	
	public RatioAnalyzer() {
		
	}

	public RatioAnalyzer(String sub_brand, String shipment_profit,
			String ratio) {
		this.sub_brand = sub_brand;
		this.shipment_profit = shipment_profit;
		this.ratio = ratio;
	}

	
	public String getSubBrand() {
		return this.sub_brand;
	}

	public void setSubBrand(String sub_brand) {
		this.sub_brand = sub_brand;
	}

	public String getShipment_Profit() {
		return this.shipment_profit;
	}

	public void setShipment_Profit(String shipment_profit) {
		this.shipment_profit = shipment_profit;
	}

	public String getRatio() {
		return this.ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.sub_brand + " ");
		sb.append(this.shipment_profit + " ");
		sb.append(this.ratio);
		return sb.toString();
	}
}

