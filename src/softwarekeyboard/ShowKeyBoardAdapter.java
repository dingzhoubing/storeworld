package softwarekeyboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ShowKeyBoardAdapter extends MouseAdapter {

	/**
	 * change the context of the text area
	 * 
	 * @param text
	 * @param number
	 */
	public static void setNumber(Text text, String number) {
		String cur_str = text.getText();
		if (number.equals("del")) {
			if (!cur_str.equals("")) {
				int len = cur_str.length();
				cur_str = cur_str.substring(0, len - 1);
				text.setText(cur_str);
			}
		} else {
			text.setText(cur_str.concat(number));
		}
		int pos = cur_str.length() + 1;
		text.setSelection(pos, pos);
	}

	/**
	 * each button of the soft number key board add this listener
	 * 
	 * @author dingyuanxiong
	 * 
	 */
	class KeyBoardAdapter extends SelectionAdapter {
		private String number = "";

		KeyBoardAdapter(String number) {
			this.number = number;
		}

		public void widgetSelected(SelectionEvent e) {
			setNumber(text, number);
		}
	}

	/**
	 * the detail of the software key board, can be modified
	 * 
	 * @author dingyuanxiong
	 * 
	 */
	class NumKeyBoard extends Dialog {
		protected Object result;
		protected Shell shell;
		private int ux = 0;
		private int uy = 0;
		private int dx = 0;
		private int dy = 0;
		private boolean up = false;
		private int KB_WIDTH = 136;
		private int KB_HEIGHT = 171;
		private int Adjust_x = 23;
		private int Adjust_y = 3;
		
		public NumKeyBoard(Shell parent, int style) {
			super(parent, style);
			setText("number key board");
		}

		/**
		 * set the location of the number keyboard
		 * 
		 * @param srect
		 *            the rectangle of the shell
		 * @param rect
		 *            the rectangle of the text
		 */
		public void setPosition(Rectangle srect, Rectangle rect) {
			int sx = srect.x;
			int sy = srect.y;
			if((sy + rect.y + Adjust_x) > KB_HEIGHT){
				ux = sx + rect.x;
				uy = sy + rect.y - KB_HEIGHT + Adjust_x;
				up = true;
			}else{
				dx = sx + rect.x;
				dy = sy + rect.y + rect.height + Adjust_x;
				up = false;
			}

		}

		public Object open() {
			createContents();
			shell.open();
			shell.layout();
			shell.setFocus();
			Display display = getParent().getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			return result;
		}

		private void createContents() {
			shell = new Shell(getParent(), SWT.NONE);
			shell.addShellListener(new ShellAdapter() {
				@Override
				public void shellDeactivated(ShellEvent e) {
					shell.dispose();
					while (!shell.isDisposed()) {
					}
					changed = false;
				}
			});
			// the keyboard size, the size of the keyboard can be changed
			shell.setSize(KB_WIDTH, KB_HEIGHT);
			// the area of the keyboard
			if(up)
				shell.setBounds(this.ux, this.uy, KB_WIDTH, KB_HEIGHT);
			else
				shell.setBounds(this.dx, this.dy + Adjust_y, KB_WIDTH, KB_HEIGHT);

			// the OK button of the keyboard
			Button button = new Button(shell, SWT.NONE);
			button.setBounds(94, 140, 36, 27);
			button.setText("\u786E\u5B9A");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					shell.dispose();
					while (!shell.isDisposed()) {
					}
					changed = false;
				}
			});

			// the composite to put the number button
			Composite composite = new Composite(shell, SWT.NONE);
			composite.setBounds(0, 0, 130, 134);
			composite.setLayout(null);

			// the number|operation button
			Button btnNewButton = new Button(composite, SWT.NONE);
			btnNewButton.addSelectionListener(new KeyBoardAdapter("1"));
			btnNewButton.setBounds(5, 5, 38, 27);
			btnNewButton.setText("1");

			Button button_1 = new Button(composite, SWT.NONE);
			button_1.addSelectionListener(new KeyBoardAdapter("2"));
			button_1.setBounds(49, 5, 38, 27);
			button_1.setText("2");

			Button button_2 = new Button(composite, SWT.NONE);
			button_2.addSelectionListener(new KeyBoardAdapter("3"));
			button_2.setBounds(93, 5, 38, 27);
			button_2.setText("3");

			Button button_3 = new Button(composite, SWT.NONE);
			button_3.addSelectionListener(new KeyBoardAdapter("4"));
			button_3.setBounds(5, 38, 38, 27);
			button_3.setText("4");

			Button button_4 = new Button(composite, SWT.NONE);
			button_4.addSelectionListener(new KeyBoardAdapter("5"));
			button_4.setBounds(49, 38, 38, 27);
			button_4.setText("5");

			Button button_5 = new Button(composite, SWT.NONE);
			button_5.addSelectionListener(new KeyBoardAdapter("6"));
			button_5.setBounds(93, 38, 38, 27);
			button_5.setText("6");

			Button button_6 = new Button(composite, SWT.NONE);
			button_6.addSelectionListener(new KeyBoardAdapter("7"));
			button_6.setBounds(5, 71, 38, 27);
			button_6.setText("7");

			Button button_7 = new Button(composite, SWT.NONE);
			button_7.addSelectionListener(new KeyBoardAdapter("8"));
			button_7.setBounds(49, 71, 38, 27);
			button_7.setText("8");

			Button button_8 = new Button(composite, SWT.NONE);
			button_8.addSelectionListener(new KeyBoardAdapter("9"));
			button_8.setBounds(93, 71, 38, 27);
			button_8.setText("9");

			Button button_9 = new Button(composite, SWT.NONE);
			button_9.addSelectionListener(new KeyBoardAdapter("0"));
			button_9.setBounds(5, 104, 38, 30);
			button_9.setText("0");

			Button button_10 = new Button(composite, SWT.NONE);
			button_10.addSelectionListener(new KeyBoardAdapter("."));
			button_10.setBounds(49, 104, 38, 30);
			button_10.setText(".");

			Button button_11 = new Button(composite, SWT.NONE);
			button_11.addSelectionListener(new KeyBoardAdapter("del"));
			button_11.setText("<---");
			button_11.setBounds(93, 104, 38, 30);

		}

	}

	// make the click useful everytime click the text
	private boolean changed = false;
	private Text text;
	private Shell shell;

	/**
	 * 
	 * @param text
	 *            : the text to write info
	 * @param shell
	 *            : the parent of the number keyboard(can be changed)
	 * @wbp.parser.entryPoint
	 */
	public ShowKeyBoardAdapter(Text text, Shell shell) {
		this.text = text;
		this.shell = shell;
	}

	// override of the mouseDown event listener
	@Override
	public void mouseDown(MouseEvent e) {
		if (!changed) {
			changed = true;
			text.setText("");
			NumKeyBoard nkb = new NumKeyBoard(shell, 0);
			nkb.setPosition(shell.getBounds(), text.getBounds());
			nkb.open();
		}
	}

}
