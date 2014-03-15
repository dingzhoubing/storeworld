package com.storeworld.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ProductList {
	
	private ArrayList<Product> productList = new ArrayList<Product>();
	//hash set, so make it only has one of one kind
	private Set<IProductListViewer> changeListeners = new HashSet<IProductListViewer>();

	
	public ProductList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
		String brand = "五得利";
		String subbrand = "精一";
		String size = "50kg";
		String unit = "包";
//		double avg_in = 68.0;
//		double avg_out = 72.0;
		int repository = 3000;
		Product prod = new Product("1",brand, subbrand, size, unit, repository);
		productList.add(prod);
		String brand2 = "五得利";
		String subbrand2 = "特精";
		String size2 = "50kg";
		String unit2 = "包";
//		double avg_in2 = 69.0;
//		double avg_out2 = 74.0;
		int repository2 = 2500;
		Product prod2 = new Product("2",brand2, subbrand2, size2, unit2, repository2);
		productList.add(prod2);
		String brand3 = "五得利";
		String subbrand3 = "普粉";
		String size3 = "50kg";
		String unit3 = "包";
//		double avg_in3 = 63.0;
//		double avg_out3 = 67.0;
		int repository3 = 3000;
		Product prod3 = new Product("3",brand3, subbrand3, size3, unit3, repository3);
		productList.add(prod3);
		String brand4 = "金龙";
		String subbrand4 = "精粉";
		String size4 = "50kg";
		String unit4 = "包";
//		double avg_in4 = 66.0;
//		double avg_out4 = 70.0;
		int repository4 = 4000;
		Product prod4 = new Product("4", brand4, subbrand4, size4, unit4, repository4);
		productList.add(prod4);
		String brand5 = "五联";
		String subbrand5 = "包子粉";
		String size5 = "50kg";
		String unit5 = "包";
//		double avg_in5 = 65.0;
//		double avg_out5 = 69.0;
		int repository5 = 2800;
		Product prod5 = new Product("5", brand5, subbrand5, size5, unit5, repository5);
		productList.add(prod5);				
		
	}
	
	public ArrayList<Product> getProducts() {
		return this.productList;
	}
	
	/**
	 * add a product
	 */
	public void addProduct() {
		Product product = new Product();
		this.productList.add(product);
		Iterator<IProductListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).addProduct(product);
	}

	/**
	 * @param remove a product
	 */
	public void removeProduct(Product product) {
		this.productList.remove(product);
		Iterator<IProductListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).removeProduct(product);
	}

	/**
	 * @param update a product
	 */
	public void productChanged(Product product) {
		Iterator<IProductListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).updateProduct(product);
	}

	/**
	 * @param may multi contentprovider?， one remove
	 */
	public void removeChangeListener(IProductListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param may multi contentprovider? one add
	 * viewer is a content provider
	 */
	public void addChangeListener(IProductListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.productList.toString();
	}
	
}
