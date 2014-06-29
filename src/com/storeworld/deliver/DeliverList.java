package com.storeworld.deliver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.DeliverInfoAllDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.product.Product;
import com.storeworld.product.ProductUtils;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.pub.service.StockInfoService;
import com.storeworld.stock.Stock;
import com.storeworld.stock.StockCellModifier;
import com.storeworld.stock.StockContentPart;
import com.storeworld.stock.StockHistory;
import com.storeworld.stock.StockUtils;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.ItemComposite;
import com.storeworld.utils.UIDataConnector;
import com.storeworld.utils.Utils;

/**
 * produce the data for deliver table
 * @author dingyuanxiong
 *
 */
public class DeliverList {
	
	private static ArrayList<DataInTable> deliverList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();
	private static final DeliverInfoService deliverinfo = new DeliverInfoService();
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	public DeliverList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
		
		//order number 
		String newID = "";
		try {
			newID = String.valueOf(deliverinfo.getNextDeliverID());
		} catch (Exception e) {
			System.out.println("get the current id of deliver info failed");
		}
		//no record
		if(newID.equals("-1") || newID.equals(""))
			newID="1";//empty
		//by the list of Customer from database
		DeliverUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)));		
		//if the brand2sub not cached, query the database
		DataCachePool.cacheProductInfo();		
				
	}
	
	/**
	 * get the deliver list data
	 * @return
	 */
	public static ArrayList<DataInTable> getDelivers() {
		return deliverList;
	}
	
	/**
	 * add a deliver
	 * @param deliver
	 */
	public void addDeliver(Deliver deliver) {
		deliverList.add(deliver);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).add(deliver);
		
		//if in edit mode, change the history
		if(DeliverUtils.getEditMode() && DeliverUtils.getStatus().equals("HISTORY")){
			DeliverUtils.updateHistory(deliverList);
		}
	}

	/**
	 * delete a deliver from table & database
	 * @param deliver
	 */
	public void removeDeliver(Deliver deliver) {
		deliverList.remove(deliver);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).remove(deliver);
			try {
				deliverinfo.deleteDeliverInfoAndUpdateGoods(Integer.valueOf(deliver.getID()), deliver);
				// how to determine if the product is new or real 0?
			} catch (Exception e) {
				System.out.println("remove the deliver failed");
			}
			//if in edit mode, change the history
			if(DeliverUtils.getEditMode() && DeliverUtils.getStatus().equals("HISTORY")){
				DeliverUtils.updateHistory(deliverList);
			}
		}
		double total = 0.00;
		for(int i=0;i<deliverList.size()-1;i++){
			Deliver st = (Deliver)(deliverList.get(i));
			String price = st.getPrice();
			String number = st.getNumber();
			double p = Double.valueOf(price);
			int n = Integer.valueOf(number);
			total+=(p * n);	
		}
		DeliverContentPart.setTotal(df.format(total));	
		DeliverContentPart.setIndeed(df.format(total));	
	}

	public void deliverChangedForUnit(Deliver deliver){
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(deliver);
		}
	}
	
	public void deliverChangedThree(Deliver deliver){
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(deliver);	
			
			for(int i=0;i<deliverList.size();i++){
				Deliver d = (Deliver)(deliverList.get(i));
				if(d.getID().equals(deliver.getID())){
					d.setBrand(deliver.getBrand());
					d.setSubBrand(deliver.getSubBrand());
					d.setSize(deliver.getSize());
					d.setUnit(deliver.getUnit());
					d.setPrice(deliver.getPrice());
					d.setNumber(deliver.getNumber());
					break;
				}				
			}
			
			DeliverUtils.refreshTableData();
			
		}
	}
	
	private boolean checkSameDeliver(Deliver deliver){
		boolean ret = false;
		for(int i=0;i<deliverList.size()-1;i++){
			Deliver stmp = (Deliver)deliverList.get(i);
			//a different stock
			if(!stmp.getID().equals(deliver.getID())){
				if(deliver.getBrand().equals(stmp.getBrand()) && deliver.getSubBrand().equals(stmp.getSubBrand())){//&&deliver.getSize().equals(stmp.getSize())
					ret = true;
					break;
				}				
			}			
		}		
		return ret;
	}
	
	//reserve1 : indeed
	//reserve2: deliver_time
	/**
	 * update the deliver data from table & database
	 * @param deliver
	 */
	public void deliverChanged(Deliver deliver) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
