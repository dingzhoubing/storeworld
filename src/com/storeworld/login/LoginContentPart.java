package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.mainui.ContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Utils;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

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
	private Label lbl_tips;
	
	private static int counter = 1;
	private static String pwFirst = "";
	private static String pwSecond = "";
	Composite current = null;
	
	private String password = "";
	private boolean first = false;
	private boolean firstInput = false;
	public LoginContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		current = parent;
		initialization();
	}
	
	class PasswordSelectionAdapter extends SelectionAdapter{
		
		String val = "";
		PasswordSelectionAdapter(String val){
			this.val = val;
		}
		@Override
		public void widgetSelected(SelectionEvent e) {
			setValue(this.val);
		}
		
	}
	
	private void setValue(String val){
		lbl_tips.setText("");
		switch(counter){
		case 1:
			text1.setText("   *");
			if(!firstInput)
				pwFirst+=val;
			else
				pwSecond+=val;
			counter++;
			break;
		case 2:
			text2.setText("   *");
			if(!firstInput)
				pwFirst+=val;
			else
				pwSecond+=val;
			counter++;
			break;
		case 3:
			text3.setText("   *");
			if(!firstInput)
				pwFirst+=val;
			else
				pwSecond+=val;
			counter++;
			break;
		case 4:
			text4.setText("   *");
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
		if(!Utils.getStatus()){//login
		//not the first time
		if(!first){
			if(pwFirst.equals("1234")){//by default, it should be password
				counter = 1;
				pwFirst = "";
				LoginMainUI.setInstanceNull();
				current.getParent().dispose();			
				EntryPoint.entry();				
			}else{
				lbl_tips.setText("您输入的密码不正确，请重试");
				text1.setText("");
				text2.setText("");
				text3.setText("");
				text4.setText("");
				counter = 1;
				pwFirst="";
			}		
		}else{
			if(!firstInput){
				lbl_tips.setText("请您再次输入同样的数字，此数字将作为您以后的登陆密码");
				text1.setText("");
				text2.setText("");
				text3.setText("");
				text4.setText("");
				counter = 1;			
				firstInput = true;
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
					pwFirst = "";
					pwSecond = "";
					setPasswordStored();
					LoginMainUI.setInstanceNull();
					current.getParent().dispose();			
					EntryPoint.entry();
				}
			}
		}
	}else{//unlock
		if(pwFirst.equals("1234")){//by default, it should be password
			counter = 1;
			pwFirst = "";
			LoginMainUI.setInstanceNull();
			current.getParent().dispose();		
			Utils.changeStatus();//be false again
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
	
	private void setPasswordStored(){
		//set the password into databse
	}
	private String getPasswordStored(){
		password = "12";
		return password;
		//check from the database
	}

	public void initialization(){
		
		lbl_tips = new Label(this, SWT.CENTER);
		lbl_tips.setBounds(150, 20, 260, 50);
		lbl_tips.setBackground(new Color(getDisplay(), 63, 63, 63));
		lbl_tips.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lbl_tips.setText("");
		
		
		text1 = new Text(this, SWT.BORDER | SWT.WRAP);
		text1.setEnabled(false);
		text1.setEditable(false);
		text1.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.BOLD));
		text1.setBounds(180, 100, 50, 30);
		text2 = new Text(this, SWT.BORDER | SWT.WRAP);
		text2.setEnabled(false);
		text2.setEditable(false);
		text2.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.BOLD));
		text2.setBounds(230, 100, 50, 30);
		text3 = new Text(this, SWT.BORDER | SWT.WRAP);
		text3.setEnabled(false);
		text3.setEditable(false);
		text3.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.BOLD));
		text3.setBounds(280, 100, 50, 30);
		text4 = new Text(this, SWT.BORDER | SWT.WRAP);
		text4.setEnabled(false);
		text4.setEditable(false);
		text4.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.BOLD));
		text4.setBounds(330, 100, 50, 30);
		
		button_1 = new Button(this, SWT.NONE);
		button_1.addSelectionListener(new PasswordSelectionAdapter("1"));
		button_1.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_1.setBounds(190, 170, 50, 50);
		button_1.setText("1");
		button_2 = new Button(this, SWT.NONE);
		button_2.addSelectionListener(new PasswordSelectionAdapter("2"));
		button_2.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_2.setBounds(255, 170, 50, 50);
		button_2.setText("2");
		button_3 = new Button(this, SWT.NONE);
		button_3.addSelectionListener(new PasswordSelectionAdapter("3"));
		button_3.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_3.setBounds(320, 170, 50, 50);
		button_3.setText("3");
		button_4 = new Button(this, SWT.NONE);
		button_4.addSelectionListener(new PasswordSelectionAdapter("4"));
		button_4.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_4.setBounds(190, 235, 50, 50);
		button_4.setText("4");
		button_5 = new Button(this, SWT.NONE);
		button_5.addSelectionListener(new PasswordSelectionAdapter("5"));
		button_5.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_5.setBounds(255, 235, 50, 50);
		button_5.setText("5");
		button_6 = new Button(this, SWT.NONE);
		button_6.addSelectionListener(new PasswordSelectionAdapter("6"));
		button_6.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_6.setBounds(320, 235, 50, 50);
		button_6.setText("6");
		button_7 = new Button(this, SWT.NONE);
		button_7.addSelectionListener(new PasswordSelectionAdapter("7"));
		button_7.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_7.setBounds(190, 300, 50, 50);
		button_7.setText("7");
		button_8 = new Button(this, SWT.NONE);
		button_8.addSelectionListener(new PasswordSelectionAdapter("8"));
		button_8.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_8.setBounds(255, 300, 50, 50);
		button_8.setText("8");
		button_9 = new Button(this, SWT.NONE);
		button_9.addSelectionListener(new PasswordSelectionAdapter("9"));
		button_9.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_9.setBounds(320, 300, 50, 50);
		button_9.setText("9");
		button_0 = new Button(this, SWT.NONE);
		button_0.addSelectionListener(new PasswordSelectionAdapter("0"));
		button_0.setFont(SWTResourceManager.getFont("微软雅黑", 14, SWT.NORMAL));
		button_0.setBounds(255, 365, 50, 50);
		button_0.setText("0");
		
		this.setBackgroundColor(new Color(getDisplay(), 63, 63, 63));
		
		if(getPasswordStored().equals("")){
			this.first = true;
			lbl_tips.setText("这是您首次登陆系统，请您输入初始密码，初始密码为任意四位数");
		}
						
	}
}
