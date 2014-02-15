package ui.storeworld;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Button;

public class testWindow2 {

	protected Shell shell;

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
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setText("New Button");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new RowData(62, 30));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

	}
}
