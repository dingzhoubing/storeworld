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
import com.storeworld.pojo.dto.DeliverInfoDTO;
import com.storeworld.utils.Utils;

public class DeliverInfoService extends BaseAction{
	
	/**
	 * 增加一条送货信息，处理的步骤包括：
	 * 1.根据界面输入的送货信息（已放入map中），查送货信息表的当日信息，判断是否已经存在相同的记录（送日期这个字段很重要）
	 * 2.如果存在，抛出异常，如不存在，执行插入。
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean addDeliverInfo(Map<String,Object> commonMap,Map<String,Object> uniMap) throws Exception{

	 try{
		//1.获得输入的用户信息值，放入param中，ADD your code below:
		boolean isExist=isExistDeliverInfo(commonMap,uniMap);
		if(isExist){
			throw new Exception("对于客户"+commonMap.get("customer_name")+"已经存在相同的送货信息，品牌，子品牌，规格，数量分别为："+uniMap.get("brand")+","+uniMap.get("sub_brand")+","+uniMap.get("standard")+","+uniMap.get("quantity"));
		}
		String sql="insert into deliver_info(customer_area,customer_name,deliver_addr,order_num,"
		+"brand,sub_brand,unit_price,unit,standard,quantity,deliver_time,total_price,real_price,"
		+"is_print,telephone,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("deliver_addr"),commonMap.get("order_num"),
				uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),
				commonMap.get("deliver_time"),commonMap.get("total_price"),commonMap.get("real_price"),commonMap.get("is_print"),commonMap.get("telephone"),uniMap.get("reserve1"),uniMap.get("reserve2"),uniMap.get("reserve3")};//来自map
		List<Object> params=objectArray2ObjectList(params_temp);
		//2.调用接口执行插入
		BaseAction tempAction=new BaseAction();
		int snum=executeUpdate(sql,params);
		if(snum<1){//插入记录失败，界面弹出异常信息,这里将异常抛出，由调用的去捕获异常
			throw new Exception("新增送货信息失败，请检查数据!");
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("新增送货信息失败!"+e.getMessage());
		}
	 return true;
	}

	/**
	 * description:判断是否存在相同的记录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean isExistDeliverInfo(Map<String,Object> commonMap,Map<String,Object> uniMap) throws Exception{
		BaseAction tempAction=new BaseAction();
		String sql="select * from deliver_info di where di.customer_area=? and di.customer_name=? and di.telephone=? "
		+" and di.order_num=? and di.deliver_time=? and di.brand=? and di.sub_brand=? and di.standard=?";

		
		Object[] params_tmp={commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("telephone"),
				commonMap.get("order_num"),commonMap.get("deliver_time"),uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard")};

		List<Object> params=objectArray2ObjectList(params_tmp);
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
	 * 批量新增送货信息
	 * @param listMap 装的是多条送货信息
	 * @return
	 * @throws Exception
	 */
	public boolean batchAddDeliverInfo (Map<String,Object> commonMap,List<Map<String,Object>> listMap) throws Exception{
		boolean ret_total=true;//执行批量插入的返回值
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=addDeliverInfo(commonMap,listMap.get(j));//执行一次插入的结果
				if(ret_one==false){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("执行批量新增送货信息异常！");
		}
		return ret_total;
	}
	
