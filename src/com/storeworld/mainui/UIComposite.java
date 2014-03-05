package com.storeworld.mainui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class UIComposite extends Composite implements UIPart{
	public Image image = null;
	public Color foregroundcolor = null;
	public Color backgroundcolor = null;
	public Cursor cursor = null; //new Cursor(getDisplay(), SWT.CURSOR_SIZEALL);
	public UIComposite(Composite parent, int style) {
		super(parent, style);	
	}

	@Override
	public void setImage(Image image) {
		this.image = image;
		this.setBackgroundImage(image);
		
	}
	@Override
	public Image getImage() {
		return this.image;
	}

	@Override
	public Color getForegroundColor() {
		return this.foregroundcolor;
	}

	@Override
	public void setForegroundColor(Color color) {
		this.foregroundcolor = color;		
		this.setForeground(color);
	}

	@Override
	public Color getBackgroundColor() {
		return this.backgroundcolor;
	}	

	@Override
	public void setBackgroundColor(Color color) {
		this.backgroundcolor = color;		
		this.setBackground(color);
	}
	
	
	@Override
	public Cursor getPartCursor() {
		return this.cursor;
	}
	
	@Override
	public void setPartCursor(Cursor cursor) {
		this.cursor = cursor;	
		this.setCursor(cursor);
	}

	@Override
	public void dispose() {
		if(image != null)
			this.image.dispose();
		if(backgroundcolor != null)
			this.backgroundcolor.dispose();
		if(foregroundcolor != null)
			this.foregroundcolor.dispose();
		if(cursor != null)
			this.cursor.dispose();
//		System.out.println("there dispose");
	}	
}
