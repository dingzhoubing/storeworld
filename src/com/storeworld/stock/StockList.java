package com.storeworld.stock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class StockList {
	
	private ArrayList<Stock> stockList = new ArrayList<Stock>();
	//hash set, so make it only has one of one kind
	private Set<IStockListViewer> changeListeners = new HashSet<IStockListViewer>();

	
	public StockList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
		String brand = "五得利";
		String subbrand = "精一";
		String size = "50kg";
		String unit = "包";
		double price = 50.0;
		int number = 30;
		Stock stock = new Stock("1",brand, subbrand, size, unit, price, number);
		stockList.add(stock);
		
		String brand2 = "五得利";
		String subbrand2 = "特精";
		String size2 = "50kg";
		String unit2 = "包";
		double price2 = 75.0;
		int number2 = 40;
		Stock stock2 = new Stock("2",brand2, subbrand2, size2, unit2, price2, number2);
		stockList.add(stock2);
		
		String brand3 = "五联";
		String subbrand3 = "包子粉";
		String size3 = "50kg";
		String unit3= "包";
		double price3 = 72.0;
		int number3 = 20;
		Stock stock3 = new Stock("3", brand3, subbrand3, size3, unit3, price3, number3);
		stockList.add(stock3);	
		
	}
	
	public ArrayList<Stock> getStocks() {
		return this.stockList;
	}
	
	/**
	 * add a stock
	 */
	public void addStock() {
		Stock stock = new Stock();
		this.stockList.add(stock);
		Iterator<IStockListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).addStock(stock);
	}

	/**
	 * @param remove a stock
	 */
	public void removeStock(Stock stock) {
		this.stockList.remove(stock);
		Iterator<IStockListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).removeStock(stock);
	}

	/**
	 * @param update a stock
	 */
	public void stockChanged(Stock stock) {
		Iterator<IStockListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).updateStock(stock);
	}

	/**
	 * @param may multi contentprovider?， one remove
	 */
	public void removeChangeListener(IStockListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param may multi contentprovider? one add
	 * viewer is a content provider
	 */
	public void addChangeListener(IStockListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.stockList.toString();
	}
	
}
