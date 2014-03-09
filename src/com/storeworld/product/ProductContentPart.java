package com.storeworld.product;

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
import com.storeworld.stock.StockContentPart;
import com.storeworld.stock.StockPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

public class ProductContentPart extends ContentPart{
	
	private int FONT_SIZE = 12;
	private Composite current = null;
	public ProductContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		current = parent;		
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;

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
		
		this.setBackgroundColor(new Color(getDisplay(),63,63,125));
						
	}
	
	
}
