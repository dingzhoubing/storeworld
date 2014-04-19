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

import com.storeworld.pojo.dto.*;
import com.storeworld.utils.*;
import com.mysql.jdbc.Connection;

public class BaseAction {


	
    /**
    * @return ������ݿ�����
    * @throws SQLException
    * @throws ClassNotFoundException
    */
    public Connection getConnection() throws Exception {
    	Connection connection=null;
    	try{
    		Class.forName(Constants.DRIVER);
    	}catch(ClassNotFoundException e){
    		e.printStackTrace();
    	}
    	try{
    		connection = (Connection) DriverManager.getConnection(Constants.URL,Constants.USERNAME,Constants.PASSWORD);
    		//connection.setAutoCommit(false);   // �������Ӳ��Զ��ύ�����ø����ӽ��еĲ����������µ����ݿ�  
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	return connection;
	}
    
    /**
     * get the next id we need in table
     * @param tablename
     * @return
     * @throws Exception
     */
    public int getNextID(String tablename) throws Exception{
    
       ResultSet rs = null;
       int id = -1;
       Connection connection = this.getConnection();
       PreparedStatement preparedStatement = null;
 	   String sql = "show table status where Name = " + "\""+ tablename + "\"";
       try {    	   
		   preparedStatement=connection.prepareStatement(sql);
		   rs = preparedStatement.executeQuery();
		   if(rs!=null){
			   while(rs.next()){
//				   System.out.println(rs.getString("Auto_increment"));
				   //is this int, the same length of integer in java??
				   String tmp = rs.getString("Auto_increment");
				   if(tmp!=null)
					   id = Integer.valueOf(tmp);				   
				   //else id = -1;
			   }
		   }
       } catch (Exception e) {
    	   throw new Exception("get the id failed");
       }finally{
       	this.closeAll(connection, preparedStatement, null);
       }
       
       return id;
       
    }
    
    /**
    * ִ������ɾ����SQL���
    *
    * @param sql
    *            sql���
    * @param param
    *            ֵ��
    * @param type
    *            ֵ���ͼ�
    * @return ��Ӱ�������
    */
    public int executeUpdate(String sql,List<Object> param) throws Exception {
    	int snum=0;
        Connection connection = this.getConnection();
        PreparedStatement preparedStatement = null;
        
        try{
        	preparedStatement=connection.prepareStatement(sql);
            for(int i =0;i<param.size();i++){
                preparedStatement.setObject(i+1, param.get(i));
            }
            snum= preparedStatement.executeUpdate();
        }catch(SQLException e){
        	e.printStackTrace();
        }finally{
        	this.closeAll(connection, preparedStatement, null);
        }
        return snum;
    }

    /**
    * ִ�в�ѯSQL���
    *
    * @param sql
    *            sql���
    * @param param
    *            ֵ��
    * @param type
    *            ֵ���ͼ�
    * @return �����
     * @throws Exception 
    */
   @SuppressWarnings("null")
public List executeQuery(String sql, List<Object> param) throws Exception {
             ResultSet rs = null;
             List list = null;
             Connection connection=null;
             PreparedStatement preparedStatement = null;
             ReturnObject ro=new ReturnObject();
             Pagination page=new Pagination();
			try {
				connection = this.getConnection();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new Exception("�������ݿ�����ʧ�ܣ�");
			}
            try {
            	 preparedStatement = connection.prepareStatement(sql);
            	 if(param!=null){
	            	 int len=param.size();
	            	 if(len>0){
	                      for (int i = 0; i < len; i++) {
	                    	  preparedStatement.setObject(i+1, param.get(i));
	                      }
	            	 }
            	 }
                      rs = preparedStatement.executeQuery();
                      list = new ArrayList();
                      ResultSetMetaData rsm = rs.getMetaData();
                      Map map = null;
                      while (rs.next()) {
                               map = new HashMap();
                               for (int i = 1; i <= rsm.getColumnCount(); i++) {
                                   //map.put(rsm.getColumnName(i), rs.getObject(rsm.getColumnName(i)));
                            	   map.put(rsm.getColumnLabel(i), rs.getObject(rsm.getColumnLabel(i)));
                               }
                               list.add(map);
                      }
             } catch (SQLException e) {
                      e.printStackTrace();
                      throw new Exception("ִ�����ݿ��ѯ����ʧ�ܣ�");
             }finally{
                      this.closeAll(connection, preparedStatement, rs);
             }
            return list;
            /*page.setItems(list);
            ro.setReturnDTO(page);
            return ro;*/
   }
   
   /**
    * ��ҳ��ѯSQL���
    *
    * @param sql
    *            sql���
    * @param param
    *            ֵ��
    * @param map
    *            ��ҳ����
    * @return �����
    */
   public List executeQueryForPage(String sql, List<Object> param,Map<String,Object> map) {
             ResultSet rs = null;
             List list = null;
             Connection connection=null;
             PreparedStatement preparedStatement = null;
             ReturnObject ro=new ReturnObject();
             Pagination page=null;
             int pageSize=(Integer) map.get("pageSize");
 			 int pageNo=(Integer) map.get("pageNo");
 			 int start_index=(pageNo-1)*pageSize;
			try {
				connection = getConnection();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
            	sql=sql+" limit ?,?";
            	 preparedStatement = connection.prepareStatement(sql);
            	 int total_param_length=param.size()+2;//����pageNo��pageSize
                      for (int i = 0; i < param.size(); i++) {
                    	  preparedStatement.setObject(i+1, param.get(i));
                      }
                      preparedStatement.setObject(total_param_length-1,start_index);
                      preparedStatement.setObject(total_param_length, pageSize);
                      
                      rs = preparedStatement.executeQuery();
                      list = new ArrayList();
                      ResultSetMetaData rsm = rs.getMetaData();
                      Map hashmap = null;
                      while (rs.next()) {
                    	  hashmap=new HashMap<String,Object>();
                          for (int i = 1; i <= rsm.getColumnCount(); i++) {
                            	   hashmap.put(rsm.getColumnName(i), rs.getObject(rsm.getColumnName(i)));
                          }
                               list.add(hashmap);
                      }
             } catch (SQLException e) {
                      e.printStackTrace();
             }finally{
                      closeAll(connection, preparedStatement, rs);
             }
            return list;
            /*page.setItems(list);
            ro.setReturnDTO(page);
            return ro;*/
   }
   
    /**
    * �ر����ݿ�����
    * @param conn���ݿ�����
    * @param prsts  PreparedStatement ����
    * @param rs�����
    */
   public void closeAll(Connection conn, PreparedStatement prsts, ResultSet rs) {
             if (rs != null) {
                      try {
                               rs.close();
                      } catch (SQLException e) {
                               e.printStackTrace();
                      }
             }
             if (prsts != null) {
                      try {
                               prsts.close();
                      } catch (SQLException e) {
                               e.printStackTrace();
                      }
             }
             if (conn != null) {
                      try {
                               conn.close();
                      } catch (SQLException e) {
                               e.printStackTrace();
                      }
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
   public static Timestamp str2Timestamp(String str){
	   Date date=str2Date(str,DEFAULT_FORMAT);
	   return new Timestamp(date.getTime());
   }
   public static Date str2Date(String str,String format){
	   if(null==str||"".equals(str)){
		   return null;
	   }
	// ���û��ָ���ַ���ת���ĸ�ʽ������Ĭ�ϸ�ʽ����ת��
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
   


}