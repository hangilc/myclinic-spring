package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.lib.drug.DrugFormHelper;
import jp.chang.myclinic.practice.lib.drug.DrugInputConstraints;

public class DrugEditForm extends DrugForm {

    private CheckBox allFixedCheck = new CheckBox("用量・用法・日数をそのままに");

    protected DrugEditForm(VisitDTO visit, DrugFullDTO drugFull) {
        super(visit);
        DrugFormHelper.setDrug(getDrugFormSetter(), drugFull, getDrugFormGetter(), getConstraints());
        setConstraints(new DrugInputConstraints() {
            @Override
            public boolean isAmountFixed() {
                return allFixedCheck.isSelected();
            }

            @Override
            public boolean isUsageFixed() {
                return allFixedCheck.isSelected();
            }

            @Override
            public boolean isDaysFixed() {
                return allFixedCheck.isSelected();
            }
        });
        addDrugInputRow(allFixedCheck);
    }

}
