package com.storeworld.returndeliver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.storeworld.deliver.Deliver;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.extenddialog.IndeedKeyBoard;
import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Utils;




public class ReturnItemComposite extends Composite{

	private ReturnItemComposite self ; 
	private String id = "";
	private String brand = "";
	private String sub = "";
	private String size = "";
	private String unit = "";
	private String price = "";
	private String delivernumber = "";
	private String returnnumber ="";
	private String ordernumber = "";
	
	private Button check = null;
	private Text text_brand;
	private Text text_sub;
	private Text text_size;
	private Text text_unit;
	private Text text_price;
	private Text text_delivernumber;
	private Text text_returnnumber;
	private static DecimalFormat df = new DecimalFormat("0.00");
	private static Pattern pattern_return_val = Pattern.compile("\\d+");
	/**
	 * getter/setter of deliver info
	 * @param id
	 */
	public void setID(String id){
		this.id = id;
	}
	public String getID(){
		return this.id;
	}
	
	public void setOrderNumber(String od){
		this.ordernumber = od;
	}
	public String getOrderNumber(){
		return this.ordernumber;
	}
	
	public void setBrand(String bd){
		this.brand = bd;
	}
	public String getBrand(){
		return this.brand;
	}
	
	public void setSub(String sd){
		this.sub = sd;
	}
	public String getSub(){
		return this.sub;
	}
	
	public void setProdSize(String size){
		this.size = size;
	}
	public String getProdSize(){
		return this.size;
	}
	
	public void setUnit(String ut){
		this.unit = ut;
	}
	public String getUnit(){
		return this.unit;
	}
	
	public void setPrice(String price){
		this.price = price;
	}
	public String getPrice(){
		return this.price;
	}
	
	public void setDeliverNumber(String number){
		this.delivernumber = number;
	}
	public String getDeliverNumber(){
		return this.delivernumber;
	}
	
	public void setReturnNumber(String number){
		this.returnnumber = number;
	}
	public String getReturnNumber(){
		return this.returnnumber;
	}
	
	/**
	 * if return this item
	 * @return
	 */
	public boolean getCheck(){
		if(check.getSelection())
			return true;
		else
			return false;
	}
	
	public ReturnItemComposite(Composite parent, Deliver deliver) {
		super(parent, SWT.NONE);		
		setSize(736, 33);
		self = this;
		this.setID(deliver.getID());
		this.setBrand(deliver.getBrand());
		this.setSub(deliver.getSubBrand());
		this.setProdSize(deliver.getSize());
		this.setUnit(deliver.getUnit());
		this.setPrice(deliver.getPrice());
		this.setDeliverNumber(deliver.getNumber());
		this.setReturnNumber("0");
		this.setOrderNumber(deliver.getOrderNumber());
		
		check= new Button(this, SWT.CHECK);
		check.setBounds(10, 4, 23, 17);
		
		text_brand = new Text(this, SWT.NONE);
		text_brand.setBounds(60, 4, 130, 23);
		text_brand.setText(this.getBrand());
		text_brand.setEnabled(false);
		
		text_sub = new Text(this, SWT.NONE);
		text_sub.setBounds(196, 4, 170, 23);
		text_sub.setText(this.getSub());
		text_sub.setEnabled(false);
		
		text_size = new Text(this, SWT.NONE);
		text_size.setBounds(372, 4, 60, 23);
		text_size.setText(this.getProdSize());
		text_size.setEnabled(false);
		
		text_unit = new Text(this, SWT.NONE);
		text_unit.setBounds(438, 4, 50, 23);
		text_unit.setText(this.getUnit());
		text_unit.setEnabled(false);
		
		
		text_price = new Text(this, SWT.NONE);
		text_price.setBounds(494, 4, 60, 23);
		text_price.setText(df.format(Double.valueOf(this.getPrice())));
		text_price.setEnabled(false);
		
		text_delivernumber = new Text(this, SWT.NONE);
		text_delivernumber.setBounds(560, 4, 65, 23);
		text_delivernumber.setText(this.getDeliverNumber());
		text_delivernumber.setEnabled(false);
		
		text_returnnumber = new Text(this, SWT.NONE);
		text_returnnumber.setBounds(632, 4, 65, 23);
		text_returnnumber.setText(this.getReturnNumber());
		text_returnnumber.setEnabled(false);
		
		addListeners();
		
		this.layout();
	}

	private void addListeners(){
		check.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {

	    		if(check.getSelection()){					
	    			text_returnnumber.setEnabled(true);
	    		}
	    		else{
	    			text_returnnumber.setEnabled(false);
	    			text_returnnumber.setText("0");
	    		}
			}
		});
		
		text_returnnumber.addFocusListener(new FocusAdapter(){
			
			@Override
			public void focusGained(FocusEvent e) {
				check.forceFocus();				
				IndeedKeyBoard inkb = new IndeedKeyBoard(text_returnnumber, self.getParent().getShell(), 0, 180, 250);
				inkb.open();
				if(Utils.getIndeedClickButton() && Utils.getIndeedNeedChange()){
					String txt = Utils.getIndeed();
					//reasonable value
					if(pattern_return_val.matcher(txt).matches()){
						int deli = Integer.valueOf(text_delivernumber.getText());
						int ret = Integer.valueOf(txt);
						if(ret <= deli){
							text_returnnumber.setText(txt);
							setReturnNumber(txt);
							//compute to get the data show in UI
							double total = 0.0;
							ArrayList<ReturnItemComposite> items = ReturnComposite.getReturnItems();
							for(int i=0;i<items.size();i++){
								ReturnItemComposite rc = items.get(i);
								if(rc.getCheck()){
									double price = Double.valueOf(rc.getPrice());
									total+=(price * Integer.valueOf(rc.getReturnNumber()));									
								}
							}
							DeliverContentPart.setTotal(df.format(total));
							DeliverContentPart.setIndeed(df.format(total));
						}else{
							MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
							mbox.setMessage("退货数应不大于送货量");
							mbox.open();
						}
					}else{
						MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage("数值应为整数");
						mbox.open();
					}
					//initial the next click
					Utils.setIndeedClickButton(false);
				}
			}
			
		});
	}

	public ReturnItemComposite getSelf(){
		return this.self;
	}
	@Override
	public boolean equals(Object obj) {
		ReturnItemComposite rc = (ReturnItemComposite)obj;
		if(rc.getBrand().equals(this.getBrand()) && rc.getSub().equals(this.getSub()) && rc.getProdSize().equals(this.getProdSize())){
			return true;
		}
		return true;
	}
}
