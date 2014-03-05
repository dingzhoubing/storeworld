package com.storeworld.mainui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ContentPart extends UIComposite {
	
	public ContentPart(Composite parent, int style) {
		super(parent, style);		
	}

	public ContentPart(Composite parent, int style, Image image) {
		super(parent, style);		
		if(image == null){//by default
			//contentpart has no image?
			//image = new Image(getDisplay(), "icon/northwest.png");
		}else{
			setImage(image);
		}		
	}	
		
}
