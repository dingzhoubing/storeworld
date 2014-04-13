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
	 * 增加一条进货信息，处理的步骤包括：
	 * 1.根据界面输入的进货信息（已放入map中），查进货信息表的当日信息，判断是否已经存在相同的记录（进货日期这个字段很重要）
	 * 2.如果存在，抛出异常，如不存在，执行插入。
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean addStockInfo(Map<String,Object> map) throws Exception{

	 try{
		//1.获得输入的用户信息值，放入param中，ADD your code below:
		boolean isExist=isExistStockInfo(map);
		if(isExist){
			throw new Exception("已经存在相同的进货信息，品牌，子品牌，规格，进货时间分别为："+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+","+map.get("stock_time"));
		}
		String sql="insert into stock_info(brand,sub_brand,unit_price,unit,standard,quantity,stock_time,stock_from,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard"),map.get("quantity"),map.get("stock_time"),map.get("stock_from"),map.get("reserve1"),map.get("reserve3"),map.get("reserve3")};//来自map
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
		}else if(snum==1){
			try{
				GoodsInfoService tempService=new GoodsInfoService();
				tempService.addGoodsInfo(map);
			}catch(Exception e){
				System.out.println("进货单中的货品已存在货品信息表中，无需重复加入。");
			}finally{
				addBatch(map);
			}
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
			throw new Exception("查询是否已存在将要插入的记录出现异常"+e.getMessage());
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
			if(snum<1){//插入记录失败，界面弹出异常信息,这里将异常抛出，由调用的去捕获异常
				throw new Exception("新增货品批次失败，请检查数据!");
			}
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("新增货品批次失败!"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * 批量新增进货信息
	 * @param listMap 装的是多条进货信息
	 * @return
	 * @throws Exception
	 */
	public boolean batchAddStockInfo (List<Map<String,Object>> listMap) throws Exception{
		boolean ret_total=true;//执行批量插入的返回值
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=addStockInfo(listMap.get(j));//执行一次插入的结果
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
	 * 删除一条进货信息，用ID标识。delete操作表不能用别名
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
	 * 批量删除进货信息
	 * @param listId
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteStockInfo(List<String> listId) throws Exception{
		boolean ret_total=true;//执行批量删除的返回值
		int num=listId.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=deleteStockInfo(listId.get(j));//执行删除一条记录的返回值
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
	 * 根据ID更新一条进货信息，步骤分为：
	 * 1.校验更新后的数据是否存在与存量数据重复的情况。
	 * 2.根据ID更新所有字段（即便某些字段可能没有变化）。
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
				throw new Exception("已经存在相同的进货信息，品牌，子品牌，规格，进货时间分别为："+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+","+map.get("stock_time"));
			}
			int rows=executeUpdate(sql,params);
			if(rows!=1){
				throw new Exception("更新单条信息失败");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("更新进货信息失败"+":"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * 批量更新进货信息
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
	public boolean batchUpdateStockInfo(List<String> listId,List<Map<String,Object>> listMap) throws Exception{

		boolean ret_total=true;//执行批量更新的返回值
		int num=listMap.size();
		try{
			for(int j=0;j<num;j++){
				boolean ret_one=updateStockInfo(listId.get(j),listMap.get(j));//执行一次更新的结果
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
			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}
	
	/**
	 * description:查询满足条件的某几条信息(条件最多为：品牌，子品牌，规格，进货厂家，进货日期)
	 * @param map：装的是查询条件
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
			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}

}
