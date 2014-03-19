package com.storeworld.stock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.storeworld.analyze.AnalyzeContentPart;
import com.storeworld.customer.CustomerContentPart;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.login.Login;
import com.storeworld.mainui.MainUI;
import com.storeworld.mainui.NorthPart;
import com.storeworld.product.ProductContentPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.FUNCTION;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

public class StockPart extends NorthPart{

	private Composite current = null;
//	private Button button_last;
	
	public StockPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, new Image(parent.getDisplay(), "icon/north.png"));
		current = parent;
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;
		final Button button_index = new Button(this, SWT.NONE);
		button_index.setBounds((int)(w*0.03), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_index.setText("首頁");
		Utils.getFunc_Button().put(FUNCTION.NONE, button_index);
//		Label label_index = new Label(this, SWT.NONE);
//		label_index.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
//		label_index.setBackground(new Color(getDisplay(), 127,127,127));
//		label_index.setAlignment(SWT.CENTER);
//		label_index.setBounds(30, 70, 45, 17);
//		label_index.setText("\u9996\u9875");
		button_index.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.NONE);
								
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				//we must have entered the main ui page
				shell.show_North_bottom();
				shell.show_Content_main();
			}
		});
		
		final Button button_in = new Button(this, SWT.NONE);
		button_in.setBounds((int)(2*w*0.03+w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_in.setText("进货");
		Utils.getFunc_Button().put(FUNCTION.STOCK, button_in);
//		Label label_in = new Label(this, SWT.NONE);
//		label_in.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
//		label_in.setBackground(new Color(getDisplay(), 127,127,127));
//		label_in.setAlignment(SWT.CENTER);
//		label_in.setBounds(100, 70, 45, 17);
//		label_in.setText("\u8FDB\u8D27");
		button_in.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.STOCK);

				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
//				System.out.println("shell is null: "+ (shell==null));
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new StockPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_STOCK) == null)
					shell.setContentPart(new StockContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_STOCK);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.STOCK);
				shell.show_Content_stock();
			}
		});
		
		
		final Button button_out = new Button(this, SWT.NONE);
		button_out.setBounds((int)(3*w*0.03+2*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_out.setText("送货");
		Utils.getFunc_Button().put(FUNCTION.DELIVER, button_out);
//		Label label_out = new Label(this, SWT.NONE);
//		label_out.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
//		label_out.setBackground(new Color(getDisplay(), 127,127,127));
//		label_out.setAlignment(SWT.CENTER);
//		label_out.setBounds(170, 70, 45, 17);
//		label_out.setText("\u9001\u8D27");
		button_out.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.DELIVER);
				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)
					shell.setNorthPart(new StockPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_DELIVER) == null)
					shell.setContentPart(new DeliverContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_DELIVER);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.DELIVER);
				shell.show_Content_deliver();
			}
		});
		
		final Button button_aly = new Button(this, SWT.NONE);
		button_aly.setBounds((int)(4*w*0.03+3*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_aly.setText("盘仓");
		Utils.getFunc_Button().put(FUNCTION.ANALYZE, button_aly);
//		Label label_aly = new Label(this, SWT.NONE);
//		label_aly.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
//		label_aly.setBackground(new Color(getDisplay(), 127,127,127));
//		label_aly.setAlignment(SWT.CENTER);
//		label_aly.setBounds(240, 70, 45, 17);
//		label_aly.setText("\u76D8\u4ED3");
		button_aly.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.ANALYZE);
				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new StockPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_ANALYZE) == null)
					shell.setContentPart(new AnalyzeContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_ANALYZE);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.ANALYZE);
				shell.show_Content_analyze();
			}
		});
		
		Button button_lock = new Button(this, SWT.NONE);
		button_lock.setBounds((int)(5*w*0.03+4*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_lock.setText("保密");
//		Label label_lock = new Label(this, SWT.NONE);
//		label_lock.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
//		label_lock.setBackground(new Color(getDisplay(), 127,127,127));
//		label_lock.setAlignment(SWT.CENTER);
//		label_lock.setBounds(310, 70, 45, 17);
//		label_lock.setText("\u4FDD\u5BC6");
		button_lock.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.changeStatus();//be true, make it unlock				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				shell.setVisible(false);
				Login.login();
			}
		});
		
		final Button button_prod = new Button(this, SWT.NONE);
		button_prod.setBounds((int)(11*w*0.03+9*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_prod.setText("货品");
		Utils.getFunc_Button().put(FUNCTION.PRODUCT, button_prod);
//		Label label_prod = new Label(this, SWT.NONE);
//		label_prod.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
//		label_prod.setBackground(new Color(getDisplay(), 127,127,127));
//		label_prod.setAlignment(SWT.CENTER);
//		label_prod.setBounds(930, 70, 45, 17);
//		label_prod.setText("\u8D27\u54C1");
		button_prod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.PRODUCT);
				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new StockPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_PRODUCT) == null)
					shell.setContentPart(new ProductContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_PRODUCT);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.PRODUCT);
				shell.show_Content_product();
			}
		});	
		
		
		final Button button_cus = new Button(this, SWT.NONE);
		button_cus.setBounds((int)(12*w*0.03+10*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_cus.setText("客户");
		Utils.getFunc_Button().put(FUNCTION.CUSTOMER, button_cus);
//		Label label_cus = new Label(this, SWT.NONE);
//		label_cus.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
//		label_cus.setBackground(new Color(getDisplay(), 127,127,127));
//		label_cus.setAlignment(SWT.CENTER);
//		label_cus.setBounds(1000, 70, 45, 17);
//		label_cus.setText("\u5BA2\u6237");
		button_cus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.CUSTOMER);
				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new StockPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_CUSTOMER) == null)
					shell.setContentPart(new CustomerContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_CUSTOMER);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.CUSTOMER);
				shell.show_Content_customer();
			}
		});
		
		
//		this.setCursor(new Cursor(display, SWT.CURSOR_SIZEALL));
//		this.setBackgroundColor(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
//		this.setBackgroundColor(new Color(getDisplay(),63,63,125));				
	}
	
	
}
