package com.storeworld.test;

import java.text.DecimalFormat;

public class TestPrecise {

//	public static String precise(double d){
//		d+=0.00;
//	}
	public static void main(String[] args) {
		double d1 = 123.4567;
		double d2 = 123;
		double d3 = 123.4;
		
		DecimalFormat df = new DecimalFormat("#.00");
		System.out.println(df.format(d1));
		System.out.println(df.format(d2));
		System.out.println(df.format(d3));

	}

}
