package com.storeworld.stock;

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
import com.storeworld.database.BaseAction;
import com.storeworld.login.EntryPoint;
import com.storeworld.login.RestartSoftware;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.product.Product;
import com.storeworld.product.ProductCellModifier;
import com.storeworld.product.ProductUtils;
import com.storeworld.pub.service.GoodsInfoService;
import com.storeworld.pub.service.StockInfoService;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.Utils;

/**
 * get the data of the stock table
 * connect database
 * @author dingyuanxiong
 *
 */
public class StockList {
	
	private static ArrayList<DataInTable> stockList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();
	//stock related database operations
	private static final StockInfoService stockinfo = new StockInfoService();
	private static boolean isFirst = true;
	private static DecimalFormat df = new DecimalFormat("0.00");
	private static BaseAction baseAction = new BaseAction();
	private static ArrayList<Product> product_changed = new ArrayList<Product>();
	
	public StockList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
		//get the new line id in database
		String newID = "";
		try {
			newID = String.valueOf(stockinfo.getNextStockID());
//			throw new Exception("1");
		} catch (Exception e) {
//			System.out.println("failed");
//			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
//			mbox.setMessage("查询进货信息失败");//ok, cancel, if ok, restart?
//			mbox.open();
//			return;
			MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK);						
	    	messageBox.setMessage("初始化数据库失败，请重新启动"); 	
	    	if (messageBox.open() == SWT.OK){	    			    	
	    		MainUI.getMainUI_Instance(Display.getDefault()).dispose();
	    		System.exit(0);
	    		return;
	    	}
//	    	return;
		}
		
		// if no record, new id is 1
		if(newID.equals("-1") || newID.equals(""))
			newID="1";//empty
		//by the list of Customer from database
		StockUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)));					
		//if the brand2sub not cached, query the database
		try {
			DataCachePool.cacheProductInfo();
		} catch (Exception e) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("缓存货品信息失败");
			mbox.open();
		}
		
	}
	
	/**
	 * get all the stocks in stock table
	 * @return
	 */
	public static ArrayList<DataInTable> getStocks() {
		return stockList;
	}	
	
	public void basicAddStock(Stock stock) {

		stockList.add(stock);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){		
			(iterator.next()).add(stock);
		}
	}
	
	/**
	 * add a stock in table UI
	 * @param stock
	 * @throws Exception 
	 */
	public void addStock(Stock stock) throws Exception {

		
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){

//			//if in edit mode, change the history
//			if(StockUtils.getEditMode() && StockUtils.getStatus().equals("HISTORY")){
//				Connection conn;
//				try {
//					conn = baseAction.getConnection();
//				} catch (Exception e1) {
//					throw e1;
//				}
//				
//				try {
//					conn.setAutoCommit(false);
//					Stock tmp = (Stock)stockList.get(0);
//					String time_tmp = tmp.getTime();//the new line may have no time
//					stockinfo.updateStocksIndeedByTime(conn, time_tmp, "");
//					conn.commit();
//				} catch (Exception e) {
//					conn.rollback();
//					System.out.println("add stock failed");
//					throw e;
//				}finally{
//					conn.close();
//				}
//			}
			
			(iterator.next()).add(stock);
		}
		
		//if in edit mode, change the history
		if(StockUtils.getEditMode() && StockUtils.getStatus().equals("HISTORY")){
			StockUtils.updateHistory(stockList);
		}
		
		stockList.add(stock);
	}

	/**
	 * delete a stock in table & database
	 * @param stock
	 */
	public void removeStock(Stock stock) throws Exception{
		Product p_rec = null;
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			throw e1;
		}
		
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			
			try {
				conn.setAutoCommit(false);
				p_rec = stockinfo.deleteStockInfo(conn, stock.getID(), stock);
				
				if(StockUtils.getEditMode()){
//					StockUtils.updateHistory(stockList);
					String time_tmp = stock.getTime();
					stockinfo.updateStocksIndeedByTime(conn, time_tmp, "");
				}
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}finally{
				conn.close();
			}
			
			(iterator.next()).remove(stock);
		}	
		
		if (p_rec != null) {
			ProductCellModifier.getProductList().productChangedThree(p_rec);
			String brand = p_rec.getBrand();
			String sub = p_rec.getSubBrand();
			int repo = Integer.valueOf(p_rec.getRepository());
			if (repo < 0) {
				MessageBox mbox = new MessageBox(
						MainUI.getMainUI_Instance(Display.getDefault()));
				mbox.setMessage("商品  " + brand + ":" + sub + " 库存小于零，请查看并更新");
				mbox.open();
			}
		}
		
		//update the history panel
		stockList.remove(stock);
		//if in edit mode, change the history
		StockUtils.updateHistory(stockList);
		
		//update the sum panel value, total & indeed
		double total = 0.00;
		for(int i=0;i<stockList.size()-1;i++){
			Stock st = (Stock)(stockList.get(i));
			String price = st.getPrice();
			String number = st.getNumber();
			double p = Double.valueOf(price);
			int n = Integer.valueOf(number);
			total+=(p * n);	
		}
		StockContentPart.setTotal(df.format(total));
		StockContentPart.setIndeed(df.format(total));				
	}
	
	/**
	 * remove current history from database & navigator
	 * @throws SQLException 
	 */
	public static void removeCurrentHistory() throws Exception{
		StockInfoService stockinfo = new StockInfoService();
		//get the current timer value
		String time_current = StockContentPart.getStockTimer();
		List<String> listid = new ArrayList<String>();
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		ArrayList<Product> impacted = new ArrayList<Product>();
		Connection conn;
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
			//get all the stock
			ReturnObject ret = stockinfo.queryStockInfo(conn, time_current);
			
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();			
			for(int i=0;i<list.size();i++){
				StockInfoDTO cDTO = (StockInfoDTO) list.get(i);
				listid.add(cDTO.getId());
				Stock s = new Stock();
				s.setBrand(cDTO.getBrand());
				s.setSubBrand(cDTO.getSub_brand());
				s.setSize(cDTO.getStandard());
				s.setNumber(cDTO.getQuantity());
				stocks.add(s);
			}
			
			impacted = stockinfo.batchDeleteStockInfo(conn, listid, stocks);

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			System.out.println("in remove Currenthistory, batch delete stocks failed");	
			throw new SQLException("数据库删除失败");//throw it again
		}finally{
			conn.close();
		}
		
		//update the UI shows in product table
		String info = "";
		int count=0;
		for(int i=0;i<impacted.size();i++){
			Product tmp_p = impacted.get(i);
			ProductCellModifier.getProductList().productChangedThree(tmp_p);
			String brand = tmp_p.getBrand();
			String sub = tmp_p.getSubBrand();
			int repo = Integer.valueOf(tmp_p.getRepository());
			//the repository contains negative numbers
			//only record 3 product cases
			if(repo < 0){
				count++;	
				if(count<=3){
					if(i<impacted.size()-1){
						info+=(brand+":"+sub+", ");
					}else{
						info+=(brand+":"+sub);
					}
				}
			}
		}
		
		if(!info.equals("")){
			if(count<=3){
				MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
				mbox.setMessage("商品  " + info + " 库存小于零，请查看并更新");
				mbox.open();
			}else{//>3
				MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
				mbox.setMessage(info + " 等商品库存小于零，请查看并更新");
				mbox.open();
			}
		}
		
	}
	
	/**
	 * remove all the stocks in table when user click button to add a new stock table
	 * @throws Exception 
	 */
	public static void removeAllStocks(){
		//the new item
		Stock st = new Stock(StockUtils.getNewLineID());
		ArrayList<DataInTable> backup = new ArrayList<DataInTable>();
		backup.addAll(stockList);
		
		stockList.clear();
		//add a new line
		StockCellModifier.getStockList().basicAddStock(st);

		//refresh the table to show color
		Utils.refreshTable(StockCellModifier.getTableViewer().getTable());
	}

	
	
	
	public static void showHistoryTableValue(StockHistory history, ArrayList<Stock> stock_input, double total){
		
		String time = history.getTime();
		//clear the stocks first
		stockList.clear();
		//show the editable button
		StockContentPart.makeHistoryEditable();
		//make the table & time picker enable = false
		StockContentPart.makeDisable();				
		//show the clicked stock history

		StockUtils.setTime(time);
		String indeed = history.getIndeed();

		//remove the items
		StockCellModifier.getTableViewer().getTable().removeAll();
//		stockList.clear();//clear first
		
		for(int i=0;i<stock_input.size();i++){
			StockCellModifier.getStockList().basicAddStock(stock_input.get(i));
		}
		
		//add new line
		Stock st = new Stock(StockUtils.getNewLineID());
		StockCellModifier.getStockList().basicAddStock(st);
		
		
		//refresh table
		Utils.refreshTable(StockCellModifier.getTableViewer().getTable());
		
		
		//show time of the stock
		int year = Integer.valueOf(time.substring(0, 4));
		int month = Integer.valueOf(time.substring(4, 6));
		int day = Integer.valueOf(time.substring(6, 8));
		int hour = Integer.valueOf(time.substring(8, 10));
		int min = Integer.valueOf(time.substring(10, 12));
		int sec = Integer.valueOf(time.substring(12, 14));
		StockContentPart.setStockTimer(year, month-1, day, hour, min, sec);//month-1, be care
		
		//show total, if indeed is the empty, means indeed==total
		StockContentPart.setTotal(df.format(total));
		if(indeed.equals(""))
			StockContentPart.setIndeed(df.format(total));
		else
			StockContentPart.setIndeed(indeed);
	}
	/**
	 * if user want to see the detail of a specified history item
	 * double click to show the history in stock table
	 * 1. query the detail by the history time
	 * 2. add the detail info into the table stocklist, auto show it in table
	 * 3. refresh the table
	 * @param history
	 * @throws SQLException 
	 */
	public static double showHistory(StockHistory history, ArrayList<Stock> stock_input) throws Exception{
		
		double total = 0.00;
		String time = history.getTime();
//		ArrayList<Stock> stock_input = new ArrayList<Stock>();

		Connection conn = null;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			throw e1;
		}
		
		try {
			conn.setAutoCommit(false);
			ReturnObject ret = stockinfo.queryStockInfo(conn, time);
			conn.commit();
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();			
			for(int i=0;i<list.size();i++){
				StockInfoDTO cDTO = (StockInfoDTO) list.get(i);
				Stock st_tmp = new Stock();
				st_tmp.setID(cDTO.getId());
				st_tmp.setBrand(cDTO.getBrand());
				st_tmp.setSubBrand(cDTO.getSub_brand());
				st_tmp.setSize(cDTO.getStandard());		
				st_tmp.setUnit(cDTO.getUnit());
				st_tmp.setPrice(String.valueOf(cDTO.getUnit_price()));
				st_tmp.setNumber(cDTO.getQuantity());
				st_tmp.setTime(cDTO.getStock_time());	
				stock_input.add(st_tmp);
				double p = Double.valueOf(cDTO.getUnit_price());
				int n = Integer.valueOf(cDTO.getQuantity());
				total+=(p * n);		
			}
		
		} catch (Exception e) {
			conn.rollback();
			System.out.println("query the stock by time failed");
			throw e;
		}finally{
			conn.close();
		}

		return total;
	}
	
	/**
	 * in fact, stock changed for size & unit
	 * when users fill in the brand and sub, we automaticly fill in the size and unit
	 * @param stock
	 */
	public void stockChangedForUnit(Stock stock){
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(stock);
		}
	}

	/**
	 * check if the current stock is duplicated with the exist stock items
	 * @param stock
	 * @return
	 */
	private boolean checkSameStock(Stock stock){
		boolean ret = false;
		for(int i=0;i<stockList.size()-1;i++){
			Stock stmp = (Stock)stockList.get(i);
			//a different stock
			if(!stmp.getID().equals(stock.getID())){
				//now, only detect brand & sub brand
				if(stock.getBrand().equals(stmp.getBrand()) && stock.getSubBrand().equals(stmp.getSubBrand())){//&&stock.getSize().equals(stmp.getSize())
					ret = true;
					break;
				}				
			}			
		}		
		return ret;
	}
	
	/**
	 * changed the stock item only in UI side
	 * @param stock
	 */
	public void stockChangedThree(Stock stock){
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(stock);	
			
			for(int i=0;i<stockList.size();i++){
				Stock s = (Stock)(stockList.get(i));
				if(s.getID().equals(stock.getID())){
					s.setBrand(stock.getBrand());
					s.setSubBrand(stock.getSubBrand());
					s.setSize(stock.getSize());
					s.setUnit(stock.getUnit());
					s.setPrice(stock.getPrice());
					s.setNumber(stock.getNumber());
					//time and indeed?
					break;
				}				
			}			
			StockUtils.refreshTableData();
			
		}
	}
	
	private void relatedProductChange(){
		String info = "";
		String current = ProductUtils.getNewLineID();
		int dup = 0;
		for(Product p : product_changed){//update product table UI
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
		if(!info.equals("")){
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("商品 "+info+" 库存小于零，请检查库存表并更新");
			mbox.open();
		}
//		return info;
	}
	
	/**
	 * update the stock table
	 * @param stock
	 * @throws Exception 
	 */
	public void stockChanged(Stock stock) throws Exception {
		
		int caseIn = 0;
		product_changed.clear();
		
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			throw e1;
		}
		
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			//do the change at the last
			(iterator.next()).update(stock);
			Map<String, Object> st = new HashMap<String ,Object>();
			st.put("id", stock.getID());
			st.put("brand", stock.getBrand());
			st.put("sub_brand", stock.getSubBrand());
			st.put("standard", stock.getSize());
			st.put("unit", stock.getUnit());
			if(!stock.getPrice().equals(""))
				st.put("unit_price", Double.valueOf(stock.getPrice()));
			else
				st.put("unit_price", Double.valueOf("0.0"));
			if(!stock.getNumber().equals(""))
				st.put("quantity", Integer.valueOf(stock.getNumber()));
			else
				st.put("quantity", Integer.valueOf("0"));
			//blank table
			//then, add a stock, we initialize the time, if not blank table, we use the same time
			//every time we click "add a new", we set the time ""
			//but: if in editable mode, the time is just from the time picker
			if(StockUtils.getEditMode()){
				st.put("stock_time", StockContentPart.getStockTimer());
			}else{
				if(!StockUtils.getTime().equals("")){
					st.put("stock_time", StockUtils.getTime());
				}else{
					StockUtils.setTime(null);
					st.put("stock_time", StockUtils.getTime());
				}
			}
			//update the time of the stock in table(even cannot see)
			stock.setTime(String.valueOf(st.get("stock_time")));
			
			
			try{
				conn.setAutoCommit(false);

				//change the values of table items not the new row
				if(!StockValidator.checkID(stock.getID()) && StockValidator.rowLegal(conn, stock) 
					&& StockValidator.rowComplete(stock) ){			
					//update the database here					
					int ret = 0;
					//if exist the same stock item(brand & sub) in UI side, this stock table, and reasonable
					if(checkSameStock(stock)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的货品在该进货表中，请重新选择！"));		    		    	
	    		    	//we clear the already set value for this stock item
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Stock s = new Stock();
	    					s.setID(stock.getID());//new row in fact
	    					s.setBrand(stock.getBrand());
	    					s.setSubBrand("");
	    					s.setSize("");
	    					s.setUnit(stock.getUnit());	
	    					s.setNumber(stock.getNumber());//initial it's empty, not null
	    					s.setPrice(stock.getPrice());
//	    					s.setTime(time);time and indeed?
	    					StockCellModifier.getStockList().stockChangedThree(s);
	    					ret = -1;//in this way, we do not update history
	    		    	}
					}else{
						
						boolean exist = DataCachePool.ifExistBrandSub(stock.getBrand(), stock.getSubBrand());
						
						stockinfo.updateStocksIndeedByTime(conn, stock.getTime(), "");//anyway, we change the indeed
						
						//if exist the same product in the product table
						//we may not need to query the database, right?
						if(exist){
							caseIn = 1;
							
							product_changed = stockinfo.updateStockInfo(conn, stock.getID(), st);
							
							conn.commit();
							
							relatedProductChange();		
							
							//cache old
						}else{
							//if there is no such product in product table, we try to add it
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);						
		    		    	messageBox.setMessage(String.format("库存中不存在品牌:%s，子品牌:%s 的商品，确定要加入库存吗？点击确定继续，点击取消重新选择", 
		    		    			stock.getBrand(), stock.getSubBrand()));//， 规格:%s , stock.getSize() 		    		    	
		    		    	if (messageBox.open() == SWT.OK){
		    		    		caseIn = 2;
		    		    		//if user want to update the product table
		    		    		product_changed = stockinfo.updateStockInfo(conn, stock.getID(), st);
		    		    		conn.commit();
		    		    		
		    		    		relatedProductChange();	
																
		    		    	}else{
		    		    		//if user do not want to update the product table, we clear the already input value
		    		    		Stock s = new Stock();
		    					s.setID(stock.getID());//new row in fact
		    					s.setBrand(stock.getBrand());
		    					s.setSubBrand("");
		    					s.setSize("");
		    					s.setUnit(stock.getUnit());	
		    					s.setNumber(stock.getNumber());
		    					s.setPrice(stock.getPrice());
//		    					s.setTime(time);time and indeed?
		    					StockCellModifier.getStockList().stockChangedThree(s);
		    					ret = -1;//in this way, we do not update history
		    		    	}							
						}
					}					
					//if in edit mode, change the history, and ret==0 means all actions succeed 
					if(StockUtils.getEditMode() && StockUtils.getStatus().equals("HISTORY") && ret ==0){
						StockUtils.updateHistory(stockList);
					}
				}
				
				//if the new row of the data table
				if(StockValidator.checkID(stock.getID()) && StockValidator.rowLegal(conn, stock) && StockValidator.rowComplete(stock)){				
					//exist the same stock in this table
					if(checkSameStock(stock)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的货品在该进货表中，请重新选择！"));		    		    	
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Stock s = new Stock();
	    					s.setID(stock.getID());//new row in fact
	    					s.setBrand(stock.getBrand());
	    					s.setSubBrand("");
	    					s.setSize("");
	    					s.setUnit(stock.getUnit());	
	    					s.setNumber(stock.getNumber());//initial it's empty, not null
	    					s.setPrice(stock.getPrice());
	    					StockCellModifier.getStockList().stockChangedThree(s);		    		    		
	    		    	}						
					}else{//do not have such product in this table
						
						boolean exist = DataCachePool.ifExistBrandSub(stock.getBrand(), stock.getSubBrand());
						stockinfo.updateStocksIndeedByTime(conn, stock.getTime(), "");//anyway, we change the indeed
						
						//if exist such product in product table
						if(exist){
							caseIn = 3;
							product_changed = stockinfo.addStockInfo(conn, st);
							conn.commit();
							
							relatedProductChange();	
							
							StockCellModifier.addNewTableRow(stock);
							
						}else{
							//if not exist the same product, we info the user to add the new product
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);						
		    		    	messageBox.setMessage(String.format("库存中不存在品牌:%s，子品牌:%s， 的商品，确定要加入库存吗？点击确定继续，点击取消重新选择", 
		    		    			stock.getBrand(), stock.getSubBrand()));//规格:%s , stock.getSize() 		    		    	
		    		    	if (messageBox.open() == SWT.OK){
		    		    		caseIn = 4;
								product_changed = stockinfo.addStockInfo(conn, st);
								conn.commit();
								
								relatedProductChange();	
								
								StockCellModifier.addNewTableRow(stock);
								
		    		    	}else{
		    		    		Stock s = new Stock();
		    					s.setID(stock.getID());//new row in fact
		    					s.setBrand(stock.getBrand());
		    					s.setSubBrand("");
		    					s.setSize("");
		    					s.setUnit(stock.getUnit());	
		    					s.setNumber(stock.getNumber());//initial it's empty, not null
		    					s.setPrice(stock.getPrice());
//		    					s.setTime(time);time and indeed?
		    					StockCellModifier.getStockList().stockChangedThree(s);		    					
		    		    	}		
						}
					}
				}
