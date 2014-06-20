package com.storeworld.extenddialog;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;

public class ShowUpdateProgress extends Dialog implements Runnable{

	
	class IncresingOperator extends Thread {  
        private ProgressBar bar;  
  
        IncresingOperator(ProgressBar bar) {  
            this.bar = bar;  
        }  
  
        public void run() {  
           
            Display.getDefault().asyncExec(new Runnable() {  
                public void run() {                     
                    if (bar.isDisposed())  
                        return;  
                    for (int i = 0; i < bar.getMaximum(); i++) {  
                        try {                           
                            Thread.sleep(100);  
                        } catch (InterruptedException e) {  
                            e.printStackTrace();  
                        }                         
                        bar.setSelection(bar.getSelection() + 1);  
                    }  
                }  
            });  
        }  
    } 
	
	
	protected Object result;
	protected Shell shell;
	private ProgressBar progressBar;
	private Label tips = null;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ShowUpdateProgress(Shell parent, int style) {
		super(parent, style);
//		setText("SWT Dialog");
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

//	public void setStatus(String txt, int process){
//		tips.setText(txt);
//		progressBar.setSelection(process);
//	}
	
	public void dispose(){
		this.dispose();
	}
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.NONE);
		shell.setSize(450, 214);
		shell.setText(getText());
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(46, 28, 348, 17);
		lblNewLabel.setText("正在更新数据库，请您耐心等候...");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 51, 448, 161);
		
		progressBar = new ProgressBar(composite, SWT.HORIZONTAL| SWT.INDETERMINATE);
		progressBar.setBounds(0, 146, 448, 15);
		progressBar.setMaximum(30);
		progressBar.setMinimum(0);
		progressBar.setVisible(true);
		
		tips = new Label(composite, SWT.NONE);
		tips.setAlignment(SWT.CENTER);
		tips.setBounds(76, 32, 313, 17);

	}

//	public void showProgress(){
//
//	}

	@Override
	public void run() {
		(new IncresingOperator(progressBar)).start();  
		open();		
		
	}

}
