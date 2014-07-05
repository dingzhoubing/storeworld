package com.storeworld.login;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.storeworld.mainui.MainUI;

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
//	                continue;
	            }
//	            if(temp.contains("storeworld.exe")){
//	            	throw new Exception("程序已经在运行");
//	            }
	        }
			//if the proc is there, do not create a new one
	        if (!foundProc) {
				File mysqld = new File("../");
				String path = mysqld.getCanonicalPath();
				String mysqlexe = path + "/mysql-5.1.73-win32/bin/";

				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
						"cd " + mysqlexe + " && mysqld.exe --skip-grant-tables");
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
		}catch (Exception e){
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
					MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK);						
			    	messageBox.setMessage("初始化软件失败，请重新启动"); 	
			    	if (messageBox.open() == SWT.OK){	    			    	
			    		MainUI.getMainUI_Instance(Display.getDefault()).dispose();
			    		System.exit(0);
			    		return;
			    	}
			    }
			    });
//			
//			MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK);						
//	    	messageBox.setMessage("初始化软件失败，请重新启动"); 	
//	    	if (messageBox.open() == SWT.OK){	    			    	
//	    		MainUI.getMainUI_Instance(Display.getDefault()).dispose();
//	    		System.exit(0);
//	    		return;
//	    	}
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
