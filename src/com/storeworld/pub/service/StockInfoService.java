package com.storeworld.pub.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockInfoDTO;

public class StockInfoService extends BaseAction{
	
	/**
	 * ����һ��������Ϣ������Ĳ��������
	 * 1.���ݽ�������Ľ�����Ϣ���ѷ���map�У����������Ϣ��ĵ�����Ϣ���ж��Ƿ��Ѿ�������ͬ�ļ�¼��������������ֶκ���Ҫ��
	 * 2.������ڣ��׳��쳣���粻���ڣ�ִ�в��롣
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Boolean addStockInfo(Map<String,Object> map) throws Exception{

	 try{
		//1.���������û���Ϣֵ������param�У�ADD your code below:
		boolean isExist=isExistStockInfo(map);
		if(isExist){
			throw new Exception("�Ѿ�������ͬ�Ľ�����Ϣ��Ʒ�ƣ���Ʒ�ƣ���񣬽���ʱ��ֱ�Ϊ��"+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+","+map.get("stock_time"));
		}
		String sql="insert into stock_info(brand,sub_brand,unit_price,unit,standard,quantity,stock_time,stock_from,reserve1,reserve2,reserve3) values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard")+"KG",map.get("quantity"),map.get("stock_time"),map.get("stock_from"),map.get("reserve1"),map.get("reserve3"),map.get("reserve3")};//����map
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
		String sql="select * from stock_info si where si.brand=? and si.sub_brand=? and si.standard=? and date_format(si.stock_time,'%Y-%m-%d')=?";
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date s_date =(Date)sdf.parse(time);*/
		//Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard"),str2Timestamp(time)};
		
		Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard")};

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
				stockInfoDto.setQuantity((Integer) retMap.get("quantity"));
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Timestamp date_temp = (Timestamp) retMap.get("stock_time");
				String date = df.format(date_temp);
				stockInfoDto.setStock_time(date);
				
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

}
