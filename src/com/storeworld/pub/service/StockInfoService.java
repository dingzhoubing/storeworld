package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.mysql.jdbc.Connection;
import com.storeworld.database.BaseAction;
import com.storeworld.database.DataBaseCommonInfo;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.product.Product;
import com.storeworld.product.ProductCellModifier;
import com.storeworld.product.ProductUtils;
import com.storeworld.stock.Stock;
import com.storeworld.utils.Utils;

public class StockInfoService extends BaseAction {

	
	
	private void fillInStockInfoDTO(StockInfoDTO stockInfoDto, Map<String, Object> retMap){
		
		stockInfoDto.setId(String.valueOf(retMap.get("id")));
		stockInfoDto.setBrand(String.valueOf(retMap.get("brand")));
		stockInfoDto.setSub_brand(String.valueOf(retMap.get("sub_brand")));
		
		if(retMap.get("quantity")!=null){
			stockInfoDto.setQuantity(String.valueOf(retMap.get("quantity")));	
		}else{
			stockInfoDto.setQuantity("");
		}
		
		if (retMap.get("reserve1") == null)
			stockInfoDto.setReserve1("");
		else
			stockInfoDto.setReserve1(String.valueOf(retMap.get("reserve1")));
		
		if (retMap.get("reserve2") == null)
			stockInfoDto.setReserve2("");
		else
			stockInfoDto.setReserve2(String.valueOf(retMap.get("reserve2")));
		
		if (retMap.get("reserve3") == null)
			stockInfoDto.setReserve3("");
		else
			stockInfoDto.setReserve3(String.valueOf(retMap.get("reserve3")));
		
		if (retMap.get("standard") == null)
			stockInfoDto.setStandard("");
		else
			stockInfoDto.setStandard(String.valueOf(retMap.get("standard")));
		
		if (retMap.get("unit") == null)
			stockInfoDto.setUnit("");
		else
			stockInfoDto.setUnit(String.valueOf(retMap.get("unit")));
		
		if (retMap.get("unit_price") == null)
			stockInfoDto.setUnit_price((float)0.0);
		else
			stockInfoDto.setUnit_price(Float.valueOf(String.valueOf(retMap.get("unit_price"))));
		
		if (retMap.get("stock_from") == null)
			stockInfoDto.setStock_from("");
		else
			stockInfoDto.setStock_from(String.valueOf(retMap.get("stock_from")));
		
		if (retMap.get("stock_time") == null)
			stockInfoDto.setStock_time("");
		else
			stockInfoDto.setStock_time(String.valueOf(retMap.get("stock_time")));
		
	}
	

