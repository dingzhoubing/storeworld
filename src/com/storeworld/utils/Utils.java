package com.storeworld.utils;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.storeworld.softwarekeyboard.ShowKeyBoardAdapter;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;

public class Utils {
	
	private static ImageLoader imageLoader = new ImageLoader();
	private static HashMap<NORTH_TYPE, Composite> northComps = new HashMap<NORTH_TYPE, Composite>();
	private static HashMap<CONTENT_TYPE, Composite> contentComps = new HashMap<CONTENT_TYPE, Composite>();
	public static void center(Shell shell) {

		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;

		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;

		if (shellH > screenH)
			shellH = screenH;

		if (shellW > screenW)
			shellW = screenW;

		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2));
	}
	
	public static Composite getNorthPartComposites(NORTH_TYPE type){
		Composite comp = null;
		switch (type) {
		case NORTH_BOTTOM:
		case NORTH_INDEX:
			comp = northComps.get(type);
			break;
		default:
			break;
		}
		return comp;		
	}
	public static Composite getContentPartComposites(CONTENT_TYPE type){
		Composite comp = null;
		switch (type) {
		case CONTENT_BOTTOM:
		case CONTENT_CUSTOMER:
		case CONTENT_DELIVER:
		case CONTENT_PRODUCT:
		case CONTENT_STOCK:
		case CONTENT_MAIN:
			comp = contentComps.get(type);
			break;
		default:
			break;
		}
		return comp;
	}
	
	public static void setNorthPartComposite(Composite comp, NORTH_TYPE type){
		northComps.put(type, comp);
	}
	public static void setContentPartComposite(Composite comp, CONTENT_TYPE type){
		contentComps.put(type, comp);
	}
	
	public static void bindTextKeyboard(ArrayList<Text> texts, Shell shell){
		for(int i=0;i<texts.size();i++){
			texts.get(i).addMouseListener(new ShowKeyBoardAdapter(texts.get(i), shell));
		}
	}
	
	public static Image getScaledImage(String imagepath, int width, int height){
		ImageData[] data = imageLoader.load(imagepath);
		Image imageScale = new Image(null, data[0].scaledTo(width, height));
		return imageScale;
	}
	public static Image getGrayImage(Image image){
		return new Image(null,image, SWT.IMAGE_GRAY);
	}
	
}
