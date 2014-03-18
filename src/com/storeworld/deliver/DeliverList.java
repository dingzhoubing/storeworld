package com.storeworld.deliver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class DeliverList {
	
	private ArrayList<Deliver> deliverList = new ArrayList<Deliver>();
	//hash set, so make it only has one of one kind
	private Set<IDeliverListViewer> changeListeners = new HashSet<IDeliverListViewer>();

	
	public DeliverList() {
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
		Deliver deliver = new Deliver("1",brand, subbrand, size, unit, price, number);
		deliverList.add(deliver);
		
		String brand2 = "五得利";
		String subbrand2 = "特精";
		String size2 = "50kg";
		String unit2 = "包";
		double price2 = 75.0;
		int number2 = 40;
		Deliver deliver2 = new Deliver("2",brand2, subbrand2, size2, unit2, price2, number2);
		deliverList.add(deliver2);
		
		String brand3 = "五联";
		String subbrand3 = "包子粉";
		String size3 = "50kg";
		String unit3= "包";
		double price3 = 72.0;
		int number3 = 20;
		Deliver deliver3 = new Deliver("3", brand3, subbrand3, size3, unit3, price3, number3);
		deliverList.add(deliver3);	
		
	}
	
	public ArrayList<Deliver> getStocks() {
		return this.deliverList;
	}
	
	/**
	 * add a deliver
	 */
	public void addStock() {
		Deliver deliver = new Deliver();
		this.deliverList.add(deliver);
		Iterator<IDeliverListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).addStock(deliver);
	}

	/**
	 * @param remove a deliver
	 */
	public void removeStock(Deliver deliver) {
		this.deliverList.remove(deliver);
		Iterator<IDeliverListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).removeStock(deliver);
	}

	/**
	 * @param update a deliver
	 */
	public void deliverChanged(Deliver deliver) {
		Iterator<IDeliverListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).updateStock(deliver);
	}

	/**
	 * @param may multi contentprovider?， one remove
	 */
	public void removeChangeListener(IDeliverListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param may multi contentprovider? one add
	 * viewer is a content provider
	 */
	public void addChangeListener(IDeliverListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.deliverList.toString();
	}
	
}
