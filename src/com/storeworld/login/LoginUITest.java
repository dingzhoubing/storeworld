package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Constants;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Utils;

//can make this one instance in the sysytem
public class LoginUITest {

	public static void main(String[] args) {
		try {
			
			int screenH =Constants.LOGIN_HEIGHT;
			int screenW =Constants.LOGIN_WIDTH;
//			System.out.println(screenH + ":" + screenW + "swh");
			Display display = Display.getDefault();
			MainUI shell = MainUI.getMainUI_Instance(display);
			//set the percent of the north part
			shell.setSize(screenW, screenH);
//			shell.setRatio(0.15);
			shell.setup();
//			shell.setNorthPart(new StockPart(shell.getNorthPart(NORTH_TYPE.NORTH_BOTTOM), SWT.NONE, null, null), NORTH_TYPE.NORTH_INDEX);
			shell.setContentPart(new LoginContentPart(shell.getContentPart(CONTENT_TYPE.CONTENT_BOTTOM), SWT.NONE, null, null), CONTENT_TYPE.CONTENT_MAIN);
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
