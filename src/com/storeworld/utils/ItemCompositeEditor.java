package com.storeworld.utils;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * have no use now, if need can extend this class to fill a columnItem in table
 * @author dingyuanxiong
 *
 */
public class ItemCompositeEditor extends CellEditor {

	private int id = 0;
	private Color color;
	private int width;
	private int height;
	private Composite parent;
	protected Composite history;
	public void setID(int id){
		this.id = id;
	}
	public int getID(){
		return this.id;
	}
	
	private Text up;
//	private Text down_left;
//	private Text down_right;
	
	public ItemCompositeEditor(Composite parent, int style, Color color, int width, int height) {
		this(parent, 0);
		this.parent = parent;
		this.color = color;
		this.width = width;
		this.height = height;
//		createControl(parent);
	}
	 public ItemCompositeEditor(Composite parent, int style) {
	        super(parent, 0);
	}
	private void setSize() {
		
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_text.widthHint = (int)((this.width-4)/2);
		gd_text.heightHint = (int)(this.height/2);
		up.setLayoutData(gd_text);
				
//		GridData gd_text_2 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
//		gd_text_2.widthHint = (int)((this.width-4)/2);
//		gd_text_2.heightHint = (int)(this.height/2);
//		down_left.setLayoutData(gd_text_2);
//		
//		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
//		gd_text_1.widthHint = (int)((this.width-4)/2);
//		gd_text_1.heightHint = (int)(this.height/2);
//		down_right.setLayoutData(gd_text_1);

	}
	private void setColor(){
		up.setBackground(color);
//		down_left.setBackground(color);
//		down_right.setBackground(color);
	}
	
	public void setValue(String u, String dl, String dr){
		this.up.setText(u);
//		this.down_left.setText(dl);
//		this.down_right.setText(dr);
	}
	@Override
	protected Control createControl(Composite parent) {
		history = new Composite(parent, SWT.NONE);	
		GridLayout gd = new GridLayout(2, false);
		gd.horizontalSpacing = 0;
		gd.verticalSpacing = 0;
		gd.marginWidth = 0;
		gd.marginHeight = 0;
		history.setLayout(gd);		
		history.setBackground(color);
		history.setSize(width, height);

		up = new Text(history, SWT.NONE);
//		down_left = new Text(history, SWT.NONE);
//		down_right = new Text(history, SWT.RIGHT | SWT.NONE);
		up.setText("1");;
//		down_left.setText("2");
//		down_right.setText("3");
		
		
		setSize();
		setColor();
		
		up.setEnabled(false);
//		down_left.setEnabled(false);
//		down_right.setEnabled(false);	
		history.addListener(SWT.MouseDoubleClick, new Listener(){

			@Override
			public void handleEvent(Event event) {
				MessageBox messageBox =   
						   new MessageBox(new Shell(),   					     
						    SWT.ICON_WARNING);   
				messageBox.setMessage("显示进货信息");   
				messageBox.open(); 
				
			}
			
		});
		history.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	history.setVisible(true);
            	ItemCompositeEditor.this.focusLost();
            }
        });
        
		history.setVisible(true);
		history.layout();
		return history;
	}
	@Override
	protected Object doGetValue() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void doSetFocus() {
		 if (history != null) {
			 history.setFocus();
			 history.setVisible(true);
	        }
		
	}
	@Override
	protected void doSetValue(Object value) {
		// TODO Auto-generated method stub
		
	}
}
