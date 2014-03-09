package com.storeworld.stock;

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

import com.storeworld.mainui.ContentPart;
import com.storeworld.stock.StockPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;

public class StockContentPart extends ContentPart{
	
	private int FONT_SIZE = 12;
	private Composite current = null;
	public StockContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		current = parent;		
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;
		Button btnicon = new Button(this, SWT.NONE);
		btnicon.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 14, SWT.NORMAL));		
		btnicon.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnicon.setBounds((int)(w*0.2), (int)(h*0.2), (int)(w*0.1), (int)(w*0.1));
		btnicon.setText("\u8FDB\u8D27ICON");		
		Label label_in = new Label(this, SWT.NONE);
		label_in.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", FONT_SIZE, SWT.NORMAL));
		label_in.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label_in.setBackground(new Color(getDisplay(),63,63,63));
		label_in.setAlignment(SWT.CENTER);
		label_in.setBounds((int)(w*0.2), (int)(h*0.2)+(int)(w*0.1), (int)(w*0.1), (int)(w*0.02));
		label_in.setText("\u8FDB\u8D27");
		
		
		this.setBackgroundColor(new Color(getDisplay(),63,63,125));
						
	}
	
	
}
