package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.events.DrugEnteredEvent;
import jp.chang.myclinic.practice.lib.PracticeService;
import jp.chang.myclinic.practice.lib.drug.DrugInputConstraints;

public class DrugEnterForm extends DrugForm implements DrugInputConstraints {

    private CheckBox daysFixedCheck;

    public DrugEnterForm(VisitDTO visit) {
        super(visit);
    }

    @Override
    protected DrugInput createDrugInput(){
        daysFixedCheck = new CheckBox("固定");
        daysFixedCheck.setSelected(true);
        return new DrugInput(){
            @Override
            protected void setupDaysInputArea(ObservableList<Node> children) {
                super.setupDaysInputArea(children);
                children.add(daysFixedCheck);
            }
        };
    }

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

    protected void onEntered(DrugFullDTO newDrug){
        fireEvent(new DrugEnteredEvent(newDrug));
    }

    @Override
    protected void onEnter(DrugForm form){
        convertToDrug((drug, errors) -> {
            PracticeService.enterDrug(drug)
                    .thenCompose(PracticeService::getDrugFull)
                    .thenAccept(drugFull -> Platform.runLater(() -> onEntered(drugFull)));
        });
    }

    @Override
    protected void onClose(DrugForm self){

    }
}
