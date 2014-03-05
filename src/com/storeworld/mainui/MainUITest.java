package com.storeworld.mainui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.storeworld.utils.Constants;
import com.storeworld.utils.Utils;

public class MainUITest {

	public static void main(String args[]) {
		try {
			int screenH = Constants.SCREEN_HEIGHT;
			int screenW = Constants.SCREEN_WIDTH;
			Display display = Display.getDefault();
			MainUI shell = new MainUI(display);
			shell.setup(null, null, null, null,
					null, null, null, null, new MainContentPart(shell, SWT.NONE, null, null), null);

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
