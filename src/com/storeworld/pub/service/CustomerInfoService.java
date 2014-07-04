package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.storeworld.customer.Customer;
import com.storeworld.customer.CustomerCellModifier;
import com.storeworld.customer.CustomerUtils;
import com.storeworld.database.BaseAction;
import com.storeworld.database.DataBaseCommonInfo;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.utils.Utils;

/**
 * try to use generic type
 *
 */
public class CustomerInfoService extends BaseAction{
	
	private void fillInCustomerInfoDTO(CustomerInfoDTO customerInfoDto, Map<String, Object> retMap){
				
		if(retMap.get("id")!=null){
			customerInfoDto.setId(String.valueOf(retMap.get("id")));	
		}else{
			customerInfoDto.setId("");
		}
		if(retMap.get("customer_area")!=null){
			customerInfoDto.setCustomer_area(String.valueOf(retMap.get("customer_area")));	
		}else{
			customerInfoDto.setCustomer_area("");
		}
		if(retMap.get("customer_name")!=null){
			customerInfoDto.setCustomer_name(String.valueOf(retMap.get("customer_name")));	
		}else{
			customerInfoDto.setCustomer_name("");
		}
		if(retMap.get("reserve1")!=null){
			customerInfoDto.setReserve1(String.valueOf(retMap.get("reserve1")));	
		}else{
			customerInfoDto.setReserve1("");
		}
		if(retMap.get("reserve2")!=null){
			customerInfoDto.setReserve2(String.valueOf(retMap.get("reserve2")));	
		}else{
			customerInfoDto.setReserve2("");
		}
		if(retMap.get("reserve3")!=null){
			customerInfoDto.setReserve3(String.valueOf(retMap.get("reserve3")));	
		}else{
			customerInfoDto.setReserve3("");
		}
		if(retMap.get("telephone")!=null){
			customerInfoDto.setTelephone(String.valueOf(retMap.get("telephone")));	
		}else{
			customerInfoDto.setTelephone("");
		}
		if(retMap.get("customer_addr")!=null){
			customerInfoDto.setCustomer_addr(String.valueOf(retMap.get("customer_addr")));	
		}else{
			customerInfoDto.setCustomer_addr("");
		}
		
	}

	/**
	 * 新增一条客户信息，处理过程分为两步：
	 * 1.判断是否已存在相同客户：片区，客户名来判断。?? still need, since when we use this function, we already do the existence test
	 * 2.插入新记录。
	 */
	public boolean addCustomerInfo(Connection conn, Map<String,Object> map) throws Exception{
			try{
			//no need?
			boolean isExist=isExistCustomerInfo(conn, map);
			if(isExist){
				throw new Exception("已经存在相同的客户，片区，姓名，电话，分别为："+map.get("customer_area")+","+map.get("customer_name")+","+map.get("telephone"));
			}
			String sql="insert into customer_info(customer_area,customer_name,telephone,customer_addr,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?)";
			Object[] params_temp={map.get("customer_area"),map.get("customer_name"),map.get("telephone"),map.get("customer_addr"),map.get("reserve1"),map.get("reserve2"),map.get("reserve3")};//来自map
			List<Object> params=objectArray2ObjectList(params_temp);
			executeUpdate(conn, sql,params);
			
			}catch (Exception e) {
				throw new Exception("新增用户信息失败!"+e.getMessage());
			}
		 return true;
		
	}
	
	
	/**
	 * 判断一条记录是否已经存在：片区，客户名，电话。
	 */
	private boolean isExistCustomerInfo(Connection conn, Map<String,Object> map) throws Exception{

		String sql="select * from customer_info ci where ci.customer_area=? and ci.customer_name=?";// and ci.telephone=?
		Object[] params_tmp={map.get("customer_area"),map.get("customer_name")};//,map.get("telephone")
		List<Object> params=objectArray2ObjectList(params_tmp);

		List<Object> list=executeQuery(conn, sql, params);
		
		if(list==null||list.size()==0)
		{
			return false;
		}
		return true;
	}
	

	

