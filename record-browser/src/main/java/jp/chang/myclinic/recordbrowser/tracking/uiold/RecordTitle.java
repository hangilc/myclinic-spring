package jp.chang.myclinic.recordbrowser.tracking.uiold;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.recordbrowser.Main;
import jp.chang.myclinic.recordbrowser.PatientHistoryDialog;
import jp.chang.myclinic.recordbrowser.tracking.modelold.Patient;
import jp.chang.myclinic.recordbrowser.tracking.modelold.Visit;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.time.LocalDateTime;

public class RecordTitle extends TextFlow {

    RecordTitle(Visit visit){
        getStyleClass().add("record-title");
        setMaxWidth(Double.MAX_VALUE);
        Patient patient = visit.getPatient();
        Hyperlink patientLink = new Hyperlink();
        patientLink.textProperty().bind(
                Bindings.concat(patient.lastNameProperty(), patient.firstNameProperty())
        );
        patientLink.getStyleClass().add("patient-link");
        patientLink.setOnAction(evt -> {
            Service.api.getPatient(patient.getPatientId())
                    .thenAccept(patientDTO -> Platform.runLater(() -> {
                        PatientHistoryDialog dialog = new PatientHistoryDialog(patientDTO);
                        Main.setAsChildWindow(dialog);
                        dialog.setX(Main.getXofMainStage() + 40);
                        dialog.setY(Main.getYofMainStage() + 20);
                        dialog.show();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        });
        String s = dateTimeString(visit.getVisitedAt());
        visit.wqueueStateProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue.intValue() == WqueueWaitState.InExam.getCode() ){
                getStyleClass().add("current-visit");
            } else {
                getStyleClass().removeAll("current-visit");
            }
        });
        getChildren().addAll(
                new Label(String.format("(%d) ", patient.getPatientId())),
                patientLink,
                new Label(s)
        );
    }

    private String dateTimeString(LocalDateTime at){
        return DateTimeUtil.toKanji(at, DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4, "");
    }

}
