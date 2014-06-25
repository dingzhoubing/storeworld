package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.storeworld.customer.Customer;
import com.storeworld.customer.CustomerCellModifier;
import com.storeworld.customer.CustomerUtils;
import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.utils.Utils;

/**
 * try to use generic type
 *
 */
public class CustomerInfoService extends BaseAction{
	
	/**
	 * ����һ���ͻ���Ϣ��������̷�Ϊ������
	 * 1.�ж��Ƿ��Ѵ�����ͬ�ͻ���Ƭ�����ͻ������жϡ�?? still need, since when we use this function, we already do the existence test
	 * 2.�����¼�¼��
	 */
	public boolean addCustomerInfo(Map<String,Object> map) throws Exception{
		try{
			//no need?
			boolean isExist=isExistCustomerInfo(map);
			if(isExist){
				throw new Exception("�Ѿ�������ͬ�Ŀͻ���Ƭ�����������绰���ֱ�Ϊ��"+map.get("customer_area")+","+map.get("customer_name")+","+map.get("telephone"));
			}
			String sql="insert into customer_info(customer_area,customer_name,telephone,customer_addr,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?)";
			Object[] params_temp={map.get("customer_area"),map.get("customer_name"),map.get("telephone"),map.get("customer_addr"),map.get("reserve1"),map.get("reserve2"),map.get("reserve3")};//����map
			List<Object> params=objectArray2ObjectList(params_temp);
			int snum=executeUpdate(sql,params);
			if(snum<1){//why this happens
				throw new Exception("�����û���Ϣʧ�ܣ���������!");
				}
			}catch (Exception e) {
				throw new Exception("�����û���Ϣʧ��!"+e.getMessage());
			}
		 return true;
		
	}
	
	
	/**
	 * �ж�һ����¼�Ƿ��Ѿ����ڣ�Ƭ�����ͻ������绰��
	 */
	private boolean isExistCustomerInfo(Map<String,Object> map) throws Exception{
		BaseAction tempAction=new BaseAction();
		String sql="select * from customer_info ci where ci.customer_area=? and ci.customer_name=?";// and ci.telephone=?
		Object[] params_tmp={map.get("customer_area"),map.get("customer_name")};//,map.get("telephone")
		List<Object> params=objectArray2ObjectList(params_tmp);
//		System.out.println(params);
		List list=null;
		try{
			list=executeQuery(sql, params);
		}catch(Exception e){
			throw new Exception("��ѯ�Ƿ��Ѵ��ڽ�Ҫ����ļ�¼�����쳣"+e.getMessage());
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
			throw new Exception("�޸Ŀͻ���¼ʱ��ѯ�Ƿ��޸��˸ü�¼�Ĺؼ��ֶγ����쳣"+e.getMessage());
		}
		if(list==null||list.size()==0)
		{
			throw new Exception("û���ҵ����ڱ��޸ĵĿͻ���¼��IDΪ��"+id);
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
	 * ɾ��һ���ͻ���Ϣ����ID��ʶ��
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
				throw new Exception("ɾ��һ��ָ����¼ʧ�ܣ�ɾ�������ķ�����Ŀ����Ϊ1��");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("ִ��ɾ����¼�Ĳ���ʧ�ܣ�"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * ����ɾ���ͻ���Ϣ
	 * @param listId
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteCustomerInfo(List<String> listId) throws Exception{
		boolean ret_total=true;//ִ������ɾ���ķ���ֵ
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=deleteCustomerInfo(listId.get(j));//ִ��ɾ��һ����¼�ķ���ֵ
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("ִ������ɾ���ͻ���Ϣ�쳣��"+":"+e.getMessage());
		}
		return ret_total;
	}
	
	
	public int updateCommonInfoIntoCustomer(String area, String name, String tele, String addr){
		int ret = 0;
		try{
		BaseAction tempAction=new BaseAction();
		String sql="select * from customer_info ci where ci.customer_area=? and ci.customer_name=?";
		Object[] params_tmp={area, name};
		List<Object> params=objectArray2ObjectList(params_tmp);
		System.out.println(params);
		List list=null;
		try{
			list=executeQuery(sql, params);			
		}catch(Exception e){
			System.out.println("get customer info failed");
		}
		
		if(list==null||list.size()==0){
			//no such customer, insert into it
			String sql_ins="insert into customer_info(customer_area,customer_name,telephone,customer_addr,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?)";
			Object[] params_temp_ins={area, name, tele, addr, "", "", ""};
			List<Object> params_ins=objectArray2ObjectList(params_temp_ins);
			int snum=executeUpdate(sql_ins,params_ins);
			
			Customer c = new Customer();
			c.setID(CustomerUtils.getNewLineID());
			c.setName(name);
			c.setArea(area);
			c.setAddress(addr);
			c.setPhone(tele);
			
			CustomerCellModifier.getCustomerList().customerChangedTwo(c);
			
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
				int rows=executeUpdate(sql_update,params_update);
				
				Customer c = new Customer();
				c.setID(id_update);
				c.setName(name);
				c.setArea(area);
				c.setAddress(addr);
				c.setPhone(tele);				
				CustomerCellModifier.getCustomerList().customerChangedThree(c);
			}			
		}
		}catch(Exception e){
			System.out.println("add common info into customer table failed");
		}
		return ret;
	}
	
	/**
	 * ����ID����һ���û���Ϣ�������Ϊ��
	 * 1.У����º�������Ƿ��������������ظ������, we have already checked this in UI side
	 * 2.����ID���������ֶΣ�����ĳЩ�ֶο���û�б仯��, already checked this in UI side
	 */
	public boolean updateCustomerInfo(String id,Map<String,Object> map) throws Exception{
		String sql="update customer_info ci set ci.customer_area=?,ci.customer_name=?,ci.telephone=?,"
	+"ci.customer_addr=? where ci.id=?";
		Object[] params_temp={map.get("customer_area"),map.get("customer_name"),map.get("telephone"),
				map.get("customer_addr"),id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
//			if(isKeyFactorModified(map)){
//				boolean isExist=isExistCustomerInfo(map);
//				if(isExist){
//					throw new Exception("�Ѿ�������ͬ�Ŀͻ���Ƭ�����������绰���ֱ�Ϊ��"+map.get("customer_area")+","+map.get("customer_name")+","+map.get("telephone"));
//				}
//			}
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("���µ�����Ϣʧ��");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("���¿ͻ���Ϣʧ��"+":"+e.getMessage());
		}
		return true;
	}
	

	/**
	 * ��ѯ���пͻ���Ϣ�����Ӳ�ѯ������
	 */
	public ReturnObject queryCustomerInfoAll() throws Exception{
		ReturnObject ro=new ReturnObject();
		List list=null;
		String sql="select * from customer_info ci";
		List<Object> params=null;
		Pagination page = new Pagination();
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
			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}

	/**
	 * query the customer info by id, keep the return object the same as Bing
	 * @param id
	 * @return
	 */
	public ReturnObject queryCustomerInfoByID(String id){
		List list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<CustomerInfoDTO> customerInfoList = new ArrayList<CustomerInfoDTO>();
		String sql="select * from customer_info ci where id=?";
		Object[] params_temp={id};
		List<Object> params=objectArray2ObjectList(params_temp);
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
			System.out.println("query customre info by id failed");
		}
		return ro;
		
	}
	
	
	/**
	 * ��������ѯ�ͻ���Ϣ��������Ƭ����ͻ�ʱ����
	 * contains the function of "queryCustomerInfoByID"
	 */
	public ReturnObject queryCustomerInfo(Map<String,Object> map) throws Exception{
		//ReturnObject ro=null;
		List list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<CustomerInfoDTO> customerInfoList = new ArrayList<CustomerInfoDTO>();
		String tmp = (String)map.get("id");
		Integer id = null;
		if(tmp!=null)
			id=Integer.valueOf((String)map.get("id"));
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
			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * get the next row id of customer table
	 * @return
	 * @throws Exception
	 */
	public int getNextCustomerID() throws Exception{
		return getNextID("customer_info");
	}
	
//====================================================================================================================	
	/**
	 * ���������ͻ���Ϣ
	 * @param listMap װ���Ƕ����ͻ���Ϣ
	 * @return
	 * @throws Exception
	 */
//	public boolean batchAddCustomerInfo (List<Map<String,Object>> listMap) throws Exception{
//		boolean ret_total=true;//ִ����������ķ���ֵ
//		int num=listMap.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=addCustomerInfo(listMap.get(j));//ִ��һ�β���Ľ��
//				if(ret_one==false){
//					ret_total=false;
//					return ret_total;
//				}
//			}
//		}catch(Exception e){
//			throw new Exception("ִ������������Ʒ��Ϣ�쳣��");
//		}
//		return ret_total;
//	}
	
	
	/**
	 * �������¿ͻ���Ϣ
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
//	public boolean batchUpdateCustomerInfo(List<String> listId,List<Map<String,Object>> listMap) throws Exception{
//
//		boolean ret_total=true;//ִ���������µķ���ֵ
//		int num=listMap.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=updateCustomerInfo(listId.get(j),listMap.get(j));//ִ��һ�θ��µĽ��
//				if(ret_one!=true){
//					ret_total=false;
//					return ret_total;
//				}
//			}
//		}catch(Exception e){
//			throw new Exception("ִ������������Ʒ��Ϣ�쳣��");
//		}
//		return ret_total;
//
//	}
	
//==========================================================================================================================	
}
