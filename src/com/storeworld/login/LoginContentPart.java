package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.analyze.AnalyzeContentPart;
import com.storeworld.customer.CustomerContentPart;
import com.storeworld.database.PasswordHandler;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.mainui.ContentPart;
import com.storeworld.mainui.CoolBarPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.product.ProductContentPart;
import com.storeworld.stock.StockContentPart;
import com.storeworld.utils.Utils;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;

/**
 * the logic of login
 * @author dingyuanxiong
 *
 */
public class LoginContentPart extends ContentPart{
		
	private Text text1;
	private Text text2;
	private Text text3;
	private Text text4;
	private Button button_1;
	private Button button_2;
	private Button button_3;
	private Button button_4;
	private Button button_5;
	private Button button_6;
	private Button button_7;
	private Button button_8;
	private Button button_9;
	private Button button_0;
	private Button reset;
	private Label lbl_tips;
	
	private ProgressBar progressBar;
	
	private static int counter = 1;
	private static String pwFirst = "";
	private static String pwSecond = "";
	Composite current = null;
	
//	private String password = "";
	private boolean first = false;
	private static boolean firstInput = false;
	
	private boolean resetPW = false;
	private boolean whileChanging = false;
	
	private boolean wrongcase = false;
	public LoginContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		current = parent;
		initialization();
	}
	
	
	public boolean getFirst(){
		return first;
	}
	public void setFirst(boolean ft){
		this.first = ft;
	}
	
	public boolean getResetPW(){
		return resetPW;
	}
	public void setResetPW(boolean ft){
		this.resetPW = ft;
	}
	
	public boolean getWhileChanging(){
		return whileChanging;
	}
	public void setWhileChanging(boolean swc){
		this.whileChanging = swc;
	}
	
	class PasswordSelectionAdapter extends SelectionAdapter{
		
		String val = "";
		PasswordSelectionAdapter(String val){
			this.val = val;
		}
		@Override
		public void widgetSelected(SelectionEvent e) {
			if(!wrongcase)
				setValue(this.val);
			else{
				//do nothing				
			}
		}
		
	}
	
	private void setValue(String val){
		lbl_tips.setText("");
		switch(counter){
		case 1:
			text1.setText("*");
			if(!firstInput)
				pwFirst+=val;
			else
				pwSecond+=val;
			counter++;
			break;
		case 2:
			text2.setText("*");
			if(!firstInput)
				pwFirst+=val;
			else
				pwSecond+=val;
			counter++;
			break;
		case 3:
			text3.setText("*");
			if(!firstInput)
				pwFirst+=val;
			else
				pwSecond+=val;
			counter++;
			break;
		case 4:
			text4.setText("*");
			if(!firstInput)
				pwFirst+=val;
			else
				pwSecond+=val;
			counter++;
			authCheck();
			break;			
		}
	}
	private void authCheck(){
		if(!Utils.getStatus() || getWhileChanging()){//login
		//not the first time
		if(!first){//test if the first time to login, if not the first time, should already exist an pw
			if(pwFirst.equals(PasswordHandler.getPassword())){//by default, it should be password
				counter = 1;
				lbl_tips.setText("正在登陆系统。。。");
				pwFirst = "";
				LoginMainUI.setInstanceNull();
				progressBar.setVisible(true);
				
				EntryPoint.entry(progressBar, current.getParent());									
//				shell.open();				
//				current.getParent().dispose();
			}else{
				lbl_tips.setText("您输入的密码不正确，请重试");
				text1.setText("");
				text2.setText("");
				text3.setText("");
				text4.setText("");
				counter = 1;
				pwFirst="";
			}		
		}else{//no pw or reset the pw, we need to input the pw twice
			if(!firstInput){//if first time
				if(!getResetPW()){//if not reset the pw
				lbl_tips.setText("请您再次输入同样的数字，此数字将作为您以后的登陆密码");
				text1.setText("");
				text2.setText("");
				text3.setText("");
				text4.setText("");
				counter = 1;			
				firstInput = true;
				}else{//reset the pw, we need to check the input old pw
					if(pwFirst.equals(PasswordHandler.getPassword())){//pw is right as the old one
						lbl_tips.setText("密码验证正确， 请输入新密码");
						counter = 1;
						pwFirst = "";
						pwSecond = "";
						firstInput = false;
						text1.setText("");
						text2.setText("");
						text3.setText("");
						text4.setText("");		
						setResetPW(false);//no need to validate the old pw any more
						
					}else{
						lbl_tips.setText("密码验证错误，请重新输入");
						counter = 1;
						pwFirst = "";
						pwSecond = "";
						firstInput = false;
						text1.setText("");
						text2.setText("");
						text3.setText("");
						text4.setText("");	
					}
				}
			}else{
				if(!pwFirst.equals(pwSecond)){
					lbl_tips.setText("您兩次輸入的密碼不相同，請重新輸入并確認密碼");
					text1.setText("");
					text2.setText("");
					text3.setText("");
					text4.setText("");
					counter = 1;			
					firstInput = false;
					pwFirst = "";
					pwSecond = "";
				}else{
					
					counter = 1;
					lbl_tips.setText("正在登陆系统。。。");
					setPasswordStored(pwFirst);
					PasswordHandler.setPassword(pwFirst);
					pwFirst = "";
					pwSecond = "";
					firstInput = false;
					LoginMainUI.setInstanceNull();
//					current.getParent().dispose();			
//					EntryPoint.entry(progressBar);
					
					if(!Utils.getStatus() && getWhileChanging()){//in login ui, we reset pw
						progressBar.setVisible(true);					
						EntryPoint.entry(progressBar, current.getParent());
					}else{//in unlock ui, we reset ow
						current.getParent().dispose();		
						Utils.changeStatus();//be false again
						//show the progress bar here to identify the progress 
						//TODO:
//						progressBar.setVisible(true);
//						progressBar.setSelection(5);
						MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());						
						shell.setActive();
//						System.out.println("shell is null: " + (shell==null));
						shell.setVisible(true);
					}
					
					
					setWhileChanging(false);//if reset, then, reset is false
				}
			}
		}
	}else{//unlock
		if(pwFirst.equals(PasswordHandler.getPassword())){//by default, it should be password
			counter = 1;
			pwFirst = "";
			LoginMainUI.setInstanceNull();
			current.getParent().dispose();		
			Utils.changeStatus();//be false again
			//show the progress bar here to identify the progress 
			//TODO:
//			progressBar.setVisible(true);
//			progressBar.setSelection(5);
			MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());						
			shell.setActive();
//			System.out.println("shell is null: " + (shell==null));
			shell.setVisible(true);
		}else{
			lbl_tips.setText("您输入的密码不正确，请重试");
			text1.setText("");
			text2.setText("");
			text3.setText("");
			text4.setText("");
			counter = 1;
			pwFirst="";
		}				
	}
	}
	
	private void setPasswordStored(String pw){
//		PasswordHandler ph = new PasswordHandler();
		PasswordHandler.setPasswordStored(pw);
	}
	private String getPasswordStored(){
		
//		PasswordHandler ph = new PasswordHandler();
		String password = PasswordHandler.getPasswordStored();
		return password;
		
	}

	public void initialization(){
		
		lbl_tips = new Label(this, SWT.CENTER|SWT.BORDER);//border need to removed
		lbl_tips.setBounds(150, 0, 260, 50);
		lbl_tips.setBackground(new Color(getDisplay(), 63, 63, 63));
		lbl_tips.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lbl_tips.setText("");
		
		
		text1 = new Text(this, SWT.BORDER | SWT.WRAP | SWT.CENTER);
		text1.setEnabled(false);
		text1.setEditable(false);
		text1.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.BOLD));
		text1.setBounds(180, 50, 50, 30);
		text2 = new Text(this, SWT.BORDER | SWT.WRAP | SWT.CENTER);
		text2.setEnabled(false);
		text2.setEditable(false);
		text2.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.BOLD));
		text2.setBounds(230, 50, 50, 30);
		text3 = new Text(this, SWT.BORDER | SWT.WRAP | SWT.CENTER);
		text3.setEnabled(false);
		text3.setEditable(false);
		text3.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.BOLD));
		text3.setBounds(280, 50, 50, 30);
		text4 = new Text(this, SWT.BORDER | SWT.WRAP | SWT.CENTER);
		text4.setEnabled(false);
		text4.setEditable(false);
		text4.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.BOLD));
		text4.setBounds(330, 50, 50, 30);
		
		button_1 = new Button(this, SWT.NONE);
		button_1.addSelectionListener(new PasswordSelectionAdapter("1"));
		button_1.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_1.setBounds(190, 110, 50, 50);
		button_1.setText("1");
		button_2 = new Button(this, SWT.NONE);
		button_2.addSelectionListener(new PasswordSelectionAdapter("2"));
		button_2.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_2.setBounds(255, 110, 50, 50);
		button_2.setText("2");
		button_3 = new Button(this, SWT.NONE);
		button_3.addSelectionListener(new PasswordSelectionAdapter("3"));
		button_3.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_3.setBounds(320, 110, 50, 50);
		button_3.setText("3");
		button_4 = new Button(this, SWT.NONE);
		button_4.addSelectionListener(new PasswordSelectionAdapter("4"));
		button_4.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_4.setBounds(190, 175, 50, 50);
		button_4.setText("4");
		button_5 = new Button(this, SWT.NONE);
		button_5.addSelectionListener(new PasswordSelectionAdapter("5"));
		button_5.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_5.setBounds(255, 175, 50, 50);
		button_5.setText("5");
		button_6 = new Button(this, SWT.NONE);
		button_6.addSelectionListener(new PasswordSelectionAdapter("6"));
		button_6.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_6.setBounds(320, 175, 50, 50);
		button_6.setText("6");
		button_7 = new Button(this, SWT.NONE);
		button_7.addSelectionListener(new PasswordSelectionAdapter("7"));
		button_7.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_7.setBounds(190, 240, 50, 50);
		button_7.setText("7");
		button_8 = new Button(this, SWT.NONE);
		button_8.addSelectionListener(new PasswordSelectionAdapter("8"));
		button_8.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_8.setBounds(255, 240, 50, 50);
		button_8.setText("8");
		button_9 = new Button(this, SWT.NONE);
		button_9.addSelectionListener(new PasswordSelectionAdapter("9"));
		button_9.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_9.setBounds(320, 240, 50, 50);
		button_9.setText("9");
		button_0 = new Button(this, SWT.NONE);
		button_0.addSelectionListener(new PasswordSelectionAdapter("0"));
		button_0.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_0.setBounds(255, 305, 50, 50);
		button_0.setText("0");
		
		reset = new Button(this, SWT.NONE);
		reset.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
