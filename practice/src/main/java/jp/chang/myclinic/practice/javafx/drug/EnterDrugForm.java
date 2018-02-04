package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.lib.GuiUtil;
import jp.chang.myclinic.practice.lib.PracticeLib;

public class EnterDrugForm extends VBox {

    public interface Callback {
        void onEnter(DrugFullDTO drug);
        void onClose();
    }

    private int patientId;
    private int visitId;
    private DrugInputModel inputModel = new DrugInputModel();
    private Callback callback;

    public EnterDrugForm(int patientId, int visitId){
        super(4);
        this.patientId = patientId;
        this.visitId = visitId;
        getStyleClass().add("drug-enter-form");
        getStyleClass().add("form");
        getChildren().addAll(
                createTitle(),
                createDisp(),
                createButtons(),
                createSearch()
        );
    }

    public void setCallback(Callback callback){
        this.callback = callback;
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
                        PracticeLib.enterDrug(drug, newDrug -> {
                            if( callback != null ){
                                callback.onEnter(newDrug);
                            }
                        });
                    }
                });
            });
            closeButton.setOnAction(event -> {
                if( callback != null ){
                    callback.onClose();
                }
            });
            clearLink.setOnAction(event -> inputModel.clear());
            hbox.getChildren().addAll(enterButton, closeButton, clearLink);
            vbox.getChildren().add(hbox);
        }
        return vbox;
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
