package com.storeworld.printer;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.storeworld.common.DataInTable;
import com.storeworld.customer.Customer;
import com.storeworld.deliver.Deliver;
import com.storeworld.product.Product;
import com.storeworld.utils.Utils;

public class PrintHandler {
	
	private ArrayList<DataInTable> dataset = new ArrayList<DataInTable>();
	private boolean type = false;//false means deliver, true means return
//	private double total_val = 0.00;
	private String indeed_val = "";
	private static DecimalFormat df = new DecimalFormat("0.00");
	private String ordernum = "";
	private Connection conn;
	private String historyIndeed = "";
	private ArrayList<Product> products = new ArrayList<Product>();
	private ArrayList<Customer> customers = new ArrayList<Customer>();
	
	public PrintHandler(Connection conn, ArrayList<DataInTable> ds, boolean type, String indeed, String ordernum){
		this.dataset.clear();
		this.dataset.addAll(ds);
		this.indeed_val = indeed;		
		this.type = type;		
		this.ordernum = ordernum;
		this.conn = conn;
	}
	
	public void setHistoryIndeed(String indeed){
		this.historyIndeed = indeed;
	}
	
	public void setProductsChanged(ArrayList<Product> products){
		this.products = products;
	}
	
	public void setCustomersChanged(ArrayList<Customer> customers){
		this.customers = customers;
	}
	
	public void doPrint() throws Exception{
		Utils.setPrintSuccess(true);
		double t = 0.00;//total
		int size = dataset.size();
		int part = (int) Math.ceil(size/7);
		
		for(int i=0;i<dataset.size();i++){
			Deliver st = (Deliver)(dataset.get(i));
			String price = st.getPrice();
			String number = st.getNumber();
			double p = Double.valueOf(price);
			int n = Integer.valueOf(number);
			t+=(p * n);		
		}
		
		String total_str = df.format(t);
		String indeed_str= indeed_val;
		
		int counter = 0;
		ArrayList<DataInTable> temp = new ArrayList<DataInTable>();
		temp.clear();
		for(int i=0;i<dataset.size();i++){
			counter++;			
			temp.add(dataset.get(i));
			//a list contains 7 elements
			if((counter%7)==0){
				Print p = new Print(temp, (int)(counter/7), part, total_str, indeed_str, type, ordernum, conn);
				p.setHistoryIndeed(historyIndeed);//even if change multi-times, still right!
				p.setProductsChanged(products);
				p.setCustomersChanged(customers);
				Thread td = new Thread(p);
				td.start();
				td.join();
				temp.clear();
			}
		}
		//still need one table
		if(!temp.isEmpty()){
			Print p = new Print(temp, part, part, total_str, indeed_str, type, ordernum, conn);
			p.setHistoryIndeed(historyIndeed);
			p.setProductsChanged(products);
			p.setCustomersChanged(customers);
			Thread td = new Thread(p);
			td.start();	
			td.join();
		}

	}

}
