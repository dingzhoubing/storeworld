package com.storeworld.stock;


public interface IStockListViewer {
	
	/**
	 * Update the view to reflect the fact that a stock was added 
	 * to the stock list
	 * 
	 * @param stock
	 */
	public void addStock(Stock stock);
	
	/**
	 * Update the view to reflect the fact that a stock was removed 
	 * from the stock list
	 * 
	 * @param stock
	 */
	public void removeStock(Stock stock);
	
	/**
	 * Update the view to reflect the fact that one of the stocks
	 * was modified 
	 * 
	 * @param stock
	 */
	public void updateStock(Stock stock);
}
