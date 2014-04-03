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

	/** �ܼ�¼�� */
	protected int totalRecord = 0;
	/** ÿҳ��ʾ��¼�� */
	protected int pageSize = Pagination.PAGESIZE;//<0Ϊ��ʾ���м�¼

	/** ��ǰҳ��� */
	protected int currentPage = 1;
	/** ��ǰҳ���¼�� */
	protected List<Object> items;
	
	/** ��ǰҳ�渨����¼�� */
	protected List<Object> otherItems;
	/** ��ѯ������������,����index�Ⱥ�˳��������� */
	protected String[] orders = null;



	public Pagination(){

	}
	/**
	 * ���캯�� ҳ��--->��̨ ��

	 * @param currentPage
	 * @param pageSize
	 */
	public Pagination(int currentPage,int pageSize) {
		this.setCurrentPage(currentPage);
		this.setPageSize(pageSize);
	}

	/**
	 * ���캯�� ҳ��--->��̨ ��

	 * @param currentPage
	 * @param pageSize
	 */
	public Pagination(int currentPage,int pageSize,String[] orders) {
		this.setCurrentPage(currentPage);
		this.setPageSize(pageSize);
		this.setOrders(orders);
	}


	/**
	 *  ���캯�� ��̨--->ҳ�� ��

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

	// ��ҳ
	public void pageFirst() {
		currentPage = 1;
	}

	// ��һҳ

	public void pagePrev() {
		if (currentPage > 1)
			currentPage--;
	}

	// ��һҳ

	public void pageNext() {
		currentPage++;
	}

	// ��ת�����һҳ

	public void pageLast() {
		currentPage = this.getMaxPage();
	}

	// ת��ĳҳ
	public void convert() {
		if (currentPage < 1)
			currentPage = 1;
	}

	//�õ���ѯ��ʼ��¼���

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