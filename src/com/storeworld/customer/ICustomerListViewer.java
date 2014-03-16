package com.storeworld.customer;


public interface ICustomerListViewer {
	
	/**
	 * Update the view to reflect the fact that a customer was added 
	 * to the customer list
	 * 
	 * @param customer
	 */
	public void addCustomer(Customer customer);
	
	/**
	 * Update the view to reflect the fact that a customer was removed 
	 * from the customer list
	 * 
	 * @param customer
	 */
	public void removeCustomer(Customer customer);
	
	/**
	 * Update the view to reflect the fact that one of the customers
	 * was modified 
	 * 
	 * @param customer
	 */
	public void updateCustomer(Customer customer);
}
