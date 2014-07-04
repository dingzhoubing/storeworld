package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.storeworld.customer.Customer;
import com.storeworld.database.BaseAction;
import com.storeworld.database.DataBaseCommonInfo;
import com.storeworld.deliver.Deliver;
import com.storeworld.pojo.dto.DeliverInfoAllDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.product.Product;
import com.storeworld.product.ProductCellModifier;
import com.storeworld.product.ProductUtils;
import com.storeworld.utils.Utils;

/**
 * use the generic type in the future
 *
 */
public class DeliverInfoService extends BaseAction{
	
	private void fillInDeliverInfoAllDTO(DeliverInfoAllDTO deliverInfoDto, Map<String, Object> retMap){

		if(retMap.get("uniId")!=null){
			deliverInfoDto.setUni_id(String.valueOf(retMap.get("uniId")));
		}else{
			deliverInfoDto.setUni_id("");
		}
		if(retMap.get("commonId")!=null){
			deliverInfoDto.setCommon_id(String.valueOf(retMap.get("commonId")));
		}else{
			deliverInfoDto.setCommon_id("");
		}
		if(retMap.get("customer_area")!=null){
			deliverInfoDto.setCustomer_area(String.valueOf(retMap.get("customer_area")));
		}else{
			deliverInfoDto.setCustomer_area("");
		}
		if(retMap.get("customer_name")!=null){
			deliverInfoDto.setCustomer_name(String.valueOf(retMap.get("customer_name")));
		}else{
			deliverInfoDto.setCustomer_name("");
		}
		if(retMap.get("deliver_addr")!=null){
			deliverInfoDto.setDeliver_addr(String.valueOf(retMap.get("deliver_addr")));
		}else{
			deliverInfoDto.setDeliver_addr("");
		}
		if(retMap.get("order_num")!=null){
			deliverInfoDto.setOrder_num(String.valueOf(retMap.get("order_num")));
		}else{
			deliverInfoDto.setOrder_num("");
		}
		if(retMap.get("total_price")!=null){
			deliverInfoDto.setTotal_price(Float.valueOf(String.valueOf(retMap.get("total_price"))));
		}else{
			deliverInfoDto.setTotal_price((float)0.0);
		}
		if(retMap.get("real_price")!=null){
			deliverInfoDto.setReal_price(Float.valueOf(String.valueOf(retMap.get("real_price"))));
		}else{
			deliverInfoDto.setReal_price((float)0.0);
		}
		if(retMap.get("telephone")!=null){
			deliverInfoDto.setTelephone(String.valueOf(retMap.get("telephone")));
		}else{
			deliverInfoDto.setTelephone("");
		}
		if(retMap.get("brand")!=null){
			deliverInfoDto.setBrand(String.valueOf(retMap.get("brand")));
		}else{
			deliverInfoDto.setBrand("");
		}
		if(retMap.get("sub_brand")!=null){
			deliverInfoDto.setSub_brand(String.valueOf(retMap.get("sub_brand")));
		}else{
			deliverInfoDto.setSub_brand("");
		}
		if(retMap.get("quantity")!=null){
			deliverInfoDto.setQuantity(String.valueOf(retMap.get("quantity")));
		}else{
			deliverInfoDto.setQuantity("");
		}
		if(retMap.get("deliver_time")!=null){
			deliverInfoDto.setDeliver_time(String.valueOf(retMap.get("deliver_time")));
		}else{
			deliverInfoDto.setDeliver_time("");
		}
		if(retMap.get("commonReserve1")!=null){
			deliverInfoDto.setCommon_reserve1(String.valueOf(retMap.get("commonReserve1")));
		}else{
			deliverInfoDto.setCommon_reserve1("");
		}
		if(retMap.get("commonReserve2")!=null){
			deliverInfoDto.setCommon_reserve2(String.valueOf(retMap.get("commonReserve2")));
		}else{
			deliverInfoDto.setCommon_reserve2("");
		}
		if(retMap.get("commonReserve3")!=null){
			deliverInfoDto.setCommon_reserve3(String.valueOf(retMap.get("commonReserve3")));
		}else{
			deliverInfoDto.setCommon_reserve3("");
		}
		if(retMap.get("uniReserve1")!=null){
			deliverInfoDto.setUni_reserve1(String.valueOf(retMap.get("uniReserve1")));
		}else{
			deliverInfoDto.setUni_reserve1("");
		}
		if(retMap.get("standard")!=null){
			deliverInfoDto.setStandard(String.valueOf(retMap.get("standard")));
		}else{
			deliverInfoDto.setStandard("");
		}
		if(retMap.get("unit")!=null){
			deliverInfoDto.setUnit(String.valueOf(retMap.get("unit")));
		}else{
			deliverInfoDto.setUnit("");
		}
		if(retMap.get("unit_price")!=null){
			deliverInfoDto.setUnit_price(Float.valueOf(String.valueOf(retMap.get("unit_price"))));
		}else{
			deliverInfoDto.setUnit_price((float)0.0);
		}

	}
	
