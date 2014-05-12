package com.storeworld.analyze;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.storeworld.mainui.ContentPart;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.Utils;
import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;

/**
 * 盘仓界面
 * @author dingyuanxiong
 *
 */
public class AnalyzeContentPart extends ContentPart{
	
	/**
	 * the base composite 
	 */
	private Composite current = null;
	/**
	 * the composite based on "current", add all controls on it
	 */
	private Composite composite = null;
	private Text text_tips;
	/**
	 * record all the analyzed result in this list, easy to remove all
	 */
	private ArrayList<AnalyzerBase> alys = new ArrayList<AnalyzerBase>();
	
	public AnalyzeContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);	
		composite = new Composite(this, SWT.NONE);	
		current = parent;		
		initialization();
		
	}
	
	
	/**
	 * initialize the table elements
	 */
	public void initialization(){
		final int w = current.getBounds().width;
		final int h = current.getBounds().height;
		composite.setBounds(0, 0, w, h);
		
		/**
		 * left side navigate
		 */
		Composite composite_left = new Composite(composite, SWT.NONE);
//		final Color base = new Color(composite.getDisplay(), 255,240,245);
		final Color base = new Color(composite.getDisplay(), 0xed, 0xf4, 0xfa);//??
		composite_left.setBackground(base);
//		composite_left.setBounds(0, 0, (int)(w/5), h);
		composite_left.setBounds(0, 0, 200, h);
		composite_left.setLayout(new FillLayout());
		Composite comp1 = null;
		Composite comp2 = null;

		//expand bar
		ExpandBar expandBar = new ExpandBar(composite_left, SWT.V_SCROLL);  		
		
		final ExpandItem item1 =  new ExpandItem(expandBar, SWT.NONE);
		final ExpandItem item2 =  new ExpandItem(expandBar, SWT.NONE);
		
		expandBar.addExpandListener(new ExpandAdapter() {
			@Override
			public void itemExpanded(ExpandEvent e) {
				if(e.item == item1){
					item2.setExpanded(false);
				}else{
					item1.setExpanded(false);
				}
				
			}
		});
		
	     {  
	    	 //shipment expandbar
	         comp1 = new Composite(expandBar, SWT.NONE);  
	         GridLayout gd = new GridLayout(1, false);
//	         gd.marginWidth=(int)(w/5/10);
	         gd.marginWidth=10;
	         comp1.setLayout(gd);  
	         //used for spacing the controls
//	         Label lbl_space2 = new Label(comp1, SWT.NONE);
//	         lbl_space2.setText("");
//	         lbl_space2.setVisible(false);
	         
	         Label lbl_brand = new Label(comp1, SWT.NONE);
	         lbl_brand.setText("品牌");
	         
	         final CCombo combo_brand = new CCombo(comp1, SWT.BORDER|SWT.READ_ONLY);
	         combo_brand.setVisibleItemCount(5);
	         GridData gd_combo_brand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_brand.widthHint = 151;
	         combo_brand.setLayoutData(gd_combo_brand);
	         
	         final CCombo combo_subbrand = new CCombo(comp1, SWT.BORDER|SWT.READ_ONLY);
	         combo_subbrand.setText("全部子品牌");
	         combo_subbrand.setVisibleItemCount(5);
	         GridData gd_combo_sub_brand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_sub_brand.widthHint = 151;
	         combo_subbrand.setLayoutData(gd_combo_sub_brand);
	         combo_subbrand.setEnabled(false);
	         
	         combo_brand.addListener(SWT.MouseDown, new Listener() {

	 			@Override
	 			public void handleEvent(Event event) {
	 				List<String> list = Utils.getBrands();
	         		combo_brand.setItems(list.toArray(new String[list.size()]));	         		
	 			}
	         });	 
	         combo_subbrand.addListener(SWT.MouseDown, new Listener() {
	 			@Override
	 			public void handleEvent(Event event) {
	 				String brand = combo_brand.getText();
	 				List<String> list = Utils.getSub_Brands(brand);	 						
	 				combo_subbrand.setItems(list.toArray(new String[list.size()]));
	 			}
	         });
	         combo_brand.addSelectionListener(new SelectionAdapter() {
		         	@Override
		         	public void widgetSelected(SelectionEvent e) {
//		         		combo_subbrand.clearSelection();
		         		combo_subbrand.deselectAll();
		         		combo_subbrand.setEnabled(true);
		         		combo_subbrand.setText("全部子品牌");
		         	}
		         });
	         
	         Label lbl_space = new Label(comp1, SWT.NONE);
	         lbl_space.setText("");
	         lbl_space.setVisible(false);
	         
	         Label lbl_area = new Label(comp1, SWT.NONE);
	         lbl_area.setText("片区/客户");
	         
	         final CCombo combo_area = new CCombo(comp1, SWT.BORDER|SWT.READ_ONLY);
	         combo_area.setVisibleItemCount(5);
	         GridData gd_combo_area = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_area.widthHint = 151;
	         combo_area.setLayoutData(gd_combo_area);
	         
	         final CCombo combo_customer = new CCombo(comp1, SWT.BORDER|SWT.READ_ONLY);
	         combo_customer.setText("全部客户");
	         combo_customer.setVisibleItemCount(5);
	         GridData gd_combo_customer = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_customer.widthHint = 151;
	         combo_customer.setLayoutData(gd_combo_customer);
	         combo_customer.setEnabled(false);
	         
	         combo_area.addListener(SWT.MouseDown, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				combo_area.setItems(DataCachePool.getCustomerAreas());		         		     		
		 			}
		         });	 
	         combo_customer.addListener(SWT.MouseDown, new Listener() {
		 			@Override
		 			public void handleEvent(Event event) {
		 				String area = combo_area.getText();
//						System.out.println("area: "+area);
						String[] names = DataCachePool.getCustomerNames(area);
						if(names.length != 0){//no such areas
							combo_customer.setItems(names);
						}
		 			}
		     });
		         
	         combo_area.addSelectionListener(new SelectionAdapter() {
		         	@Override
		         	public void widgetSelected(SelectionEvent e) {
		         		combo_customer.deselectAll();
		         		combo_customer.setEnabled(true);
		         		combo_customer.setText("全部客户");
		         	}
		         });
	         
