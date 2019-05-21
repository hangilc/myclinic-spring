package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.parts.PatientDisp;
import jp.chang.myclinic.practice.javafx.parts.PatientSearchBox;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class NewVisitDialog extends Stage {

    private PatientDisp disp;
    private PatientSearchBox searchBox;

    NewVisitDialog() {
        setTitle("患者受付  ");
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

    public void simulateSearchTextInsert(String text){
        searchBox.simulateSearchTextInsert(text);
    }

    public void simulateSearchTextFocus() {
        searchBox.simulateSearchTextFocus();
    }

    public void simulateSearchButtonClick() {
        searchBox.simulateSearchButtonClick();
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
        this.searchBox = new PatientSearchBox();
        searchBox.setOnSelectCallback(disp::setPatient);
        return searchBox;
    }

    private void doEnter(){
        int patientId = disp.getPatientId();
        if( patientId == 0 ){
            GuiUtil.alertError("患者が選択されていません。");
        } else {
            Context.frontend.startVisit(patientId, LocalDateTime.now())
                    .thenAccept(visitId -> Platform.runLater(this::close))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

    public List<PatientDTO> getSearchResults() {
        return searchBox.getSearchResults();
    }
}