//			(iterator.next()).update(deliver);
			Map<String, Object> common = new HashMap<String ,Object>();
			Map<String, Object> st = new HashMap<String ,Object>();
			st.put("id", deliver.getID());
			st.put("brand", deliver.getBrand());
			st.put("sub_brand", deliver.getSubBrand());
			st.put("standard", deliver.getSize());
			st.put("unit", deliver.getUnit());
			st.put("deliver_time", DeliverUtils.getTime());
			st.put("order_num", DeliverUtils.getOrderNumber());
			if(!deliver.getPrice().equals(""))
				st.put("unit_price", String.valueOf(deliver.getPrice()));
			else
				st.put("unit_price", String.valueOf("0.0"));
			if(!deliver.getNumber().equals(""))
				st.put("quantity", String.valueOf(deliver.getNumber()));
			else
				st.put("quantity", String.valueOf("0"));			
			
			//add the time & ordernumber to the deliver
			deliver.setTime(DeliverUtils.getTime());
			deliver.setOrderNumber(DeliverUtils.getOrderNumber());
			(iterator.next()).update(deliver);
			
			if(!DeliverValidator.checkID(deliver.getID()) && DeliverValidator.rowLegal(deliver) && DeliverValidator.rowComplete(deliver)){
				//update the database here		
				int ret = 0;
					try {						
						//exist the same deliver
						if(checkSameDeliver(deliver)){
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
		    		    	messageBox.setMessage(String.format("存在相同的货品在该送货表中，请重新选择！"));		    		    	
		    		    	if (messageBox.open() == SWT.OK){	    		    		
		    		    		Deliver s = new Deliver();
		    					s.setID(deliver.getID());//new row in fact
		    					s.setBrand(deliver.getBrand());
		    					s.setSubBrand("");
		    					s.setSize("");
		    					s.setUnit(deliver.getUnit());	
		    					s.setNumber(deliver.getNumber());//initial it's empty, not null
		    					s.setPrice(deliver.getPrice());
//		    					s.setTime(time);time and indeed?
		    					DeliverCellModifier.getDeliverList().deliverChangedThree(s);
		    					ret = -1;//in this way, we do not update history
		    		    	}
						}else{
						
						ret = deliverinfo.updateDeliverInfo(deliver.getID(),st, "");
						if(ret == -1){
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
		    		    	messageBox.setMessage(String.format("品牌:%s, 子品牌:%s, 规格:%s 的货品不存在仓库中， 点击确定将添加货品至仓库表中并继续发货，点击取消重新选择其他货品", 
		    		    			deliver.getBrand(),deliver.getSubBrand(),deliver.getSize()));		    		    	
		    		    	if (messageBox.open() == SWT.OK){
		    		    		deliverinfo.insertGoodsAndUpdateDeliverTwo(deliver.getID(),st);
		    		    		DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
		    		    		ret = 0;//back to normal case
		    		    	}else{
		    		    		//update the deliver
		    		    		Deliver d = new Deliver();
		    					d.setID(deliver.getID());
		    					d.setBrand(deliver.getBrand());
		    					d.setSubBrand("");
		    					d.setSize("");
		    					d.setUnit(deliver.getUnit());	
		    					d.setNumber(deliver.getNumber());//initial it's empty, not null
		    					d.setPrice(deliver.getPrice());
//		    					d.setOrderNumber(deliver.getOrderNumber());//?
		    					DeliverCellModifier.getDeliverList().deliverChangedThree(d);		
		    		    	}
						}else if(ret == -2){
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
		    		    	messageBox.setMessage(String.format("品牌:%s, 子品牌:%s, 规格:%s 的货品库存不足， 点击确定将添加货品至仓库表中并继续发货，点击取消重新选择其他货品或更新库存", 
		    		    			deliver.getBrand(), deliver.getSubBrand(), deliver.getSize()));		    		    	
		    		    	if (messageBox.open() == SWT.OK){
		    		    		deliverinfo.updateGoodsAndUpdateDeliverTwo(deliver.getID(), st);
		    		    		DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
		    		    		ret = 0;//back to normal case		    		    		
		    		    	}else{
		    		    		//update the deliver
		    		    		Deliver d = new Deliver();
		    					d.setID(deliver.getID());
		    					d.setBrand(deliver.getBrand());
		    					d.setSubBrand("");
		    					d.setSize("");
		    					d.setUnit(deliver.getUnit());	
		    					d.setNumber(deliver.getNumber());//initial it's empty, not null
		    					d.setPrice(deliver.getPrice());
//		    					d.setOrderNumber(deliver.getOrderNumber());//?
		    					DeliverCellModifier.getDeliverList().deliverChangedThree(d);		
		    		    	}							
						}
						}
					} catch (Exception e) {
						System.out.println("update deliver failed");
					}
//				}
				//if in edit mode, change the history
				if(DeliverUtils.getEditMode() && DeliverUtils.getStatus().equals("HISTORY") && ret == 0){
					DeliverUtils.updateHistory(deliverList);
				}
			}
			if(DeliverValidator.checkID(deliver.getID()) && DeliverValidator.rowLegal(deliver) && DeliverValidator.rowComplete(deliver)){				
				try {
					
					if(checkSameDeliver(deliver)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的货品在该送货表中，请重新选择！"));		    		    	
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Deliver s = new Deliver();
	    					s.setID(deliver.getID());//new row in fact
	    					s.setBrand(deliver.getBrand());
	    					s.setSubBrand("");
	    					s.setSize("");
	    					s.setUnit(deliver.getUnit());	
	    					s.setNumber(deliver.getNumber());//initial it's empty, not null
	    					s.setPrice(deliver.getPrice());
//	    					s.setTime(time);time and indeed?
	    					DeliverCellModifier.getDeliverList().deliverChangedThree(s);
//	    					ret = -1;//in this way, we do not update history
	    		    	}
					}else{
					int ret = deliverinfo.addDeliverInfo(st);
					if(ret == -1){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
	    		    	messageBox.setMessage(String.format("品牌:%s, 子品牌:%s, 规格:%s 的货品不存在仓库中， 点击确定将添加货品至仓库表中并继续发货，点击取消重新选择其他货品", 
	    		    			deliver.getBrand(), deliver.getSubBrand(), deliver.getSize()));		    		    	
	    		    	if (messageBox.open() == SWT.OK){   
	    		    		deliverinfo.insertGoodsAndUpdateDeliver(deliver.getID(), st);
	    		    		DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
	    					DeliverCellModifier.addNewTableRow(deliver);
	    		    	}else{
	    		    		Deliver d = new Deliver();
	    					d.setID(deliver.getID());//in fact, the new row
	    					d.setBrand("");
	    					d.setSubBrand("");
	    					d.setSize("");
	    					d.setUnit("");	
	    					d.setNumber("");//initial it's empty, not null
	    					d.setPrice("");
	    					d.setOrderNumber("");//?
	    					deliverChangedThree(d);
	    		    	}												
					}else if(ret == -2){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
	    		    	messageBox.setMessage(String.format("品牌:%s, 子品牌:%s, 规格:%s 的货品库存不足， 点击确定将添加货品至仓库表中并继续发货，点击取消重新选择其他货品或更新库存", 
	    		    			deliver.getBrand(), deliver.getSubBrand(), deliver.getSize()));	    		    	
	    		    	if (messageBox.open() == SWT.OK){   
	    		    		deliverinfo.updateGoodsAndUpdateDeliver(deliver.getID(), st);
	    		    		DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
	    					DeliverCellModifier.addNewTableRow(deliver);
	    		    	}else{
	    		    		Deliver d = new Deliver();
	    					d.setID(deliver.getID());//in fact, the new row
	    					d.setBrand("");
	    					d.setSubBrand("");
	    					d.setSize("");
	    					d.setUnit("");	
	    					d.setNumber("");//initial it's empty, not null
	    					d.setPrice("");
	    					d.setOrderNumber("");//?
	    					deliverChangedThree(d);
	    		    	}						
					}else{//0
						DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
						DeliverCellModifier.addNewTableRow(deliver);	
					}
					}
				} catch (Exception e) {
					System.out.println("add deliver failed");
				}
			}
			boolean updateSum = true;
			double total = 0.000;
			for (int i = 0; i < deliverList.size() - 1; i++) {
				Deliver stin = (Deliver) (deliverList.get(i));
				String price = stin.getPrice();
				String number = stin.getNumber();
				if(!(DeliverValidator.rowLegal(stin) && DeliverValidator.rowComplete(stin))){
					updateSum = false;
					break;
				}					
				double p = Double.valueOf(price);
				int n = Integer.valueOf(number);
				total += (p * n);
			}
			if(updateSum){
				DeliverContentPart.setTotal(df.format(total));
				DeliverContentPart.setIndeed(df.format(total));
			}
			
		}

	}

	/**
	 * remove current history from database & navigator
	 */
	public static void removeCurrentHistory(){
		DeliverInfoService deliverinfo = new DeliverInfoService();		
		String order_num = DeliverUtils.getOrderNumber();				
		try {
			deliverinfo.deleteDeliverAndCommonByOrderNumber(order_num);
		} catch (Exception e) {
			System.out.println("delete deliver failed");
		}
		
		//remove history & item composite
		ItemComposite itemCurrent = DeliverUtils.getItemCompositeRecord();
		ArrayList<ItemComposite> itemlist = DeliverUtils.getItemList();
		ArrayList<DeliverHistory> hislist = DeliverUtils.getHistoryList();
		DeliverHistory his = (DeliverHistory)itemCurrent.getHistory();
		hislist.remove(his);//remove the StockHistory
		itemlist.remove(itemCurrent);
		itemCurrent.dispose();
		//layout the item list
		DeliverUtils.layoutItemList();
		DeliverContentPart.disableEditContent();
	}
	
	/**
	 * remove all the delivers from the table, not the database
	 * if the user click add a new deliver table, we initialize the table
	 */
	public static void removeAllDelivers(){
		Deliver st = new Deliver(DeliverUtils.getNewLineID());
		deliverList.clear();
		//add a new line, do not wat the addDeliver method to become a static
		DeliverCellModifier.getDeliverList().addDeliver(st);
		//refresh the table to show color
		Utils.refreshTable(DeliverCellModifier.getTableViewer().getTable());
	}
	
	public static void updateDeliversByOrderNumber(String ordernum, String indeed){
		
		try {
			deliverinfo.updateDeliversIndeedByOrderNumber(ordernum, indeed);
		} catch (Exception e) {
			System.out.println("update the indeed failed");
		}
		
		
	}
	
	/**
	 * remove the deliver items from database
	 * @param ordernum
	 */
	public static void deleteDeliversUseLess(String ordernum){		
		try {
			deliverinfo.deleteDeliversByOrderNumber(ordernum);
		} catch (Exception e) {
			System.out.println("update the indeed failed");
		}
	}
	
	/**
	 * if the user double click the history, show it
	 * @param history
	 */
	public static void showHistory(DeliverHistory history){
		//clear the stocks first
		deliverList.clear();
		//show the editable button
		DeliverContentPart.makeHistoryEditable();
		//make the table & time picker enable = false
		DeliverContentPart.makeDisable();		
		
		//show the clicked stock history
		String order = history.getOrderNumber();
		String time = history.getTime();
		String area = "";
		String name = "";
		String phone = "";
		String addr = "";
		//query database to get the history and addStock
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("deliver_time", time);
		double total = 0.00;
		double p = 0.0;
		int n=0;
		try {
			//remove the items
			DeliverCellModifier.getTableViewer().getTable().removeAll();
			deliverList.clear();//clear first
			ReturnObject ret = deliverinfo.queryDeliverInfo(map);
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			String indeed = "";
			String ordernum = "";
			for(int i=0;i<list.size();i++){				
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				if(i==0){
					order = cDTO.getOrder_num();
					time = cDTO.getDeliver_time();
					area = cDTO.getCustomer_area();
					name = cDTO.getCustomer_name();
					phone = cDTO.getTelephone();
					addr = cDTO.getDeliver_addr();	
					DeliverContentPart.setCommonInfo(area, name, phone, addr, order, time);
				}
				
				Deliver de_tmp = new Deliver();
				de_tmp.setID(cDTO.getUni_id());
				de_tmp.setBrand(cDTO.getBrand());
				de_tmp.setSubBrand(cDTO.getSub_brand());
				de_tmp.setSize(cDTO.getStandard());		
				de_tmp.setUnit(cDTO.getUnit());
				de_tmp.setPrice(String.valueOf(cDTO.getUnit_price()));
				de_tmp.setNumber(cDTO.getQuantity());
				ordernum = cDTO.getOrder_num();
				de_tmp.setOrderNumber(ordernum);
				indeed = cDTO.getUni_reserve1();
				if(indeed == null)
					indeed = "";
				//...now in test, only show these three property
				DeliverCellModifier.getDeliverList().addDeliver(de_tmp);
				p = Double.valueOf(cDTO.getUnit_price());
				n = Integer.valueOf(cDTO.getQuantity());
				total+=(p * n);		
			}
			DeliverUtils.setOrderNumber(ordernum);
			//add new line
			Deliver de = new Deliver(DeliverUtils.getNewLineID());
			DeliverCellModifier.getDeliverList().addDeliver(de);
			//refresh table
			Utils.refreshTable(DeliverCellModifier.getTableViewer().getTable());
			//show total
			DeliverContentPart.setTotal(df.format(total));
			if(indeed.equals(""))
				DeliverContentPart.setIndeed(df.format(total));
			else
				DeliverContentPart.setIndeed(indeed);
			
			
		} catch (Exception e) {
			System.out.println("query the deliver by time failed");
		}		
		


	}
	
	/**
	 * may multi content provider
	 * @param viewer
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * 
	 * @param viewer
	 */
	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return deliverList.toString();
	}
	
}