//	         item1 = new ExpandItem(expandBar, SWT.NONE);  
	         item1.setText("出货量分析");  
	         item1.setExpanded(true);
	         item1.setHeight((int)(h/3));// 设置Item的高度  
	         comp1.setBackground(new Color(composite.getDisplay(), 240,255,255));
	         item1.setControl(comp1);// setControl方法控制comp1的显现  	         	        
	     }  
	     {  
	    	 //the profit expandbar
	         comp2 = new Composite(expandBar, SWT.NONE);  
	         GridLayout gd = new GridLayout(1, false);
//	         gd.marginWidth=(int)(w/5/10);
	         gd.marginWidth=10;
	         comp2.setLayout(gd);  

//	         Label lbl_space2 = new Label(comp2, SWT.NONE);
//	         lbl_space2.setText("");
//	         lbl_space2.setVisible(false);
	         
	         Label lbl_brand = new Label(comp2, SWT.NONE);
	         lbl_brand.setText("品牌");
	         
	         final CCombo combo_brand = new CCombo(comp2, SWT.BORDER);
	         GridData gd_combo_brand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_brand.widthHint = 151;
	         combo_brand.setLayoutData(gd_combo_brand);
	         
	         final CCombo combo_subbrand = new CCombo(comp2, SWT.BORDER);
	         combo_subbrand.setText("全部子品牌");
	         GridData gd_combo_sub_brand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_sub_brand.widthHint = 151;
	         combo_subbrand.setLayoutData(gd_combo_sub_brand);
	         
	         combo_brand.addListener(SWT.MouseDown, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				List<String> list = Utils.getBrands();
		         		combo_brand.setItems(list.toArray(new String[list.size()]));	         		
		 			}
		         });	 
		         combo_subbrand.addListener(SWT.MouseDown, new Listener() {
		 			@Override
		 			public void handleEvent(Event event) {
		 				String brand = combo_brand.getText();
		 				List<String> list = Utils.getSub_Brands(brand);	 						
		 				combo_subbrand.setItems(list.toArray(new String[list.size()]));
		 			}
		         });
		         combo_brand.addSelectionListener(new SelectionAdapter() {
			         	@Override
			         	public void widgetSelected(SelectionEvent e) {
//			         		combo_subbrand.clearSelection();
			         		combo_subbrand.deselectAll();
			         		combo_subbrand.setEnabled(true);
			         		combo_subbrand.setText("全部子品牌");
			         	}
			         });
		         
	         Label lbl_space = new Label(comp2, SWT.NONE);
	         lbl_space.setText("");
	         lbl_space.setVisible(false);
	         
	         Label lbl_area = new Label(comp2, SWT.NONE);
	         lbl_area.setText("片区/客户");
	         
	         final CCombo combo_area = new CCombo(comp2, SWT.BORDER);
	         GridData gd_combo_area = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_area.widthHint = 151;
	         combo_area.setLayoutData(gd_combo_area);
	         
	         final CCombo combo_customer = new CCombo(comp2, SWT.BORDER);
	         combo_customer.setText("全部客户");
	         GridData gd_combo_customer = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_customer.widthHint = 151;
	         combo_customer.setLayoutData(gd_combo_customer);
	         combo_area.addListener(SWT.MouseDown, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				combo_area.setItems(DataCachePool.getCustomerAreas());		         		     		
		 			}
		         });	 
	         combo_customer.addListener(SWT.MouseDown, new Listener() {
		 			@Override
		 			public void handleEvent(Event event) {
		 				String area = combo_area.getText();
//						System.out.println("area: "+area);
						String[] names = DataCachePool.getCustomerNames(area);
						if(names.length != 0){//no such areas
							combo_customer.setItems(names);
						}
		 			}
		     });
		         
	         combo_area.addSelectionListener(new SelectionAdapter() {
		         	@Override
		         	public void widgetSelected(SelectionEvent e) {
		         		combo_customer.deselectAll();
		         		combo_customer.setEnabled(true);
		         		combo_customer.setText("全部客户");
		         	}
		         }); 
  	        
