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
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

class NewPharmaDrugRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(NewPharmaDrugRoot.class);
    private TextField inputField = new TextField();
    private Text drugInfoText = new Text("");
    private TextArea descriptionTextArea = new TextArea();
    private TextArea sideEffectTextArea = new TextArea();
    private int iyakuhincode;

    NewPharmaDrugRoot() {
        super(4);
        getStylesheets().add("Pharma.css");
        getStyleClass().add("new-pharma-drug-dialog");
        descriptionTextArea.getStyleClass().add("drug-description-text-area");
        descriptionTextArea.setWrapText(true);
        sideEffectTextArea.getStyleClass().add("side-effect-text-area");
        sideEffectTextArea.setWrapText(true);
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        enterButton.setOnAction(evt -> doEnter());
        closeButton.setOnAction(evt -> onClose());
        getChildren().addAll(
                createSearchTextInput(),
                createDrugInfoPane(),
                new Label("説明"),
                descriptionTextArea,
                new Label("副作用"),
                sideEffectTextArea,
                new HBox(4, enterButton, closeButton)
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
        if( text.isEmpty() ){
            return;
        }
        Service.api.searchIyakuhinMasterByName(text, LocalDate.now().toString())
                .thenAccept(result -> Platform.runLater(() -> {
                    IyakuhinListDialog listDialog = new IyakuhinListDialog(result){
                        @Override
                        protected void onSelect(IyakuhinMasterDTO master) {
                            drugInfoText.setText(master.name);
                            iyakuhincode = master.iyakuhincode;
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
            PharmaDrugDTO pd = new PharmaDrugDTO();
            pd.iyakuhincode = iyakuhincode;
            pd.description = descriptionTextArea.getText();
            pd.sideeffect = sideEffectTextArea.getText();
            Service.api.enterPharmaDrug(pd)
                    .thenAccept(result -> {
                        clear();
                        onEnter(pd.iyakuhincode);
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    void clear(){
        drugInfoText.setText("");
        descriptionTextArea.setText("");
        sideEffectTextArea.setText("");
    }

    protected void onEnter(int iyakuhincode){

    }

    protected void onClose(){

    }
}
