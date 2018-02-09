package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.practice.lib.DrugFormGetter;
import jp.chang.myclinic.practice.lib.GuiUtil;

public class DrugForm extends VBox {

    private int drugId;
    private int patientId;
    private int visitId;
    private String at;
    private DrugInput drugInput;
    private DrugInputModel inputModel = new DrugInputModel();
    private InputConstraints inputConstraints = new InputConstraints();

    public DrugForm(int patientId, int visitId, String at){
        super(4);
        this.patientId = patientId;
        this.visitId = visitId;
        this.at = at;
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        getChildren().addAll(
                createTitle(),
                createDisp(),
                createButtons(),
                createSearch()
        );
    }

    private Node createTitle(){
        Label title = new Label("新規処方の入力");
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    protected InputConstraints getInputConstraints() {
        return inputConstraints;
    }

    protected DrugInput createDrugInput(){
        return new DrugInput();
    }

    private Node createDisp(){
        this.drugInput = createDrugInput();
        DrugCommon.bindDrugInputAndModel(drugInput, inputModel);
        return drugInput;
    }

    protected void onEnter(DrugDTO drug){

    }

    protected void onClose(DrugForm self){

    }

    private Node createButtons(){
        VBox vbox = new VBox(4);
        vbox.getStyleClass().add("commands");
        {
            HBox hbox = new HBox(4);
            Button enterButton = new Button("入力");
            Button closeButton = new Button("閉じる");
            Hyperlink clearLink = new Hyperlink("クリア");
            enterButton.setOnAction(event -> {
                inputModel.convertToDrug(visitId, (drug, err) -> {
                    if( err != null ){
                        GuiUtil.alertError(String.join("\n", err));
                    } else {
                        onEnter(drug);
                    }
                });
            });
            closeButton.setOnAction(event -> onClose(this));
            clearLink.setOnAction(event -> DrugCommon.clearInputModel(inputModel, inputConstraints));
            hbox.getChildren().addAll(enterButton, closeButton, clearLink);
            vbox.getChildren().add(hbox);
        }
        return vbox;
    }

    private Node createSearch(){
        DrugSearch drugSearch = new DrugSearch(patientId, at);
        drugSearch.setCallback(new DrugSearch.Callback() {
            @Override
            public void onSelect(SearchResultModel searchResultModel) {
                searchResultModel.stuffInto(inputModel, inputConstraints);
            }
        });
        return drugSearch;
    }

    protected DrugFormGetter createDrugFormGetter(){
        return new DrugFormGetter() {
            @Override
            public int getDrugId() {
                return drugId;
            }

            @Override
            public int getVisitId() {
                return visitId;
            }

            @Override
            public int getIyakuhincode() {
                return drugInput.();
            }

            @Override
            public String getAmount() {
                return null;
            }

            @Override
            public String getUsage() {
                return null;
            }

            @Override
            public String getDays() {
                return null;
            }

            @Override
            public DrugCategory getCategory() {
                return null;
            }
        }
    }

}
