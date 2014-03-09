package com.storeworld.analyze;

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

import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.mainui.ContentPart;
import com.storeworld.stock.StockContentPart;
import com.storeworld.stock.StockPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

public class AnalyzeContentPart extends ContentPart{
	
	private int FONT_SIZE = 12;
	private Composite current = null;
	public AnalyzeContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);
		current = parent;		
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;

		
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
		
		this.setBackgroundColor(new Color(getDisplay(),63,63,125));
						
	}
	
	
}
