package com.storeworld.utils;

import java.awt.Toolkit;

import org.eclipse.swt.widgets.Shell;

public class Utils {
	
	public static void center(Shell shell) {
		// ��ȡ��Ļ�߶ȺͿ��
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		// ��ȡ���󴰿ڸ߶ȺͿ��
		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;

		// ������󴰿ڸ߶ȳ�����Ļ�߶ȣ���ǿ��������Ļ�ȸ�
		if (shellH > screenH)
			shellH = screenH;

		// ������󴰿ڿ�ȳ�����Ļ��ȣ���ǿ��������Ļ�ȿ�
		if (shellW > screenW)
			shellW = screenW;

		// ��λ���󴰿�����
		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2));
	}
	

}
