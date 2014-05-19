package com.storeworld.analyze;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.storeworld.analyze.trendutils.TrendDataSet;

/**
 * show the trend graph of the result
 * @author dingyuanxiong
 *
 */
public class TrendComposite extends Composite implements AnalyzerBase{
	
	private TrendDataSet args;
	
	public TrendComposite(Composite parent, int style, TrendDataSet args) {
		super(parent, style);
		this.args = args;

		showComposite();
	}
	/**
	 * get the trend image and show it on the compiste
	 * @return
	 */
	private Image computeTrend(){
		Image image = null;
		//compute to get the trend image
		return image;
	}
	
	public void showComposite(){
//		int width= args.getWidth();
//		int height = args.getHeight();
		
		int width= 881;
		int height = 558;		
		this.setSize(width, height);
		
		
		Label lbl_title = new Label(this, SWT.BORDER);
		lbl_title.setBounds(0, 0, (int)(width/10), (int)(height/20));
		lbl_title.setText("äNÁ¿Ú…„Ý");
		
		Image image = computeTrend();			
		Composite composite_image  = new Composite(this, SWT.BORDER);
		composite_image.setBackground(new Color(this.getDisplay(), 204, 250, 204));
		composite_image.setBounds(0, (int)(height/20), (int)(width), (int)(5*height/10));		

	}
	/**
	 * release the source
	 */
	public void remove(){
		this.dispose();
	}
	
}

