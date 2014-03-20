package com.storeworld.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/** 
 * wheels from the Internet
 * make the ComboCellEditor of Jface editable
 * */
public class GeneralComboCellEditor<T extends Object> extends CellEditor {
    private static final int STYLE_DEFAULT = SWT.NONE;
    /** The combo control that is used internally. */
    protected CCombo comboBox;
    public static final int DROP_DOWN_ON_MOUSE_ACTIVATION = 1;

	/**
	 * The list is dropped down when the activation is done through the keyboard
	 */
	public static final int DROP_DOWN_ON_KEY_ACTIVATION = 1 << 1;

	/**
	 * The list is dropped down when the activation is done without
	 * ui-interaction
	 */
	public static final int DROP_DOWN_ON_PROGRAMMATIC_ACTIVATION = 1 << 2;

	/**
	 * The list is dropped down when the activation is done by traversing from
	 * cell to cell
	 */
	public static final int DROP_DOWN_ON_TRAVERSE_ACTIVATION = 1 << 3;

	private int activationStyle = SWT.NONE;
    //ComboBoxCellEditor already has an attribute "items", so use "objects"
    /** The list of objects stored whose labels are use in the dropdown list. */
    protected List<T> objects;
    /** This object determines how to read out the label String from the objects. */
    protected ILabelProvider labelProvider;

    /** Creates a new cell editor that shows the specified items in its list.
     * The default LabelProvider (which uses the <code>toString()</code> method)
     * is used to find the label of each item.
     * @param parent the parent control
     * @param items the objects to select from
     * @param editable if <code>true</code>, the user can type any value into
     * the combo. In case of such a custom value a <code>String</code> is returned.
     */
    public GeneralComboCellEditor(Composite parent, final List<T> items, boolean editable) {
        this(parent, items, editable, new GeneralComboLabelProvider());
    }

    /** Creates a new cell editor that shows the specified items in its list.
     * A custom LabelProvider can be set. 
     * @param parent the parent control
     * @param items the objects to select from
     * @param editable if <code>true</code>, the user can type any value into
     * the combo. In case of such a custom value a <code>String</code> is returned.
     * @param labelProvider determines how to read out the label String from the objects.
     */
    public GeneralComboCellEditor(Composite parent, final List<T> items,
            boolean editable, ILabelProvider labelProvider) {
        this(parent, items, labelProvider, (editable) ? STYLE_DEFAULT : STYLE_DEFAULT
                | SWT.READ_ONLY);
    }

    /** Creates a new cell editor that shows the specified items in its list.
     * A custom LabelProvider and style can be set. 
     * @param parent the parent control
     * @param items the objects to select from
     * @param labelProvider determines how to read out the label String from the objects.
     * @param style the style bits, such as <code>SWT.READ_ONLY</code>
     */
    public GeneralComboCellEditor(Composite parent, final List<T> items,
            ILabelProvider labelProvider, int style) {
        super(parent, style);
        this.objects = items;
        this.labelProvider = labelProvider;

        if (this.comboBox != null) {
            comboBox.setItems(getStrings(items, labelProvider));
        }
    }

    /*public void setItems(T[] items) {
     super.setItems(getStrings(items, this.labelProvider));
     }*/

    /** We need an array of strings. Currently we assume the items to be sorted. 
     * @param items the items whose labels are to be determined
     * @param labelProvider the label provider that knows how to get the label
     * of this sort of objects
     * @return the items's labels in an array of Strings */
    protected String[] getStrings(List<T> items, ILabelProvider labelProvider) {
        if (items == null)
            throw new NullPointerException("item is null"); //$NON-NLS-1$ //"Items may not be null"
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < items.size(); ++i) {
            String label = labelProvider.getText(items.get(i));
            list.add(i, label);
        }
        //Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

//    protected void setActivationStyle(boolean){
////    	comboBox.set
//    }
    /** Returns the object at the selected index. */
    protected Object doGetValue() {
        if (comboBox == null) return null;
        int i = comboBox.getSelectionIndex();
        if (i > -1) return objects.get(i);
        else return comboBox.getText();
    }

    protected void doSetValue(Object value) {
        if (value == null || comboBox == null) return;
        String valueLabel = labelProvider.getText(value);

        for (int i = 0; i < this.objects.size(); ++i) {
            if (labelProvider.getText(objects.get(i)).equals(valueLabel)) {
                comboBox.select(i);
            }
        }
        comboBox.setText(valueLabel);
    }

    /**
     * active the combo, comes from CellEditor
     */
    public void activate(ColumnViewerEditorActivationEvent activationEvent) {
		super.activate(activationEvent);
		if (activationStyle != SWT.NONE) {
			boolean dropDown = false;
			if ((activationEvent.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION || activationEvent.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION)
					&& (activationStyle & DROP_DOWN_ON_MOUSE_ACTIVATION) != 0 ) {
				dropDown = true;
			} else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED
					&& (activationStyle & DROP_DOWN_ON_KEY_ACTIVATION) != 0 ) {
				dropDown = true;
			} else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
					&& (activationStyle & DROP_DOWN_ON_PROGRAMMATIC_ACTIVATION) != 0) {
				dropDown = true;
			} else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
					&& (activationStyle & DROP_DOWN_ON_TRAVERSE_ACTIVATION) != 0) {
				dropDown = true;
			}

			if (dropDown) {
				getControl().getDisplay().asyncExec(new Runnable() {

					public void run() {
//						((CCombo) getControl()).setListVisible(true);
						comboBox.setListVisible(true);
					}

				});

			}
		}
	}

	/**
	 * This method allows to control how the combo reacts when activated
	 *
	 * @param activationStyle
	 *            the style used
	 */
	public void setActivationStyle(int activationStyle) {
		this.activationStyle = activationStyle;
	}
	
//  Focus and such--------------------------------------------------------------
    protected void doSetFocus() {
        comboBox.setFocus();
    }

    protected void focusLost() {
        if (isActivated()) {
            //fireApplyEditorValue();
            deactivate();
        }
    }

    protected void keyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\u001b') { // Escape character
            fireCancelEditor();
        } else if (keyEvent.character == '\t') { // tab key
            //fireApplyEditorValue();
            deactivate();
        }
    }

    /**
     * Create control
     */
    protected Control createControl(Composite parent) {
        comboBox = new CCombo(parent, getStyle());
        comboBox.setVisibleItemCount(10);
        comboBox.setFont(parent.getFont());
        
        comboBox.addKeyListener(new KeyAdapter() {
            // hook key pressed - see PR 14201  
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });

        comboBox.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                fireApplyEditorValue();
                deactivate();
                //selection = comboBox.getSelectionIndex();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                //If the user entered a value, check if it is one of the values
                //in the list. If so, select this value.
                if (comboBox.getSelectionIndex() == -1) {
                    String[] items = comboBox.getItems();
                    for (int i = 0; i < items.length; ++i) {
                        if (items[i].equals(comboBox.getText())) {
                            comboBox.select(i);
                            break;
                        }
                    }
                }
                fireApplyEditorValue();
                deactivate();
            }

        });

        comboBox.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });

        comboBox.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                GeneralComboCellEditor.this.focusLost();
            }
        });
        return comboBox;
    }

    public LayoutData getLayoutData() {
        LayoutData layoutData = super.getLayoutData();
        if ((comboBox == null) || comboBox.isDisposed()) layoutData.minimumWidth = 60;
        else {
            // make the comboBox 10 characters wide
            GC gc = new GC(comboBox);
            layoutData.minimumWidth = (gc.getFontMetrics().getAverageCharWidth() * 10) + 10;
            gc.dispose();
        }
        return layoutData;
    }
}