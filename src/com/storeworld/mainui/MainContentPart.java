package com.storeworld.mainui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainContentPart extends ContentPart{
	
	private int FONT_SIZE = 12;
	private Composite current = null;
	public MainContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		current = parent;		
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;
//		int x = current.getBounds().x;
//		int y = current.getBounds().y;
		//add button and label in the part
		Button btnicon = new Button(this, SWT.NONE);
		btnicon.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));		
		btnicon.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
//		System.out.println(w+":"+h+":"+x+":"+y);
		btnicon.setBounds((int)(w*0.2), (int)(h*0.2), (int)(w*0.1), (int)(w*0.1));
		btnicon.setText("\u8FDB\u8D27ICON");		
		Label label_in = new Label(this, SWT.NONE);
		label_in.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", FONT_SIZE, SWT.NORMAL));
		label_in.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_in.setBackground(new Color(getDisplay(),63,63,63));
		label_in.setAlignment(SWT.CENTER);
		label_in.setBounds((int)(w*0.2), (int)(h*0.2)+(int)(w*0.1), (int)(w*0.1), (int)(w*0.02));
		label_in.setText("\u8FDB\u8D27");
		//not a good way
		btnicon.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				main.dispose();
//				String[] args = null;
//				StockUITest.main(args);
			}
		});
		
		
		Button btnicon_1 = new Button(this, SWT.NONE);
		btnicon_1.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_1.setText("\u9001\u8D27ICON");
		btnicon_1.setBounds((int)(w*0.2) + (int)(w*0.25), (int)(h*0.2), (int)(w*0.1), (int)(w*0.1));
		Label label_out = new Label(this, SWT.NONE);
		label_out.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", FONT_SIZE, SWT.NORMAL));
		label_out.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_out.setBackground(new Color(getDisplay(),63,63,63));
		label_out.setBounds((int)(w*0.2)+(int)(w*0.25), (int)(h*0.2)+(int)(w*0.1), (int)(w*0.1), (int)(w*0.02));
		label_out.setAlignment(SWT.CENTER);
		label_out.setText("\u9001\u8D27");
		
		
		Button btnicon_2 = new Button(this, SWT.NONE);
		btnicon_2.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_2.setText("\u76D8\u4ED3ICON");
		btnicon_2.setBounds((int)(w*0.2) + (int)(w*0.5), (int)(h*0.2), (int)(w*0.1), (int)(w*0.1));
		Label label_analyze = new Label(this, SWT.NONE);
		label_analyze.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", FONT_SIZE, SWT.NORMAL));
		label_analyze.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_analyze.setBackground(new Color(getDisplay(),63,63,63));
		label_analyze.setAlignment(SWT.CENTER);
		label_analyze.setBounds((int)(w*0.2)+(int)(w*0.5), (int)(h*0.2)+(int)(w*0.1), (int)(w*0.1), (int)(w*0.02));
		label_analyze.setText("\u76D8\u4ED3");
		
		
		Button btnicon_3 = new Button(this, SWT.NONE);
		btnicon_3.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_3.setText("\u8D27\u54C1ICON");
		btnicon_3.setBounds((int)(w*0.2), (int)(h*0.2)+(int)(w*0.1)+(int)(h*0.2), (int)(w*0.1), (int)(w*0.1));
		Label label_product = new Label(this, SWT.NONE);
		label_product.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", FONT_SIZE, SWT.NORMAL));
		label_product.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_product.setBackground(new Color(getDisplay(),63,63,63));
		label_product.setAlignment(SWT.CENTER);
		label_product.setBounds((int)(w*0.2), (int)(h*0.2)+(int)(w*0.1)+(int)(h*0.2)+(int)(w*0.1), (int)(w*0.1), (int)(w*0.02));
		label_product.setText("\u8D27\u54C1");
		
		
		Button btnicon_4 = new Button(this, SWT.NONE);
		btnicon_4.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_4.setText("\u5BA2\u6237ICON");
		btnicon_4.setBounds((int)(w*0.2)+(int)(w*0.25), (int)(h*0.2)+(int)(w*0.1)+(int)(h*0.2), (int)(w*0.1), (int)(w*0.1));
		Label label_customer = new Label(this, SWT.NONE);
		label_customer.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", FONT_SIZE, SWT.NORMAL));
		label_customer.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_customer.setBackground(new Color(getDisplay(),63,63,63));
		label_customer.setAlignment(SWT.CENTER);
		label_customer.setBounds((int)(w*0.2)+(int)(w*0.25), (int)(h*0.2)+(int)(w*0.1)+(int)(h*0.2)+(int)(w*0.1), (int)(w*0.1), (int)(w*0.02));
		label_customer.setText("\u5BA2\u6237");
		
		Button btnicon_5 = new Button(this, SWT.NONE);
		btnicon_5.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_5.setText("\u4FDD\u5BC6ICON");
		btnicon_5.setBounds((int)(w*0.2)+(int)(w*0.5), (int)(h*0.2)+(int)(w*0.1)+(int)(h*0.2), (int)(w*0.1), (int)(w*0.1));
		Label label_lock = new Label(this, SWT.NONE);
		label_lock.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_lock.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_lock.setBackground(new Color(getDisplay(),63,63,63));
		label_lock.setAlignment(SWT.CENTER);
		label_lock.setBounds((int)(w*0.2)+(int)(w*0.5), (int)(h*0.2)+(int)(w*0.1)+(int)(h*0.2)+(int)(w*0.1), (int)(w*0.1), (int)(w*0.02));
		label_lock.setText("\u4FDD\u5BC6");
		
		this.setBackgroundColor(new Color(getDisplay(),63,63,63));
						
	}
	
	
}
