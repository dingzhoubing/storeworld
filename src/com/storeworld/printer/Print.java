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
   private int pageSize;//��ӡ����ҳ��
   private double paperW=609.1937007874016;//609.4;//��ӡ��ֽ�ſ��
   private double paperH=425.1968503937008;//425.2;//��ӡ��ֽ�Ÿ߶�
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
	
   //ʵ��java.awt.print.Printable�ӿڵĴ�ӡ����
   //pageIndex:��ӡ�ĵ�ǰҳ���˲�����ϵͳ�Զ�ά���ģ�����Ҫ�ֶ�ά����ϵͳ���Զ�����
   @Override
   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
        throws PrinterException {

      if (pageIndex >= pageSize)
        //�˳���ӡ
        return Printable.NO_SUCH_PAGE;
      else {
        Graphics2D g2 = (Graphics2D) graphics;
        //g2.setColor(Color.BLUE);
        Paper p = new Paper();
        //�˴���paperW��paperH�Ǵ�Ŀ���ӡ���Ľ�ֽ����л�ȡ�ģ�ʵ����ʽ��ӡ���Ŀɴ�ӡ���������޵ģ�
        //��ֽ�ŵ���������1inch(Ӣ��)���м�ľ��ο�Ϊʵ�ʿɴ�ӡ���򣬳�����Χ�����ݽ������ӡ����(û������ƫ�Ƶ����)
        //�������ƫ��������ô�����ķ�ΧҲ�ǿ��Դ�ӡ�ģ������pageW��pageH����ֱ�ӻ�ȡ��ӡ���Ľ�ֽ���Ŀ�͸�
        //Ҳ�����ֶ�ָ������������ֶ�ָ���Ŀ�ߺ�Ŀ���ӡ���Ľ�ֽ������ϴ󣬽���Ĭ����A4ֽΪ��ӡģ��
        p.setImageableArea(0, 0, paperW, paperH);// ���ÿɴ�ӡ����
        p.setSize(paperW,paperH);// ����ֽ�ŵĴ�С
        pageFormat.setPaper(p);
        drawCurrentPageText(g2, pageFormat);//���ô�ӡ���ݵķ���
        
        return PAGE_EXISTS;
      }
   }
 
   // ��ӡ����
   private void drawCurrentPageText(Graphics2D g2, PageFormat pf) {
      Font font = null;
      //���ô�ӡ������
      font = new Font("������", Font.PLAIN, 18);
      g2.setFont(font);// ��������
      //�˴���ӡһ�仰����ӡ��ʼλ����(200,200),��ʾ��pf.getPaper()������Ϊ(200,200)��ʼ��ӡ
      //�˴�200�ĵ�λ��1/72(inch)��inch:Ӣ�磬��������ĳ��ȣ��ڲ�������Ҫ����ת��
      if(!this.type)
    	  g2.drawString("�ͻ���",300,100);
      else
    	  g2.drawString("�˻���",300,100);
      
      font = new Font("������", Font.PLAIN, 10);
      g2.setFont(font);
      g2.drawString("Т������Դ�������",75,100);
//      g2.drawLine(75, 80, 499, 80);//up border
//      g2.drawLine(75, 345, 499, 345);//bottom border
//      g2.drawLine(75, 80, 75, 345);//left side border
//      g2.drawLine(499, 80, 499, 345);/right side border

      g2.drawString("Ʒ��",95,115);
      g2.drawString("��Ʒ��",185,115);
      g2.drawString("���",275,115);
      g2.drawString("��λ",315,115);
      g2.drawString("����",355,115);
      g2.drawString("����",435,115);
      
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
    	g2.drawString("�ܼ�(��д):"+NumberConverter.getInstance().number2CNMontrayUnit(this.total),75,280);
//    g2.drawString("��ʮ��ǧ��",100,280);
    	g2.drawString("�ܼ�(Сд):"+this.total,310,280);
//    g2.drawString("3000.00",335,280);
    
    	if(!this.type){
    		g2.drawString("ʵ��(��д):"+NumberConverter.getInstance().number2CNMontrayUnit(this.indeed),75,300);
    		g2.drawString("ʵ��(Сд):"+this.indeed,310,300);
    	}else{
    		g2.drawString("ʵ��(��д):"+NumberConverter.getInstance().number2CNMontrayUnit(this.indeed),75,300);
    		g2.drawString("ʵ��(Сд):"+this.indeed,310,300);
    	}
    
    	g2.drawString("�յ���ǩ�ָ���:",75,330);
    }
    
   }
   //���Ӵ�ӡ����������ӡ�Ի���
   public void starPrint() {
      try {
  		
        PrinterJob prnJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = new PageFormat();
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        prnJob.setPrintable(this);
        //������ӡ�Ի���Ҳ����ѡ�񲻵�����ӡ��ʾ��ֱ�Ӵ�ӡ
        
//        if (!prnJob.printDialog())
//           return;
        
        //��ȡ�����ӵ�Ŀ���ӡ���Ľ�ֽ���Ŀ�ȣ���λ��1/72(inch)
//        paperW=prnJob.getPageFormat(null).getPaper().getWidth();
        //��ȡ�����ӵ�Ŀ���ӡ���Ľ�ֽ���Ŀ�ȣ���λ��1/72(inch)
//        paperH=prnJob.getPageFormat(null).getPaper().getHeight();
//        System.out.println("paperW:"+paperW+";paperH:"+paperH);         
         
//        prnJob.print();//������ӡ����
        
//        //here we update the database
        if(!ordernum.equals("") && this.part == this.total_part){
//        	Connection conn=null;
//			try {
//				conn = baseAction.getConnection();
//			} catch (Exception e1) {
//				Display.getDefault().syncExec(new Runnable() {
//				    public void run() {
//				    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
//						mbox.setMessage("�����ͻ���Ϊ�Ѵ�ӡʧ��");
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
							mbox.setMessage("���ݿ��쳣");
							mbox.open();	

					    }
					    });
				}
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
						mbox.setMessage("���ݿ��쳣");
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
							mbox.setMessage("���ݿ��쳣");
							mbox.open();	
					    }
					    });
				}
			}

          prnJob.print();//������ӡ����
          
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
    					mbox.setMessage("�˻��򵥳ɹ��� ������");
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
        		    	if(!dh.getTitleShow().contains("�Ѵ�"))
        		    		ic.setValue(dh.getTitle()+"(�Ѵ�)");
        		    	
        		    	}
            	});
            	
            }

        }
        

        
      } catch (PrinterException ex) {
    	//!!
    	//�ڷ�SWT�̵߳��߳�����Ҫ�޸�SWT����,need to do this
		Display.getDefault().syncExec(new Runnable() {
		    public void run() {
		    	MessageBox mbox = new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.ERROR);
				mbox.setMessage("��ӡ�����ô��������ӡ�����û���ϵ�ͷ�");
				mbox.open();	
		    }
		    }); 
		
      }
   }
   
   @Override
   public void run() {
	   starPrint();	
   }

////��ڷ���
//   public static void main(String[] args) {
////      Print pm = new Print();// ʵ������ӡ��
////      pm.pageSize = 1;//��ӡ��ҳ
////      pm.starPrint();
//	   
//	   Thread t1 = new Thread(new Print());
////	   Thread t2 = new Thread(new Print());
//	   
//	   t1.start();
////	   t2.start();
//   }
}
