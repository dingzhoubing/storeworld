package com.storeworld.stock;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.storeworld.login.LoginContentPart;
import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Constants;
import com.storeworld.utils.Utils;

public class StockUITest {

	public static void main(String args[]) {
		try {
			int screenH = Constants.SCREEN_HEIGHT;
			int screenW = Constants.SCREEN_WIDTH;
			Display display = Display.getDefault();
			MainUI shell = new MainUI(display);
			HashMap<String, String> close_min = new HashMap<String,String>();
			close_min.put(Constants.CLOSE_IMAGE, "icon/close_2.png");
			close_min.put(Constants.CLOSE_OVER_IMAGE, "icon/closeover_2.png");
			close_min.put(Constants.CLOSE_DOWN_IMAGE, "icon/closedown_2.png");
			shell.setup(null, null, new StockPart(shell, SWT.NONE, null, null), null,
					null, null, null, null, null,close_min);

			shell.setSize(screenW, screenH);
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