	/**
	 * 删除一条客户信息，用ID标识。
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCustomerInfo(Connection conn, String id) throws Exception{
		String sql="delete from customer_info where id=?";
		Object[] params_temp={id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			executeUpdate(conn, sql,params);			
		} catch (Exception e) {
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
//	public boolean batchDeleteCustomerInfo(List<String> listId) throws Exception{
//		boolean ret_total=true;//执行批量删除的返回值
//		int num=listId.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=deleteCustomerInfo(listId.get(j));//执行删除一条记录的返回值
//				if(ret_one!=true){
//					ret_total=false;
//					return ret_total;
//				}
//			}
//		}catch(Exception e){
//			throw new Exception("执行批量删除客户信息异常！"+":"+e.getMessage());
//		}
//		return ret_total;
//	}
	
	
	public int updateCommonInfoIntoCustomer(Connection conn, String area, String name, String tele, String addr, 
			ArrayList<Customer> customers) throws Exception{
		int ret = 0;
		try{

		String sql="select * from customer_info ci where ci.customer_area=? and ci.customer_name=?";
		Object[] params_tmp={area, name};
		List<Object> params=objectArray2ObjectList(params_tmp);
//		System.out.println(params);
		List<Object> list=executeQuery(conn, sql, params);			

		
		if(list==null||list.size()==0){
			//no such customer, insert into it
			String sql_ins="insert into customer_info(customer_area,customer_name,telephone,customer_addr,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?)";
			Object[] params_temp_ins={area, name, tele, addr, "", "", ""};
			List<Object> params_ins=objectArray2ObjectList(params_temp_ins);
			executeUpdate(conn, sql_ins,params_ins);
			
			Customer c = new Customer();
			c.setID(CustomerUtils.getNewLineID());
			c.setName(name);
			c.setArea(area);
			c.setAddress(addr);
			c.setPhone(tele);
			
//			CustomerCellModifier.getCustomerList().customerChangedTwo(c);
			customers.add(c);
			
		}else{
			//if addr or tele changed
			Map<String,Object> mapRes = (Map<String,Object>)list.get(0); 
			String tele_old="";
			String addr_old="";
			String id_update = String.valueOf(mapRes.get("id"));
			if(mapRes.get("telephone") != null){
				tele_old = String.valueOf(mapRes.get("telephone"));
			}
			if(mapRes.get("customer_addr") != null){
				addr_old = String.valueOf(mapRes.get("customer_addr"));
			}
			if(!(tele.equals(tele_old) && addr.equals(addr_old))){
				String sql_update="update customer_info ci set ci.customer_area=?,ci.customer_name=?,ci.telephone=?,"
						+"ci.customer_addr=? where ci.id=?";
				Object[] params_temp_update={area,name,tele,addr,id_update};
				List<Object> params_update=objectArray2ObjectList(params_temp_update);
				executeUpdate(conn, sql_update,params_update);
				
				Customer c = new Customer();
				c.setID(id_update);
				c.setName(name);
				c.setArea(area);
				c.setAddress(addr);
				c.setPhone(tele);				
//				CustomerCellModifier.getCustomerList().customerChangedThree(c);
				customers.add(c);
				
			}			
		}
		}catch(Exception e){
			System.out.println("add common info into customer table failed");
			throw e;
		}
		return ret;
//		return customers;
	}
	
	/**
	 * 根据ID更新一条用户信息，步骤分为：
	 * 1.校验更新后的数据是否存在与存量数据重复的情况, we have already checked this in UI side
	 * 2.根据ID更新所有字段（即便某些字段可能没有变化）, already checked this in UI side
	 */
	public boolean updateCustomerInfo(Connection conn, String id,Map<String,Object> map) throws Exception{
		String sql="update customer_info ci set ci.customer_area=?,ci.customer_name=?,ci.telephone=?,"
				+"ci.customer_addr=? where ci.id=?";
		Object[] params_temp={map.get("customer_area"),map.get("customer_name"),map.get("telephone"),
				map.get("customer_addr"),id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			executeUpdate(conn, sql,params);
			
		} catch (Exception e) {

			throw new Exception("更新客户信息失败"+":"+e.getMessage());
		}
		return true;
	}
	

