package com.storeworld.analyze;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;
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

import com.storeworld.analyze.AnalyzerUtils.KIND;
import com.storeworld.analyze.AnalyzerUtils.TYPE;
import com.storeworld.analyze.ratioutils.RatioBlock;
import com.storeworld.analyze.ratioutils.RatioResultList;
import com.storeworld.analyze.trendutils.TrendDataSet;
import com.storeworld.mainui.ContentPart;
import com.storeworld.pojo.dto.AnalysticDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ResultSetDTO;
import com.storeworld.pub.service.Statistic;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.Utils;

/**
 * 盘仓界面
 * @author dingyuanxiong
 *
 */
public class AnalyzeContentPart extends ContentPart{
	
	private static Statistic statistic = new Statistic();
	/**
	 * the base composite 
	 */
	private Composite current = null;
	/**
	 * the composite based on "current", add all controls on it
	 */
	private Composite composite = null;
	private Text text_tips;
	
	private static final String RECENT_MONTH = "最近一个月";
	private static final String RECENT_SEASON = "最近一个季度";
	private static final String RECENT_YEAR = "最近一个年";
	private static final String RECENT_ALL = "所有记录";
	
	

	
	private static ExpandBar expandBar = null;
	private static ExpandItem item1 = null;
	private static ExpandItem item2 = null;
	private static CCombo combo_brand_shipment = null;
	private static CCombo combo_sub_shipment = null;
	private static CCombo combo_area_shipment = null;
	private static CCombo combo_cus_shipment = null;
	private static CCombo combo_brand_profit = null;
	private static CCombo combo_sub_profit = null;
	private static CCombo combo_area_profit = null;
	private static CCombo combo_cus_profit = null;
	private static StyledText styledText = null;
	
	
	private static boolean shipment_profit = true;
	private static boolean has_args = false;
	
	//four combo box value as args
	private static String brand = "";
	private static String sub = "";
	private static String area = "";
	private static String customer = "";
	

	
	private static String prefix = "";//the description prefix 
	/**
	 * record all the analyzed result in this list, easy to remove all
	 */
	private ArrayList<AnalyzerBase> alys = new ArrayList<AnalyzerBase>();
	
	
	private static ScrolledComposite composite_scroll = null;
	private static Composite composite_content = null;
	private static GridLayout layout_content = null;
	
	public AnalyzeContentPart(Composite parent, int style, Image image, Color color) {
		super(parent, style, image);	
		composite = new Composite(this, SWT.NONE);	
		current = parent;		
		initialization();
		
	}
	
	/**
	 * initial the title of the analyzation
	 * @param type enum type
	 */
	private void initialTitle(TYPE type){
		has_args = false;
		String tail = "";
		String period = ""; 
		prefix="";
		StringBuilder sb = new StringBuilder();
		switch(type){
		case MONTH:
			period = RECENT_MONTH;
			break;
		case SEASON:
			period = RECENT_SEASON;
			break;
		case YEAR:
			period = RECENT_YEAR;
			break;
		case ALL:
			period = RECENT_ALL;
			break;
		}
		
		
		if(item1.getExpanded()){
			has_args = true;
			shipment_profit = true;//shipment
			tail = "的出货量";
			if(combo_brand_shipment.getText().equals(AnalyzerConstants.ALL_BRAND)){
				brand = AnalyzerConstants.ALL_BRAND;
				sub = "";
			}else{
				brand = combo_brand_shipment.getText();
				sub = combo_sub_shipment.getText();
			}
			if(combo_area_shipment.getText().equals(AnalyzerConstants.ALL_AREA)){
				area = AnalyzerConstants.ALL_AREA;
				customer = "";
			}else{
				area = combo_area_shipment.getText();
				customer = combo_cus_shipment.getText();
			}
			
			sb.append(area);
			sb.append(customer);
			sb.append(period);
			sb.append(brand);
			sb.append(sub);
			sb.append(tail+"\n");
			
			prefix+=(sb.toString());
		}
		if(item2.getExpanded()){
			has_args = true;
			shipment_profit = false;
			tail = "的利润";
			if(combo_brand_profit.getText().equals(AnalyzerConstants.ALL_BRAND)){
				brand = AnalyzerConstants.ALL_BRAND;
				sub = "";
			}else{
				brand = combo_brand_profit.getText();
				sub = combo_sub_profit.getText();
			}
			if(combo_area_profit.getText().equals(AnalyzerConstants.ALL_AREA)){
				area = AnalyzerConstants.ALL_AREA;
				customer = "";
			}else{
				area = combo_area_profit.getText();
				customer = combo_cus_profit.getText();
			}
			
			sb.append(area);
			sb.append(customer);
			sb.append(period);
			sb.append(brand);
			sb.append(sub);
			sb.append(tail+"\n");
			prefix+=(sb.toString());
		}
		
		styledText.setText(prefix);
		StyleRange styleRange  =   new  StyleRange();
		styleRange.start  =  0;
		styleRange.length  =  prefix.length();
		styleRange.fontStyle  =  SWT.BOLD;
		styledText.setStyleRange(styleRange);
	}
	
