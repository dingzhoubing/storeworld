package com.storeworld.test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Text;

//import ui.storeworld.DesktopLock.DrawPanel;





import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
public class testWindow2 implements Runnable{

	protected Shell shell;
	private Text text;
	private JFrame chooseFrame=new JFrame("选择类型");
	private JFrame lockFrame = new JFrame("系统锁");
	private JFrame uFrame=new JFrame("U盘锁");
	private JButton uButton=new JButton("U盘锁");
	private JButton okButton=new JButton("确定");
	private JButton refreshButton=new JButton("刷新");
	private JLabel diskInfo=new JLabel("请选择移动磁盘:");
	private JLabel unlockLabel=new JLabel("解锁");
	private JComboBox diskBox=new JComboBox();
	private Toolkit tool = Toolkit.getDefaultToolkit();
	private Robot robot = null;
	private final int width = (int) tool.getScreenSize().getWidth();
	private final int height = (int) tool.getScreenSize().getHeight();
	private int intMark = 0;
	private String keyChar = null;
	private String[] lowerUnLockValue = null;
	private String type="";
	private String filepath="";
	private String lockpsw;
	static Thread checkThread;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testWindow2 window = new testWindow2();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		init();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(null);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(153, 39, 60, 27);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("100");
			}
		});
		btnNewButton.setText("进货");
		
		CLabel label = new CLabel(shell, SWT.NONE);
		label.setBounds(33, 39, 30, 23);
		label.setText("\u6570\u91CF");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(69, 39, 73, 23);
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//if (((MouseEvent) e).getButton() == MouseEvent.BUTTON1){
					try {
						pswLock();
						checkThread.start();
					} catch (AWTException e1) {
						e1.printStackTrace();
					//}
				}
			}
		});
		btnNewButton_2.setBounds(318, 10, 80, 27);
		btnNewButton_2.setText("锁屏");
		
		/*JButton btnNewButton_1 = new JButton();
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1){
					try {
						pswLock();
						checkThread.start();
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}
			}
		});*/
		/*btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});*/
		//btnNewButton_1.setBounds(344, 10, 80, 27);
		//btnNewButton_1.setText("锁屏");

	}
	
	public void pswLock() throws AWTException{
		String message="";
		String remessage="";
		do{
			message = JOptionPane.showInputDialog(lockFrame, "请输入解锁码:", "设置解锁码",
					JOptionPane.INFORMATION_MESSAGE);
			if (message==null || message.equals("")) {
				System.exit(0);
			}
			remessage = JOptionPane.showInputDialog(lockFrame, "请重复输入解锁码:", "设置解锁码",
					JOptionPane.INFORMATION_MESSAGE);
			if (remessage==null) {
				System.exit(0);
			}
			if(!message.equals(remessage)){
				JOptionPane.showMessageDialog(lockFrame, "两次输入解锁码不一致,请重新输入!", "错误", JOptionPane.WARNING_MESSAGE);
			}else {
				lowerUnLockValue = new String[message.length()];
				for (int i = 0; i < message.length(); i++) {
					lowerUnLockValue[i] = String.valueOf(message
							.toLowerCase().charAt(i));
				}
			}
		}while(!message.equals(remessage));
		lock();
	}
	
	public void lock(){
		lockFrame.setContentPane(new DrawPanel());
		lockFrame.setVisible(true);
		lockMouse();
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		lockFrame.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 524) {
					robot.keyRelease(e.getKeyCode());
					robot.keyPress(e.getKeyCode());
					robot.keyRelease(e.getKeyCode());
					robot.keyPress(27);
					robot.keyRelease(27);
				} else if (e.getKeyCode() == 18) {
					robot.keyRelease(e.getKeyCode());
					robot.keyPress(524);
					robot.keyRelease(524);
					robot.keyPress(17);
				} else {
					robot.keyRelease(e.getKeyCode());
				}
				lockFrame.toFront();
			}

			public void keyReleased(KeyEvent e) {
				robot.keyRelease(17);
			}

			public void keyTyped(KeyEvent e) {
					keyChar = String.valueOf(e.getKeyChar()).toLowerCase();
					if (keyChar.equals(lowerUnLockValue[intMark])) {
						intMark++;
					} else {
						intMark = 0;
					}
					if (intMark == (lowerUnLockValue.length)) {
						System.exit(0);
					}
			}

		});
		lockFrame.addWindowFocusListener(new WindowFocusListener() {

			public void windowGainedFocus(WindowEvent e) {
				
			}

			public void windowLostFocus(WindowEvent e) {
				lockFrame.toFront();
			}

		});
		lockFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				lockFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
	}
	
	public void lockMouse() {
		lockFrame.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				unlockLabel.setForeground(Color.black);
				Point mousepoint = MouseInfo.getPointerInfo().getLocation();
				int x=mousepoint.x,y=mousepoint.y;
				if(x>(width/2+100)){
					robot.mouseMove((width/2-100), y);
				}else if(x<(width/2-100)){
					robot.mouseMove((width/2+100), y);
				}else if(y>(height*(2f/8f))){
					robot.mouseMove(x, (int)(height*(1f/8f)));
				}else if(y<(height*(1f/8f))){
					robot.mouseMove(x, (int)(height*(2f/8f)));
				}
			}
			public void mouseDragged(MouseEvent e) {
			}
		});
	}
	
	class DrawPanel extends JPanel {
		public void paint(Graphics g) {
			super.paint(g);
			setLayout(null);
			String[] about = { "系统锁", "版本: 2.1"};
				this.setBackground(Color.BLACK);
				g.setColor(Color.WHITE);
			g.setFont(new Font("幼圆", Font.BOLD, 200));
			g.drawString("锁", width/2-110, height/2+70);
			g.drawOval(width / 2 - 135, height / 2 - 135, 270, 270);
			g.setFont(new Font("幼圆", Font.PLAIN, 18));
			g.drawString(about[0], (int)(width*(3f/4f)), (int)(height*(3f/4f)));
			g.drawString(about[1], (int)(width*(3f/4f)), (int)(height*(3f/4f))+30);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(50);
				if(!lockFrame.isMaximumSizeSet() || !lockFrame.isActive()){
					lockFrame.toFront();
					lockFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void init(){
		checkThread=new Thread(this);
	}
}
