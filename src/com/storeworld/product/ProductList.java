package com.storeworld.product;

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
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.extenddialog.ShowUpdateProgress;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.pub.service.GoodsInfoService;
import com.storeworld.pub.service.StockInfoService;
import com.storeworld.stock.StockContentPart;
import com.storeworld.utils.DataCachePool;

/**
 * get the data in product table
 * connect database
 * @author dingyuanxiong
 *
 */
public class ProductList {
	
	private ArrayList<DataInTable> productList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();
	private static final GoodsInfoService goodsinfo = new GoodsInfoService();
	
	public ProductList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
		
		String newID = "";
		try {
			ReturnObject ret = goodsinfo.queryGoodsInfoAll();
			newID = String.valueOf(goodsinfo.getNextGoodsID());
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			for(int i=0;i<list.size();i++){
				GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(i);
				Product prod = new Product();
//				newID = cDTO.getId();
				prod.setID(cDTO.getId());
				prod.setBrand(cDTO.getBrand());
				prod.setSubBrand(cDTO.getSub_brand());
				prod.setSize(cDTO.getStandard());
				if(cDTO.getUnit() == null)
					prod.setUnit("");
				else
					prod.setUnit(cDTO.getUnit());
				if(cDTO.getRepertory() == null)
					prod.setRepository("");
				else
					prod.setRepository(String.valueOf(cDTO.getRepertory()));
				
				productList.add(prod);
//				System.out.println("name: "+cDTO.getCustomer_name());
				//add to cache
				DataCachePool.addBrand2Sub(cDTO.getBrand(), cDTO.getSub_brand());
			}
		} catch (Exception e) {
			System.out.println("failed");
		}
		//no record
		if(newID.equals("-1") || newID.equals(""))
			newID="1";//empty
		//by the list of Customer from database
		ProductUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)));
		
	}
	
	/**
	 * get the products
	 * @return
	 */
	public ArrayList<DataInTable> getProducts() {
		return this.productList;
	}
	
	/**
	 * add a product into table (UI side)
	 * @param product
	 */
	public void addProduct(Product product) {
		this.productList.add(product);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).add(product);
	}

	/**
	 * delete a product UI table & database
	 * @param product
	 */
	public void removeProduct(Product product) {
		this.productList.remove(product);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).remove(product);
			try {
				goodsinfo.deleteGoodsInfo(product.getID());
			} catch (Exception e) {
				System.out.println("remove product failed");
			}
			//update the cache
			DataCachePool.removeProductInfoOfCache(product.getBrand(), product.getSubBrand());
			ProductUtils.refreshBrands();
		}
	}

	public void productChangedThree(Product product) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(product);	
			
			for(int i=0;i<productList.size();i++){
				Product p = (Product)(productList.get(i));
				if(p.getID().equals(product.getID())){
					p.setBrand(product.getBrand());
					p.setSubBrand(product.getSubBrand());
					p.setSize(product.getSize());
					p.setUnit(product.getUnit());
					p.setRepository(product.getRepository());
					break;
				}				
			}
			
			ProductUtils.refreshTableData();
			
		}
	}
	
	public void productChangedTwo(Product product) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(product);
			Product p = (Product)(productList.get(productList.size()-1));
			p.setBrand(product.getBrand());
			p.setSubBrand(product.getSubBrand());
			p.setSize(product.getSize());
			p.setUnit(product.getUnit());
			p.setRepository(product.getRepository());
			
			ProductCellModifier.addNewTableRow(product);
			
			DataCachePool.addBrand2Sub(product.getBrand(), product.getSubBrand());
			
			ProductUtils.refreshBrands();
			
			ProductUtils.refreshTableData();
			
		}
	}
	
	private boolean checkSameProduct(Product product){
		boolean ret = false;
		for(int i=0;i<productList.size()-1;i++){
			Product stmp = (Product)productList.get(i);
			//a different stock
			if(!stmp.getID().equals(product.getID())){
				if(product.getBrand().equals(stmp.getBrand()) && product.getSubBrand().equals(stmp.getSubBrand()) 
						&&product.getSize().equals(stmp.getSize())){
					ret = true;
					break;
				}				
			}			
		}		
		return ret;
	}
	
	
	
	/**
	 * update the product table in UI & database
	 * @param product
	 */
	public void productChanged(Product product) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(product);
			//not the new row, we update, or we do not update, just update he table
			Map<String, Object> prod = new HashMap<String ,Object>();
			prod.put("id", product.getID());
			prod.put("brand", product.getBrand());
			prod.put("sub_brand", product.getSubBrand());
			prod.put("standard", product.getSize());
			prod.put("unit", product.getUnit());
			prod.put("repertory", product.getRepository());
			if(!ProductValidator.checkID(product.getID()) && ProductValidator.rowLegal(product)){
				//update the database here				
				try {

					//check if exist the same product
					if(checkSameProduct(product)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的货品在库存表中，请重新选择！"));		    		    	
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Product s = new Product();
	    					s.setID(product.getID());//new row in fact
	    					s.setBrand(product.getBrand());
	    					s.setSubBrand("");
	    					s.setSize("");
	    					s.setUnit(product.getUnit());	
	    					s.setRepository(product.getRepository());
//	    					s.setTime(time);time and indeed?
	    					ProductCellModifier.getProductList().productChangedThree(s);
	    		    	}
						
					}else{
					
						//before update the database, record the old brand/sub, for updating the cache
						Map<String, Object> prod_old = new HashMap<String, Object>();
						prod_old.put("id", product.getID());
						ReturnObject ret = goodsinfo.queryGoodsInfo(prod_old);
						Pagination page = (Pagination) ret.getReturnDTO();
						List<Object> list = page.getItems();
						String old_brand="";
						String old_sub="";
						String old_size = "";
						String old_unit = "";
						Map<String, Object> prod_rec = new HashMap<String ,Object>();
						//it should contains only one element, or something wrong
						if(!list.isEmpty()){
							GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(0);
							old_brand = cDTO.getBrand();
							old_sub = cDTO.getSub_brand();
							old_size = cDTO.getStandard();
							old_unit = cDTO.getUnit();
							prod_rec.put("brand", old_brand);
							prod_rec.put("sub_brand", old_sub);
							prod_rec.put("standard", old_size);
							prod_rec.put("unit", old_unit);
						}else{
							System.out.println("query a product with an exist ID returns empty");
						}
						
						if(product.getBrand().equals(old_brand) && product.getSubBrand().equals(old_sub) && product.getSize().equals(old_size)
								&&product.getUnit().equals(old_unit)){
							//update the repository, just update it
							goodsinfo.updateGoodsInfo(product.getID(), prod);
							DataCachePool.updateProductInfoOfCache(old_brand, old_sub, product.getBrand(), product.getSubBrand());					
							ProductUtils.refreshBrands();
						}else{//change other property
							MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);						
		    		    	messageBox.setMessage(String.format("品牌:%s，子品牌:%s，规格%s 的商品将被修改，进货与送货表中将会发生相应更新，确认要操作吗？", old_brand, old_sub, old_size));		    		    	
		    		    	if (messageBox.open() == SWT.OK){	
		    		    		
//		    		    		ShowUpdateProgress sp = new ShowUpdateProgress(MainUI.getMainUI_Instance(Display.getDefault()), 0);
//		    		    		Thread th = new Thread(sp);
//		    		    		th.start();
		    		    		
		    		    		goodsinfo.updateGoodsInfo(product.getID(), prod);
								//update the cache
								//based on: the product/customer page will not often be changed
								DataCachePool.updateProductInfoOfCache(old_brand, old_sub, product.getBrand(), product.getSubBrand());					
								ProductUtils.refreshBrands();
//								sp.setStatus("更新库存表", 5);
								
								//update all the stock info and deliver info, a long time maybe
								//update all stock info and current stock table
								StockInfoService stockinfo = new StockInfoService();
								stockinfo.updateAllStockInfoForProductChanged(prod, prod_rec);
								
//								sp.setStatus("更新进货表", 25);
								
								StockContentPart.reNewStock();
								StockContentPart.reNewStockHistory();
//								sp.setStatus("更新进货界面", 55);

								//update all deliver info and current deliver table
								DeliverInfoService deliverinfo = new DeliverInfoService();
								deliverinfo.updateAllDeliverInfoForProductChanged(prod, prod_rec);
//								sp.setStatus("更新送货表", 75);
								
								DeliverContentPart.reNewDeliver();
								DeliverContentPart.reNewDeliverHistory();
//								sp.setStatus("更新进货界面", 100);
								
//								th.destroy();
								
		    		    	}else{//back to the same
		    		    		Product s = new Product();
		    					s.setID(product.getID());//new row in fact
		    					s.setBrand(old_brand);
		    					s.setSubBrand(old_sub);
		    					s.setSize(old_size);
		    					s.setUnit(old_unit);	
		    					s.setRepository(product.getRepository());
//		    					s.setTime(time);time and indeed?
		    					ProductCellModifier.getProductList().productChangedThree(s);
		    		    	}
						
						}
					}
				} catch (Exception e) {
					System.out.println("update product failed");
				}
			}
			if(ProductValidator.checkID(product.getID()) && ProductValidator.rowLegal(product)){				
				try {
					
					if(checkSameProduct(product)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的货品在库存表中，请重新选择！"));		    		    	
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Product s = new Product();
	    					s.setID(product.getID());//new row in fact
	    					s.setBrand(product.getBrand());
	    					s.setSubBrand("");
	    					s.setSize("");
	    					s.setUnit(product.getUnit());	
	    					s.setRepository(product.getRepository());
//	    					s.setTime(time);time and indeed?
	    					ProductCellModifier.getProductList().productChangedThree(s);
	    		    	}						
					}else{					
						goodsinfo.addGoodsInfo(prod);
						ProductCellModifier.addNewTableRow(product);					
						DataCachePool.addBrand2Sub(product.getBrand(), product.getSubBrand());					
						ProductUtils.refreshBrands();
					}
				} catch (Exception e) {
					System.out.println("add product failed");
				}
			}
		}
	}

	/**
	 *  may multi content provider
	 * @param viewer
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}


	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.productList.toString();
	}
	
}
