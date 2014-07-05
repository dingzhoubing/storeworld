package com.storeworld.login;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.storeworld.mainui.ContentPart;
import com.storeworld.mainui.EastPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.mainui.NorthEastPart;
import com.storeworld.mainui.NorthPart;
import com.storeworld.mainui.NorthWestPart;
import com.storeworld.mainui.SouthEastPart;
import com.storeworld.mainui.SouthPart;
import com.storeworld.mainui.SouthWestPart;
import com.storeworld.mainui.WestPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.LOGIN_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

/**
 * 
 * @author dingyuanxiong
 * the wheels of MainUI, a simple one
 * to show the UI of the login page
 */
public class LoginMainUI extends Shell implements ControlListener, PaintListener,
		MouseListener, MouseMoveListener, MouseTrackListener {

	private NorthWestPart northwestpart = null;
	private NorthEastPart northeastpart = null;
	private NorthPart northpart = null;
	private SouthWestPart southwestpart = null;
	private SouthEastPart southeastpart = null;
	private SouthPart southpart = null;
	private WestPart westpart = null;
	private EastPart eastpart = null;
	private ContentPart contentpart = null;

	// window button
	private Composite closeButton;
	private Composite minButton;
	
	private Composite up;//for move
	// icon
	private Image closeImage;
	private Image closeOverImage;
	private Image closeDownImage;
	private Image minImage;
	private Image minOverImage;
	private Image minDownImage;

	private Point location;
	private StackLayout northLayout;
	private StackLayout contentLayout;
	
	private double ratio = 0.0;//the percent of the northpart

	//one instance in the system
	private static LoginMainUI instance = null; 
	private LoginMainUI(Display display) {
		super(display, SWT.NO_TRIM);
		northLayout = new StackLayout();
		contentLayout = new StackLayout();
	}
	
	public static LoginMainUI getMainUI_Instance(Display display){
		if (instance == null) {
			instance = new LoginMainUI(display);
			instance.setActive();
	       }

	       return instance;
	}
	
	public static void setInstanceNull(){
		instance = null;
	}
	public void setRatio(double rat){
		if(rat < 0)
			rat = 0.0;
		this.ratio = rat;
	}
	public double getRatio(){
		return this.ratio;
	}

	//we may need to get/set all the nine parts of the UI, but mainly, we need
	//these two parts(contentpart & northpart)
	public void setContentPart(ContentPart contentpart, LOGIN_TYPE type){
//		this.contentpart = contentpart;
		Utils.setLoginPartComposite(contentpart, type);
	}
	public Composite getContentPart(LOGIN_TYPE type){
		return Utils.getLoginPartComposites(type);
	}
	
	
	 
	
	public void show_Content_main(){
		contentLayout.topControl = getContentPart(LOGIN_TYPE.LOGIN_INPUT);
		contentpart.layout();
	}

		
	
	public void setup(){
		initialClose_Min();
		createContents();
		addControlListener(this);
	}

	public void initialClose_Min(){//HashMap<String, String> close_min
		closeButton = new Composite(this, SWT.NONE);
		minButton = new Composite(this, SWT.NONE);
		closeImage = new Image(getDisplay(), "icon/close.png");
		closeOverImage = new Image(getDisplay(), "icon/closeover.png");
		closeDownImage = new Image(getDisplay(), "icon/closedown.png");
		minImage = new Image(getDisplay(), "icon/min.png");
		minOverImage = new Image(getDisplay(), "icon/minover.png");
		minDownImage = new Image(getDisplay(), "icon/mindown.png");
		closeButton.setBackgroundImage(closeImage);
		minButton.setBackgroundImage(minImage);
	}

	/**
	 * Create contents of the window
	 * at initial, all parts are by default
	 * 
	 */
	private void createContents() {
		int w = getSize().x;
		int h = getSize().y;
//		System.out.println(w+ ":" +h+ "wh");
		// cut the whole shell into nine piece
		if (northwestpart == null){
			northwestpart = new NorthWestPart(this, SWT.NONE, null);
			northwestpart.setBounds(0, 0,
					northwestpart.getImage().getBounds().width, northwestpart
							.getImage().getBounds().height);
		}
		if (northeastpart == null){
			northeastpart = new NorthEastPart(this, SWT.NONE, null);
			northeastpart.setBounds(w - northeastpart.getImage().getBounds().width,
					0, northeastpart.getImage().getBounds().width, northeastpart
							.getImage().getBounds().height);
		}
		//use the up part to control movable
		up = new Composite(this, SWT.NONE);
		Image image_up = new Image(getDisplay(), "icon/up.png");
		up.setBackgroundImage(image_up);
		up.setCursor(new Cursor(getDisplay(),SWT.CURSOR_SIZEALL));
		up.setBounds(northwestpart.getImage().getBounds().width,
						0,
						w - northwestpart.getImage().getBounds().width
								- northeastpart.getImage().getBounds().width
								- closeImage.getBounds().width
								- minImage.getBounds().width, image_up
								.getBounds().height);
		closeButton.setBounds(w - northeastpart.getImage().getBounds().width
				- closeImage.getBounds().width, 0,
				closeImage.getBounds().width, closeImage.getBounds().height);
		minButton.setBounds(w - northeastpart.getImage().getBounds().width
				- closeImage.getBounds().width- minImage.getBounds().width, 0,
				minImage.getBounds().width, minImage.getBounds().height);
		if (northpart == null){
			northpart = new NorthPart(this, SWT.NONE, null);
			if (Math.abs((this.ratio - 0.0)) < 1e-6) {
//				northpart.setBounds(
//						northwestpart.getImage().getBounds().width,
//						0,
//						w - northwestpart.getImage().getBounds().width
//								- northeastpart.getImage().getBounds().width
//								- closeImage.getBounds().width
//								- minImage.getBounds().width, 0);
				northpart.setBounds(
						0,
						northwestpart.getImage().getBounds().height,
						w, 0);
			}else{
//				northpart.setBounds(
//						northwestpart.getImage().getBounds().width,
//						northpart.getImage().getBounds().height,
//						w - northwestpart.getImage().getBounds().width
//								- northeastpart.getImage().getBounds().width
//								- closeImage.getBounds().width
//								- minImage.getBounds().width, (int)(h * this.ratio)-northpart.getImage().getBounds().height);
				northpart.setBounds(
						0,
						image_up.getBounds().height,
						w, 124);
//				System.out.println(northpart.getBounds().toString()+" northpart");
			}
//			Utils.setNorthPartComposite(northpart, NORTH_TYPE.NORTH_BOTTOM);			
			northpart.setLayout(northLayout);
			//show the title
			final Label lbl_title = new Label(up, SWT.NONE);
			lbl_title.setBounds(0, 12, (int)(5*w/18),(int)(w/18/2));
			lbl_title.setBackground(new Color(up.getDisplay(),63, 63,63));
			lbl_title.setForeground(new Color(up.getDisplay(),255, 255,255));
			lbl_title.setText("钱多多 - 进出货小助手");
		}
		if (southwestpart == null){
			southwestpart = new SouthWestPart(this, SWT.NONE, null);
			southwestpart.setBounds(0, h
					- southwestpart.getImage().getBounds().height, southwestpart
					.getImage().getBounds().width, southwestpart.getImage()
					.getBounds().height);
		}
		if (southeastpart == null){
			southeastpart = new SouthEastPart(this, SWT.NONE, null);
			southeastpart.setBounds(w - southeastpart.getImage().getBounds().width,
					h - southeastpart.getImage().getBounds().height, southeastpart
							.getImage().getBounds().width, southeastpart.getImage()
							.getBounds().height);
		}
		if (southpart == null){
			southpart = new SouthPart(this, SWT.NONE, null);
			southpart.setBounds(southwestpart.getImage().getBounds().width, h
					- southpart.getImage().getBounds().height, w
					- southwestpart.getImage().getBounds().width
					- southwestpart.getImage().getBounds().width, southpart
					.getImage().getBounds().height);
		}
		if (westpart == null){
			westpart = new WestPart(this, SWT.NONE, null);
			westpart.setBounds(0, northwestpart.getImage().getBounds().height,
					westpart.getImage().getBounds().width, h
							- northwestpart.getImage().getBounds().height
							- southwestpart.getImage().getBounds().height);
		}
		if (eastpart == null){
			eastpart = new EastPart(this, SWT.NONE, null);
			eastpart.setBounds(w - eastpart.getImage().getBounds().width,
					northeastpart.getImage().getBounds().height, eastpart
							.getImage().getBounds().width, h
							- northeastpart.getImage().getBounds().height
							- southeastpart.getImage().getBounds().height);
		}
		//by default, set the color into dark_shadow
		if (contentpart == null){
			contentpart = new ContentPart(this, SWT.NONE, null);
			if (Math.abs((this.ratio - 0.0)) < 1e-6) {
				contentpart.setBounds(westpart.getImage().getBounds().width,
						northpart.getImage().getBounds().height, w
								- westpart.getImage().getBounds().width
								- eastpart.getImage().getBounds().width, h
								- northpart.getImage().getBounds().height
								- southpart.getImage().getBounds().height);
			} else {
				contentpart.setBounds(westpart.getImage().getBounds().width,
						(int)(h * this.ratio), w
								- westpart.getImage().getBounds().width
								- eastpart.getImage().getBounds().width, h
								- (int)(h * this.ratio)
								- southpart.getImage().getBounds().height);		
//				System.out.println(contentpart.getBounds().toString()+" contentpart");
			}
			Utils.setLoginPartComposite(contentpart, LOGIN_TYPE.LOGIN_BOTTOM);
			contentpart.setLayout(contentLayout);
		}
		
		//set the background color
		this.setBackground(new Color(getDisplay(), 63,63,63));
		// add listener as you want here for each part
		contentpart.addPaintListener(this);
//		northpart.addMouseListener(this);
//		northpart.addMouseMoveListener(this);
//		northpart.addPaintListener(this);
		up.addMouseListener(this);
		up.addMouseMoveListener(this);
		up.addPaintListener(this);
		southeastpart.addMouseListener(this);// resize, disable now
		southeastpart.addMouseMoveListener(this);// resize, disable now
		
		closeButton.addMouseListener(this);
		closeButton.addMouseTrackListener(this);
		minButton.addMouseListener(this);
		minButton.addMouseTrackListener(this);

	}

	public void controlMoved(ControlEvent e) {

	}

	//there is no resize any more, but still show the framwork
	public void controlResized(ControlEvent e) {
		int w = getSize().x;
		int h = getSize().y;
		/*
		northwestpart.setBounds(0, 0,
				northwestpart.getImage().getBounds().width, northwestpart
						.getImage().getBounds().height);
		northeastpart.setBounds(w - northeastpart.getImage().getBounds().width,
				0, northeastpart.getImage().getBounds().width, northeastpart
						.getImage().getBounds().height);
		// close button and min button
		closeButton.setBounds(w - northeastpart.getImage().getBounds().width
				- closeImage.getBounds().width, 0,
				closeImage.getBounds().width, closeImage.getBounds().height);
		minButton.setBounds(w - northeastpart.getImage().getBounds().width
				- closeImage.getBounds().width- minImage.getBounds().width, 0,
				minImage.getBounds().width, minImage.getBounds().height);
//		if not set the ratio, by default
		if (Math.abs((this.ratio - 0.0)) < 1e-6) {
			northpart.setBounds(
					northwestpart.getImage().getBounds().width,
					0,
					w - northwestpart.getImage().getBounds().width
							- northeastpart.getImage().getBounds().width
							- closeImage.getBounds().width
							- minImage.getBounds().width, northpart.getImage()
							.getBounds().height);
		}else{
			northpart.setBounds(
					northwestpart.getImage().getBounds().width,
					0,
					w - northwestpart.getImage().getBounds().width
							- northeastpart.getImage().getBounds().width
							- closeImage.getBounds().width
							- minImage.getBounds().width, (int)(h * this.ratio));
		}
		southwestpart.setBounds(0, h
				- southwestpart.getImage().getBounds().height, southwestpart
				.getImage().getBounds().width, southwestpart.getImage()
				.getBounds().height);
		southeastpart.setBounds(w - southeastpart.getImage().getBounds().width,
				h - southeastpart.getImage().getBounds().height, southeastpart
						.getImage().getBounds().width, southeastpart.getImage()
						.getBounds().height);
		southpart.setBounds(southwestpart.getImage().getBounds().width, h
				- southpart.getImage().getBounds().height, w
				- southwestpart.getImage().getBounds().width
				- southwestpart.getImage().getBounds().width, southpart
				.getImage().getBounds().height);
		westpart.setBounds(0, northwestpart.getImage().getBounds().height,
				westpart.getImage().getBounds().width, h
						- northwestpart.getImage().getBounds().height
						- southwestpart.getImage().getBounds().height);
		eastpart.setBounds(w - eastpart.getImage().getBounds().width,
				northeastpart.getImage().getBounds().height, eastpart
						.getImage().getBounds().width, h
						- northeastpart.getImage().getBounds().height
						- southeastpart.getImage().getBounds().height);
//		by default
		if (Math.abs((this.ratio - 0.0)) < 1e-6) {
			contentpart.setBounds(westpart.getImage().getBounds().width,
					northpart.getImage().getBounds().height, w
							- westpart.getImage().getBounds().width
							- eastpart.getImage().getBounds().width, h
							- northpart.getImage().getBounds().height
							- southpart.getImage().getBounds().height);
		} else {
			contentpart.setBounds(westpart.getImage().getBounds().width,
					(int)(h * this.ratio), w
							- westpart.getImage().getBounds().width
							- eastpart.getImage().getBounds().width, h
							- (int)(h * this.ratio)
							- southpart.getImage().getBounds().height);			
		}
		*/
//		System.out.println(contentpart.getBounds().x);
		// region
		Region oldRegion = getRegion();
		if (oldRegion != null && !oldRegion.isDisposed()) {
			oldRegion.dispose();
		}
		Region newRegion = new Region();
		newRegion.add(0, 0, getSize().x, getSize().y);
		newRegion.subtract(getImageTransparenceRegion(northwestpart.getImage(),
				0, 0));
		newRegion.subtract(getImageTransparenceRegion(northeastpart.getImage(),
				getSize().x - northeastpart.getImage().getBounds().width, 0));
		int yl = getSize().y;
		int ys = southwestpart.getImage().getBounds().height;
		newRegion.subtract(getImageTransparenceRegion(southwestpart.getImage(),
				0, (yl - ys)));
		newRegion.subtract(getImageTransparenceRegion(southeastpart.getImage(),
				getSize().x - southeastpart.getImage().getBounds().width,
				getSize().y - southeastpart.getImage().getBounds().height));
		setRegion(newRegion);
	}

	public void paintControl(PaintEvent e) {
		GC gc = e.gc;
		if (e.getSource() == contentpart) {
			//gc.setBackground(color1);
			// gc.setForeground(color2);
			// gc.fillGradientRectangle(0, 0, e.width, e.height, true);
		} else if (e.getSource() == up) {
			// String text = getText();
			// if (text != null) {
			// gc.setForeground(titleColor);
			// gc.drawText(text, e.width / 2 - gc.stringExtent(text).x / 2,
			// e.height / 2 - gc.stringExtent(text).y / 2, true);
			// }
			// Image image = getImage();
			// if (text == null) {
			// text = "";
			// }
			// if (image != null) {
			// gc.drawImage(image, e.width / 2 - gc.stringExtent(text).x / 2
			// - image.getBounds().width - 10, e.height / 2
			// - image.getBounds().height / 2);
			// }
		}
		gc.dispose();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void mouseDoubleClick(MouseEvent e) {
		// if (e.getSource() == northPanel) {
		// setMaximized(!getMaximized());
		// }
	}

	public void mouseDown(MouseEvent e) {
		if (e.getSource() == up) {
			// make the main UI movable
			if (!getMaximized()) {
				location = new Point(e.x, e.y);
			}
		} else if (e.getSource() == southeastpart) {
			// make the main UI resizable
			// size = new Point(e.x, e.y);
		} else if (e.getSource() == closeButton) {
			closeButton.setBackgroundImage(closeDownImage);
		}else if(e.getSource() == minButton){
			minButton.setBackgroundImage(minDownImage);
		}
	}

	public void mouseUp(MouseEvent e) {
		if (e.getSource() == up) {
			location = null;
		} else if (e.getSource() == southeastpart) {
			// if (size == null) {
			// return;
			// }
			// setSize(new Point(getBounds().width + e.x - size.x,
			// getBounds().height + e.y - size.y));
			// size = null;
		} else if (e.getSource() == closeButton) {
			if (e.x > 0 && e.x < closeButton.getSize().x && e.y > 0
					&& e.y < closeButton.getSize().y) {
				closeButton.setBackgroundImage(closeOverImage);
				dispose();
			} else {
				closeButton.setBackgroundImage(closeImage);
			}
		}else if (e.getSource() == minButton) {
			if (e.x > 0 && e.x < minButton.getSize().x && e.y > 0
					&& e.y < minButton.getSize().y) {
				minButton.setBackgroundImage(minOverImage);
				this.setMinimized(true);
			} else {
				minButton.setBackgroundImage(minImage);
			}
		}
	}

	public void mouseMove(MouseEvent e) {
		if (e.getSource() == up) {
			if (location != null) {
				Point p = getDisplay().map(this, null, e.x, e.y);
				setLocation(p.x - location.x, p.y - location.y);
			}
		}
	}

	public void mouseEnter(MouseEvent e) {
		if (e.getSource() == closeButton) {
			closeButton.setBackgroundImage(closeOverImage);
		}else if(e.getSource() == minButton){
			minButton.setBackgroundImage(minOverImage);
		}
	}

	public void mouseExit(MouseEvent e) {
		if (e.getSource() == closeButton) {
			closeButton.setBackgroundImage(closeImage);
		}else if (e.getSource() == minButton) {
			minButton.setBackgroundImage(minImage);
		}
	}

	public void mouseHover(MouseEvent e) {

	}
	@Override
	public void dispose() {
		try {
			if(Utils.getStatus()){//unlock status
				//have to dispose the main first
				MainUI.getMainUI_Instance(Display.getDefault()).dispose();
			}
			
			//kill the process
//			if(DataBaseService.getProc() != null){
//				try {
//					Process process = Runtime.getRuntime().exec("taskList");
//					Scanner in = new Scanner(process.getInputStream());
//					while (in.hasNextLine()) {
//					    String temp = in.nextLine();
//					    if (temp.contains("mysqld.exe")) {
//					        temp = temp.replaceAll(" ", "");
//					        String pid = temp.substring(10, temp.indexOf("Console"));
//					        Runtime.getRuntime().exec("tskill " + pid);
//					    }
//					}
//				} catch (IOException e) {
//					System.out.println("kill the database pid failed");
//				}		
//			}
			
			northwestpart.dispose();
			northeastpart.dispose();			
			southwestpart.dispose();
			southeastpart.dispose();
			southpart.dispose();
			westpart.dispose();
			eastpart.dispose();
			if(up != null)
				up.dispose();
			//check it, and maybe we need to check all
			if(northpart != null)
				northpart.dispose();
			if(contentpart != null)
				contentpart.dispose();
			
			closeImage.dispose();
			closeOverImage.dispose();
			closeDownImage.dispose();
			minImage.dispose();
			minOverImage.dispose();
			minDownImage.dispose();
		} finally {
			super.dispose();
		}
	}

	private Region getImageTransparenceRegion(Image image, int offsetX,
			int offsetY) {
		Region region = new Region();
		final ImageData imageData = image.getImageData();
		if (imageData.alphaData != null) {
			Rectangle pixel = new Rectangle(0, 0, 1, 1);
			for (int y = 0; y < imageData.height; y++) {
				for (int x = 0; x < imageData.width; x++) {
					if (imageData.getAlpha(x, y) != 255) {
						pixel.x = imageData.x + x + offsetX;
						pixel.y = imageData.y + y + offsetY;
						region.add(pixel);
					}
				}
			}
		}
		return region;
	}
}
