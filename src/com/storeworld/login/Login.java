package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.storeworld.mainui.MainUI;
import com.storeworld.utils.Constants;
import com.storeworld.utils.Constants.CONTENT_TYPE;
import com.storeworld.utils.Constants.LOGIN_TYPE;
import com.storeworld.utils.Utils;

/**
 * the entry point of the whole project
 * @author dingyuanxiong
 *
 */
public class Login {

	public static void login() {
		try {
			
			Thread thread = new Thread(new DataBaseService());
			thread.start();
			
			int screenH =Constants.LOGIN_HEIGHT;
			int screenW =Constants.LOGIN_WIDTH;
			Display display = Display.getDefault();
			LoginMainUI shell = LoginMainUI.getMainUI_Instance(display);
			shell.setSize(screenW, screenH);
			shell.setup();
			shell.setContentPart(new LoginContentPart(shell.getContentPart(LOGIN_TYPE.LOGIN_BOTTOM), SWT.NONE, null, null), LOGIN_TYPE.LOGIN_INPUT);
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
	
	public static void main(String[] args){
		login();
	}

}
