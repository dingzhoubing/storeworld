package com.storeworld.product;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.mysql.jdbc.Connection;
import com.storeworld.common.DataInTable;
import com.storeworld.common.IDataListViewer;
import com.storeworld.database.BaseAction;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.pojo.dto.GoodsInfoDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.pub.service.GoodsInfoService;
import com.storeworld.pub.service.StockInfoService;
import com.storeworld.stock.StockContentPart;
import com.storeworld.utils.DataCachePool;

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
	private static final StockInfoService stockinfo = new StockInfoService();
	private static final DeliverInfoService deliverinfo = new DeliverInfoService();
	private static final BaseAction baseAction = new BaseAction();
	
	public ProductList() {
		super();
		this.initial();
	}
	
	//initial data, later, in database
	public void initial(){		
		
		String newID = "";
		try {
			newID = String.valueOf(goodsinfo.getNextGoodsID());
		} catch (Exception e) {
			MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK);						
	    	messageBox.setMessage("初始化数据库失败，请重新启动"); 	
	    	if (messageBox.open() == SWT.OK){	    			    	
	    		MainUI.getMainUI_Instance(Display.getDefault()).dispose();
	    		System.exit(0);
	    		return;
	    	}
		}
		
		//no record
		if(newID.equals("-1") || newID.equals(""))//is this right?
			newID="1";//empty
		//by the list of Customer from database
		ProductUtils.setNewLineID(String.valueOf(Integer.valueOf(newID)));
		
		try {
			DataCachePool.cacheProductInfo2(productList);
		} catch (Exception e) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("缓存货品信息失败");
			mbox.open();
		}
				
		
	}
	
	/**
	 * get the products
	 * @return
	 */
	public ArrayList<DataInTable> getProducts() {
		return this.productList;
	}
	
	/**
	 * add a product into table (UI side)
	 * @param product
	 */
	public void addProduct(Product product) {
		this.productList.add(product);
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			(iterator.next()).add(product);
	}

	/**
	 * delete a product UI table & database
	 * @param product
	 * @throws Exception 
	 */
	public void removeProduct(Product product) throws Exception {
		
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}

		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).remove(product);
			try {
				conn.setAutoCommit(false);
				goodsinfo.deleteGoodsInfo(conn, product.getID());
				conn.commit();
				
			} catch (Exception e) {
				conn.rollback();
				System.out.println("remove product failed");
				throw e;
			}finally{
				conn.close();
			}
			
			//update the cache
			DataCachePool.removeProductInfoOfCache(product.getBrand(), product.getSubBrand());
			ProductUtils.refreshBrands();
		}
		this.productList.remove(product);
	}

	public void productChangedThree(Product product) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(product);	
			
			for(int i=0;i<productList.size();i++){
				Product p = (Product)(productList.get(i));
				if(p.getID().equals(product.getID())){
					p.setBrand(product.getBrand());
					p.setSubBrand(product.getSubBrand());
					p.setSize(product.getSize());
					p.setUnit(product.getUnit());
					p.setRepository(product.getRepository());
					break;
				}				
			}
			
			ProductUtils.refreshTableData();
			
		}
	}
	
	public void productChangedTwo(Product product) {
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(product);
			Product p = (Product)(productList.get(productList.size()-1));
			p.setBrand(product.getBrand());
			p.setSubBrand(product.getSubBrand());
			p.setSize(product.getSize());
			p.setUnit(product.getUnit());
			p.setRepository(product.getRepository());
			
			ProductCellModifier.addNewTableRow(product);
			
			DataCachePool.addBrand2Sub(product.getBrand(), product.getSubBrand());
			
			ProductUtils.refreshBrands();
			
			ProductUtils.refreshTableData();
			
		}
	}
	
	private boolean checkSameProduct(Product product){
		boolean ret = false;
		for(int i=0;i<productList.size()-1;i++){
			Product stmp = (Product)productList.get(i);
			//a different stock
			if(!stmp.getID().equals(product.getID())){
				if(product.getBrand().equals(stmp.getBrand()) && product.getSubBrand().equals(stmp.getSubBrand())){//&&product.getSize().equals(stmp.getSize())
					ret = true;
					break;
				}				
			}			
		}		
		return ret;
//		return DataCachePool.ifExistBrandSub(product.getBrand(), product.getSubBrand());
	}
	
	
	
	/**
	 * update the product table in UI & database
	 * @param product
	 * @throws Exception 
	 */
	public void productChanged(final Product product) throws Exception {
		
		Connection conn;
		try {
			conn = baseAction.getConnection();
		} catch (Exception e1) {
			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
			mbox.setMessage("连接数据库失败");
			mbox.open();
			return;
		}
		
		Iterator<IDataListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			(iterator.next()).update(product);
			//not the new row, we update, or we do not update, just update he table
			final Map<String, Object> prod = new HashMap<String ,Object>();
			prod.put("id", product.getID());
			prod.put("brand", product.getBrand());
			prod.put("sub_brand", product.getSubBrand());
			prod.put("standard", product.getSize());
			prod.put("unit", product.getUnit());
			prod.put("repertory", product.getRepository());
			
			try {				
				conn.setAutoCommit(false);
				
				if(!ProductValidator.checkID(product.getID()) && ProductValidator.rowLegal(product)){
					//update the database here				
					//check if exist the same product
					if(checkSameProduct(product)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的货品在库存表中，请重新选择！"));		    		    	
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Product s = new Product();
	    					s.setID(product.getID());//new row in fact
	    					s.setBrand(product.getBrand());
	    					s.setSubBrand("");
	    					s.setSize("");
	    					s.setUnit(product.getUnit());	
	    					s.setRepository(product.getRepository());
//	    					s.setTime(time);time and indeed?
	    					ProductCellModifier.getProductList().productChangedThree(s);
	    		    	}
						
					}else{

						//before update the database, record the old brand/sub, for updating the cache
						Map<String, Object> prod_old = new HashMap<String, Object>();
						prod_old.put("id", product.getID());
						ReturnObject ret = goodsinfo.queryGoodsInfo(conn, prod_old);
						Pagination page = (Pagination) ret.getReturnDTO();
						List<Object> list = page.getItems();
						String old_brand="";
						String old_sub="";
						String old_size = "";
						String old_unit = "";
						final Map<String, Object> prod_rec = new HashMap<String ,Object>();
						//it should contains only one element, or something wrong
						if(!list.isEmpty()){
							GoodsInfoDTO cDTO = (GoodsInfoDTO) list.get(0);
							old_brand = cDTO.getBrand();
							old_sub = cDTO.getSub_brand();
							old_size = cDTO.getStandard();
							old_unit = cDTO.getUnit();
							prod_rec.put("brand", old_brand);
							prod_rec.put("sub_brand", old_sub);
							prod_rec.put("standard", old_size);
							prod_rec.put("unit", old_unit);
						}else{
							System.out.println("query a product with an exist ID returns empty");
						}
						//if change the repository, just change it
						if(product.getBrand().equals(old_brand) && product.getSubBrand().equals(old_sub) && product.getSize().equals(old_size)
								&&product.getUnit().equals(old_unit)){
							//update the repository, just update it
							goodsinfo.updateGoodsInfo(conn, product.getID(), prod);
							conn.commit();
							
							//only change the repository, no need to refresh the navigator
//							DataCachePool.updateProductInfoOfCache(old_brand, old_sub, product.getBrand(), product.getSubBrand());					
//							ProductUtils.refreshBrands();
						}else{//change other property
							final String old_brand_final = old_brand;
							final String old_sub_final = old_sub;
														
							ReturnObject ro1 = stockinfo.queryStockInfoByBrandAndSub(conn, old_brand, old_sub);
							Map<String, Object> verify = new HashMap<String, Object>();
							verify.put("brand", old_brand);
							verify.put("sub_brand", old_sub);
							ReturnObject ro2 = deliverinfo.queryDeliverInfo(conn, verify);
							Pagination page1 = (Pagination) ro1.getReturnDTO();
							Pagination page2 = (Pagination) ro2.getReturnDTO();
							//if no items in stock table and deliver table, then, just do the change
							if(page1.getItems().isEmpty() && page2.getItems().isEmpty()){
								goodsinfo.updateGoodsInfo(conn, product.getID(), prod);
								conn.commit();
								
								DataCachePool.updateProductInfoOfCache(old_brand, old_sub, product.getBrand(), product.getSubBrand());					
								ProductUtils.refreshBrands();
							}else{
														
								MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.CANCEL);						
								messageBox.setMessage(String.format("品牌:%s，子品牌:%s，规格%s 的商品将被修改，进货与送货表中将会发生相应更新，确认要操作吗？", old_brand, old_sub, old_size));		    		    	
								if (messageBox.open() == SWT.OK){	
		    		    				    		    		
									ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
									IRunnableWithProgress runnable = new IRunnableWithProgress() {  
										public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException{  
		    		    		    	
											Display.getDefault().asyncExec(new Runnable() {  
													public void run() {  
														monitor.beginTask("正在进行更新，请勿关闭系统...", 100);  		    		    		        
		    		    		        
														Connection connin; 
														try {
															connin = baseAction.getConnection();
														} catch (Exception e1) {
															MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
															mbox.setMessage("连接数据库失败");
															mbox.open();
															return;
														}
														
														try {
															connin.setAutoCommit(false);
															goodsinfo.updateGoodsInfo(connin, product.getID(), prod);
															monitor.worked(10);  
															monitor.subTask("更新产品表");  
															
															//update all the stock info and deliver info, a long time maybe
															//update all stock info and current stock table
															StockInfoService stockinfo = new StockInfoService();
															stockinfo.updateAllStockInfoForProductChanged(connin, prod, prod_rec);
															
															monitor.worked(20);  
															monitor.subTask("更新进货表");  
															
															
															//update all deliver info and current deliver table
															DeliverInfoService deliverinfo = new DeliverInfoService();
															deliverinfo.updateAllDeliverInfoForProductChanged(connin, prod, prod_rec);


															StockContentPart.reNewStock();
															StockContentPart.reNewStockHistory();
															
															connin.commit();															
															//update the cache
															//based on: the product/customer page will not often be changed
															DataCachePool.updateProductInfoOfCache(old_brand_final, old_sub_final, product.getBrand(), product.getSubBrand());					
															ProductUtils.refreshBrands();
		
															monitor.worked(50);  
															monitor.subTask("更新进货界面");  


															monitor.worked(75);  
															monitor.subTask("更新送货表");  

															DeliverContentPart.reNewDeliver();
															DeliverContentPart.reNewDeliverHistory();
														} catch (Exception e) {
															try {
																connin.rollback();
															} catch (SQLException e1) {
																MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
																mbox.setMessage("连接数据库异常");
																mbox.open();
															}
															System.out.println("update goods failed");
															MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
															mbox.setMessage("连接数据库异常");
															mbox.open();
															return;
														}finally{
															try {
																connin.close();
															} catch (SQLException e) {
																MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
																mbox.setMessage("连接数据库异常");
																mbox.open();
															}
														}

														monitor.worked(100);  
														monitor.subTask("更新送货界面");  

														monitor.done();
		    		    		                }
		    		    		    	  });
		    		    		    }  
		    		    		};  
		    		    		  
		    		    		try {  
		    		    		    progressDialog.run(true,/*是否开辟另外一个线程*/  
		    		    		    false,/*是否可执行取消操作的线程*/  
		    		    		    runnable/*线程所执行的具体代码*/  
		    		    		    );  
		    		    		} catch (InvocationTargetException e) {  
		    		    			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
									mbox.setMessage("未知异常");
									mbox.open();
									return;
		    		    		} catch (InterruptedException e) {  
		    		    			MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
									mbox.setMessage("未知异常");
									mbox.open(); 
									return;
		    		    		}  
		    		    		
								
		    		    	}else{//back to the same
		    		    		Product s = new Product();
		    					s.setID(product.getID());//new row in fact
		    					s.setBrand(old_brand);
		    					s.setSubBrand(old_sub);
		    					s.setSize(old_size);
		    					s.setUnit(old_unit);	
		    					s.setRepository(product.getRepository());
//		    					s.setTime(time);time and indeed?
		    					ProductCellModifier.getProductList().productChangedThree(s);
		    		    	}
							}
						}
					}
				}
				if(ProductValidator.checkID(product.getID()) && ProductValidator.rowLegal(product)){				
					if(checkSameProduct(product)){
						MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK|SWT.ICON_WARNING);						
	    		    	messageBox.setMessage(String.format("存在相同的货品在库存表中，请重新选择！"));		    		    	
	    		    	if (messageBox.open() == SWT.OK){	    		    		
	    		    		Product s = new Product();
	    					s.setID(product.getID());//new row in fact
	    					s.setBrand(product.getBrand());
	    					s.setSubBrand("");
	    					s.setSize("");
	    					s.setUnit(product.getUnit());	
	    					s.setRepository(product.getRepository());
//	    					s.setTime(time);time and indeed?
	    					ProductCellModifier.getProductList().productChangedThree(s);
	    		    	}						
					}else{					
						goodsinfo.addGoodsInfo(conn, prod);
						conn.commit();
						
						ProductCellModifier.addNewTableRow(product);					
						DataCachePool.addBrand2Sub(product.getBrand(), product.getSubBrand());					
						ProductUtils.refreshBrands();
					}
			}
//				conn.commit();
			}catch(Exception e){
				conn.rollback();
				throw e;
			}finally{
				conn.close();
			}
		}

	}

	/**
	 *  may multi content provider
	 * @param viewer
	 */
	public void removeChangeListener(IDataListViewer viewer) {
		changeListeners.remove(viewer);
	}


	public void addChangeListener(IDataListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String toString(){
		return this.productList.toString();
	}
	
}