//	         item2 = new ExpandItem(expandBar, SWT.NONE);  
	            
	         item2.setText("利润分析");  	         
	         item2.setHeight((int)(h/3));// 设置Item的高度  
	         comp2.setBackground(new Color(composite.getDisplay(), 240,255,255));
	         item2.setControl(comp2);// setControl方法控制comp1的显现  
	     }  
	     expandBar.setBackground(new Color(composite.getDisplay(), 204,255,204));
	     composite_left.layout();
 
	        
		

	     /**
	      * right part to show the analyzed result	
	      */
	    //right part base compoiste
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
//		composite_right.setBounds((int)(w/5), 0, (int)(4*w/5), h);
		composite_right.setBounds(200, 0, 760, h);
		
		//text area to show the tips
		StyledText styledText = new StyledText(composite_right, SWT.BORDER);
		styledText.setBounds((int)(4*w/5/50), (int)(4*w/5/50), (int)(4*w/5/4), (int)(h/10));
		styledText.setText("五得利最近一个月出货量\n"+"总计:15000包");
		StyleRange styleRange  =   new  StyleRange();
		styleRange.start  =  0;
		styleRange.length  =  "五得利最近一个月出货量".length();
		styleRange.fontStyle  =  SWT.BOLD;
		styledText.setStyleRange(styleRange);
		
		/**
		 * the group buttons, showing the for kind of classification
		 */
		Composite composite_group = new Composite(composite_right, SWT.NONE);
		composite_group.setBounds((int)(2*4*w/5/3), (int)(2*4*w/5/50/3), (int)(4*w/5/3-h/30), (int)(h/18));
		GridLayout layout = new GridLayout(4, true);  
		//put the four button
        layout.numColumns = 4;  
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite_group.setLayout(layout);
        
        GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.heightHint = (int)(h/18);
        Button btn_month = new Button(composite_group, SWT.NONE);       
        btn_month.setText("一个月");
        btn_month.setLayoutData(gd_text);

		
        GridData gd_text2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_text2.heightHint = (int)(h/18);
        Button btn_season = new Button(composite_group, SWT.NONE);
        btn_season.setText("一个季度");
        btn_season.setLayoutData(gd_text2);
        
        GridData gd_text3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_text3.heightHint = (int)(h/18);
        Button btn_year = new Button(composite_group, SWT.NONE);
        btn_year.setText("一年");
        btn_year.setLayoutData(gd_text3);

        
        GridData gd_text4 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_text4.heightHint = (int)(h/18);
        Button btn_all = new Button(composite_group, SWT.NONE);
        btn_all.setText("全部记录");
        btn_all.setLayoutData(gd_text4);        
        composite_group.layout();

        /**
         * the scroll composite to show the analyzed table result and graph
         */
        final Composite composite_main = new Composite(composite_right, SWT.NONE);
        composite_main.setBounds(0, (int)(h/8+h/100), (int)(4*w/5), (int)(7*h/8-h/100));
        
        composite_main.setBackground(new Color(composite.getDisplay(), 255,240,245));
        composite_main.setLayout(new FillLayout());
        final ScrolledComposite composite_scroll = new ScrolledComposite(composite_main,  SWT.NONE|SWT.V_SCROLL);//