	private void fillInDeliver(Deliver deliver, Map<String, Object> retMap){

		if(retMap.get("id")!=null){
			deliver.setID(String.valueOf(retMap.get("id")));
		}else{
			deliver.setID("");
		}
		if(retMap.get("order_num")!=null){
			deliver.setOrderNumber(String.valueOf(retMap.get("order_num")));
		}else{
			deliver.setOrderNumber("");
		}
		
		if(retMap.get("brand")!=null){
			deliver.setBrand(String.valueOf(retMap.get("brand")));
		}else{
			deliver.setBrand("");
		}
		if(retMap.get("sub_brand")!=null){
			deliver.setSubBrand(String.valueOf(retMap.get("sub_brand")));
		}else{
			deliver.setSubBrand("");
		}
		if(retMap.get("quantity")!=null){
			deliver.setNumber(String.valueOf(retMap.get("quantity")));
		}else{
			deliver.setNumber("");
		}
		
		if(retMap.get("standard")!=null){
			deliver.setSize(String.valueOf(retMap.get("standard")));
		}else{
			deliver.setSize("");
		}
		if(retMap.get("unit")!=null){
			deliver.setUnit(String.valueOf(retMap.get("unit")));
		}else{
			deliver.setUnit("");
		}
		if(retMap.get("unit_price")!=null){
			deliver.setPrice(String.valueOf(retMap.get("unit_price")));
		}else{
			deliver.setPrice("");
		}		

	}
	
	
	/**
	 * add the deliver info into the deliver table and database
	 * return -1: no such product in the product table
	 * return -2: the product left number is not enough
	 */
	public int addDeliverInfo(Connection conn, Map<String, Object> uniMap, ArrayList<Product> products) throws Exception {

		int ret = 0;
		try {
			BaseAction tempAction = new BaseAction();
			String sql = "select * from goods_info gi where gi.brand=? and gi.sub_brand=?";// and gi.standard=?
			Object[] params_tmp = { uniMap.get("brand"),uniMap.get("sub_brand")};//, uniMap.get("standard") 
			List<Object> params = objectArray2ObjectList(params_tmp);
			List<Object> list = tempAction.executeQuery(conn, sql, params);
			
			if (list == null || list.size() == 0) {
				//there is no such goods
				ret = -1;
				return ret;				
			}
			
			Map<String,Object> mapRes = (Map<String,Object>)list.get(0); 
			String id = String.valueOf(mapRes.get("id"));
			String reponum = String.valueOf(mapRes.get("repertory"));
			String newnum = String.valueOf(uniMap.get("quantity"));
			int reponum_int = Integer.valueOf(reponum);
			int newnum_int = Integer.valueOf(newnum);
			
//			if(reponum_int < newnum_int){
//				ret = -2;
//				return -2;//no enough goods in repository
//			}else{
				//add deliver and update goods_info				
				String sql_uni="insert into deliver_info(order_num,"
						+"brand,sub_brand,unit_price,unit,standard,quantity,"
						+"reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?)";						
						
				Object[] uni_params_temp={uniMap.get("order_num"),uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),
								uniMap.get("reserve1"),uniMap.get("reserve2"),uniMap.get("reserve3")};//����map	
				List<Object> uni_params=objectArray2ObjectList(uni_params_temp);
				executeUpdate(conn, sql_uni,uni_params);
				
				//update goods info
				String sql_update="update goods_info gi set gi.repertory=? where gi.id=?";
				Object[] params_update={String.valueOf(reponum_int - newnum_int),id};
				List<Object> params_do=objectArray2ObjectList(params_update);				
				executeUpdate(conn, sql_update,params_do);
				
				Product p = new Product();
				p.setID(id);
				p.setBrand(String.valueOf(mapRes.get("brand")));
				p.setSubBrand(String.valueOf(mapRes.get("sub_brand")));
				p.setSize(String.valueOf(mapRes.get("standard")));
				p.setUnit(String.valueOf(mapRes.get("unit")));	
				p.setRepository(String.valueOf(reponum_int - newnum_int));//initial it's empty, not null
//				ProductCellModifier.getProductList().productChangedThree(p);
				products.add(p);
//			}

		} catch (Exception e) {
			throw e;
		}
		return ret;
	}
	
	/**
	 * 	update the whole deliver table if the users changed the value of product table
	 *  now we update the deliver info one by one, which is very slow 
	 * @throws Exception 
	 */
	public int updateAllDeliverInfoForProductChanged(Connection conn, Map<String,Object> mapnew, Map<String,Object> mapold) throws Exception{
		int ret = 0;
		BaseAction tempAction=new BaseAction();
		String sql = "select * from deliver_info where brand=? and sub_brand=? and unit=? and standard=?";
		Object[] params_tmp={mapold.get("brand"),mapold.get("sub_brand"),mapold.get("unit"),mapold.get("standard")
			};
		List<Object> params=objectArray2ObjectList(params_tmp);
		try {
			List<Object> list = tempAction.executeQuery(conn, sql, params);
			if(list==null || list.size() == 0){
				return ret;
			}else{
				//update the deliver info one by one
				for(int i=0;i<list.size();i++){
					Map<String,Object> map = (Map<String,Object>)list.get(i); 
					String id = String.valueOf(map.get("id"));
					String sql_u = "update deliver_info si set si.brand=?,si.sub_brand=?, si.unit=?, si.standard=? where si.id=?";
					Object[] params_tmp_u={mapnew.get("brand"),mapnew.get("sub_brand"),mapnew.get("unit"),mapnew.get("standard"), id};
					List<Object> params_u=objectArray2ObjectList(params_tmp_u);
					tempAction.executeUpdate(conn, sql_u, params_u);
					
				}			
			}
		} catch (Exception e) {
			throw e;
		}
		
		return ret;
	}
	
	/**
	 * 	update the whole deliver table if the users changed the value of product table
	 *  now we update the deliver info one by one, which is very slow 
	 * @throws Exception 
	 */
	public int updateAllDeliverInfoForCustomerChanged(Connection conn, Map<String,Object> mapnew, Map<String,Object> mapold) throws Exception{
		int ret = 0;
		BaseAction tempAction=new BaseAction();
		String sql = "select * from deliver_common_info where customer_area=? and customer_name =?";
		Object[] params_tmp={mapold.get("customer_area"),mapold.get("customer_name")};
		List<Object> params=objectArray2ObjectList(params_tmp);
		try {
			List<Object> list = tempAction.executeQuery(conn, sql, params);
			if(list==null || list.size() == 0){
				return ret;
			}else{
				for(int i=0;i<list.size();i++){
					Map<String,Object> map = (Map<String,Object>)list.get(i); 
					String id = String.valueOf(map.get("id"));
					String sql_u = "update deliver_common_info si set si.customer_area=?,si.customer_name=?, si.telephone=?, "
							+ "si.deliver_addr=? where si.id=?";
					Object[] params_tmp_u={mapnew.get("customer_area"),mapnew.get("customer_name"),mapnew.get("telephone"),mapnew.get("customer_addr"), id};
					List<Object> params_u=objectArray2ObjectList(params_tmp_u);
					tempAction.executeUpdate(conn, sql_u, params_u);
					
				}			
			}
		} catch (Exception e) {
			System.out.println("update all deliver info for product changed failed");
			throw e;
		}		
		return ret;
	}
	
	/**
	 * update the delivers' indeed value by order number, and the field is "reserve1"
	 * @param ordernum
	 * @param indeed
	 * @return
	 * @throws Exception
	 */
	public boolean updateDeliversIndeedByOrderNumber(Connection conn, String ordernum, String indeed) throws Exception{
		boolean ret = false;
		//update this table, so we only need to update one row
		String sql_update="update deliver_info di set di.reserve1=? where di.order_num=?";
		Object[] params_update={indeed,ordernum};
		List<Object> params_do=objectArray2ObjectList(params_update);
		
		executeUpdate(conn, sql_update,params_do);
		ret = true;
		
		return ret;		
	}
	
	/**
	 * update the is_print field by order number
	 * @param ordernum
	 * @return
	 * @throws Exception
	 */
	public boolean updateIsPrintByOrderNumber(Connection conn, String ordernum) throws Exception{
		boolean ret = false;
		//update this table, so we only need to update one row
		String sql_update="update deliver_common_info di set di.is_print=? where di.order_num=?";
		Object[] params_update={"y",ordernum};
		List<Object> params_do=objectArray2ObjectList(params_update);
		
		executeUpdate(conn, sql_update,params_do);
		ret = true;
		
		return ret;		
	}
	
	/**
	 * delete the deliver common info and deliver_info by order number
	 * this is used when user click delete button
	 * @param ordernum
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Product> deleteDeliverAndCommonByOrderNumber(Connection conn, String ordernum) throws Exception{
//		boolean ret = false;
		ArrayList<Product> products = new ArrayList<Product>();
		//update this table, so we only need to update one row
		String sql_update="delete from deliver_common_info where id=?";
		Object[] params_update={ordernum};
		List<Object> params_do=objectArray2ObjectList(params_update);		
		executeUpdate(conn, sql_update,params_do);
		
//		String sql_update2="delete from deliver_info where order_num=?";
//		Object[] params_update2={ordernum};
//		List<Object> params_do2=objectArray2ObjectList(params_update2);		
//		executeUpdate(conn, sql_update2,params_do2);
		products = deleteDeliversByOrderNumber(conn, ordernum);
		
//		ret = true;		
//		return ret;		
		return products;
	}
	
	/**
	 * delete the deliver item in current deliver table(drop them), so no common info
	 * @param ordernum
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Product> deleteDeliversByOrderNumber(Connection conn, String ordernum) throws Exception{
//		boolean ret = false;
		ArrayList<Product> products = new ArrayList<Product>();
		
		//update this table, so we only need to update one row
		String sql_update="select * from deliver_info where order_num=?";
		Object[] params_update={ordernum};
		List<Object> params_do=objectArray2ObjectList(params_update);
		
		List<Object> list = executeQuery(conn, sql_update,params_do);
		if(list.size()==0 || list==null)
			return products;
		else{
			
			ArrayList<Deliver> delivers = new ArrayList<Deliver>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> map = (Map<String,Object>)list.get(i);
				Deliver deliver = new Deliver();
				fillInDeliver(deliver, map);
				delivers.add(deliver);				
			}
			
			products = batchDeleteDeliverInfoAndUpdateGoods(conn, delivers);
			
		}
//		ret = true;		
//		return ret;		
		return products;
	}

	
	/**
	 * save the common info
	 */
	public int printSaveCommonInfo(Connection conn, Map<String,Object> commonMap, 
			ArrayList<Customer> customers) throws Exception{
//		ArrayList<Customer> customers = new ArrayList<Customer>();
		int ret = 0;
		try {
			addCommonPart(conn, commonMap, customers);
		} catch (Exception e) {			
			System.out.println("save common info failed");
			throw e;
		}
//		return customers;
		return ret;
	}
	
	/**
	 * add the common info into the deliver common info table
	 */
	public ArrayList<Customer> addCommonPart(Connection conn, Map<String,Object> commonMap, 
			ArrayList<Customer> customers) throws Exception{
		
		try{
			String sql_common="insert into deliver_common_info(id,customer_area,customer_name,deliver_addr,"
				+"deliver_time,total_price,real_price,"
				+"is_print,telephone,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			
			Object[] common_params_temp={commonMap.get("order_num"),commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("deliver_addr"),
					commonMap.get("deliver_time"),commonMap.get("total_price"),commonMap.get("real_price"),commonMap.get("is_print"),commonMap.get("telephone"),commonMap.get("reserve1"),commonMap.get("reserve2"),commonMap.get("reserve3")};//����map
			List<Object> common_params=objectArray2ObjectList(common_params_temp);
			executeUpdate(conn, sql_common,common_params);
			
		}catch(Exception e){
			throw new Exception("�����ͻ���ʱ���ͻ����������ֲ��������쳣��");
		}
		
		//update customer table
		String area = String.valueOf(commonMap.get("customer_area"));
		String name = String.valueOf(commonMap.get("customer_name"));
		String addr = String.valueOf(commonMap.get("deliver_addr"));
		String tele = String.valueOf(commonMap.get("telephone"));
		
		CustomerInfoService customerinfo = new CustomerInfoService();
		customerinfo.updateCommonInfoIntoCustomer(conn, area, name, tele, addr, customers);
		
//		return true;
		return customers;
	}

	
	/**
	 * when we update the common info of the table
	 */
	public boolean updateCommonInfo(Connection conn, Map<String,Object> commonMap, ArrayList<Customer> customers) throws Exception{
		try{
			String sql_common="update deliver_common_info dci set dci.customer_area=?,dci.customer_name=?,dci.deliver_addr=?,"
				+"dci.deliver_time=?,dci.total_price=?,dci.real_price=?,"
				+"dci.is_print=?,dci.telephone=?,dci.reserve1=?,dci.reserve2=?,dci.reserve3=? where dci.id=?";
			
			Object[] common_params_temp={commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("deliver_addr"),
					commonMap.get("deliver_time"),commonMap.get("total_price"),commonMap.get("real_price"),commonMap.get("is_print"),commonMap.get("telephone"),commonMap.get("reserve1"),commonMap.get("reserve2"),commonMap.get("reserve3"), commonMap.get("order_num")};//����map
			List<Object> common_params=objectArray2ObjectList(common_params_temp);
			executeUpdate(conn, sql_common,common_params);
			
			//update customer table
			String area = String.valueOf(commonMap.get("customer_area"));
			String name = String.valueOf(commonMap.get("customer_name"));
			String addr = String.valueOf(commonMap.get("deliver_addr"));
			String tele = String.valueOf(commonMap.get("telephone"));
			CustomerInfoService customerinfo = new CustomerInfoService();
			customerinfo.updateCommonInfoIntoCustomer(conn, area, name, tele, addr, customers);
			
		}catch(Exception e){
			throw new Exception("����commonInfoʧ�ܣ�");
		}
		return true;
	}
	
	/**
	 * delete the deliver item and update the product number
	 * @param id
	 * @param deliver
	 * @return
	 * @throws Exception
	 */
	public Product deleteDeliverInfoAndUpdateGoods(Connection conn, Integer id, Deliver deliver) throws Exception{
		
		Product p = new Product();
		BaseAction tempAction=new BaseAction();
		String sql_goods="select * from goods_info gi where gi.brand=? and gi.sub_brand=?";// and gi.standard=?
		Object[] params_tmp_goods={deliver.getBrand(), deliver.getSubBrand()};//, deliver.getSize()
		List<Object> params_goods=objectArray2ObjectList(params_tmp_goods);
		List<Object> list=tempAction.executeQuery(conn, sql_goods, params_goods);
		
		if(list==null||list.size()==0){//insert
			String sql_insert_old="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
			Object[] params_temp_insert_old={deliver.getBrand(), deliver.getSubBrand(), "", deliver.getUnit(), deliver.getSize(), "","","", deliver.getNumber()};
			List<Object> params_insert_old=objectArray2ObjectList(params_temp_insert_old);
			executeUpdate(conn, sql_insert_old,params_insert_old);
			
//			Product p = new Product();
			p.setID(ProductUtils.getNewLineID());
			p.setBrand(deliver.getBrand());
			p.setSubBrand(deliver.getSubBrand());
			p.setSize(deliver.getSize());
			p.setUnit(deliver.getUnit());	
			p.setRepository(deliver.getNumber());//initial it's empty, not null
//			ProductCellModifier.getProductList().productChangedTwo(p);
			
		}else{//update
			Map<String,Object> mapRes = (Map<String,Object>)list.get(0);
			String Repo = String.valueOf(mapRes.get("repertory"));
			String id_add = String.valueOf(mapRes.get("id"));
			int repo_old = Integer.valueOf(Repo);
			int add = Integer.valueOf(deliver.getNumber());			
			
			String sql_update="update goods_info gi set gi.repertory=? where gi.id=?";
			Object[] params_update={String.valueOf(repo_old + add),id_add};
			List<Object> params_do=objectArray2ObjectList(params_update);				
			executeUpdate(conn, sql_update,params_do);
			
//			Product p = new Product();
			p.setID(id_add);
			p.setBrand(deliver.getBrand());
			p.setSubBrand(deliver.getSubBrand());
			p.setSize(deliver.getSize());
			p.setUnit(deliver.getUnit());	
			p.setRepository(String.valueOf(repo_old + add));//initial it's empty, not null
//			ProductCellModifier.getProductList().productChangedThree(p);
			
			
		}
		
		//delete deliver
		String sql="delete from deliver_info  where id=?";
		Object[] params_temp={id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			executeUpdate(conn, sql,params);
			
		} catch (Exception e) {			
			throw new Exception("ִ��ɾ����¼�Ĳ���ʧ�ܣ�"+e.getMessage());
		}
		
//		return true;
		return p;
	}
	
	
	
	public ArrayList<Product> batchDeleteDeliverInfoAndUpdateGoods(Connection conn, 
			ArrayList<Deliver> delivers) throws Exception{
		
		ArrayList<Product> products = new ArrayList<Product>();
		for(int i=0;i<delivers.size();i++){
			Deliver deliver = delivers.get(i);
			Product p;
			try {
				p = deleteDeliverInfoAndUpdateGoods(conn, Integer.valueOf(deliver.getID()), deliver);
			} catch (Exception e) {
				throw e;
			}
			products.add(p);
		}
		return products;		
	}
	
	/**
	 * delete the deliver info only
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteDeliverInfo(Connection conn, Integer id) throws Exception{
		
		String sql="delete from deliver_info  where id=?";
		Object[] params_temp={id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			executeUpdate(conn, sql,params);
			
		} catch (Exception e) {

			throw new Exception("ִ��ɾ����¼�Ĳ���ʧ�ܣ�"+e.getMessage());
		}
		
		return true;
	}
	
	/**
	 * the product is not exist in the product table, so we add the product into the product table
	 * and insert the deliver table
	 * @param id
	 * @param uniMap
	 * @return
	 */
	public int insertGoodsAndUpdateDeliver(Connection conn, final String id, final Map<String,Object> uniMap, 
			ArrayList<Product> products) throws Exception {
		int ret = 0;
		//update the goods info and deliver info
		//update goods info
		String sql_insert_new="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
		Object[] params_temp_insert_new={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("reserve1"),uniMap.get("reserve2"),uniMap.get("reserve3"),"0"};
		List<Object> params_insert_new=objectArray2ObjectList(params_temp_insert_new);
		
		executeUpdate(conn, sql_insert_new,params_insert_new);
		
		Product p = new Product();
		p.setID(ProductUtils.getNewLineID());
		p.setBrand(String.valueOf(uniMap.get("brand")));
		p.setSubBrand(String.valueOf(uniMap.get("sub_brand")));
		p.setSize(String.valueOf(uniMap.get("standard")));
		p.setUnit(String.valueOf(uniMap.get("unit")));	
		p.setRepository("0");//initial it's empty, not null
//		ProductCellModifier.getProductList().productChangedTwo(p);
		products.add(p);
		
		//insert deliver info
		try {
			String sql="insert into deliver_info(order_num,"
					+"brand,sub_brand,unit_price,unit,standard,quantity,"
					+"reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?)";
			Object[] params_temp={uniMap.get("order_num"), uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),
					uniMap.get("reserve1"), uniMap.get("reserve2"), uniMap.get("reserve3")};
			List<Object> params=objectArray2ObjectList(params_temp);
			executeUpdate(conn, sql,params);
		} catch (Exception e) {
			System.out.println("update deliver info if the product is new failed");
			throw e;
		}	

		return ret;		
	}
	
	/**
	 * the product is not exist in the product table, so we add the product into the product table
	 * and update the deliver table
	 * @param id
	 * @param uniMap
	 * @return
	 */
	public int insertGoodsAndUpdateDeliverTwo(Connection conn, final String id, final Map<String,Object> uniMap, 
			ArrayList<Product> products) throws Exception{
		int ret = 0;
		//update the goods info and deliver info
		//update goods info
		String sql_insert_new="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
		Object[] params_temp_insert_new={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("reserve1"),uniMap.get("reserve2"),uniMap.get("reserve3"),"0"};
		List<Object> params_insert_new=objectArray2ObjectList(params_temp_insert_new);
		
		executeUpdate(conn, sql_insert_new,params_insert_new);
				
		Product p = new Product();
		p.setID(ProductUtils.getNewLineID());
		p.setBrand(String.valueOf(uniMap.get("brand")));
		p.setSubBrand(String.valueOf(uniMap.get("sub_brand")));
		p.setSize(String.valueOf(uniMap.get("standard")));
		p.setUnit(String.valueOf(uniMap.get("unit")));	
		p.setRepository("0");//initial it's empty, not null
//		ProductCellModifier.getProductList().productChangedTwo(p);
		products.add(p);

		//update deliver info
		try {
			String sql="update deliver_info di set "
					+" di.brand=?,di.sub_brand=?,di.unit_price=?,"
					+" di.unit=?,di.standard=?,di.quantity=?, di.order_num=? where di.id=?";
			Object[] params_temp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),uniMap.get("order_num"),
					id};
			List<Object> params=objectArray2ObjectList(params_temp);
			executeUpdate(conn, sql,params);
		} catch (Exception e) {
			throw e;
		}
		
		return ret;		
	}
	
	/**
	 * update the product table and insert the deliver table
	 * @param id
	 * @param uniMap
	 * @return
	 */
	@Deprecated
	public int updateGoodsAndUpdateDeliver(Connection conn, final String id, final Map<String,Object> uniMap) throws Exception{
		int ret = 0;
		
		//update goods info
		String sql_oldmap_product="select * from goods_info gi where gi.brand=? and gi.sub_brand=? ";//and gi.standard=?
		Object[] params_tmp_oldmap_product={uniMap.get("brand"),uniMap.get("sub_brand")};//,uniMap.get("standard")
		List<Object> params_oldmap_product=objectArray2ObjectList(params_tmp_oldmap_product);

		BaseAction tempAction=new BaseAction();
		try {
			List<Object> list_oldmap_product=tempAction.executeQuery(conn, sql_oldmap_product, params_oldmap_product);
			
			Map<String,Object> mapRes = (Map<String,Object>)list_oldmap_product.get(0);
			String id_good = String.valueOf(mapRes.get("id"));
			
			String sql_update="update goods_info gi set gi.repertory=? where gi.id=?";
			Object[] params_update={"0",id_good};
			List<Object> params_do=objectArray2ObjectList(params_update);				
			executeUpdate(conn, sql_update,params_do);
			
			Product p = new Product();
			p.setID(id_good);
			p.setBrand(String.valueOf(mapRes.get("brand")));
			p.setSubBrand(String.valueOf(mapRes.get("sub_brand")));
			p.setSize(String.valueOf(mapRes.get("standard")));
			p.setUnit(String.valueOf(mapRes.get("unit")));	
			p.setRepository("0");//initial it's empty, not null
			ProductCellModifier.getProductList().productChangedThree(p);
		} catch (Exception e1) {
			System.out.println("update the goods info failed");
			throw e1;
		}
		
		//insert deliver info
		try {
			String sql="insert into deliver_info(order_num,"
					+"brand,sub_brand,unit_price,unit,standard,quantity,"
					+"reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?)";
			Object[] params_temp={uniMap.get("order_num"), uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),
					uniMap.get("reserve1"), uniMap.get("reserve2"), uniMap.get("reserve3")};
			List<Object> params=objectArray2ObjectList(params_temp);
			executeUpdate(conn, sql,params);
		} catch (Exception e) {
			System.out.println("update deliver info if the product is new failed");
			throw e;
		}	

		return ret;
	}
	
	/**
	 * update the product table and update deliver table
	 * @param id
	 * @param uniMap
	 * @return
	 */
	@Deprecated
	public int updateGoodsAndUpdateDeliverTwo(Connection conn, final String id, final Map<String,Object> uniMap, 
			ArrayList<Product> products) throws Exception {
		int ret = 0;
		
		//update goods info
		String sql_oldmap_product="select * from goods_info gi where gi.brand=? and gi.sub_brand=?";// and gi.standard=?
		Object[] params_tmp_oldmap_product={uniMap.get("brand"),uniMap.get("sub_brand")};//,uniMap.get("standard")
		List<Object> params_oldmap_product=objectArray2ObjectList(params_tmp_oldmap_product);
		//System.out.println(params);
		BaseAction tempAction=new BaseAction();
		try {
			List<Object> list_oldmap_product=tempAction.executeQuery(conn, sql_oldmap_product, params_oldmap_product);
			
			Map<String,Object> mapRes = (Map<String,Object>)list_oldmap_product.get(0);
			String id_good = String.valueOf(mapRes.get("id"));
			
			String sql_update="update goods_info gi set gi.repertory=? where gi.id=?";
			Object[] params_update={"0",id_good};
			List<Object> params_do=objectArray2ObjectList(params_update);				
			executeUpdate(conn, sql_update,params_do);
			
			Product p = new Product();
			p.setID(id_good);
			p.setBrand(String.valueOf(mapRes.get("brand")));
			p.setSubBrand(String.valueOf(mapRes.get("sub_brand")));
			p.setSize(String.valueOf(mapRes.get("standard")));
			p.setUnit(String.valueOf(mapRes.get("unit")));	
			p.setRepository("0");//initial it's empty, not null
//			ProductCellModifier.getProductList().productChangedThree(p);
			products.add(p);
			
		} catch (Exception e1) {
			throw e1;
		}
		
		//update deliver info
		try {
			String sql="update deliver_info di set "
					+" di.brand=?,di.sub_brand=?,di.unit_price=?,"
					+" di.unit=?,di.standard=?,di.quantity=?, di.order_num=? where di.id=?";
			Object[] params_temp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),uniMap.get("order_num"),
					id};
			List<Object> params=objectArray2ObjectList(params_temp);
			executeUpdate(conn, sql,params);
		} catch (Exception e) {
			System.out.println("update deliver info if the product is new failed");
			throw e;
		}
		
		return ret;
	}
	
	/**
	 * normally update the deliver info
	 * @param id
	 * @param uniMap
	 * @return
	 * @throws Exception
	 */
	public int updateDeliverInfo(Connection conn, final String id, final Map<String,Object> uniMap, String returnOrDeliver, 
			ArrayList<Product> products) throws Exception{
		int type = 0;
		BaseAction tempAction=new BaseAction();	
		String sql="update deliver_info di set "
				+" di.brand=?,di.sub_brand=?,di.unit_price=?,"
				+" di.unit=?,di.standard=?,di.quantity=?, di.order_num=? where di.id=?";
		Object[] params_temp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),uniMap.get("order_num"),
				id};
		List<Object> params=objectArray2ObjectList(params_temp);

		try{
			String sql_cur = "select * from deliver_info di where di.id=?";
			Object[] params_tmp={uniMap.get("id")};
			List<Object> params_cur=objectArray2ObjectList(params_tmp);
			List<Object> list=tempAction.executeQuery(conn, sql_cur, params_cur);
		
			Map<String,Object> mapRes = (Map<String,Object>)list.get(0); 
			String oldRepo = String.valueOf(mapRes.get("quantity"));
			String newRepo = String.valueOf(uniMap.get("quantity"));
			
//			int gap = Integer.valueOf(newRepo) - Integer.valueOf(oldRepo);
			int gap_old = Integer.valueOf(oldRepo);
			int gap_new = Integer.valueOf(newRepo);
			
			//update the goods info
			GoodsInfoService goods_service = new GoodsInfoService();
			type = goods_service.minusGoodsInfoAndUpdate(conn, mapRes, uniMap, gap_old, gap_new, returnOrDeliver, products);

			if(type == 0){
				executeUpdate(conn, sql,params);
			}

		}catch (Exception e) {
			throw new Exception("���½�����Ϣʧ��"+":"+e.getMessage());
		}
		
		return type;
	}
	
	
	


	public boolean queryCommonInfo(Connection conn, String area, String name) throws Exception{
		try{
			String sql_common="select * from  deliver_common_info where customer_area=? and customer_name=?";
			
			Object[] common_params_temp={area, name};
			List<Object> common_params=objectArray2ObjectList(common_params_temp);
			List<Object> list = executeQuery(conn, sql_common,common_params);
			if(list==null || list.size()==0)
				return false;
			else
				return true;
			
		}catch(Exception e){
			throw new Exception("��ѯcommonInfoʧ�ܣ�");
		}
	}
	
	/**
	 * ��ѯ����������ĳ�����ͻ���Ϣ
	 * @param map��װ���ǲ�ѯ����
	 */
	public ReturnObject queryDeliverInfo(Connection conn, Map<String,Object> map) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
		
		String uniId="";
		String commonId="";
		String customer_area="";
		String customer_name="";
		String brand="";
		String sub_brand="";
		String standard="";
		String order_num="";
		String deliver_time="";
		String uniReserve1="";
		String uniReserve2="";
		String uniReserve3="";
		
		if(map.get("uniId")!=null){
			uniId=String.valueOf(map.get("uniId"));
		}
		if(map.get("commonId")!=null){
			commonId=String.valueOf(map.get("commonId"));
		}
		if(map.get("customer_area")!=null){
			customer_area=String.valueOf(map.get("customer_area"));
		}
		if(map.get("customer_name")!=null){
			customer_name=String.valueOf(map.get("customer_name"));
		}
		if(map.get("brand")!=null){
			brand=String.valueOf(map.get("brand"));
		}
		if(map.get("sub_brand")!=null){
			sub_brand=String.valueOf(map.get("sub_brand"));
		}
		if(map.get("standard")!=null){
			standard=String.valueOf(map.get("standard"));
		}
		if(map.get("order_num")!=null){
			order_num=String.valueOf(map.get("order_num"));
		}
		if(map.get("deliver_time")!=null){
			deliver_time=String.valueOf(map.get("deliver_time"));
		}
		if(map.get("uniReserve1")!=null){
			uniReserve1=String.valueOf(map.get("uniReserve1"));
		}
		if(map.get("uniReserve2")!=null){
			uniReserve2=String.valueOf(map.get("uniReserve2"));
		}
		if(map.get("uniReserve3")!=null){
			uniReserve3=String.valueOf(map.get("uniReserve3"));
		}
		
		
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";
		//
		List<Object> params = new ArrayList<Object>();

		if(Utils.isNotNull(commonId)){
			sql=sql+" and commonId=?";
			params.add(commonId);
		}
		if(Utils.isNotNull(customer_area)){
			sql=sql+" and dci.customer_area=?";
			params.add(customer_area);
		}
		if(Utils.isNotNull(customer_name)){
			sql=sql+" and dci.customer_name=?";
			params.add(customer_name);
		}
		if(Utils.isNotNull(deliver_time)){
			sql=sql+" and dci.deliver_time=?";
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
		if(Utils.isNotNull(uniReserve1)){
			sql=sql+" and uniReserve1=?";
			params.add(uniReserve1);
		}
		if(Utils.isNotNull(uniReserve2)){
			sql=sql+" and uniReserve2=?";
			params.add(uniReserve2);
		}
		if(Utils.isNotNull(uniReserve3)){
			sql=sql+" and uniReserve3=?";
			params.add(uniReserve3);
		}
				
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);				
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			throw new Exception("��ѯ��Ʒ��Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * Ĭ�ϵ��ͻ���¼��ѯ����ѯ����
	 */
	public ReturnObject queryDeliverInfoByDefaultDelivertime(Connection conn, Map<String,Object> map) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String deliver_time_temp=(String) map.get("deliver_time");
		String deliver_time=deliver_time_temp.substring(0, 8);
		String start_time=deliver_time+"000000";
		String end_time=deliver_time+"235959";
		
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();

		if(Utils.isNotNull(deliver_time)){
			sql=sql+" and dci.deliver_time>? and dci.deliver_time<?";
			params.add(start_time);
			params.add(end_time);
		}
				
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);            	
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
		
	/**
	 * query deliver by timer interval
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeInterval(Connection conn, String start, String end) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<?";
		params.add(start);
		params.add(end);		
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);            	
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * query deliver by time interval and area
	 * @param start
	 * @param end
	 * @param area
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeIntervalAndCustomerArea(Connection conn, String start, String end, String area) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and dci.customer_area = ?";
		params.add(start);
		params.add(end);		
		params.add(area);
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);            	
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * query the deliver by time interval and customer area, customer name
	 * @param start
	 * @param end
	 * @param area
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeIntervalAndCustomerAreaName(Connection conn, String start, String end, String area, String name) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and dci.customer_area=? and dci.customer_name=?";
		params.add(start);
		params.add(end);		
		params.add(area);
		params.add(name);
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);            	
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * query deliver by time interval and brand
	 * @param start
	 * @param end
	 * @param brandstr
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeIntervalAndBrand(Connection conn, String start, String end, String brandstr) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and di.brand=?";
		params.add(start);
		params.add(end);		
		params.add(brandstr);
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);            	
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * query deliver by time interval and brand and customer area
	 * @param start
	 * @param end
	 * @param brandstr
	 * @param areaStr
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeIntervalAndBrandArea(Connection conn, String start, String end, String brandstr, String areaStr) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and di.brand=? and dci.customer_area=?";
		params.add(start);
		params.add(end);		
		params.add(brandstr);
		params.add(areaStr);
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);            	
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * query deliver by time interval and brand, customer area and customer name
	 * @param start
	 * @param end
	 * @param brandstr
	 * @param areaStr
	 * @param nameStr
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeIntervalAndBrandAreaName(Connection conn, String start, String end, String brandstr, String areaStr, String nameStr) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";
		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and di.brand=? and dci.customer_area=? and dci.customer_name=?";
		params.add(start);
		params.add(end);		
		params.add(brandstr);
		params.add(areaStr);
		params.add(nameStr);
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);            	
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * query deliver by time interval and brand and sub brand
	 * @param start
	 * @param end
	 * @param brandstr
	 * @param subStr
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeIntervalAndBrandSub(Connection conn, String start, String end, String brandstr, String subStr) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and di.brand=? and di.sub_brand=?";
		params.add(start);
		params.add(end);		
		params.add(brandstr);
		params.add(subStr);
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * query deliver by time interval and brand, sub brand and customer area
	 * @param start
	 * @param end
	 * @param brandstr
	 * @param subStr
	 * @param areaStr
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeIntervalAndBrandSubArea(Connection conn, String start, String end, String brandstr, String subStr, String areaStr) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and di.brand=? and di.sub_brand=? and dci.customer_area=?";
		params.add(start);
		params.add(end);		
		params.add(brandstr);
		params.add(subStr);
		params.add(areaStr);
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
				fillInDeliverInfoAllDTO(deliverInfoDto, retMap);
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * query deliver by time interval and brand, sub brand, customer area, customer name
	 * @param start
	 * @param end
	 * @param brandstr
	 * @param subStr
	 * @param areaStr
	 * @param nameStr
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByTimeIntervalAndBrandSubAreaName(Connection conn, String start, String end, String brandstr, String subStr, String areaStr, String nameStr) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();
		sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and di.brand=? and di.sub_brand=? and dci.customer_area=? and dci.customer_name=?";
		params.add(start);
		params.add(end);		
		params.add(brandstr);
		params.add(subStr);
		params.add(areaStr);
		params.add(nameStr);
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
            	fillInDeliverInfoAllDTO(deliverInfoDto, retMap);
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * ��ѯ����ĳһ����ͻ���¼��ʱ��������룬��ȷ���죿����֧���·ݣ�
	 */
	public ReturnObject queryDeliverInfoByInputDelivertime(Connection conn, Map<String,Object> map) throws Exception{
		List<Object> list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
	
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";

		List<Object> params = new ArrayList<Object>();

		String area = String.valueOf(map.get("customer_area"));
		String cus = String.valueOf(map.get("customer_name"));
		
		if(cus.equals("")){
			String deliver_time_temp=(String) map.get("deliver_time");
			String deliver_time=deliver_time_temp.substring(0, 8);
			String start_time=deliver_time+"000000";
			String end_time=deliver_time+"235959";		
			if(Utils.isNotNull(deliver_time)){
				sql=sql+" and dci.deliver_time>? and dci.deliver_time<?";
				params.add(start_time);
				params.add(end_time);
			}
		}else{
			String deliver_time_temp=(String) map.get("deliver_time");
			String deliver_time=deliver_time_temp.substring(0, 6);
			String start_time=deliver_time+"00000000";
			String end_time=deliver_time+"31235959";		
			if(Utils.isNotNull(deliver_time)){
				sql=sql+" and dci.deliver_time>? and dci.deliver_time<? and dci.customer_area=? and dci.customer_name=?";
				params.add(start_time);
				params.add(end_time);
				params.add(area);
				params.add(cus);
			}
		}
		
		try {
			list=executeQuery(conn, sql, params);
			for(int i=0;i<list.size();i++){
				Map<String, Object> retMap=(Map<String, Object>) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
            	fillInDeliverInfoAllDTO(deliverInfoDto, retMap);
				deliverInfoList.add(deliverInfoDto);
			}
			page.setItems((List)deliverInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * ��Ϊ����������ͬ�ֶ���Ҫȡ���������Ի���һ�����ݿ��ѯ������
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
//	public List executeQuery4Deliver(String sql, List<Object> param) throws Exception{
//		ResultSet rs = null;
//        List list = null;
//        Connection connection=null;
//        PreparedStatement preparedStatement = null;
//        ReturnObject ro=new ReturnObject();
//        Pagination page=new Pagination();
//		try {
//			connection = this.getConnection();
//		} catch (Exception e1) {
//			throw new Exception("�������ݿ�����ʧ�ܣ�");
//		}
//       try {
//       	 preparedStatement = connection.prepareStatement(sql);
//       	 if(param!=null){
//           	 int len=param.size();
//           	 if(len>0){
//                     for (int i = 0; i < len; i++) {
//                   	  preparedStatement.setObject(i+1, param.get(i));
//                     }
//           	 }
//       	 }
//                 rs = preparedStatement.executeQuery();
//                 list = new ArrayList();
//                 ResultSetMetaData rsm = rs.getMetaData();
//                 Map map = null;
//                 while (rs.next()) {
//                          map = new HashMap();
//                          for (int i = 1; i <= rsm.getColumnCount(); i++) {
//                              map.put(rsm.getColumnLabel(i), rs.getObject(rsm.getColumnLabel(i)));
//                          }
//                          list.add(map);
//                 }
//        } catch (SQLException e) {
//                 throw new Exception("ִ�����ݿ��ѯ����ʧ�ܣ�");
//        }finally{
//                 this.closeAll(connection, preparedStatement, null);
//        }
//       return list;
//	}

	/**
	 * get next deliver id of deliver table
	 * @return
	 * @throws Exception
	 */
	public int getNextDeliverID() throws Exception{

		int id = -1;
		Connection conn = getConnection();

		try {
			conn.setAutoCommit(false);
			
			id = getNextID(conn, "deliver_info");
			
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			System.out.println("get next deliver id failed");
			throw new Exception(DataBaseCommonInfo.GET_NEXT_ID_FAILED);
		}finally{
			conn.close();
		}
		return id;
	}
	
	
//==================================================================================================================================	

	/**
	 * ����һ���ͻ���Ϣ������Ĳ��������
	 * 1.���ݽ���������ͻ���Ϣ���ѷ���map�У������ͻ���Ϣ��ĵ�����Ϣ���ж��Ƿ��Ѿ�������ͬ�ļ�¼������������ֶκ���Ҫ��
	 * 2.������ڣ��׳��쳣���粻���ڣ�ִ�в��롣
	 * @param map
	 * @return
	 * @throws Exception
	 */
//	public boolean addDeliverInfo(Map<String,Object> commonMap,Map<String,Object> uniMap) throws Exception{
//
//	 try{
//		 //0.�������У�飺
////		 Float unit_price=(Float)uniMap.get("unit_price");
//		 Float unit_price=Float.valueOf(String.valueOf(uniMap.get("unit_price")));
//		 String brand=(String)uniMap.get("brand");
//		 String sub_brand=(String)uniMap.get("sub_brand");
//		 String standard=(String)uniMap.get("standard");
////		 Integer quantity=(Integer)uniMap.get("quantity");
//		 Integer quantity=Integer.valueOf(String.valueOf(uniMap.get("quantity")));
////		 List<Object> paramList = null;
//		 List<Object> paramList =new ArrayList<Object>();
//		 paramList.add(unit_price);
//		 paramList.add(brand);
//		 paramList.add(sub_brand);
//		 paramList.add(standard);
//		 paramList.add(quantity);
//		 
//		 if(!inputCheck(paramList)){
//			 throw new Exception("����У�鲻ͨ�����в�����Ҫ����Ϊ�գ������ύ�ͻ���Ϣ��");
//		 }
//		 //by Ding: ����ע����
////		 if(!queryRepertory4Deliver(uniMap)){
////			 throw new Exception("���ĸ���Ʒ���������ͻ����и���Ʒ��������治�㣬���޸��ͻ����и���Ʒ������");
////		 }
//		//1.���������û���Ϣֵ������param�У�ADD your code below:
//		boolean isExist=isExistDeliverInfo(commonMap,uniMap);
//		if(isExist){
//			throw new Exception("���ڿͻ�"+commonMap.get("customer_name")+"�Ѿ�������ͬ���ͻ���Ϣ��Ʒ�ƣ���Ʒ�ƣ���������ֱ�Ϊ��"+uniMap.get("brand")+","+uniMap.get("sub_brand")+","+uniMap.get("standard")+","+uniMap.get("quantity"));
//		}
//		String sql_uni="insert into deliver_info(order_num,"
//		+"brand,sub_brand,unit_price,unit,standard,quantity,"
//		+"reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?)";
//		
//		/*String sql_common="insert into deliver_common_info(id,customer_area,customer_name,deliver_addr,"
//		+"deliver_time,total_price,real_price,"
//		+"is_print,telephone,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?,?)";*/
//		
//		Object[] uni_params_temp={commonMap.get("order_num"),uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),
//				uniMap.get("reserve1"),uniMap.get("reserve2"),uniMap.get("reserve3")};//����map
//		
//		/*Object[] common_params_temp={commonMap.get("order_num"),commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("deliver_addr"),
//				commonMap.get("deliver_time"),commonMap.get("total_price"),commonMap.get("real_price"),commonMap.get("is_print"),commonMap.get("telephone"),commonMap.get("reserve1"),commonMap.get("reserve2"),commonMap.get("reserve3")};*///����map
//		
//		List<Object> uni_params=objectArray2ObjectList(uni_params_temp);
//		
//		//List<Object> common_params=objectArray2ObjectList(common_params_temp);
//		//2.���ýӿ�ִ�в���
//		//BaseAction tempAction=new BaseAction();
//		int suninum=executeUpdate(sql_uni,uni_params);
//		//int scomnum=executeUpdate(sql_common,common_params);
//		if(suninum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
//			throw new Exception("�����ͻ���Ϣʧ�ܣ���������!");
//		}
//		else if(suninum==1){
//			try{
//				GoodsInfoService tempService=new GoodsInfoService();
//				tempService.addGoodsInfo(uniMap);
//			}catch(Exception e){
//				//System.out.println("�������еĻ�Ʒ�Ѵ��ڻ�Ʒ��Ϣ���У������ظ����롣");
//			}finally{
//				//by Ding: ����ע����
////				boolean rest=updateBatchInfoAndDeliverInfo(uniMap);
//			}
//		}
//		}catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new Exception("�����ͻ���Ϣʧ��!"+e.getMessage());
//		}
//	 return true;
//	}
	
	
	/**
	 * ���������ͻ���Ϣ
	 * @param listMap װ���Ƕ����ͻ���Ϣ
	 * @return
	 * @throws Exception
	 */
//	public boolean batchAddDeliverInfo (Map<String,Object> commonMap,List<Map<String,Object>> listMap) throws Exception{
//		boolean ret_total=true;//ִ����������ķ���ֵ
//		int num=listMap.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=addDeliverInfo(commonMap,listMap.get(j));//ִ��һ�β���Ľ��
//				if(ret_one==false){
//					ret_total=false;
//					return ret_total;
//				}
//			}
//		}catch(Exception e){
//			throw new Exception("ִ�����������ͻ���Ϣ�쳣��");
//		}
//		return ret_total;
//	}		
	/**
	 * �������У��
	 * @param list
	 * @return
	 */
//	private boolean inputCheck(List<Object> list){
//		int j=list.size();
//		for(int i=0;i<j;i++){
//			if(list.get(i)==null||list.get(i).toString().length()==0){
//				return false;
//			}
//		}
//		return true;
//	}
//	private boolean updateBatchInfoAndDeliverInfo(Map<String,Object> uniMap) throws Exception{
//		String sql_query_batch="select * from goods_batch_info where brand=? and sub_brand=?"
//				+" and standard=? order by batch_no";
//		Object[] params_tmp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard")};
//
//		List<Object> params=objectArray2ObjectList(params_tmp);
//		List list=null;
//		try{
//			Integer quantity4Deliver=(Integer)(uniMap.get("quantity"));
//			list=executeQuery(sql_query_batch, params);
//			Integer sum=0;
//			Integer lastSecondSum=0;
//			int last_index=0;
//			int last_index_quantity=0;//�������ʣ�������
//			int last_index_deliver=0;//��������ͳ�ȥ������
//			float deliver_total_price=0;
//			for(int i=0;i<list.size();i++){
//				
//            	Map retMap=(Map) list.get(i);
//            	Integer quantity=(Integer)retMap.get("quantity");
//            	lastSecondSum=sum;
//            	sum=sum+quantity;
//            	if(sum>quantity4Deliver){
//            		last_index=i;
//            		last_index_quantity=sum-quantity4Deliver;
//            		last_index_deliver=quantity4Deliver-lastSecondSum;
//            		break;
//            	}
//			}
//			for(int i=0;i<last_index;i++){
//				Map retMap=(Map) list.get(i);
//				Float unit_price=(Float)retMap.get("unit_price");
//				Integer quantity=(Integer)retMap.get("quantity");
//				String batchNo=(String)retMap.get("batch_no");
//				deliver_total_price+=unit_price*quantity;
//				//ɾ���Ѿ����͵��Ļ�Ʒ���Σ��Ա�������κ�
//				deleteGoodsBatchInfo(uniMap,batchNo);
//			}
//			if(last_index_deliver>0){
//				Map retMap=(Map) list.get(last_index);
//				Float unit_price=(Float)retMap.get("unit_price");
//				//Integer quantity=(Integer)retMap.get("quantity");
//				String batchNo=(String)retMap.get("batch_no");
//				deliver_total_price+=unit_price*last_index_deliver;
//				//�������һ�����λ�Ʒ��ʣ������
//				updateLastBatchQuantity(uniMap,batchNo,last_index_quantity);
//			}
//			
//			//ʣ�����λ�Ʒ�����κ�ǰ��
//			for(int i=last_index;i<list.size();i++){
//				Map retMap=(Map) list.get(last_index);
//				String batchNo=(String)retMap.get("batch_no");
//				String newBatchNo=String.valueOf(Integer.parseInt(batchNo)-last_index);
//				updateGoodsBatchInfo(uniMap,batchNo,newBatchNo);
//			}
//			//���¸����ͻ���¼����Ӧ�Ľ������ܼۣ�Ҳ���Ǹ��»�Ʒ���α�
//			updateDeliverInfo4TotaldeliverPrice(uniMap,deliver_total_price);
//		}catch(Exception e){
//			throw new Exception("�ͻ�ʱ��ѯ���ͻ�Ʒ���α�����쳣"+e.getMessage());
//		}
//		if(list==null||list.size()==0)
//		{
//			return false;
//		}
//		return true;
//	}
	
//	private boolean updateDeliverInfo4TotaldeliverPrice(Map<String,Object> uniMap,float deliver_total_price) throws Exception{
//		String sql="update deliver_info di set "
//				+" di.reserve1=? where di.brand=? and di.sub_brand=? "
//				+" and di.standard=? and di.order_num=?";
//		Object[] params_temp={deliver_total_price,uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard"),uniMap.get("order_num")};
//		List<Object> params=objectArray2ObjectList(params_temp);
//		try {
//			int rows=executeUpdate(sql,params);
//			if(rows!=1){
//				throw new Exception("����һ���ͻ���Ϣ��Ӧ���ܼ���Ϣʧ��");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new Exception("����һ���ͻ���Ϣ��Ӧ���ܼ���Ϣʧ��"+":"+e.getMessage());
//		}
//		return true;
//	}
	/**
	 * description:�жϿ�������Ƿ���㣬����ɸñ��ͻ�
	 * @param uniMap
	 * @return
	 * @throws Exception 
	 */
//	private boolean queryRepertory4Deliver(Map<String,Object> uniMap) throws Exception{
//		String sql_query_batch="select sum(quantity) quantity from goods_batch_info where brand=? and sub_brand=?"
//				+" and standard=?";
//		Object[] params_tmp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard")};
//
//		List<Object> params=objectArray2ObjectList(params_tmp);
//		List list=null;
//		try{
//			list=executeQuery(sql_query_batch, params);
//			Map retMap=(Map) list.get(0);
//			Integer repertory=(Integer)(retMap.get("quantity"));
//			Integer quantity=(Integer)(uniMap.get("quantity"));
//			if(repertory<quantity){
//				return false;
//			}
//		}catch(Exception e){
//			throw new Exception("��ѯ���ʧ��",e);
//		}
//		return true;
//	}
//	private boolean updateLastBatchQuantity(Map<String,Object> uniMap,String batchNo,Integer quantity) throws Exception{
//		String sql="update goods_batch_info set quantity=?"
//				+" where brand=? and sub_brand=? and standard=? and batch_no=?";
//		Object[] params_temp={quantity,uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard"),batchNo};
//		List<Object> params=objectArray2ObjectList(params_temp);
//		try {
//			int rows=executeUpdate(sql,params);
//			if(rows!=1){
//				throw new Exception("���»�Ʒ���α������һ���ͻ����εĵ�ʣ���Ʒ����¼ʧ�ܣ����²����ķ�����Ŀ����Ϊ1��");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new Exception("���»�Ʒ���α������һ���ͻ����εĵ�ʣ���Ʒ����¼ʧ�ܣ�"+e.getMessage());
//		}
//		return true;
//		
//	}

	/**
	 * description:�ж��Ƿ������ͬ�ļ�¼
	 * @param map
	 * @return
	 * @throws Exception
	 */
//	private boolean isExistDeliverInfo(Map<String,Object> commonMap,Map<String,Object> uniMap) throws Exception{
//		BaseAction tempAction=new BaseAction();
//		String sql="select * from deliver_info di where 1=1"
//		+" and di.order_num=?  and di.brand=? and di.sub_brand=? and di.standard=?";
//
//		
//		Object[] params_tmp={commonMap.get("order_num"),uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard")};
//
//		List<Object> params=objectArray2ObjectList(params_tmp);
//		List list=null;
//		try{
//			list=tempAction.executeQuery(sql, params);
//		}catch(Exception e){
//			throw new Exception("��ѯ�Ƿ��Ѵ��ڽ�Ҫ����ļ�¼�����쳣"+e.getMessage());
//		}
//		if(list==null||list.size()==0)
//		{
//			return false;
//		}
//
//		return true;
//	}
	
	/**
	 * description:��ӡ���ܣ�������ʵ�ֶ��ͻ���Common���ֵ��޸�
	 */
//	public void print_voucher(Map<String,Object> commonMap,Map<String,Object> uniMap){
//		try {
//			addCommonPart(commonMap);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * ɾ��һ��goodsBatch��Ϣ����batchNo��ʶ��
	 * @param id
	 * @return
	 * @throws Exception
	 */
//	public boolean deleteGoodsBatchInfo(Map<String,Object> uniMap,String batchNo) throws Exception{
//		String sql="delete from goods_batch_info  where brand=? and sub_brand=? and standard=? and batch_no=?";
//		Object[] params_temp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard"),batchNo};
//		List<Object> params=objectArray2ObjectList(params_temp);
//		try {
//			int rows=executeUpdate(sql,params);
//			if(rows!=1){
//				throw new Exception("ɾ��һ��ָ����¼ʧ�ܣ�ɾ�������ķ�����Ŀ����Ϊ1��");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new Exception("ִ��ɾ����¼�Ĳ���ʧ�ܣ�"+e.getMessage());
//		}
//		return true;
//	}
	
	/**
	 * @throws Exception 
	 * 
	 */
//	private boolean updateGoodsBatchInfo(Map<String,Object> uniMap,String batchNo,String newBatchNo) throws Exception{
//		String sql="update goods_batch_info set batch_no=?"
//				+" where brand=? and sub_brand=? and standard=? and batch_no=?";
//		Object[] params_temp={batchNo,uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard"),newBatchNo};
//		List<Object> params=objectArray2ObjectList(params_temp);
//		try {
//			int rows=executeUpdate(sql,params);
//			if(rows!=1){
//				throw new Exception("����һ��ָ����Ʒ���μ�¼ʧ�ܣ����²����ķ�����Ŀ����Ϊ1��");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new Exception("ִ�и��»�Ʒ���μ�¼�Ĳ���ʧ�ܣ�"+e.getMessage());
//		}
//		return true;
//	}
	
	/**
	 * ����ɾ���ͻ���Ϣ
	 * @param listId
	 * @return
	 * @throws Exception
	 */
//	public boolean batchDeleteDeliverInfo(List<Integer> listId) throws Exception{
//		boolean ret_total=true;//ִ������ɾ���ķ���ֵ
//		int num=listId.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=deleteDeliverInfo(listId.get(j));//ִ��ɾ��һ����¼�ķ���ֵ
//				if(ret_one!=true){
//					ret_total=false;
//					return ret_total;
//				}
//			}
//		}catch(Exception e){
//			throw new Exception("ִ������ɾ���ͻ���Ϣ�쳣��"+":"+e.getMessage());
//		}
//		return ret_total;
//	}
	
	/**
	 * ����ID����һ���ͻ���Ϣ�������Ϊ��
	 * 1.У����º�������Ƿ��������������ظ��������
	 * 2.����ID���������ֶΣ�����ĳЩ�ֶο���û�б仯����
	 * @param id
	 * @param map
	 * @return
	 * @throws Exception
	 */
//	public boolean updateDeliverInfo(String id,Map<String,Object> commonMap, Map<String,Object> uniMap) throws Exception{
//		/*float total_price=0;
//		if(uniMap.get("unit_price")!=null&&uniMap.get("quantity")!=null){
//			total_price=((Float)uniMap.get("unit_price"))*((Float)uniMap.get("quantity"));
//			commonMap.put("total_price", total_price);
//		}*/
////		String sql="update deliver_info di set "
////				+" di.brand=?,di.sub_brand=?,di.unit_price=?,"
////				+" di.unit=?,di.standard=?,di.quantity=? where di.id=?";
//		//by Ding: ���滻����������
//		String sql="update deliver_info di set "
//				+" di.brand=?,di.sub_brand=?,di.unit_price=?,"
//				+" di.unit=?,di.standard=?,di.quantity=?, di.order_num=? where di.id=?";
//		Object[] params_temp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),uniMap.get("order_num"),
//				id};
//		List<Object> params=objectArray2ObjectList(params_temp);
//		try {
//			boolean isExist=isExistDeliverInfo(commonMap,uniMap);
//			if(isExist){
//				throw new Exception("���ڿͻ�"+commonMap.get("customer_name")+"�Ѿ�������ͬ���ͻ���Ϣ��Ʒ�ƣ���Ʒ�ƣ���������ֱ�Ϊ��"+uniMap.get("brand")+","+uniMap.get("sub_brand")+","+uniMap.get("standard")+","+uniMap.get("quantity"));
//			}
//			int rows=executeUpdate(sql,params);
//			if(rows!=1){
//				throw new Exception("���µ�����Ϣʧ��");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new Exception("�����ͻ���Ϣʧ��"+":"+e.getMessage());
//		}
//		return true;
//	}
//	
	/**
	 * ���������ͻ���Ϣ
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */

//	public boolean batchUpdateDeliverInfo(List<String> listId,Map<String,Object> commonMap,List<Map<String,Object>> listUniMap) throws Exception{
//
//		boolean ret_total=true;//ִ���������µķ���ֵ
//		int num=listId.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=updateDeliverInfo(listId.get(j),commonMap,listUniMap.get(j));//ִ��һ�θ��µĽ��
//				if(ret_one!=true){
//					ret_total=false;
//					return ret_total;
//				}
//			}
//		}catch(Exception e){
//			throw new Exception("ִ����������������Ϣ�쳣��");
//		}
//		return ret_total;
//
//	}
//	

	/**
	 * ��ѯ�����ͻ���Ϣ��������ѯ����������չʾ��ߵ��ͻ���Ϣ��Ӧ����һ����ѯ�������ͻ����ڣ�
	 */
//	public ReturnObject queryDeliverInfoAll() throws Exception{
//		//ReturnObject ro=null;
//		List list=null;
//		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
//		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
//		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
//		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";
//		//Object[] params={};
//		List<Object> params=null;
//		Pagination page = new Pagination();
//		ReturnObject ro=new ReturnObject();
//		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
//		try {
//			list=executeQuery4Deliver(sql, params);
//			
//            for(int i=0;i<list.size();i++){
//            	
//            	Map retMap=(Map) list.get(i);
//            	DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
//            	deliverInfoDto.setUni_id(String.valueOf(retMap.get("uniId")));
//            	deliverInfoDto.setCommon_id((String)retMap.get("commonId"));
//				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
//				deliverInfoDto.setCustomer_name((String) retMap.get("customer_name"));
//				deliverInfoDto.setDeliver_addr((String) retMap.get("deliver_addr"));
//				deliverInfoDto.setOrder_num((String) retMap.get("order_num"));
//				deliverInfoDto.setTotal_price((Float)retMap.get("total_price"));
//				deliverInfoDto.setReal_price((Float)retMap.get("real_price"));
//				deliverInfoDto.setIs_print((String) retMap.get("is_print"));
//				deliverInfoDto.setTelephone((String) retMap.get("telephone"));
//				deliverInfoDto.setBrand((String) retMap.get("brand"));
//				deliverInfoDto.setSub_brand((String) retMap.get("sub_brand"));
//				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
//				deliverInfoDto.setDeliver_time((String) retMap.get("deliver_time"));
//				deliverInfoDto.setCommon_reserve1((String) retMap.get("commonReserve1"));
//				deliverInfoDto.setCommon_reserve2((String) retMap.get("commonReserve2"));
//				deliverInfoDto.setCommon_reserve3((String) retMap.get("commonReserve3"));
//				deliverInfoDto.setUni_reserve1((String) retMap.get("uniReserve1"));
//				deliverInfoDto.setUni_reserve2((String) retMap.get("uniReserve2"));
//				deliverInfoDto.setUni_reserve3((String) retMap.get("uniReserve3"));
//				deliverInfoDto.setStandard((String) retMap.get("standard"));
//				deliverInfoDto.setUnit((String) retMap.get("unit"));
//				deliverInfoDto.setUnit_price((Float) retMap.get("unit_price"));
//				deliverInfoList.add(deliverInfoDto);
//			}
//			page.setItems((List)deliverInfoList);
//			ro.setReturnDTO(page);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new Exception("��ѯ������Ϣʧ�ܣ�");
//		}
//		return ro;
//	}
	
}
