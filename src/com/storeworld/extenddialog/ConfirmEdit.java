package com.storeworld.extenddialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.storeworld.database.PasswordHandler;
import com.storeworld.utils.Utils;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;

public class ConfirmEdit extends Dialog {

	protected Object result;
	protected Shell shell;
	private Label label;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private Button button7;
	private Button button8;
	private Button button9;
	private Button button0;
	private Button button_reset;
	private Button button_enter;
	private Button button_cancel;
	private static int counter = 1;//only when counter is 4, we initial the auth_right
	private static String pw = "";//record the password
	private static boolean auth_right = false;//whether the input pw is right or not
	
	private static final String STR_INITIAL = "请输入解锁码进入编辑模式，点击取消退出";
	private static final String STR_FAIL = "输入解锁码错误";
	private Shell parentrecord = null;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ConfirmEdit(Shell parent, int style) {
		super(parent, style);
		parentrecord = parent;
		setText("编辑模式");
	}

	class PasswordSelectionAdapter extends SelectionAdapter{
		
		String val = "";
		PasswordSelectionAdapter(String val){
			this.val = val;
		}
		@Override
		public void widgetSelected(SelectionEvent e) {
			setValue(this.val);
		}
		
	}

	private void setValue(String val){
//		label.setText("");
		switch(counter){
		case 1:
			text_1.setText("*");
			pw+=val;
			counter++;
			break;
		case 2:
			text_2.setText("*");
			pw+=val;
			counter++;
			break;
		case 3:
			text_3.setText("*");
			pw+=val;
			counter++;
			break;
		case 4:
			text_4.setText("*");
			pw+=val;
			counter++;
			authCheck();
			break;			
		}
	}

	private void authCheck(){
		if(pw.equals(PasswordHandler.getPassword())){
			auth_right = true;
		}
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

	private void initialStatus(){
		pw = "";
		text_1.setText("");
		text_2.setText("");
		text_3.setText("");
		text_4.setText("");
		counter = 1;
	}
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		Utils.setEnter(false);//initial
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				initialStatus();
			}
		});
		shell.setSize(400, 250);
		int x = parentrecord.getLocation().x;
		int y = parentrecord.getLocation().y;
		shell.setLocation(x+280, y+235);
		shell.setText(getText());
		
		label = new Label(shell, SWT.NONE|SWT.CENTER);
		label.setBounds(10, 10, 375, 30);
		label.setText(STR_INITIAL);
		
		
		text_1 = new Text(shell, SWT.BORDER|SWT.CENTER);
		text_1.setBounds(117, 45, 40, 25);
//		text_1.setEditable(false);
		text_1.setEnabled(false);
		
		text_2 = new Text(shell, SWT.BORDER|SWT.CENTER);
		text_2.setBounds(157, 45, 40, 25);
//		text_2.setEditable(false);
		text_2.setEnabled(false);
		
		text_3 = new Text(shell, SWT.BORDER|SWT.CENTER);
		text_3.setBounds(197, 45, 40, 25);
//		text_3.setEditable(false);
		text_3.setEnabled(false);
		
		text_4 = new Text(shell, SWT.BORDER|SWT.CENTER);
		text_4.setBounds(237, 45, 40, 25);
//		text_4.setEditable(false);
		text_4.setEnabled(false);
		
		button1 = new Button(shell, SWT.NONE);
		button1.addSelectionListener(new PasswordSelectionAdapter("1"));
		button1.setBounds(90, 85, 40, 27);
		button1.setText("1");
		
		button2 = new Button(shell, SWT.NONE);
		button2.addSelectionListener(new PasswordSelectionAdapter("2"));
		button2.setText("2");
		button2.setBounds(136, 85, 40, 27);
		
		button3 = new Button(shell, SWT.NONE);
		button3.addSelectionListener(new PasswordSelectionAdapter("3"));
		button3.setText("3");
		button3.setBounds(182, 85, 40, 27);
		
		button4 = new Button(shell, SWT.NONE);
		button4.addSelectionListener(new PasswordSelectionAdapter("4"));
		button4.setText("4");
		button4.setBounds(228, 85, 40, 27);
		
		button5 = new Button(shell, SWT.NONE);
		button5.addSelectionListener(new PasswordSelectionAdapter("5"));
		button5.setText("5");
		button5.setBounds(274, 85, 40, 27);
		
		button6 = new Button(shell, SWT.NONE);
		button6.addSelectionListener(new PasswordSelectionAdapter("6"));
		button6.setText("6");
		button6.setBounds(90, 118, 40, 27);
		
		button7 = new Button(shell, SWT.NONE);
		button7.addSelectionListener(new PasswordSelectionAdapter("7"));
		button7.setText("7");
		button7.setBounds(136, 118, 40, 27);
		
		button8 = new Button(shell, SWT.NONE);
		button8.addSelectionListener(new PasswordSelectionAdapter("8"));
		button8.setText("8");
		button8.setBounds(182, 118, 40, 27);
		
		button9 = new Button(shell, SWT.NONE);
		button9.addSelectionListener(new PasswordSelectionAdapter("9"));
		button9.setText("9");
		button9.setBounds(228, 118, 40, 27);
		
		button0 = new Button(shell, SWT.NONE);
		button0.addSelectionListener(new PasswordSelectionAdapter("0"));
		button0.setText("0");
		button0.setBounds(274, 118, 40, 27);
		
		button_reset = new Button(shell, SWT.NONE);
		button_reset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initialStatus();
				auth_right = false;//every time we re-initial, we initial it
			}
		});
		button_reset.setBounds(160, 151, 80, 27);
		button_reset.setText("重置");
		
		button_enter = new Button(shell, SWT.NONE);
		button_enter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(auth_right){//auth is right
					Utils.setEnter(true);
					shell.dispose();
					auth_right = false;//every time we success, we initial it
					pw = "";
				}else{
					Utils.setEnter(false);
					initialStatus();
					label.setText(STR_FAIL);					
				}
			}
		});
		button_enter.setBounds(255, 195, 65, 27);
		button_enter.setText("确定");
		
		button_cancel = new Button(shell, SWT.NONE);
		button_cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		button_cancel.setText("取消");
		button_cancel.setBounds(320, 195, 65, 27);
	}
	
//	public static void main(String[] args){
//		ConfirmEdit ce = new ConfirmEdit(new Shell(), 0);
//		ce.open();
//	}
	
}
