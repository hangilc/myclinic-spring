package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.lib.drug.*;

public class DrugForm extends VBox {

    private DrugInput drugInput = new DrugInput();
    private HBox commandBox;
    private DrugInputConstraints constraints = DrugInputConstraints.defaultDrugInputConstraints();
    private DrugSearch drugSearch;

    DrugForm(VisitDTO visit) {
        super(4);
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        getChildren().addAll(
                createTitle(),
                drugInput,
                createButtons(),
                createSearch(visit.patientId, visit.visitedAt)
        );
    }

    DrugInputConstraints getConstraints() {
        return constraints;
    }

    void setConstraints(DrugInputConstraints constraints) {
        this.constraints = constraints;
    }

    void addToDaysRow(Node... nodes) {
        drugInput.addToDaysRow(nodes);
    }

    void addDrugInputRow(Node node) {
        drugInput.addRow(node);
    }

    public DrugFormGetter getDrugFormGetter() {
        return drugInput;
    }

    DrugFormSetter getDrugFormSetter() {
        return drugInput;
    }

    void addCommand(Node node){
        commandBox.getChildren().add(node);
    }

    public void clear(){
        DrugFormHelper.clear(drugInput,getConstraints());
        drugSearch.clear();
    }

    private Node createTitle() {
        Label title = new Label("新規処方の入力");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    protected void onEnter(DrugForm self) {

    }

    protected void onClose(DrugForm self) {

    }

    private Node createButtons() {
        HBox hbox = new HBox(4);
        hbox.getStyleClass().add("commands");
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(event -> onEnter(this));
        closeButton.setOnAction(event -> onClose(this));
        clearLink.setOnAction(event -> DrugFormHelper.clear(drugInput, constraints));
        hbox.getChildren().addAll(enterButton, closeButton, clearLink);
        commandBox = hbox;
        return hbox;
    }

    private Node createSearch(int patientId, String at) {
        drugSearch = new DrugSearch(patientId, at);
//        drugSearch.setCallback(new DrugSearch.Callback() {
//            @Override
//            public void onSelect(DrugSearchResultModel searchResultModel) {
//                searchResultModel.stuffInto(drugInput, drugInput, constraints);
//            }
//        });
        return drugSearch;
    }

}
