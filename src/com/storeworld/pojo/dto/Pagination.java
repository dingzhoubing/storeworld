package com.storeworld.pojo.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import com.storeworld.framework.ObjectExternalizable;

public class Pagination extends ObjectExternalizable{
	/** 
	 * @Fields serialVersionUID :  
	 */
	private static final long serialVersionUID = 1L;

	public transient static final int PAGESIZE = 10;

	public transient static final String ORDER_SPLIT = "|";

	public transient static final String ORDER_SPLIT_REGEX = "[" + ORDER_SPLIT + "]";

	public transient static final String ORDER_ASC = "A";

	public transient static final String ORDER_DESC = "D";

	/** 总记录数 */
	protected int totalRecord = 0;
	/** 每页显示记录数 */
	protected int pageSize = Pagination.PAGESIZE;//<0为显示所有记录

	/** 当前页序号 */
	protected int currentPage = 1;
	/** 当前页面记录集 */
	protected List<Object> items;
	
	/** 当前页面辅助记录集 */
	protected List<Object> otherItems;
	/** 查询排序条件数组,按照index先后顺序进行排序 */
	protected String[] orders = null;



	public Pagination(){

	}
	/**
	 * 构造函数 页面--->后台 用

	 * @param currentPage
	 * @param pageSize
	 */
	public Pagination(int currentPage,int pageSize) {
		this.setCurrentPage(currentPage);
		this.setPageSize(pageSize);
	}

	/**
	 * 构造函数 页面--->后台 用

	 * @param currentPage
	 * @param pageSize
	 */
	public Pagination(int currentPage,int pageSize,String[] orders) {
		this.setCurrentPage(currentPage);
		this.setPageSize(pageSize);
		this.setOrders(orders);
	}


	/**
	 *  构造函数 后台--->页面 用

	 * @param totalRecord
	 * @param pageSize
	 * @param pageStartRecord
	 * @param items
	 */
	public Pagination(int totalRecord,int currentPage, int pageSize, List<Object> items) {
		this.setTotalRecord(totalRecord);
		this.setPageSize(pageSize);
		this.setCurrentPage(currentPage);
		this.setItems(items);
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getMaxPage() {
		if (totalRecord <= 0){
			return 0;
		}else{
			if(totalRecord % pageSize == 0){
			  return totalRecord / pageSize;
			}else{
			  return totalRecord / pageSize + 1;
			}
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if(currentPage < 1){
			this.currentPage = 1;
		}else{
			this.currentPage = currentPage;
		}
	}

	public List<Object> getOtherItems() {
		return otherItems;
	}
	public void setOtherItems(List<Object> otherItems) {
		this.otherItems = otherItems;
	}
	public List<Object> getItems() {
		return items;
	}

	public void setItems(List<Object> items) {
		this.items = items;
	}

	// 首页
	public void pageFirst() {
		currentPage = 1;
	}

	// 上一页

	public void pagePrev() {
		if (currentPage > 1)
			currentPage--;
	}

	// 下一页

	public void pageNext() {
		currentPage++;
	}

	// 跳转到最后一页

	public void pageLast() {
		currentPage = this.getMaxPage();
	}

	// 转向某页
	public void convert() {
		if (currentPage < 1)
			currentPage = 1;
	}

	//得到查询开始记录序号

	public int getStartIndex() {
		if (currentPage <= 1) {
			return 0;
		} else if (currentPage > this.getMaxPage()) {
			return ((this.getMaxPage() - 1) * pageSize);
		} else {
			return ((currentPage - 1) * pageSize);
		}
	}

	/** @return : orders */
	public String[] getOrders() {
		return orders;
	}

	/** set orders
	 * @param orders
	 */
	public void setOrders(String[] orders) {
		this.orders = orders;
	}

	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {
//		PAGESIZE = in.readInt();
//		ORDER_SPLIT = readUTF(in);
//		ORDER_SPLIT_REGEX = readUTF(in);
//		ORDER_ASC = readUTF(in);
//		ORDER_DESC = readUTF(in);
		totalRecord = in.readInt();
		pageSize = in.readInt();
		currentPage = in.readInt();
		items = readArrayList(in);
		otherItems = readArrayList(in);
		orders = (String[])in.readObject();
	}


	public void writeExternal(ObjectOutput out) throws IOException {
//		out.writeInt(PAGESIZE);
//		writeUTF(out,ORDER_SPLIT);
//		writeUTF(out,ORDER_SPLIT_REGEX);
//		writeUTF(out,ORDER_ASC);
//		writeUTF(out,ORDER_DESC);
		out.writeInt(totalRecord);
		out.writeInt(pageSize);
		out.writeInt(currentPage);
		writeArrayList(out,items);
		writeArrayList(out,otherItems);
		out.writeObject(orders);
	}
}