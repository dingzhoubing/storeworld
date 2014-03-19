package com.storeworld.analyze.shipmentutils;

/**
 * the Sub_Brand_Anayzer
 * @author dingyuanxiong
 *
 */
public class SBrandAnalyzer {
	private String sub_brand;
	private String shipment;
	private String ratio;

	public SBrandAnalyzer() {

	}

	public SBrandAnalyzer(String sub_brand, String shipment,
			String ratio) {
		this.sub_brand = sub_brand;
		this.shipment = shipment;
		this.ratio = ratio;
	}

	public String getSubBrand() {
		return this.sub_brand;
	}

	public void setSubBrand(String sub_brand) {
		this.sub_brand = sub_brand;
	}

	public String getShipment() {
		return this.shipment;
	}

	public void setShipmen(String shipment) {
		this.shipment = shipment;
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
		sb.append(this.shipment + " ");
		sb.append(this.ratio);
		return sb.toString();
	}
}

