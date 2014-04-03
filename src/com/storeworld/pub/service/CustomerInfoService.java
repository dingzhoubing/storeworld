package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.utils.Utils;

public class CustomerInfoService extends BaseAction{
	
	/**
	 * 新增一条客户信息，处理过程分为两步：
	 * 1.判断是否已存在相同客户：片区，客户名，电话来判断。
	 * 2.插入新记录。
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean addCustomerInfo(Map<String,Object> map) throws Exception{
		try{
			//1.获得输入的用户信息值，放入param中，ADD your code below:
			boolean isExist=isExistCustomerInfo(map);
			if(isExist){
				throw new Exception("已经存在相同的客户，片区，姓名，电话，分别为："+map.get("customer_area")+","+map.get("customer_name")+","+map.get("telephone"));
			}
			String sql="insert into customer_info(customer_area,customer_name,telephone,customer_addr,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?)";
			Object[] params_temp={map.get("customer_area"),map.get("customer_name"),map.get("telephone"),map.get("customer_addr"),map.get("reserve1"),map.get("reserve2"),map.get("reserve3")};//来自map
			List<Object> params=objectArray2ObjectList(params_temp);
			//2.调用接口执行插入
			int snum=executeUpdate(sql,params);
			if(snum<1){//插入记录失败，界面弹出异常信息,这里将异常抛出，由调用的去捕获异常
				throw new Exception("新增用户信息失败，请检查数据!");
				}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("新增用户信息失败!"+e.getMessage());
			}
		 return true;
		
	}
	/**
	 * 判断一条记录是否已经存在：片区，客户名，电话。
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean isExistCustomerInfo(Map<String,Object> map) throws Exception{
		BaseAction tempAction=new BaseAction();
		String sql="select * from customer_info ci where ci.customer_area=? and ci.customer_name=? and ci.telephone=?";
		Object[] params_tmp={map.get("customer_area"),map.get("customer_name"),map.get("telephone")};
		List<Object> params=objectArray2ObjectList(params_tmp);
		System.out.println(params);
		List list=null;
		try{
			list=executeQuery(sql, params);
		}catch(Exception e){
			throw new Exception("查询是否已存在将要插入的记录出现异常"+e.getMessage());
		}
		if(list==null||list.size()==0)
		{
			return false;
		}

		return true;
	}
	
	private boolean isKeyFactorModified(Map<String,Object> map) throws Exception{
		String id_temp=(String) map.get("id");
		String customer_area=(String)map.get("customer_area");
		String customer_name=(String)map.get("customer_name");
		String telephone=(String)map.get("telephone");
		Integer id=Integer.parseInt(id_temp);
		String sql="select * from customer_info ci where ci.id=?";
		Object[] params_tmp={id};
		List<Object> params=objectArray2ObjectList(params_tmp);
		List list=null;
		try{
			list=executeQuery(sql, params);
		}catch(Exception e){
			throw new Exception("修改客户记录时查询是否修改了该记录的关键字段出现异常"+e.getMessage());
		}
		if(list==null||list.size()==0)
		{
			throw new Exception("没有找到正在被修改的客户记录，ID为："+id);
		}else if(list.size()==1){
			Map retMap=(Map) list.get(0);
			String DB_customer_area=(String) retMap.get("customer_area");
			String DB_customer_name=(String) retMap.get("customer_name");
			String DB_telephone=(String) retMap.get("telephone");
			if(DB_customer_area.equals(customer_area)&&DB_customer_name.equals(customer_name)&&DB_telephone.equals(telephone)){
				return false;
			}
			
		}
		return true;
	}
	/**
	 * 批量新增客户信息
	 * @param listMap 装的是多条客户信息
	 * @return
	 * @throws Exception
	 */
	public boolean batchAddCustomerInfo (List<Map<String,Object>> listMap) throws Exception{
		boolean ret_total=true;//执行批量插入的返回值
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=addCustomerInfo(listMap.get(j));//执行一次插入的结果
				if(ret_one==false){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("执行批量新增货品信息异常！");
		}
		return ret_total;
	}
	
