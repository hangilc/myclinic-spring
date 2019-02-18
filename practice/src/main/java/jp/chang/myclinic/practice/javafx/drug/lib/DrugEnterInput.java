package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;

public class DrugEnterInput extends DrugInput {

    private CheckBox daysFixedCheck = new CheckBox("固定");
    private String daysBackup = "";

    public DrugEnterInput() {
        daysFixedCheck.setSelected(true);
        addToDaysRow(daysFixedCheck);
        daysFixedCheck.selectedProperty().addListener((obs, oldValue, newValue) -> {
            DrugEnterInputState state = new DrugEnterInputState();
            getStateTo(state);
            state.adaptToDaysFixedChange();
            setStateFrom(state);
        });
        categoryProperty().addListener((obs, oldValue, newValue) -> {
            DrugEnterInputState state = new DrugEnterInputState();
            getStateTo(state);
            state.adaptToCategoryChange();
            setStateFrom(state);
        });
    }

    void setStateFrom(DrugEnterInputState state){
        super.setStateFrom(state);
        daysFixedCheck.setSelected(state.isDaysFixed());
        this.daysBackup = state.getDaysBackup();
        daysFixedCheck.setDisable(state.isDaysFixedDisabled());
    }

    void getStateTo(DrugEnterInputState state){
        super.getStateTo(state);
        state.setDaysFixed(daysFixedCheck.isSelected());
        state.setDaysFixedDisabled(daysFixedCheck.isDisabled());
        state.setDaysBackup(daysBackup);
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
