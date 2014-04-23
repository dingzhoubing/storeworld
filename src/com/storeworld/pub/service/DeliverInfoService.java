package com.storeworld.pub.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.DeliverInfoAllDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.GoodsInfoService;
import com.storeworld.utils.Utils;

public class DeliverInfoService extends BaseAction{
	
	/**
	 * ����һ���ͻ���Ϣ������Ĳ��������
	 * 1.���ݽ���������ͻ���Ϣ���ѷ���map�У������ͻ���Ϣ��ĵ�����Ϣ���ж��Ƿ��Ѿ�������ͬ�ļ�¼������������ֶκ���Ҫ��
	 * 2.������ڣ��׳��쳣���粻���ڣ�ִ�в��롣
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean addDeliverInfo(Map<String,Object> commonMap,Map<String,Object> uniMap) throws Exception{

	 try{
		 //0.�������У�飺
//		 Float unit_price=(Float)uniMap.get("unit_price");
		 Float unit_price=Float.valueOf(String.valueOf(uniMap.get("unit_price")));
		 String brand=(String)uniMap.get("brand");
		 String sub_brand=(String)uniMap.get("sub_brand");
		 String standard=(String)uniMap.get("standard");
//		 Integer quantity=(Integer)uniMap.get("quantity");
		 Integer quantity=Integer.valueOf(String.valueOf(uniMap.get("quantity")));
//		 List<Object> paramList = null;
		 List<Object> paramList =new ArrayList<Object>();
		 paramList.add(unit_price);
		 paramList.add(brand);
		 paramList.add(sub_brand);
		 paramList.add(standard);
		 paramList.add(quantity);
		 
		 if(!inputCheck(paramList)){
			 throw new Exception("����У�鲻ͨ�����в�����Ҫ����Ϊ�գ������ύ�ͻ���Ϣ��");
		 }
		 //by Ding: ����ע����
//		 if(!queryRepertory4Deliver(uniMap)){
//			 throw new Exception("���ĸ���Ʒ���������ͻ����и���Ʒ��������治�㣬���޸��ͻ����и���Ʒ������");
//		 }
		//1.���������û���Ϣֵ������param�У�ADD your code below:
		boolean isExist=isExistDeliverInfo(commonMap,uniMap);
		if(isExist){
			throw new Exception("���ڿͻ�"+commonMap.get("customer_name")+"�Ѿ�������ͬ���ͻ���Ϣ��Ʒ�ƣ���Ʒ�ƣ���������ֱ�Ϊ��"+uniMap.get("brand")+","+uniMap.get("sub_brand")+","+uniMap.get("standard")+","+uniMap.get("quantity"));
		}
		String sql_uni="insert into deliver_info(order_num,"
		+"brand,sub_brand,unit_price,unit,standard,quantity,"
		+"reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?)";
		
		/*String sql_common="insert into deliver_common_info(id,customer_area,customer_name,deliver_addr,"
		+"deliver_time,total_price,real_price,"
		+"is_print,telephone,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?,?)";*/
		
