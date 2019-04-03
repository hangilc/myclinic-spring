package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.PatientDisp;
import jp.chang.myclinic.practice.javafx.parts.PatientSearchBox;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

class NewVisitDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(NewVisitDialog.class);
    private PatientDisp disp;

    NewVisitDialog() {
        VBox root = new VBox(4);
        root.getStyleClass().add("new-visit-dialog");
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        root.getChildren().addAll(
                createDisp(),
                createCommands(),
                createSearchBox()
        );
        setScene(new Scene(root));
    }

    private Node createDisp() {
        disp = new PatientDisp();
        return disp;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("診察受付");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> close());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private Node createSearchBox() {
        PatientSearchBox searchBox = new PatientSearchBox();
        searchBox.setOnSelectCallback(disp::setPatient);
        return searchBox;
    }

    private void doEnter(){
        int patientId = disp.getPatientId();
        if( patientId == 0 ){
            GuiUtil.alertError("患者が選択されていません。");
        } else {
            Context.getInstance().getFrontend().startVisit(patientId, LocalDateTime.now())
                    .thenAccept(visitId -> Platform.runLater(this::close))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
