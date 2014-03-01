package utils;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import softwarekeyboard.ShowKeyBoardAdapter;

public class BindText2NumberKeyBoard {

	//bind the Text list with the keyboard
	public static void bindTextKeyboard(ArrayList<Text> texts, Shell shell){
		for(int i=0;i<texts.size();i++){
			texts.get(i).addMouseListener(new ShowKeyBoardAdapter(texts.get(i), shell));
		}
	}
}
