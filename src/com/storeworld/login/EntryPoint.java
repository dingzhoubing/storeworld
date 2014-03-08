package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.storeworld.mainui.MainContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.stock.StockPart;
import com.storeworld.utils.Constants;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.NORTH_TYPE;
import com.storeworld.utils.Utils;

//can make this one instance in the sysytem
public class EntryPoint {

	public static void main(String[] args) {
		try {
			
			int screenH =(int)(Constants.SCREEN_HEIGHT * 0.85);
			int screenW =(int)(Constants.SCREEN_WIDTH * 0.8);
//			System.out.println(screenH + ":" + screenW + "swh");
			Display display = Display.getDefault();
			MainUI shell = MainUI.getMainUI_Instance(display);
			//set the percent of the north part
			shell.setSize(screenW, screenH);
			shell.setRatio(0.15);
			shell.setup();
//			shell.setNorthPart(new StockPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
			shell.setContentPart(new MainContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_MAIN);
//			shell.show_North_index();
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

}
