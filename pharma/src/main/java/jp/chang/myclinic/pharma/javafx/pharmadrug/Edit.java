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
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.function.Consumer;

class Edit extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(Edit.class);
    private static String noSelectText = "（薬剤選択なし）";
    private int iyakuhincode;
    private Text infoText = new Text(noSelectText);
    private TextArea descTextArea = new TextArea("");
    private TextArea sideTextArea = new TextArea("");
    private Button newButton = new Button("新規作成");
    private Button editButton = new Button("編集");
    private Button clearButton = new Button("クリア");
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
        newButton.setOnAction(evt -> doNew());
        editButton.setOnAction(evt -> editMode());
        clearButton.setOnAction(evt-> clear());
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
                .exceptionally(HandlerFX.exceptionally(this));
    }

    void onEnter(int iyakuhincode){

    }

    void onDelete(int iyakuhincode){

    }

    private Node createViewCommands(){
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(
                newButton,
                editButton,
                clearButton
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
        iyakuhincode = 0;
        infoText.setText(noSelectText);
        descTextArea.setText("");
        sideTextArea.setText("");
        editButton.setDisable(true);
        viewMode();
    }

    private void doNew(){
        NewPharmaDrugDialog dialog = new NewPharmaDrugDialog(){
            @Override
            protected void onEnter(int iyakuhincode) {
                Edit.this.onEnter(iyakuhincode);
                close();
            }
        };
        if( iyakuhincode > 0 ){
            dialog.setDescription(descTextArea.getText());
            dialog.setSideEffect(sideTextArea.getText());
        }
        dialog.show();
    }

    private void doEnter(Runnable cb){
        if( iyakuhincode > 0 ){
            PharmaDrugDTO data = new PharmaDrugDTO();
            data.iyakuhincode = iyakuhincode;
            data.description = descTextArea.getText();
            data.sideeffect = sideTextArea.getText();
            Service.api.updatePharmaDrug(data)
                    .thenAccept(result -> Platform.runLater(cb))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

    private void doDelete(Consumer<Integer> cb){
        if( iyakuhincode > 0 ){
            if( !GuiUtil.confirm("この薬剤情報を削除していいですか？") ){
                return;
            }
            Service.api.deletePharmaDrug(iyakuhincode)
                    .thenAccept(result -> Platform.runLater(() -> cb.accept(iyakuhincode)))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

}
