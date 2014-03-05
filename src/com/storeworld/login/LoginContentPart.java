package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.mainui.ContentPart;

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
	
	public LoginContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		initialization();
	}
	

	public void initialization(){
		text1 = new Text(this, SWT.BORDER | SWT.WRAP);
		text1.setEnabled(false);
		text1.setEditable(false);
		text1.setBounds(180, 100, 50, 30);
		text2 = new Text(this, SWT.BORDER | SWT.WRAP);
		text2.setEnabled(false);
		text2.setEditable(false);
		text2.setBounds(230, 100, 50, 30);
		text3 = new Text(this, SWT.BORDER | SWT.WRAP);
		text3.setEnabled(false);
		text3.setEditable(false);
		text3.setBounds(280, 100, 50, 30);
		text4 = new Text(this, SWT.BORDER | SWT.WRAP);
		text4.setEnabled(false);
		text4.setEditable(false);
		text4.setBounds(330, 100, 50, 30);
		
		button_1 = new Button(this, SWT.NONE);
		button_1.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_1.setBounds(190, 170, 50, 50);
		button_1.setText("1");
		button_2 = new Button(this, SWT.NONE);
		button_2.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_2.setBounds(255, 170, 50, 50);
		button_2.setText("2");
		button_3 = new Button(this, SWT.NONE);
		button_3.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_3.setBounds(320, 170, 50, 50);
		button_3.setText("3");
		button_4 = new Button(this, SWT.NONE);
		button_4.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_4.setBounds(190, 235, 50, 50);
		button_4.setText("4");
		button_5 = new Button(this, SWT.NONE);
		button_5.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_5.setBounds(255, 235, 50, 50);
		button_5.setText("5");
		button_6 = new Button(this, SWT.NONE);
		button_6.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_6.setBounds(320, 235, 50, 50);
		button_6.setText("6");
		button_7 = new Button(this, SWT.NONE);
		button_7.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_7.setBounds(190, 300, 50, 50);
		button_7.setText("7");
		button_8 = new Button(this, SWT.NONE);
		button_8.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_8.setBounds(255, 300, 50, 50);
		button_8.setText("8");
		button_9 = new Button(this, SWT.NONE);
		button_9.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_9.setBounds(320, 300, 50, 50);
		button_9.setText("9");
		button_0 = new Button(this, SWT.NONE);
		button_0.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		button_0.setBounds(255, 365, 50, 50);
		button_0.setText("0");
		
		this.setBackgroundColor(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
						
	}
	
	
}
