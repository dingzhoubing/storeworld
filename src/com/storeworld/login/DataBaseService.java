package com.storeworld.login;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DataBaseService implements Runnable{
	
	public static Process proc = null;
	
	public static Process getProc(){
		return proc;
	}
	@Override
	public void run() {
		
		try {
			boolean foundProc = false;
			Process process = Runtime.getRuntime().exec("taskList");
	        Scanner in = new Scanner(process.getInputStream());
	        while (in.hasNextLine()) {
	            String temp = in.nextLine();
	
	            if (temp.contains("mysqld.exe")) {
	                foundProc = true;
	                break;
	            }
	        }
			//if the proc is there, do not create a new one
	        if (!foundProc) {
				File mysqld = new File("../");
				String path = mysqld.getCanonicalPath();
				String mysqlexe = path + "/mysql-5.5.37-winx64/bin/";

				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
						"cd " + mysqlexe + " && mysqld.exe");
				builder.redirectErrorStream(true);
//				if (proc) {
				proc = builder.start();
				proc.waitFor();
//				} else {
//					// show message?
//				}
			}
		} catch (IOException e) {
			//do something
		} catch (InterruptedException e) {
			//do something
		}
		
	}
	
	
	
	public static void main(String[] args) throws IOException{
		
//		Process process = Runtime.getRuntime().exec("taskList");
//        Scanner in = new Scanner(process.getInputStream());
//        while (in.hasNextLine()) {
//            String temp = in.nextLine();
//
//            if (temp.contains("mysqld.exe")) {
//                temp = temp.replaceAll(" ", "");
//                String pid = temp.substring(10, temp.indexOf("Console"));
//                Runtime.getRuntime().exec("tskill " + pid);
//            }
//        }
	}
	
}
