package jp.chang.myclinic.practice.javafx.drug;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.events.DrugEnteredEvent;
import jp.chang.myclinic.practice.lib.PracticeLib;

public class DrugEnterForm extends DrugForm {

    public DrugEnterForm(VisitDTO visit) {
        super(visit);
    }

    @Override
    protected DrugInput createDrugInput(){
        return new DrugInput(){
            @Override
            protected void setupDaysInputArea(ObservableList<Node> children) {
                super.setupDaysInputArea(children);
                CheckBox daysFixedCheck = new CheckBox("固定");
                daysFixedCheck.setSelected(true);
                getInputConstraints().daysFixedProperty().bindBidirectional(daysFixedCheck.selectedProperty());
                children.add(daysFixedCheck);
            }
        };
    }

    protected void onEntered(DrugFullDTO newDrug){
        fireEvent(new DrugEnteredEvent(newDrug));
    }

    @Override
    protected void onEnter(DrugDTO drug){
        PracticeLib.enterDrug(drug, this::onEntered);
    }

    @Override
    protected void onClose(DrugForm self){

    }
}
