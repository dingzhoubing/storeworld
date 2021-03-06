package com.storeworld.mainui;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class NorthPart extends UIComposite {
	
	public NorthPart(Composite parent, int style) {
		super(parent, style);
	
	}

	public NorthPart(Composite parent, int style, Image image) {
		super(parent, style);		
		if(image == null){//by default
			image = new Image(getDisplay(), "icon/north.png");
		}
		setImage(image);
		setBackgroundColor(new Color(parent.getDisplay(), 63, 63, 63));
	}	

	
}
