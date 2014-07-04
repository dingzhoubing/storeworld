package com.storeworld.deliver;

import java.sql.SQLException;
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

import com.mysql.jdbc.Connection;
import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.customer.Customer;
import com.storeworld.customer.CustomerCellModifier;
import com.storeworld.customer.CustomerUtils;
import com.storeworld.database.BaseAction;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.DeliverInfoAllDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.product.Product;
import com.storeworld.product.ProductCellModifier;
import com.storeworld.product.ProductUtils;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.ItemComposite;
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
	private static final BaseAction baseAction = new BaseAction();
	private static DecimalFormat df = new DecimalFormat("0.00");
	private static ArrayList<Product> products_changed = new ArrayList<Product>();
	
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
			MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK);						
	    	messageBox.setMessage("初始化数据库失败，请重新启动"); 	
	    	if (messageBox.open() == SWT.OK){	    			    	
	    		MainUI.getMainUI_Instance(Display.getDefault()).dispose();
	    		System.exit(0);
	    		return;
	    	}
		}
		//no record
		if(newID.equals("-1") || newID.equals(""))
			newID="1";//empty
		//by the list of Customer from database
		DeliverUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)));		
		//if the brand2sub not cached, query the database
		try {
			DataCachePool.cacheProductInfo();
		} catch (Exception e) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("查询货品信息失败， 重新启动软件");
			mbox.open();
		}		
				
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
	 * @throws Exception 
	 */
	public void removeDeliver(Deliver deliver) throws Exception {
		Product prec = null;
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}
		
		
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){			
			try {
				conn.setAutoCommit(false);
				deliverinfo.updateDeliversIndeedByOrderNumber(conn, DeliverUtils.getOrderNumber(), "");
				prec = deliverinfo.deleteDeliverInfoAndUpdateGoods(conn, Integer.valueOf(deliver.getID()), deliver);
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
				
			}finally{
				conn.close();
			}
			(iterator.next()).remove(deliver);
		}
		deliverList.remove(deliver);
		
		if(prec != null){
			String nid = DeliverUtils.getNewLineID();
			if(prec.getID().equals(nid)){
				ProductCellModifier.getProductList().productChangedTwo(prec);
			}else{
				ProductCellModifier.getProductList().productChangedThree(prec);
			}			
		}
		
		
		//if in edit mode, change the history
		if(DeliverUtils.getEditMode() && DeliverUtils.getStatus().equals("HISTORY")){
			DeliverUtils.updateHistory(deliverList);
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
	
	public static void relatedProductChange(ArrayList<Product> products_changed, boolean show){
		String info = "";
		String current = ProductUtils.getNewLineID();
		int dup = 0;
		for(Product p : products_changed){//update product table UI
			int repo = Integer.valueOf(p.getRepository());
			if(repo < 0){
			String bp = p.getBrand();
			String sp = p.getSubBrand();
			info += bp+":"+sp+" ";
			}
			if(p.getID().equals(current)){
				int tid = Integer.valueOf(p.getID());
				int nid = tid + dup;//new id
				p.setID(String.valueOf(nid));
				//already do the cache
				ProductCellModifier.getProductList().productChangedTwo(p);
				dup++;									
			}else{
				ProductCellModifier.getProductList().productChangedThree(p);
			}								
		}	
		if(!info.equals("") && show){
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("商品 "+info+" 库存小于零，请检查库存表并更新");
			mbox.open();
		}
//		return info;
	}
	

	
	//reserve1 : indeed
	//reserve2: deliver_time
	/**
	 * update the deliver data from table & database
	 * @param deliver
	 * @throws Exception 
	 */
	public void deliverChanged(Deliver deliver) throws Exception {
				
		products_changed.clear();
		
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}
		
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(deliver);
//			(iterator.next()).update(deliver);
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
			
			try {	
				conn.setAutoCommit(false);
				if(!DeliverValidator.checkID(deliver.getID()) && DeliverValidator.rowLegal(conn, deliver) && DeliverValidator.rowComplete(deliver)){
				//update the database here		
				int ret = 0;
					
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
						
						ret = deliverinfo.updateDeliverInfo(conn, deliver.getID(),st, "", products_changed);
						//update indeed
						deliverinfo.updateDeliversIndeedByOrderNumber(conn, deliver.getOrderNumber(), "");
						if(ret == -1){
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
		    		    	messageBox.setMessage(String.format("品牌:%s, 子品牌:%s, 规格:%s 的货品不存在仓库中， 点击确定将添加货品至仓库表中并继续发货，点击取消重新选择其他货品", 
		    		    			deliver.getBrand(),deliver.getSubBrand(),deliver.getSize()));		    		    	
		    		    	if (messageBox.open() == SWT.OK){
		    		    		deliverinfo.insertGoodsAndUpdateDeliverTwo(conn, deliver.getID(),st, products_changed);
		    		    		
		    		    		conn.commit();
		    		    		DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
		    		    		ret = 0;//back to normal case
		    		    	}else{
		    		    		conn.commit();
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
//						else if(ret == -2){
//							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
//		    		    	messageBox.setMessage(String.format("品牌:%s, 子品牌:%s, 规格:%s 的货品库存不足， 点击确定将添加货品至仓库表中并继续发货，点击取消重新选择其他货品或更新库存", 
//		    		    			deliver.getBrand(), deliver.getSubBrand(), deliver.getSize()));		    		    	
//		    		    	if (messageBox.open() == SWT.OK){
//		    		    		deliverinfo.updateGoodsAndUpdateDeliverTwo(conn, deliver.getID(), st, products_changed);
//		    		    		conn.commit();
//		    		    		
//		    		    		DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
//		    		    		ret = 0;//back to normal case		    		    		
//		    		    	}else{
//		    		    		conn.commit();
//		    		    		//update the deliver
//		    		    		Deliver d = new Deliver();
//		    					d.setID(deliver.getID());
//		    					d.setBrand(deliver.getBrand());
//		    					d.setSubBrand("");
//		    					d.setSize("");
//		    					d.setUnit(deliver.getUnit());	
//		    					d.setNumber(deliver.getNumber());//initial it's empty, not null
//		    					d.setPrice(deliver.getPrice());
////		    					d.setOrderNumber(deliver.getOrderNumber());//?
//		    					DeliverCellModifier.getDeliverList().deliverChangedThree(d);		
//		    		    	}							
//						}
						else{
							conn.commit();
						}
						//change the product
						relatedProductChange(products_changed, true);
						
						}
//				}
				//if in edit mode, change the history
				if(DeliverUtils.getEditMode() && DeliverUtils.getStatus().equals("HISTORY") && ret == 0){
					DeliverUtils.updateHistory(deliverList);
				}
			}
			if(DeliverValidator.checkID(deliver.getID()) && DeliverValidator.rowLegal(conn, deliver) && DeliverValidator.rowComplete(deliver)){				
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
					int ret = deliverinfo.addDeliverInfo(conn, st, products_changed);
					//update indeed
					deliverinfo.updateDeliversIndeedByOrderNumber(conn, deliver.getOrderNumber(), "");
					
					if(ret == -1){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
	    		    	messageBox.setMessage(String.format("品牌:%s, 子品牌:%s, 规格:%s 的货品不存在仓库中， 点击确定将添加货品至仓库表中并继续发货，点击取消重新选择其他货品", 
	    		    			deliver.getBrand(), deliver.getSubBrand(), deliver.getSize()));		    		    	
	    		    	if (messageBox.open() == SWT.OK){   
	    		    		deliverinfo.insertGoodsAndUpdateDeliver(conn, deliver.getID(), st, products_changed);
	    		    		conn.commit();
//	    		    		DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
//	    					DeliverCellModifier.addNewTableRow(deliver);
	    		    	}else{
	    		    		conn.commit();
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
					}
//					else if(ret == -2){
//						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);
//	    		    	messageBox.setMessage(String.format("品牌:%s, 子品牌:%s, 规格:%s 的货品库存不足， 点击确定将添加货品至仓库表中并继续发货，点击取消重新选择其他货品或更新库存", 
//	    		    			deliver.getBrand(), deliver.getSubBrand(), deliver.getSize()));	    		    	
//	    		    	if (messageBox.open() == SWT.OK){   
//	    		    		deliverinfo.updateGoodsAndUpdateDeliver(conn, deliver.getID(), st);
//	    		    		DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
//	    					DeliverCellModifier.addNewTableRow(deliver);
//	    		    	}else{
//	    		    		Deliver d = new Deliver();
//	    					d.setID(deliver.getID());//in fact, the new row
//	    					d.setBrand("");
//	    					d.setSubBrand("");
//	    					d.setSize("");
//	    					d.setUnit("");	
//	    					d.setNumber("");//initial it's empty, not null
//	    					d.setPrice("");
//	    					d.setOrderNumber("");//?
//	    					deliverChangedThree(d);
//	    		    	}						
//					}
					else{//ret==0, normal case
					conn.commit();
					}
					relatedProductChange(products_changed, true);
					
					DeliverCellModifier.addNewTableRow(deliver);	
					}

			}
//			conn.commit();
			} catch (Exception e) {
				System.out.println("add deliver failed");
				conn.rollback();
				throw e;
			}
			finally{
				conn.close();
			}
			
			boolean updateSum = true;
			double total = 0.000;
			for (int i = 0; i < deliverList.size() - 1; i++) {
				Deliver stin = (Deliver) (deliverList.get(i));
				String price = stin.getPrice();
				String number = stin.getNumber();
				if(!(DeliverValidator.rowLegal2(stin) && DeliverValidator.rowComplete(stin))){
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
	 * @throws Exception 
	 */
	public static void removeCurrentHistory() throws Exception{
		ArrayList<Product> products = new ArrayList<Product>();
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}
		
		DeliverInfoService deliverinfo = new DeliverInfoService();		
		String order_num = DeliverUtils.getOrderNumber();				
		try {
			conn.setAutoCommit(false);
			products = deliverinfo.deleteDeliverAndCommonByOrderNumber(conn, order_num);
			conn.commit();
		} catch (Exception e) {
			System.out.println("delete deliver failed");
			conn.rollback();
			throw e;
		}finally{
			conn.close();
		}
		
		relatedProductChange(products, false);
		
		try {
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
		} catch (Exception e) {
			System.out.println("wrong");
			throw e;
		}
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
	
	public static void updateDeliversByOrderNumber(String ordernum, String indeed) throws Exception{
		
		Connection conn=null;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}
		
		try {
			conn.setAutoCommit(false);
			deliverinfo.updateDeliversIndeedByOrderNumber(conn, ordernum, indeed);
			conn.commit();
		} catch (Exception e) {
			System.out.println("update the indeed failed");
			conn.rollback();
		}finally{
			conn.close();
		}
		
		
	}
	
	/**
	 * remove the deliver items from database
	 * @param ordernum
	 */
	public static ArrayList<Product> deleteDeliversUseLess(String ordernum) throws Exception{		
//		int ret = 0;
		ArrayList<Product> products = new ArrayList<Product>();
		Connection conn=null;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return products;
		}
		
		try {
			conn.setAutoCommit(false);
			products = deliverinfo.deleteDeliversByOrderNumber(conn, ordernum);
			conn.commit();
		} catch (Exception e) {
			System.out.println("update the indeed failed");
			conn.rollback();
		}finally{
			conn.close();
		}
		return products;
	}
	
	public static void showHistoryTableValue(DeliverHistory history,
			ArrayList<Deliver> delivers, HashMap<String, String> kvs) {
		// clear the stocks first
		deliverList.clear();
		// show the editable button
		DeliverContentPart.makeHistoryEditable();
		// make the table & time picker enable = false
		DeliverContentPart.makeDisable();

		// show the clicked stock history
		String order = history.getOrderNumber();
		String time = history.getTime();
		String area = kvs.get("area");
		String name = kvs.get("name");
		String phone = kvs.get("phone");
		String addr = kvs.get("addr");
		String indeed = kvs.get("indeed");
		String total = kvs.get("total");

		DeliverCellModifier.getTableViewer().getTable().removeAll();
		// deliverList.clear();//clear first

		DeliverContentPart.setCommonInfo(area, name, phone, addr, order, time);
		for (int i = 0; i < delivers.size(); i++) {
			DeliverCellModifier.getDeliverList().addDeliver(delivers.get(i));
		}

		DeliverUtils.setOrderNumber(order);
		// add new line
		Deliver de = new Deliver(DeliverUtils.getNewLineID());
		DeliverCellModifier.getDeliverList().addDeliver(de);
		// refresh table
		Utils.refreshTable(DeliverCellModifier.getTableViewer().getTable());
		// show total
		DeliverContentPart.setTotal(df.format(Double.valueOf(total)));
		if (indeed.equals(""))
			DeliverContentPart.setIndeed(df.format(Double.valueOf(total)));
		else
			DeliverContentPart.setIndeed(indeed);
	}
	
	/**
	 * if the user double click the history, show it
	 * @param history
	 * @throws SQLException 
	 */
	public static void showHistory(DeliverHistory history, ArrayList<Deliver> delivers, 
			HashMap<String, String> kvs) throws SQLException{
		//clear the stocks first
		String time = history.getTime();
		//query database to get the history and addStock
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("deliver_time", time);

		
		Connection conn=null;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}
		double total = 0.0;
		double p = 0.0;
		int n=0;
		try {
			conn.setAutoCommit(false);
			ReturnObject ret = deliverinfo.queryDeliverInfo(conn, map);
			conn.commit();
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			String indeed = "";
			String ordernum = "";
			for(int i=0;i<list.size();i++){				
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				if(i==0){
					String order = cDTO.getOrder_num();
					String timein = cDTO.getDeliver_time();
					String area = cDTO.getCustomer_area();
					String name = cDTO.getCustomer_name();
					String phone = cDTO.getTelephone();
					String addr = cDTO.getDeliver_addr();	
					kvs.put("order", order);
					kvs.put("time", timein);
					kvs.put("area", area);
					kvs.put("name", name);
					kvs.put("phone", phone);
					kvs.put("addr", addr);
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
				delivers.add(de_tmp);
				
				indeed = cDTO.getUni_reserve1();
				if(indeed == null)
					indeed = "";
				kvs.put("indeed", indeed);
				
				p = Double.valueOf(cDTO.getUnit_price());
				n = Integer.valueOf(cDTO.getQuantity());
				total+=(p * n);		
			}
			kvs.put("total", String.valueOf(total));
			
		} catch (Exception e) {
			System.out.println("query the deliver by time failed");
			conn.rollback();
		}finally{
			conn.close();
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
