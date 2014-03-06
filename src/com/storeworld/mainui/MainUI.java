package com.storeworld.mainui;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.storeworld.utils.Constants;

public class MainUI extends Shell implements ControlListener, PaintListener,
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
	
	// icon
	private Image closeImage;
	private Image closeOverImage;
	private Image closeDownImage;
	private Image minImage;
	private Image minOverImage;
	private Image minDownImage;

	// color
//	private Color color1 = new Color(getDisplay(), 255, 255, 254);//254
	// private Color color2 = new Color(getDisplay(), 204, 220, 247);
	// private Color titleColor = new Color(getDisplay(), 128, 128, 128);
	//
	private Point location;

	// private Point size;

	public MainUI(Display display) {
		super(display, SWT.NO_TRIM);
	}

	public void setup(NorthWestPart northwestpart,
			NorthEastPart northeastpart, NorthPart northpart,
			SouthWestPart southwestpart, SouthEastPart southeastpart,
			SouthPart southpart, WestPart westpart, EastPart eastpart,
			ContentPart contentpart, HashMap<String, String> close_min){
		initial(northwestpart, northeastpart, northpart, southwestpart,
				southeastpart, southpart, westpart, eastpart, contentpart);
		initialClose_Min(close_min);
		createContents();
		addControlListener(this);
	}

	public void initialClose_Min(HashMap<String, String> close_min){
		closeButton = new Composite(this, SWT.NONE);
		minButton = new Composite(this, SWT.NONE);
		if(close_min == null){//default
		// close button and min button
		closeImage = new Image(getDisplay(), "icon/close.png");
		closeOverImage = new Image(getDisplay(), "icon/closeover.png");
		closeDownImage = new Image(getDisplay(), "icon/closedown.png");
		minImage = new Image(getDisplay(), "icon/min.png");
		minOverImage = new Image(getDisplay(), "icon/minover.png");
		minDownImage = new Image(getDisplay(), "icon/mindown.png");
		}else{
			try {
				closeImage = new Image(getDisplay(), close_min.get(Constants.CLOSE_IMAGE));
				closeOverImage = new Image(getDisplay(), close_min.get(Constants.CLOSE_OVER_IMAGE));
				closeDownImage = new Image(getDisplay(), close_min.get(Constants.CLOSE_DOWN_IMAGE));
				minImage = new Image(getDisplay(), close_min.get(Constants.MIN_IMAGE));
				minOverImage = new Image(getDisplay(), close_min.get(Constants.MIN_OVER_IMAGE));
				minDownImage = new Image(getDisplay(), close_min.get(Constants.MIN_DOWN_IMAGE));
			} catch (Exception e) {
				System.out.println("hashmap key is null");
				e.printStackTrace();
			}
		}
		closeButton.setBackgroundImage(closeImage);
		minButton.setBackgroundImage(minImage);
	}
	public void initial(NorthWestPart northwestpart,
			NorthEastPart northeastpart, NorthPart northpart,
			SouthWestPart southwestpart, SouthEastPart southeastpart,
			SouthPart southpart, WestPart westpart, EastPart eastpart,
			ContentPart contentpart) {
		if (northwestpart != null) {
			this.northwestpart = northwestpart;
		}
		if (northeastpart != null) {
			this.northeastpart = northeastpart;
		}
		if (northpart != null) {
			this.northpart = northpart;
		}
		if (southwestpart != null) {
			this.southwestpart = southwestpart;
		}
		if (southeastpart != null) {
			this.southeastpart = southeastpart;
		}
		if (southpart != null) {
			this.southpart = southpart;
		}
		if (westpart != null) {
			this.westpart = westpart;
		}
		if (eastpart != null) {
			this.eastpart = eastpart;
		}
		if (contentpart != null) {
			this.contentpart = contentpart;
		}
	}

	/**
	 * Create contents of the window
	 */
	private void createContents() {
		// cut the whole shell into nine piece
		if (northwestpart == null)
			northwestpart = new NorthWestPart(this, SWT.NONE, null);
		if (northeastpart == null)
			northeastpart = new NorthEastPart(this, SWT.NONE, null);
		if (northpart == null)
			northpart = new NorthPart(this, SWT.NONE, null);
		if (southwestpart == null)
			southwestpart = new SouthWestPart(this, SWT.NONE, null);
		if (southeastpart == null)
			southeastpart = new SouthEastPart(this, SWT.NONE, null);
		if (southpart == null)
			southpart = new SouthPart(this, SWT.NONE, null);
		if (westpart == null)
			westpart = new WestPart(this, SWT.NONE, null);
		if (eastpart == null)
			eastpart = new EastPart(this, SWT.NONE, null);
		//by default, set the color into dark_shadow
		if (contentpart == null)
			contentpart = new ContentPart(this, SWT.NONE, null);

		// add listener as you want here for each part
		contentpart.addPaintListener(this);
		northpart.addMouseListener(this);
		northpart.addMouseMoveListener(this);
		northpart.addPaintListener(this);
		southeastpart.addMouseListener(this);// resize, disable now
		southeastpart.addMouseMoveListener(this);// resize, disable now

		
		closeButton.addMouseListener(this);
		closeButton.addMouseTrackListener(this);
		minButton.addMouseListener(this);
		minButton.addMouseTrackListener(this);

	}

	public void controlMoved(ControlEvent e) {

	}

	public void controlResized(ControlEvent e) {
		int w = getSize().x;
		int h = getSize().y;
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
		northpart.setBounds(northwestpart.getImage().getBounds().width, 0,
				w - northwestpart.getImage().getBounds().width
						- northeastpart.getImage().getBounds().width
						- closeImage.getBounds().width- minImage.getBounds().width, northpart.getImage()
						.getBounds().height);
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
		contentpart.setBounds(westpart.getImage().getBounds().width, northpart
				.getImage().getBounds().height, w
				- westpart.getImage().getBounds().width
				- eastpart.getImage().getBounds().width, h
				- northpart.getImage().getBounds().height
				- southpart.getImage().getBounds().height);
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
//			gc.setBackground(color1);
			// gc.setForeground(color2);
			// gc.fillGradientRectangle(0, 0, e.width, e.height, true);
		} else if (e.getSource() == northpart) {
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
		if (e.getSource() == northpart) {
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
		if (e.getSource() == northpart) {
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
		if (e.getSource() == northpart) {
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
			northwestpart.dispose();
			northeastpart.dispose();
			northpart.dispose();
			southwestpart.dispose();
			southeastpart.dispose();
			southpart.dispose();
			westpart.dispose();
			eastpart.dispose();
			contentpart.dispose();
			
			closeImage.dispose();
			closeOverImage.dispose();
			closeDownImage.dispose();
			minImage.dispose();
			minOverImage.dispose();
			minDownImage.dispose();
			//color1.dispose();
			// color2.dispose();
			// titleColor.dispose();
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
