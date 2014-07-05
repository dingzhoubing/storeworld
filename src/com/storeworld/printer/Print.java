package com.storeworld.printer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.mysql.jdbc.Connection;
import com.storeworld.common.DataInTable;
import com.storeworld.common.NumberConverter;
import com.storeworld.customer.Customer;
import com.storeworld.customer.CustomerList;
import com.storeworld.database.BaseAction;
import com.storeworld.deliver.Deliver;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.deliver.DeliverHistory;
import com.storeworld.deliver.DeliverList;
import com.storeworld.deliver.DeliverUtils;
import com.storeworld.mainui.MainUI;
import com.storeworld.product.Product;
import com.storeworld.pub.service.DeliverInfoService;
import com.storeworld.utils.ItemComposite;
 
public class Print implements Printable, Runnable{
   private int pageSize;//打印的总页数
   private double paperW=609.1937007874016;//609.4;//打印的纸张宽度
   private double paperH=425.1968503937008;//425.2;//打印的纸张高度
   private static final BaseAction baseAction = new BaseAction();
   private ArrayList<DataInTable> elements = new ArrayList<DataInTable>();
   private int part = 0;
   private int total_part = 0;
   private String total = "";
   private String indeed = "";
   private boolean type = false;
   private String ordernum="";
   private Connection conn;
   private String historyIndeed = "";
   private ArrayList<Product> products = new ArrayList<Product>();
   private ArrayList<Customer> customers = new ArrayList<Customer>();	
	
	
   public Print(ArrayList<DataInTable> temp, int part, int total_part, String total, String indeed_val, boolean type, String ordernum, Connection conn){
	   elements.clear();
	   elements.addAll(temp);
	   this.part = part;
	   this.total_part = total_part;
	   this.total = total;
	   this.indeed = indeed_val;
	   this.type = type;
	   this.pageSize = 1;
	   this.ordernum = ordernum;
	   this.conn = conn;
   }
      
	public void setHistoryIndeed(String indeed){
		this.historyIndeed = indeed;
	}
	public String getHistoryIndeed(){
		return this.historyIndeed;
	}
	public void setProductsChanged(ArrayList<Product> products){
		this.products = products;
	}
	public ArrayList<Product> getProductsChanged(){
		return this.products;
	}
	public void setCustomersChanged(ArrayList<Customer> customers){
		this.customers = customers;
	}
	public ArrayList<Customer> getCustomersChanged(){
		return this.customers;
	}
	
   //实现java.awt.print.Printable接口的打印方法
   //pageIndex:打印的当前页，此参数是系统自动维护的，不需要手动维护，系统会自动递增
   @Override
   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
        throws PrinterException {

      if (pageIndex >= pageSize)
        //退出打印
        return Printable.NO_SUCH_PAGE;
      else {
        Graphics2D g2 = (Graphics2D) graphics;
        //g2.setColor(Color.BLUE);
        Paper p = new Paper();
        //此处的paperW和paperH是从目标打印机的进纸规格中获取的，实际针式打印机的可打印区域是有限的，
        //距纸张的上下左右1inch(英寸)的中间的距形框为实际可打印区域，超出范围的内容将不会打印出来(没有设置偏移的情况)
        //如果设置偏移量，那么超出的范围也是可以打印的，这里的pageW和pageH我是直接获取打印机的进纸规格的宽和高
        //也可以手动指定，从是如果手动指定的宽高和目标打印机的进纸规格相差较大，将会默认以A4纸为打印模版
        p.setImageableArea(0, 0, paperW, paperH);// 设置可打印区域
        p.setSize(paperW,paperH);// 设置纸张的大小
        pageFormat.setPaper(p);
        drawCurrentPageText(g2, pageFormat);//调用打印内容的方法
        
        return PAGE_EXISTS;
      }
   }
 
