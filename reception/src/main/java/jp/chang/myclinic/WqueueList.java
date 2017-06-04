package jp.chang.myclinic;

import javax.swing.*;

class WqueueList extends JList<WqueueData> {

	public WqueueList(){
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new WqueueListCellRenderer());
	}

	public void setSelectedVisitId(int visitId){
		if( visitId == 0 ){
			clearSelection();
		} else {
			ListModel<WqueueData> model = getModel();
			for(int i=0;i<model.getSize();i++){
				WqueueData data = model.getElementAt(i);
				if( data.getVisitId() == visitId ){
					setSelectedIndex(i);
					break;
				}
			}
		}
	}

}