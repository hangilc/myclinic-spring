package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.recordbrowser.event.OpenPatientRecordsEvent;
import jp.chang.myclinic.recordbrowser.tracking.model.PatientModel;
import jp.chang.myclinic.recordbrowser.tracking.model.RecordModel;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDateTime;

public class RecordTitle extends TextFlow {

    private boolean current = false;

    RecordTitle(RecordModel recordModel){
        getStyleClass().add("record-title");
        setMaxWidth(Double.MAX_VALUE);
        PatientModel patient = recordModel.getPatient();
        Hyperlink patientLink = new Hyperlink();
        patientLink.textProperty().bind(
                Bindings.concat(patient.lastNameProperty(), patient.firstNameProperty())
        );
        patientLink.getStyleClass().add("patient-link");
        patientLink.setOnAction(evt -> {
            RecordTitle.this.fireEvent(new OpenPatientRecordsEvent(recordModel.getPatient().getPatientId()));
        });
        String s = dateTimeString(recordModel.getVisitedAt());
        recordModel.waitStateProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue == WqueueWaitState.InExam ){
                getStyleClass().add("current-visit");
                this.current = true;
            } else {
                getStyleClass().removeAll("current-visit");
                this.current = false;
            }
        });
        getChildren().addAll(
                new Label(String.format("(%d) ", patient.getPatientId())),
                patientLink,
                new Label(s)
        );
    }

    public boolean isCurrent(){
        return current;
    }

    private String dateTimeString(LocalDateTime at){
        return DateTimeUtil.toKanji(at, DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4, "");
    }

}
