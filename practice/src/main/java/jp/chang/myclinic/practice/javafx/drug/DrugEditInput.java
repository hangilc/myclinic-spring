package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugFullDTO;

class DrugEditInput extends DrugInput {

    //private static Logger logger = LoggerFactory.getLogger(DrugEditInput.class);
    private CheckBox allFixedCheck = new CheckBox("用量・用法・日数をそのままに");

    DrugEditInput() {
        addRow(allFixedCheck);
    }

    @Override
    void setDrug(DrugFullDTO drugFull) {
        if( allFixedCheck.isSelected() ){
            setMaster(drugFull.master);
        } else {
            super.setDrug(drugFull);
        }
    }
}
