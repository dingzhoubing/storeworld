package com.storeworld.softwarekeyboard;

import java.awt.Toolkit;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.storeworld.utils.BindText2NumberKeyBoard;

public class TestKeyBoard extends Shell {
	private Text text;
	public static TestKeyBoard shell;
	private Text text_1;
	private Text text_3;
	private static int screenH = 0;
	private static int screenW = 0;
	
	private static ArrayList<Text> texts = new ArrayList<Text>();

	public static void center(Shell shell) {
		
		

		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;

		if (shellH > screenH)
			shellH = screenH;

		if (shellW > screenW)
			shellW = screenW;

		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2));
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
			screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
			Display display = Display.getDefault();
			shell = new TestKeyBoard(display);
			shell.setSize((int)(screenW*0.85), (int)(screenH*0.85));
			BindText2NumberKeyBoard.bindTextKeyboard(texts, shell);
			center(shell);
//			shell.setFocus();
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public TestKeyBoard(Display display) {
		super(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);

		
		text = new Text(this, SWT.BORDER);
		text.setBounds(65, 30, 73, 23);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(13, 33, 49, 17);
		lblNewLabel.setText("One:");
		
		Label lblThree = new Label(this, SWT.NONE);
		lblThree.setText("Three:");
		lblThree.setBounds(13, 71, 49, 17);
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(65, 68, 73, 23);
		
		Label lblTwo = new Label(this, SWT.NONE);
		lblTwo.setText("Two:");
		lblTwo.setBounds(163, 33, 49, 17);
		
		text_3 = new Text(this, SWT.BORDER);
		text_3.setBounds(215, 30, 73, 23);
		texts.add(text);
		texts.add(text_1);
		texts.add(text_3);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
