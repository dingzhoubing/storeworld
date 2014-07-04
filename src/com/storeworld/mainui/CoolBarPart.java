package com.storeworld.mainui;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import com.storeworld.analyze.AnalyzeContentPart;
import com.storeworld.customer.CustomerContentPart;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.login.Login;
import com.storeworld.product.ProductContentPart;
import com.storeworld.stock.StockContentPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.FUNCTION;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

/**
 * the self made CoolBar part on NorthPart
 * @author dingyuanxiong
 *
 */
public class CoolBarPart extends NorthPart{

	private Composite current = null;
//	private Button button_last;
	
	public CoolBarPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, new Image(parent.getDisplay(), "icon/north.png"));//style
		current = parent;
		initialization();
	}
	

	public void initialization(){
		int w = current.getBounds().width;
		int h = current.getBounds().height;
		
		final Button button_index = new Button(this, SWT.NONE);
//		button_index.setBounds((int)(w*0.03), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_index.setBounds(12, 20, 64, 64);
		Label lbl_index = new Label(this, SWT.CENTER);
		lbl_index.setText("Ê×Ò³");
		lbl_index.setBounds(12, 92, 64, 15);
		lbl_index.setBackground(new Color(getDisplay(),21, 23,26));
		lbl_index.setForeground(new Color(getDisplay(),255, 255,255));
		//9 == 14px
		lbl_index.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 9, SWT.NORMAL));
		button_index.setText("Ê×Ò³");			
		Utils.getFunc_Button().put(FUNCTION.NONE, button_index);

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
//		button_in.setBounds((int)(2*w*0.03+w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_in.setBounds(96, 20, 64, 64);
		
		Label lbl_in = new Label(this, SWT.CENTER);
		lbl_in.setText("½ø»õ");
		lbl_in.setBounds(96, 92, 64, 15);
		lbl_in.setBackground(new Color(getDisplay(),21, 23,26));
		lbl_in.setForeground(new Color(getDisplay(),255, 255,255));
		//9 == 14px
		lbl_in.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 9, SWT.NORMAL));
		
		button_in.setText("½ø»õ");
		Utils.getFunc_Button().put(FUNCTION.STOCK, button_in);

		button_in.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.STOCK);

				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());

				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new CoolBarPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_STOCK) == null)
					shell.setContentPart(new StockContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_STOCK);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.STOCK);
				shell.show_Content_stock();
				if(Utils.getUseSoftKeyBoard()){
					StockContentPart.getButtonSWKB().setSelection(true);
				}
			}
		});
		
		
		final Button button_out = new Button(this, SWT.NONE);
//		button_out.setBounds((int)(3*w*0.03+2*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_out.setBounds(180, 20, 64, 64);
		Label lbl_out = new Label(this, SWT.CENTER);
		lbl_out.setText("ËÍ»õ");
		lbl_out.setBounds(180, 92, 64, 15);
		lbl_out.setBackground(new Color(getDisplay(),21, 23,26));
		lbl_out.setForeground(new Color(getDisplay(),255, 255,255));
		lbl_out.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 9, SWT.NORMAL));
		
		button_out.setText("ËÍ»õ");
		Utils.getFunc_Button().put(FUNCTION.DELIVER, button_out);

		button_out.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.DELIVER);
				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)
					shell.setNorthPart(new CoolBarPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_DELIVER) == null)
					shell.setContentPart(new DeliverContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_DELIVER);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.DELIVER);
				shell.show_Content_deliver();
				if(Utils.getUseSoftKeyBoard()){
					DeliverContentPart.getButtonSWKB().setSelection(true);
				}
			}
		});
		
		final Button button_aly = new Button(this, SWT.NONE);
