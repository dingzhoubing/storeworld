package com.storeworld.mainui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class EastPart extends UIComposite {

	public EastPart(Composite parent, int style) {
		super(parent, style);		
	}

	public EastPart(Composite parent, int style, Image image) {
		super(parent, style);		
		if(image == null){//by default
			image = new Image(getDisplay(), "icon/east.png");
		}
		setImage(image);
	}	


}
