package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.GoodsInfo;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.utils.Utils;

public class GoodsInfoService extends BaseAction{

	/**
	 * ����һ����Ʒ��Ϣ������Ĳ��������
	 * 1.���ݽ�������Ļ�Ʒ��Ϣ���ѷ���map�У������Ʒ��Ϣ���ж��Ƿ��Ѿ�������ͬ�ļ�¼
	 * 2.������ڣ��׳��쳣���粻���ڣ�ִ�в��롣
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Boolean addGoodsInfo(Map<String,Object> map) throws Exception{

	 try{
		//1.���������û���Ϣֵ������param�У�ADD your code below:
		boolean isExist=isExistGoodsInfo(map);
		if(isExist){
			throw new Exception("�Ѿ�������ͬ�Ļ�Ʒ��Ʒ�ƣ���Ʒ�ƣ����ֱ�Ϊ��"+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard"));
		}
		String sql="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard"),map.get("reserve1"),map.get("reserve2"),map.get("reserve3"),map.get("repertory")};//����map
		List<Object> params=objectArray2ObjectList(params_temp);
		//2.���ýӿ�ִ�в���
		BaseAction tempAction=new BaseAction();
		int snum=executeUpdate(sql,params);
		if(snum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
			throw new Exception("�����û���Ϣʧ�ܣ���������!");
				/*MessageBox box=new MessageBox();
				box.setMessage("�����¼ʧ�ܣ�");
				box.open();
				return;*/
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("�����û���Ϣʧ��!"+e.getMessage());
		}
	 return true;
	}

	/**
	 * description:�ж��Ƿ������ͬ�ļ�¼
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean isExistGoodsInfo(Map<String,Object> map) throws Exception{
		BaseAction tempAction=new BaseAction();
		String sql="select * from goods_info gi where gi.brand=? and gi.sub_brand=? and gi.standard=?";
		Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard")};
		List<Object> params=objectArray2ObjectList(params_tmp);
		System.out.println(params);
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
	 * �������ӻ�Ʒ��Ϣ���ڽ�����ʱ�򣬻����˹�¼���Ʒ��Ϣʱ����¼�����һ����¼ʱ����һ���ύ��
	 * ���ߵ��С����������ߡ����桱��ťʱ��Ҳ��һ���ύ������д���ݿ⡣
	 * @param listMap ��ȡtable������Ļ�Ʒ��Ϣ��¼
	 * @return
	 * @throws Exception 
	 */
	public boolean batchAddGoodsInfo (List<Map<String,Object>> listMap) throws Exception{
		boolean ret_total=true;//ִ����������ķ���ֵ
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=addGoodsInfo(listMap.get(j));//ִ��һ�β���Ľ��
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
	 * Description:ɾ��ĳһ����Ʒ��Ϣ
	 * @param ID
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteGoodsInfo(String id) throws Exception{
		String sql="delete from goods_info where id=?";
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
	 * ִ������ɾ����Ʒ��Ϣ����Ҫ��Ʒ��Ϣ�б�����м�¼id
	 * @param listId:
	 * @return
	 * @throws Exception 
	 */
	public boolean batchDeleteGoodsInfo(List<String> listId) throws Exception{
		boolean ret_total=true;//ִ������ɾ���ķ���ֵ
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=deleteGoodsInfo(listId.get(j));//ִ��ɾ��һ����¼�ķ���ֵ
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("ִ������ɾ����Ʒ��Ϣ�쳣��"+":"+e.getMessage());
		}
		return ret_total;
	}

	/**
	 * ����һ����¼������Ĳ��������
	 * 1.�жϸ���֮���ֵ�Ƿ������ݿ������ظ�������У����׳��쳣
	 * 2.ִ�и��£��жϸ��µķ���ֵ�����¼�¼�����Ƿ�Ϊ1.
	 * @param id �����¼�¼��ID
	 * @param map������֮��ĸ����ֶ�
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoodsInfo(String id,Map<String,Object> map) throws Exception{
		String sql="update goods_info gi set gi.brand=?,gi.sub_brand=?,gi.standard=?,"
	+"gi.unit=?,gi.repertory=? where gi.id=?";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("standard"),
				map.get("unit"),map.get("repertory"),id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			boolean isExist=isExistGoodsInfo(map);
			if(isExist){
				throw new Exception("�Ѿ�������ͬ�Ļ�Ʒ��Ʒ�ƣ���Ʒ�ƣ����ֱ�Ϊ��"+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+",�������������£�");
			}
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("���µ�����Ϣʧ��");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("���»�Ʒ��Ϣʧ��"+":"+e.getMessage());
		}
		return true;
	}

	/**
	 * �������»�Ʒ��Ϣ
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
	public boolean batchUpdateGoodsInfo(List<String> listId,List<Map<String,Object>> listMap) throws Exception{

		boolean ret_total=true;//ִ���������µķ���ֵ
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=updateGoodsInfo(listId.get(j),listMap.get(j));//ִ��һ�θ��µĽ��
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
	 * description����ѯ���л�Ʒ��Ϣ��������ѯ����
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryGoodsInfoAll() throws Exception{
		//ReturnObject ro=null;
		List list=null;
		String sql="select * from goods_info gi";
		//Object[] params={};
		List<Object> params=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<GoodsInfoDTO> goodsInfoList = new ArrayList<GoodsInfoDTO>();
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				GoodsInfoDTO goodsInfoDto=new GoodsInfoDTO();
				goodsInfoDto.setId((String.valueOf(retMap.get("id"))));
				goodsInfoDto.setBrand((String) retMap.get("brand"));
				goodsInfoDto.setRepertory((Integer) retMap.get("repertory"));
				goodsInfoDto.setReserve1((String) retMap.get("reserve1"));
				goodsInfoDto.setReserve2((String) retMap.get("reserve2"));
				goodsInfoDto.setReserve3((String) retMap.get("reserve3"));
				goodsInfoDto.setStandard((String) retMap.get("standard"));
				goodsInfoDto.setSub_brand((String) retMap.get("sub_brand"));
				goodsInfoDto.setUnit((String) retMap.get("unit"));
				goodsInfoDto.setUnit_price((Float) retMap.get("unit_price"));
				goodsInfoList.add(goodsInfoDto);
			}
			page.setItems((List)goodsInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("��ѯ��Ʒ��Ϣʧ�ܣ�");
		}
		return ro;
	}
	/**
	 * description:��ѯ����������ĳ������Ϣ(�������Ϊ��Ʒ�ƣ���Ʒ�ƣ���񣬵�λ�����)
	 * @param map��װ���ǲ�ѯ����
	 * @return
	 * @throws Exception 
	 */
	public ReturnObject queryGoodsInfo(Map<String,Object> map) throws Exception{
		List list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<GoodsInfoDTO> goodsInfoList = new ArrayList<GoodsInfoDTO>();
		Integer id=(Integer) map.get("id");
		String brand=(String) map.get("brand");
		String sub_brand=(String) map.get("sub_brand");
		String standard=(String) map.get("standard");
		String unit=(String) map.get("unit");
		Integer repertory=(Integer) map.get("repertory");
		
		String sql="select * from goods_info gi where 1=1";
		//Object[] params=new Object[]{};
		List<Object> params = new ArrayList<Object>();
		int p_num=0;
		if(Utils.isNotNull(String.valueOf(id))){
			sql=sql+" and gi.id=?";
			params.add(id);
			
		}
		if(Utils.isNotNull(brand)){
			sql=sql+" and gi.brand=?";
			params.add(brand);
			
		}
		if(Utils.isNotNull(sub_brand)){
			sql=sql+" and gi.sub_brand=?";
			params.add(sub_brand);
		}
		if(Utils.isNotNull(standard)){
			sql=sql+" and gi.standard=?";
			params.add(standard);
		}
		if(Utils.isNotNull(unit)){
			sql=sql+" and gi.unit=?";
			params.add(unit);
		}
		if(Utils.isNotNull(String.valueOf(repertory))){
			sql=sql+" and gi.repertory=?";
			params.add(repertory);
		}
		
		
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				GoodsInfoDTO goodsInfoDto=new GoodsInfoDTO();
				goodsInfoDto.setId((String.valueOf(retMap.get("id"))));
				goodsInfoDto.setBrand((String) retMap.get("brand"));
				goodsInfoDto.setRepertory((Integer) retMap.get("repertory"));
				goodsInfoDto.setReserve1((String) retMap.get("reserve1"));
				goodsInfoDto.setReserve2((String) retMap.get("reserve2"));
				goodsInfoDto.setReserve3((String) retMap.get("reserve3"));
				goodsInfoDto.setStandard((String) retMap.get("standard"));
				goodsInfoDto.setSub_brand((String) retMap.get("sub_brand"));
				goodsInfoDto.setUnit((String) retMap.get("unit"));
				goodsInfoDto.setUnit_price((Float) retMap.get("unit_price"));
				goodsInfoList.add(goodsInfoDto);
			}
			page.setItems((List)goodsInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("��ѯ��Ʒ��Ϣʧ�ܣ�");
		}
		return ro;
	}

}