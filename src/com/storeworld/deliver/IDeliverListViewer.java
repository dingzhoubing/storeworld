package com.storeworld.deliver;


public interface IDeliverListViewer {
	
	/**
	 * Update the view to reflect the fact that a deliver was added 
	 * to the deliver list
	 * 
	 * @param deliver
	 */
	public void addStock(Deliver deliver);
	
	/**
	 * Update the view to reflect the fact that a deliver was removed 
	 * from the deliver list
	 * 
	 * @param deliver
	 */
	public void removeStock(Deliver deliver);
	
	/**
	 * Update the view to reflect the fact that one of the delivers
	 * was modified 
	 * 
	 * @param deliver
	 */
	public void updateStock(Deliver deliver);
}
