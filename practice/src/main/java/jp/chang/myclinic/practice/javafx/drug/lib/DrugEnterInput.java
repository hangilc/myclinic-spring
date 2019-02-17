package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;

public class DrugEnterInput extends DrugInput {

    //private static Logger logger = LoggerFactory.getLogger(DrugEnterInput.class);
    private CheckBox daysFixedCheck = new CheckBox("固定");

    public DrugEnterInput() {
        daysFixedCheck.setSelected(true);
        addToDaysRow(daysFixedCheck);
    }

    public void setStateFrom(DrugEnterInputState state){
        super.setStateFrom(state);
        daysFixedCheck.setSelected(state.isDaysFixed());
    }

    public void getStateTo(DrugEnterInputState state){
        super.getStateTo(state);
        state.setDaysFixed(daysFixedCheck.isSelected());
    }

    private boolean isDaysFixed(){
        return daysFixedCheck.isSelected();
    }

    @Override
    public void setDrug(DrugFullDTO drugFull) {
        setDrug(drugFull, isDaysFixed());
    }

    @Override
    public void setExample(PrescExampleFullDTO exampleFull) {
        setExample(exampleFull, isDaysFixed());
    }

    public void clear(){
        clearMaster();
        clearAmount();
        clearUsage();
        if( !isDaysFixed() ){
            clearDays();
        }
        clearComment();
    }

}
