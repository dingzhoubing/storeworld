package com.storeworld.pub.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;

public class StockInfoService extends BaseAction{
	
	/**
	 * 增加一条进货信息，处理的步骤包括：
	 * 1.根据界面输入的进货信息（已放入map中），查进货信息表的当日信息，判断是否已经存在相同的记录（进货日期这个字段很重要）
	 * 2.如果存在，抛出异常，如不存在，执行插入。
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Boolean addStockInfo(Map<String,Object> map) throws Exception{

	 try{
		//1.获得输入的用户信息值，放入param中，ADD your code below:
		boolean isExist=isExistStockInfo(map);
		if(isExist){
			throw new Exception("已经存在相同的进货信息，品牌，子品牌，规格，进货时间分别为："+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+","+map.get("stock_time"));
		}
		String sql="insert into stock_info(brand,sub_brand,unit_price,unit,standard,quantity,stock_time,stock_from,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard")+"KG",map.get("quantity"),map.get("stock_time"),map.get("stock_from"),map.get("reserve1"),map.get("reserve3"),map.get("reserve3")};//来自map
		List<Object> params=objectArray2ObjectList(params_temp);
		//2.调用接口执行插入
		BaseAction tempAction=new BaseAction();
		int snum=executeUpdate(sql,params);
		if(snum<1){//插入记录失败，界面弹出异常信息,这里将异常抛出，由调用的去捕获异常
			throw new Exception("新增进货信息失败，请检查数据!");
				/*MessageBox box=new MessageBox();
				box.setMessage("插入记录失败！");
				box.open();
				return;*/
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("新增进货信息失败!"+e.getMessage());
		}
	 return true;
	}

	/**
	 * description:判断是否存在相同的记录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean isExistStockInfo(Map<String,Object> map) throws Exception{
		BaseAction tempAction=new BaseAction();
		String time = (String) map.get("stock_time");
		//String sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and si.stock_time=str_to_date(?, '%Y-%m-%d')";
		String sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and date_format(si.stock_time,'%Y-%m-%d')=?";
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date s_date =(Date)sdf.parse(time);*/
		//Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard"),str2Timestamp(time)};
		
		Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard")};

		List<Object> params=objectArray2ObjectList(params_tmp);
		//System.out.println(params);
		List list=null;
		try{
			list=tempAction.executeQuery(sql, params);
		}catch(Exception e){
			throw new Exception("查询是否已存在将要插入的记录出现异常"+e.getMessage());
		}
		if(list==null||list.size()==0)
		{
			return false;
		}

		return true;
	}
	
	/**
	 * description：查询所有进货信息，不带查询条件
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryStockInfoAll() throws Exception{
		//ReturnObject ro=null;
		List list=null;
		String sql="select * from stock_info si";
		//Object[] params={};
		List<Object> params=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<StockInfoDTO> stockInfoList = new ArrayList<StockInfoDTO>();
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				StockInfoDTO stockInfoDto=new StockInfoDTO();
				stockInfoDto.setId(String.valueOf(retMap.get("id")));
				stockInfoDto.setBrand((String) retMap.get("brand"));
				stockInfoDto.setSub_brand((String) retMap.get("sub_brand"));
				stockInfoDto.setQuantity((Integer) retMap.get("quantity"));
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Timestamp date_temp = (Timestamp) retMap.get("stock_time");
				String date = df.format(date_temp);
				stockInfoDto.setStock_time(date);
				
				stockInfoDto.setStock_from((String) retMap.get("stock_from"));
				stockInfoDto.setReserve1((String) retMap.get("reserve1"));
				stockInfoDto.setReserve2((String) retMap.get("reserve2"));
				stockInfoDto.setReserve3((String) retMap.get("reserve3"));
				stockInfoDto.setStandard((String) retMap.get("standard"));
				stockInfoDto.setUnit((String) retMap.get("unit"));
				stockInfoDto.setUnit_price((Float) retMap.get("unit_price"));
				stockInfoList.add(stockInfoDto);
			}
			page.setItems((List)stockInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}

}
