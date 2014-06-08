package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.DeliverInfoAllDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockFactor;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.utils.Utils;
public class statistic extends BaseAction{
	
	public ReturnObject deliverQuantityAnalysis(Map<String,Object> params) throws Exception{
		int flag=0;
		String type=(String)params.get("type");
		ReturnObject ro=new ReturnObject();
		String brand=(String) params.get("brand");
		String sub_brand=(String) params.get("sub_brand");
		String customer_area=(String)  params.get("customer_area");
		String customer_name=(String) params.get("customer_name");
		String end_time=(String) params.get("end_time");
		int time_flag=(Integer) params.get("time_flag");//一个月or一个季度or一年or所有时间
		String start_time=calculateStartTimeByEndTime(end_time,time_flag);
		
		if(Utils.isNotNull(brand)&&Utils.isNotNull(sub_brand)&&!Utils.isNotNull(customer_area)&&!Utils.isNotNull(customer_name))
			flag=1;
		if(Utils.isNotNull(brand)&&Utils.isNotNull(sub_brand)&&Utils.isNotNull(customer_area)&&!Utils.isNotNull(customer_name))
			flag=2;
		if(Utils.isNotNull(brand)&&Utils.isNotNull(sub_brand)&&Utils.isNotNull(customer_area)&&Utils.isNotNull(customer_name))
			flag=3;
		if(!Utils.isNotNull(brand)&&!Utils.isNotNull(sub_brand)&&!Utils.isNotNull(customer_area)&&!Utils.isNotNull(customer_name))
			flag=4;
		if(!Utils.isNotNull(brand)&&!Utils.isNotNull(sub_brand)&&Utils.isNotNull(customer_area)&&!Utils.isNotNull(customer_name))
			flag=5;
		if(!Utils.isNotNull(brand)&&!Utils.isNotNull(sub_brand)&&Utils.isNotNull(customer_area)&&Utils.isNotNull(customer_name))
			flag=6;
		if(Utils.isNotNull(brand)&&!Utils.isNotNull(sub_brand)&&!Utils.isNotNull(customer_area)&&!Utils.isNotNull(customer_name))
			flag=7;
		if(Utils.isNotNull(brand)&&!Utils.isNotNull(sub_brand)&&Utils.isNotNull(customer_area)&&!Utils.isNotNull(customer_name))
			flag=8;
		if(Utils.isNotNull(brand)&&!Utils.isNotNull(sub_brand)&&Utils.isNotNull(customer_area)&&Utils.isNotNull(customer_name))
			flag=9;
		try{
			switch(flag){
				case 1:
					ro=func1(brand,sub_brand,start_time,end_time);
					break;
				case 2:
					ro=func2(brand,sub_brand,customer_area,start_time,end_time);
					break;
				case 3:
					ro=func3(brand,sub_brand,customer_area,customer_name,start_time,end_time);
					break;
				case 4:
					ro=func4(start_time,end_time);
					break;
				case 5:
					ro=func5(customer_area,start_time,end_time);
					break;
				case 6:
					ro=func6(customer_area,customer_name,start_time,end_time);
					break;
				case 7:
					ro=func7(brand,start_time,end_time);
					break;
				case 8:
					ro=func8(brand,customer_area,start_time,end_time);
					break;
				case 9:
					ro=func9(brand,customer_area,customer_name,start_time,end_time);
					break;
				default:
					break;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("执行出货量分析异常："+e.getMessage());
		}
		return ro;
	}
	/**
	 * description:片区与客户均为空，则只按片区的维度统计
	 * @param brand
	 * @param sub_brand
	 * @param start_time
	 * @param end_time
	 * @throws Exception 
	 */
	private ReturnObject func1(String brand,String sub_brand,String start_time,String end_time) throws Exception{
		List list=null;
		String sql_area="select dci.customer_area,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and di.sub_brand=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_area";
		
		Object[] params_temp={brand,sub_brand,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
			list=executeQuery(sql_area, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoAllList);
			ro.setReturnDTO(page);
			return ro;
	}
	
	/**
	 * description:客户未选，以客户为维度
	 * @param brand
	 * @param sub_brand
	 * @param customer_area
	 * @param start_time
	 * @param end_time
	 * @throws Exception 
	 */
	private ReturnObject func2(String brand,String sub_brand,String customer_area,String start_time,String end_time) throws Exception{
		List list=null;
		String sql_area="select dci.customer_name,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and di.sub_brand=? and dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={brand,sub_brand,customer_area,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
		list=executeQuery(sql_area, params);
		for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_name"));
				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				deliverInfoAllList.add(deliverInfoDto);
		}
		page.setItems((List)deliverInfoAllList);
		ro.setReturnDTO(page);
		return ro;
	}
	private ReturnObject func3(String brand,String sub_brand,String customer_area,String customer_name,String start_time,String end_time) throws Exception{
		List list=null;
		String sql_area="select dci.customer_name,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and di.sub_brand=? and dci.customer_area=? and dci.customer_name=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={brand,sub_brand,customer_area,customer_name,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
		list=executeQuery(sql_area, params);
		for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				deliverInfoAllList.add(deliverInfoDto);
		}
		page.setItems((List)deliverInfoAllList);
		ro.setReturnDTO(page);
		return ro;
	}
	/**
	 * description:品牌，子品牌，片区，客户均为空，统计的维度为品牌和片区
	 * @param start_time
	 * @param end_time
	 * @return
	 * @throws Exception
	 */
	private ReturnObject func4(String start_time,String end_time) throws Exception{
		List listBrand=null;
		List listArea=null;
		String sql_brand="select di.brand,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.brand";
		
		String sql_area="select dci.customer_area,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
				" where dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_area";
		
		Object[] params_temp={start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllListBrand = new ArrayList<DeliverInfoAllDTO>();
		List<DeliverInfoAllDTO> deliverInfoAllListArea = new ArrayList<DeliverInfoAllDTO>();
		//====================================================================//
		listBrand=executeQuery(sql_brand, params);
		for(int i=0;i<listBrand.size();i++){
				Map retMap=(Map) listBrand.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllListBrand.add(deliverInfoDto);
		}
		//====================================================================//
		listArea=executeQuery(sql_area, params);
		for(int i=0;i<listArea.size();i++){
				Map retMap=(Map) listArea.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllListArea.add(deliverInfoDto);
		}
		//========================================================================//
		page.setItems((List)deliverInfoAllListBrand);
		page.setOtherItems((List)deliverInfoAllListArea);
		ro.setReturnDTO(page);
		return ro;
	}
	
	private ReturnObject func5(String customer_area,String start_time,String end_time) throws Exception{
	
		List listBrand=null;
		List listName=null;
		String sql_brand="select di.brand,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.brand";
		
		String sql_name="select dci.customer_name,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
				" where dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={customer_area,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllListBrand = new ArrayList<DeliverInfoAllDTO>();
		List<DeliverInfoAllDTO> deliverInfoAllListName = new ArrayList<DeliverInfoAllDTO>();
		//====================================================================//
		listBrand=executeQuery(sql_brand, params);
		for(int i=0;i<listBrand.size();i++){
				Map retMap=(Map) listBrand.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllListBrand.add(deliverInfoDto);
		}
		//====================================================================//
		listName=executeQuery(sql_name, params);
		for(int i=0;i<listName.size();i++){
				Map retMap=(Map) listName.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_name"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllListName.add(deliverInfoDto);
		}
		//========================================================================//
		page.setItems((List)deliverInfoAllListBrand);
		page.setOtherItems((List)deliverInfoAllListName);
		ro.setReturnDTO(page);
		return ro;
	}
	private ReturnObject func6(String customer_area,String customer_name,String start_time,String end_time) throws Exception{
	
		List list=null;
		String sql_brand="select di.brand,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where dci.customer_area=? and dci.customer_name=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.brand";
		
		Object[] params_temp={customer_area,customer_name,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
			list=executeQuery(sql_brand, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoAllList);
			ro.setReturnDTO(page);
			return ro;
	}
	private ReturnObject func7(String brand,String start_time,String end_time) throws Exception{
		List listBrand=null;
		List listArea=null;
		String sql_brand="select di.sub_brand,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.sub_brand";
		
		String sql_area="select dci.customer_area,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
				" where di.brand=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_area";
		
		Object[] params_temp={brand,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllListBrand = new ArrayList<DeliverInfoAllDTO>();
		List<DeliverInfoAllDTO> deliverInfoAllListArea = new ArrayList<DeliverInfoAllDTO>();
		//====================================================================//
		listBrand=executeQuery(sql_brand, params);
		for(int i=0;i<listBrand.size();i++){
				Map retMap=(Map) listBrand.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllListBrand.add(deliverInfoDto);
		}
		//====================================================================//
		listArea=executeQuery(sql_area, params);
		for(int i=0;i<listArea.size();i++){
				Map retMap=(Map) listArea.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllListArea.add(deliverInfoDto);
		}
		//========================================================================//
		page.setItems((List)deliverInfoAllListBrand);
		page.setOtherItems((List)deliverInfoAllListArea);
		ro.setReturnDTO(page);
		return ro;
	}
	private ReturnObject func8(String brand,String customer_area,String start_time,String end_time) throws Exception{
		List listBrand=null;
		List listArea=null;
		String sql_brand="select di.sub_brand,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.sub_brand";
		
		String sql_area="select dci.customer_name,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={brand,customer_area,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllListBrand = new ArrayList<DeliverInfoAllDTO>();
		List<DeliverInfoAllDTO> deliverInfoAllListArea = new ArrayList<DeliverInfoAllDTO>();
		//====================================================================//
		listBrand=executeQuery(sql_brand, params);
		for(int i=0;i<listBrand.size();i++){
				Map retMap=(Map) listBrand.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllListBrand.add(deliverInfoDto);
		}
		//====================================================================//
		listArea=executeQuery(sql_area, params);
		for(int i=0;i<listArea.size();i++){
				Map retMap=(Map) listArea.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_name"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllListArea.add(deliverInfoDto);
		}
		//========================================================================//
		page.setItems((List)deliverInfoAllListBrand);
		page.setOtherItems((List)deliverInfoAllListArea);
		ro.setReturnDTO(page);
		return ro;
	}
	private ReturnObject func9(String brand,String customer_area,String customer_name,String start_time,String end_time) throws Exception{
		List list=null;
		String sql_brand="select di.sub_brand,sum(di.quantity) quantity from deliver_info di,deliver_common_info dci "+
	" where dci.customer_area=? and dci.customer_name=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.sub_brand";
		
		Object[] params_temp={brand,customer_area,customer_name,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
			list=executeQuery(sql_brand, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoAllList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoAllList);
			ro.setReturnDTO(page);
			return ro;
	}
	
	//=====================================利润统计===========================================//
	/**
	 * description:片区与客户均为空，则只按片区的维度统计
	 * @param brand
	 * @param sub_brand
	 * @param start_time
	 * @param end_time
	 * @throws Exception 
	 */
	private ReturnObject funcPro1(String brand,String sub_brand,String start_time,String end_time) throws Exception{
		/*List<StockInfoDTO> stockInfoList=new ArrayList<StockInfoDTO>();
		String sql_stock="select si.quantity batchQuantity,si.unit_price from stock_info si order by si.stock_time desc";
		List stock_list=null;
		stock_list=executeQuery(sql_stock, null);
		Map<Integer,Integer> stock_map=new HashMap<Integer,Integer>();
		Map<String,Float> result_profit=new HashMap<String,Float>();
		List<Map<String,Float>> resultList=null;
		for(int j=0;j<10;j++){
			StockInfoDTO stockInfoDto=new StockInfoDTO();
			Map retMap=(Map) stock_list.get(j);
			stockInfoDto.setQuantity(String.valueOf(retMap.get("batchQuantity")));
			stockInfoDto.setUnit_price((Float)retMap.get("unit_price"));
			stockInfoList.add(stockInfoDto);
			int num=(Integer)retMap.get("batchQuantity");
			stock_map.put(j,num);
		}*/
		List<Map<String,Float>> resultList=null;
		Map<String,Float> result_profit=new HashMap<String,Float>();
		StockFactor stockFactor=this.getStockFactor();
		List<StockInfoDTO> stockInfoList=stockFactor.getStockInfoList();
		Map<Integer,Integer> stock_map=stockFactor.getStock_map();
		String sql_area="select dci.customer_area,di.quantity,sum(di.quantity*di.unit_price) t_price from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and di.sub_brand=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num order by dci.deliver_time desc";
		//Float stock_price=queryStockPrice(end_time);
		Object[] params_temp={brand,sub_brand,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List list=null;
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
		list=executeQuery(sql_area, params);
		for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setTotal_price((Float)retMap.get("t_price"));
				float deliver_totol_price=deliverInfoDto.getTotal_price();
				int lsize=stockInfoList.size();
				float sum_stock_price=0;
				for(int k=0;k<lsize;k++){
					int batchStockNum=stock_map.get(k);
					if(batchStockNum>0){
						int deliverNum=Integer.parseInt(deliverInfoDto.getQuantity());
						if(deliverNum>batchStockNum){
							//如果跨了批次，需要把当前批次的数量与进价相乘，再继续算下一进货批次
							sum_stock_price+=stock_map.get(k)*stockInfoList.get(k).getUnit_price();
							stock_map.put(k, 0);
							stockInfoList.get(k).setQuantity("0");
							deliverInfoDto.setQuantity(String.valueOf(Integer.parseInt(deliverInfoDto.getQuantity())-batchStockNum));
							k++;
						}else{
							sum_stock_price+=deliverNum*stockInfoList.get(k).getUnit_price();
							stock_map.put(k, batchStockNum-deliverNum);
							int lastnum=Integer.parseInt(stockInfoList.get(k).getQuantity())-deliverNum;
							stockInfoList.get(k).setQuantity(String.valueOf(lastnum));
							break;
						}
					}
				}
				if(result_profit.get(deliverInfoDto.getCustomer_area())==null){
					result_profit.put(deliverInfoDto.getCustomer_area(), deliver_totol_price-sum_stock_price);
				}
				else{
					result_profit.put(deliverInfoDto.getCustomer_area(), deliver_totol_price+result_profit.get(deliverInfoDto.getCustomer_area())-sum_stock_price);
				}
				resultList.add(result_profit);			
		}
		page.setItems((List)resultList);
		ro.setReturnDTO(page);
		return ro;
	}
	
	/*private ReturnObject funcPro1(String brand,String sub_brand,String start_time,String end_time) throws Exception{
		List list=null;
		String sql_area="select dci.customer_area,sum(di.quantity) quantity,sum(di.quantity*di.unit_price) t_price,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and di.sub_brand=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_area";
		//Float stock_price=queryStockPrice(end_time);
		Object[] params_temp={brand,sub_brand,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
			list=executeQuery(sql_area, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllList.add(deliverInfoDto);
				
			}
			page.setItems((List)deliverInfoAllList);
			ro.setReturnDTO(page);
			return ro;
	}*/
	
	/**
	 * description:客户未选，以客户为维度
	 * @param brand
	 * @param sub_brand
	 * @param customer_area
	 * @param start_time
	 * @param end_time
	 * @throws Exception 
	 */
	private ReturnObject funcPro2(String brand,String sub_brand,String customer_area,String start_time,String end_time) throws Exception{
		List list=null;
		
		List<Map<String,Float>> resultList=null;
		Map<String,Float> result_profit=new HashMap<String,Float>();
		StockFactor stockFactor=this.getStockFactor();
		List<StockInfoDTO> stockInfoList=stockFactor.getStockInfoList();
		Map<Integer,Integer> stock_map=stockFactor.getStock_map();
		String sql_area="select dci.customer_name,di.quantity,sum(di.quantity*di.unit_price) t_price from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and di.sub_brand=? and dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num order by dci.deliver_time desc";
		
		//String sql_area="select dci.customer_name,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	//" where di.brand=? and di.sub_brand=? and dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={brand,sub_brand,customer_area,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
		list=executeQuery(sql_area, params);
		for(int i=0;i<list.size();i++){
				/*Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_name"));
				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllList.add(deliverInfoDto);*/
			
			Map retMap=(Map) list.get(i);
			DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
			deliverInfoDto.setCustomer_area((String)retMap.get("customer_name"));
			deliverInfoDto.setQuantity((String)retMap.get("quantity"));
			deliverInfoDto.setTotal_price((Float)retMap.get("t_price"));
			float deliver_totol_price=deliverInfoDto.getTotal_price();
			int lsize=stockInfoList.size();
			float sum_stock_price=0;
			for(int k=0;k<lsize;k++){
				int batchStockNum=stock_map.get(k);
				if(batchStockNum>0){
					int deliverNum=Integer.parseInt(deliverInfoDto.getQuantity());
					if(deliverNum>batchStockNum){
						//如果跨了批次，需要把当前批次的数量与进价相乘，再继续算下一进货批次
						sum_stock_price+=stock_map.get(k)*stockInfoList.get(k).getUnit_price();
						stock_map.put(k, 0);
						stockInfoList.get(k).setQuantity("0");
						deliverInfoDto.setQuantity(String.valueOf(Integer.parseInt(deliverInfoDto.getQuantity())-batchStockNum));
						k++;
					}else{
						sum_stock_price+=deliverNum*stockInfoList.get(k).getUnit_price();
						stock_map.put(k, batchStockNum-deliverNum);
						int lastnum=Integer.parseInt(stockInfoList.get(k).getQuantity())-deliverNum;
						stockInfoList.get(k).setQuantity(String.valueOf(lastnum));
						break;
					}
				}
			}
			if(result_profit.get(deliverInfoDto.getCustomer_name())==null){
				result_profit.put(deliverInfoDto.getCustomer_name(), deliver_totol_price-sum_stock_price);
			}
			else{
				result_profit.put(deliverInfoDto.getCustomer_area(), deliver_totol_price+result_profit.get(deliverInfoDto.getCustomer_name())-sum_stock_price);
			}
			resultList.add(result_profit);			
	}
	page.setItems((List)resultList);
	ro.setReturnDTO(page);
	return ro;
	
	
		/*}
		page.setItems((List)deliverInfoAllList);
		ro.setReturnDTO(page);
		return ro;*/
	}
	/*private ReturnObject funcPro2(String brand,String sub_brand,String customer_area,String start_time,String end_time) throws Exception{
		List list=null;
		String sql_area="select dci.customer_name,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and di.sub_brand=? and dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={brand,sub_brand,customer_area,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
		list=executeQuery(sql_area, params);
		for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_name"));
				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllList.add(deliverInfoDto);
		}
		page.setItems((List)deliverInfoAllList);
		ro.setReturnDTO(page);
		return ro;
	}*/
	private ReturnObject funcPro3(String brand,String sub_brand,String customer_area,String customer_name,String start_time,String end_time) throws Exception{
		List list=null;
		String sql_area="select dci.customer_name,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and di.sub_brand=? and dci.customer_area=? and dci.customer_name=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={brand,sub_brand,customer_area,customer_name,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
		list=executeQuery(sql_area, params);
		for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllList.add(deliverInfoDto);
		}
		page.setItems((List)deliverInfoAllList);
		ro.setReturnDTO(page);
		return ro;
	}
	/**
	 * description:品牌，子品牌，片区，客户均为空，统计的维度为品牌和片区
	 * @param start_time
	 * @param end_time
	 * @return
	 * @throws Exception
	 */
	private ReturnObject funcPro4(String start_time,String end_time) throws Exception{
		List listBrand=null;
		List listArea=null;
		String sql_brand="select di.brand,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	" where dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.brand";
		
		String sql_area="select dci.customer_area,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
				" where dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_area";
		
		Object[] params_temp={start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllListBrand = new ArrayList<DeliverInfoAllDTO>();
		List<DeliverInfoAllDTO> deliverInfoAllListArea = new ArrayList<DeliverInfoAllDTO>();
		//====================================================================//
		listBrand=executeQuery(sql_brand, params);
		for(int i=0;i<listBrand.size();i++){
				Map retMap=(Map) listBrand.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllListBrand.add(deliverInfoDto);
		}
		//====================================================================//
		listArea=executeQuery(sql_area, params);
		for(int i=0;i<listArea.size();i++){
				Map retMap=(Map) listArea.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllListArea.add(deliverInfoDto);
		}
		//========================================================================//
		page.setItems((List)deliverInfoAllListBrand);
		page.setOtherItems((List)deliverInfoAllListArea);
		ro.setReturnDTO(page);
		return ro;
	}
	
	private ReturnObject funcPro5(String customer_area,String start_time,String end_time) throws Exception{
	
		List listBrand=null;
		List listName=null;
		String sql_brand="select di.brand,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	" where dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.brand";
		
		String sql_name="select dci.customer_name,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
				" where dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={customer_area,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllListBrand = new ArrayList<DeliverInfoAllDTO>();
		List<DeliverInfoAllDTO> deliverInfoAllListName = new ArrayList<DeliverInfoAllDTO>();
		//====================================================================//
		listBrand=executeQuery(sql_brand, params);
		for(int i=0;i<listBrand.size();i++){
				Map retMap=(Map) listBrand.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllListBrand.add(deliverInfoDto);
		}
		//====================================================================//
		listName=executeQuery(sql_name, params);
		for(int i=0;i<listName.size();i++){
				Map retMap=(Map) listName.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_name"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllListName.add(deliverInfoDto);
		}
		//========================================================================//
		page.setItems((List)deliverInfoAllListBrand);
		page.setOtherItems((List)deliverInfoAllListName);
		ro.setReturnDTO(page);
		return ro;
	}
	private ReturnObject funcPro6(String customer_area,String customer_name,String start_time,String end_time) throws Exception{
	
		List list=null;
		String sql_brand="select di.brand,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price  from deliver_info di,deliver_common_info dci "+
	" where dci.customer_area=? and dci.customer_name=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.brand";
		
		Object[] params_temp={customer_area,customer_name,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
			list=executeQuery(sql_brand, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoAllList);
			ro.setReturnDTO(page);
			return ro;
	}
	private ReturnObject funcPro7(String brand,String start_time,String end_time) throws Exception{
		List listBrand=null;
		List listArea=null;
		String sql_brand="select di.sub_brand,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price   from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.sub_brand";
		
		String sql_area="select dci.customer_area,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price   from deliver_info di,deliver_common_info dci "+
				" where di.brand=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_area";
		
		Object[] params_temp={brand,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllListBrand = new ArrayList<DeliverInfoAllDTO>();
		List<DeliverInfoAllDTO> deliverInfoAllListArea = new ArrayList<DeliverInfoAllDTO>();
		//====================================================================//
		listBrand=executeQuery(sql_brand, params);
		for(int i=0;i<listBrand.size();i++){
				Map retMap=(Map) listBrand.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllListBrand.add(deliverInfoDto);
		}
		//====================================================================//
		listArea=executeQuery(sql_area, params);
		for(int i=0;i<listArea.size();i++){
				Map retMap=(Map) listArea.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllListArea.add(deliverInfoDto);
		}
		//========================================================================//
		page.setItems((List)deliverInfoAllListBrand);
		page.setOtherItems((List)deliverInfoAllListArea);
		ro.setReturnDTO(page);
		return ro;
	}
	private ReturnObject funcPro8(String brand,String customer_area,String start_time,String end_time) throws Exception{
		List listBrand=null;
		List listArea=null;
		String sql_brand="select di.sub_brand,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.sub_brand";
		
		String sql_area="select dci.customer_name,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	" where di.brand=? and dci.customer_area=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by dci.customer_name";
		
		Object[] params_temp={brand,customer_area,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllListBrand = new ArrayList<DeliverInfoAllDTO>();
		List<DeliverInfoAllDTO> deliverInfoAllListArea = new ArrayList<DeliverInfoAllDTO>();
		//====================================================================//
		listBrand=executeQuery(sql_brand, params);
		for(int i=0;i<listBrand.size();i++){
				Map retMap=(Map) listBrand.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllListBrand.add(deliverInfoDto);
		}
		//====================================================================//
		listArea=executeQuery(sql_area, params);
		for(int i=0;i<listArea.size();i++){
				Map retMap=(Map) listArea.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_name"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllListArea.add(deliverInfoDto);
		}
		//========================================================================//
		page.setItems((List)deliverInfoAllListBrand);
		page.setOtherItems((List)deliverInfoAllListArea);
		ro.setReturnDTO(page);
		return ro;
	}
	private ReturnObject funcPro9(String brand,String customer_area,String customer_name,String start_time,String end_time) throws Exception{
		List list=null;
		String sql_brand="select di.sub_brand,sum(di.quantity) quantity,sum(di.reserve1) stock_total_price from deliver_info di,deliver_common_info dci "+
	" where dci.customer_area=? and dci.customer_name=? and dci.deliver_time>? and dci.deliver_time<? and dci.id=di.order_num group by di.sub_brand";
		
		Object[] params_temp={brand,customer_area,customer_name,start_time,end_time};
		List<Object> params=objectArray2ObjectList(params_temp);
		
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoAllList = new ArrayList<DeliverInfoAllDTO>();
			list=executeQuery(sql_brand, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				deliverInfoDto.setCustomer_area((String)retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((String)retMap.get("quantity"));
				deliverInfoDto.setUni_reserve1((Float)retMap.get("stock_total_price"));
				deliverInfoAllList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoAllList);
			ro.setReturnDTO(page);
			return ro;
	}
	
	public String calculateStartTimeByEndTime(String end_time, int flag){
		String year=end_time.substring(0, 4);
		String month=end_time.substring(4, 6);
		String day=end_time.substring(6, 8);
		String retStr=null;
		switch(flag){
		case 1:
			retStr=calFunc1(year,month,day);//一个月
			break;
		case 2:
			retStr=calFunc2(year,month,day);//一个季度
			break;
		case 3:
			retStr=calFunc3(year,month,day);//一年
			break;
		case 4:
			retStr=calFunc4(year,month,day);//所有
			break;
		default:
			break;
		}
		return retStr;
	}
	
	String calFunc1(String year,String month,String day){
		if(month.equals("01")){
			year=String.valueOf(Integer.parseInt(year)-1);
			month=String.valueOf(12);
		}else{
			month=String.valueOf(Integer.parseInt(month)-1);
			if(month.length()<2){
				month="0"+month;
			}
		}
		day=String.valueOf(Integer.parseInt(day)-1);
		if(day.length()<2){
			day="0"+day;
		}
		/*else if(month.equals("03")&&Integer.parseInt(day)>28){
			day=String.valueOf(28);
			month="02";
		}
		else if((month.equals("05")||month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12"))&&day.equals("31")){
			day=String.valueOf(30);
			month=String.valueOf(Integer.parseInt(month)-1);
			if(month.length()<2){
				month="0"+month;
			}
		}
		else{
			month=String.valueOf(Integer.parseInt(month)-1);
			if(month.length()<2){
				month="0"+month;
			}
			day=String.valueOf(Integer.parseInt(day)-1);
			if(day.length()<2){
				day="0"+day;
			}
		}*/
		String retStr=year+month+day;
		return retStr;
	}
	
	String calFunc2(String year,String month,String day){
		if(month.equals("01")){
			year=String.valueOf(Integer.parseInt(year)-1);
			month=String.valueOf(10);
		}else if(month.equals("02")){
			year=String.valueOf(Integer.parseInt(year)-1);
			month="11";
		}
		else if(month.equals("03")){
			year=String.valueOf(Integer.parseInt(year)-1);
			month="12";
		}
		else{
			month=String.valueOf(Integer.parseInt(month)-3);
			if(month.length()<2){
				month="0"+month;
			}
		}
		day=String.valueOf(Integer.parseInt(day)-1);
		if(day.length()<2){
			day="0"+day;
		}
		String retStr=year+month+day;
		return retStr;
	}
	
	String calFunc3(String year,String month,String day){
		year=String.valueOf(Integer.parseInt(year)-1);
		day=String.valueOf(Integer.parseInt(day)-1);
		if(day.length()<2){
			day="0"+day;
		}
		String retStr=year+month+day;
		return retStr;
	}
	
	String calFunc4(String year,String month,String day){
		return "00000000";
	}	
	
	public StockFactor getStockFactor() throws Exception{
		StockFactor stockFactor=new StockFactor();
		List<StockInfoDTO> stockInfoList=new ArrayList<StockInfoDTO>();
		String sql_stock="select si.quantity batchQuantity,si.unit_price from stock_info si order by si.stock_time desc";
		List stock_list=null;
		try{
			stock_list=executeQuery(sql_stock, null);
		}catch(Exception e){
			throw new Exception("查询送货批次要素信息异常");
		}
		Map<Integer,Integer> stock_map=new HashMap<Integer,Integer>();
		Map<String,Float> result_profit=new HashMap<String,Float>();
		List<Map<String,Float>> resultList=null;
		for(int j=0;j<10;j++){
			StockInfoDTO stockInfoDto=new StockInfoDTO();
			Map retMap=(Map) stock_list.get(j);
			stockInfoDto.setQuantity(String.valueOf(retMap.get("batchQuantity")));
			stockInfoDto.setUnit_price((Float)retMap.get("unit_price"));
			stockInfoList.add(stockInfoDto);
			int num=(Integer)retMap.get("batchQuantity");
			stock_map.put(j,num);
		}
		stockFactor.setStock_map(stock_map);
		stockFactor.setStockInfoList(stockInfoList);
		return stockFactor;
	}

}