//		button_aly.setBounds((int)(4*w*0.03+3*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_aly.setBounds(264, 20, 64, 64);
		button_aly.setText("ÅÌ²Ö");
		
		Label lbl_aly = new Label(this, SWT.CENTER);
		lbl_aly.setText("ÅÌ²Ö");
		lbl_aly.setBounds(264, 92, 64, 15);
		lbl_aly.setBackground(new Color(getDisplay(),21, 23,26));
		lbl_aly.setForeground(new Color(getDisplay(),255, 255,255));
		lbl_aly.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 9, SWT.NORMAL));
		
		Utils.getFunc_Button().put(FUNCTION.ANALYZE, button_aly);

		button_aly.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.ANALYZE);
				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new CoolBarPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_ANALYZE) == null)
					shell.setContentPart(new AnalyzeContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_ANALYZE);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.ANALYZE);
				shell.show_Content_analyze();
			}
		});
		
		Button button_lock = new Button(this, SWT.NONE);
//		button_lock.setBounds((int)(5*w*0.03+4*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_lock.setBounds(348, 20, 64, 64);
		button_lock.setText("Ëø¶¨");
		Label lbl_lock = new Label(this, SWT.CENTER);
		lbl_lock.setText("Ëø¶¨");
		lbl_lock.setBounds(348, 92, 64, 15);
		lbl_lock.setBackground(new Color(getDisplay(),21, 23,26));
		lbl_lock.setForeground(new Color(getDisplay(),255, 255,255));
		lbl_lock.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 9, SWT.NORMAL));
		
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
//		button_prod.setBounds((int)(11*w*0.03+9*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_prod.setBounds(800, 20, 64, 64);
		button_prod.setText("»õÆ·");
		Label lbl_prod = new Label(this, SWT.CENTER);
		lbl_prod.setText("»õÆ·");
		lbl_prod.setBounds(800, 92, 64, 15);
		lbl_prod.setBackground(new Color(getDisplay(),21, 23,26));
		lbl_prod.setForeground(new Color(getDisplay(),255, 255,255));
		lbl_prod.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 9, SWT.NORMAL));
				
		Utils.getFunc_Button().put(FUNCTION.PRODUCT, button_prod);

		button_prod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.PRODUCT);
				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new CoolBarPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_PRODUCT) == null)
					shell.setContentPart(new ProductContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_PRODUCT);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.PRODUCT);
				shell.show_Content_product();
				
				if(Utils.getUseSoftKeyBoard()){
					ProductContentPart.getButtonSWKB().setSelection(true);
				}
			}
		});	
		
		
		final Button button_cus = new Button(this, SWT.NONE);
//		button_cus.setBounds((int)(12*w*0.03+10*w/18), (int)(h*0.15), (int)(w/18),(int)(w/18));
		button_cus.setBounds(884, 20, 64, 64);
		button_cus.setText("¿Í»§");
		Label lbl_cus = new Label(this, SWT.CENTER);
		lbl_cus.setText("¿Í»§");
		lbl_cus.setBounds(884, 92, 64, 15);
		lbl_cus.setBackground(new Color(getDisplay(),21, 23,26));
		lbl_cus.setForeground(new Color(getDisplay(),255, 255,255));
		lbl_cus.setFont(SWTResourceManager.getFont("Î¢ÈíÑÅºÚ", 9, SWT.NORMAL));
		
		Utils.getFunc_Button().put(FUNCTION.CUSTOMER, button_cus);

		button_cus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.setFunctin(FUNCTION.CUSTOMER);
				
				MainUI shell = MainUI.getMainUI_Instance(Display.getDefault());
				if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)				
					shell.setNorthPart(new CoolBarPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
				if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_CUSTOMER) == null)
					shell.setContentPart(new CustomerContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_CUSTOMER);
				shell.show_North_index();
				Utils.setFunctinLast(FUNCTION.CUSTOMER);
				shell.show_Content_customer();
				
				if(Utils.getUseSoftKeyBoard()){
					CustomerContentPart.getButtonSWKB().setSelection(true);
				}
			}
		});
		
		
	}
	
	
}
