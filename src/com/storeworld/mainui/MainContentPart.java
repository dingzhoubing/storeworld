package com.storeworld.mainui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.stock.StockUITest;

public class MainContentPart extends ContentPart{

	private MainUI main = null;
	public MainContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		main = (MainUI) parent;
		initialization();
	}
	

	public void initialization(){
		//add button and label in the part
		Button btnicon = new Button(this, SWT.NONE);
		btnicon.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon.setSelection(true);
		btnicon.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon.setBounds(250, 200, 100, 100);
		btnicon.setText("\u8FDB\u8D27ICON");
		
		Label label_in = new Label(this, SWT.NONE);
		label_in.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_in.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_in.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		label_in.setAlignment(SWT.CENTER);
		label_in.setBounds(250, 310, 100, 30);
		label_in.setText("\u8FDB\u8D27");
		//not a good way
		btnicon.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				main.dispose();
				String[] args = null;
				StockUITest.main(args);
			}
		});
		
		
		Button btnicon_1 = new Button(this, SWT.NONE);
		btnicon_1.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_1.setText("\u9001\u8D27ICON");
		btnicon_1.setBounds(500, 200, 100, 100);
		Label label_out = new Label(this, SWT.NONE);
		label_out.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_out.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_out.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		label_out.setAlignment(SWT.CENTER);
		label_out.setBounds(500, 310, 100, 30);
		label_out.setText("\u9001\u8D27");
		
		
		Button btnicon_2 = new Button(this, SWT.NONE);
		btnicon_2.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_2.setText("\u76D8\u4ED3ICON");
		btnicon_2.setBounds(750, 200, 100, 100);
		Label label_analyze = new Label(this, SWT.NONE);
		label_analyze.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_analyze.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_analyze.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		label_analyze.setAlignment(SWT.CENTER);
		label_analyze.setBounds(750, 310, 100, 30);
		label_analyze.setText("\u76D8\u4ED3");
		
		
		Button btnicon_3 = new Button(this, SWT.NONE);
		btnicon_3.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_3.setText("\u8D27\u54C1ICON");
		btnicon_3.setBounds(250, 500, 100, 100);
		Label label_product = new Label(this, SWT.NONE);
		label_product.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_product.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_product.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		label_product.setAlignment(SWT.CENTER);
		label_product.setBounds(250, 610, 100, 30);
		label_product.setText("\u8D27\u54C1");
		
		
		Button btnicon_4 = new Button(this, SWT.NONE);
		btnicon_4.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_4.setText("\u5BA2\u6237ICON");
		btnicon_4.setBounds(500, 500, 100, 100);
		Label label_customer = new Label(this, SWT.NONE);
		label_customer.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_customer.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_customer.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		label_customer.setAlignment(SWT.CENTER);
		label_customer.setBounds(500, 610, 100, 30);
		label_customer.setText("\u5BA2\u6237");
		
		Button btnicon_5 = new Button(this, SWT.NONE);
		btnicon_5.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));
		btnicon_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon_5.setText("\u4FDD\u5BC6ICON");
		btnicon_5.setBounds(750, 500, 100, 100);
		Label label_lock = new Label(this, SWT.NONE);
		label_lock.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 12, SWT.NORMAL));
		label_lock.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_lock.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		label_lock.setAlignment(SWT.CENTER);
		label_lock.setBounds(750, 610, 100, 30);
		label_lock.setText("\u4FDD\u5BC6");
		
		this.setBackgroundColor(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
						
	}
	
	
}
