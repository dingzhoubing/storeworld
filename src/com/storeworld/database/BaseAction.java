package com.storeworld.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.storeworld.login.DataBaseService;
import com.storeworld.utils.Constants;

public class BaseAction {


    /**
    * @return 获得数据库连接
    * @throws SQLException
    * @throws ClassNotFoundException
    */
    public Connection getConnection() throws Exception {
    	Connection connection=null;
    	try{
    		Class.forName(Constants.DRIVER);
    	}catch (ClassNotFoundException e){    		
    		throw new Exception(DataBaseCommonInfo.DRIVER_NOT_FOUND);
    	}
    	int count=0;
    	//we can try 3 time at most
		while (count < 10) {
			try {
				connection = (Connection) DriverManager.getConnection(
						Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				if(connection != null)
					break;
			} catch (SQLException e) {
				count++;
				//try to start the service again, and record the proc
				//avoid if user kill the service
				Thread thread = new Thread(new DataBaseService());
				thread.start();							
				Thread.sleep(250);
			}
		}
		if(connection == null){
			//do something
			System.out.println("connection still null?, we try more times");
			count =0;
			while (count < 10) {
				try {
					connection = (Connection) DriverManager.getConnection(
							Constants.URL, Constants.USERNAME, Constants.PASSWORD);
					if(connection != null)
						break;
				} catch (SQLException e) {
					count++;
					//try to start the service again, and record the proc
					//avoid if user kill the service
					Thread thread = new Thread(new DataBaseService());
					thread.start();									
					Thread.sleep(250);
				}
			}
			
			if(connection == null){
//				System.out.println("after try another 5 times, connection still null?");
				throw new Exception(DataBaseCommonInfo.CONNECTION_IS_NULL);
			}
		}
    	return connection;
	}
    
    
//    private void createTable(Connection conn, String tablename){
//    	
//    }
    
    
    /**
     * get the next id we need in table
     * @param tablename
     * @return
     * @throws Exception
     */
	public int getNextID(Connection connection, String tablename)
			throws Exception {
//		tablename = tablename+"2";
		ResultSet rs = null;
		int id = -1;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement_test = null;
		PreparedStatement preparedStatement_create1 = null;
		PreparedStatement preparedStatement_create2 = null;
		try {
			boolean exist_table = false;
			// test if the table exist, if not, we create this table
			String sql_test = "show tables like " + "\'%" + tablename + "%\'";
			preparedStatement_test = connection.prepareStatement(sql_test);
			rs = preparedStatement_test.executeQuery();
			if (rs != null) {
				ResultSetMetaData metaDa = rs.getMetaData();
				int colTotal = metaDa.getColumnCount();
				while (rs.next()) {
					for (int i = 0; i < colTotal; i++) {
						String temptext = rs.getString(i + 1);
						if (tablename.equals(temptext)) {
							exist_table = true;
							break;//exist
						}
					}
				}
				if(!exist_table){
					//table not exist, create them
//					createTable(connection, tablename);
					String sql_create1 = "";
					String sql_create2 = "";
					if(tablename.equals("customer_info")){
						sql_create1 = "create  table customer_info(id int AUTO_INCREMENT PRIMARY KEY,customer_area varchar(30),"
								+ "customer_name varchar(20),telephone varchar(20),customer_addr varchar(30), "
								+ "reserve1 varchar(30),reserve2 varchar(30),reserve3 varchar(30))";
					}else if(tablename.equals("goods_info")){
						sql_create1 = "create table goods_info(id int AUTO_INCREMENT PRIMARY KEY,brand varchar(30),sub_brand varchar(30),"
								+ "unit_price float,unit varchar(10), standard varchar(30),repertory int, "
								+ "reserve1 varchar(30),reserve2 varchar(30),reserve3 varchar(30))";
					}else if(tablename.equals("stock_info")){
						sql_create1 = "create table stock_info(id int AUTO_INCREMENT PRIMARY KEY,brand varchar(30),sub_brand varchar(30),"
								+ "unit_price float,unit varchar(10), standard varchar(30),quantity int,stock_time varchar(30),"
								+ "stock_from varchar(30), reserve1 varchar(30),reserve2 varchar(30),reserve3 varchar(30))";
					}else if(tablename.equals("deliver_info")){
						sql_create1 = "create table deliver_info(id int AUTO_INCREMENT PRIMARY KEY,order_num varchar(20),brand varchar(30),"
								+ "sub_brand varchar(30),unit_price float,unit varchar(10),quantity int, standard varchar(30),"
								+ "is_print varchar(1), reserve1 varchar(30),reserve2 varchar(30),reserve3 varchar(30))";
						sql_create2 = " create table deliver_common_info(id varchar(20) PRIMARY KEY,customer_area varchar(30),"
								+ "customer_name varchar(30),deliver_addr varchar(50),total_price float,real_price float,"
								+ "deliver_time varchar(30),is_print varchar(1),telephone varchar(20),reserve1 varchar(30),"
								+ "reserve2 varchar(30),reserve3 varchar(30))";
					}
					if(!sql_create1.equals("")){
						preparedStatement_create1 = connection.prepareStatement(sql_create1);
						preparedStatement_create1.executeUpdate();
					}
					if(!sql_create2.equals("")){
						preparedStatement_create2 = connection.prepareStatement(sql_create2);
						preparedStatement_create2.executeUpdate();
					}
				}
			} else {
				throw new Exception(DataBaseCommonInfo.GET_NEXT_ID_FAILED);
			}

//			tablename =tablename.substring(0, tablename.length()-1);
			String sql = "show table status where Name = " + "\"" + tablename
					+ "\"";

			preparedStatement = connection.prepareStatement(sql);
			rs = preparedStatement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					String tmp = rs.getString("Auto_increment");
					if (tmp != null)
						id = Integer.valueOf(tmp);
					// else id = -1;
				}
			} else {
				throw new Exception(DataBaseCommonInfo.GET_NEXT_ID_FAILED);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			this.closeAll(null, preparedStatement, null);
			this.closeAll(null, preparedStatement_test, null);
			this.closeAll(null, preparedStatement_create1, null);
			this.closeAll(null, preparedStatement_create2, null);
		}

		return id;

	}
    
