package com.storeworld.returndeliver;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.storeworld.common.DataInTable;
import com.storeworld.deliver.Deliver;


public class ReturnComposite extends Composite {

	private static ScrolledComposite context = null;
	private static Composite composite_fn = null;
	static ArrayList<ReturnItemComposite> returnitems = new ArrayList<ReturnItemComposite>();
	
//	private static HashMap<String, Deliver> id2Deliver = new HashMap<String, Deliver>();
	
//	public static HashMap<String, Deliver> getId2DeliverMap(){
//		return id2Deliver;
//	}
	
	public static ArrayList<ReturnItemComposite> getReturnItems(){
		return returnitems;
	}
	
	private void initialContext(){
		composite_fn = new Composite(context, SWT.NONE);
		context.setContent(composite_fn);
		GridLayout layout = new GridLayout(1, false);  
        layout.numColumns = 1;  
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 2;
        layout.marginWidth = 0;
        composite_fn.setLayout(layout);
        context.setMinSize(composite_fn.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public ReturnComposite(Composite parent, int type) {
		super(parent, type);				
		setSize(736, 230);
		Label label = new Label(this, SWT.NONE);
		label.setBounds(0, 0, 54, 17);
		label.setText("是否退货");
		
		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("品牌");
		label_1.setBounds(60, 0, 130, 17);
		
		Label label_2 = new Label(this, SWT.NONE);
		label_2.setText("子品牌");
		label_2.setBounds(196, 0, 170, 17);
		
		Label label_3 = new Label(this, SWT.NONE);
		label_3.setText("规格");
		label_3.setBounds(372, 0, 60, 17);
		
		Label label_4 = new Label(this, SWT.NONE);
		label_4.setText("单位");
		label_4.setBounds(438, 0, 50, 17);
		
		Label label_5 = new Label(this, SWT.NONE);
		label_5.setText("单价");
		label_5.setBounds(494, 0, 60, 17);
		
		Label label_6 = new Label(this, SWT.NONE);
		label_6.setText("进货数量");
		label_6.setBounds(560, 0, 65, 17);
		
		Label label_7 = new Label(this, SWT.NONE);
		label_7.setText("退货数量");
		label_7.setBounds(631, 0, 65, 17);
		
		
		
		context = new ScrolledComposite(this, SWT.V_SCROLL);//
		context.setBounds(0, 23, 733, 207);
		context.setExpandHorizontal(true);  
		context.setExpandVertical(true);  
		context.addListener(SWT.Activate, new Listener(){    
			public void handleEvent(Event e){     
				context.forceFocus();
				}
		}); 
		
		initialContext();		
	}
	
	public void showDelivers(ArrayList<DataInTable> delivers){
		//first, we clear the old values in list and UI side

		for(int i=0;i<returnitems.size(); i++){
			returnitems.get(i).dispose();
		}
		returnitems.clear();
		
		//second, we add new
		//do not use the last empty row
		for (int i = 0; i < delivers.size()-1; i++) {
			Deliver ret = (Deliver)delivers.get(i);
//			id2Deliver.put(ret.getID(), ret);
			
			ReturnItemComposite rc = new ReturnItemComposite(composite_fn, ret);			
			returnitems.add(rc);
			context.setMinSize(composite_fn.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));
			composite_fn.layout();
		}		
	}	
}