	/**
	 * 增加一条进货信息，处理的步骤包括：
	 * 1.根据界面输入的进货信息（已放入map中），查进货信息表的当日信息，判断是否已经存在相同的记录（进货日期这个字段很重要）
	 * 2.如果存在，抛出异常，如不存在，执行插入。
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Product> addStockInfo(Connection conn, Map<String, Object> map) throws Exception {
		// return type, if 0 means normal, -1 means exist such stock,
//		int type = 0;
		ArrayList<Product> products = new ArrayList<Product>();
		try {
			String sql = "insert into stock_info(brand,sub_brand,unit_price,unit,standard,quantity,stock_time,stock_from,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?)";
			Object[] params_temp = { map.get("brand"), map.get("sub_brand"),
					map.get("unit_price"), map.get("unit"),
					map.get("standard"), map.get("quantity"),
					map.get("stock_time"), map.get("stock_from"),
					map.get("reserve1"), map.get("reserve3"),
					map.get("reserve3") };
			List<Object> params = objectArray2ObjectList(params_temp);
			// 2.调用接口执行插入
			executeUpdate(conn, sql, params);
			
			GoodsInfoService tempService = new GoodsInfoService();
			tempService.addGoodsInfoAndUpdate(conn, map, products);			

		} catch (Exception e) {
			throw e;
		}

		return products;
	}

	/**
	 * update the stock info due to product changed
	 * 
	 * @param mapnew
	 * @param mapold
	 * @return
	 */
	public int updateAllStockInfoForProductChanged(Connection conn, Map<String, Object> mapnew,
			Map<String, Object> mapold) throws Exception{
		int ret = 0;
		BaseAction tempAction = new BaseAction();
		String sql = "select * from stock_info where brand=? and sub_brand=? and unit=? and standard=?";
		Object[] params_tmp = { mapold.get("brand"), mapold.get("sub_brand"),
				mapold.get("unit"), mapold.get("standard") };
		List<Object> params = objectArray2ObjectList(params_tmp);
		List<Object> list = tempAction.executeQuery(conn, sql, params);
		
		if (list == null || list.size() == 0) {
			return ret;
		} else {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) list.get(i);
				String id = String.valueOf(map.get("id"));
				String sql_u = "update stock_info si set si.brand=?,si.sub_brand=?, si.unit=?, si.standard=? where si.id=?";
				Object[] params_tmp_u = { mapnew.get("brand"),
						mapnew.get("sub_brand"), mapnew.get("unit"),
						mapnew.get("standard"), id };
				List<Object> params_u = objectArray2ObjectList(params_tmp_u);
				tempAction.executeUpdate(conn, sql_u, params_u);
			}
		}

		return ret;
	}

	/**
	 * 删除一条进货信息，用ID标识。delete操作表不能用别名
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Product deleteStockInfo(Connection conn, String id, Stock stock) throws Exception {
		
		BaseAction tempAction = new BaseAction();
		Product p = null;
		String sql = "delete from stock_info where id=?";
		Object[] params_temp = { id };
		List<Object> params = objectArray2ObjectList(params_temp);
		
		executeUpdate(conn, sql, params);
		
		// find the product in product table, update the repository
		String sql_u = "select * from goods_info gi where gi.brand=? and gi.sub_brand=?";// and gi.standard=?
		Object[] params_u = { stock.getBrand(), stock.getSubBrand() };// ,stock.getSize()
		List<Object> params_exe = objectArray2ObjectList(params_u);
		
		List<Object> list_u = null;	
		list_u = tempAction.executeQuery(conn, sql_u, params_exe);
		if(!(list_u==null || list_u.isEmpty())){
		Map<String, Object> map_u = (Map<String, Object>) list_u.get(0);
		String id_u = String.valueOf(map_u.get("id"));
		String repo_old = String.valueOf(map_u.get("repertory"));
		String repo_del = stock.getNumber();
		int repo_tmp = Integer.valueOf(repo_old) - Integer.valueOf(repo_del);
		String repo_update = String.valueOf(repo_tmp);
//		String repo_update = "";
//		if (repo_tmp >= 0)
//			repo_update = String.valueOf(repo_tmp);
//		else
//			repo_update = "0";
		String sql_update = "update goods_info gi set gi.repertory=? where gi.id=?";
		Object[] params_update = { repo_update, id_u };
		List<Object> params_do = objectArray2ObjectList(params_update);

		executeUpdate(conn, sql_update, params_do);

		p = new Product();
		p.setID(id_u);
		p.setBrand(String.valueOf(map_u.get("brand")));
		p.setSubBrand(String.valueOf(map_u.get("sub_brand")));
		p.setSize(String.valueOf(map_u.get("standard")));
		p.setUnit(String.valueOf(map_u.get("unit")));
		// even if it's negative number, we show it, and remind the users, we can not just replace it to 0
		p.setRepository(repo_update);
		}
		return p;//can be empty
	}

	/**
	 * 批量删除进货信息
	 * 
	 * @param listId
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Product> batchDeleteStockInfo(Connection conn, List<String> listId,ArrayList<Stock> stocks) throws Exception {
		ArrayList<Product> products_impacted = new ArrayList<Product>();
		int num = listId.size();
		try {
			for (int j = 0; j < num; j++) {
				Product p = deleteStockInfo(conn, listId.get(j), stocks.get(j));// 执行删除一条记录的返回值
				if(p!=null)
					products_impacted.add(p);
			}
		} catch (Exception e) {
			throw new Exception("执行批量删除客户信息异常！" + ":" + e.getMessage());
		}
		return products_impacted;
	}

	/**
	 * 根据ID更新一条进货信息，步骤分为： 1.校验更新后的数据是否存在与存量数据重复的情况。 2.根据ID更新所有字段（即便某些字段可能没有变化）。
	 * 
	 * @param id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Product> updateStockInfo(Connection conn, String id, Map<String, Object> map) throws Exception {
		ArrayList<Product> products_changed = new ArrayList<Product>();
		
		BaseAction tempAction = new BaseAction();//a private static one?
		String sql = "update stock_info si set si.brand=?,si.sub_brand=?,si.unit_price=?,"
				+ "si.unit=?, si.standard=? ,si.quantity=? ,si.stock_time=? where si.id=?";
		Object[] params_temp = { map.get("brand"), map.get("sub_brand"),
				map.get("unit_price"), map.get("unit"), map.get("standard"),
				map.get("quantity"), map.get("stock_time"), id };
		List<Object> params = objectArray2ObjectList(params_temp);
		try {
			// there is no need to test if the stock with same brand, sub, size
			// exist or not
			// we need to get the record, if we need to update the product table
			String sql_cur = "select * from stock_info si where si.id=?";
			Object[] params_tmp = { map.get("id") };
			List<Object> params_cur = objectArray2ObjectList(params_tmp);
			List<Object> list = tempAction.executeQuery(conn, sql_cur, params_cur);
			Map<String, Object> mapRes = (Map<String, Object>) list.get(0);
			String oldRepo = String.valueOf(mapRes.get("quantity"));
			String newRepo = String.valueOf(map.get("quantity"));

			// int gap = Integer.valueOf(newRepo) - Integer.valueOf(oldRepo);
			int gap_old = Integer.valueOf(oldRepo);
			int gap_new = Integer.valueOf(newRepo);
			GoodsInfoService goods_service = new GoodsInfoService();
			//update goods table
			products_changed = goods_service.addGoodsInfoAndUpdate(conn, mapRes, map, gap_old, gap_new);
			//update stock table
			executeUpdate(conn, sql, params);			
		} catch (Exception e) {
			throw new Exception("更新进货信息失败" + ":" + e.getMessage());
		}
		return products_changed;
	}

	/**
	 * update the stock table indeed value by time
	 * 
	 * @param time
	 * @param indeed
	 * @return
	 * @throws Exception
	 */
	public boolean updateStocksIndeedByTime(Connection conn, String time, String indeed)
			throws Exception {
		boolean ret = false;

		String sql_update = "update stock_info si set si.reserve1=? where si.stock_time=?";
		Object[] params_update = { indeed, time };
		List<Object> params_do = objectArray2ObjectList(params_update);

		executeUpdate(conn, sql_update, params_do);
		
		ret = true;
		return ret;
	}

	/**
	 * description：查询所有进货信息，不带查询条件
	 * 
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryStockInfoAll(Connection conn) throws Exception {
		List<Object> list = null;
		String sql = "select * from stock_info si";
		List<Object> params = null;
		Pagination page = new Pagination();
		ReturnObject ro = new ReturnObject();
		List<StockInfoDTO> stockInfoList = new ArrayList<StockInfoDTO>();
		try {
			list = executeQuery(conn, sql, params);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> retMap = (Map<String, Object>) list.get(i);
				StockInfoDTO stockInfoDto = new StockInfoDTO();
				fillInStockInfoDTO(stockInfoDto, retMap);
				stockInfoList.add(stockInfoDto);
			}
			page.setItems((List) stockInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}

	public ReturnObject queryStockInfoByBrandAndSub(Connection conn, String brand, String sub)
			throws Exception {
		List<Object> list = null;
		Pagination page = new Pagination();
		ReturnObject ro = new ReturnObject();
		List<StockInfoDTO> stockInfoList = new ArrayList<StockInfoDTO>();

		String sql = "select * from stock_info si where si.brand=? and si.sub_brand=?";
		List<Object> params = new ArrayList<Object>();
		params.add(brand);
		params.add(sub);

		try {
			list = executeQuery(conn, sql, params);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> retMap = (Map<String, Object>) list.get(i);
				StockInfoDTO stockInfoDto = new StockInfoDTO();
				fillInStockInfoDTO(stockInfoDto, retMap);
				stockInfoList.add(stockInfoDto);
			}
			page.setItems((List) stockInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}

	/**
	 * description:查询满足条件的某几条信息(条件最多为：品牌，子品牌，规格，进货厂家，进货日期)
	 * 
	 * @param map
	 *            ：装的是查询条件
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryStockInfo(Connection conn, String time)
			throws Exception {
		List<Object> list = null;
		Pagination page = new Pagination();
		ReturnObject ro = new ReturnObject();
		List<StockInfoDTO> stockInfoList = new ArrayList<StockInfoDTO>();
		
		String sql = "select * from stock_info si where si.stock_time=?";
		
		List<Object> params = new ArrayList<Object>();
		params.add(time);
		
		try {
			list = executeQuery(conn, sql, params);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> retMap = (Map<String, Object>) list.get(i);
				StockInfoDTO stockInfoDto = new StockInfoDTO();
				fillInStockInfoDTO(stockInfoDto, retMap);
				stockInfoList.add(stockInfoDto);
			}
			page.setItems((List) stockInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			throw new Exception("查询货品信息失败！");
		}
		return ro;
	}

	/**
	 * description:默认查一个月的历史记录，从今天算
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryStockInfoByDefaultStocktime(Connection conn, String option)
			throws Exception {
		List<Object> list = null;
		Pagination page = new Pagination();
		ReturnObject ro = new ReturnObject();
		List<StockInfoDTO> stockInfoList = new ArrayList<StockInfoDTO>();

		String stock_time_temp = option;//(String) map.get("stock_time");
		String stock_time = stock_time_temp.substring(0, 6);
		String start_time = stock_time + "00000000";
		stock_time = stock_time + "31235959";

		String sql = "select * from stock_info si where 1=1";
		List<Object> params = new ArrayList<Object>();

		if (Utils.isNotNull(stock_time)) {
			sql = sql + " and si.stock_time>? and si.stock_time<?";
			params.add(start_time);
			params.add(stock_time);
		}

		try {
			list = executeQuery(conn, sql, params);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> retMap = (Map<String, Object>) list.get(i);
				StockInfoDTO stockInfoDto = new StockInfoDTO();
				fillInStockInfoDTO(stockInfoDto, retMap);
				stockInfoList.add(stockInfoDto);
			}
			page.setItems((List) stockInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {
			throw new Exception("查询进货信息的历史记录失败！");
		}
		return ro;
	}

	/**
	 * 输入一个月份，供查询。日期格式要限制一下
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public ReturnObject queryStockInfoByInputStocktime(Connection conn, String date)
			throws Exception {
		List<Object> list = null;
//		Statistic tempService = new Statistic();
		Pagination page = new Pagination();
		ReturnObject ro = new ReturnObject();
		List<StockInfoDTO> stockInfoList = new ArrayList<StockInfoDTO>();

		String stock_time_temp = date;
		String year = stock_time_temp.substring(0, 4);
		String month = stock_time_temp.substring(4);
		if (month.length() < 2) {
			month = "0" + month;
		}
		String stock_time = year + month;
		String start_time = stock_time + "00000000";
		String end_time = stock_time + "31" + "235959";

		String sql = "select * from stock_info si where 1=1";
		List<Object> params = new ArrayList<Object>();

		if (Utils.isNotNull(stock_time)) {
			sql = sql + " and si.stock_time>? and si.stock_time<?";
			params.add(start_time);
			params.add(end_time);
		}

		try {
			list = executeQuery(conn, sql, params);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> retMap = (Map<String, Object>) list.get(i);
				StockInfoDTO stockInfoDto = new StockInfoDTO();
				fillInStockInfoDTO(stockInfoDto, retMap);
				stockInfoList.add(stockInfoDto);
			}
			page.setItems((List) stockInfoList);
			ro.setReturnDTO(page);
		} catch (Exception e) {

			throw new Exception("查询进货信息的历史记录失败！");
		}
		return ro;
	}

	/**
	 * get the new stock row id
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getNextStockID() throws Exception {
		int id = -1;
		Connection conn = getConnection();

		try {
			conn.setAutoCommit(false);
			
			id = getNextID(conn, "stock_info");
			
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			System.out.println("get next stock id failed");
			throw new Exception(DataBaseCommonInfo.GET_NEXT_ID_FAILED);
		}finally{
			conn.close();
		}
		return id;
	}

	// ==========================================================================================================

	/**
	 * description:判断是否存在相同的记录
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	// private boolean isExistStockInfo(Map<String,Object> map) throws
	// Exception{
	// BaseAction tempAction=new BaseAction();
	// String time = (String) map.get("stock_time");
	// //String
	// sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and si.stock_time=str_to_date(?, '%Y-%m-%d')";
	// //String
	// sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and date_format(si.stock_time,'%Y-%m-%d')=?";
	// // String
	// sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and si.stock_from=? and si.stock_time=?";
	// String
	// sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and si.stock_time=?";
	// /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// Date s_date =(Date)sdf.parse(time);*/
	// //Object[]
	// params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard"),str2Timestamp(time)};
	//
	// // Object[]
	// params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard"),map.get("stock_from"),time};
	// Object[]
	// params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard"),time};
	//
	// List<Object> params=objectArray2ObjectList(params_tmp);
	// //System.out.println(params);
	// List list=null;
	// try{
	// list=tempAction.executeQuery(sql, params);
	// }catch(Exception e){
	// throw new Exception(DataBaseCommonInfo.EXE_QUERY_FAILED);
	// }
	// if(list==null||list.size()==0)
	// {
	// return false;
	// }
	//
	// return true;
	// }

	// public boolean addBatch(Map<String,Object> map) throws Exception{
	// String
	// sql_query="select count(*) batchNo from goods_batch_info where brand=? and sub_brand=? and standard=?";
	// String
	// sql_insert="insert into goods_batch_info values(?,?,?,?,?,?,?,?,?)";
	// Object[]
	// params_query_temp={map.get("brand"),map.get("sub_brand"),map.get("standard")};
	// List<Object> params_query=objectArray2ObjectList(params_query_temp);
	// List list=null;
	// try {
	// list=executeQuery(sql_query, params_query);
	// Map retMap=(Map) list.get(0);
	// String batchNo=String.valueOf(retMap.get("batchNo"));
	//
	// Object[]
	// params_insert_temp={map.get("brand"),map.get("sub_brand"),map.get("standard"),map.get("unit_price"),map.get("quantity"),batchNo,map.get("reserve1"),map.get("reserve2"),map.get("reserve3")};
	// List<Object> params_insert=objectArray2ObjectList(params_insert_temp);
	// int snum=executeUpdate(sql_insert,params_insert);
	// if(snum<1){//插入记录失败，界面弹出异常信息,这里将异常抛出，由调用的去捕获异常
	// throw new Exception("新增货品批次失败，请检查数据!");
	// }
	// }catch(Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// throw new Exception("新增货品批次失败!"+e.getMessage());
	// }
	// return true;
	// }

	/**
	 * 批量新增进货信息
	 * 
	 * @param listMap
	 *            装的是多条进货信息
	 * @return
	 * @throws Exception
	 */
	// public boolean batchAddStockInfo (List<Map<String,Object>> listMap)
	// throws Exception{
	// boolean ret_total=true;//执行批量插入的返回值
	// int num=listMap.size();
	// try{
	// for(int j=0;j<num;j++){
	// boolean ret_one=addStockInfo(listMap.get(j));//执行一次插入的结果
	// if(ret_one==false){
	// ret_total=false;
	// return ret_total;
	// }
	// }
	// }catch(Exception e){
	// throw new Exception("执行批量新增货品信息异常！");
	// }
	// return ret_total;
	// }

	/**
	 * 批量更新进货信息
	 * 
	 * @param listId
	 * @param listMap
	 * @return
	 * @throws Exception
	 */
	// public boolean batchUpdateStockInfo(List<String>
	// listId,List<Map<String,Object>> listMap) throws Exception{
	//
	// boolean ret_total=true;//执行批量更新的返回值
	// int num=listMap.size();
	// try{
	// for(int j=0;j<num;j++){
	// boolean ret_one=updateStockInfo(listId.get(j),listMap.get(j));//执行一次更新的结果
	// if(ret_one!=true){
	// ret_total=false;
	// return ret_total;
	// }
	// }
	// }catch(Exception e){
	// throw new Exception("执行批量新增货品信息异常！");
	// }
	// return ret_total;
	//
	// }

}