   // 打印内容
   private void drawCurrentPageText(Graphics2D g2, PageFormat pf) {
      Font font = null;
      //设置打印的字体
      font = new Font("新宋体", Font.PLAIN, 18);
      g2.setFont(font);// 设置字体
      //此处打印一句话，打印开始位置是(200,200),表示从pf.getPaper()中座标为(200,200)开始打印
      //此处200的单位是1/72(inch)，inch:英寸，所以这里的长度，在测量后需要进行转换
      if(!this.type)
    	  g2.drawString("送货单",300,100);
      else
    	  g2.drawString("退货单",300,100);
      
      font = new Font("新宋体", Font.PLAIN, 10);
      g2.setFont(font);
      g2.drawString("孝感市三源面粉批发",75,100);
//      g2.drawLine(75, 80, 499, 80);//up border
//      g2.drawLine(75, 345, 499, 345);//bottom border
//      g2.drawLine(75, 80, 75, 345);//left side border
//      g2.drawLine(499, 80, 499, 345);/right side border

      g2.drawString("品牌",95,115);
      g2.drawString("子品牌",185,115);
      g2.drawString("规格",275,115);
      g2.drawString("单位",315,115);
      g2.drawString("单价",355,115);
      g2.drawString("数量",435,115);
      
      g2.drawLine(75, 125, 499, 125);//border under table title

    for(int i=0;i<this.elements.size();i++){//7
    	Deliver d = (Deliver)elements.get(i);
    	g2.drawString(d.getBrand(),97,135+20*i);
        g2.drawString(d.getSubBrand(),187,135+20*i);
        g2.drawString(d.getSize(),277,135+20*i);
        g2.drawString(d.getUnit(),317,135+20*i);
        g2.drawString(d.getPrice(),357,135+20*i);
        g2.drawString(d.getNumber(),437,135+20*i);	
    }
    
    //only print this if meet
    if(this.part == this.total_part){
    	g2.drawString("总计(大写):"+NumberConverter.getInstance().number2CNMontrayUnit(this.total),75,280);
//    g2.drawString("个十百千万",100,280);
    	g2.drawString("总计(小写):"+this.total,310,280);
//    g2.drawString("3000.00",335,280);
    
    	if(!this.type){
    		g2.drawString("实收(大写):"+NumberConverter.getInstance().number2CNMontrayUnit(this.indeed),75,300);
    		g2.drawString("实收(小写):"+this.indeed,310,300);
    	}else{
    		g2.drawString("实退(大写):"+NumberConverter.getInstance().number2CNMontrayUnit(this.indeed),75,300);
    		g2.drawString("实退(小写):"+this.indeed,310,300);
    	}
    
    	g2.drawString("收单人签字盖章:",75,330);
    }
    
   }
   //连接打印机，弹出打印对话框
   public void starPrint() {
      try {
  		
        PrinterJob prnJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = new PageFormat();
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        prnJob.setPrintable(this);
        //弹出打印对话框，也可以选择不弹出打印提示框，直接打印
        
//        if (!prnJob.printDialog())
//           return;
        
        //获取所连接的目标打印机的进纸规格的宽度，单位：1/72(inch)
//        paperW=prnJob.getPageFormat(null).getPaper().getWidth();
        //获取所连接的目标打印机的进纸规格的宽度，单位：1/72(inch)
//        paperH=prnJob.getPageFormat(null).getPaper().getHeight();
//        System.out.println("paperW:"+paperW+";paperH:"+paperH);         
         
//        prnJob.print();//启动打印工作
        
//        //here we update the database
        if(!ordernum.equals("") && this.part == this.total_part){
//        	Connection conn=null;
//			try {
//				conn = baseAction.getConnection();
//			} catch (Exception e1) {
//				Display.getDefault().syncExec(new Runnable() {
//				    public void run() {
//				    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
//						mbox.setMessage("更新送货表为已打印失败");
//						mbox.open();	
//
//				    }
//				    });
//				return;
//			}
//        	
        	DeliverInfoService deliverInfo = new DeliverInfoService();
        	try {
//        		conn.setAutoCommit(false);
				deliverInfo.updateIsPrintByOrderNumber(conn, ordernum);
				conn.commit();
			} catch (Exception e) {
				System.out.println("update is_print failed");
				try {
					if(conn!=null)
						conn.rollback();
				} catch (SQLException e1) {
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
					    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
							mbox.setMessage("数据库异常");
							mbox.open();	

					    }
					    });
				}
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
						mbox.setMessage("数据库异常");
						mbox.open();	

				    }
				    });
				return;
			}finally{
				try {
					if(conn!=null)
						conn.close();
				} catch (SQLException e) {
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
					    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
							mbox.setMessage("数据库异常");
							mbox.open();	
					    }
					    });
				}
			}

          prnJob.print();//启动打印工作
          
            if(DeliverUtils.getReturnMode()){
            	Display.getDefault().syncExec(new Runnable() {
        		    public void run() {
        	           	DeliverHistory dh = (DeliverHistory)DeliverUtils.getItemCompositeRecord().getHistory();
        				//after return, show this
        				dh.setIndeed(historyIndeed);//this is needed
        				DeliverUtils.getItemCompositeRecord().setDownRight(historyIndeed);
        				//change the product table ui side
        				DeliverList.relatedProductChange(products, true);		
        				
                    	DeliverUtils.leaveReturnMode();
                    	
        		    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()));
    					mbox.setMessage("退货打单成功， 请重试");
    					mbox.open();
        		    }
            	});
            	
 
            	
            }else{
            	Display.getDefault().syncExec(new Runnable() {
        		    public void run() {
        		    	//update the customer table
        				CustomerList.relatedCustomerChange(customers);
        				
        				if(DeliverUtils.getStatus().equals("NEW")){
        					DeliverUtils.addToHistory();
        				}
        				
        				//status: NEW, HISTORY, EMPTY, empty mode is necessary?
        				DeliverUtils.setStatus("EMPTY");
        				
        				//step 2: initial the deliver page
        				//clear table
        				//and add a new line					
        				DeliverContentPart.afterPrintFinished();
        				
        		    	ItemComposite ic = DeliverUtils.getItemCompositeRecord();
        		    	DeliverHistory dh = (DeliverHistory)ic.getHistory();
        		    	if(!dh.getTitleShow().contains("已打单"))
        		    		ic.setValue(dh.getTitle()+"(已打单)");
        		    	
        		    	}
            	});
            	
            }

        }
        

        
      } catch (PrinterException ex) {
    	//!!
    	//在非SWT线程的线程里想要修改SWT界面,need to do this
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
		    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
				mbox.setMessage("打印机配置错误，请检查打印机配置或联系客服");
				mbox.open();	
		    }
		    }); 
		
      }
   }
   
   @Override
   public void run() {
	   starPrint();	
   }

////入口方法
//   public static void main(String[] args) {
////      Print pm = new Print();// 实例化打印类
////      pm.pageSize = 1;//打印两页
////      pm.starPrint();
//	   
//	   Thread t1 = new Thread(new Print());
////	   Thread t2 = new Thread(new Print());
//	   
//	   t1.start();
////	   t2.start();
//   }
}
