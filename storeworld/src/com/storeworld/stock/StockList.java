package com.storeworld.stock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.customer.CustomerUtils;

/**
 * get the data of the stock table
 * connect database
 * @author dingyuanxiong
 *
 */
public class StockList {
	
	private ArrayList<DataInTable> stockList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();

	
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
		String price = "50.0";
		String number = "30";
		Stock stock = new Stock("1",brand, subbrand, size, unit, price, number);
		stockList.add(stock);
		
		String brand2 = "五得利";
		String subbrand2 = "特精";
		String size2 = "50kg";
		String unit2 = "包";
		String price2 = "75.0";
		String number2 = "40";
		Stock stock2 = new Stock("2",brand2, subbrand2, size2, unit2, price2, number2);
		stockList.add(stock2);
		
		String brand3 = "五联";
		String subbrand3 = "包子粉";
		String size3 = "50kg";
		String unit3= "包";
		String price3 = "72.0";
		String number3 = "20";
		Stock stock3 = new Stock("3", brand3, subbrand3, size3, unit3, price3, number3);
		stockList.add(stock3);	
	
		StockUtils.setNewLineID("4");
	}
	
	public ArrayList<DataInTable> getStocks() {
		return this.stockList;
	}
	
	/**
	 * add a stock
	 */
	public void addStock(Stock stock) {
		this.stockList.add(stock);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).add(stock);
	}

	/**
	 * @param remove a stock
	 */
	public void removeStock(Stock stock) {
		this.stockList.remove(stock);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).remove(stock);
	}

	/**
	 * @param update a stock
	 */
	public void stockChanged(Stock stock) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).update(stock);
	}

	/**
	 * @param may multi contentprovider?， one remove
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param may multi contentprovider? one add
	 * viewer is a content provider
	 */
	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.stockList.toString();
	}
	
}