	/**
	 * 删除一条客户信息，用ID标识。
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCustomerInfo(String id) throws Exception{
		String sql="delete from customer_info where id=?";
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
	 * 批量删除客户信息
	 * @param listId
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteCustomerInfo(List<String> listId) throws Exception{
		boolean ret_total=true;//执行批量删除的返回值
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=deleteCustomerInfo(listId.get(j));//执行删除一条记录的返回值
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("执行批量删除客户信息异常！"+":"+e.getMessage());
		}
		return ret_total;
	}
	
	/**
	 * 根据ID更新一条用户信息，步骤分为：
	 * 1.校验更新后的数据是否存在与存量数据重复的情况。
	 * 2.根据ID更新所有字段（即便某些字段可能没有变化）。
	 * @param id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomerInfo(String id,Map<String,Object> map) throws Exception{
		String sql="update customer_info ci set ci.customer_area=?,ci.customer_name=?,ci.telephone=?,"
	+"ci.customer_addr=? where ci.id=?";
		Object[] params_temp={map.get("customer_area"),map.get("customer_name"),map.get("telephone"),
				map.get("customer_addr"),id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			if(isKeyFactorModified(map)){
				boolean isExist=isExistCustomerInfo(map);
				if(isExist){
					throw new Exception("已经存在相同的客户，片区，姓名，电话，分别为："+map.get("customer_area")+","+map.get("customer_name")+","+map.get("telephone"));
				}
			}
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("更新单条信息失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("更新客户信息失败"+":"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * 批量更新客户信息
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
	public boolean batchUpdateCustomerInfo(List<String> listId,List<Map<String,Object>> listMap) throws Exception{

		boolean ret_total=true;//执行批量更新的返回值
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=updateCustomerInfo(listId.get(j),listMap.get(j));//执行一次更新的结果
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("执行批量新增货品信息异常！");
		}
		return ret_total;

	}
	
	/**
	 * 查询所有客户信息，不加查询条件。
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryCustomerInfoAll() throws Exception{
		//ReturnObject ro=null;
		List list=null;
		String sql="select * from customer_info ci";
		//Object[] params={};
		List<Object> params=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<CustomerInfoDTO> customerInfoList = new ArrayList<CustomerInfoDTO>();
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				CustomerInfoDTO customerInfoDto=new CustomerInfoDTO();
				customerInfoDto.setId(String.valueOf(retMap.get("id")));
				customerInfoDto.setCustomer_area((String) retMap.get("customer_area"));
				customerInfoDto.setCustomer_name((String) retMap.get("customer_name"));
				customerInfoDto.setReserve1((String) retMap.get("reserve1"));
				customerInfoDto.setReserve2((String) retMap.get("reserve2"));
				customerInfoDto.setReserve3((String) retMap.get("reserve3"));
				customerInfoDto.setTelephone((String) retMap.get("telephone"));
				customerInfoDto.setCustomer_addr((String) retMap.get("customer_addr"));
				customerInfoList.add(customerInfoDto);
			}
			page.setItems((List)customerInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("查询客户信息失败！");
		}
		return ro;
	}

	/**
	 * 按条件查询客户信息，当根据片区查客户时可用
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryCustomerInfo(Map<String,Object> map) throws Exception{
		//ReturnObject ro=null;
		List list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<CustomerInfoDTO> customerInfoList = new ArrayList<CustomerInfoDTO>();
		
		Integer id=(Integer) map.get("id");
		String customer_area=(String) map.get("customer_area");
		String customer_name=(String) map.get("customer_name");
		String telephone=(String) map.get("telephone");
		String customer_addr=(String) map.get("customer_addr");
		String reserve1=(String) map.get("reserve1");
		String reserve2=(String) map.get("reserve2");
		String reserve3=(String) map.get("reserve3");
		String sql="select * from customer_info ci where 1=1";
		//Object[] params=new Object[]{};
		List<Object> params = new ArrayList<Object>();
		int p_num=0;
		if(Utils.isNotNull(String.valueOf(id))){
			sql=sql+" and ci.id=?";
			params.add(id);		
		}
		if(Utils.isNotNull(customer_area)){
			sql=sql+" and ci.customer_area=?";
			params.add(customer_area);		
		}
		if(Utils.isNotNull(customer_name)){
			sql=sql+" and ci.customer_name=?";
			params.add(customer_name);		
		}
		if(Utils.isNotNull(telephone)){
			sql=sql+" and ci.telephone=?";
			params.add(telephone);		
		}
		if(Utils.isNotNull(customer_addr)){
			sql=sql+" and ci.customer_addr=?";
			params.add(customer_addr);		
		}
		if(Utils.isNotNull(reserve1)){
			sql=sql+" and ci.reserve1=?";
			params.add(reserve1);		
		}
		if(Utils.isNotNull(reserve2)){
			sql=sql+" and ci.reserve2=?";
			params.add(reserve2);		
		}
		if(Utils.isNotNull(reserve3)){
			sql=sql+" and ci.reserve3=?";
			params.add(reserve3);		
		}
		
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				CustomerInfoDTO customerInfoDto=new CustomerInfoDTO();
				customerInfoDto.setId((String) retMap.get("id"));
				customerInfoDto.setCustomer_area((String) retMap.get("customer_area"));
				customerInfoDto.setCustomer_name((String) retMap.get("customer_name"));
				customerInfoDto.setReserve1((String) retMap.get("reserve1"));
				customerInfoDto.setReserve2((String) retMap.get("reserve2"));
				customerInfoDto.setReserve3((String) retMap.get("reserve3"));
				customerInfoDto.setTelephone((String) retMap.get("telephone"));
				customerInfoDto.setCustomer_addr((String) retMap.get("customer_addr"));
				customerInfoList.add(customerInfoDto);
			}
			page.setItems((List)customerInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("查询客户信息失败！");
		}
		return ro;
	}
}
