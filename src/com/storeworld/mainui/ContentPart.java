package com.storeworld.mainui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * Content part definition
 * all Contentpart based on this
 * @author dingyuanxiong
 *
 */
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
		setBackgroundColor(new Color(parent.getDisplay(), 63, 63, 63));
	}	
		
}
