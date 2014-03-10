package com.storeworld.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Product{
	private String brand;
	private String sub_brand;
	private String size;
	private String unit;
	private double avg_stock_price;
	private double avg_deliver_price;
	private int repository;
	public Product(String brand,String sub_brand,String size,String unit, double avg_stock,double avg_deliver, int repository){
		this.brand = brand;
		this.sub_brand = sub_brand;
		this.size = size;
		this.unit = unit;
		this.avg_stock_price = avg_stock;
		this.avg_deliver_price = avg_deliver;
		this.repository = repository;		
	}
	public String getBrand() {
		return this.brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSubBrand() {
		return this.sub_brand;
	}
	public void setSubBrand(String sub_brand) {
		this.sub_brand = sub_brand;
	}
	public String getSize() {
		return this.size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getUnit() {
		return this.unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getAvgStockPrice() {
		return this.avg_stock_price;
	}
	public void setAvgStockPrice(double price) {
		this.avg_stock_price = price;
	}
	public double getAvgDeliverPrice() {
		return this.avg_deliver_price;
	}
	public void setAvgDeliverPrice(double price) {
		this.avg_deliver_price = price;
	}
	public int getRepository() {
		return this.repository;
	}
	public void setRepository(int repository) {
		this.repository = repository;
	}
	public static List getProduct(){
		List list = new ArrayList();
		String brand = "五得利";
		String subbrand = "精一";
		String size = "50kg";
		String unit = "包";
		double avg_in = 68.0;
		double avg_out = 72.0;
		int repository = 3000;
		Product prod = new Product(brand, subbrand, size, unit, avg_in, avg_out, repository);
		list.add(prod);
		String brand2 = "五得利";
		String subbrand2 = "特精";
		String size2 = "50kg";
		String unit2 = "包";
		double avg_in2 = 69.0;
		double avg_out2 = 74.0;
		int repository2 = 2500;
		Product prod2 = new Product(brand2, subbrand2, size2, unit2, avg_in2, avg_out2, repository2);
		list.add(prod2);
		String brand3 = "五得利";
		String subbrand3 = "普粉";
		String size3 = "50kg";
		String unit3 = "包";
		double avg_in3 = 63.0;
		double avg_out3 = 67.0;
		int repository3 = 3000;
		Product prod3 = new Product(brand3, subbrand3, size3, unit3, avg_in3, avg_out3, repository3);
		list.add(prod3);
		String brand4 = "金龙";
		String subbrand4 = "精粉";
		String size4 = "50kg";
		String unit4 = "包";
		double avg_in4 = 66.0;
		double avg_out4 = 70.0;
		int repository4 = 4000;
		Product prod4 = new Product(brand4, subbrand4, size4, unit4, avg_in4, avg_out4, repository4);
		list.add(prod4);
		String brand5 = "五联";
		String subbrand5 = "包子粉";
		String size5 = "50kg";
		String unit5 = "包";
		double avg_in5 = 65.0;
		double avg_out5 = 69.0;
		int repository5 = 2800;
		Product prod5 = new Product(brand5, subbrand5, size5, unit5, avg_in5, avg_out5, repository5);
		list.add(prod5);
		return list;
	}
}