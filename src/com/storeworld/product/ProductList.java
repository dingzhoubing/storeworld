package com.storeworld.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.customer.CustomerCellModifier;
import com.storeworld.customer.CustomerUtils;
import com.storeworld.customer.CustomerValidator;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.GoodsInfoService;

/**
 * get the data in product table
 * connect database
 * @author dingyuanxiong
 *
 */
public class ProductList {
	
	private ArrayList<DataInTable> productList = new ArrayList<DataInTable>();
	//hash set, so make it only has one of one kind
	private Set<IDataListViewer> changeListeners = new HashSet<IDataListViewer>();
	private static final GoodsInfoService goodsinfo = new GoodsInfoService();
	
	public ProductList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
//		String brand = "五得利";
//		String subbrand = "精一";
//		String size = "50kg";
//		String unit = "包";
////		double avg_in = 68.0;
////		double avg_out = 72.0;
//		int repository = 3000;
//		Product prod = new Product("1",brand, subbrand, size, unit, repository+"");
//		productList.add(prod);
//		String brand2 = "五得利";
//		String subbrand2 = "特精";
//		String size2 = "50kg";
//		String unit2 = "包";
////		double avg_in2 = 69.0;
////		double avg_out2 = 74.0;
//		int repository2 = 2500;
//		Product prod2 = new Product("2",brand2, subbrand2, size2, unit2, repository2+"");
//		productList.add(prod2);
//		String brand3 = "五得利";
//		String subbrand3 = "普粉";
//		String size3 = "50kg";
//		String unit3 = "包";
////		double avg_in3 = 63.0;
////		double avg_out3 = 67.0;
//		int repository3 = 3000;
//		Product prod3 = new Product("3",brand3, subbrand3, size3, unit3, repository3+"");
//		productList.add(prod3);
//		String brand4 = "金龙";
//		String subbrand4 = "精粉";
//		String size4 = "50kg";
//		String unit4 = "包";
////		double avg_in4 = 66.0;
////		double avg_out4 = 70.0;
//		int repository4 = 4000;
//		Product prod4 = new Product("4", brand4, subbrand4, size4, unit4, repository4+"");
//		productList.add(prod4);
//		String brand5 = "五联";
//		String subbrand5 = "包子粉";
//		String size5 = "50kg";
//		String unit5 = "包";
////		double avg_in5 = 65.0;
////		double avg_out5 = 69.0;
//		int repository5 = 2800;
//		Product prod5 = new Product("5", brand5, subbrand5, size5, unit5, repository5+"");
//		productList.add(prod5);				
//		
//		ProductUtils.setNewLineID("6");
		
		String newID = "";
		try {
			ReturnObject ret = goodsinfo.queryGoodsInfoAll();
			Pagination page = (Pagination) ret.getReturnDTO();
			List<Object> list = page.getItems();
			for(int i=0;i<list.size();i++){
				GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(i);
				Product prod = new Product();
				newID = cDTO.getId();
				prod.setID(cDTO.getId());
				prod.setBrand(cDTO.getBrand());
				prod.setSubBrand(cDTO.getSub_brand());
				prod.setSize(cDTO.getStandard());
				prod.setUnit(cDTO.getUnit());
				prod.setRepository(String.valueOf(cDTO.getRepertory()));
				
				productList.add(prod);
//				System.out.println("name: "+cDTO.getCustomer_name());
			}
		} catch (Exception e) {
			System.out.println("failed");
		}
		//no record
		if(newID.equals(""))
			newID="0";//empty
		//by the list of Customer from database
		ProductUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)+1));
		
	}
	
	public ArrayList<DataInTable> getProducts() {
		return this.productList;
	}
	
	/**
	 * add a product
	 */
	public void addProduct(Product product) {
		this.productList.add(product);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).add(product);
	}

	/**
	 * @param remove a product
	 */
	public void removeProduct(Product product) {
		this.productList.remove(product);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).remove(product);
			try {
				goodsinfo.deleteGoodsInfo(product.getID());
			} catch (Exception e) {
				System.out.println("remove product failed");
			}
		}
	}

	/**
	 * @param update a product
	 */
	public void productChanged(Product product) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(product);
			//not the new row, we update, or we do not update, just update he table
			Map<String, Object> prod = new HashMap<String ,Object>();
			prod.put("id", product.getID());
			prod.put("brand", product.getBrand());
			prod.put("sub_brand", product.getSubBrand());
			prod.put("standard", product.getSize());
			prod.put("unit", product.getUnit());
			prod.put("repertory", product.getRepository());
			if(!ProductValidator.checkID(product.getID())){
				//update the database here				
				try {
					goodsinfo.updateGoodsInfo(product.getID(), prod);
				} catch (Exception e) {
					System.out.println("update product failed");
				}
			}
			if(ProductValidator.checkID(product.getID()) && ProductValidator.rowLegal(product)){				
				try {
					goodsinfo.addGoodsInfo(prod);
					ProductCellModifier.addNewTableRow(product);
				} catch (Exception e) {
					System.out.println("add product failed");
				}
			}
		}
	}

	/**
	 * @param may multi contentprovider?， one remove
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param may multi contentprovider? one add
	 * viewer is a content provider
	 */
	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.productList.toString();
	}
	
}
