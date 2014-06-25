package com.storeworld.pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.storeworld.analyze.AnalyzerConstants;
import com.storeworld.analyze.AnalyzerUtils.KIND;
import com.storeworld.analyze.AnalyzerUtils.TYPE;
import com.storeworld.database.BaseAction;
import com.storeworld.pojo.dto.AnalysticDTO;
import com.storeworld.pojo.dto.DeliverInfoAllDTO;
import com.storeworld.pojo.dto.Pagination;
import com.storeworld.pojo.dto.ResultSetDTO;
import com.storeworld.pojo.dto.ReturnObject;
import com.storeworld.pojo.dto.StockFactor;
import com.storeworld.pojo.dto.StockInfoDTO;
import com.storeworld.utils.Utils;
public class Statistic extends BaseAction{
	
	private static DeliverInfoService deliverinfo = new DeliverInfoService();
	private static StockInfoService stockinfo = new StockInfoService();
	
	private static int BRAND_SUB = 5;
	private static int AREA_CUS = 10;
	
	
	//we do not take the day into consider
	public String oneMonthAgo(String year, String month, String day){
		
		int y = Integer.valueOf(year);		
		if(month.startsWith("0"))
			month=month.substring(1);

		int m = Integer.valueOf(month);
		if(m == 1){
			y = y-1;
			m=12;
		}else{
			m=m-1;
		}
		String lastmonth = "";
		if(m<10){
			lastmonth = "0" + String.valueOf(m);
		}else{
			lastmonth = String.valueOf(m);
		}		
		return y+lastmonth+day;
	}
	
	public String oneSeasonAgo(String year, String month, String day){
		
		int y = Integer.valueOf(year);		
		if(month.startsWith("0"))
			month=month.substring(1);

		int m = Integer.valueOf(month);
		if(m <= 3){
			y = y-1;
			m=m+9;
		}else{
			m=m-3;
		}
		String lastmonth = "";
		if(m<10){
			lastmonth = "0" + String.valueOf(m);
		}else{
			lastmonth = String.valueOf(m);
		}		
		return y+lastmonth+day;
	}
	
	public String oneYearAgo(String year, String month, String day){
		
		int y = Integer.valueOf(year);						
		return (y-1)+month+day;
	}
	
	public String oneAllAgo(String year, String month, String day){
		return "00000000";
	}
	
	public String calculateStartTimeByEndTime(String end_time, String timeType){
		String year=end_time.substring(0, 4);
		String month=end_time.substring(4, 6);
		String day=end_time.substring(6, 8);
		String tail = end_time.substring(8);
		String retStr=null;
		if(timeType.equals(TYPE.MONTH)){
			retStr = oneMonthAgo(year, month, day);
		}else if(timeType.equals(TYPE.SEASON)){
			retStr = oneSeasonAgo(year, month, day);
		}else if(timeType.equals(TYPE.YEAR)){
			retStr = oneYearAgo(year, month, day);
		}else{//TYPE.ALL
			retStr = oneAllAgo(year, month, day);
		}		
		return retStr+tail;
	}
	
	//sort the keyset of the result list from larger -> smaller
	private void sortKeySetForShipment(ArrayList<String> keylist, HashMap<String, Integer> map){
		for(int i=0;i<keylist.size();i++){
			for(int j=i+1; j<keylist.size()-i; j++){
				String keya = keylist.get(i);
				String keyb = keylist.get(j);
				int a = map.get(keylist.get(i));
				int b = map.get(keylist.get(j));
				if(a < b){
					keylist.set(i, keyb);
					keylist.set(j, keya);
				}				
			}			
		}		
	}
	
	private void sortKeySetForProfit(ArrayList<String> keylist, HashMap<String, Double> map){
		for(int i=0;i<keylist.size();i++){
			for(int j=i+1; j<keylist.size()-i; j++){
				String keya = keylist.get(i);
				String keyb = keylist.get(j);
				double a = map.get(keylist.get(i));
				double b = map.get(keylist.get(j));
				if(a < b){
					keylist.set(i, keyb);
					keylist.set(j, keya);
				}				
			}			
		}		
	}
	
	//sort the time 
	private void sortKeySetForTrend(ArrayList<String> keylist){
		for(int i=0;i<keylist.size();i++){
			for(int j=i+1; j<keylist.size()-i; j++){
				String keya = keylist.get(i);
				String keyb = keylist.get(j);
				if((keya.compareTo(keyb)) < 0){
					keylist.set(i, keyb);
					keylist.set(j, keya);
				}				
			}			
		}		
	}
	
	//sort the stock search result
	private void sortStockResultByTime(List<Object> stocks){
		for(int i=0;i<stocks.size();i++){
			for(int j=i+1; j<stocks.size()-i; j++){
				StockInfoDTO sa = (StockInfoDTO)stocks.get(i);
				StockInfoDTO sb = (StockInfoDTO)stocks.get(j);
				String ta = sa.getStock_time();
				String tb = sb.getStock_time();
				if((ta.compareTo(tb)) < 0){
					stocks.set(i, tb);
					stocks.set(j, ta);
				}				
			}			
		}		
	}
		
	//if the number of keylist is larger than what we want, add "other"
	private void combineKeySetForShipment(ArrayList<String> keylist, HashMap<String, Integer> map, int num){
		if(keylist.size() <= num){
			return;
		}else{
			int sum = 0;
			for(int i=num; i< keylist.size(); i++){
				sum+=map.get(keylist.get(i));
				map.remove(keylist.get(i));
			}
			//remove the key list 
			for(int i=num; i< keylist.size(); i++){
				keylist.remove(i--);
			}
			//add the "else"
			keylist.add("其他");
			//put the "else" into map
			map.put("其他", sum);
		}
	}
	
	private void combineKeySetForProfit(ArrayList<String> keylist, HashMap<String, Double> map, int num){
		if(keylist.size() <= num){
			return;
		}else{
			double sum = 0;
			for(int i=num; i< keylist.size(); i++){
				sum+=map.get(keylist.get(i));
				map.remove(keylist.get(i));
			}
			//remove the key list 
			for(int i=num; i< keylist.size(); i++){
				keylist.remove(i--);
			}
			//add the "else"
			keylist.add("其他");
			//put the "else" into map
			map.put("其他", sum);
		}
	}
	
