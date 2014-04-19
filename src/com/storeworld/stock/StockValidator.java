package com.storeworld.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Text;

import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.GoodsInfoService;

public class StockValidator {
//	private static String current_id = "";
//	
//	public static void setNewID(String id){
//		current_id = id;
//	}
//	public static String getNewID(){
//		return current_id;
//	}
	
	private static Pattern pattern_brand = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
	//start with number, end with(out) Chinese character or 26 English character
	private static Pattern pattern_size = Pattern.compile("^\\d+[\\u4E00-\\u9FA5\\uF900-\\uFA2DA-Za-z]*$");
	private static Pattern pattern_unit = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]{1,5}$");
	private static Pattern pattern_price = Pattern.compile("\\d+|^\\d+.\\d{0,4}");
	private static Pattern pattern_number = Pattern.compile("\\d+");
	private static final int unitColumn = 4;
	
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
	public static boolean validateUnit(String unit){//Table table, TableItem item, int col,

		Matcher matcher = pattern_unit.matcher(unit); 
		
		if(unit != null && !unit.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	
	/**
	 * validate the price
	 * @param price
	 * @return
	 */
	public static boolean validatePrice(String price){//Table table, TableItem item, int col,
		
		Matcher matcher = pattern_price.matcher(price); 
		
		if(price != null && !price.equals("") && matcher.matches()){

			return true;
		}
		else{

			return false;
		}
	}
	
	/**
	 * validate the stock number
	 * @param number
	 * @return
	 */
	public static boolean validateNumber(String number){//Table table, TableItem item, int col,
		
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
//		if(id.equals(getNewID()))//dynamic to the newest ID
		if(id.equals(StockUtils.getNewLineID()))
			return true;
		else
			return false;
	}
	
	/**
	 * check if the current row is legal
	 * @param p
	 * @return
	 */
	public static boolean rowLegal(Stock p){
		if(p.getBrand() !=null && p.getSubBrand()!=null){
			
			if(!p.getBrand().equals("") && !p.getSubBrand().equals("") && !p.getSize().equals("")){

				//set the unit
				//here, only set once!!!
				Map<String, Object> prod = new HashMap<String, Object>();
				prod.put("brand", p.getBrand());
				prod.put("sub_brand", p.getSubBrand());
				prod.put("standard", p.getSize());
				ReturnObject ret;
				try {
					ret = goodsinfo.queryGoodsInfo(prod);
					Pagination page = (Pagination) ret.getReturnDTO();
					List<Object> list = page.getItems();
					if(list.size() > 0){
						GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(0);
						p.setUnit(cDTO.getUnit());
					}else{
						p.setUnit("");
					}							
				} catch (Exception e) {
					System.out.println("query the unit with brand&sub&size failed");
				}				
				StockCellModifier.getStockList().stockChangedForUnit(p);

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
	public static boolean rowComplete(Stock p){
		if(!p.getPrice().equals("") && !p.getNumber().equals("")){
			return true;
		}else{
			return false;
		}
		
	}
	
}
