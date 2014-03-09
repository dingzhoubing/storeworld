package com.storeworld.deliver;

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
import com.storeworld.stock.StockContentPart;
import com.storeworld.stock.StockPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;

public class DeliverContentPart extends ContentPart{
	
	private int FONT_SIZE = 12;
	private Composite current = null;
	public DeliverContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		current = parent;		
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;

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
		
		this.setBackgroundColor(new Color(getDisplay(),63,63,125));
						
	}
	
	
}
