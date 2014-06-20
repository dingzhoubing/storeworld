package com.storeworld.stock;

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
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.pub.service.GoodsInfoService;
import com.storeworld.pub.service.StockInfoService;
import com.storeworld.utils.DataCachePool;
import com.storeworld.utils.ItemComposite;
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
	private static final StockInfoService stockinfo = new StockInfoService();
	private static boolean isFirst = true;
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	public StockList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		

		//at initial, the newLine is the latest number of the stock in database
		//??quick query for the last one?
		String newID = "";
		try {
//			ReturnObject ret = stockinfo.queryStockInfoAll();
			newID = String.valueOf(stockinfo.getNextStockID());
//			Pagination page = (Pagination) ret.getReturnDTO();
//			List<Object> list = page.getItems();
//			for(int i=0;i<list.size();i++){
//				StockInfoDTO cDTO = (StockInfoDTO) list.get(i);
//				newID = cDTO.getId();						
//			}
		} catch (Exception e) {
			System.out.println("failed");
		}
		//no record
		if(newID.equals("-1") || newID.equals(""))
			newID="1";//empty
		//by the list of Customer from database
		StockUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)));
		
		//if the brand2sub not cached, query the database
		DataCachePool.cacheProductInfo();
		
	}
	
	/**
	 * get all the stocks
	 * @return
	 */
	public static ArrayList<DataInTable> getStocks() {
		return stockList;
	}
	
	
	
	
	/**
	 * add a stock in table UI
	 * @param stock
	 */
	public void addStock(Stock stock) {
		stockList.add(stock);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).add(stock);
//			if(isFirst){
//				StockUtils.refreshTableData();
//				isFirst = false;
//			}
			//if in edit mode, change the history
			if(StockUtils.getEditMode() && StockUtils.getStatus().equals("HISTORY")){
				StockUtils.updateHistory(stockList);
			}
		}
	}

	/**
	 * delete a stock in table & database
	 * @param stock
	 */
	public void removeStock(Stock stock) {
		stockList.remove(stock);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).remove(stock);
			try {
				stockinfo.deleteStockInfo(stock.getID(), stock);
			} catch (Exception e) {
				System.out.println("remove the stock failed");
			}
			//if in edit mode, change the history
			if(StockUtils.getEditMode()){
				StockUtils.updateHistory(stockList);
			}
		}
		
		//only complete record can used to compute the total value of this stock ??
		double total = 0.00;
