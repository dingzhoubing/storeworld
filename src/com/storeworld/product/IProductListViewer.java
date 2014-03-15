package com.storeworld.product;


public interface IProductListViewer {
	
	/**
	 * Update the view to reflect the fact that a product was added 
	 * to the product list
	 * 
	 * @param product
	 */
	public void addProduct(Product product);
	
	/**
	 * Update the view to reflect the fact that a product was removed 
	 * from the product list
	 * 
	 * @param product
	 */
	public void removeProduct(Product product);
	
	/**
	 * Update the view to reflect the fact that one of the products
	 * was modified 
	 * 
	 * @param product
	 */
	public void updateProduct(Product product);
}
