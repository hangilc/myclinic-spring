package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.GuiUtil;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

class Edit extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Edit.class);
    private int iyakuhincode;
    private Text infoText = new Text("");
    private TextArea descTextArea = new TextArea("");
    private TextArea sideTextArea = new TextArea("");
    private Button editButton = new Button("編集");
    private Button enterButton = new Button("入力");
    private Button cancelEditButton = new Button("キャンセル");
    private Button deleteButton = new Button("削除");
    private StackPane commandPane = new StackPane();
    private Node viewCommands;
    private Node editCommands;

    Edit() {
        super(4);
        descTextArea.setWrapText(true);
        descTextArea.setEditable(false);
        descTextArea.getStyleClass().add("drug-description-text-area");
        sideTextArea.setWrapText(true);
        sideTextArea.setEditable(false);
        sideTextArea.getStyleClass().add("side-effect-text-area");
        viewCommands = createViewCommands();
        editCommands = createEditCommands();
        commandPane.getChildren().setAll(viewCommands);
        editButton.setDisable(true);
        editButton.setOnAction(evt -> editMode());
        enterButton.setOnAction(evt -> doEnter(this::viewMode));
        cancelEditButton.setOnAction(evt -> viewMode());
        deleteButton.setOnAction(evt -> doDelete(iyakuhincode -> {
            clear();
            onDelete(iyakuhincode);
        }));
        getChildren().addAll(
                new TextFlow(infoText),
                new Label("説明"),
                descTextArea,
                new Label("副作用"),
                sideTextArea,
                commandPane
        );
    }

    void setPharmaDrug(PharmaDrugNameDTO pharmaDrugName){
        Service.api.getPharmaDrug(pharmaDrugName.iyakuhincode)
                .thenAccept(pharmaDrug -> Platform.runLater(() ->{
                    Edit.this.iyakuhincode = pharmaDrug.iyakuhincode;
                    infoText.setText(pharmaDrugName.name);
                    descTextArea.setText(pharmaDrug.description);
                    sideTextArea.setText(pharmaDrug.sideeffect);
                    editButton.setDisable(false);
                    viewMode();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    void onDelete(int iyakuhincode){

    }

    private void viewMode(){
        descTextArea.setEditable(false);
        sideTextArea.setEditable(false);
        commandPane.getChildren().setAll(viewCommands);
    }

    private void editMode(){
        descTextArea.setEditable(true);
        sideTextArea.setEditable(true);
        commandPane.getChildren().setAll(editCommands);
    }

    private void clear(){
        infoText.setText("");
        descTextArea.setText("");
        sideTextArea.setText("");
        editButton.setDisable(true);
        viewMode();
    }

    private Node createViewCommands(){
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(
                editButton
        );
        return hbox;
    }

    private Node createEditCommands(){
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(
                enterButton,
                cancelEditButton,
                deleteButton
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

    private void doDelete(Consumer<Integer> cb){
        if( iyakuhincode > 0 ){
            if( !GuiUtil.confirm("この薬剤情報を削除していいですか？") ){
                return;
            }
            Service.api.deletePharmaDrug(iyakuhincode)
                    .thenAccept(result -> Platform.runLater(() -> cb.accept(iyakuhincode)))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
