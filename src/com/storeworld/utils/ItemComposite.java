package com.storeworld.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

import com.storeworld.common.History;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.deliver.DeliverHistory;
import com.storeworld.deliver.DeliverList;
import com.storeworld.deliver.DeliverUtils;
import com.storeworld.stock.StockHistory;
import com.storeworld.stock.StockList;
import com.storeworld.stock.StockUtils;

/**
 * the item in left navigate part
 * the history items
 * @author dingyuanxiong
 *
 */
public class ItemComposite extends Composite {

	private int id = 0;
	private Color color;
	private int width;
	private int height;
	private ItemComposite self ; 
	private History his;
	
	public void setID(int id){
		this.id = id;
	}
	public int getID(){
		return this.id;
	}
	
	//three text of the history item
	private Text up = new Text(this, SWT.NONE);
	private Text down_left = new Text(this, SWT.NONE);
	private Text down_right = new Text(this, SWT.RIGHT | SWT.NONE);
	private ToolTip tip;
	
	
	public ItemComposite(Composite parent, Color color, int width, int height, History his) {
		super(parent, SWT.BORDER);		
		final Color color1 = new Color(parent.getDisplay(), 255, 245, 238);
		final Color color2 = new Color(parent.getDisplay(), 255, 250, 250);
		tip = new ToolTip(this.getShell(), SWT.NONE);
		tip.setText("Ë«»÷ÏÔÊ¾ÏêÏ¸ÐÅÏ¢");
		this.his = his;
		self = this;
		GridLayout gd = new GridLayout(2, false);
		gd.horizontalSpacing = 0;
		gd.verticalSpacing = 0;
		gd.marginWidth = 0;
		gd.marginHeight = 0;
		this.setLayout(gd);		
		this.setBackground(color);
		this.color = color;
		this.width = width;
		this.height = height;
		//composite size
		this.setSize(width, height);
		setSize();
		setColor();
		
		up.setEnabled(false);
		down_left.setEnabled(false);
		down_right.setEnabled(false);	
		this.addListener(SWT.MouseDoubleClick, new Listener(){

			@Override
			public void handleEvent(Event event) {
				History history = getHistory();
				if(history instanceof StockHistory){
					//if click to show history, leave edit mode
					StockUtils.leaveEditMode();
					StockUtils.setStatus("HISTORY");
					
					StockList.showHistory((StockHistory)history);
					StockUtils.recordItemComposite(getSelf());
					
					
				}else{//DeliverHistory
					
					DeliverUtils.setStatus("HISTORY");
					//if click to show history, leave edit mode
					DeliverUtils.leaveEditMode();
					DeliverUtils.leaveReturnMode();
					
					DeliverList.showHistory((DeliverHistory)history);
					DeliverUtils.recordItemComposite(getSelf());
					DeliverContentPart.resetInfo();
				}
				
			}
			
		});
		this.addListener(SWT.MouseEnter, new Listener(){

			@Override
			public void handleEvent(Event event) {
				 self.setBackgroundColor(color1);
				 tip.setVisible(true);
			}
			
		});
		this.addListener(SWT.MouseExit, new Listener(){

			@Override
			public void handleEvent(Event event) {
				 self.setBackgroundColor(color2);
				 tip.setVisible(false);
			}
			
		});			
		
		this.layout();
	}
	private void setBackgroundColor(Color color){
		self.setBackground(color);
		up.setBackground(color);
		down_left.setBackground(color);
		down_right.setBackground(color);
	}
	private void setSize() {
		
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_text.widthHint = (int)((this.width-4)/2);
		gd_text.heightHint = (int)(this.height/2);
		up.setLayoutData(gd_text);
				
		GridData gd_text_2 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_text_2.widthHint = (int)((this.width-4)/2);
		gd_text_2.heightHint = (int)(this.height/2);
		down_left.setLayoutData(gd_text_2);
		
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_1.widthHint = (int)((this.width-4)/2);
		gd_text_1.heightHint = (int)(this.height/2);
		down_right.setLayoutData(gd_text_1);

	}
	private void setColor(){
		up.setBackground(color);
		down_left.setBackground(color);
		down_right.setBackground(color);
	}
	
	//set the title
	public void setValue(String u){
		this.up.setText(u);
	}
	//set the three fields
	public void setValue(String u, String dl, String dr){
		this.up.setText(u);
		this.down_left.setText(dl);
		this.down_right.setText(dr+Constants.SPACE);
		
	}
	public History getHistory(){
		return this.his;
	}
	public ItemComposite getSelf(){
		return this.self;
	}
	@Override
	public boolean equals(Object obj) {
		ItemComposite ic = (ItemComposite)obj;
		History history = getHistory();
		if(history instanceof StockHistory){
			StockHistory hisic = (StockHistory)ic.getHistory();
			if(((StockHistory)history).getTime().equals(hisic.getTime()))
				return true;
			else 
				return false;
		}else{
			DeliverHistory hisic = (DeliverHistory)ic.getHistory();
			if(((DeliverHistory)history).getTime().equals(hisic.getTime()))
				return true;
			else 
				return false;
		}
	}
	
}
