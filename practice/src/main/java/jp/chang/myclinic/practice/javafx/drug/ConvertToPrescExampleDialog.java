package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.practice.javafx.drug.lib.PrescExampleInput;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.HandlerFX;

class ConvertToPrescExampleDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(ConvertToPrescExampleDialog.class);
    private PrescExampleInput input = new PrescExampleInput();

    ConvertToPrescExampleDialog(DrugFullDTO drugFull) {
        setTitle("処方例に追加");
        Parent mainPane = createMainPane(drugFull);
        mainPane.getStyleClass().add("presc-example-dialog");
        mainPane.getStylesheets().add("css/Practice.css");
        setScene(new Scene(mainPane));
    }

    private Parent createMainPane(DrugFullDTO drug){
        VBox vbox = new VBox(4);
        input.setDrug(drug);
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
        Validated<PrescExampleDTO> validated = input.getValidatedToEnter();
        if( validated.isFailure() ){
            AlertDialog.alert(validated.getErrorsAsString(), this);
            return;
        }
        PrescExampleDTO example = validated.getValue();
        Context.frontend.enterPrescExample(example)
                .thenAcceptAsync(result -> {
                    close();
                }, Platform::runLater)
                .exceptionally(HandlerFX.exceptionally(this));
    }
}
