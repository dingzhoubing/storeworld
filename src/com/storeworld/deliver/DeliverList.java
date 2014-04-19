package com.storeworld.deliver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.pojo.dto.DeliverInfoAllDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.stock.Stock;
import com.storeworld.utils.DataCachePool;
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
	
	public DeliverList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
		
		//order number 
		//it's wrong to get the id in this way!!!
		String newID = "";
		try {
			newID = String.valueOf(deliverinfo.getNextDeliverID());
//			ReturnObject ret = deliverinfo.queryDeliverInfoAll();
//			Pagination page = (Pagination) ret.getReturnDTO();
//			List<Object> list = page.getItems();
//			for(int i=0;i<list.size();i++){
//				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
//				newID = cDTO.getUni_id();//if this the id?
//
//			}
		} catch (Exception e) {
			System.out.println("get the current id of deliver info failed");
		}
		//no record
		if(newID.equals("-1"))
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
				deliverinfo.deleteDeliverInfo(Integer.valueOf(deliver.getID()));
			} catch (Exception e) {
				System.out.println("remove the deliver failed");
			}
		}
	}

	public void deliverChangedForUnit(Deliver deliver){
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(deliver);
		}
	}
	
	/**
	 * update the deliver data from table & database
	 * @param deliver
	 */
	public void deliverChanged(Deliver deliver) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(deliver);
			Map<String, Object> common = new HashMap<String ,Object>();
			Map<String, Object> st = new HashMap<String ,Object>();
			st.put("id", deliver.getID());
			st.put("brand", deliver.getBrand());
			st.put("sub_brand", deliver.getSubBrand());
			st.put("standard", deliver.getSize());
			st.put("unit", deliver.getUnit());
			
			if(!deliver.getPrice().equals(""))
				st.put("unit_price", String.valueOf(deliver.getPrice()));
			else
				st.put("unit_price", String.valueOf("0.0"));
			if(!deliver.getNumber().equals(""))
				st.put("quantity", String.valueOf(deliver.getNumber()));
			else
				st.put("quantity", String.valueOf("0"));
			
			if(!DeliverValidator.checkID(deliver.getID())){
				//update the database here				
				try {
					deliverinfo.updateDeliverInfo(deliver.getID(), common, st);
					//if the brand, sub are new, we update the product cache !!
					//if area, name are new, update the customer cache(if user click print button) !!
				} catch (Exception e) {
					System.out.println("update deliver failed");
				}
			}
			if(DeliverValidator.checkID(deliver.getID()) && DeliverValidator.rowLegal(deliver) && DeliverValidator.rowComplete(deliver)){				
				try {
					deliverinfo.addDeliverInfo(common, st);
					//add a new brand->sub in cache
					DataCachePool.addBrand2Sub(deliver.getBrand(), deliver.getSubBrand());
					//if also add into the product table, we should update the productlist
					//every time we enter the product/customer page, we should refresh the table
					//to show the data from stock, deliver page
					DeliverCellModifier.addNewTableRow(deliver);
				} catch (Exception e) {
					System.out.println("add deliver failed");
				}
			}
		}
	}

	/**
	 * remove all the delivers from the table, not the database
	 * if the user click add a new deliver table, we initialize the table
	 */
	public static void removeAllDelivers(){
		//the new item
//		Deliver st = new Deliver(DeliverValidator.getNewID());
		Deliver st = new Deliver(DeliverUtils.getNewLineID());
		deliverList.clear();
		//add a new line, do not wat the addDeliver method to become a static
		DeliverCellModifier.getDeliverList().addDeliver(st);
		//refresh the table to show color
		Utils.refreshTable(DeliverCellModifier.getTableViewer().getTable());
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
