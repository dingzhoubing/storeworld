package com.storeworld.login;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Constants;
import com.storeworld.utils.Utils;

public class LoginUITest {

	public static void main(String args[]) {
		try {
			int screenH = Constants.LOGIN_HEIGHT;
			int screenW = Constants.LOGIN_WIDTH;
			Display display = Display.getDefault();
			MainUI shell = new MainUI(display);
			shell.setup(null, null, null, null,
					null, null, null, null, new LoginContentPart(shell, SWT.NONE, null, null),null);

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
