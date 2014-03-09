package com.storeworld.customer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.analyze.AnalyzeContentPart;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.mainui.ContentPart;
import com.storeworld.product.ProductContentPart;
import com.storeworld.stock.StockContentPart;
import com.storeworld.stock.StockPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

public class CustomerContentPart extends ContentPart{
	
	private int FONT_SIZE = 12;
	private Composite current = null;
	public CustomerContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		current = parent;		
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;

		
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
	
		this.setBackgroundColor(new Color(getDisplay(),63,63,125));
						
	}
	
	
}
