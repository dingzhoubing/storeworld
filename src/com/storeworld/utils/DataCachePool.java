package com.storeworld.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.CustomerInfoService;
import com.storeworld.pub.service.GoodsInfoService;

/**
 * Whether need to cache or NOT???
 * @author dingyuanxiong
 * Using this class to cache the data from Database, which will not change a lot
 * Using this to speedup the process of actions
 */
public class DataCachePool {
	
	//cache the brand 2 sub mapping
	private static HashMap<String, HashSet<String>> brand2sub = new HashMap<String, HashSet<String>>();	
	//cache the customer area 2 names mapping
	private static HashMap<String, HashSet<String>> area2names = new HashMap<String, HashSet<String>>();
	//whether to cache the <area, name> --> <phone, address> mapping ??
	
	/**
	 * cache the brand -> sub_brand
	 * @param brand
	 * @param sub_brand
	 */
	public static void addBrand2Sub(String brand, String sub_brand){
		//before adding into database, we check the validation
		if(brand2sub.keySet().contains(brand)){
			if(!brand2sub.get(brand).contains(sub_brand)){
				brand2sub.get(brand).add(sub_brand);
			}
		}else{//do not contain this brand
			HashSet<String> sub_tmp = new HashSet<String>();
			sub_tmp.add(sub_brand);
			brand2sub.put(brand, sub_tmp);
		}
	}
	
	/**
	 * cache the area -> names
	 * @param area
	 * @param name
	 */
	public static void addArea2Names(String area, String name){
		//before adding into database, we check the validation
		if(area2names.keySet().contains(area)){
			if(!area2names.get(area).contains(name)){
				area2names.get(area).add(name);
			}
		}else{//do not contain this brand
			HashSet<String> sub_tmp = new HashSet<String>();
			sub_tmp.add(name);
			area2names.put(area, sub_tmp);
		}
	}
	
	
	
	/**
	 * if a customer has been deleted, update the cache
	 * @param area
	 * @param name
	 */
	public static void removeCustomerInfoOfCache(String area, String name){
		if(area2names.keySet().contains(area)){
			if(area2names.get(area).contains(name)){
				area2names.get(area).remove(name);
			}
			//if only one name in this area, after name removed, remove area
			if(area2names.get(area).isEmpty())
				area2names.remove(area);
		}
	}
	
	/**
	 * if a product has been deleted, update the cache
	 * @param brand
	 * @param sub_brand
	 */
	public static void removeProductInfoOfCache(String brand, String sub_brand){
		if(brand2sub.keySet().contains(brand)){
			if(brand2sub.get(brand).contains(sub_brand)){
				brand2sub.get(brand).remove(sub_brand);
			}
			if(brand2sub.get(brand).isEmpty())
				brand2sub.remove(brand);
		}
	}
	
	/**
	 * update he customer, remove the old info, add the new info
	 * @param oldarea
	 * @param oldname
	 * @param newarea
	 * @param newname
	 */
	public static void updateCustomerInfoOfCache(String oldarea, String oldname, String newarea, String newname){
		removeCustomerInfoOfCache(oldarea, oldname);
		addArea2Names(newarea, newname);
	}
	
	public static void updateProductInfoOfCache(String oldbrand, String oldsub, String newbrand, String newsub){
		removeProductInfoOfCache(oldbrand, oldsub);
		addBrand2Sub(newbrand, newsub);
	}
		
	/**
	 * get the brand2sub mapping
	 * @return
	 */
	public static HashMap<String, HashSet<String>> getBrand2Sub(){
		return brand2sub;
	}
	
	/**
	 * get the area2names mapping
	 * @return
	 */
	public static HashMap<String, HashSet<String>> getArea2Names(){
		return area2names;
	}
	
	/**
	 * if brand2sub is empty, not cached, else, cached, and there is no need to query the database 
	 * once again
	 * @return
	 */
	public static boolean isBrand2SubCached(){
		return !brand2sub.isEmpty();
	}
	
	/**
	 * if the area2names is empty,  not cached, else, cached, and there is no need to query the database 
	 * once again
	 * @return
	 */
	public static boolean isArea2NamesCached(){
		return !area2names.isEmpty();
	}
	
	/**
	 * cache the product info if haven't yet(brand -> sub_brand)
	 */
	public static void cacheProductInfo(){
		if(!isBrand2SubCached()){
			try {
				GoodsInfoService goodsinfo = new GoodsInfoService();
				ReturnObject ret = goodsinfo.queryGoodsInfoAll();
				Pagination page = (Pagination) ret.getReturnDTO();
				List<Object> list = page.getItems();
				for(int i=0;i<list.size();i++){
					GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(i);
					addBrand2Sub(cDTO.getBrand(), cDTO.getSub_brand());					
				}
			} catch (Exception e) {
				System.out.println("cache the brand2sub failed");
			}
		}
	}
	
	
	/**
	 * cache the customer info if haven't yet
	 */
	public static void cacheCustomerInfo(){
		if(!isArea2NamesCached()){
			try {	
				CustomerInfoService cusinfo = new CustomerInfoService();			
				ReturnObject ret = cusinfo.queryCustomerInfoAll();
				Pagination page = (Pagination) ret.getReturnDTO();
				List<Object> list = page.getItems();
				for(int i=0;i<list.size();i++){
					CustomerInfoDTO cDTO = (CustomerInfoDTO) list.get(i);
					addArea2Names(cDTO.getCustomer_area(), cDTO.getCustomer_name());
				}
			} catch (Exception e) {
				System.out.println("get customer areas in deliver page failed");
			}	
		}
	}
	
	/**
	 * get the customer area set from cache
	 */
	public static String[] getCustomerAreas(){
		//cache the data if haven't yet
		cacheCustomerInfo();
		//if not cached, cache the customer data
		//after cached, directly get the data
		//if() need to check the area2names??
		HashSet<String> areas = new HashSet<String>();
		areas.addAll(area2names.keySet());
		return areas.toArray(new String[areas.size()]);
	}
	
	/**
	 * get the customer names by a given area from the cache
	 * @param area
	 * @return
	 */
	public static String[] getCustomerNames(String area){
		cacheCustomerInfo();
		
		HashSet<String> names = new HashSet<String>();
		if(!area.equals(""))
			names.addAll(area2names.get(area));
		else{//area is ""
			for(String ar : area2names.keySet()){
				names.addAll(area2names.get(ar));
			}
		}
		return names.toArray(new String[names.size()]);
	}
	
}
