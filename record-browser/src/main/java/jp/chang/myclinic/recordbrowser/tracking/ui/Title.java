package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.Patient;
import jp.chang.myclinic.recordbrowser.tracking.model.Visit;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

class Title extends TextFlow {

    Title(Visit visit){
        getStyleClass().add("record-title");
        setMaxWidth(Double.MAX_VALUE);
        Patient patient = visit.getPatient();
        String s = String.format("(%d) %s%s %s", patient.getPatientId(),
                patient.getLastName(),
                patient.getFirstName(),
                dateTimeString(visit.getVisitedAt()));
        getChildren().addAll(
                new Text(s)
        );
    }

    private String dateTimeString(LocalDateTime at){
        return DateTimeUtil.toKanji(at, DateTimeUtil.kanjiFormatter3) +
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
    }

}