//		boolean has = false;
		for(int i=0;i<stockList.size()-1;i++){
			Stock st = (Stock)(stockList.get(i));
			String price = st.getPrice();
			String number = st.getNumber();
			double p = Double.valueOf(price);
			int n = Integer.valueOf(number);
			total+=(p * n);	
//			has = true;
		}
		StockContentPart.setTotal(df.format(total));
		StockContentPart.setIndeed(df.format(total));
		
		
	}
	
	/**
	 * remove current history from database & navigator
	 */
	public static void removeCurrentHistory(){
		StockInfoService stockinfo = new StockInfoService();
		String time_current = StockContentPart.getStockTimer();
		List<String> listid = new ArrayList<String>();
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("stock_time", time_current);
		
		try {
			ReturnObject ret = stockinfo.queryStockInfo(map);
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();			
			for(int i=0;i<list.size();i++){
				StockInfoDTO cDTO = (StockInfoDTO) list.get(i);
				listid.add(cDTO.getId());
				Stock s = new Stock();
				//three is enough
				s.setBrand(cDTO.getBrand());
				s.setSubBrand(cDTO.getSub_brand());
				s.setSize(cDTO.getStandard());
				s.setNumber(cDTO.getQuantity());
				stocks.add(s);
			}
		} catch (Exception e) {
			System.out.println("in remove Currenthistory, query stocks by time failed");			
		}
		
		try {
			//delete the stocks from database
			stockinfo.batchDeleteStockInfo(listid, stocks);
		} catch (Exception e) {
			System.out.println("in remove Currenthistory, batch delete stocks failed");	
		}
		
		//remove history & item composite
		ItemComposite itemCurrent = StockUtils.getItemCompositeRecord();
		ArrayList<ItemComposite> itemlist = StockUtils.getItemList();
		ArrayList<StockHistory> hislist = StockUtils.getHistoryList();
		StockHistory his = (StockHistory)itemCurrent.getHistory();
		hislist.remove(his);//remove the StockHistory
		itemlist.remove(itemCurrent);
		itemCurrent.dispose();
		//layout the item list
		StockUtils.layoutItemList();
	}
	/**
	 * remove all the stocks in table when user click button to add a new stock table
	 */
	public static void removeAllStocks(){
		//the new item
//		Stock st = new Stock(StockValidator.getNewID());
		Stock st = new Stock(StockUtils.getNewLineID());
		stockList.clear();
		//add a new line
		StockCellModifier.getStockList().addStock(st);
		//refresh the table to show color
		Utils.refreshTable(StockCellModifier.getTableViewer().getTable());
	}

	/**
	 * if user want to see the detail of a specified history item
	 * double click to show the history in stock table
	 * 1. query the detail by the history time
	 * 2. add the detail info into the table stocklist, auto show it in table
	 * 3. refresh the table
	 * @param history
	 */
	public static void showHistory(StockHistory history){
		//clear the stocks first
		stockList.clear();
		//show the editable button
		StockContentPart.makeHistoryEditable();
		//make the table & time picker enable = false
		StockContentPart.makeDisable();		
		
		//show the clicked stock history
		String time = history.getTime();
		StockUtils.setTime(time);
		String indeed = history.getIndeed();
		//query database to get the history and addStock
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("stock_time", time);
		double total = 0.00;
		try {
			//remove the items
			StockCellModifier.getTableViewer().getTable().removeAll();
			stockList.clear();//clear first
			ReturnObject ret = stockinfo.queryStockInfo(map);
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
				//...now in test, only show these three property
				StockCellModifier.getStockList().addStock(st_tmp);
				double p = Double.valueOf(cDTO.getUnit_price());
				int n = Integer.valueOf(cDTO.getQuantity());
				total+=(p * n);		
			}
			//add new line
//			Stock st = new Stock(StockValidator.getNewID());
			Stock st = new Stock(StockUtils.getNewLineID());
			StockCellModifier.getStockList().addStock(st);
			//refresh table
			Utils.refreshTable(StockCellModifier.getTableViewer().getTable());
			
		} catch (Exception e) {
			System.out.println("query the stock by time failed");
		}
		//show time of the stock
		int year = Integer.valueOf(time.substring(0, 4));
		int month = Integer.valueOf(time.substring(4, 6));
		int day = Integer.valueOf(time.substring(6, 8));
		int hour = Integer.valueOf(time.substring(8, 10));
		int min = Integer.valueOf(time.substring(10, 12));
		int sec = Integer.valueOf(time.substring(12, 14));
		StockContentPart.setStockTimer(year, month-1, day, hour, min, sec);//month-1, be care
		
		//show total
		StockContentPart.setTotal(df.format(total));
		if(indeed.equals(""))
			StockContentPart.setIndeed(df.format(total));
		else
			StockContentPart.setIndeed(indeed);

	}
	
	public void stockChangedForUnit(Stock stock){
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(stock);
		}
	}

	private boolean checkSameStock(Stock stock){
		boolean ret = false;
		for(int i=0;i<stockList.size()-1;i++){
			Stock stmp = (Stock)stockList.get(i);
			//a different stock
			if(!stmp.getID().equals(stock.getID())){
				if(stock.getBrand().equals(stmp.getBrand()) && stock.getSubBrand().equals(stmp.getSubBrand()) 
						&&stock.getSize().equals(stmp.getSize())){
					ret = true;
					break;
				}				
			}			
		}		
		return ret;
	}
	
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
	
	/**
	 * update the stock table
	 * @param stock
	 */
	public void stockChanged(Stock stock) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