    /**
     * execute update(insert, delete, update) operations 
     * @param connection
     * @param sql
     * @param param
     * @throws Exception
     */
    public void executeUpdate(Connection connection, String sql,List<Object> param) throws Exception {
//    	int snum=0;
        PreparedStatement preparedStatement = null;       
        try{
			if (connection != null) {
				preparedStatement = connection.prepareStatement(sql);
				for (int i = 0; i < param.size(); i++) {
					preparedStatement.setObject(i + 1, param.get(i));
				}
				preparedStatement.executeUpdate();
			}
        }catch(SQLException e){
        	e.printStackTrace();
        	throw new Exception(DataBaseCommonInfo.EXE_UPDATE_FAILED);
        }finally{
        	this.closeAll(null, preparedStatement, null);
        }
//        return snum;
    }
 
   /**
    * query operations
    * @param connection
    * @param sql
    * @param param
    * @return
    * @throws Exception
    */
	public List<Object> executeQuery(Connection connection, String sql, List<Object> param) throws Exception {
		ResultSet rs = null;
		List<Object> list = null;
		PreparedStatement preparedStatement = null;
//		ReturnObject ro = new ReturnObject();
//		Pagination page = new Pagination();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (param != null) {
				int len = param.size();
				if (len > 0) {
					for (int i = 0; i < len; i++) {
						preparedStatement.setObject(i + 1, param.get(i));
					}
				}
			}
			rs = preparedStatement.executeQuery();
			list = new ArrayList<Object>();
			ResultSetMetaData rsm = rs.getMetaData();
			Map<String, Object> map = null;
			while (rs.next()) {
				map = new HashMap<String, Object>();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					map.put(rsm.getColumnLabel(i),rs.getObject(rsm.getColumnLabel(i)));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			throw new Exception(DataBaseCommonInfo.EXE_QUERY_FAILED);
		} finally {
			this.closeAll(null, preparedStatement, rs);
		}
		return list;
	}


    /**
    * 关闭数据库连接
    * @param conn数据库连接
    * @param prsts  PreparedStatement 对象
    * @param rs结果集
    */
	public void closeAll(Connection conn, PreparedStatement prsts, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (prsts != null) {
				prsts.close();
			}
//			if (conn != null) {
//				conn.close();
//			}
		} catch (SQLException e) {
			//do what?
//			System.out.println("close the connection failed");
		}
	}

   public List<Object> objectArray2ObjectList(Object[] obj){
	   List<Object> params=new ArrayList<Object>();
	   if(obj!=null&&obj.length>0){
		   for(int i=0;i<obj.length;i++){
			   params.add(obj[i]);
		   }
		   return params;
	   }
	   return null;
	   
   }
   
   	private static final String DEFAULT_FORMAT="yyyy-MM-dd";
   
   	@Deprecated
	public static Timestamp str2Timestamp(String str) {
		Date date = str2Date(str, DEFAULT_FORMAT);
		return new Timestamp(date.getTime());
	}

   	@Deprecated
	public static Date str2Date(String str, String format) {
		if (null == str || "".equals(str)) {
			return null;
		}
		// 如果没有指定字符串转换的格式，则用默认格式进行转换
		if (null == format || "".equals(format)) {
			format = DEFAULT_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = (Date) sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
   
//=====================================================================================================================
   /**
    * 分页查询SQL语句
    *
    * @param sql
    *            sql语句
    * @param param
    *            值集
    * @param map
    *            分页参数
    * @return 结果集
    */
//   public List executeQueryForPage(String sql, List<Object> param,Map<String,Object> map) {
//             ResultSet rs = null;
//             List list = null;
//             Connection connection=null;
//             PreparedStatement preparedStatement = null;
//             ReturnObject ro=new ReturnObject();
//             Pagination page=null;
//             int pageSize=(Integer) map.get("pageSize");
// 			 int pageNo=(Integer) map.get("pageNo");
// 			 int start_index=(pageNo-1)*pageSize;
//			try {
//				connection = getConnection();
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//            try {
//            	sql=sql+" limit ?,?";
//            	 preparedStatement = connection.prepareStatement(sql);
//            	 int total_param_length=param.size()+2;//加上pageNo与pageSize
//                      for (int i = 0; i < param.size(); i++) {
//                    	  preparedStatement.setObject(i+1, param.get(i));
//                      }
//                      preparedStatement.setObject(total_param_length-1,start_index);
//                      preparedStatement.setObject(total_param_length, pageSize);
//                      
//                      rs = preparedStatement.executeQuery();
//                      list = new ArrayList();
//                      ResultSetMetaData rsm = rs.getMetaData();
//                      Map hashmap = null;
//                      while (rs.next()) {
//                    	  hashmap=new HashMap<String,Object>();
//                          for (int i = 1; i <= rsm.getColumnCount(); i++) {
//                            	   hashmap.put(rsm.getColumnName(i), rs.getObject(rsm.getColumnName(i)));
//                          }
//                               list.add(hashmap);
//                      }
//             } catch (SQLException e) {
//                      e.printStackTrace();
//             }finally{
//                      closeAll(connection, preparedStatement, rs);
//             }
//            return list;
//            /*page.setItems(list);
//            ro.setReturnDTO(page);
//            return ro;*/
//   }
   
   
   
   
// /**
//  * start the transaction, from the Internet
//  * @param con
//  * @param sqls
//  * @throws Exception
//  */
// public void StartTransaction(Connection con, String[] sqls) throws Exception {  
//     
//     if (sqls == null) {  
//         return;  
//     }  
//     Statement sm = null;  
//     try {  
//         // 事务开始  
//         System.out.println("事务处理开始！");  
//         con.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库  
//         sm = con.createStatement(); // 创建Statement对象  
//           
//         //依次执行传入的SQL语句  
//         for (int i = 0; i < sqls.length; i++) {  
//             sm.execute(sqls[i]);// 执行添加事物的语句  
//         }  
//         System.out.println("提交事务处理！");  
//           
//         con.commit();   // 提交给数据库处理  
//           
//         System.out.println("事务处理结束！");  
//         // 事务结束  
//           
//     //捕获执行SQL语句组中的异常      
//     } catch (SQLException e) {  
//         try {  
//             System.out.println("事务执行失败，进行回滚！\n");  
//             con.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作  
//         } catch (SQLException e1) {  
//             e1.printStackTrace();  
//         }  
//     } finally {  
//         sm.close();  
//     }  
// }
// 
   
   /**
    * 执行增、删、改SQL语句
    *
    * @param sql
    *            sql语句
    * @param param
    *            值集
    * @param type
    *            值类型集
    * @return 受影响的行数
    */
//    public int executeUpdate(String sql,List<Object> param) throws Exception {
//    	int snum=0;
//    	Connection connection = null;
//    	try{
//    		connection = this.getConnection();
//    	}catch (Exception e1) {
//			throw new Exception(DataBaseCommonInfo.CONNECTION_FAILED);
//		}
//        PreparedStatement preparedStatement = null;
//        
//        try{
//			if (connection != null) {
//				preparedStatement = connection.prepareStatement(sql);
//				for (int i = 0; i < param.size(); i++) {
//					preparedStatement.setObject(i + 1, param.get(i));
//				}
//				snum = preparedStatement.executeUpdate();
//			}
//        }catch(SQLException e){
//        	throw new Exception(DataBaseCommonInfo.EXE_UPDATE_FAILED);
//        }finally{
//        	this.closeAll(connection, preparedStatement, null);
//        }
//        return snum;
//    }
   
   /**
   * 执行查询SQL语句
   *
   * @param sql
   *            sql语句
   * @param param
   *            值集
   * @param type
   *            值类型集
   * @return 结果集
    * @throws Exception 
   */
//  @SuppressWarnings("null")
//public List executeQuery(String sql, List<Object> param) throws Exception {
//            ResultSet rs = null;
//            List list = null;
//            Connection connection=null;
//            PreparedStatement preparedStatement = null;
//            ReturnObject ro=new ReturnObject();
//            Pagination page=new Pagination();
//			try {
//				connection = this.getConnection();
//			} catch (Exception e1) {
//				throw new Exception(DataBaseCommonInfo.CONNECTION_FAILED);
//			}
//           try {
//           	 preparedStatement = connection.prepareStatement(sql);
//           	 if(param!=null){
//	            	 int len=param.size();
//	            	 if(len>0){
//	                      for (int i = 0; i < len; i++) {
//	                    	  preparedStatement.setObject(i+1, param.get(i));
//	                      }
//	            	 }
//           	 }
//                     rs = preparedStatement.executeQuery();
//                     list = new ArrayList();
//                     ResultSetMetaData rsm = rs.getMetaData();
//                     Map map = null;
//                     while (rs.next()) {
//                              map = new HashMap();
//                              for (int i = 1; i <= rsm.getColumnCount(); i++) {
//                                  //map.put(rsm.getColumnName(i), rs.getObject(rsm.getColumnName(i)));
//                           	   map.put(rsm.getColumnLabel(i), rs.getObject(rsm.getColumnLabel(i)));
//                              }
//                              list.add(map);
//                     }
//            } catch (SQLException e) {
//           	 throw new Exception(DataBaseCommonInfo.EXE_QUERY_FAILED);
//            }finally{
//                this.closeAll(connection, preparedStatement, rs);
//            }
//           return list;
//  }
//  

}