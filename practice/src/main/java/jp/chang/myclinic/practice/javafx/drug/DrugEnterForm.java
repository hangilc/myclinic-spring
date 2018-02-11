package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.events.DrugEnteredEvent;
import jp.chang.myclinic.practice.lib.GuiUtil;
import jp.chang.myclinic.practice.lib.PracticeService;
import jp.chang.myclinic.practice.lib.drug.DrugFormHelper;
import jp.chang.myclinic.practice.lib.drug.DrugInputConstraints;

class DrugEnterForm extends DrugForm {

    private int visitId;
    private CheckBox daysFixedCheck = new CheckBox("固定");

    DrugEnterForm(VisitDTO visit) {
        super(visit);
        this.visitId = visit.visitId;
        daysFixedCheck.setSelected(true);
        addToDaysRow(daysFixedCheck);
        setConstraints(new DrugInputConstraints() {
            @Override
            public boolean isAmountFixed() {
                return false;
            }

            @Override
            public boolean isUsageFixed() {
                return false;
            }

            @Override
            public boolean isDaysFixed() {
                return daysFixedCheck.isSelected();
            }
        });
    }

    private void onEntered(DrugFullDTO newDrug){
        fireEvent(new DrugEnteredEvent(newDrug));
    }

    @Override
    protected void onEnter(DrugForm form){
        DrugFormHelper.convertToDrug(getDrugFormGetter(), 0, visitId, 0, (drug, errors) ->{
            if( errors.size() > 0 ){
                GuiUtil.alertError(String.join("\n", errors));
            } else {
                PracticeService.enterDrug(drug)
                        .thenCompose(PracticeService::getDrugFull)
                        .thenAccept(drugFull -> Platform.runLater(() -> onEntered(drugFull)));
            }
        });
    }

    @Override
    protected void onClose(DrugForm self){

    }
}
