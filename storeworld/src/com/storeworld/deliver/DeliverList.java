package com.storeworld.deliver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.stock.StockUtils;

/**
 * produce the data for deliver table
 * @author dingyuanxiong
 *
 */
public class DeliverList {
	
	private ArrayList<DataInTable> deliverList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();

	
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
		String price = "50.0";
		String number = "30";
		Deliver deliver = new Deliver("1",brand, subbrand, size, unit, price, number);
		deliverList.add(deliver);
		
		String brand2 = "五得利";
		String subbrand2 = "特精";
		String size2 = "50kg";
		String unit2 = "包";
		String price2 = "75.0";
		String number2 = "40";
		Deliver deliver2 = new Deliver("2",brand2, subbrand2, size2, unit2, price2, number2);
		deliverList.add(deliver2);
		
		String brand3 = "五联";
		String subbrand3 = "包子粉";
		String size3 = "50kg";
		String unit3= "包";
		String price3 = "72.0";
		String number3 = "20";
		Deliver deliver3 = new Deliver("3", brand3, subbrand3, size3, unit3, price3, number3);
		deliverList.add(deliver3);	
		
		DeliverUtils.setNewLineID("4");
		
	}
	
	public ArrayList<DataInTable> getDelivers() {
		return this.deliverList;
	}
	
	/**
	 * add a deliver
	 */
	public void addDeliver(Deliver deliver) {
		this.deliverList.add(deliver);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).add(deliver);
	}

	/**
	 * @param remove a deliver
	 */
	public void removeDeliver(Deliver deliver) {
		this.deliverList.remove(deliver);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).remove(deliver);
	}

	/**
	 * @param update a deliver
	 */
	public void deliverChanged(Deliver deliver) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).update(deliver);
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
		return this.deliverList.toString();
	}
	
}
