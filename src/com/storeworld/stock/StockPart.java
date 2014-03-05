package com.storeworld.stock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.mainui.NorthPart;

public class StockPart extends NorthPart{

	Display display = null;
	public StockPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, new Image(parent.getDisplay(), "icon/north_2.png"));
		display = parent.getDisplay();
		initialization();
	}
	

	public void initialization(){
		Button button_index = new Button(this, SWT.NONE);
		button_index.setBounds(30, 20, 45, 45);
		button_index.setText("1");
		Label label_index = new Label(this, SWT.NONE);
		label_index.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_index.setBackground(new Color(getDisplay(), 127,127,127));
		label_index.setAlignment(SWT.CENTER);
		label_index.setBounds(30, 70, 45, 17);
		label_index.setText("\u9996\u9875");
		
		Button button_in = new Button(this, SWT.NONE);
		button_in.setBounds(100, 20, 45, 45);
		button_in.setText("2");
		Label label_in = new Label(this, SWT.NONE);
		label_in.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_in.setBackground(new Color(getDisplay(), 127,127,127));
		label_in.setAlignment(SWT.CENTER);
		label_in.setBounds(100, 70, 45, 17);
		label_in.setText("\u8FDB\u8D27");
		
		Button button_out = new Button(this, SWT.NONE);
		button_out.setBounds(170, 20, 45, 45);
		button_out.setText("3");
		Label label_out = new Label(this, SWT.NONE);
		label_out.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_out.setBackground(new Color(getDisplay(), 127,127,127));
		label_out.setAlignment(SWT.CENTER);
		label_out.setBounds(170, 70, 45, 17);
		label_out.setText("\u9001\u8D27");
		
		Button button_aly = new Button(this, SWT.NONE);
		button_aly.setBounds(240, 20, 45, 45);
		button_aly.setText("4");
		Label label_aly = new Label(this, SWT.NONE);
		label_aly.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_aly.setBackground(new Color(getDisplay(), 127,127,127));
		label_aly.setAlignment(SWT.CENTER);
		label_aly.setBounds(240, 70, 45, 17);
		label_aly.setText("\u76D8\u4ED3");
		
		Button button_lock = new Button(this, SWT.NONE);
		button_lock.setBounds(310, 20, 45, 45);
		button_lock.setText("5");
		Label label_lock = new Label(this, SWT.NONE);
		label_lock.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_lock.setBackground(new Color(getDisplay(), 127,127,127));
		label_lock.setAlignment(SWT.CENTER);
		label_lock.setBounds(310, 70, 45, 17);
		label_lock.setText("\u4FDD\u5BC6");
		
		
		Button button_prod = new Button(this, SWT.NONE);
		button_prod.setBounds(930, 20, 45, 45);
		button_prod.setText("6");
		Label label_prod = new Label(this, SWT.NONE);
		label_prod.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_prod.setBackground(new Color(getDisplay(), 127,127,127));
		label_prod.setAlignment(SWT.CENTER);
		label_prod.setBounds(930, 70, 45, 17);
		label_prod.setText("\u8D27\u54C1");
		
		Button button_cus = new Button(this, SWT.NONE);
		button_cus.setBounds(1000, 20, 45, 45);
		button_cus.setText("7");
		Label label_cus = new Label(this, SWT.NONE);
		label_cus.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_cus.setBackground(new Color(getDisplay(), 127,127,127));
		label_cus.setAlignment(SWT.CENTER);
		label_cus.setBounds(1000, 70, 45, 17);
		label_cus.setText("\u5BA2\u6237");
		
//		this.setCursor(new Cursor(display, SWT.CURSOR_SIZEALL));
		this.setBackgroundColor(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
						
	}
	
	
}