//			(iterator.next()).update(stock);
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
					st.put("stock_time", StockUtils.getTime());//time, when to count the time
				}else{
					StockUtils.setTime(null);
					st.put("stock_time", StockUtils.getTime());//time, when to count the time
				}
			}
			//update the time of the stock in table(even cannot see)
			stock.setTime(String.valueOf(st.get("stock_time")));
			(iterator.next()).update(stock);
			
			if(!StockValidator.checkID(stock.getID()) && StockValidator.rowLegal(stock) && StockValidator.rowComplete(stock) ){//not the new line
				
				//update the database here					
				int ret = 0;
				try {
					if(checkSameStock(stock)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("������ͬ�Ļ�Ʒ�ڸý������У�������ѡ��"));		    		    	
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
						
						GoodsInfoService goodsinfo = new GoodsInfoService();
						boolean exist = goodsinfo.isExistGoodsInfo(st);
						if(exist){
							stockinfo.updateStockInfo(stock.getID(), st);
							DataCachePool.addBrand2Sub(stock.getBrand(), stock.getSubBrand());
						}else{
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);						
		    		    	messageBox.setMessage(String.format("����в�����Ʒ��:%s����Ʒ��:%s�� ���:%s ����Ʒ��ȷ��Ҫ�������𣿵��ȷ�����������ȡ������ѡ��", 
		    		    			stock.getBrand(), stock.getSubBrand(), stock.getSize()));		    		    	
		    		    	if (messageBox.open() == SWT.OK){
		    		    		stockinfo.updateStockInfo(stock.getID(), st);
								DataCachePool.addBrand2Sub(stock.getBrand(), stock.getSubBrand());
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
		    					ret = -1;//in this way, we do not update history
		    		    	}							
						}
					}					
					
					//if the brand, sub are new, we update the product cache !!
				} catch (Exception e) {
					//message box will do in the future
					System.out.println("update stock failed");
				}
				//if in edit mode, change the history
				if(StockUtils.getEditMode() && StockUtils.getStatus().equals("HISTORY") && ret ==0){
					StockUtils.updateHistory(stockList);
				}
			}
			if(StockValidator.checkID(stock.getID()) && StockValidator.rowLegal(stock) && StockValidator.rowComplete(stock)){				
				try {
					//exist the same stock in this table
					if(checkSameStock(stock)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("������ͬ�Ļ�Ʒ�ڸý������У�������ѡ��"));		    		    	
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
						

    		    		GoodsInfoService goodsinfo = new GoodsInfoService();
						boolean exist = goodsinfo.isExistGoodsInfo(st);
						
						if(exist){
							int ret = stockinfo.addStockInfo(st);//???a mass of the return, need to fix
							if(ret == -1 || ret == -2){
								//insert into stock info failed						
								MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
								mbox.setMessage("���ݿ����ʧ��");
								mbox.open();	
							}else{//successful
								//need to update the product table
								//insert into the cache anyway
								DataCachePool.addBrand2Sub(stock.getBrand(), stock.getSubBrand());
								//create a new line
								StockCellModifier.addNewTableRow(stock);							
							}
						}else{
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);						
		    		    	messageBox.setMessage(String.format("����в�����Ʒ��:%s����Ʒ��:%s�� ���:%s ����Ʒ��ȷ��Ҫ�������𣿵��ȷ�����������ȡ������ѡ��", 
		    		    			stock.getBrand(), stock.getSubBrand(), stock.getSize()));		    		    	
		    		    	if (messageBox.open() == SWT.OK){
		    		    		int ret = stockinfo.addStockInfo(st);//
								if(ret == -1 || ret == -2){
									//insert into stock info failed						
									MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
									mbox.setMessage("���ݿ����ʧ��");
									mbox.open();	
								}else{//successful
									//need to update the product table
									//insert into the cache anyway
									DataCachePool.addBrand2Sub(stock.getBrand(), stock.getSubBrand());
									//create a new line
									StockCellModifier.addNewTableRow(stock);							
								}
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
				} catch (Exception e) {
					MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
					mbox.setMessage("����ʧ��");
					mbox.open();	
					
				}
//				//if in edit mode, change the history
//				if(StockUtils.getEditMode()){
//					StockUtils.updateHistory(stockList);
//				}
				boolean updateSum = true;
				double total = 0.00;
				for(int i=0;i<stockList.size()-1;i++){
					Stock stin = (Stock)(stockList.get(i));
					String price = stin.getPrice();
					String number = stin.getNumber();
					if(!(StockValidator.rowLegal(stin) && StockValidator.rowComplete(stin))){
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

		
	}
	
	public static void updateStocksByTime(String time, String indeed){
		
		try {
			stockinfo.updateStocksIndeedByTime(time, indeed);
		} catch (Exception e) {
			System.out.println("update the indeed failed");
		}
		
		
	}
	
	
	
	
	/**
	 * if we change the timer, update table&database stock time
	 */
	public static void changeStocksTime(){
		String time = StockContentPart.getStockTimer();		
		for(int i=0; i<stockList.size()-1; i++){
			Stock st = (Stock)(stockList.get(i));
			st.setTime(time);
			StockCellModifier.getStockList().stockChanged(st);
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