//				conn.commit();
			}catch(Exception e){
				conn.rollback();
				throw e;
			}finally{
				conn.close();			
			}
			
				//update sum
				boolean updateSum = true;
				double total = 0.00;
				for(int i=0;i<stockList.size()-1;i++){
					Stock stin = (Stock)(stockList.get(i));
					String price = stin.getPrice();
					String number = stin.getNumber();
					if(!(StockValidator.rowLegal2(stin) && StockValidator.rowComplete(stin))){
						updateSum = false;
						break;
					}	
					double p = Double.valueOf(price);
					int n = Integer.valueOf(number);
					total+=(p * n);						
				}
				if(updateSum){
					StockContentPart.setTotal(df.format(total));
					StockContentPart.setIndeed(df.format(total));
				}

			}
	}
	
	/**
	 * update the stock items by time if the users changed the indeed value not the same as total
	 * @param time
	 * @param indeed
	 * @throws Exception 
	 */
	public static void updateStocksByTime(String time, String indeed) throws Exception{
		Connection conn=null;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			throw e1;
		}
		
		try {
			conn.setAutoCommit(false);			
			stockinfo.updateStocksIndeedByTime(conn, time, indeed);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			System.out.println("update the indeed failed");
			throw e;
		}finally{
			conn.close();
		}
		
		
	}
	

	/**
	 * if we change the timer, update table&database stock time
	 * @throws Exception 
	 */
	public static void changeStocksTime() throws Exception{
		String time = StockContentPart.getStockTimer();		
		for(int i=0; i<stockList.size()-1; i++){
			Stock st = (Stock)(stockList.get(i));
			st.setTime(time);
			try {
				StockCellModifier.getStockList().stockChanged(st);
			} catch (Exception e) {
				throw e;
			}
		}
	}

	/**
	 * may multi content provider
	 * @param viewer
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}

	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return stockList.toString();
	}
	
}