	/**
	 * 查询所有客户信息，不加查询条件。
	 */
	public ReturnObject queryCustomerInfoAll(Connection conn) throws Exception{
		ReturnObject ro=new ReturnObject();
		List<Object> list=null;
		String sql="select * from customer_info ci";
		List<Object> params=null;
		Pagination page = new Pagination();
		List<CustomerInfoDTO> customerInfoList = new ArrayList<CustomerInfoDTO>();
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				CustomerInfoDTO customerInfoDto=new CustomerInfoDTO();
				fillInCustomerInfoDTO(customerInfoDto, retMap);
				customerInfoList.add(customerInfoDto);
			}
			page.setItems((List)customerInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			throw new Exception("查询客户信息失败！");
		}
		return ro;
	}

	/**
	 * query the customer info by id, keep the return object the same as Bing
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public ReturnObject queryCustomerInfoByID(Connection conn, String id) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<CustomerInfoDTO> customerInfoList = new ArrayList<CustomerInfoDTO>();
		String sql="select * from customer_info ci where id=?";
		Object[] params_temp={id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				CustomerInfoDTO customerInfoDto=new CustomerInfoDTO();
				fillInCustomerInfoDTO(customerInfoDto, retMap);
				customerInfoList.add(customerInfoDto);
			}
			page.setItems((List)customerInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			System.out.println("query customre info by id failed");
			throw e;
		}
		return ro;
		
	}
	
	
	/**
	 * 按条件查询客户信息，当根据片区查客户时可用
	 * contains the function of "queryCustomerInfoByID"
	 */
	public ReturnObject queryCustomerInfo(Connection conn, Map<String,Object> map) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<CustomerInfoDTO> customerInfoList = new ArrayList<CustomerInfoDTO>();
		
		String id="";
		String customer_area="";
		String customer_name="";
		String telephone="";
		String customer_addr="";
		String reserve1="";
		String reserve2="";
		String reserve3="";
		if(map.get("id")!=null){
			id = String.valueOf(map.get("id"));
		}
		if(map.get("customer_area")!=null){
			customer_area = String.valueOf(map.get("customer_area"));
		}
		if(map.get("customer_name")!=null){
			customer_name = String.valueOf(map.get("customer_name"));
		}
		if(map.get("telephone")!=null){
			telephone = String.valueOf(map.get("telephone"));
		}
		if(map.get("customer_addr")!=null){
			customer_addr = String.valueOf(map.get("customer_addr"));
		}
		if(map.get("reserve1")!=null){
			reserve1 = String.valueOf(map.get("reserve1"));
		}
		if(map.get("reserve2")!=null){
			reserve2 = String.valueOf(map.get("reserve2"));
		}
		if(map.get("reserve3")!=null){
			reserve3 = String.valueOf(map.get("reserve3"));
		}
		
		String sql="select * from customer_info ci where 1=1";

		List<Object> params = new ArrayList<Object>();
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
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				CustomerInfoDTO customerInfoDto=new CustomerInfoDTO();
				fillInCustomerInfoDTO(customerInfoDto, retMap);
				customerInfoList.add(customerInfoDto);
			}
			page.setItems((List)customerInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("查询客户信息失败！");
		}
		return ro;
	}
	
	/**
	 * get the next row id of customer table
	 * @return
	 * @throws Exception
	 */
	public int getNextCustomerID() throws Exception{
		int id = -1;
		Connection conn = getConnection();

		try {
			conn.setAutoCommit(false);
			
			id = getNextID(conn, "customer_info");
			
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			System.out.println("get next customer id failed");
			throw new Exception(DataBaseCommonInfo.GET_NEXT_ID_FAILED);
		}finally{
			conn.close();
		}
		return id;
	}
	
//====================================================================================================================	
	/**
	 * 批量新增客户信息
	 * @param listMap 装的是多条客户信息
	 * @return
	 * @throws Exception
	 */
//	public boolean batchAddCustomerInfo (List<Map<String,Object>> listMap) throws Exception{
//		boolean ret_total=true;//执行批量插入的返回值
//		int num=listMap.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=addCustomerInfo(listMap.get(j));//执行一次插入的结果
//				if(ret_one==false){
//					ret_total=false;
//					return ret_total;
//				}
//			}
//		}catch(Exception e){
//			throw new Exception("执行批量新增货品信息异常！");
//		}
//		return ret_total;
//	}
	
	
	/**
	 * 批量更新客户信息
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
//	public boolean batchUpdateCustomerInfo(List<String> listId,List<Map<String,Object>> listMap) throws Exception{
//
//		boolean ret_total=true;//执行批量更新的返回值
//		int num=listMap.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=updateCustomerInfo(listId.get(j),listMap.get(j));//执行一次更新的结果
//				if(ret_one!=true){
//					ret_total=false;
//					return ret_total;
//				}
//			}
//		}catch(Exception e){
//			throw new Exception("执行批量新增货品信息异常！");
//		}
//		return ret_total;
//
//	}
	
	
//	private boolean isKeyFactorModified(Map<String,Object> map) throws Exception{
//	String id_temp=(String) map.get("id");
//	String customer_area=(String)map.get("customer_area");
//	String customer_name=(String)map.get("customer_name");
//	String telephone=(String)map.get("telephone");
//	Integer id=Integer.parseInt(id_temp);
//	String sql="select * from customer_info ci where ci.id=?";
//	Object[] params_tmp={id};
//	List<Object> params=objectArray2ObjectList(params_tmp);
//	List list=null;
//	try{
//		list=executeQuery(sql, params);
//	}catch(Exception e){
//		throw new Exception("修改客户记录时查询是否修改了该记录的关键字段出现异常"+e.getMessage());
//	}
//	if(list==null||list.size()==0)
//	{
//		throw new Exception("没有找到正在被修改的客户记录，ID为："+id);
//	}else if(list.size()==1){
//		Map retMap=(Map) list.get(0);
//		String DB_customer_area=(String) retMap.get("customer_area");
//		String DB_customer_name=(String) retMap.get("customer_name");
//		String DB_telephone=(String) retMap.get("telephone");
//		if(DB_customer_area.equals(customer_area)&&DB_customer_name.equals(customer_name)&&DB_telephone.equals(telephone)){
//			return false;
//		}
//		
//	}
//	return true;
//}
//==========================================================================================================================	
}