//		composite_scroll.setVisible(true);
		composite_scroll.setExpandHorizontal(true);  
		composite_scroll.setExpandVertical(true);  
		composite_scroll.addListener(SWT.Activate, new Listener(){    
			public void handleEvent(Event e){
				//need to forceFocus
				composite_scroll.forceFocus();
				}
		}); 
		final Composite composite_content = new Composite(composite_scroll, SWT.NONE);
		composite_scroll.setContent(composite_content);
		composite_content.setBackground(new Color(composite.getDisplay(), 255,240,245));
		GridLayout layout_content = new GridLayout(1, false);  
		layout_content.numColumns = 1;  
		layout_content.horizontalSpacing = 0;
		layout_content.verticalSpacing = 0;
		layout_content.marginHeight = 20;//not recommended
		layout_content.marginWidth = 20;
        composite_content.setLayout(layout_content);
        composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        /**
         * add the button listener for the classification
         */
//        btn_month.addSelectionListener(new SelectionAdapter() {
//        	@Override
//        	public void widgetSelected(SelectionEvent e) {
//        		RatioComposite bc = new RatioComposite(composite_content, 0, new RatioBlock());    
//        		alys.add(bc);
//                composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
//                composite_content.layout(); 
//        	}
//        });
        
//        btn_season.addSelectionListener(new SelectionAdapter() {
//        	@Override
//        	public void widgetSelected(SelectionEvent e) {
//        		TrendComposite tc = new TrendComposite(composite_content, 0, new RatioBlock()); 
//        		alys.add(tc);
//                composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
//                composite_content.layout(); 
//        	}
//        });
//        btn_year.addSelectionListener(new SelectionAdapter() {
//        	@Override
//        	public void widgetSelected(SelectionEvent e) {
//        		for(int i=0;i<alys.size();i++){
//        			alys.get(i).remove();
//        		}
//				composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));  
//				composite_content.layout();  
//        	}
//        });
        composite_main.layout();
        composite_content.layout();
//        composite_scroll.layout();
        
        DataCachePool.cacheProductInfo();
        DataCachePool.cacheCustomerInfo();

	}
}