	/**
	 * show the analyze result, different options give different number & different kinds of results
	 * @param type
	 */
	private void showResult(TYPE type){
		String brand = "";//brand
		String sub = "";//sub brand
		String area = "";//area
		String cus = "";//customer name
		String time = "";//time in second level
		KIND kind = null;//profit or shipment 
		
		if(item1.getExpanded()){
			kind = KIND.SHIPMENT;
			brand = combo_brand_shipment.getText();
			sub = combo_sub_shipment.getText();
			area = combo_area_shipment.getText();
			cus = combo_cus_shipment.getText();
		}else if(item2.getExpanded()){
			kind = KIND.PROFIT;
			brand = combo_brand_profit.getText();
			sub = combo_sub_profit.getText();
			area = combo_area_profit.getText();
			cus = combo_cus_profit.getText();
		}else{//either item1 & item2 are not expanded
			//show a message?
			kind = KIND.NONE;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		time = formatter.format(new Date());
		Map<String, Object> args = new HashMap<String ,Object>();
		args.put("brand", brand);
		args.put("sub_brand", sub);
		args.put("area", area);
		args.put("customer", cus);
		args.put("time", time);
		args.put("kind", kind.toString());
		args.put("type", type.toString());
		//call engine to get the data
		//??
		ResultSetDTO ro=null;
		try {
			ro=statistic.startAnalyzing(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ro == null)
			return;
		
		Pagination page1 = (Pagination) ro.get("table1");
		Pagination page2 = (Pagination) ro.get("table2");
		Pagination page3 = (Pagination) ro.get("table3");
		List<Object> res1 = (List<Object>)page1.getItems();
		List<Object> res2 = (List<Object>)page2.getItems();
		List<Object> res3 = (List<Object>)page3.getItems();
		
		
		//this if/else can be optimized, since it looks better in this way, leave it		
		//case 1: brand ratio, area ratio, trend
		if(brand.equals(AnalyzerConstants.ALL_BRAND) && area.equals(AnalyzerConstants.ALL_AREA)){
						
			//brand ratio
			RatioBlock rb = new RatioBlock();
			rb.setBrand_sub(true);//all brands
			rb.setKind(kind);
			rb.setBrand_area(true);//brand
			RatioResultList rrl = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl.initilByQuery(res1, 1);//the input arg is the query result from Engine			
			RatioComposite bc = new RatioComposite(composite_content, 0, rb, rrl);    
    		alys.add(bc);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    		
    		//area ratio
    		RatioBlock rb2 = new RatioBlock();
    		rb2.setArea_customer(true);//all areas
    		rb2.setKind(kind);
    		rb2.setBrand_area(false);//area
			RatioResultList rrl2 = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl2.initilByQuery(res2, 2);//the input arg is the query result from Engine			
			RatioComposite bc2 = new RatioComposite(composite_content, 0, rb2, rrl2);    
    		alys.add(bc2);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		}
		//case 2: brand ratio, customer ratio, trend
		else if(brand.equals(AnalyzerConstants.ALL_BRAND) && cus.equals(AnalyzerConstants.ALL_CUSTOMER)){
			//brand ratio
			RatioBlock rb = new RatioBlock();
			rb.setBrand_sub(true);//all brands
			rb.setKind(kind);
			rb.setBrand_area(true);//brand
			RatioResultList rrl = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl.initilByQuery(res1, 1);//the input arg is the query result from Engine			
			RatioComposite bc = new RatioComposite(composite_content, 0, rb, rrl);    
    		alys.add(bc);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            
    		//customer ratio
    		RatioBlock rb2 = new RatioBlock();
    		rb2.setArea_customer(false);//all areas
    		rb2.setArea(area);
    		rb2.setKind(kind);
    		rb2.setBrand_area(false);//area
			RatioResultList rrl2 = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl2.initilByQuery(res2, 2);//the input arg is the query result from Engine			
			RatioComposite bc2 = new RatioComposite(composite_content, 0, rb2, rrl2);    
    		alys.add(bc2);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		//case 3: brand ratio, trend
		else if(brand.equals(AnalyzerConstants.ALL_BRAND) && !area.equals(AnalyzerConstants.ALL_AREA) && !cus.equals(AnalyzerConstants.ALL_CUSTOMER)){
			//brand ratio
			RatioBlock rb = new RatioBlock();
			rb.setBrand_sub(true);//all brands
			rb.setKind(kind);
			rb.setBrand_area(true);//brand
			RatioResultList rrl = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl.initilByQuery(res1, 1);//the input arg is the query result from Engine			
			RatioComposite bc = new RatioComposite(composite_content, 0, rb, rrl);    
    		alys.add(bc);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		//case 4: sub brand ratio, area ratio, trend
		else if(sub.equals(AnalyzerConstants.ALL_SUB) && area.equals(AnalyzerConstants.ALL_AREA)){
			//sub brand ratio
			RatioBlock rb = new RatioBlock();
			rb.setBrand_sub(false);//all brands
			rb.setBrand(brand);
			rb.setKind(kind);
			rb.setBrand_area(true);//brand
			RatioResultList rrl = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl.initilByQuery(res1, 1);//the input arg is the query result from Engine			
			RatioComposite bc = new RatioComposite(composite_content, 0, rb, rrl);    
    		alys.add(bc);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            
    		//area ratio
    		RatioBlock rb2 = new RatioBlock();
    		rb2.setArea_customer(true);//all areas
    		rb2.setKind(kind);
    		rb2.setBrand_area(false);//area
			RatioResultList rrl2 = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl2.initilByQuery(res2, 2);//the input arg is the query result from Engine			
			RatioComposite bc2 = new RatioComposite(composite_content, 0, rb2, rrl2);    
    		alys.add(bc2);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		//case 5: sub brand ratio, customer ratio, trend
		else if(sub.equals(AnalyzerConstants.ALL_SUB) && cus.equals(AnalyzerConstants.ALL_CUSTOMER)){
			//sub brand ratio
			RatioBlock rb = new RatioBlock();
			rb.setBrand_sub(false);//all brands
			rb.setBrand(brand);
			rb.setKind(kind);
			rb.setBrand_area(true);//brand
			RatioResultList rrl = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl.initilByQuery(res1, 1);//the input arg is the query result from Engine			
			RatioComposite bc = new RatioComposite(composite_content, 0, rb, rrl);    
    		alys.add(bc);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            
    		//customer ratio
    		RatioBlock rb2 = new RatioBlock();
    		rb2.setArea_customer(false);//all areas
    		rb2.setArea(area);
    		rb2.setKind(kind);
    		rb2.setBrand_area(false);//area
			RatioResultList rrl2 = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl2.initilByQuery(res2, 2);//the input arg is the query result from Engine			
			RatioComposite bc2 = new RatioComposite(composite_content, 0, rb2, rrl2);    
    		alys.add(bc2);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		//case 6: sub brand ratio, trend
		else if(sub.equals(AnalyzerConstants.ALL_SUB) && !area.equals(AnalyzerConstants.ALL_AREA) && !cus.equals(AnalyzerConstants.ALL_CUSTOMER)){
			//sub brand ratio
			RatioBlock rb = new RatioBlock();
			rb.setBrand_sub(false);//all brands
			rb.setBrand(brand);
			rb.setKind(kind);
			rb.setBrand_area(true);//brand
			RatioResultList rrl = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl.initilByQuery(res1, 1);//the input arg is the query result from Engine			
			RatioComposite bc = new RatioComposite(composite_content, 0, rb, rrl);    
    		alys.add(bc);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		//case 7: area ratio, trend
		else if(!brand.equals(AnalyzerConstants.ALL_BRAND) && !sub.equals(AnalyzerConstants.ALL_SUB) && area.equals(AnalyzerConstants.ALL_AREA)){
    		//area ratio
    		RatioBlock rb2 = new RatioBlock();
    		rb2.setArea_customer(true);//all areas
    		rb2.setKind(kind);
    		rb2.setBrand_area(false);//brand
			RatioResultList rrl2 = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl2.initilByQuery(res2, 2);//the input arg is the query result from Engine			
			RatioComposite bc2 = new RatioComposite(composite_content, 0, rb2, rrl2);    
    		alys.add(bc2);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		//case 8: customer ratio, trend
		else if(!brand.equals(AnalyzerConstants.ALL_BRAND) && !sub.equals(AnalyzerConstants.ALL_SUB) && cus.equals(AnalyzerConstants.ALL_CUSTOMER)){
    		//customer ratio
    		RatioBlock rb2 = new RatioBlock();
    		rb2.setArea_customer(false);//all areas
    		rb2.setArea(area);
    		rb2.setKind(kind);
    		rb2.setBrand_area(false);//brand
			RatioResultList rrl2 = new RatioResultList();
			//we should notice that, all the table can not be modified!
			rrl2.initilByQuery(res2, 2);//the input arg is the query result from Engine			
			RatioComposite bc2 = new RatioComposite(composite_content, 0, rb2, rrl2);    
    		alys.add(bc2);
    		composite_content.setLayout(layout_content);
            composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		//case 9: trend
		else if(!brand.equals(AnalyzerConstants.ALL_BRAND) && !sub.equals(AnalyzerConstants.ALL_SUB) && !area.equals(AnalyzerConstants.ALL_AREA) & !cus.equals(AnalyzerConstants.ALL_CUSTOMER)){
			//now, just trend, do nothing here
		}
		if(!kind.equals(KIND.NONE)){
			//trend, always has the trend graph  	
			TrendDataSet ts = new TrendDataSet();
			ts.setKind(kind);	
			ts.setType(type);
			TrendComposite tc = new TrendComposite(composite_content, 0, ts, res3); 
			alys.add(tc);
			composite_content.setLayout(layout_content);
			composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
				
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
		expandBar = new ExpandBar(composite_left, SWT.V_SCROLL);  		
		
		item1 =  new ExpandItem(expandBar, SWT.NONE);
		item2 =  new ExpandItem(expandBar, SWT.NONE);
		
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
	         
	         combo_brand_shipment = new CCombo(comp1, SWT.BORDER|SWT.READ_ONLY);
	         combo_brand_shipment.setVisibleItemCount(5);
	         combo_brand_shipment.setText(AnalyzerConstants.ALL_BRAND);
	         GridData gd_combo_brand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_brand.widthHint = 151;
	         combo_brand_shipment.setLayoutData(gd_combo_brand);
	         
	         
	         combo_sub_shipment = new CCombo(comp1, SWT.BORDER|SWT.READ_ONLY);
	         combo_sub_shipment.setText(AnalyzerConstants.ALL_SUB);
	         combo_sub_shipment.setVisibleItemCount(5);
	         GridData gd_combo_sub_brand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_sub_brand.widthHint = 151;
	         combo_sub_shipment.setLayoutData(gd_combo_sub_brand);
	         combo_sub_shipment.setEnabled(false);
	         combo_sub_shipment.setVisible(false);
	         
	         combo_brand_shipment.addListener(SWT.MouseDown, new Listener() {

	 			@Override
	 			public void handleEvent(Event event) {
	 				List<String> list = Utils.getBrands();
	 				combo_brand_shipment.setItems(list.toArray(new String[list.size()]));
	 				combo_brand_shipment.add(AnalyzerConstants.ALL_BRAND);
	 			}
	         });
	         combo_brand_shipment.addListener(SWT.MouseUp, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				if(combo_brand_shipment.getText().equals("")){
		 					combo_brand_shipment.setText(AnalyzerConstants.ALL_BRAND);
		 					combo_sub_shipment.setVisible(false);
		 				}		 				
		 			}
		         });
	         combo_sub_shipment.addListener(SWT.MouseUp, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				if(combo_sub_shipment.getText().equals("")){
		 					combo_sub_shipment.setText(AnalyzerConstants.ALL_SUB);
		 				}		 				
		 			}
		         });
	         combo_sub_shipment.addListener(SWT.MouseDown, new Listener() {
	 			@Override
	 			public void handleEvent(Event event) {
	 				String brand = combo_brand_shipment.getText();
	 				List<String> list = Utils.getSub_Brands(brand);	 						
	 				combo_sub_shipment.setItems(list.toArray(new String[list.size()]));
	 				combo_sub_shipment.add(AnalyzerConstants.ALL_SUB);
	 			}
	         });
	         combo_brand_shipment.addSelectionListener(new SelectionAdapter() {
		         	@Override
		         	public void widgetSelected(SelectionEvent e) {
//		         		combo_subbrand.clearSelection();
		         		if(!combo_brand_shipment.getText().equals(AnalyzerConstants.ALL_BRAND)){
		         			combo_sub_shipment.deselectAll();
		         			combo_sub_shipment.setEnabled(true);
		         			combo_sub_shipment.setVisible(true);
		         			combo_sub_shipment.setText(AnalyzerConstants.ALL_SUB);
		         		}else{
		         			combo_sub_shipment.setVisible(false);
		         		}
		         	}
		         });
	         
	         Label lbl_space = new Label(comp1, SWT.NONE);
	         lbl_space.setText("");
	         lbl_space.setVisible(false);
	         
	         Label lbl_area = new Label(comp1, SWT.NONE);
	         lbl_area.setText("片区/客户");
	         
	         combo_area_shipment = new CCombo(comp1, SWT.BORDER|SWT.READ_ONLY);
	         combo_area_shipment.setVisibleItemCount(5);
	         combo_area_shipment.setText(AnalyzerConstants.ALL_AREA);
	         GridData gd_combo_area = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_area.widthHint = 151;
	         combo_area_shipment.setLayoutData(gd_combo_area);
	         
	         combo_cus_shipment = new CCombo(comp1, SWT.BORDER|SWT.READ_ONLY);
	         combo_cus_shipment.setText(AnalyzerConstants.ALL_CUSTOMER);
	         combo_cus_shipment.setVisible(false);
	         combo_cus_shipment.setVisibleItemCount(5);
	         GridData gd_combo_customer = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_customer.widthHint = 151;
	         combo_cus_shipment.setLayoutData(gd_combo_customer);
	         combo_cus_shipment.setEnabled(false);
	         
	         combo_area_shipment.addListener(SWT.MouseDown, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				combo_area_shipment.setItems(DataCachePool.getCustomerAreas());	
		 				combo_area_shipment.add(AnalyzerConstants.ALL_AREA);
		 			}
		         });	 
	         combo_area_shipment.addListener(SWT.MouseUp, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				if(combo_area_shipment.getText().equals("")){
		 					combo_area_shipment.setText(AnalyzerConstants.ALL_AREA);
		 					combo_cus_shipment.setVisible(false);
		 				}		 				
		 			}
		         });
	         combo_cus_shipment.addListener(SWT.MouseUp, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				if(combo_cus_shipment.getText().equals("")){
		 					combo_cus_shipment.setText(AnalyzerConstants.ALL_CUSTOMER);
		 				}		 				
		 			}
		         });
	         combo_cus_shipment.addListener(SWT.MouseDown, new Listener() {
		 			@Override
		 			public void handleEvent(Event event) {
		 				String area = combo_area_shipment.getText();
//						System.out.println("area: "+area);
						String[] names = DataCachePool.getCustomerNames(area);
						if(names.length != 0){//no such areas
							combo_cus_shipment.setItems(names);
						}
						combo_sub_shipment.add(AnalyzerConstants.ALL_CUSTOMER);
		 			}
		     });
		         
	         combo_area_shipment.addSelectionListener(new SelectionAdapter() {
		         	@Override
		         	public void widgetSelected(SelectionEvent e) {
		         		if(!combo_area_shipment.getText().equals(AnalyzerConstants.ALL_AREA)){
		         		combo_cus_shipment.deselectAll();
		         		combo_cus_shipment.setEnabled(true);
		         		combo_cus_shipment.setVisible(true);
		         		combo_cus_shipment.setText(AnalyzerConstants.ALL_CUSTOMER);
		         		}else{
		         			combo_cus_shipment.setVisible(false);
		         		}
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
	         
	         combo_brand_profit = new CCombo(comp2, SWT.BORDER|SWT.READ_ONLY);
	         GridData gd_combo_brand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_brand.widthHint = 151;
	         combo_brand_profit.setText(AnalyzerConstants.ALL_BRAND);
	         combo_brand_profit.setLayoutData(gd_combo_brand);
	         
	         combo_sub_profit = new CCombo(comp2, SWT.BORDER|SWT.READ_ONLY);
	         combo_sub_profit.setText(AnalyzerConstants.ALL_SUB);
	         combo_sub_profit.setVisible(false);
	         GridData gd_combo_sub_brand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_sub_brand.widthHint = 151;
	         combo_sub_profit.setLayoutData(gd_combo_sub_brand);
	         
	         combo_brand_profit.addListener(SWT.MouseDown, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				List<String> list = Utils.getBrands();
		 				combo_brand_profit.setItems(list.toArray(new String[list.size()]));
		 				combo_brand_profit.add(AnalyzerConstants.ALL_BRAND);
		 			}
		         });
	         combo_brand_profit.addListener(SWT.MouseUp, new Listener() {

			 			@Override
			 			public void handleEvent(Event event) {
			 				if(combo_brand_profit.getText().equals("")){
			 					combo_brand_profit.setText(AnalyzerConstants.ALL_BRAND);
			 					combo_sub_profit.setVisible(false);
			 				}		 				
			 			}
			         });
	         combo_sub_profit.addListener(SWT.MouseUp, new Listener() {

			 			@Override
			 			public void handleEvent(Event event) {
			 				if(combo_sub_profit.getText().equals("")){
			 					combo_sub_profit.setText(AnalyzerConstants.ALL_SUB);
			 				}		 				
			 			}
			         });
	         combo_sub_profit.addListener(SWT.MouseDown, new Listener() {
		 			@Override
		 			public void handleEvent(Event event) {
		 				String brand = combo_brand_profit.getText();
		 				List<String> list = Utils.getSub_Brands(brand);	 						
		 				combo_sub_profit.setItems(list.toArray(new String[list.size()]));
		 				combo_sub_profit.add(AnalyzerConstants.ALL_SUB);
		 			}
		         });
	         combo_brand_profit.addSelectionListener(new SelectionAdapter() {
			         	@Override
			         	public void widgetSelected(SelectionEvent e) {
//			         		combo_subbrand.clearSelection();
			         		if(!combo_brand_profit.getText().equals(AnalyzerConstants.ALL_BRAND)){
			         			combo_sub_profit.deselectAll();
			         			combo_sub_profit.setEnabled(true);
			         			combo_sub_profit.setVisible(true);
			         			combo_sub_profit.setText(AnalyzerConstants.ALL_SUB);
			         		}else{
			         			combo_sub_profit.setVisible(false);
			         		}
			         	}
			         });
		         
	         Label lbl_space = new Label(comp2, SWT.NONE);
	         lbl_space.setText("");
	         lbl_space.setVisible(false);
	         
	         Label lbl_area = new Label(comp2, SWT.NONE);
	         lbl_area.setText("片区/客户");
	         
	         combo_area_profit = new CCombo(comp2, SWT.BORDER|SWT.READ_ONLY);
	         GridData gd_combo_area = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_area.widthHint = 151;
	         combo_area_profit.setText(AnalyzerConstants.ALL_AREA);
	         combo_area_profit.setLayoutData(gd_combo_area);
	         
	         combo_cus_profit = new CCombo(comp2, SWT.BORDER|SWT.READ_ONLY);
	         combo_cus_profit.setText(AnalyzerConstants.ALL_CUSTOMER);
	         combo_cus_profit.setVisible(false);
	         GridData gd_combo_customer = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	         gd_combo_customer.widthHint = 151;
	         combo_cus_profit.setLayoutData(gd_combo_customer);
	         
	         combo_area_profit.addListener(SWT.MouseDown, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				combo_area_profit.setItems(DataCachePool.getCustomerAreas());	
		 				combo_area_profit.add(AnalyzerConstants.ALL_AREA);
		 			}
		         });	 
	         combo_area_profit.addListener(SWT.MouseUp, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				if(combo_area_profit.getText().equals("")){
		 					combo_area_profit.setText(AnalyzerConstants.ALL_AREA);
		 					combo_cus_profit.setVisible(false);
		 				}		 				
		 			}
		         });
	         combo_cus_profit.addListener(SWT.MouseUp, new Listener() {

		 			@Override
		 			public void handleEvent(Event event) {
		 				if(combo_cus_profit.getText().equals("")){
		 					combo_cus_profit.setText(AnalyzerConstants.ALL_CUSTOMER);
		 				}		 				
		 			}
		         });
	         combo_cus_profit.addListener(SWT.MouseDown, new Listener() {
		 			@Override
		 			public void handleEvent(Event event) {
		 				String area = combo_area_profit.getText();
//						System.out.println("area: "+area);
						String[] names = DataCachePool.getCustomerNames(area);
						if(names.length != 0){//no such areas
							combo_cus_profit.setItems(names);
						}
						combo_cus_profit.add(AnalyzerConstants.ALL_CUSTOMER);
		 			}
		     });
		         
	         combo_area_profit.addSelectionListener(new SelectionAdapter() {
		         	@Override
		         	public void widgetSelected(SelectionEvent e) {
		         		if(!combo_area_profit.getText().equals(AnalyzerConstants.ALL_AREA)){
		         			combo_cus_profit.deselectAll();
		         			combo_cus_profit.setEnabled(true);
		         			combo_cus_profit.setVisible(true);
		         			combo_cus_profit.setText(AnalyzerConstants.ALL_CUSTOMER);
		         		}else{
		         			combo_cus_profit.setVisible(false);
		         		}
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
 
	        
		//=============================================================================================

	     /**
	      * right part to show the analyzed result	
	      */
	    //right part base compoiste
		Composite composite_right  = new Composite(composite, SWT.NONE);
		composite_right.setBackground(new Color(composite.getDisplay(), 255, 250, 250));
//		composite_right.setBounds((int)(w/5), 0, (int)(4*w/5), h);
		composite_right.setBounds(200, 0, 760, h);
		
		//text area to show the tips
		styledText = new StyledText(composite_right, SWT.BORDER|SWT.WRAP);
		styledText.setEditable(false);
		styledText.setBounds((int)(4*w/5/50), (int)(4*w/5/50), (int)(6*w/5/4), (int)(h/10));
		styledText.setText("");//"五得利最近一个月出货量\n"+"总计:15000包"
//		StyleRange styleRange  =   new  StyleRange();
//		styleRange.start  =  0;
//		styleRange.length  =  "五得利最近一个月出货量".length();
//		styleRange.fontStyle  =  SWT.BOLD;
//		styledText.setStyleRange(styleRange);
		
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
//        composite_main.setBounds(0, (int)(h/8+h/100), (int)(4*w/5), (int)(7*h/8-h/100));
        composite_main.setBounds(0, (int)(h/8+h/100), 740, (int)(7*h/8-h/100));
        
        composite_main.setBackground(new Color(composite.getDisplay(), 255,250,250));
        composite_main.setLayout(new FillLayout());
        composite_scroll = new ScrolledComposite(composite_main,  SWT.NONE|SWT.V_SCROLL);//
//		composite_scroll.setVisible(true);
		composite_scroll.setExpandHorizontal(true);  
		composite_scroll.setExpandVertical(true);  
		composite_scroll.addListener(SWT.Activate, new Listener(){    
			public void handleEvent(Event e){
				//need to forceFocus
				composite_scroll.forceFocus();
				}
		}); 
		composite_content = new Composite(composite_scroll, SWT.NONE);
		composite_scroll.setContent(composite_content);
		composite_content.setBackground(new Color(composite.getDisplay(), 255,240,245));
		layout_content = new GridLayout(1, false);  
		layout_content.numColumns = 1;  
		layout_content.horizontalSpacing = 0;
		layout_content.verticalSpacing = 0;
		layout_content.marginHeight = 0;//not recommended
		layout_content.marginWidth = 10;
        composite_content.setLayout(layout_content);
        composite_scroll.setMinSize(composite_content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        /**
         * add the button listener for the classification
         */
        btn_month.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		
        		initialTitle(TYPE.MONTH);
        		for(int i=0;i<alys.size();i++){
        			Composite c = (Composite)alys.get(i);
        			c.dispose();
        		}
        		alys.clear();
        		showResult(TYPE.MONTH);
        	}
        });
        
        btn_season.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		initialTitle(TYPE.SEASON);
        		for(int i=0;i<alys.size();i++){
        			Composite c = (Composite)alys.get(i);
        			c.dispose();
        		}
        		alys.clear();
        		showResult(TYPE.SEASON);
        	}
        });
        btn_year.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		initialTitle(TYPE.YEAR);
        		for(int i=0;i<alys.size();i++){
        			Composite c = (Composite)alys.get(i);
        			c.dispose();
        		}
        		alys.clear();
        		showResult(TYPE.YEAR);
        	}
        });
        
        btn_all.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		initialTitle(TYPE.ALL);
        		for(int i=0;i<alys.size();i++){
        			Composite c = (Composite)alys.get(i);
        			c.dispose();
        		}
        		alys.clear();
        		showResult(TYPE.ALL);
        	}
        });
        composite_main.layout();
        composite_content.layout();
//      composite_scroll.layout();
        
        DataCachePool.cacheProductInfo();
        DataCachePool.cacheCustomerInfo();

	}
}
