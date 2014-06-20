package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.storeworld.database.BaseAction;
import com.storeworld.database.DataBaseCommonInfo;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.CustomerInfoDTO;
import com.storeworld.pojo.dto.GoodsInfo;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.product.Product;
import com.storeworld.product.ProductCellModifier;
import com.storeworld.product.ProductUtils;
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
	public int addGoodsInfo(Map<String,Object> map) throws Exception{
		//return type, 0 means normal, -1 means exist such goods
		int type = 0;
	 try{
		//1.���������û���Ϣֵ������param�У�ADD your code below:
		boolean isExist=isExistGoodsInfo(map);
		if(isExist){
			type = -1;//this good already exist
			return type;
		}
		String sql="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard"),map.get("reserve1"),map.get("reserve2"),map.get("reserve3"),map.get("repertory")};//����map
		List<Object> params=objectArray2ObjectList(params_temp);
		//2.���ýӿ�ִ�в���
//		BaseAction tempAction=new BaseAction();
		int snum=executeUpdate(sql,params);
		if(snum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
			type = -2;	//insert failed, but no exception?
			return type;
		}
		}catch (Exception e) {
			throw e;
		}
	 return type;
	}

	/**
	 * ����һ����Ʒ��Ϣ������Ĳ��������
	 * 1.���ݽ�������Ļ�Ʒ��Ϣ���ѷ���map�У������Ʒ��Ϣ���ж��Ƿ��Ѿ�������ͬ�ļ�¼
	 * 2.������ڣ��׳��쳣���粻���ڣ�ִ�в��롣
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int addGoodsInfoAndUpdate(Map<String,Object> map) throws Exception{
		//return type, 0 means normal, -1 means exist such goods
		int type = 0;
	 try{
		//1.���������û���Ϣֵ������param�У�ADD your code below:
		boolean isExist=isExistGoodsInfoAndUpdate(map);
		if(isExist){
			type = -1;//this good already exist, do not insert
			return type;
		}
		//not exist 
		String sql="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard"),map.get("reserve1"),map.get("reserve2"),map.get("reserve3"),map.get("repertory")};//����map
		List<Object> params=objectArray2ObjectList(params_temp);
		//2.���ýӿ�ִ�в���
//		BaseAction tempAction=new BaseAction();
		int snum=executeUpdate(sql,params);
		if(snum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
			type = -2;	//insert failed, but no exception?
			return type;
		}
		}catch (Exception e) {
			throw e;
		}
	 return type;
	}
	
	public int minusGoodsInfoAndUpdate(Map<String,Object> mapold, Map<String,Object> map, int gapold, int gapnew) throws Exception{
		//return type, 0 means normal, -1 means exist such goods
		int type = 0;
		try{
			//1.���������û���Ϣֵ������param�У�ADD your code below:
			type=isExistGoodsInfoAndMinus(mapold, map, gapold, gapnew);
			if(type == -1){				
				return type;
			}else if(type == -2){
				//do something here?
			}
	 	}catch(Exception e){
	 		System.out.println("minus goods info and update failed");
	 	}
	 
		return type;
	}
	
	//check if the old map is the same product as new map 
	private boolean checkProductSame(Map<String,Object> mapold, Map<String,Object> map){
		String brand_old = String.valueOf(mapold.get("brand"));
		String subbrand_old = String.valueOf(mapold.get("sub_brand"));
		String size_old = String.valueOf(mapold.get("standard"));
		
		String brand_new = String.valueOf(map.get("brand"));
		String subbrand_new = String.valueOf(map.get("sub_brand"));
		String size_new = String.valueOf(map.get("standard"));
		
		if(brand_old.equals(brand_new) && subbrand_old.equals(subbrand_new) && size_old.equals(size_new))
			return true;
		else
			return false;		
	}
	
	private int isExistGoodsInfoAndMinus(Map<String,Object> mapold, Map<String,Object> map, int gapold, int gapnew) throws Exception{
		int ret = 0;
		
		BaseAction tempAction=new BaseAction();
		String sql="select * from goods_info gi where gi.brand=? and gi.sub_brand=? and gi.standard=?";
		Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard")};
		List<Object> params=objectArray2ObjectList(params_tmp);
		//System.out.println(params);
		List list=null;
		try{
			list=tempAction.executeQuery(sql, params);
		}catch(Exception e){
			throw e;//throw it up?
		}
		if(list==null||list.size()==0)
		{
			//no such goods, no repository, prevent this.???
			ret = -1;
			return ret;
		}
		//if the new good exist:
		//if the new good not the same as old good, update new good & old good
		//else update the old good 		
		String newUnit = String.valueOf(map.get("unit"));		
		if(checkProductSame(mapold, map)){			
			Map<String,Object> mapRes = (Map<String,Object>)list.get(0); 
			String Repo = String.valueOf(mapRes.get("repertory"));
			String id = String.valueOf(mapRes.get("id"));
			int repo_number = Integer.valueOf(Repo);
			int new_deliver_num = Integer.valueOf(String.valueOf(map.get("quantity")));
			int old_deliver_num = Integer.valueOf(String.valueOf(mapold.get("quantity")));
			if(new_deliver_num > repo_number){
				ret = -2;// for this deliver, repository is not enough
				return ret;
			}else{
				//				
				int new_repo = repo_number - (new_deliver_num - old_deliver_num);
				String sql_update="update goods_info gi set gi.unit=?,gi.repertory=? where gi.id=?";
				Object[] params_update={newUnit,String.valueOf(new_repo),id};
				List<Object> params_do=objectArray2ObjectList(params_update);				
				int rows=executeUpdate(sql_update,params_do);
				
				Product p = new Product();
				p.setID(id);
				p.setBrand(String.valueOf(map.get("brand")));
				p.setSubBrand(String.valueOf(map.get("sub_brand")));
				p.setSize(String.valueOf(map.get("standard")));
				p.setUnit(newUnit);	
				p.setRepository(String.valueOf(new_repo));//initial it's empty, not null
				ProductCellModifier.getProductList().productChangedThree(p);
			}
		}else{
			//old map is not the same product as new map
			//update old product & new product
			//step 1: update old map product			
			String sql_oldmap_product="select * from goods_info gi where gi.brand=? and gi.sub_brand=? and gi.standard=?";
			Object[] params_tmp_oldmap_product={mapold.get("brand"),mapold.get("sub_brand"),mapold.get("standard")};
			List<Object> params_oldmap_product=objectArray2ObjectList(params_tmp_oldmap_product);
			//System.out.println(params);
			List list_oldmap_product=tempAction.executeQuery(sql_oldmap_product, params_oldmap_product);
			
			//if the old map product does not exist anymore, add it
			if(list_oldmap_product==null||list_oldmap_product.size()==0){
				
				String sql_insert_old="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
				Object[] params_temp_insert_old={mapold.get("brand"),mapold.get("sub_brand"),mapold.get("unit_price"),mapold.get("unit"),mapold.get("standard"),mapold.get("reserve1"),mapold.get("reserve2"),mapold.get("reserve3"),mapold.get("quantity")};
				List<Object> params_insert_old=objectArray2ObjectList(params_temp_insert_old);
				int snum=executeUpdate(sql_insert_old,params_insert_old);
				
				Product p = new Product();
				p.setID(ProductUtils.getNewLineID());
				p.setBrand(String.valueOf(mapold.get("brand")));
				p.setSubBrand(String.valueOf(mapold.get("sub_brand")));
				p.setSize(String.valueOf(mapold.get("standard")));
				p.setUnit(String.valueOf(mapold.get("unit")));	
				p.setRepository(String.valueOf(mapold.get("quantity")));//initial it's empty, not null
				ProductCellModifier.getProductList().productChangedTwo(p);
			}else{//if the old map product exist, update the repo number
				
				Map<String,Object> mapRes = (Map<String,Object>)list_oldmap_product.get(0);
				String Repo = String.valueOf(mapRes.get("repertory"));
				String id = String.valueOf(mapRes.get("id"));
				int repo_number = Integer.valueOf(Repo);
				int old_deliver_num = Integer.valueOf(String.valueOf(mapold.get("quantity")));
				int new_repo = repo_number + old_deliver_num;
				
				String sql_update="update goods_info gi set gi.unit=?,gi.repertory=? where gi.id=?";
				Object[] params_update={newUnit,String.valueOf(new_repo),id};
				List<Object> params_do=objectArray2ObjectList(params_update);				
				int rows=executeUpdate(sql_update,params_do);
				
				Product p = new Product();
				p.setID(id);
				p.setBrand(String.valueOf(map.get("brand")));
				p.setSubBrand(String.valueOf(map.get("sub_brand")));
				p.setSize(String.valueOf(map.get("standard")));
				p.setUnit(newUnit);	
				p.setRepository(String.valueOf(new_repo));//initial it's empty, not null
				ProductCellModifier.getProductList().productChangedThree(p);
			}
			
			//update the new map product(exist new map product in goods_info)
			Map<String,Object> mapRes = (Map<String,Object>)list.get(0); 
			String Repo = String.valueOf(mapRes.get("repertory"));
			String id = String.valueOf(mapRes.get("id"));
			int repo_number = Integer.valueOf(Repo);
			int new_deliver_num = Integer.valueOf(String.valueOf(map.get("quantity")));
			if(new_deliver_num > repo_number){//new map product not enough
				ret=-2;
				return ret;
			}else{
				
				int new_repo = repo_number - new_deliver_num;
				String sql_update="update goods_info gi set gi.unit=?,gi.repertory=? where gi.id=?";
				Object[] params_update={newUnit,String.valueOf(new_repo),id};
				List<Object> params_do=objectArray2ObjectList(params_update);				
				int rows=executeUpdate(sql_update,params_do);
				
				Product p = new Product();
				p.setID(id);
				p.setBrand(String.valueOf(map.get("brand")));
				p.setSubBrand(String.valueOf(map.get("sub_brand")));
				p.setSize(String.valueOf(map.get("standard")));
				p.setUnit(newUnit);	
				p.setRepository(String.valueOf(new_repo));//initial it's empty, not null
				ProductCellModifier.getProductList().productChangedThree(p);

			}

		}		
		
		return ret;
	}
	
	public int addGoodsInfoAndUpdate(Map<String,Object> mapold, Map<String,Object> map, int gapold, int gapnew) throws Exception{
		//return type, 0 means normal, -1 means exist such goods
		int type = 0;
	 try{
		//1.���������û���Ϣֵ������param�У�ADD your code below:
		boolean isExist=isExistGoodsInfoAndUpdate(mapold, map, gapold, gapnew);
		if(isExist){
			type = -1;//this good already exist
			return type;
		}
		//not exist
		String sql="insert into goods_info(brand,sub_brand,unit_price,unit,standard,reserve1,reserve2,reserve3,repertory) values(?,?,?,?,?,?,?,?,?)";
		Object[] params_temp={map.get("brand"),map.get("sub_brand"),map.get("unit_price"),map.get("unit"),map.get("standard"),map.get("reserve1"),map.get("reserve2"),map.get("reserve3"),map.get("quantity")};//����map
		List<Object> params=objectArray2ObjectList(params_temp);
		//2.���ýӿ�ִ�в���
//		BaseAction tempAction=new BaseAction();
		int snum=executeUpdate(sql,params);
		if(snum<1){//�����¼ʧ�ܣ����浯���쳣��Ϣ,���ｫ�쳣�׳����ɵ��õ�ȥ�����쳣
			type = -2;	//insert failed, but no exception?
			return type;
		}
		}catch (Exception e) {
			throw e;
		}
	 
		Product p = new Product();
		p.setID(ProductUtils.getNewLineID());
		p.setBrand(String.valueOf(map.get("brand")));
		p.setSubBrand(String.valueOf(map.get("sub_brand")));
		p.setSize(String.valueOf(map.get("standard")));
		p.setUnit(String.valueOf(map.get("unit")));	
		p.setRepository(String.valueOf(map.get("quantity")));//initial it's empty, not null

		ProductCellModifier.getProductList().productChangedTwo(p);
	 
	 return type;
	}
	
	private boolean isExistGoodsInfoAndUpdate(Map<String,Object> mapold, Map<String,Object> map, int gapold, int gapnew) throws Exception{
		BaseAction tempAction=new BaseAction();
		String sql="select * from goods_info gi where gi.brand=? and gi.sub_brand=? and gi.standard=?";
		Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard")};
		List<Object> params=objectArray2ObjectList(params_tmp);
		//System.out.println(params);
		List list=null;
		try{
			list=tempAction.executeQuery(sql, params);
		}catch(Exception e){
			throw e;//throw it up?
		}
		if(list==null||list.size()==0)
		{
			//new product is not exist, find out the old one, update it
			//find out the one need to minus the stock number
			String sql_u="select * from goods_info gi where gi.brand=? and gi.sub_brand=? and gi.standard=?";
			Object[] params_u={mapold.get("brand"),mapold.get("sub_brand"),mapold.get("standard")};
			List<Object> params_exe=objectArray2ObjectList(params_u);
			//System.out.println(params);
			List list_u=null;
			try{
				list_u=tempAction.executeQuery(sql_u, params_exe);
			}catch(Exception e){
				throw e;//throw it up?
			}
			Map<String,Object> map_u = (Map<String,Object>)list_u.get(0); 
			
			//no such product, first we need to minus the old repo, and insert the new  
			String oldRepo = String.valueOf(map_u.get("repertory"));
			String id = String.valueOf(map_u.get("id"));
			
			int new_repo = Integer.valueOf(oldRepo) - (gapold);//add gap
			String str_new_repo = "";
			if(new_repo >= 0){
				str_new_repo = String.valueOf(new_repo); 
			}else{
				str_new_repo = "0";
			}
			 
			
			String sql_update="update goods_info gi set gi.repertory=? where gi.id=?";
			Object[] params_update={str_new_repo,id};
			List<Object> params_do=objectArray2ObjectList(params_update);
			
			int rows=executeUpdate(sql_update,params_do);
			
			Product p = new Product();
			p.setID(id);
			p.setBrand(String.valueOf(map_u.get("brand")));
			p.setSubBrand(String.valueOf(map_u.get("sub_brand")));
			p.setSize(String.valueOf(map_u.get("standard")));
			p.setUnit(String.valueOf(map_u.get("unit")));	
			p.setRepository(str_new_repo);//initial it's empty, not null
			ProductCellModifier.getProductList().productChangedThree(p);
			
			final String brand = p.getBrand();
			final String sub = p.getSubBrand();
			final String size = p.getSize();
			if(new_repo < 0){
				Display.getDefault().syncExec(new Runnable() {
	    		    public void run() {
	    		    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
						mbox.setMessage(String.format("���¿����ֿ����ĿС���㣬���鲢���� Ʒ��:%s����Ʒ��:%s�� ���:%s �Ŀ��",brand, sub, size));
						mbox.open();	
	    		    }
	        	});		
			}
			
			return false;
		}
		//new product is exist, find out the new product and update it
		//update the unit and repository
		String newUnit = String.valueOf(map.get("unit"));
		
		Map<String,Object> mapRes = (Map<String,Object>)list.get(0); 
		String oldRepo = String.valueOf(mapRes.get("repertory"));
		String id = String.valueOf(mapRes.get("id"));
		
		int new_repo = Integer.valueOf(oldRepo) + (gapnew-gapold);//add gap
		String str_new_repo = String.valueOf(new_repo);
		
		String sql_update="update goods_info gi set gi.unit=?,gi.repertory=? where gi.id=?";
		Object[] params_update={newUnit,str_new_repo,id};
		List<Object> params_do=objectArray2ObjectList(params_update);
		
		int rows=executeUpdate(sql_update,params_do);
		
		Product p = new Product();
		p.setID(id);
		p.setBrand(String.valueOf(map.get("brand")));
		p.setSubBrand(String.valueOf(map.get("sub_brand")));
		p.setSize(String.valueOf(map.get("standard")));
		p.setUnit(newUnit);	
		p.setRepository(str_new_repo);//initial it's empty, not null
		ProductCellModifier.getProductList().productChangedThree(p);
		
		
		if(rows!=1){
			throw new Exception("���¿����Ϣʧ��");
		}
		
		return true;
	}
	
	
	/**
	 * description:�ж��Ƿ������ͬ�ļ�¼, ������»�Ʒ��
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean isExistGoodsInfoAndUpdate(Map<String,Object> map) throws Exception{
		BaseAction tempAction=new BaseAction();
		String sql="select * from goods_info gi where gi.brand=? and gi.sub_brand=? and gi.standard=?";
		Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard")};
		List<Object> params=objectArray2ObjectList(params_tmp);
		//System.out.println(params);
		List list=null;
		try{
			list=tempAction.executeQuery(sql, params);
		}catch(Exception e){
			throw e;//throw it up?
		}
		if(list==null||list.size()==0)
		{			
			return false;
		}
		//update the unit and repository
		String newUnit = String.valueOf(map.get("unit"));
		String addRepo = String.valueOf(map.get("quantity"));
		
		Map<String,Object> mapRes = (Map<String,Object>)list.get(0); 
		String oldRepo = String.valueOf(mapRes.get("repertory"));
		String id = String.valueOf(mapRes.get("id"));
		
		int new_repo = Integer.valueOf(addRepo) + Integer.valueOf(oldRepo);
		String str_new_repo = String.valueOf(new_repo);
		
		String sql_update="update goods_info gi set gi.unit=?,gi.repertory=? where gi.id=?";
		Object[] params_update={newUnit,str_new_repo,id};
		List<Object> params_do=objectArray2ObjectList(params_update);
		
		int rows=executeUpdate(sql_update,params_do);
		
		Product p = new Product();
		p.setID(id);
		p.setBrand(String.valueOf(map.get("brand")));
		p.setSubBrand(String.valueOf(map.get("sub_brand")));
		p.setSize(String.valueOf(map.get("standard")));
		p.setUnit(newUnit);	
		p.setRepository(str_new_repo);//initial it's empty, not null
		ProductCellModifier.getProductList().productChangedThree(p);
		
		
		if(rows!=1){
			throw new Exception("���¿����Ϣʧ��");
		}
		
		return true;
	}
	
	
	/**
	 * description:�ж��Ƿ������ͬ�ļ�¼
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean isExistGoodsInfo(Map<String,Object> map) throws Exception{
		BaseAction tempAction=new BaseAction();
		String sql="select * from goods_info gi where gi.brand=? and gi.sub_brand=? and gi.standard=?";
		Object[] params_tmp={map.get("brand"),map.get("sub_brand"),map.get("standard")};
		List<Object> params=objectArray2ObjectList(params_tmp);
		//System.out.println(params);
		List list=null;
		try{
			list=tempAction.executeQuery(sql, params);
		}catch(Exception e){
			throw e;//throw it up?
		}
		if(list==null||list.size()==0)
		{
			return false;
		}
		//if run to here, return true
		//if the unit is not the same, update the product
//		String newUnit = String.valueOf(map.get("unit"));
//		Map<String,Object> mapRes = (Map<String,Object>)list.get(0); 
//		String oldUnit = String.valueOf(mapRes.get("unit"));
//		if(!oldUnit.equals(newUnit)){
//			
//		}
		
		return true;
	}
	/**
	 * description:�жϻ�Ʒ��Ϣ��һ����¼�Ĺؼ��ֶ��Ƿ��޸�
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private boolean isKeyFactorModified(Map<String,Object> map) throws Exception{
		String id_temp=(String) map.get("id");
		String brand=(String)map.get("brand");
		String sub_brand=(String)map.get("sub_brand");
		String standard=(String)map.get("standard");
		Integer id=Integer.parseInt(id_temp);
		String sql="select * from goods_info gi where gi.id=?";
		Object[] params_tmp={id};
		List<Object> params=objectArray2ObjectList(params_tmp);
		List list=null;
		try{
			list=executeQuery(sql, params);
		}catch(Exception e){
			throw new Exception("�޸ļ�¼ʱ��ѯ�Ƿ��޸��˸ü�¼�Ĺؼ��ֶγ����쳣"+e.getMessage());
		}
		if(list==null||list.size()==0)
		{
			throw new Exception("û���ҵ����ڱ��޸ĵļ�¼��IDΪ��"+id);
		}else if(list.size()==1){
			Map retMap=(Map) list.get(0);
			String DB_brand=(String) retMap.get("brand");
			String DB_sub_brand=(String) retMap.get("sub_brand");
			String DB_standard=(String) retMap.get("standard");
			if(DB_brand.equals(brand)&&DB_sub_brand.equals(sub_brand)&&DB_standard.equals(standard)){
				return false;
			}
			
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
//	public boolean batchAddGoodsInfo (List<Map<String,Object>> listMap) throws Exception{
//		boolean ret_total=true;//ִ����������ķ���ֵ
//		int num=listMap.size();
//		try{
//			for(int j=0;j<num;j++){
//				boolean ret_one=addGoodsInfo(listMap.get(j));//ִ��һ�β���Ľ��
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
			if(isKeyFactorModified(map)){
				boolean isExist=isExistGoodsInfo(map);
				if(isExist){
					throw new Exception("�Ѿ�������ͬ�Ļ�Ʒ��Ʒ�ƣ���Ʒ�ƣ����ֱ�Ϊ��"+map.get("brand")+","+map.get("sub_brand")+","+map.get("standard")+",�������������£�");
				}
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

	
	public ReturnObject queryProductInfoByID(String id){
		List list=null;
		Pagination page = new Pagination();
		ReturnObject ro=new ReturnObject();
		List<GoodsInfoDTO> goodsInfoList = new ArrayList<GoodsInfoDTO>();
		String sql="select * from goods_info ci where id=?";
		Object[] params_temp={id};
		List<Object> params=objectArray2ObjectList(params_temp);
		try {
			list=executeQuery(sql, params);
			for(int i=0;i<list.size();i++){
				Map retMap=(Map) list.get(i);
				GoodsInfoDTO goodsInfoDto=new GoodsInfoDTO();
				goodsInfoDto.setId((String.valueOf(retMap.get("id"))));
				goodsInfoDto.setBrand((String) retMap.get("brand"));
				goodsInfoDto.setRepertory((String) retMap.get("repertory"));
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
			System.out.println("query product info by id failed");
		}
		return ro;
		
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
				goodsInfoDto.setRepertory((String) retMap.get("repertory"));
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
		Integer id;
		String tmp = (String)map.get("id");
		if(tmp == null)
			id=null;
		else
			id=Integer.valueOf(tmp);
		String brand=(String) map.get("brand");
		String sub_brand=(String) map.get("sub_brand");
		String standard=(String) map.get("standard");
		String unit=(String) map.get("unit");
		String repertory=(String) map.get("repertory");
		
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
		if(Utils.isNotNull(repertory)){
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
				goodsInfoDto.setRepertory((String) retMap.get("repertory"));
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

	public int getNextGoodsID() throws Exception{
		return getNextID("goods_info");
	}
	
}
