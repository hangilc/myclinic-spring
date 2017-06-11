package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PharmaQueueFullDTO;

import javax.swing.*;

/**
 * Created by hangil on 2017/06/11.
 */
public class PharmaQueueList extends JList<PharmaQueueFullDTO> {

    public PharmaQueueList(){
        super();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCellRenderer(new PharmaQueueListListCellRenderer());
    }

    public void setSelectedVisitId(int visitId){
        if( visitId == 0 ){
            clearSelection();
        } else {
            ListModel<PharmaQueueFullDTO> model = getModel();
            for(int i=0;i<model.getSize();i++){
                PharmaQueueFullDTO data = model.getElementAt(i);
                if( data.pharmaQueue.visitId == visitId ){
                    setSelectedIndex(i);
                    break;
                }
            }
        }
    }

}
