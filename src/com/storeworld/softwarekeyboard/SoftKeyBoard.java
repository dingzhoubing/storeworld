package com.storeworld.softwarekeyboard;

import org.eclipse.swt.SWT;
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

import com.storeworld.softwarekeyboard.SoftKeyBoard.KeyBoardAdapter;
import com.storeworld.utils.Constants;
import com.storeworld.utils.Utils;

public class SoftKeyBoard extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Text text;
	protected Shell pShell;
	private int ux = 0;
	private int uy = 0;
	private int dx = 0;
	private int dy = 0;
	private boolean up = false;
	private int KB_WIDTH = 136;
	private int KB_HEIGHT = 190;
	private int Adjust_y = 20;
	private int Adjust_x = 3;
	//layout_adjust means: the north part has a height, so the main part y is not right
	private int layout_adjust = 0;
	//composite_shift means: the left navigate has a width, so the main part x is not right
	private int composite_shift = 0;
	//composite_updown means: the right part itself has a up/down shift, like the stock page
	private int composite_updown = 0;
	private Text text_1;
	
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
			setNumber(text_1, number);
		}
	}
	
	//not a good way, some hard code in this
	public void setPosition(Rectangle srect, Rectangle rect) {
		int sx = srect.x;
		int sy = srect.y;
		if((sy + rect.y + layout_adjust + Adjust_y-1 + composite_updown) > KB_HEIGHT){
			ux = sx + rect.x + Adjust_x + composite_shift;
			uy = sy + rect.y + layout_adjust +composite_updown - KB_HEIGHT;// + Adjust_x
			up = true;
		}else{
			dx = sx + rect.x + Adjust_x + composite_shift;
			dy = sy + rect.y + layout_adjust + composite_updown + rect.height - Adjust_y+4;// + Adjust_x
			up = false;
		}
//		System.out.println("result bounds: "+ ux+" "+uy+" "+KB_WIDTH+" "+KB_HEIGHT);

	}
	
	public SoftKeyBoard(Text text, Shell parent, int style, int composite_shift, int composite_updown) {		
		super(parent, style);
		this.text = text;
		this.pShell = parent;
//		System.out.println("text bounds: "+text.getBounds());
//		System.out.println("parent shell bounds: "+pShell.getBounds());
		layout_adjust = (int)(Constants.SCREEN_HEIGHT * 0.85 * 0.15);//make it from a function
		this.composite_shift = composite_shift;
		this.composite_updown = composite_updown;
//		System.out.println("text parent bounds: "+text.getParent().getParent().getBounds());
		setPosition(this.pShell.getBounds(), this.text.getBounds());
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(136, 216);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellDeactivated(ShellEvent e) {
				
				shell.dispose();			
			}
		});
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
				if(!text_1.getText().trim().equals(""))
				{					
					//only set value when input into the value box
					Utils.setInputNeedChange(true);
					Utils.setInput(text_1.getText());
				}else{
					Utils.setInputNeedChange(false);
				}
				Utils.setClickButton(true);
				shell.dispose();
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
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(5, 140, 88, 27);
		
		final Button btnCheckButton = new Button(shell, SWT.CHECK);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnCheckButton.getSelection())
					Utils.settUseSoftKeyBoard(false);
				else
					Utils.settUseSoftKeyBoard(true);
			}
		});
		btnCheckButton.setBounds(5, 171, 98, 17);
		btnCheckButton.setText("\u4E0D\u518D\u4F7F\u7528");
	}
}
