package com.storeworld.printer;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.storeworld.common.DataInTable;
import com.storeworld.deliver.Deliver;

public class PrintHandler {
	
	private ArrayList<DataInTable> dataset = new ArrayList<DataInTable>();
	private boolean type = false;//false means deliver, true means return
//	private double total_val = 0.00;
	private String indeed_val = "";
	private static DecimalFormat df = new DecimalFormat("0.00");
	private String ordernum = "";
	
	public PrintHandler(ArrayList<DataInTable> ds, boolean type, String indeed, String ordernum){
		this.dataset.clear();
		this.dataset.addAll(ds);
		this.indeed_val = indeed;		
		this.type = type;		
		this.ordernum = ordernum;
	}
	
	public void doPrint(){
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
				Thread td = new Thread(new Print(temp, (int)(counter/7), part, total_str, indeed_str, type, ordernum));
				td.start();
				temp.clear();
			}
		}
		//still need one table
		if(!temp.isEmpty()){
			Thread td = new Thread(new Print(temp, part, part, total_str, indeed_str, type, ordernum));
			td.start();			
		}

	}

}
