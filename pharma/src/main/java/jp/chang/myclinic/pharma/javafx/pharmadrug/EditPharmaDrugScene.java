package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EditPharmaDrugScene extends VBox {

    private static Logger logger = LoggerFactory.getLogger(EditPharmaDrugScene.class);
    private TextField inputField = new TextField();
    private Text drugInfoText = new Text("");
    private TextArea descriptionTextArea = new TextArea();
    private TextArea sideEffectTextArea = new TextArea();
    private int iyakuhincode;

    EditPharmaDrugScene() {
        super(4);
        getStylesheets().add("Pharma.css");
        getStyleClass().add("edit-pharma-drug-dialog");
        descriptionTextArea.getStyleClass().add("drug-description-text-area");
        descriptionTextArea.setWrapText(true);
        sideEffectTextArea.getStyleClass().add("side-effect-text-area");
        sideEffectTextArea.setWrapText(true);
        Button enterButton = new Button("入力");
        enterButton.setOnAction(evt -> doEnter());
        getChildren().addAll(
                createSearchTextInput(),
                createDrugInfoPane(),
                new Label("説明"),
                descriptionTextArea,
                new Label("副作用"),
                sideEffectTextArea,
                new HBox(4, enterButton)
        );
    }

    private Node createSearchTextInput(){
        HBox hbox = new HBox(4);
        inputField.getStyleClass().add("search-text-input");
        Button searchButton = new Button("検索");
        inputField.setOnAction(evt ->  doSearch(inputField.getText().trim()));
        searchButton.setOnAction(evt -> doSearch(inputField.getText().trim()));
        hbox.getChildren().addAll(inputField, searchButton);
        return hbox;
    }

    private Node createDrugInfoPane(){
        return new TextFlow(drugInfoText);
    }

    private void doSearch(String text){
        Service.api.searchPharmaDrugNames(text)
                .thenAccept(result -> Platform.runLater(() -> {
                    PharmaDrugListDialog listDialog = new PharmaDrugListDialog(result){
                        @Override
                        protected void onSelect(PharmaDrugNameDTO item, PharmaDrugDTO pharmaDrug) {
                            drugInfoText.setText(item.name);
                            iyakuhincode = item.iyakuhincode;
                            descriptionTextArea.setText(pharmaDrug.description);
                            sideEffectTextArea.setText(pharmaDrug.sideeffect);
                            close();
                        }

                        @Override
                        protected void onCancel() {
                            close();
                        }
                    };
                    listDialog.show();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doEnter(){
        if( iyakuhincode > 0 ){
            PharmaDrugDTO data = new PharmaDrugDTO();
            data.iyakuhincode = iyakuhincode;
            data.description = descriptionTextArea.getText();
            data.sideeffect = sideEffectTextArea.getText();
            Service.api.updatePharmaDrug(data)
                    .thenAccept(result -> Platform.runLater(() -> {
                        onEnter();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    protected void onEnter(){

    }
}