		Object[] uni_params_temp={commonMap.get("order_num"),uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),
				uniMap.get("reserve1"),uniMap.get("reserve2"),uniMap.get("reserve3")};//����map
		
		/*Object[] common_params_temp={commonMap.get("order_num"),commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("deliver_addr"),
				commonMap.get("deliver_time"),commonMap.get("total_price"),commonMap.get("real_price"),commonMap.get("is_print"),commonMap.get("telephone"),commonMap.get("reserve1"),commonMap.get("reserve2"),commonMap.get("reserve3")};*///����map
		
		List<Object> uni_params=objectArray2ObjectList(uni_params_temp);
		
		//List<Object> common_params=objectArray2ObjectList(common_params_temp);
		//2.���ýӿ�ִ�в���
		//BaseAction tempAction=new BaseAction();
		int suninum=executeUpdate(sql_uni,uni_params);
		//int scomnum=executeUpdate(sql_common,common_params);
		if(suninum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
			throw new Exception("�����ͻ���Ϣʧ�ܣ���������!");
		}
		else if(suninum==1){
			try{
				GoodsInfoService tempService=new GoodsInfoService();
				tempService.addGoodsInfo(uniMap);
			}catch(Exception e){
				//System.out.println("�������еĻ�Ʒ�Ѵ��ڻ�Ʒ��Ϣ���У������ظ����롣");
			}finally{
				//by Ding: ����ע����
//				boolean rest=updateBatchInfoAndDeliverInfo(uniMap);
			}
		}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("�����ͻ���Ϣʧ��!"+e.getMessage());
		}
	 return true;
	}
	
	//by Ding: ������ӵ�
	public boolean addDeliverInfo(Map<String,Object> uniMap) throws Exception{

		 try{
			 //0.�������У�飺
//			 Float unit_price=(Float)uniMap.get("unit_price");
			 Float unit_price=Float.valueOf(String.valueOf(uniMap.get("unit_price")));
			 String brand=(String)uniMap.get("brand");
			 String sub_brand=(String)uniMap.get("sub_brand");
			 String standard=(String)uniMap.get("standard");
//			 Integer quantity=(Integer)uniMap.get("quantity");
			 Integer quantity=Integer.valueOf(String.valueOf(uniMap.get("quantity")));
			 String order_num = String.valueOf(uniMap.get("order_num"));
//			 List<Object> paramList = null;
			 List<Object> paramList =new ArrayList<Object>();
			 paramList.add(unit_price);
			 paramList.add(brand);
			 paramList.add(sub_brand);
			 paramList.add(standard);
			 paramList.add(quantity);
			 paramList.add(order_num);
			 
			 if(!inputCheck(paramList)){
				 throw new Exception("����У�鲻ͨ�����в�����Ҫ����Ϊ�գ������ύ�ͻ���Ϣ��");
			 }

			String sql_uni="insert into deliver_info(order_num,"
			+"brand,sub_brand,unit_price,unit,standard,quantity,"
			+"reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?)";
			
			/*String sql_common="insert into deliver_common_info(id,customer_area,customer_name,deliver_addr,"
			+"deliver_time,total_price,real_price,"
			+"is_print,telephone,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?,?)";*/
			
			Object[] uni_params_temp={uniMap.get("order_num"),uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),
					uniMap.get("reserve1"),uniMap.get("reserve2"),uniMap.get("reserve3")};//����map
			
			/*Object[] common_params_temp={commonMap.get("order_num"),commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("deliver_addr"),
					commonMap.get("deliver_time"),commonMap.get("total_price"),commonMap.get("real_price"),commonMap.get("is_print"),commonMap.get("telephone"),commonMap.get("reserve1"),commonMap.get("reserve2"),commonMap.get("reserve3")};*///����map
			
			List<Object> uni_params=objectArray2ObjectList(uni_params_temp);
			
			//List<Object> common_params=objectArray2ObjectList(common_params_temp);
			//2.���ýӿ�ִ�в���
			//BaseAction tempAction=new BaseAction();
			int suninum=executeUpdate(sql_uni,uni_params);
			//int scomnum=executeUpdate(sql_common,common_params);
			if(suninum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
				throw new Exception("�����ͻ���Ϣʧ�ܣ���������!");
			}
			else if(suninum==1){
				try{
					GoodsInfoService tempService=new GoodsInfoService();
					tempService.addGoodsInfo(uniMap);
				}catch(Exception e){
					//System.out.println("�������еĻ�Ʒ�Ѵ��ڻ�Ʒ��Ϣ���У������ظ����롣");
				}finally{
					//by Ding: ����ע����
//					boolean rest=updateBatchInfoAndDeliverInfo(uniMap);
				}
			}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("�����ͻ���Ϣʧ��!"+e.getMessage());
			}
		 return true;
		}
	
	
	
	/**
	 * �������У��
	 * @param list
	 * @return
	 */
	private boolean inputCheck(List<Object> list){
		int j=list.size();
		for(int i=0;i<j;i++){
			if(list.get(i)==null||list.get(i).toString().length()==0){
				return false;
			}
		}
		return true;
	}
	private boolean updateBatchInfoAndDeliverInfo(Map<String,Object> uniMap) throws Exception{
		String sql_query_batch="select * from goods_batch_info where brand=? and sub_brand=?"
				+" and standard=? order by batch_no";
		Object[] params_tmp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard")};

		List<Object> params=objectArray2ObjectList(params_tmp);
		List list=null;
		try{
			Integer quantity4Deliver=(Integer)(uniMap.get("quantity"));
			list=executeQuery(sql_query_batch, params);
			Integer sum=0;
			Integer lastSecondSum=0;
			int last_index=0;
			int last_index_quantity=0;//�������ʣ�������
			int last_index_deliver=0;//��������ͳ�ȥ������
			float deliver_total_price=0;
			for(int i=0;i<list.size();i++){
				
            	Map retMap=(Map) list.get(i);
            	Integer quantity=(Integer)retMap.get("quantity");
            	lastSecondSum=sum;
            	sum=sum+quantity;
            	if(sum>quantity4Deliver){
            		last_index=i;
            		last_index_quantity=sum-quantity4Deliver;
            		last_index_deliver=quantity4Deliver-lastSecondSum;
            		break;
            	}
			}
			for(int i=0;i<last_index;i++){
				Map retMap=(Map) list.get(i);
				Float unit_price=(Float)retMap.get("unit_price");
				Integer quantity=(Integer)retMap.get("quantity");
				String batchNo=(String)retMap.get("batch_no");
				deliver_total_price+=unit_price*quantity;
				//ɾ���Ѿ����͵��Ļ�Ʒ���Σ��Ա�������κ�
				deleteGoodsBatchInfo(uniMap,batchNo);
			}
			if(last_index_deliver>0){
				Map retMap=(Map) list.get(last_index);
				Float unit_price=(Float)retMap.get("unit_price");
				//Integer quantity=(Integer)retMap.get("quantity");
				String batchNo=(String)retMap.get("batch_no");
				deliver_total_price+=unit_price*last_index_deliver;
				//�������һ�����λ�Ʒ��ʣ������
				updateLastBatchQuantity(uniMap,batchNo,last_index_quantity);
			}
			
			//ʣ�����λ�Ʒ�����κ�ǰ��
			for(int i=last_index;i<list.size();i++){
				Map retMap=(Map) list.get(last_index);
				String batchNo=(String)retMap.get("batch_no");
				String newBatchNo=String.valueOf(Integer.parseInt(batchNo)-last_index);
				updateGoodsBatchInfo(uniMap,batchNo,newBatchNo);
			}
			//���¸����ͻ���¼����Ӧ�Ľ������ܼۣ�Ҳ���Ǹ��»�Ʒ���α�
			updateDeliverInfo4TotaldeliverPrice(uniMap,deliver_total_price);
		}catch(Exception e){
			throw new Exception("�ͻ�ʱ��ѯ���ͻ�Ʒ���α�����쳣"+e.getMessage());
		}
		if(list==null||list.size()==0)
		{
			return false;
		}
		return true;
	}
	
	private boolean updateDeliverInfo4TotaldeliverPrice(Map<String,Object> uniMap,float deliver_total_price) throws Exception{
		String sql="update deliver_info di set "
				+" di.reserve1=? where di.brand=? and di.sub_brand=? "
				+" and di.standard=? and di.order_num=?";
		Object[] params_temp={deliver_total_price,uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard"),uniMap.get("order_num")};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("����һ���ͻ���Ϣ��Ӧ���ܼ���Ϣʧ��");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("����һ���ͻ���Ϣ��Ӧ���ܼ���Ϣʧ��"+":"+e.getMessage());
		}
		return true;
	}
	/**
	 * description:�жϿ�������Ƿ���㣬����ɸñ��ͻ�
	 * @param uniMap
	 * @return
	 * @throws Exception 
	 */
	private boolean queryRepertory4Deliver(Map<String,Object> uniMap) throws Exception{
		String sql_query_batch="select sum(quantity) quantity from goods_batch_info where brand=? and sub_brand=?"
				+" and standard=?";
		Object[] params_tmp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard")};

		List<Object> params=objectArray2ObjectList(params_tmp);
		List list=null;
		try{
			list=executeQuery(sql_query_batch, params);
			Map retMap=(Map) list.get(0);
			Integer repertory=(Integer)(retMap.get("quantity"));
			Integer quantity=(Integer)(uniMap.get("quantity"));
			if(repertory<quantity){
				return false;
			}
		}catch(Exception e){
			throw new Exception("��ѯ���ʧ��",e);
		}
		return true;
	}
	private boolean updateLastBatchQuantity(Map<String,Object> uniMap,String batchNo,Integer quantity) throws Exception{
		String sql="update goods_batch_info set quantity=?"
				+" where brand=? and sub_brand=? and standard=? and batch_no=?";
		Object[] params_temp={quantity,uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard"),batchNo};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("���»�Ʒ���α������һ���ͻ����εĵ�ʣ���Ʒ����¼ʧ�ܣ����²����ķ�����Ŀ����Ϊ1��");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("���»�Ʒ���α������һ���ͻ����εĵ�ʣ���Ʒ����¼ʧ�ܣ�"+e.getMessage());
		}
		return true;
		
	}

	/**
	 * description:�ж��Ƿ������ͬ�ļ�¼
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean isExistDeliverInfo(Map<String,Object> commonMap,Map<String,Object> uniMap) throws Exception{
		BaseAction tempAction=new BaseAction();
		String sql="select * from deliver_info di where 1=1"
		+" and di.order_num=?  and di.brand=? and di.sub_brand=? and di.standard=?";

		
		Object[] params_tmp={commonMap.get("order_num"),uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard")};

		List<Object> params=objectArray2ObjectList(params_tmp);
		List list=null;
		try{
			list=tempAction.executeQuery(sql, params);
		}catch(Exception e){
			throw new Exception("��ѯ�Ƿ��Ѵ��ڽ�Ҫ����ļ�¼�����쳣"+e.getMessage());
		}
		if(list==null||list.size()==0)
		{
			return false;
		}

		return true;
	}
	
	/**
	 * ���������ͻ���Ϣ
	 * @param listMap װ���Ƕ����ͻ���Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean batchAddDeliverInfo (Map<String,Object> commonMap,List<Map<String,Object>> listMap) throws Exception{
		boolean ret_total=true;//ִ����������ķ���ֵ
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=addDeliverInfo(commonMap,listMap.get(j));//ִ��һ�β���Ľ��
				if(ret_one==false){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("ִ�����������ͻ���Ϣ�쳣��");
		}
		return ret_total;
	}
	
	/**
	 * description:��ӡ���ܣ�������ʵ�ֶ��ͻ���Common���ֵ��޸�
	 */
	public void print_voucher(Map<String,Object> commonMap,Map<String,Object> uniMap){
		try {
			addCommonPart(commonMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * by Ding: ������ӵ�
	 * description:��ӡ���ܣ�������ʵ�ֶ��ͻ���Common���ֵ��޸� 2
	 */
	public void print_voucher(Map<String,Object> commonMap){
		try {
			addCommonPart(commonMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean addCommonPart(Map<String,Object> commonMap) throws Exception{
		try{
			String sql_common="insert into deliver_common_info(id,customer_area,customer_name,deliver_addr,"
				+"deliver_time,total_price,real_price,"
				+"is_print,telephone,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			
			Object[] common_params_temp={commonMap.get("order_num"),commonMap.get("customer_area"),commonMap.get("customer_name"),commonMap.get("deliver_addr"),
					commonMap.get("deliver_time"),commonMap.get("total_price"),commonMap.get("real_price"),commonMap.get("is_print"),commonMap.get("telephone"),commonMap.get("reserve1"),commonMap.get("reserve2"),commonMap.get("reserve3")};//����map
			List<Object> common_params=objectArray2ObjectList(common_params_temp);
			int scomnum=executeUpdate(sql_common,common_params);
			if(scomnum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
				return false;
			}
		}catch(Exception e){
			throw new Exception("�����ͻ���ʱ���ͻ����������ֲ��������쳣��");
		}
		return true;
	}
	
	/**
	 * ɾ��һ���ͻ���Ϣ����ID��ʶ��
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
	 * ɾ��һ��goodsBatch��Ϣ����batchNo��ʶ��
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsBatchInfo(Map<String,Object> uniMap,String batchNo) throws Exception{
		String sql="delete from goods_batch_info  where brand=? and sub_brand=? and standard=? and batch_no=?";
		Object[] params_temp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard"),batchNo};
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
	 * @throws Exception 
	 * 
	 */
	private boolean updateGoodsBatchInfo(Map<String,Object> uniMap,String batchNo,String newBatchNo) throws Exception{
		String sql="update goods_batch_info set batch_no=?"
				+" where brand=? and sub_brand=? and standard=? and batch_no=?";
		Object[] params_temp={batchNo,uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("standard"),newBatchNo};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("����һ��ָ����Ʒ���μ�¼ʧ�ܣ����²����ķ�����Ŀ����Ϊ1��");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("ִ�и��»�Ʒ���μ�¼�Ĳ���ʧ�ܣ�"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * ����ɾ���ͻ���Ϣ
	 * @param listId
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteDeliverInfo(List<Integer> listId) throws Exception{
		boolean ret_total=true;//ִ������ɾ���ķ���ֵ
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=deleteDeliverInfo(listId.get(j));//ִ��ɾ��һ����¼�ķ���ֵ
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
	
	/**
	 * ����ID����һ���ͻ���Ϣ�������Ϊ��
	 * 1.У����º�������Ƿ��������������ظ��������
	 * 2.����ID���������ֶΣ�����ĳЩ�ֶο���û�б仯����
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
//		String sql="update deliver_info di set "
//				+" di.brand=?,di.sub_brand=?,di.unit_price=?,"
//				+" di.unit=?,di.standard=?,di.quantity=? where di.id=?";
		//by Ding: ���滻����������
		String sql="update deliver_info di set "
				+" di.brand=?,di.sub_brand=?,di.unit_price=?,"
				+" di.unit=?,di.standard=?,di.quantity=?, di.order_num=? where di.id=?";
		Object[] params_temp={uniMap.get("brand"),uniMap.get("sub_brand"),uniMap.get("unit_price"),uniMap.get("unit"),uniMap.get("standard"),uniMap.get("quantity"),uniMap.get("order_num"),
				id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			boolean isExist=isExistDeliverInfo(commonMap,uniMap);
			if(isExist){
				throw new Exception("���ڿͻ�"+commonMap.get("customer_name")+"�Ѿ�������ͬ���ͻ���Ϣ��Ʒ�ƣ���Ʒ�ƣ���������ֱ�Ϊ��"+uniMap.get("brand")+","+uniMap.get("sub_brand")+","+uniMap.get("standard")+","+uniMap.get("quantity"));
			}
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("���µ�����Ϣʧ��");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("�����ͻ���Ϣʧ��"+":"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * ���������ͻ���Ϣ
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */

	public boolean batchUpdateDeliverInfo(List<String> listId,Map<String,Object> commonMap,List<Map<String,Object>> listUniMap) throws Exception{

		boolean ret_total=true;//ִ���������µķ���ֵ
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=updateDeliverInfo(listId.get(j),commonMap,listUniMap.get(j));//ִ��һ�θ��µĽ��
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("ִ����������������Ϣ�쳣��");
		}
		return ret_total;

	}
	
	
	/**
	 * description����ѯ�����ͻ���Ϣ��������ѯ����������չʾ��ߵ��ͻ���Ϣ��Ӧ����һ����ѯ�������ͻ����ڣ�
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoAll() throws Exception{
		//ReturnObject ro=null;
		List list=null;
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";
		//Object[] params={};
		List<Object> params=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
		try {
			list=executeQuery4Deliver(sql, params);
			
            for(int i=0;i<list.size();i++){
            	
            	Map retMap=(Map) list.get(i);
            	DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
            	deliverInfoDto.setUni_id(String.valueOf(retMap.get("uniId")));
            	deliverInfoDto.setCommon_id((String)retMap.get("commonId"));
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setCustomer_name((String) retMap.get("customer_name"));
				deliverInfoDto.setDeliver_addr((String) retMap.get("deliver_addr"));
				deliverInfoDto.setOrder_num((String) retMap.get("order_num"));
				deliverInfoDto.setTotal_price((Float)retMap.get("total_price"));
				deliverInfoDto.setReal_price((Float)retMap.get("real_price"));
				deliverInfoDto.setIs_print((String) retMap.get("is_print"));
				deliverInfoDto.setTelephone((String) retMap.get("telephone"));
				deliverInfoDto.setBrand((String) retMap.get("brand"));
				deliverInfoDto.setSub_brand((String) retMap.get("sub_brand"));
				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				deliverInfoDto.setDeliver_time((String) retMap.get("deliver_time"));
				deliverInfoDto.setCommon_reserve1((String) retMap.get("commonReserve1"));
				deliverInfoDto.setCommon_reserve2((String) retMap.get("commonReserve2"));
				deliverInfoDto.setCommon_reserve3((String) retMap.get("commonReserve3"));
				deliverInfoDto.setUni_reserve1((Float) retMap.get("uniReserve1"));
				deliverInfoDto.setUni_reserve2((String) retMap.get("uniReserve2"));
				deliverInfoDto.setUni_reserve3((String) retMap.get("uniReserve3"));
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
			throw new Exception("��ѯ������Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * description:��ѯ����������ĳ�����ͻ���Ϣ
	 * @param map��װ���ǲ�ѯ����
	 * @return
	 * @throws Exception 
	 */
	public ReturnObject queryDeliverInfo(Map<String,Object> map) throws Exception{
		List list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<DeliverInfoAllDTO> deliverInfoList = new ArrayList<DeliverInfoAllDTO>();
		String uniId=(String) map.get("uniId");
		String commonId=(String) map.get("commonId");
		String customer_area=(String) map.get("customer_area");
		String customer_name=(String) map.get("customer_name");
		String brand=(String) map.get("brand");
		String sub_brand=(String) map.get("sub_brand");
		String standard=(String) map.get("standard");
		String order_num=(String) map.get("order_num");
		String deliver_time=(String) map.get("deliver_time");
		String uniReserve1=(String) map.get("uniReserve1");
		String uniReserve2=(String) map.get("uniReserve2");
		String uniReserve3=(String) map.get("uniReserve3");
		
		String sql="select dci.id commonId,dci.customer_area,dci.customer_name,dci.deliver_addr,dci.total_price,"
		+"dci.real_price,dci.deliver_time,dci.is_print,dci.telephone,dci.reserve1 commonReserve1,dci.reserve2 commonReserve2,dci.reserve3 commonReserve3,"
		+"di.id uniId,di.order_num,di.brand,di.sub_brand,di.unit_price,di.unit,di.quantity,di.standard,"
		+"di.reserve1 uniReserve1,di.reserve2 uniReserve2,di.reserve3 uniReserve3 from deliver_common_info dci,deliver_info di where dci.id=di.order_num";
		//Object[] params=new Object[]{};
		List<Object> params = new ArrayList<Object>();
		int p_num=0;
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
			sql=sql+" and uniReserve3s=?";
			params.add(uniReserve3);
		}
		
		
		try {
			list=executeQuery4Deliver(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
            	deliverInfoDto.setUni_id(String.valueOf(retMap.get("uniId")));
            	deliverInfoDto.setCommon_id((String)retMap.get("commonId"));
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setCustomer_name((String) retMap.get("customer_name"));
				deliverInfoDto.setDeliver_addr((String) retMap.get("deliver_addr"));
				deliverInfoDto.setOrder_num((String) retMap.get("order_num"));
				deliverInfoDto.setTotal_price((Float)retMap.get("total_price"));
				deliverInfoDto.setReal_price((Float)retMap.get("real_price"));
				deliverInfoDto.setIs_print((String) retMap.get("is_print"));
				deliverInfoDto.setTelephone((String) retMap.get("telephone"));
				deliverInfoDto.setBrand((String) retMap.get("brand"));
				deliverInfoDto.setSub_brand((String) retMap.get("sub_brand"));
//				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				//by Ding: ���滻����������
				deliverInfoDto.setQuantity(String.valueOf(retMap.get("quantity")));
				
				deliverInfoDto.setDeliver_time((String) retMap.get("deliver_time"));
				deliverInfoDto.setCommon_reserve1((String) retMap.get("commonReserve1"));
				deliverInfoDto.setCommon_reserve2((String) retMap.get("commonReserve2"));
				deliverInfoDto.setCommon_reserve3((String) retMap.get("commonReserve3"));
				deliverInfoDto.setUni_reserve1((Float) retMap.get("uniReserve1"));
				deliverInfoDto.setUni_reserve2((String) retMap.get("uniReserve2"));
				deliverInfoDto.setUni_reserve3((String) retMap.get("uniReserve3"));
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
			throw new Exception("��ѯ��Ʒ��Ϣʧ�ܣ�");
		}
		return ro;
	}
	/**
	 * Ĭ�ϵ��ͻ���¼��ѯ����ѯ����
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByDefaultDelivertime(Map<String,Object> map) throws Exception{
		List list=null;
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
		//Object[] params=new Object[]{};
		List<Object> params = new ArrayList<Object>();
		int p_num=0;
		if(Utils.isNotNull(deliver_time)){
			sql=sql+" and dci.deliver_time>? and dci.deliver_time<?";
			params.add(start_time);
			params.add(end_time);
		}
		
		
		try {
			list=executeQuery4Deliver(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
            	deliverInfoDto.setUni_id(String.valueOf(retMap.get("uniId")));
            	deliverInfoDto.setCommon_id((String)retMap.get("commonId"));
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setCustomer_name((String) retMap.get("customer_name"));
				deliverInfoDto.setDeliver_addr((String) retMap.get("deliver_addr"));
				deliverInfoDto.setOrder_num((String) retMap.get("order_num"));
				deliverInfoDto.setTotal_price((Float)retMap.get("total_price"));
				deliverInfoDto.setReal_price((Float)retMap.get("real_price"));
				deliverInfoDto.setIs_print((String) retMap.get("is_print"));
				deliverInfoDto.setTelephone((String) retMap.get("telephone"));
				deliverInfoDto.setBrand((String) retMap.get("brand"));
				deliverInfoDto.setSub_brand((String) retMap.get("sub_brand"));
//				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				//by Ding: ���滻����������
				deliverInfoDto.setQuantity(String.valueOf(retMap.get("quantity")));
				
				deliverInfoDto.setDeliver_time((String) retMap.get("deliver_time"));
				deliverInfoDto.setCommon_reserve1((String) retMap.get("commonReserve1"));
				deliverInfoDto.setCommon_reserve2((String) retMap.get("commonReserve2"));
				deliverInfoDto.setCommon_reserve3((String) retMap.get("commonReserve3"));
				deliverInfoDto.setUni_reserve1((Float) retMap.get("uniReserve1"));
				deliverInfoDto.setUni_reserve2((String) retMap.get("uniReserve2"));
				deliverInfoDto.setUni_reserve3((String) retMap.get("uniReserve3"));
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
			throw new Exception("��ѯ�ͻ���Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * ��ѯ����ĳһ����ͻ���¼��ʱ��������룬��ȷ���죿����֧���·ݣ�
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryDeliverInfoByInputDelivertime(Map<String,Object> map) throws Exception{
		List list=null;
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
		//Object[] params=new Object[]{};
		List<Object> params = new ArrayList<Object>();
		int p_num=0;
		if(Utils.isNotNull(deliver_time)){
			sql=sql+" and dci.deliver_time>? and dci.deliver_time<?";
			params.add(start_time);
			params.add(end_time);
		}
		
		
		try {
			list=executeQuery4Deliver(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				DeliverInfoAllDTO deliverInfoDto=new DeliverInfoAllDTO();
            	deliverInfoDto.setUni_id(String.valueOf(retMap.get("uniId")));
            	deliverInfoDto.setCommon_id((String)retMap.get("commonId"));
				deliverInfoDto.setCustomer_area((String)retMap.get("customer_area"));
				deliverInfoDto.setCustomer_name((String) retMap.get("customer_name"));
				deliverInfoDto.setDeliver_addr((String) retMap.get("deliver_addr"));
				deliverInfoDto.setOrder_num((String) retMap.get("order_num"));
				deliverInfoDto.setTotal_price((Float)retMap.get("total_price"));
				deliverInfoDto.setReal_price((Float)retMap.get("real_price"));
				deliverInfoDto.setIs_print((String) retMap.get("is_print"));
				deliverInfoDto.setTelephone((String) retMap.get("telephone"));
				deliverInfoDto.setBrand((String) retMap.get("brand"));
				deliverInfoDto.setSub_brand((String) retMap.get("sub_brand"));
				//by Ding: ���滻����������
//				deliverInfoDto.setQuantity((String) retMap.get("quantity"));
				deliverInfoDto.setQuantity(String.valueOf(retMap.get("quantity")));
				
				deliverInfoDto.setDeliver_time((String) retMap.get("deliver_time"));
				deliverInfoDto.setCommon_reserve1((String) retMap.get("commonReserve1"));
				deliverInfoDto.setCommon_reserve2((String) retMap.get("commonReserve2"));
				deliverInfoDto.setCommon_reserve3((String) retMap.get("commonReserve3"));
				deliverInfoDto.setUni_reserve1((Float) retMap.get("uniReserve1"));
				deliverInfoDto.setUni_reserve2((String) retMap.get("uniReserve2"));
				deliverInfoDto.setUni_reserve3((String) retMap.get("uniReserve3"));
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
	public List executeQuery4Deliver(String sql, List<Object> param) throws Exception{
		ResultSet rs = null;
        List list = null;
        Connection connection=null;
        PreparedStatement preparedStatement = null;
        ReturnObject ro=new ReturnObject();
        Pagination page=new Pagination();
		try {
			connection = this.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new Exception("�������ݿ�����ʧ�ܣ�");
		}
       try {
       	 preparedStatement = connection.prepareStatement(sql);
       	 if(param!=null){
           	 int len=param.size();
           	 if(len>0){
                     for (int i = 0; i < len; i++) {
                   	  preparedStatement.setObject(i+1, param.get(i));
                     }
           	 }
       	 }
                 rs = preparedStatement.executeQuery();
                 list = new ArrayList();
                 ResultSetMetaData rsm = rs.getMetaData();
                 Map map = null;
                 while (rs.next()) {
                          map = new HashMap();
                          for (int i = 1; i <= rsm.getColumnCount(); i++) {
                              map.put(rsm.getColumnLabel(i), rs.getObject(rsm.getColumnLabel(i)));
                          }
                          list.add(map);
                 }
        } catch (SQLException e) {
                 e.printStackTrace();
                 throw new Exception("ִ�����ݿ��ѯ����ʧ�ܣ�");
        }finally{
                 this.closeAll(connection, preparedStatement, null);
        }
       return list;
	}

	public int getNextDeliverID() throws Exception{
		return getNextID("deliver_info");
	}
}
