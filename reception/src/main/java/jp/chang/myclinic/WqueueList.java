package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class WqueueList extends JList<WqueueData> {

	public WqueueList(){
		super();
		this.setCellRenderer(new WqueueListCellRenderer());
	}

	
}