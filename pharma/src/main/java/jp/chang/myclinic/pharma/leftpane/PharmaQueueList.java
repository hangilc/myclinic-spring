package jp.chang.myclinic.pharma.leftpane;

import jp.chang.myclinic.dto.PharmaQueueFullDTO;

import javax.swing.*;

class PharmaQueueList extends JList<PharmaQueueFullDTO> {

    enum LoadMode { PharmaQueueOnly, TodaysVisits };

    PharmaQueueList(Icon waitCashierImage, Icon waitDrugImage){
        super();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCellRenderer(new PharmaQueueListListCellRenderer(waitCashierImage, waitDrugImage));
    }

    @Override
    public void setListData(PharmaQueueFullDTO[] listData) {
        PharmaQueueFullDTO selected = getSelectedValue();
        super.setListData(listData);
        if( selected != null ){
            setSelectedVisitId(selected.visitId);
        }
    }

    private void setSelectedVisitId(int visitId){
        if( visitId == 0 ){
            clearSelection();
        } else {
            ListModel<PharmaQueueFullDTO> model = getModel();
            for(int i=0;i<model.getSize();i++){
                PharmaQueueFullDTO data = model.getElementAt(i);
                if( data.visitId == visitId ){
                    setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void reload(LoadMode lodeMode){

    }

}
