package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;

public class DrugEditInput extends DrugInput {

    //private static Logger logger = LoggerFactory.getLogger(DrugEditInput.class);
    private int drugId;
    private int visitId;
    private int prescribed;
    private CheckBox allFixedCheck = new CheckBox("用量・用法・日数をそのままに");

    public DrugEditInput() {
        addRow(allFixedCheck);
    }

    public int getDrugId() {
        return drugId;
    }

    @Override
    public void setDrug(DrugFullDTO drugFull) {
        this.drugId = drugFull.drug.drugId;
        this.visitId = drugFull.drug.visitId;
        this.prescribed = drugFull.drug.prescribed;
        if( allFixedCheck.isSelected() ){
            setMaster(drugFull.master);
        } else {
            super.setDrug(drugFull);
        }
    }

    @Override
    public void setExample(PrescExampleFullDTO exampleFull) {
        if( allFixedCheck.isSelected() ){
            setMaster(exampleFull.master);
        } else {
            super.setExample(exampleFull);
        }
    }

    public DrugDTO createDrug(){
        return createDrug(drugId, visitId, prescribed);
    }

}
