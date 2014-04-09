package com.storeworld.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.storeworld.common.DataInTable;
import com.storeworld.common.History;
import com.storeworld.common.IDataListViewer;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.pub.service.GoodsInfoService;
import com.storeworld.pub.service.StockInfoService;
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
	
	public StockList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
//		String brand = "五得利";
//		String subbrand = "精一";
//		String size = "50kg";
//		String unit = "包";
//		String price = "50.0";
//		String number = "30";
//		Stock stock = new Stock("1",brand, subbrand, size, unit, price, number);
//		stockList.add(stock);
//		
//		String brand2 = "五得利";
//		String subbrand2 = "特精";
//		String size2 = "50kg";
//		String unit2 = "包";
//		String price2 = "75.0";
//		String number2 = "40";
//		Stock stock2 = new Stock("2",brand2, subbrand2, size2, unit2, price2, number2);
//		stockList.add(stock2);
//		
//		String brand3 = "五联";
//		String subbrand3 = "包子粉";
//		String size3 = "50kg";
//		String unit3= "包";
//		String price3 = "72.0";
//		String number3 = "20";
//		Stock stock3 = new Stock("3", brand3, subbrand3, size3, unit3, price3, number3);
//		stockList.add(stock3);	
	
		//at initial, the newLine is the latest number of the stock in database
		//??quick query for the last one?
		String newID = "";
		try {
			ReturnObject ret = stockinfo.queryStockInfoAll();
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			for(int i=0;i<list.size();i++){
				StockInfoDTO cDTO = (StockInfoDTO) list.get(i);
				newID = cDTO.getId();		
//				String brand = cDTO.getBrand();
//				String sub_brand = cDTO.getSub_brand();
//				StockUtils.addBrand2Sub(brand, sub_brand);
			}
		} catch (Exception e) {
			System.out.println("failed");
		}
		//no record
		if(newID.equals(""))
			newID="0";//empty
		//by the list of Customer from database
		StockUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)+1));
		
		try {
			GoodsInfoService goodsinfo = new GoodsInfoService();
			ReturnObject ret = goodsinfo.queryGoodsInfoAll();
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			for(int i=0;i<list.size();i++){
				GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(i);
//				Product prod = new Product();				
//				prod.setBrand(cDTO.getBrand());
//				prod.setSubBrand(cDTO.getSub_brand());
				StockUtils.addBrand2Sub(cDTO.getBrand(), cDTO.getSub_brand());
			}
		} catch (Exception e) {
			System.out.println("failed");
		}
		
		
		
		
	}
	
	public static ArrayList<DataInTable> getStocks() {
		return stockList;
	}
	
	/**
	 * add a stock
	 */
	public void addStock(Stock stock) {
		stockList.add(stock);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).add(stock);
	}

	/**
	 * @param remove a stock
	 */
	public void removeStock(Stock stock) {
		stockList.remove(stock);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).remove(stock);
			try {
				stockinfo.deleteStockInfo(stock.getID());
			} catch (Exception e) {
				System.out.println("remove the stock failed");
			}
		}
	}
	
	public static void removeAllStocks(){
		//the new item
		Stock st = new Stock(StockValidator.getNewID());
		stockList.clear();
		//add a new line
		StockCellModifier.getStockList().addStock(st);
		//refresh the table to show color
		Utils.refreshTable(StockCellModifier.getTableViewer().getTable());
	}

	public static void showHistory(StockHistory history){
		String time = history.getTime();
		//query database to get the history and addStock
	}
	
	/**
	 * @param update a stock
	 */
	public void stockChanged(Stock stock) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
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
			if(!StockUtils.getTime().equals("")){
				st.put("stock_time", StockUtils.getTime());//time, when to count the time
			}
			else{
				StockUtils.setTime(null);
				st.put("stock_time", StockUtils.getTime());//time, when to count the time
			}
			if(!StockValidator.checkID(stock.getID())){
				//update the database here				
				try {
					stockinfo.updateStockInfo(stock.getID(), st);
				} catch (Exception e) {
					System.out.println("update stock failed");
				}
			}
			if(StockValidator.checkID(stock.getID()) && StockValidator.rowLegal(stock)){				
				try {
					stockinfo.addStockInfo(st);
					StockUtils.addBrand2Sub(stock.getBrand(), stock.getSubBrand());
					//if also add into the product table, we should update the productlist
					//every time we enter the product/customer page, we should refresh the table
					//to show the data from stock, deliver page
					StockCellModifier.addNewTableRow(stock);
				} catch (Exception e) {
					System.out.println("add stock failed");
				}
			}
		}
	}

	/**
	 * @param may multi contentprovider?， one remove
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param may multi contentprovider? one add
	 * viewer is a content provider
	 */
	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.stockList.toString();
	}
	
}