//				this.first = true;
				setFirst(true);
				setResetPW(true);
				setWhileChanging(true);
				lbl_tips.setText("重置密码，请您输入旧密码");
				counter = 1;
				pwFirst = "";
				pwSecond = "";
				firstInput = false;
				text1.setText("");
				text2.setText("");
				text3.setText("");
				text4.setText("");
			}
			
		});
		reset.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
		reset.setBounds(530, 365, 50, 30);
		reset.setText("重置密码");
		
		progressBar = new ProgressBar(this, SWT.HORIZONTAL);
		progressBar.setBounds(10, 400, 570, 15);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setVisible(false);
		
		
		this.setBackgroundColor(new Color(getDisplay(), 63, 63, 63));
		
		String pw = getPasswordStored();
		if(pw.equals("-1")){
			this.first = true;
			lbl_tips.setText("这是您首次登陆系统，请您输入初始密码，初始密码为任意四位数");
			reset.setVisible(false);//
		}else if(pw.equals("-2")){
			this.wrongcase = true;
			lbl_tips.setText("验证文件损毁，请联系客服");
			reset.setVisible(false);
		}else{
			reset.setVisible(true);
			this.first = false;			
			lbl_tips.setText("请输入密码");
			PasswordHandler.setPassword(pw);//set the password
		}
						
	}
}
