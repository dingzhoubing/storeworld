package com.storeworld.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.customer.CustomerUtils;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.GoodsInfoService;
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
				prod.setUnit(cDTO.getUnit());
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
		if(newID.equals("-1"))
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
			if(!ProductValidator.checkID(product.getID())){
				//update the database here				
				try {
					//before update the database, record the old brand/sub, for updating the cache
					Map<String, Object> prod_old = new HashMap<String, Object>();
					prod_old.put("id", product.getID());
					ReturnObject ret = goodsinfo.queryGoodsInfo(prod_old);
					Pagination page = (Pagination) ret.getReturnDTO();
					List<Object> list = page.getItems();
					String old_brand="";
					String old_sub="";
					//it should contains only one element, or something wrong
					if(!list.isEmpty()){
						GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(0);
						old_brand = cDTO.getBrand();
						old_sub = cDTO.getSub_brand();
					}else{
						System.out.println("query a product with an exist ID returns empty");
					}
					
					goodsinfo.updateGoodsInfo(product.getID(), prod);
					//update the cache
					//based on: the product/customer page will not often be changed
					DataCachePool.updateProductInfoOfCache(old_brand, old_sub, product.getBrand(), product.getSubBrand());
					
					ProductUtils.refreshBrands();
				} catch (Exception e) {
					System.out.println("update product failed");
				}
			}
			if(ProductValidator.checkID(product.getID()) && ProductValidator.rowLegal(product)){				
				try {
					goodsinfo.addGoodsInfo(prod);
					ProductCellModifier.addNewTableRow(product);
					
					DataCachePool.addBrand2Sub(product.getBrand(), product.getSubBrand());
					
					ProductUtils.refreshBrands();
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
