package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.Patient;
import jp.chang.myclinic.recordbrowser.tracking.model.Visit;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

class RecordTitle extends TextFlow {

    RecordTitle(Visit visit){
        getStyleClass().add("record-title");
        setMaxWidth(Double.MAX_VALUE);
        Patient patient = visit.getPatient();
        Hyperlink patientLink = new Hyperlink();
        patientLink.textProperty().bind(
                Bindings.concat(patient.lastNameProperty(), patient.firstNameProperty())
        );
        String s = dateTimeString(visit.getVisitedAt());
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
