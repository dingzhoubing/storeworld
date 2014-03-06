package com.storeworld.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;

public class customerMatch {

	protected Shell shell;
	private String[] head_char;
	//private Map<String, Object> head_char = new HashMap<String, Object>();

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			customerMatch window = new customerMatch();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		//final String[] head_char;
		final Combo combo_area = new Combo(shell, SWT.NONE);
		combo_area.setBounds(70, 43, 88, 25);
		query_area(combo_area);
		final Combo combo_customer = new Combo(shell, SWT.NONE);
		combo_customer.setBounds(265, 43, 88, 25);
		combo_area.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				head_char=query_customer(combo_area,combo_customer);
			}
		});
		
		combo_customer.addKeyListener(new KeyAdapter(){
			/*public void keyPressed(KeyEvent e1){
				//if(e1.keyCode == SWT.CR){
					int now_input=e1.keyCode;
					String index_str=combo_customer.getText();
					int num=head_char.length;//片区的总用户数
					int act_num=combo_customer.getItemCount();//下拉框中的实际用户数
					combo_customer.remove(0, act_num-1);//将下拉框清空
					
					for(int i=0;i<num;i++){
						combo_customer.add(head_char[i]);//将总用户数依次加入
						String head_str=getPinYinHeadChar(head_char[i]);
						if(!(head_str.contains(index_str))){
							combo_customer.remove(head_char[i]);//剔除不在姓名拼音索引中的用户
						}
					}
				//}
				
			}*/
			public void keyReleased(KeyEvent e0){
				//int now_input=e0.keyCode;
				String index_str=combo_customer.getText();
				int num=head_char.length;//片区的总用户数
				int act_num=combo_customer.getItemCount();//下拉框中的实际用户数
				combo_customer.remove(0, act_num-1);//将下拉框清空
				
				for(int i=0;i<num;i++){
					combo_customer.add(head_char[i]);//将总用户数依次加入
					String head_str=getPinYinHeadChar(head_char[i]);
					if(!(head_str.contains(index_str))){
						combo_customer.remove(head_char[i]);//剔除不在姓名拼音索引中的用户
					}
				}
		}
		});
		/*combo_customer.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unused")
				String index_str=combo_customer.getText();
				int num=head_char.length;
				/*Iterator iter = head_char.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Object val = entry.getValue();
					if(!(((String)key).contains(index_str))){
						combo_customer.remove((String)val);
					}
				}*/
				/*for(int i=0;i<num;i++){
					String head_str=getPinYinHeadChar(head_char[i]);
					if(!(head_str.contains(index_str))){
						combo_customer.remove(head_char[i]);
					}
				}
				
			}
			
		});*/
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(27, 46, 37, 25);
		lblNewLabel.setText("片区");
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(222, 46, 37, 25);
		lblNewLabel_1.setText("客户");

	}
	public void query_area(Combo combo){
		try
	    {
	      String url="jdbc:mysql://127.0.0.1:3306/mysql";//mysql
	      String user="root";
	      String pwd="";
	      
	      //加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
	     Class.forName("com.mysql.jdbc.Driver").newInstance();
	      //建立到MySQL的连接
	       Connection conn = DriverManager.getConnection(url,user, pwd);
	      
	      //执行SQL语句
	       Statement stmt = conn.createStatement();//创建语句对象，用以执行sql语言
	      ResultSet rs = stmt.executeQuery("select distinct ci.customer_area as area from customer_info ci");
	     
	       //处理结果集
	      while (rs.next())
	      {
	        String name = rs.getString("area");
	        combo.add(name);
	      }
	      rs.close();//关闭数据库
	      conn.close();
	    }
	    catch (Exception ex)
	    {
	      System.out.println("Error : " + ex.toString());
	    }
	  }
	
	public String[] query_customer(Combo combo_area,Combo combo_customer){
		try
	    {
	      String url="jdbc:mysql://127.0.0.1:3306/mysql";
	      String user="root";
	      String pwd="";
	      String area=combo_area.getText();
	      
	      //加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
	     Class.forName("com.mysql.jdbc.Driver").newInstance();
	      //建立到MySQL的连接
	       Connection conn = DriverManager.getConnection(url,user, pwd);
	      
	      //执行SQL语句
	       Statement stmt = conn.createStatement();//创建语句对象，用以执行sql语言
	       String sql="select  ci.customer_name as name from customer_info ci where ci.customer_area='"+area+"'";
	      ResultSet rs = stmt.executeQuery(sql);
	     
	      List<String> head_list = new ArrayList<String>();
	      //Map<String, Object> head_map = new HashMap<String, Object>();
	       //处理结果集
	      combo_customer.removeAll();//填充用户列表前，先将原有的用户列表清掉
	      while (rs.next())
	      {
	        String name = rs.getString("name");
	        combo_customer.add(name);
	        //String head_char=getPinYinHeadChar(name);
	        //head_map.put(head_char, name);
	        head_list.add(name);
	      }
	      String[] headstr = new String[head_list.size()];
	      head_list.toArray(headstr);

	      rs.close();//关闭数据库
		  conn.close();
		  return headstr;
	    }
	    catch (Exception ex)
	    {
	      System.out.println("Error : " + ex.toString());
	      return null;
	    }
	  }
	
	/**
     * 得到中文首字母
     * 
     * @param str
     * @return
     */
    public  String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }
}
