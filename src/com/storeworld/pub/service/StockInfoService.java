package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.utils.Utils;

public class StockInfoService extends BaseAction{
	
	/**
	 * ����һ��������Ϣ������Ĳ��������
	 * 1.���ݽ�������Ľ�����Ϣ���ѷ���map�У����������Ϣ��ĵ�����Ϣ���ж��Ƿ��Ѿ�������ͬ�ļ�¼��������������ֶκ���Ҫ��
	 * 2.������ڣ��׳��쳣���粻���ڣ�ִ�в��롣
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean addStockInfo(Map<String,Object> map) throws Exception{

	 try{
		//1.���������û���Ϣֵ������param�У�ADD your code below:
		boolean isExist=isExistStockInfo(map);
		if(isExist){
			throw new Exception("�Ѿ�������ͬ�Ľ�����Ϣ��Ʒ�ƣ���Ʒ�ƣ���񣬽���ʱ��ֱ�Ϊ��"+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+","+map.get("stock_time"));
		}
		String sql="insert into stock_info(brand,sub_brand,unit_price,unit,standard,quantity,stock_time,stock_from,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard"),map.get("quantity"),map.get("stock_time"),map.get("stock_from"),map.get("reserve1"),map.get("reserve3"),map.get("reserve3")};//����map
		List<Object> params=objectArray2ObjectList(params_temp);
		//2.���ýӿ�ִ�в���
		BaseAction tempAction=new BaseAction();
		int snum=executeUpdate(sql,params);
		if(snum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
			throw new Exception("����������Ϣʧ�ܣ���������!");
				/*MessageBox box=new MessageBox();
				box.setMessage("�����¼ʧ�ܣ�");
				box.open();
				return;*/
		}else if(snum==1){
			try{
				GoodsInfoService tempService=new GoodsInfoService();
				tempService.addGoodsInfo(map);
			}catch(Exception e){
				System.out.println("�������еĻ�Ʒ�Ѵ��ڻ�Ʒ��Ϣ���У������ظ����롣");
			}finally{
				addBatch(map);
			}
		}
		
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("����������Ϣʧ��!"+e.getMessage());
		}
	 return true;
	}

	/**
	 * description:�ж��Ƿ������ͬ�ļ�¼
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean isExistStockInfo(Map<String,Object> map) throws Exception{
		BaseAction tempAction=new BaseAction();
		String time = (String) map.get("stock_time");
		//String sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and si.stock_time=str_to_date(?, '%Y-%m-%d')";
		//String sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and date_format(si.stock_time,'%Y-%m-%d')=?";
		String sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and si.stock_from=? and si.stock_time=?";

		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date s_date =(Date)sdf.parse(time);*/
		//Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard"),str2Timestamp(time)};
		
		Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard"),map.get("stock_from"),time};

		List<Object> params=objectArray2ObjectList(params_tmp);
		//System.out.println(params);
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
	
	public boolean addBatch(Map<String,Object> map) throws Exception{
		String sql_query="select count(*) batchNo from goods_batch_info where brand=? and sub_brand=? and standard=?";
		String sql_insert="insert into goods_batch_info values(?,?,?,?,?,?,?,?,?)";
		Object[] params_query_temp={map.get("brand"),map.get("sub_brand"),map.get("standard")};
		List<Object> params_query=objectArray2ObjectList(params_query_temp);
		List list=null;
		try {
			list=executeQuery(sql_query, params_query);
			Map retMap=(Map) list.get(0);
			String batchNo=String.valueOf(retMap.get("batchNo"));
			
			Object[] params_insert_temp={map.get("brand"),map.get("sub_brand"),map.get("standard"),map.get("unit_price"),map.get("quantity"),batchNo,map.get("reserve1"),map.get("reserve2"),map.get("reserve3")};
			List<Object> params_insert=objectArray2ObjectList(params_insert_temp);
			int snum=executeUpdate(sql_insert,params_insert);
			if(snum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
				throw new Exception("������Ʒ����ʧ�ܣ���������!");
			}
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("������Ʒ����ʧ��!"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * ��������������Ϣ
	 * @param listMap װ���Ƕ���������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean batchAddStockInfo (List<Map<String,Object>> listMap) throws Exception{
		boolean ret_total=true;//ִ����������ķ���ֵ
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=addStockInfo(listMap.get(j));//ִ��һ�β���Ľ��
				if(ret_one==false){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("ִ������������Ʒ��Ϣ�쳣��");
		}
		return ret_total;
	}
	
	/**
	 * ɾ��һ��������Ϣ����ID��ʶ��delete���������ñ���
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteStockInfo(String id) throws Exception{
		String sql="delete from stock_info where id=?";
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
	 * ����ɾ��������Ϣ
	 * @param listId
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteStockInfo(List<String> listId) throws Exception{
		boolean ret_total=true;//ִ������ɾ���ķ���ֵ
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=deleteStockInfo(listId.get(j));//ִ��ɾ��һ����¼�ķ���ֵ
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
	 * ����ID����һ��������Ϣ�������Ϊ��
	 * 1.У����º�������Ƿ��������������ظ��������
	 * 2.����ID���������ֶΣ�����ĳЩ�ֶο���û�б仯����
	 * @param id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean updateStockInfo(String id,Map<String,Object> map) throws Exception{
		String sql="update stock_info si set si.brand=?,si.sub_brand=?,si.unit_price=?,"
	+"si.unit=? and si.standard=? and si.quantity=? and si.stock_time=? where si.id=?";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),
				map.get("unit"),map.get("standard"),map.get("quantity"),map.get("stock_time"),id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			boolean isExist=isExistStockInfo(map);
			if(isExist){
				throw new Exception("�Ѿ�������ͬ�Ľ�����Ϣ��Ʒ�ƣ���Ʒ�ƣ���񣬽���ʱ��ֱ�Ϊ��"+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+","+map.get("stock_time"));
			}
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("���µ�����Ϣʧ��");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("���½�����Ϣʧ��"+":"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * �������½�����Ϣ
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
	public boolean batchUpdateStockInfo(List<String> listId,List<Map<String,Object>> listMap) throws Exception{

		boolean ret_total=true;//ִ���������µķ���ֵ
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=updateStockInfo(listId.get(j),listMap.get(j));//ִ��һ�θ��µĽ��
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("ִ������������Ʒ��Ϣ�쳣��");
		}
		return ret_total;

	}
	
	
	/**
	 * description����ѯ���н�����Ϣ��������ѯ����
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
//				stockInfoDto.setQuantity((String) retMap.get("quantity"));
				stockInfoDto.setQuantity(String.valueOf(retMap.get("quantity")));
				
				/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Timestamp date_temp = (Timestamp) retMap.get("stock_time");
				String date = df.format(date_temp);*/
				stockInfoDto.setStock_time((String) retMap.get("stock_time"));
				
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
			throw new Exception("��ѯ��Ʒ��Ϣʧ�ܣ�");
		}
		return ro;
	}
	
	/**
	 * description:��ѯ����������ĳ������Ϣ(�������Ϊ��Ʒ�ƣ���Ʒ�ƣ���񣬽������ң���������)
	 * @param map��װ���ǲ�ѯ����
	 * @return
	 * @throws Exception 
	 */
	public ReturnObject queryStockInfo(Map<String,Object> map) throws Exception{
		List list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<StockInfoDTO> stockInfoList = new ArrayList<StockInfoDTO>();
		String id=(String) map.get("id");
		String brand=(String) map.get("brand");
		String sub_brand=(String) map.get("sub_brand");
		String standard=(String) map.get("standard");
		String stock_from=(String) map.get("stock_from");
		String stock_time=(String) map.get("stock_time");
		String reserve1=(String) map.get("reserve1");
		String reserve2=(String) map.get("reserve2");
		String reserve3=(String) map.get("reserve3");
		
		String sql="select * from stock_info si where 1=1";
		//Object[] params=new Object[]{};
		List<Object> params = new ArrayList<Object>();
		int p_num=0;
		if(Utils.isNotNull(id)){
			sql=sql+" and si.id=?";
			params.add(id);
		}
		if(Utils.isNotNull(brand)){
			sql=sql+" and si.brand=?";
			params.add(brand);
		}
		if(Utils.isNotNull(sub_brand)){
			sql=sql+" and si.sub_brand=?";
			params.add(sub_brand);
		}
		if(Utils.isNotNull(standard)){
			sql=sql+" and si.standard=?";
			params.add(standard);
		}
		if(Utils.isNotNull(stock_from)){
			sql=sql+" and si.stock_from=?";
			params.add(stock_from);
		}
		if(Utils.isNotNull(stock_time)){
			sql=sql+" and si.stock_time=?";
			params.add(stock_time);
		}
		if(Utils.isNotNull(reserve1)){
			sql=sql+" and si.reserve1=?";
			params.add(reserve1);
		}
		if(Utils.isNotNull(reserve2)){
			sql=sql+" and si.reserve2=?";
			params.add(reserve2);
		}
		if(Utils.isNotNull(reserve3)){
			sql=sql+" and si.reserve3=?";
			params.add(reserve3);
		}
		
		
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				StockInfoDTO stockInfoDto=new StockInfoDTO();
				stockInfoDto.setId(String.valueOf(retMap.get("id")) );
				stockInfoDto.setBrand((String) retMap.get("brand"));
				stockInfoDto.setQuantity((String) retMap.get("quantity"));
				stockInfoDto.setReserve1((String) retMap.get("reserve1"));
				stockInfoDto.setReserve2((String) retMap.get("reserve2"));
				stockInfoDto.setReserve3((String) retMap.get("reserve3"));
				stockInfoDto.setStandard((String) retMap.get("standard"));
				stockInfoDto.setSub_brand((String) retMap.get("sub_brand"));
				stockInfoDto.setUnit((String) retMap.get("unit"));
				stockInfoDto.setUnit_price((Float) retMap.get("unit_price"));
				stockInfoDto.setStock_from((String) retMap.get("stock_from"));
				stockInfoDto.setStock_time((String) retMap.get("stock_time"));
				stockInfoList.add(stockInfoDto);
			}
			page.setItems((List)stockInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("��ѯ��Ʒ��Ϣʧ�ܣ�");
		}
		return ro;
	}

}
