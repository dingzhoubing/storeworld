package com.storeworld.utils;

import java.awt.Toolkit;

import org.eclipse.swt.widgets.Shell;

public class Utils {
	
	public static void center(Shell shell) {
		// 获取屏幕高度和宽度
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		// 获取对象窗口高度和宽度
		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;

		// 如果对象窗口高度超出屏幕高度，则强制其与屏幕等高
		if (shellH > screenH)
			shellH = screenH;

		// 如果对象窗口宽度超出屏幕宽度，则强制其与屏幕等宽
		if (shellW > screenW)
			shellW = screenW;

		// 定位对象窗口坐标
		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2));
	}
	

}