	/**
	 * all brand, all areas
	 * @param start_time
	 * @param end_time
	 * @param profit_shipment
	 * @param timeType
	 * @return
	 * @throws Exception
	 */
	private ResultSetDTO analyzeCase_One(String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> brands_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> areas_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeInterval(
				start_time, end_time);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			HashMap<String, Integer> brand2num = new HashMap<String, Integer>();
			HashMap<String, Integer> area2num = new HashMap<String, Integer>();
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String area = cDTO.getCustomer_area();
				String brand = cDTO.getBrand();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;
				if(brand2num.containsKey(brand)){//sum the brand -> num
					int sum = brand2num.get(brand)+number;
					brand2num.put(brand, sum);
				}else{
					brand2num.put(brand, Integer.valueOf(number));
				}
				
				if(area2num.containsKey(area)){
					int sum = area2num.get(area)+number;
					area2num.put(area, sum);
				}else{
					area2num.put(area, number);
				}
				
				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area
			ArrayList<String> brands = new ArrayList<String>();
			ArrayList<String> areas = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			brands.addAll(brand2num.keySet());
			areas.addAll(area2num.keySet());
			dates.addAll(trends.keySet());
			
			sortKeySetForShipment(brands, brand2num);
			sortKeySetForShipment(areas, area2num);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForShipment(brands, brand2num, BRAND_SUB);
			combineKeySetForShipment(areas, area2num, AREA_CUS);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<brands.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(brands.get(i));//brand
				int tmpnum = brand2num.get(brands.get(i));
				double ratio = (double)(Math.round((tmpnum/sumAll)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpnum + "");//number
				resDTO.setField3(ratio + "");
				brands_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(brands.get(brands.size()-1));//brand
			int tmpnum = brand2num.get(brands.get(brands.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpnum + "");//number
			resDTO.setField3(ratio + "");
			brands_ratio.add(resDTO);
			page1.setItems((List)brands_ratio);
			result.setMap("table1", page1);
			//areas ratio
			double left_area = 1.00;			
			for(int i=0;i<areas.size()-1;i++){
				AnalysticDTO resDTO_area = new AnalysticDTO();
				resDTO_area.setField1(areas.get(i));//brand
				int tmpnum_area = area2num.get(areas.get(i));
				double ratio_area = (double)(Math.round((tmpnum_area/sumAll)*10000))/10000;
				left_area-=ratio_area;
				resDTO_area.setField2(tmpnum_area + "");//number
				resDTO_area.setField3(ratio_area + "");
				areas_ratio.add(resDTO_area);
			}
			AnalysticDTO resDTO_area = new AnalysticDTO();
			resDTO_area.setField1(areas.get(areas.size()-1));//brand
			int tmpnum_area = area2num.get(areas.get(areas.size()-1));
			double ratio_area = (double)(Math.round((left_area)*10000))/10000;
			resDTO_area.setField2(tmpnum_area + "");//number
			resDTO_area.setField3(ratio_area + "");
			areas_ratio.add(resDTO_area);
			page2.setItems((List)areas_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> brand2profit = new HashMap<String, Double>();
			HashMap<String, Double> area2profit = new HashMap<String, Double>();
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String area = cDTO.getCustomer_area();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;
				if(brand2profit.containsKey(brand)){
					double sum = brand2profit.get(brand);
					brand2profit.put(brand, sum+profit);
				}else{
					brand2profit.put(brand, profit);
				}
				
				if(area2profit.containsKey(area)){
					double sum = area2profit.get(area);
					area2profit.put(area, sum+profit);
				}else{
					area2profit.put(area, profit);
				}
				
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> brands = new ArrayList<String>();
			ArrayList<String> areas = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			brands.addAll(brand2profit.keySet());
			areas.addAll(area2profit.keySet());
			dates.addAll(trendprofit.keySet());
			
			sortKeySetForProfit(brands, brand2profit);
			sortKeySetForProfit(areas, area2profit);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForProfit(brands, brand2profit, BRAND_SUB);
			combineKeySetForProfit(areas, area2profit, AREA_CUS);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<brands.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(brands.get(i));//brand
				double tmpprofit = brand2profit.get(brands.get(i));
				double ratio = (double)(Math.round((tmpprofit/allProfit)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpprofit + "");//number
				resDTO.setField3(ratio + "");
				brands_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(brands.get(brands.size()-1));//brand
			double tmpprofit = brand2profit.get(brands.get(brands.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpprofit + "");//number
			resDTO.setField3(ratio + "");
			brands_ratio.add(resDTO);
			page1.setItems((List)brands_ratio);
			result.setMap("table1", page1);
			//areas ratio
			double left_area = 1.00;			
			for(int i=0;i<brands.size()-1;i++){
				AnalysticDTO resDTO_area = new AnalysticDTO();
				resDTO_area.setField1(areas.get(i));//brand
				double tmpprofit_area = area2profit.get(areas.get(i));
				double ratio_area = (double)(Math.round((tmpprofit_area/allProfit)*10000))/10000;
				left_area-=ratio_area;
				resDTO_area.setField2(tmpprofit_area + "");//number
				resDTO_area.setField3(ratio_area + "");
				areas_ratio.add(resDTO_area);
			}
			AnalysticDTO resDTO_area = new AnalysticDTO();
			resDTO_area.setField1(areas.get(areas.size()-1));//brand
			double tmpprofit_area = area2profit.get(areas.get(areas.size()-1));
			double ratio_area = (double)(Math.round((left_area)*10000))/10000;
			resDTO_area.setField2(tmpprofit_area + "");//number
			resDTO_area.setField3(ratio_area + "");
			areas_ratio.add(resDTO_area);
			page2.setItems((List)areas_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}
		

		return ret;
	}
	
	private ResultSetDTO analyzeCase_Two(String customerArea, String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> brands_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> cus_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeIntervalAndCustomerArea(
				start_time, end_time, customerArea);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			HashMap<String, Integer> brand2num = new HashMap<String, Integer>();
			HashMap<String, Integer> cus2num = new HashMap<String, Integer>();
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String cus = cDTO.getCustomer_name();
				String brand = cDTO.getBrand();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;
				if(brand2num.containsKey(brand)){//sum the brand -> num
					int sum = brand2num.get(brand)+number;
					brand2num.put(brand, sum);
				}else{
					brand2num.put(brand, Integer.valueOf(number));
				}
				
				if(cus2num.containsKey(cus)){
					int sum = cus2num.get(cus)+number;
					cus2num.put(cus, sum);
				}else{
					cus2num.put(cus, number);
				}
				
				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area
			ArrayList<String> brands = new ArrayList<String>();
			ArrayList<String> customers = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			brands.addAll(brand2num.keySet());
			customers.addAll(cus2num.keySet());
			dates.addAll(trends.keySet());
			
			sortKeySetForShipment(brands, brand2num);
			sortKeySetForShipment(customers, cus2num);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForShipment(brands, brand2num, BRAND_SUB);
			combineKeySetForShipment(customers, cus2num, AREA_CUS);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<brands.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(brands.get(i));//brand
				int tmpnum = brand2num.get(brands.get(i));
				double ratio = (double)(Math.round((tmpnum/sumAll)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpnum + "");//number
				resDTO.setField3(ratio + "");
				brands_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(brands.get(brands.size()-1));//brand
			int tmpnum = brand2num.get(brands.get(brands.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpnum + "");//number
			resDTO.setField3(ratio + "");
			brands_ratio.add(resDTO);
			page1.setItems((List)brands_ratio);
			result.setMap("table1", page1);
			//areas ratio
			double left_cus = 1.00;			
			for(int i=0;i<customers.size()-1;i++){
				AnalysticDTO resDTO_cus = new AnalysticDTO();
				resDTO_cus.setField1(customers.get(i));//brand
				int tmpnum_cus = cus2num.get(customers.get(i));
				double ratio_cus = (double)(Math.round((tmpnum_cus/sumAll)*10000))/10000;
				left_cus-=ratio_cus;
				resDTO_cus.setField2(tmpnum_cus + "");//number
				resDTO_cus.setField3(ratio_cus + "");
				cus_ratio.add(resDTO_cus);
			}
			AnalysticDTO resDTO_cus = new AnalysticDTO();
			resDTO_cus.setField1(customers.get(customers.size()-1));//brand
			int tmpnum_cus = cus2num.get(customers.get(customers.size()-1));
			double ratio_cus = (double)(Math.round((left_cus)*10000))/10000;
			resDTO_cus.setField2(tmpnum_cus + "");//number
			resDTO_cus.setField3(ratio_cus + "");
			cus_ratio.add(resDTO_cus);
			page2.setItems((List)cus_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> brand2profit = new HashMap<String, Double>();
			HashMap<String, Double> cus2profit = new HashMap<String, Double>();
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String cus = cDTO.getCustomer_name();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;
				if(brand2profit.containsKey(brand)){
					double sum = brand2profit.get(brand);
					brand2profit.put(brand, sum+profit);
				}else{
					brand2profit.put(brand, profit);
				}
				
				if(cus2profit.containsKey(cus)){
					double sum = cus2profit.get(cus);
					cus2profit.put(cus, sum+profit);
				}else{
					cus2profit.put(cus, profit);
				}
				
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> brands = new ArrayList<String>();
			ArrayList<String> customers = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			brands.addAll(brand2profit.keySet());
			customers.addAll(cus2profit.keySet());
			dates.addAll(trendprofit.keySet());
			
			sortKeySetForProfit(brands, brand2profit);
			sortKeySetForProfit(customers, cus2profit);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForProfit(brands, brand2profit, BRAND_SUB);
			combineKeySetForProfit(customers, cus2profit, AREA_CUS);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<brands.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(brands.get(i));//brand
				double tmpprofit = brand2profit.get(brands.get(i));
				double ratio = (double)(Math.round((tmpprofit/allProfit)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpprofit + "");//number
				resDTO.setField3(ratio + "");
				brands_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(brands.get(brands.size()-1));//brand
			double tmpprofit = brand2profit.get(brands.get(brands.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpprofit + "");//number
			resDTO.setField3(ratio + "");
			brands_ratio.add(resDTO);
			page1.setItems((List)brands_ratio);
			result.setMap("table1", page1);
			//areas ratio
			double left_cus = 1.00;			
			for(int i=0;i<customers.size()-1;i++){
				AnalysticDTO resDTO_cus = new AnalysticDTO();
				resDTO_cus.setField1(customers.get(i));//brand
				double tmpprofit_cus = cus2profit.get(customers.get(i));
				double ratio_cus = (double)(Math.round((tmpprofit_cus/allProfit)*10000))/10000;
				left_cus-=ratio_cus;
				resDTO_cus.setField2(tmpprofit_cus + "");//number
				resDTO_cus.setField3(ratio_cus + "");
				cus_ratio.add(resDTO_cus);
			}
			AnalysticDTO resDTO_cus = new AnalysticDTO();
			resDTO_cus.setField1(customers.get(customers.size()-1));//brand
			double tmpprofit_cus = cus2profit.get(customers.get(customers.size()-1));
			double ratio_cus = (double)(Math.round((left_cus)*10000))/10000;
			resDTO_cus.setField2(tmpprofit_cus + "");//number
			resDTO_cus.setField3(ratio_cus + "");
			cus_ratio.add(resDTO_cus);
			page2.setItems((List)cus_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}		
		return ret;
	}
	
	private ResultSetDTO analyzeCase_Three(String customerArea, String customerName, String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> brands_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> cus_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeIntervalAndCustomerAreaName(
				start_time, end_time, customerArea, customerName);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			HashMap<String, Integer> brand2num = new HashMap<String, Integer>();
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String cus = cDTO.getCustomer_name();
				String brand = cDTO.getBrand();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;
				if(brand2num.containsKey(brand)){//sum the brand -> num
					int sum = brand2num.get(brand)+number;
					brand2num.put(brand, sum);
				}else{
					brand2num.put(brand, Integer.valueOf(number));
				}				
				
				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area
			ArrayList<String> brands = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			brands.addAll(brand2num.keySet());
			dates.addAll(trends.keySet());
			
			sortKeySetForShipment(brands, brand2num);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForShipment(brands, brand2num, BRAND_SUB);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<brands.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(brands.get(i));//brand
				int tmpnum = brand2num.get(brands.get(i));
				double ratio = (double)(Math.round((tmpnum/sumAll)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpnum + "");//number
				resDTO.setField3(ratio + "");
				brands_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(brands.get(brands.size()-1));//brand
			int tmpnum = brand2num.get(brands.get(brands.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpnum + "");//number
			resDTO.setField3(ratio + "");
			brands_ratio.add(resDTO);
			page1.setItems((List)brands_ratio);
			result.setMap("table1", page1);
			//areas ratio
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> brand2profit = new HashMap<String, Double>();
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String cus = cDTO.getCustomer_name();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;
				if(brand2profit.containsKey(brand)){
					double sum = brand2profit.get(brand);
					brand2profit.put(brand, sum+profit);
				}else{
					brand2profit.put(brand, profit);
				}				
				
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> brands = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			brands.addAll(brand2profit.keySet());
			dates.addAll(trendprofit.keySet());
			
			sortKeySetForProfit(brands, brand2profit);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForProfit(brands, brand2profit, BRAND_SUB);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<brands.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(brands.get(i));//brand
				double tmpprofit = brand2profit.get(brands.get(i));
				double ratio = (double)(Math.round((tmpprofit/allProfit)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpprofit + "");//number
				resDTO.setField3(ratio + "");
				brands_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(brands.get(brands.size()-1));//brand
			double tmpprofit = brand2profit.get(brands.get(brands.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpprofit + "");//number
			resDTO.setField3(ratio + "");
			brands_ratio.add(resDTO);
			page1.setItems((List)brands_ratio);
			result.setMap("table1", page1);
			//areas ratio
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}		
		return ret;
	}
	
	private ResultSetDTO analyzeCase_Four(String brandStr, String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> subs_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> areas_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeIntervalAndBrand(
				start_time, end_time, brandStr);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			HashMap<String, Integer> sub2num = new HashMap<String, Integer>();
			HashMap<String, Integer> area2num = new HashMap<String, Integer>();
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String area = cDTO.getCustomer_area();
				String sub = cDTO.getSub_brand();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;
				if(sub2num.containsKey(sub)){//sum the brand -> num
					int sum = sub2num.get(sub)+number;
					sub2num.put(sub, sum);
				}else{
					sub2num.put(sub, Integer.valueOf(number));
				}
				
				if(area2num.containsKey(area)){
					int sum = area2num.get(area)+number;
					area2num.put(area, sum);
				}else{
					area2num.put(area, number);
				}
				
				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area
			ArrayList<String> subs = new ArrayList<String>();
			ArrayList<String> areas = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			subs.addAll(sub2num.keySet());
			areas.addAll(area2num.keySet());
			dates.addAll(trends.keySet());
			
			sortKeySetForShipment(subs, sub2num);
			sortKeySetForShipment(areas, area2num);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForShipment(subs, sub2num, BRAND_SUB);
			combineKeySetForShipment(areas, area2num, AREA_CUS);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<subs.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(subs.get(i));//brand
				int tmpnum = sub2num.get(subs.get(i));
				double ratio = (double)(Math.round((tmpnum/sumAll)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpnum + "");//number
				resDTO.setField3(ratio + "");
				subs_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(subs.get(subs.size()-1));//brand
			int tmpnum = sub2num.get(subs.get(subs.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpnum + "");//number
			resDTO.setField3(ratio + "");
			subs_ratio.add(resDTO);
			page1.setItems((List)subs_ratio);
			result.setMap("table1", page1);
			//areas ratio
			double left_area = 1.00;			
			for(int i=0;i<areas.size()-1;i++){
				AnalysticDTO resDTO_area = new AnalysticDTO();
				resDTO_area.setField1(areas.get(i));//brand
				int tmpnum_area = area2num.get(areas.get(i));
				double ratio_area = (double)(Math.round((tmpnum_area/sumAll)*10000))/10000;
				left_area-=ratio_area;
				resDTO_area.setField2(tmpnum_area + "");//number
				resDTO_area.setField3(ratio_area + "");
				areas_ratio.add(resDTO_area);
			}
			AnalysticDTO resDTO_area = new AnalysticDTO();
			resDTO_area.setField1(areas.get(areas.size()-1));//brand
			int tmpnum_area = area2num.get(areas.get(areas.size()-1));
			double ratio_area = (double)(Math.round((left_area)*10000))/10000;
			resDTO_area.setField2(tmpnum_area + "");//number
			resDTO_area.setField3(ratio_area + "");
			areas_ratio.add(resDTO_area);
			page2.setItems((List)areas_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> sub2profit = new HashMap<String, Double>();
			HashMap<String, Double> area2profit = new HashMap<String, Double>();
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();//equal brandStr
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String area = cDTO.getCustomer_area();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;
				if(sub2profit.containsKey(sub)){
					double sum = sub2profit.get(sub);
					sub2profit.put(sub, sum+profit);
				}else{
					sub2profit.put(sub, profit);
				}
				
				if(area2profit.containsKey(area)){
					double sum = area2profit.get(area);
					area2profit.put(area, sum+profit);
				}else{
					area2profit.put(area, profit);
				}
				
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> subs = new ArrayList<String>();
			ArrayList<String> areas = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			subs.addAll(sub2profit.keySet());
			areas.addAll(area2profit.keySet());
			dates.addAll(trendprofit.keySet());
			
			sortKeySetForProfit(subs, sub2profit);
			sortKeySetForProfit(areas, area2profit);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForProfit(subs, sub2profit, BRAND_SUB);
			combineKeySetForProfit(areas, area2profit, AREA_CUS);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<subs.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(subs.get(i));//brand
				double tmpprofit = sub2profit.get(subs.get(i));
				double ratio = (double)(Math.round((tmpprofit/allProfit)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpprofit + "");//number
				resDTO.setField3(ratio + "");
				subs_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(subs.get(subs.size()-1));//brand
			double tmpprofit = sub2profit.get(subs.get(subs.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpprofit + "");//number
			resDTO.setField3(ratio + "");
			subs_ratio.add(resDTO);
			page1.setItems((List)subs_ratio);
			result.setMap("table1", page1);
			//areas ratio
			double left_area = 1.00;			
			for(int i=0;i<areas.size()-1;i++){
				AnalysticDTO resDTO_area = new AnalysticDTO();
				resDTO_area.setField1(areas.get(i));//brand
				double tmpprofit_area = area2profit.get(areas.get(i));
				double ratio_area = (double)(Math.round((tmpprofit_area/allProfit)*10000))/10000;
				left_area-=ratio_area;
				resDTO_area.setField2(tmpprofit_area + "");//number
				resDTO_area.setField3(ratio_area + "");
				areas_ratio.add(resDTO_area);
			}
			AnalysticDTO resDTO_area = new AnalysticDTO();
			resDTO_area.setField1(areas.get(areas.size()-1));//brand
			double tmpprofit_area = area2profit.get(areas.get(areas.size()-1));
			double ratio_area = (double)(Math.round((left_area)*10000))/10000;
			resDTO_area.setField2(tmpprofit_area + "");//number
			resDTO_area.setField3(ratio_area + "");
			areas_ratio.add(resDTO_area);
			page2.setItems((List)areas_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}
		

		return ret;
	}
	
	private ResultSetDTO analyzeCase_Five(String brandStr, String customerArea, String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> subs_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> cus_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeIntervalAndBrandArea(
				start_time, end_time, brandStr, customerArea);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			HashMap<String, Integer> sub2num = new HashMap<String, Integer>();
			HashMap<String, Integer> cus2num = new HashMap<String, Integer>();
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String cus = cDTO.getCustomer_name();
				String sub = cDTO.getSub_brand();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;
				if(sub2num.containsKey(sub)){//sum the brand -> num
					int sum = sub2num.get(sub)+number;
					sub2num.put(sub, sum);
				}else{
					sub2num.put(sub, Integer.valueOf(number));
				}
				
				if(cus2num.containsKey(cus)){
					int sum = cus2num.get(cus)+number;
					cus2num.put(cus, sum);
				}else{
					cus2num.put(cus, number);
				}
				
				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area
			ArrayList<String> subs = new ArrayList<String>();
			ArrayList<String> customers = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			subs.addAll(sub2num.keySet());
			customers.addAll(cus2num.keySet());
			dates.addAll(trends.keySet());
			
			sortKeySetForShipment(subs, sub2num);
			sortKeySetForShipment(customers, cus2num);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForShipment(subs, sub2num, BRAND_SUB);
			combineKeySetForShipment(customers, cus2num, AREA_CUS);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<subs.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(subs.get(i));//brand
				int tmpnum = sub2num.get(subs.get(i));
				double ratio = (double)(Math.round((tmpnum/sumAll)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpnum + "");//number
				resDTO.setField3(ratio + "");
				subs_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(subs.get(subs.size()-1));//brand
			int tmpnum = sub2num.get(subs.get(subs.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpnum + "");//number
			resDTO.setField3(ratio + "");
			subs_ratio.add(resDTO);
			page1.setItems((List)subs_ratio);
			result.setMap("table1", page1);
			//areas ratio
			double left_cus = 1.00;			
			for(int i=0;i<customers.size()-1;i++){
				AnalysticDTO resDTO_cus = new AnalysticDTO();
				resDTO_cus.setField1(customers.get(i));//brand
				int tmpnum_area = cus2num.get(customers.get(i));
				double ratio_area = (double)(Math.round((tmpnum_area/sumAll)*10000))/10000;
				left_cus-=ratio_area;
				resDTO_cus.setField2(tmpnum_area + "");//number
				resDTO_cus.setField3(ratio_area + "");
				cus_ratio.add(resDTO_cus);
			}
			AnalysticDTO resDTO_cus = new AnalysticDTO();
			resDTO_cus.setField1(customers.get(customers.size()-1));//brand
			int tmpnum_area = cus2num.get(customers.get(customers.size()-1));
			double ratio_area = (double)(Math.round((left_cus)*10000))/10000;
			resDTO_cus.setField2(tmpnum_area + "");//number
			resDTO_cus.setField3(ratio_area + "");
			cus_ratio.add(resDTO_cus);
			page2.setItems((List)cus_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> sub2profit = new HashMap<String, Double>();
			HashMap<String, Double> cus2profit = new HashMap<String, Double>();
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String cus = cDTO.getCustomer_name();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;
				if(sub2profit.containsKey(sub)){
					double sum = sub2profit.get(sub);
					sub2profit.put(sub, sum+profit);
				}else{
					sub2profit.put(sub, profit);
				}
				
				if(cus2profit.containsKey(cus)){
					double sum = cus2profit.get(cus);
					cus2profit.put(cus, sum+profit);
				}else{
					cus2profit.put(cus, profit);
				}
				
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> subs = new ArrayList<String>();
			ArrayList<String> customers = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			subs.addAll(sub2profit.keySet());
			customers.addAll(cus2profit.keySet());
			dates.addAll(trendprofit.keySet());
			
			sortKeySetForProfit(subs, sub2profit);
			sortKeySetForProfit(customers, cus2profit);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForProfit(subs, sub2profit, BRAND_SUB);
			combineKeySetForProfit(customers, cus2profit, AREA_CUS);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<subs.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(subs.get(i));//brand
				double tmpprofit = sub2profit.get(subs.get(i));
				double ratio = (double)(Math.round((tmpprofit/allProfit)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpprofit + "");//number
				resDTO.setField3(ratio + "");
				subs_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(subs.get(subs.size()-1));//brand
			double tmpprofit = sub2profit.get(subs.get(subs.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpprofit + "");//number
			resDTO.setField3(ratio + "");
			subs_ratio.add(resDTO);
			page1.setItems((List)subs_ratio);
			result.setMap("table1", page1);
			//areas ratio
			double left_cus = 1.00;			
			for(int i=0;i<customers.size()-1;i++){
				AnalysticDTO resDTO_cus = new AnalysticDTO();
				resDTO_cus.setField1(customers.get(i));//brand
				double tmpprofit_cus = cus2profit.get(customers.get(i));
				double ratio_cus = (double)(Math.round((tmpprofit_cus/allProfit)*10000))/10000;
				left_cus-=ratio_cus;
				resDTO_cus.setField2(tmpprofit_cus + "");//number
				resDTO_cus.setField3(ratio_cus + "");
				cus_ratio.add(resDTO_cus);
			}
			AnalysticDTO resDTO_cus = new AnalysticDTO();
			resDTO_cus.setField1(customers.get(customers.size()-1));//brand
			double tmpprofit_cus = cus2profit.get(customers.get(customers.size()-1));
			double ratio_cus = (double)(Math.round((left_cus)*10000))/10000;
			resDTO_cus.setField2(tmpprofit_cus + "");//number
			resDTO_cus.setField3(ratio_cus + "");
			cus_ratio.add(resDTO_cus);
			page2.setItems((List)cus_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}
		

		return ret;
	}

	private ResultSetDTO analyzeCase_Six(String brandStr, String customerArea, String customerName, String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> subs_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeIntervalAndBrandAreaName(
				start_time, end_time, brandStr, customerArea, customerName);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			HashMap<String, Integer> sub2num = new HashMap<String, Integer>();
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String sub = cDTO.getSub_brand();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;
				if(sub2num.containsKey(sub)){//sum the brand -> num
					int sum = sub2num.get(sub)+number;
					sub2num.put(sub, sum);
				}else{
					sub2num.put(sub, Integer.valueOf(number));
				}
				
				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area
			ArrayList<String> subs = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			subs.addAll(sub2num.keySet());
			dates.addAll(trends.keySet());
			
			sortKeySetForShipment(subs, sub2num);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForShipment(subs, sub2num, BRAND_SUB);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<subs.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(subs.get(i));//brand
				int tmpnum = sub2num.get(subs.get(i));
				double ratio = (double)(Math.round((tmpnum/sumAll)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpnum + "");//number
				resDTO.setField3(ratio + "");
				subs_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(subs.get(subs.size()-1));//brand
			int tmpnum = sub2num.get(subs.get(subs.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpnum + "");//number
			resDTO.setField3(ratio + "");
			subs_ratio.add(resDTO);
			page1.setItems((List)subs_ratio);
			result.setMap("table1", page1);
			//areas ratio
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> sub2profit = new HashMap<String, Double>();
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String cus = cDTO.getCustomer_name();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;
				if(sub2profit.containsKey(sub)){
					double sum = sub2profit.get(sub);
					sub2profit.put(sub, sum+profit);
				}else{
					sub2profit.put(sub, profit);
				}
								
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> subs = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			subs.addAll(sub2profit.keySet());
			dates.addAll(trendprofit.keySet());
			
			sortKeySetForProfit(subs, sub2profit);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForProfit(subs, sub2profit, BRAND_SUB);
			
			//brands ratio
			double left = 1.00;			
			for(int i=0;i<subs.size()-1;i++){
				AnalysticDTO resDTO = new AnalysticDTO();
				resDTO.setField1(subs.get(i));//brand
				double tmpprofit = sub2profit.get(subs.get(i));
				double ratio = (double)(Math.round((tmpprofit/allProfit)*10000))/10000;
				left-=ratio;
				resDTO.setField2(tmpprofit + "");//number
				resDTO.setField3(ratio + "");
				subs_ratio.add(resDTO);
			}
			AnalysticDTO resDTO = new AnalysticDTO();
			resDTO.setField1(subs.get(subs.size()-1));//brand
			double tmpprofit = sub2profit.get(subs.get(subs.size()-1));
			double ratio = (double)(Math.round((left)*10000))/10000;
			resDTO.setField2(tmpprofit + "");//number
			resDTO.setField3(ratio + "");
			subs_ratio.add(resDTO);
			page1.setItems((List)subs_ratio);
			result.setMap("table1", page1);
			//areas ratio
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}
		

		return ret;
	}

	private ResultSetDTO analyzeCase_Seven(String brandStr, String subStr, String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> area_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeIntervalAndBrandSub(
				start_time, end_time, brandStr, subStr);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			HashMap<String, Integer> area2num = new HashMap<String, Integer>();
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String area = cDTO.getCustomer_area();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;
				
				if(area2num.containsKey(area)){
					int sum = area2num.get(area)+number;
					area2num.put(area, sum);
				}else{
					area2num.put(area, number);
				}
				
				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area
			ArrayList<String> areas = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			areas.addAll(area2num.keySet());
			dates.addAll(trends.keySet());
			
			sortKeySetForShipment(areas, area2num);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForShipment(areas, area2num, AREA_CUS);
			
			//brands ratio
			
			//cus ratio
			double left_area = 1.00;			
			for(int i=0;i<areas.size()-1;i++){
				AnalysticDTO resDTO_area = new AnalysticDTO();
				resDTO_area.setField1(areas.get(i));//brand
				int tmpnum_area = area2num.get(areas.get(i));
				double ratio_area = (double)(Math.round((tmpnum_area/sumAll)*10000))/10000;
				left_area-=ratio_area;
				resDTO_area.setField2(tmpnum_area + "");//number
				resDTO_area.setField3(ratio_area + "");
				area_ratio.add(resDTO_area);
			}
			AnalysticDTO resDTO_area = new AnalysticDTO();
			resDTO_area.setField1(areas.get(areas.size()-1));//
			int tmpnum_area = area2num.get(areas.get(areas.size()-1));
			double ratio_area = (double)(Math.round((left_area)*10000))/10000;
			resDTO_area.setField2(tmpnum_area + "");//number
			resDTO_area.setField3(ratio_area + "");
			area_ratio.add(resDTO_area);
			page2.setItems((List)area_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> area2profit = new HashMap<String, Double>();
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String area = cDTO.getCustomer_area();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;			
				
				if(area2profit.containsKey(area)){
					double sum = area2profit.get(area);
					area2profit.put(area, sum+profit);
				}else{
					area2profit.put(area, profit);
				}
				
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> areas = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			areas.addAll(area2profit.keySet());
			dates.addAll(trendprofit.keySet());
			
			sortKeySetForProfit(areas, area2profit);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForProfit(areas, area2profit, AREA_CUS);
			
			//brands ratio
			
			//areas ratio
			double left_cus = 1.00;			
			for(int i=0;i<areas.size()-1;i++){
				AnalysticDTO resDTO_area = new AnalysticDTO();
				resDTO_area.setField1(areas.get(i));//brand
				double tmpprofit_area = area2profit.get(areas.get(i));
				double ratio_area = (double)(Math.round((tmpprofit_area/allProfit)*10000))/10000;
				left_cus-=ratio_area;
				resDTO_area.setField2(tmpprofit_area + "");//number
				resDTO_area.setField3(ratio_area + "");
				area_ratio.add(resDTO_area);
			}
			AnalysticDTO resDTO_area = new AnalysticDTO();
			resDTO_area.setField1(areas.get(areas.size()-1));//brand
			double tmpprofit_area = area2profit.get(areas.get(areas.size()-1));
			double ratio_area = (double)(Math.round((left_cus)*10000))/10000;
			resDTO_area.setField2(tmpprofit_area + "");//number
			resDTO_area.setField3(ratio_area + "");
			area_ratio.add(resDTO_area);
			page2.setItems((List)area_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}
		

		return ret;
	}
	
	private ResultSetDTO analyzeCase_Eight(String brandStr, String subStr, String customerArea, String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> cus_ratio = new ArrayList<AnalysticDTO>();
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeIntervalAndBrandSubArea(
				start_time, end_time, brandStr, subStr, customerArea);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			HashMap<String, Integer> cus2num = new HashMap<String, Integer>();
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String cus = cDTO.getCustomer_area();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;
				
				if(cus2num.containsKey(cus)){
					int sum = cus2num.get(cus)+number;
					cus2num.put(cus, sum);
				}else{
					cus2num.put(cus, number);
				}
				
				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area
			ArrayList<String> customers = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			customers.addAll(cus2num.keySet());
			dates.addAll(trends.keySet());
			
			sortKeySetForShipment(customers, cus2num);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForShipment(customers, cus2num, AREA_CUS);
			
			//brands ratio
			
			//cus ratio
			double left_cus = 1.00;			
			for(int i=0;i<customers.size()-1;i++){
				AnalysticDTO resDTO_cus = new AnalysticDTO();
				resDTO_cus.setField1(customers.get(i));//brand
				int tmpnum_cus = cus2num.get(customers.get(i));
				double ratio_cus = (double)(Math.round((tmpnum_cus/sumAll)*10000))/10000;
				left_cus-=ratio_cus;
				resDTO_cus.setField2(tmpnum_cus + "");//number
				resDTO_cus.setField3(ratio_cus + "");
				cus_ratio.add(resDTO_cus);
			}
			AnalysticDTO resDTO_cus = new AnalysticDTO();
			resDTO_cus.setField1(customers.get(customers.size()-1));//
			int tmpnum_cus = cus2num.get(customers.get(customers.size()-1));
			double ratio_cus = (double)(Math.round((left_cus)*10000))/10000;
			resDTO_cus.setField2(tmpnum_cus + "");//number
			resDTO_cus.setField3(ratio_cus + "");
			cus_ratio.add(resDTO_cus);
			page2.setItems((List)cus_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> cus2profit = new HashMap<String, Double>();
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String cus = cDTO.getCustomer_name();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;			
				
				if(cus2profit.containsKey(cus)){
					double sum = cus2profit.get(cus);
					cus2profit.put(cus, sum+profit);
				}else{
					cus2profit.put(cus, profit);
				}
				
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> customers = new ArrayList<String>();
			ArrayList<String> dates = new ArrayList<String>();			
			customers.addAll(cus2profit.keySet());
			dates.addAll(trendprofit.keySet());
			
			sortKeySetForProfit(customers, cus2profit);
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			combineKeySetForProfit(customers, cus2profit, AREA_CUS);
			
			//brands ratio
			
			//areas ratio
			double left_cus = 1.00;			
			for(int i=0;i<customers.size()-1;i++){
				AnalysticDTO resDTO_cus = new AnalysticDTO();
				resDTO_cus.setField1(customers.get(i));//brand
				double tmpprofit_cus = cus2profit.get(customers.get(i));
				double ratio_cus = (double)(Math.round((tmpprofit_cus/allProfit)*10000))/10000;
				left_cus-=ratio_cus;
				resDTO_cus.setField2(tmpprofit_cus + "");//number
				resDTO_cus.setField3(ratio_cus + "");
				cus_ratio.add(resDTO_cus);
			}
			AnalysticDTO resDTO_cus = new AnalysticDTO();
			resDTO_cus.setField1(customers.get(customers.size()-1));//brand
			double tmpprofit_cus = cus2profit.get(customers.get(customers.size()-1));
			double ratio_cus= (double)(Math.round((left_cus)*10000))/10000;
			resDTO_cus.setField2(tmpprofit_cus + "");//number
			resDTO_cus.setField3(ratio_cus + "");
			cus_ratio.add(resDTO_cus);
			page2.setItems((List)cus_ratio);
			result.setMap("table2", page2);
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}
		

		return ret;
	}
	
	private ResultSetDTO analyzeCase_Nine(String brandStr, String subStr, String customerArea, String customerName, String start_time, String end_time, String profit_shipment, String timeType) throws Exception{
		
		ResultSetDTO ret = new ResultSetDTO();
		int timeStartMarker = 4;//by default;
		
		//should we abstract this to a method?
		if(timeType.equals(TYPE.MONTH)){
			timeStartMarker = 6;
		}else{
			timeStartMarker = 4;
		}
				
		List<AnalysticDTO> trends_items = new ArrayList<AnalysticDTO>();
		ResultSetDTO result = new ResultSetDTO();
		Pagination page1 = new Pagination();
		Pagination page2 = new Pagination();
		Pagination page3 = new Pagination();
		
		
		ReturnObject tmp = deliverinfo.queryDeliverInfoByTimeIntervalAndBrandSubAreaName(
				start_time, end_time, brandStr, subStr, customerArea, customerName);
		Pagination page = (Pagination) tmp.getReturnDTO();
		List<Object> list = page.getItems();
		
		if (profit_shipment.equals(KIND.SHIPMENT.toString())) {
			
			HashMap<String, Integer> trends = new HashMap<String, Integer>();
			
			int sumAll = 0;
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);
				String cus = cDTO.getCustomer_area();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				sumAll+=number;

				if(trends.containsKey(time)){
					int sum = trends.get(time)+number;
					trends.put(time, sum);
				}else{
					trends.put(time, number);
				}
				
			}//end for
			
			//sort the brand, area

			ArrayList<String> dates = new ArrayList<String>();			
			dates.addAll(trends.keySet());
			

//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			
			//brands ratio
			
			//cus ratio
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trends.get(dates.get(i))+"");
				item.setField2(KIND.SHIPMENT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;

		}else{//profit
			
			HashMap<String, Double> trendprofit = new HashMap<String, Double>();
			
			HashMap<String, Integer> part2num = new HashMap<String, Integer>();			
			for (int i = 0; i < list.size(); i++) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);				
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				int number = Integer.valueOf(cDTO.getQuantity());
				
				if(part2num.containsKey(key)){
					int sum = part2num.get(key)+number;
					part2num.put(key, sum);
				}else{
					part2num.put(key, number);
				}					
			}//end for
			StockMarker smarker = new StockMarker();
			//detect the stock number matched
			for(String key : part2num.keySet()){
				int marker = key.indexOf(":");
				String brand = key.substring(0, marker);
				String sub = key.substring(marker+1);
				int number = part2num.get(key);
				//need to detect??
				ReturnObject retstock = stockinfo.queryStockInfoByBrandAndSub(brand, sub);
				Pagination pagestock = (Pagination) retstock.getReturnDTO();
				List<Object> liststock = pagestock.getItems();
				//sort the stocklist
				sortStockResultByTime(liststock);
				//put the data into stockmarker as an engine
				for(int i=liststock.size()-1; i>-0; i--){
					StockInfoDTO stmp = (StockInfoDTO)liststock.get(i);
					int stocknumber = Integer.valueOf(stmp.getQuantity());
					double stockprice = Double.valueOf(stmp.getUnit_price());
					if(number <= stocknumber){
						smarker.addIntoStocks(key, stocknumber, stockprice);
						break;
					}else{
						smarker.addIntoStocks(key, stocknumber, stockprice);
						number-=stocknumber;
					}					
				}				
			}
			
			
			//calculate the profit, from latest to oldest
			double allProfit = 0.0;
			for (int i = list.size()-1; i >=0; i--) {
				DeliverInfoAllDTO cDTO = (DeliverInfoAllDTO) list.get(i);	
				String brand = cDTO.getBrand();
				String sub = cDTO.getSub_brand();
				String key = brand+":"+sub;
				String cus = cDTO.getCustomer_name();
				String time = cDTO.getDeliver_time().substring(0, timeStartMarker+2);
				int number = Integer.valueOf(cDTO.getQuantity());
				double price = Double.valueOf(cDTO.getUnit_price());
				double profit = smarker.getProfitByKey(key, number, price);
				allProfit+=profit;			

				
				if(trendprofit.containsKey(time)){
					double sum = trendprofit.get(time);
					trendprofit.put(time, sum+profit);
				}else{
					trendprofit.put(time, profit);
				}				
			}
			
			//sort the brand, area
			ArrayList<String> dates = new ArrayList<String>();			
			dates.addAll(trendprofit.keySet());			
//			sortKeySetForTrend(dates);
			
			//combine the hashmap
			
			//brands ratio
			
			//areas ratio			
			
			//trends
			for(int i=0;i<dates.size();i++){
				AnalysticDTO item = new AnalysticDTO();
				item.setField1(trendprofit.get(dates.get(i))+"");
				item.setField2(KIND.PROFIT.toString());
				item.setField3(dates.get(i));
				trends_items.add(item);
			}
			page3.setItems((List)trends_items);
			result.setMap("table3", page3);
			
			ret = result;
		}
		

		return ret;
	}
	
	public ResultSetDTO startAnalyzing(Map<String,Object> params) throws Exception{
		
		ResultSetDTO ro=new ResultSetDTO();
				
		String brand=String.valueOf(params.get("brand"));
		String sub_brand=String.valueOf(params.get("sub_brand"));
		String customer_area=String.valueOf(params.get("area"));
		String customer_name=(String) params.get("customer");
		String end_time=String.valueOf(params.get("time"));
		String profitOrShipment = String.valueOf(params.get("kind"));
		String timeType = String.valueOf(params.get("type"));
		String start_time = calculateStartTimeByEndTime(end_time, timeType);
		
		if(brand.equals(AnalyzerConstants.ALL_BRAND) && customer_area.equals(AnalyzerConstants.ALL_AREA)){			
			ro = analyzeCase_One(start_time, end_time, profitOrShipment, timeType);
		}
		else if(brand.equals(AnalyzerConstants.ALL_BRAND) && customer_name.equals(AnalyzerConstants.ALL_CUSTOMER)){
			ro = analyzeCase_Two(customer_area, start_time, end_time, profitOrShipment, timeType);
		}
		else if(brand.equals(AnalyzerConstants.ALL_BRAND) && !customer_area.equals(AnalyzerConstants.ALL_AREA) && !customer_name.equals(AnalyzerConstants.ALL_CUSTOMER)){
			ro = analyzeCase_Three(customer_area, customer_name, start_time, end_time, profitOrShipment, timeType);
		}
		else if(sub_brand.equals(AnalyzerConstants.ALL_SUB) && customer_area.equals(AnalyzerConstants.ALL_AREA)){
			ro = analyzeCase_Four(brand, start_time, end_time, profitOrShipment, timeType);
		}
		else if(sub_brand.equals(AnalyzerConstants.ALL_SUB) && customer_name.equals(AnalyzerConstants.ALL_CUSTOMER)){
			ro = analyzeCase_Five(brand, customer_area, start_time, end_time, profitOrShipment, timeType);
		}
		else if(sub_brand.equals(AnalyzerConstants.ALL_SUB) && !customer_area.equals(AnalyzerConstants.ALL_AREA) && !customer_name.equals(AnalyzerConstants.ALL_CUSTOMER)){
			ro = analyzeCase_Six(brand, customer_area, customer_name, start_time, end_time, profitOrShipment, timeType);
		}
		else if(!brand.equals(AnalyzerConstants.ALL_BRAND) && !sub_brand.equals(AnalyzerConstants.ALL_SUB) && customer_area.equals(AnalyzerConstants.ALL_AREA)){
			ro = analyzeCase_Seven(brand, sub_brand, start_time, end_time, profitOrShipment, timeType);
		}
		else if(!brand.equals(AnalyzerConstants.ALL_BRAND) && !sub_brand.equals(AnalyzerConstants.ALL_SUB) && customer_name.equals(AnalyzerConstants.ALL_CUSTOMER)){
			ro = analyzeCase_Eight(brand, sub_brand, customer_area, start_time, end_time, profitOrShipment, timeType);
		}
		else if(!brand.equals(AnalyzerConstants.ALL_BRAND) && !sub_brand.equals(AnalyzerConstants.ALL_SUB) && !customer_area.equals(AnalyzerConstants.ALL_AREA) & !customer_name.equals(AnalyzerConstants.ALL_CUSTOMER)){
			ro = analyzeCase_Nine(brand, sub_brand, customer_area, customer_name, start_time, end_time, profitOrShipment, timeType);
		}else{
			//show messages?
		}
		
		return ro;
	}
	
}
