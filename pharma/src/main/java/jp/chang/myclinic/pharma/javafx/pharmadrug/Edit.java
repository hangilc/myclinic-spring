package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

class Edit extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Edit.class);
    private int iyakuhincode;
    private Text infoText = new Text("");
    private TextArea descTextArea = new TextArea("");
    private TextArea sideTextArea = new TextArea("");
    private Button editButton = new Button("編集");
    private Button enterButton = new Button("入力");
    private Button cancelButton = new Button("キャンセル");

    Edit() {
        super(4);
        descTextArea.setWrapText(true);
        descTextArea.setEditable(false);
        descTextArea.getStyleClass().add("drug-description-text-area");
        sideTextArea.setWrapText(true);
        sideTextArea.setEditable(false);
        sideTextArea.getStyleClass().add("side-effect-text-area");
        getChildren().addAll(
                new TextFlow(infoText),
                new Label("説明"),
                descTextArea,
                new Label("副作用"),
                sideTextArea,
                createCommands()
        );
    }

    void setPharmaDrug(PharmaDrugNameDTO pharmaDrugName){
        Service.api.getPharmaDrug(pharmaDrugName.iyakuhincode)
                .thenAccept(pharmaDrug -> Platform.runLater(() ->{
                    Edit.this.iyakuhincode = pharmaDrug.iyakuhincode;
                    infoText.setText(pharmaDrugName.name);
                    descTextArea.setText(pharmaDrug.description);
                    sideTextArea.setText(pharmaDrug.sideeffect);
                    viewMode();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void viewMode(){
        descTextArea.setEditable(false);
        sideTextArea.setEditable(false);
        editButton.setDisable(false);
        enterButton.setDisable(true);
        cancelButton.setDisable(true);
    }

    private void editMode(){
        descTextArea.setEditable(true);
        sideTextArea.setEditable(true);
        editButton.setDisable(true);
        enterButton.setDisable(false);
        cancelButton.setDisable(false);
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        editButton.setOnAction(evt -> editMode());
        enterButton.setDisable(true);
        enterButton.setOnAction(evt -> doEnter(this::viewMode));
        cancelButton.setDisable(true);
        cancelButton.setOnAction(evt -> viewMode());
        hbox.getChildren().addAll(
                editButton,
                enterButton,
                cancelButton
        );
        return hbox;
    }

    private void doEnter(Runnable cb){
        if( iyakuhincode > 0 ){
            PharmaDrugDTO data = new PharmaDrugDTO();
            data.iyakuhincode = iyakuhincode;
            data.description = descTextArea.getText();
            data.sideeffect = sideTextArea.getText();
            Service.api.updatePharmaDrug(data)
                    .thenAccept(result -> {
                        Platform.runLater(cb);
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
