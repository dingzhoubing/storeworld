package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.HashMap;

public class StockMarker {
	
	class StockItem{
		
		private int number = 0;
		private double price = 0.0;

		StockItem(int number, double price) {
			this.number = number;
			this.price = price;
		}

		void setNumber(int num) {
			this.number = num;
		}

		void setPrice(double pri) {
			this.price = pri;
		}

		int getNumber() {
			return this.number;
		}

		double getPrice() {
			return this.price;
		}

		void minus(int num) {
			this.number = this.number - num;
		}
	}
	
	
	private HashMap<String, ArrayList<StockItem>> stocks = new HashMap<String, ArrayList<StockItem>>();
	
	public HashMap<String, ArrayList<StockItem>> getStocks(){
		return this.stocks;
	}
	
	public ArrayList<StockItem> getStockItemByKey(String key){
		if(stocks.containsKey(key)){
			return stocks.get(key);
		}else{
			return null;
		}			
	}
	
	public void addIntoStocks(String key, int number, double price){
		StockItem si = new StockItem(number, price);
		if(stocks.containsKey(key)){
			ArrayList<StockItem> items = stocks.get(key);
			items.add(si);			
		}else{
			ArrayList<StockItem> items = new ArrayList<StockItem>();
			items.add(si);
		}		
	}
	
	//number between multi keys!
	public double getProfitByKey(String key, int number, double price){
		
		//if there are no stocks in stock, we think there is no profit?
		double profit = 0.0;
		if(stocks.containsKey(key)){
			ArrayList<StockItem> items = stocks.get(key);
			for (int i = items.size() - 1; i >= 0; i--) {
				if (items.get(i).getNumber() > 0) {
					if (items.get(i).getNumber() > number) {
						double inPrice = items.get(i).getPrice();
						profit += (number * (price - inPrice));
						items.get(i).minus(number);
						break;
					} else {
						int partnum = items.get(i).getNumber();
						double partprice = items.get(i).getPrice();
						profit += (partnum * (price - partprice));
						number -= partnum;
					}
				}else{
					//should we remove this item?
				}
			}						
			
		}else{
			return 0.0;//if no such product, no such profit
		}		
		return profit;
	}
	
}
