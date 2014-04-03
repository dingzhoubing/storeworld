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
	 * 增加一条货品信息，处理的步骤包括：
	 * 1.根据界面输入的货品信息（已放入map中），查货品信息表，判断是否已经存在相同的记录
	 * 2.如果存在，抛出异常，如不存在，执行插入。
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Boolean addGoodsInfo(Map<String,Object> map) throws Exception{

	 try{
		//1.获得输入的用户信息值，放入param中，ADD your code below:
		boolean isExist=isExistGoodsInfo(map);
		if(isExist){
			throw new Exception("已经存在相同的货品，品牌，子品牌，规格分别为："+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard"));
		}
		String sql="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard"),map.get("reserve1"),map.get("reserve2"),map.get("reserve3"),map.get("repertory")};//来自map
		List<Object> params=objectArray2ObjectList(params_temp);
		//2.调用接口执行插入
		BaseAction tempAction=new BaseAction();
		int snum=executeUpdate(sql,params);
		if(snum<1){//插入记录失败，界面弹出异常信息,这里将异常抛出，由调用的去捕获异常
			throw new Exception("新增用户信息失败，请检查数据!");
				/*MessageBox box=new MessageBox();
				box.setMessage("插入记录失败！");
				box.open();
				return;*/
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("新增用户信息失败!"+e.getMessage());
		}
	 return true;
	}

	/**
	 * description:判断是否存在相同的记录
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
			throw new Exception("查询是否已存在将要插入的记录出现异常"+e.getMessage());
		}
		if(list==null||list.size()==0)
		{
			return false;
		}

		return true;
	}

	/**
	 * 批量增加货品信息，在进货的时候，或者人工录入货品信息时，当录完最后一条记录时，再一起提交，
	 * 或者当有“进货”或者“保存”按钮时，也是一起提交，批量写数据库。
	 * @param listMap 获取table中输入的货品信息记录
	 * @return
	 * @throws Exception 
	 */
	public boolean batchAddGoodsInfo (List<Map<String,Object>> listMap) throws Exception{
		boolean ret_total=true;//执行批量插入的返回值
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=addGoodsInfo(listMap.get(j));//执行一次插入的结果
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
	 * Description:删除某一条货品信息
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
	 * 执行批量删除货品信息，需要货品信息列表的所有记录id
	 * @param listId:
	 * @return
	 * @throws Exception 
	 */
	public boolean batchDeleteGoodsInfo(List<String> listId) throws Exception{
		boolean ret_total=true;//执行批量删除的返回值
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=deleteGoodsInfo(listId.get(j));//执行删除一条记录的返回值
				if(ret_one!=true){
					ret_total=false;
					return ret_total;
				}
			}
		}catch(Exception e){
			throw new Exception("执行批量删除货品信息异常！"+":"+e.getMessage());
		}
		return ret_total;
	}

	/**
	 * 更新一条记录，处理的步骤包括：
	 * 1.判断更新之后的值是否在数据库中有重复，如果有，则抛出异常
	 * 2.执行更新，判断更新的返回值：更新记录条数是否为1.
	 * @param id 被更新记录的ID
	 * @param map：更新之后的各个字段
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
				throw new Exception("已经存在相同的货品，品牌，子品牌，规格分别为："+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+",不允许这样更新！");
			}
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("更新单条信息失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("更新货品信息失败"+":"+e.getMessage());
		}
		return true;
	}

	/**
	 * 批量更新货品信息
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
	public boolean batchUpdateGoodsInfo(List<String> listId,List<Map<String,Object>> listMap) throws Exception{

		boolean ret_total=true;//执行批量更新的返回值
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=updateGoodsInfo(listId.get(j),listMap.get(j));//执行一次更新的结果
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
	 * description：查询所有货品信息，不带查询条件
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
			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}
	/**
	 * description:查询满足条件的某几条信息(条件最多为：品牌，子品牌，规格，单位，库存)
	 * @param map：装的是查询条件
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
			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}

}