	/**
	 * 删除一条送货信息，用ID标识。
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteDeliverInfo(Integer id) throws Exception{
		String sql="delete from deliver_info  where id=?";
		Object[] params_temp={id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("删除一条指定记录失败，删除操作的返回条目数不为1！");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("执行删除记录的操作失败！"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * 批量删除送货信息
	 * @param listId
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteDeliverInfo(List<Integer> listId) throws Exception{
		boolean ret_total=true;//执行批量删除的返回值
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=deleteDeliverInfo(listId.get(j));//执行删除一条记录的返回值
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("执行批量删除送货信息异常！"+":"+e.getMessage());
		}
		return ret_total;
	}
	
	/**
	 * 根据ID更新一条送货信息，步骤分为：
	 * 1.校验更新后的数据是否存在与存量数据重复的情况。
	 * 2.根据ID更新所有字段（即便某些字段可能没有变化）。
	 * @param id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean updateDeliverInfo(String id,Map<String,Object> commonMap,Map<String,Object> uniMap) throws Exception{
		/*float total_price=0;
		if(uniMap.get("unit_price")!=null&&uniMap.get("quantity")!=null){
			total_price=((Float)uniMap.get("unit_price"))*((Float)uniMap.get("quantity"));
			commonMap.put("total_price", total_price);
		}*/
		String sql="update deliver_info di set di.customer_area=?,di.customer_name=?,di.telephone=?,"
				+"di.deliver_addr=?,di.order_num=?,di.deliver_time=?,"
				+"di.brand=?,di.sub_brand=?,di.unit_price=?,"
				+"di.unit=?,di.standard=?,di.quantity=? where di.id=?";
		Object[] params_temp={commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("telephone"),commonMap.get("deliver_addr"),commonMap.get("order_num"),
				commonMap.get("deliver_time"),uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),
				id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			boolean isExist=isExistDeliverInfo(commonMap,uniMap);
			if(isExist){
				throw new Exception("对于客户"+commonMap.get("customer_name")+"已经存在相同的送货信息，品牌，子品牌，规格，数量分别为："+uniMap.get("brand")+","+uniMap.get("sub_brand")+","+uniMap.get("standard")+","+uniMap.get("quantity"));
			}
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("更新单条信息失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("更新送货信息失败"+":"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * 批量更新送货信息
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
	public boolean batchUpdateDeliverInfo(List<String> listId,List<Map<String,Object>> listCommonMap,List<Map<String,Object>> listUniMap) throws Exception{

		boolean ret_total=true;//执行批量更新的返回值
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=updateDeliverInfo(listId.get(j),listCommonMap.get(j),listUniMap.get(j));//执行一次更新的结果
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("执行批量新增进货信息异常！");
		}
		return ret_total;

	}
	
	
	/**
	 * description：查询所有送货信息，不带查询条件（用于展示左边的送货信息，应该有一个查询条件：送货日期）
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoAll() throws Exception{
		//ReturnObject ro=null;
		List list=null;
		String sql="select * from deliver_info di";
		//Object[] params={};
		List<Object> params=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoDTO> deliverInfoList = new ArrayList<DeliverInfoDTO>();
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoDTO deliverInfoDto=new DeliverInfoDTO();
				deliverInfoDto.setId(String.valueOf(retMap.get("id")));
				deliverInfoDto.setCustomer_area((String) retMap.get("customer_area"));
				deliverInfoDto.setCustomer_name((String) retMap.get("customer_name"));
				deliverInfoDto.setDeliver_addr((String) retMap.get("deliver_addr"));
				deliverInfoDto.setOrder_num((String) retMap.get("order_num"));
				deliverInfoDto.setTotal_price((Float)retMap.get("total_price"));
				deliverInfoDto.setReal_price((Float)retMap.get("real_price"));
				deliverInfoDto.setIs_print((String) retMap.get("is_print"));
				deliverInfoDto.setTelephone((String) retMap.get("telephone"));
				deliverInfoDto.setBrand((String) retMap.get("brand"));
				deliverInfoDto.setSub_brand((String) retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((Integer) retMap.get("quantity"));
				deliverInfoDto.setDeliver_time((String) retMap.get("deliver_time"));
				deliverInfoDto.setReserve1((String) retMap.get("reserve1"));
				deliverInfoDto.setReserve2((String) retMap.get("reserve2"));
				deliverInfoDto.setReserve3((String) retMap.get("reserve3"));
				deliverInfoDto.setStandard((String) retMap.get("standard"));
				deliverInfoDto.setUnit((String) retMap.get("unit"));
				deliverInfoDto.setUnit_price((Float) retMap.get("unit_price"));
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("查询进货信息失败！");
		}
		return ro;
	}
	
	/**
	 * description:查询满足条件的某几条送货信息
	 * @param map：装的是查询条件
	 * @return
	 * @throws Exception 
	 */
	public ReturnObject queryDeliverInfo(Map<String,Object> map) throws Exception{
		List list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoDTO> deliverInfoList = new ArrayList<DeliverInfoDTO>();
		String id=(String) map.get("id");
		String customer_area=(String) map.get("customer_area");
		String customer_name=(String) map.get("customer_name");
		String brand=(String) map.get("brand");
		String sub_brand=(String) map.get("sub_brand");
		String standard=(String) map.get("standard");
		String deliver_time=(String) map.get("deliver_time");
		String reserve1=(String) map.get("reserve1");
		String reserve2=(String) map.get("reserve2");
		String reserve3=(String) map.get("reserve3");
		
		String sql="select * from deliver_info di where 1=1";
		//Object[] params=new Object[]{};
		List<Object> params = new ArrayList<Object>();
		int p_num=0;
		if(Utils.isNotNull(id)){
			sql=sql+" and di.id=?";
			params.add(id);
		}
		if(Utils.isNotNull(customer_area)){
			sql=sql+" and di.customer_area=?";
			params.add(customer_area);
		}
		if(Utils.isNotNull(customer_name)){
			sql=sql+" and di.customer_name=?";
			params.add(customer_name);
		}
		if(Utils.isNotNull(deliver_time)){
			sql=sql+" and di.deliver_time=?";
			params.add(deliver_time);
		}
		if(Utils.isNotNull(brand)){
			sql=sql+" and di.brand=?";
			params.add(brand);
		}
		if(Utils.isNotNull(sub_brand)){
			sql=sql+" and di.sub_brand=?";
			params.add(sub_brand);
		}
		if(Utils.isNotNull(standard)){
			sql=sql+" and di.standard=?";
			params.add(standard);
		}
		if(Utils.isNotNull(reserve1)){
			sql=sql+" and di.reserve1=?";
			params.add(reserve1);
		}
		if(Utils.isNotNull(reserve2)){
			sql=sql+" and di.reserve2=?";
			params.add(reserve2);
		}
		if(Utils.isNotNull(reserve3)){
			sql=sql+" and di.reserve3=?";
			params.add(reserve3);
		}
		
		
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoDTO deliverInfoDto=new DeliverInfoDTO();
				deliverInfoDto.setId(String.valueOf(retMap.get("id")));
				deliverInfoDto.setCustomer_area((String) retMap.get("customer_area"));
				deliverInfoDto.setCustomer_name((String) retMap.get("customer_name"));
				deliverInfoDto.setDeliver_addr((String) retMap.get("deliver_addr"));
				deliverInfoDto.setOrder_num((String) retMap.get("order_num"));
				deliverInfoDto.setTotal_price((Float)retMap.get("total_price"));
				deliverInfoDto.setReal_price((Float)retMap.get("real_price"));
				deliverInfoDto.setIs_print((String) retMap.get("is_print"));
				deliverInfoDto.setTelephone((String) retMap.get("telephone"));
				deliverInfoDto.setBrand((String) retMap.get("brand"));
				deliverInfoDto.setSub_brand((String) retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((Integer) retMap.get("quantity"));
				deliverInfoDto.setDeliver_time((String) retMap.get("deliver_time"));
				deliverInfoDto.setReserve1((String) retMap.get("reserve1"));
				deliverInfoDto.setReserve2((String) retMap.get("reserve2"));
				deliverInfoDto.setReserve3((String) retMap.get("reserve3"));
				deliverInfoDto.setStandard((String) retMap.get("standard"));
				deliverInfoDto.setUnit((String) retMap.get("unit"));
				deliverInfoDto.setUnit_price((Float) retMap.get("unit_price"));
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}

}
