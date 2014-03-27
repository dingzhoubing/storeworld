package com.storeworld.mainui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class SouthPart extends UIComposite {

	public SouthPart(Composite parent, int style) {
		super(parent, style);		
	}

	public SouthPart(Composite parent, int style, Image image) {
		super(parent, style);		
		if(image == null){//by default
			image = new Image(getDisplay(), "icon/south.png");
		}
		setImage(image);
	}	


}
