package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.util.validator.dto.DrugValidator;

public class DrugEditInput extends DrugInputBase {

    private Label tekiyouLabel = new Label();
    private HBox tekiyouRow;
    private CheckBox allFixedCheck = new CheckBox("用量・用法・日数をそのままに");
    private int drugId;
    private int prescribed;

    public DrugEditInput(DrugFullDTO drug, DrugAttrDTO attr) {
        super();
        this.drugId = drug.drug.drugId;
        this.prescribed = drug.drug.prescribed;
        addRow(allFixedCheck);
        this.tekiyouRow = addRowBeforeCategory(new Label("摘要："), tekiyouLabel);
        setDrug(drug);
        setTekiyou((attr != null && attr.tekiyou != null) ? attr.tekiyou : "");
    }

    @Override
    void categoryChangeHook(DrugCategory prevCategory, DrugCategory newCategory) {
        // nop
    }

    @Override
    void setDrugData(String amount, String usage, String days) {
        if( allFixedCheck.isSelected() ){
            // nop
        } else {
            setAmount(amount);
            setUsage(usage);
            setDays(days);
        }
    }

    public void setTekiyou(String tekiyou){
        this.tekiyouLabel.setText(tekiyou);
        boolean visible = tekiyou != null && !tekiyou.isEmpty();
        tekiyouRow.setManaged(visible);
        tekiyouRow.setVisible(visible);
    }

    public int getDrugId(){
        return drugId;
    }

    public Validated<DrugDTO> getDrug(int visitId){
        DrugValidator validator = new DrugValidator();
        validator.validateDrugId(getDrugId());
        validator.validateVisitId(visitId);
        validator.validateIyakuhincode(getIyakuhincode());
        validator.validateAmount(getAmount());
        validator.validateUsage(getUsage());
        validator.validateCategory(getCategory().getCode());
        validator.validateDays(getDays());
        validator.validatePrescribed(prescribed);
        return validator.validate();
    }

}
