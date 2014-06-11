package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.storeworld.analyze.AnalyzeContentPart;
import com.storeworld.customer.CustomerContentPart;
import com.storeworld.deliver.DeliverContentPart;
import com.storeworld.mainui.CoolBarPart;
import com.storeworld.mainui.MainContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.product.ProductContentPart;
import com.storeworld.stock.StockContentPart;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

/**
 * the entry part of the main content part
 * @author dingyuanxiong
 *
 */
public class EntryPoint {

	public static MainUI entry(ProgressBar progress, Composite parent) {
		MainUI shell = null;
		try {
			
			Thread thread = new Thread(new DataBaseService());
			thread.start();
			
			//fixed  with & height
			int screenH =720;
			int screenW =960;

			Display display = Display.getDefault();
			shell = MainUI.getMainUI_Instance(display);
			//set the percent of the north part
			shell.setSize(screenW, screenH);
			shell.setRatio(0.15);//no use in fixed with & height
			shell.setup();
			if(progress!=null)
				progress.setSelection(5);
			shell.setContentPart(new MainContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_MAIN);
			shell.show_Content_main();
			if(progress!=null)
				progress.setSelection(20);
			
			if(Utils.getNorthPartComposites(NORTH_TYPE.NORTH_INDEX) == null)
				shell.setNorthPart(new CoolBarPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);

			if(progress!=null)
				progress.setSelection(35);
			System.out.println("index");
			
			if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_STOCK) == null)
				shell.setContentPart(new StockContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_STOCK);

			if(progress!=null)
				progress.setSelection(45);
			System.out.println("stock");
			
			if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_DELIVER) == null)
				shell.setContentPart(new DeliverContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_DELIVER);

			if(progress!=null)
				progress.setSelection(65);
			System.out.println("deliver");
			
			if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_ANALYZE) == null)
				shell.setContentPart(new AnalyzeContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_ANALYZE);

			if(progress!=null)
				progress.setSelection(75);
			System.out.println("analyze");
			
			if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_PRODUCT) == null)
				shell.setContentPart(new ProductContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_PRODUCT);

			if(progress!=null)
				progress.setSelection(85);
			System.out.println("product");;
			
			if(Utils.getContentPartComposites(CONTENT_TYPE.CONTENT_CUSTOMER) == null)
				shell.setContentPart(new CustomerContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_CUSTOMER);

			if(progress!=null)
				progress.setSelection(100);
			System.out.println("customer");
			
			Utils.center(shell);
			if(parent!=null)
				parent.dispose();
			shell.open();
			
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shell;
	}
	public static void main(String [] args){
		entry(null, null);
	}

}
