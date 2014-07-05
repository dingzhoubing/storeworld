package com.storeworld.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

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
			
//			Thread thread = new Thread(new DataBaseService());
//			thread.start();
			
			int screenH =Constants.LOGIN_HEIGHT+50;
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
//			e.printStackTrace();
			MessageBox messageBox =  new MessageBox(MainUI.getMainUI_Instance(Display.getDefault()), SWT.OK);						
	    	messageBox.setMessage("初始化软件失败，请重新启动"); 	
	    	if (messageBox.open() == SWT.OK){	    			    	
	    		MainUI.getMainUI_Instance(Display.getDefault()).dispose();
	    		System.exit(0);
//	    		return;
	    	}
		}
	}
	
	public static void main(String[] args){
		login();
	}

}
