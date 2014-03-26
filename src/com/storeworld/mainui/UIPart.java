package com.storeworld.mainui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;

/**
 * interface of common UI functions
 * @author dingyuanxiong
 *
 */
public interface UIPart {

	//image of the part
	public void setImage(Image image);
	public Image getImage();

	////set cursor, maybe no need to set this
	public void setPartCursor(Cursor cursor);
	public Cursor getPartCursor();
	//set color, may be background color and front color
	public Color getForegroundColor();
	public void setForegroundColor(Color color);
	public Color getBackgroundColor();
	public void setBackgroundColor(Color color);
}
