package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.PracticeUtil;
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
        Hyperlink deleteLink = new Hyperlink("削除");
        deleteLink.setOnAction(event -> {
            if( PracticeUtil.confirmCurrentVisitAction(visit.visitId, "この処方を削除していいですか？") ){
                PracticeLib.deleteDrug(drugFull.drug, this::onDeleted);
            }
        });
        addCommand(deleteLink);
    }

    protected void onDeleted(){

    }

}
