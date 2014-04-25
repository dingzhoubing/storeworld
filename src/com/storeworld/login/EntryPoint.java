package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.storeworld.mainui.CoolBarPart;
import com.storeworld.mainui.MainContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Constants;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

/**
 * the entry part of the main content part
 * @author dingyuanxiong
 *
 */
public class EntryPoint {

	public static void entry() {
		try {
			//fixed  with & height
			int screenH =720;
			int screenW =960;

			Display display = Display.getDefault();
			MainUI shell = MainUI.getMainUI_Instance(display);
			//set the percent of the north part
			shell.setSize(screenW, screenH);
			shell.setRatio(0.15);//no use in fixed with & height
			shell.setup();
			shell.setContentPart(new MainContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_MAIN);
			shell.show_Content_main();

			Utils.center(shell);
			shell.open();
			
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String [] args){
		entry();
	}

}
