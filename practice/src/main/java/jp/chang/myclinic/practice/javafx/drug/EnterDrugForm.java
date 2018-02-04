package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EnterDrugForm extends VBox {

    private int patientId;
    private DrugInputModel inputModel = new DrugInputModel();

    public EnterDrugForm(int patientId){
        super(4);
        this.patientId = patientId;
        getStyleClass().add("form");
        getChildren().addAll(
                createTitle(),
                createDisp(),
                createSearch()
        );
    }

    private Node createTitle(){
        Label title = new Label("新規処方の入力");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    private Node createDisp(){
        DrugInput drugInput = new DrugInput();
        DrugCommon.bindDrugInputAndModel(drugInput, inputModel);
        return drugInput;
    }

    private Node createSearch(){
        DrugSearch drugSearch = new DrugSearch(patientId);
        drugSearch.setCallback(new DrugSearch.Callback() {
            @Override
            public void onSelect(SearchResultModel searchResultModel) {
                searchResultModel.stuffInto(inputModel);
            }
        });
        return drugSearch;
    }

}
