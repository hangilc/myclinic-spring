package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class WqueueListCellRenderer extends JLabel implements ListCellRenderer<WqueueData> {

	WqueueListCellRenderer(){
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends WqueueData> list, WqueueData value, int index,
		boolean isSelected, boolean cellHasFocus){
		setText(value.getLabel());
		switch(value.getState()){
			case WaitExam: setForeground(Color.RED); break;
			default: setForeground(list.getForeground()); break;
		}
		if( isSelected ){
			setBackground(list.getSelectionBackground());
		} else {
			setBackground(list.getBackground());
		}
		return this;
	}
}