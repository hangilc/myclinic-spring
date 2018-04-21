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
import jp.chang.myclinic.pharma.javafx.lib.GuiUtil;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

class NewPharmaDrugRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(NewPharmaDrugRoot.class);
    private TextField inputField = new TextField();
    private Text drugInfoText = new Text("");
    private TextArea descriptionTextArea = new TextArea();
    private TextArea sideEffectTextArea = new TextArea();
    private int iyakuhincode;
    private Button enterButton = new Button("入力");

    NewPharmaDrugRoot() {
        super(4);
        getStylesheets().add("Pharma.css");
        getStyleClass().add("new-pharma-drug-dialog");
        descriptionTextArea.getStyleClass().add("drug-description-text-area");
        descriptionTextArea.setWrapText(true);
        sideEffectTextArea.getStyleClass().add("side-effect-text-area");
        sideEffectTextArea.setWrapText(true);
        Button closeButton = new Button("閉じる");
        enterButton.setDisable(true);
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
                .thenAccept(result -> Platform.runLater(() -> openIyakuhinListDialog(result)))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void openIyakuhinListDialog(List<IyakuhinMasterDTO> masters){
        IyakuhinListDialog listDialog = new IyakuhinListDialog(masters){
            @Override
            protected void onSelect(IyakuhinMasterDTO master) {
                Service.api.findPharmaDrug(master.iyakuhincode)
                        .thenAccept(result -> Platform.runLater(() ->{
                            if( result != null ){
                                GuiUtil.alertError(master.name + "は既に登録されています。");
                            } else {
                                drugInfoText.setText(master.name);
                                iyakuhincode = master.iyakuhincode;
                                enterButton.setDisable(false);
                                close();
                            }
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }

            @Override
            protected void onCancel() {
                close();
            }
        };
        listDialog.show();
    }

    private void doEnter(){
        if( iyakuhincode > 0 ){
            PharmaDrugDTO pd = new PharmaDrugDTO();
            pd.iyakuhincode = iyakuhincode;
            pd.description = descriptionTextArea.getText();
            pd.sideeffect = sideEffectTextArea.getText();
            Service.api.enterPharmaDrug(pd)
                    .thenAccept(result -> Platform.runLater(() ->{
                        clear();
                        onEnter(pd.iyakuhincode);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    void clear(){
        iyakuhincode = 0;
        drugInfoText.setText("");
        descriptionTextArea.setText("");
        sideEffectTextArea.setText("");
        enterButton.setDisable(true);
    }

    protected void onEnter(int iyakuhincode){

    }

    protected void onClose(){

    }

    void setDescription(String description) {
        descriptionTextArea.setText(description);
    }

    void setSideEffect(String sideEffect){
        sideEffectTextArea.setText(sideEffect);
    }
}
