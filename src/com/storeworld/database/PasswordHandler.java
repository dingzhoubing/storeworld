package com.storeworld.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.storeworld.login.DesUtils;
import com.storeworld.mainui.MainUI;

public class PasswordHandler extends BaseAction {

	private static String password="";
	private static final String MARK = "login:"; 
	private static boolean pass = false;
	
	public static boolean getPassed(){
		return pass;
	}
	public static void setPassed(boolean p){
		pass = p;
	}
	public static String getPassword(){
		return password;
	}
	public static void setPassword(String pw){
		password = pw;
	}
	
	/**
	 * get the password stored in configuration.ini
	 * if not set it's -1
	 * if set we decrypt it
	 * if the configuration file is not exist or changed, we prevent the operation
	 * @return
	 */
	public static String getPasswordStored(){
		String ret_pw = "-2";
		File root = new File("../storeworld/configuration.ini");
//		String path = root.getCanonicalPath();
		try {
			DesUtils des = new DesUtils();
			if(root.exists()){
				BufferedReader bfr = new BufferedReader(new FileReader(root));
				String line="";
//				boolean needReset = false;
				boolean hasMark = false;				
				while((line=bfr.readLine()) != null){
					line = des.decrypt(line);
					if(line.startsWith(MARK)){
						String pw = line.substring((MARK.length())).trim();
						hasMark = true;
						if(!pw.equals("-1")){																					
							ret_pw = pw;							
						}else{
							ret_pw = "-1";
						}
					}
				}				
				bfr.close();
				
				if(!hasMark){
//					ret_pw = "-2";
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
					    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
							mbox.setMessage("配置文件不正确，请联系客服");
							mbox.open();	
					    }
					    }); 
				}
				
				//no such mark in file, over write the file
//				if(!hasMark){
//					//should we going on
//					BufferedWriter bfw = new BufferedWriter(new FileWriter(root));
//					bfw.write(des.encrypt(MARK+"-1"));
//					bfw.flush();
//					bfw.close();
//					ret_pw = "-1";
//				}
//				if(needReset){
//					//should we going on
//					BufferedWriter bfw = new BufferedWriter(new FileWriter(root));
//					bfw.write(des.encrypt(MARK+"-1"));
//					bfw.flush();
//					bfw.close();
//					ret_pw = "-1";
//				}
				
			}else{
				
//				BufferedWriter bfw = new BufferedWriter(new FileWriter(root));
//				bfw.write(des.encrypt(MARK+"-1"));
//				bfw.flush();
//				bfw.close();
//				ret_pw = "-1";
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
						mbox.setMessage("软件配置文件不存在，请联系客服");
						mbox.open();	
				    }
				    }); 
//				ret_pw = "-2";
			}
		} catch (Exception e) {//exception of desutil
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
			    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
					mbox.setMessage("配置文件不正确，请联系客服重置密码");
					mbox.open();	
			    }
			    }); 
//			ret_pw = "-2";
		}
		return ret_pw;
	}
	
	/**
	 * set the password stored in configuration.ini
	 * if set we encrypt it
	 * if the configuration file is not exist or changed, we prevent the operation
	 * @return
	 */
	public static boolean setPasswordStored(String pw){
		boolean ret = false;
		File root = new File("../storeworld/configuration.ini");
		try {
			boolean need_over_write = true;
			DesUtils des = new DesUtils();
			if(root.exists()){
				ArrayList<String> context = new ArrayList<String>();
				BufferedReader bfr = new BufferedReader(new FileReader(root));				
				String line="";
				while((line=bfr.readLine()) != null){
					line = des.decrypt(line);
					if(line.startsWith(MARK)){
						String sub = line.substring((MARK.length())).trim();			
						sub = pw;						
						line = MARK+sub;
						need_over_write = false;
					}					
					context.add(line);
				}
				bfr.close();
				BufferedWriter re_bfw = new BufferedWriter(new FileWriter(root));
				for(int i=0;i<context.size()-1;i++){
					re_bfw.write(des.encrypt(context.get(i)));
					re_bfw.write("\n");
				}
				re_bfw.write(des.encrypt(context.get(context.size()-1)));
				re_bfw.flush();
				re_bfw.close();	
				ret = true;
				
			}else{//file do not exist, produce one, need to change
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
						mbox.setMessage("软件配置文件不存在，请联系客服");
						mbox.open();	
				    }
				    }); 
			}
			
			if(need_over_write){//file has been changed?
//				BufferedWriter bfw = new BufferedWriter(new FileWriter(root));
//				String sub = "";
//				try {
//					sub = des.encrypt(pw);
//				} catch (Exception e) {
//					System.out.println("encrypt error");
//				}
//				bfw.write(MARK+sub);
//				bfw.flush();
//				bfw.close();
//				ret = false;//?
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
						mbox.setMessage("软件配置文件不正确，请联系客服");
						mbox.open();	
				    }
				    }); 
			}
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (Exception e) {
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
			    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
					mbox.setMessage("软件配置文件不正确，请联系客服");
					mbox.open();	
			    }
			    }); 
		}
		return ret;
	}

	
	
	
	
	
}
