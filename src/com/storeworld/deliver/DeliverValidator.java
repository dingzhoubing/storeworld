package com.storeworld.deliver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.Connection;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.GoodsInfoService;

public class DeliverValidator {

	private static Pattern pattern_brand = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
	private static Pattern pattern_size = Pattern.compile("^\\d+[\\u4E00-\\u9FA5\\uF900-\\uFA2DA-Za-z]*$");
	private static Pattern pattern_unit = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]{1,5}$");
	private static Pattern pattern_price = Pattern.compile("^([1-9][0-9]*(\\.[0-9]{1,2})?|0\\.(?!0+$)[0-9]{1,2})$");
	private static Pattern pattern_number = Pattern.compile("^([1-9][0-9]*)$");
	private static Pattern pattern_name = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{1,5}$");
	private static Pattern pattern_area = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]+$");
	
	private static GoodsInfoService goodsinfo = new GoodsInfoService();
	
	/**
	 * validate the brand of the stock
	 * @param brand
	 * @return
	 */
	public static boolean validateBrand(String brand){//Table table, TableItem item, int col, 
		
		Matcher matcher = pattern_brand.matcher(brand); 		
		if(brand != null && !brand.equals("") && matcher.matches()){
			return true;
		}
		else{
			return false;
		}
	}
		
	/**
	 * validate the sub_brand of the stock
	 * @param sub_brand
	 * @return
	 */
	public static boolean validateSub_Brand(String sub_brand){
		
		Matcher matcher = pattern_brand.matcher(sub_brand); 		
		if(sub_brand != null && !sub_brand.equals("") && matcher.matches()){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * validate the size of the stock
	 * @param size
	 * @return
	 */
	public static boolean validateSize(String size){
		
		Matcher matcher = pattern_size.matcher(size);		
		if(size != null && !size.equals("") && matcher.matches()){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * validate the unit of the stock
	 * @param unit
	 * @return
	 */
	public static boolean validateUnit(String unit){

		Matcher matcher = pattern_unit.matcher(unit); 		
		if(unit != null && !unit.equals("") && matcher.matches()){
			return true;
		}
		else{
			return false;
		}
	}
		
	public static boolean validatePrice(String price){
		
		Matcher matcher = pattern_price.matcher(price); 		
		if(price != null && !price.equals("") && matcher.matches()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean validateNumber(String number){
		
		Matcher matcher = pattern_number.matcher(number); 		
		if(number != null && !number.equals("") && matcher.matches()){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * check if the current edit row is the new row
	 * @param id
	 * @return
	 */
	public static boolean checkID(String id){
		if(id.equals(DeliverUtils.getNewLineID()))
			return true;
		else
			return false;
	}
	/**
	 * check if the current row is legal
	 * @param p
	 * @return
	 * @throws Exception 
	 */
	public static boolean rowLegal(Connection conn, Deliver p) throws Exception{
		if(p.getBrand() !=null && p.getSubBrand()!=null){
			
			if(!p.getBrand().equals("") && !p.getSubBrand().equals("")){// &&!p.getSize().equals("")

//				String sizeDefault = "";
//				String unitDefault = "";
				if(DeliverUtils.getSizeUnitChanged()){//!p.getUnit().equals("") || !p.getSize().equals("")
//					already has one unit
				}else{
				//set the unit
				Map<String, Object> prod = new HashMap<String, Object>();
				prod.put("brand", p.getBrand());
				prod.put("sub_brand", p.getSubBrand());
//				prod.put("standard", p.getSize());
				ReturnObject ret;
				try {
					ret = goodsinfo.queryGoodsInfo(conn, prod);
					Pagination page = (Pagination) ret.getReturnDTO();
					List<Object> list = page.getItems();
					if(list.size() > 0){
						GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(0);
						p.setSize(cDTO.getStandard());
						p.setUnit(cDTO.getUnit());
//						sizeDefault = cDTO.getStandard();
//						unitDefault = cDTO.getUnit();
					}else{
						p.setSize("");
						p.setUnit("");
						
					}							
				} catch (Exception e) {
					throw e;
				}
				
				DeliverCellModifier.getDeliverList().deliverChangedForUnit(p);
				}
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * validate if price and number not empty
	 * @param p
	 * @return
	 */
	public static boolean rowComplete(Deliver p){
		if(!p.getPrice().equals("") && !p.getNumber().equals("")){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static boolean rowLegal2(Deliver p){
		if(!p.getBrand().equals("") && !p.getSubBrand().equals("")){
			return true;
		}else{
			return false;
		}
		
	}
	public static boolean validateName(String name){ 
		Matcher matcher = pattern_name.matcher(name); 
		if(name != null && !name.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	
	public static boolean validateArea(String area){ 
		Matcher matcher = pattern_area.matcher(area); 
		if(area != null && !area.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
}
