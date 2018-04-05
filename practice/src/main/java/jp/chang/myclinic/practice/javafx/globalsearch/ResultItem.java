package jp.chang.myclinic.practice.javafx.globalsearch;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextVisitPatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ResultItem extends VBox {

    private static Logger logger = LoggerFactory.getLogger(ResultItem.class);

    ResultItem(TextVisitPatientDTO data) {
        super(4);
        getChildren().addAll(
                createPatientRow(data.patient),
                createVisitedAtRow(data.visit)
        );
    }

    private Node createPatientRow(PatientDTO patient){
        String text = String.format("(%d) %s %s", patient.patientId, patient.lastName,
                patient.firstName);
        return new Label(text);
    }

    private Node createVisitedAtRow(VisitDTO visit){
        String text = DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
        return new Label(text);

    }

}
