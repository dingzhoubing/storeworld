package com.storeworld.pojo.dto;

import java.util.List;
import java.util.Map;

public class StockFactor {

	public Map<Integer,Integer> stock_map;
	public List<StockInfoDTO> stockInfoList;
	public Map<Integer, Integer> getStock_map() {
		return stock_map;
	}
	public void setStock_map(Map<Integer, Integer> stock_map) {
		this.stock_map = stock_map;
	}
	public List<StockInfoDTO> getStockInfoList() {
		return stockInfoList;
	}
	public void setStockInfoList(List<StockInfoDTO> stockInfoList) {
		this.stockInfoList = stockInfoList;
	}
	
	
}
