package jp.chang.myclinic.practice.javafx.drug;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;

public class DrugEnterForm extends DrugForm {

    public interface Callback {
        void onEntered(DrugFullDTO enteredDrug);
        void onClose();
    }

    private Callback callback = new Callback() {
        @Override
        public void onEntered(DrugFullDTO enteredDrug) { }
        @Override
        public void onClose() { }
    };

    public DrugEnterForm(int patientId, int visitId, String at) {
        super(patientId, visitId, at);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
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

    @Override
    protected void onEnter(DrugDTO drug){
        PracticeLib.enterDrug(drug, callback::onEntered);
    }

    @Override
    protected void onClose(){
        callback.onClose();
    }
}
