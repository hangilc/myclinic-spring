package jp.chang.myclinic.practice.javafx.drug2;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.utilfx.HandlerFX;

class ConvertToPrescExampleDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(ConvertToPrescExampleDialog.class);
    private Input input = new Input();
    private TextField commentInput = new TextField();

    ConvertToPrescExampleDialog(DrugFullDTO drugFull) {
        setTitle("処方例に追加");
        Parent mainPane = createMainPane(drugFull);
        mainPane.getStyleClass().add("presc-example-dialog");
        mainPane.getStylesheets().add("css/Practice.css");
        setScene(new Scene(mainPane));
    }

    private Parent createMainPane(DrugFullDTO drug){
        VBox vbox = new VBox(4);
        DrugData data = DrugData.fromDrug(drug);
        input.setData(data);
        input.addRow(new Label("注釈："), commentInput);
        vbox.getChildren().addAll(input, new Separator(), createCommands());
        return vbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> close());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private void doEnter(){
        PrescExampleDTO example = input.createPrescExample(commentInput.getText());
        if( example == null ){
            return;
        }
        Service.api.enterPrescExample(example)
                .thenAcceptAsync(result -> {
                    close();
                }, Platform::runLater)
                .exceptionally(HandlerFX::exceptionally);
    }
}
