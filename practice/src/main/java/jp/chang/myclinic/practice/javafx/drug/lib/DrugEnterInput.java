package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.CheckBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.util.validator.dto.DrugValidator;

import static jp.chang.myclinic.consts.DrugCategory.Naifuku;

public class DrugEnterInput extends DrugInputBase {

    private CheckBox daysFixedCheck = new CheckBox("固定");
    private String naifukuDaysBackup = "";

    public DrugEnterInput() {
        super();
        daysFixedCheck.setSelected(true);
        addToDaysRow(daysFixedCheck);
    }

    boolean isFixedDays() {
        return daysFixedCheck.isSelected();
    }

    @Override
    void categoryChangeHook(DrugCategory prevCategory, DrugCategory newCategory) {
        if (prevCategory == Naifuku) {
            naifukuDaysBackup = getDays();
        }
        setDays(newCategory == Naifuku ? naifukuDaysBackup : "");
    }

    @Override
    void clearDays(){
        if( getCategory() == Naifuku && isFixedDays() ){
            // nop
        } else {
            super.clearDays();
        }
    }

    @Override
    void setDrugData(String amount,
                     String usage,
                     String days){
        setAmount(amount);
        setUsage(usage);
        if (getCategory() == Naifuku && isFixedDays() && !getDays().isEmpty()) {
            // nop
        } else {
            setDays(days);
        }
    }

    public Validated<DrugDTO> getDrug(int visitId){
        DrugValidator validator = new DrugValidator();
        validator.setValidatedDrugId(Validated.success(0));
        validator.validateVisitId(visitId);
        validator.validateIyakuhincode(getIyakuhincode());
        validator.validateAmount(getAmount());
        validator.validateUsage(getUsage());
        validator.validateCategory(getCategory().getCode());
        validator.validateDays(getDays());
        validator.validatePrescribed(0);
        return validator.validate();
    }

}
