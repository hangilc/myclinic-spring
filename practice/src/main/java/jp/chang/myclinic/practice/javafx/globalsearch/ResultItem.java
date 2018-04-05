package jp.chang.myclinic.practice.javafx.globalsearch;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.TextVisitPatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ResultItem extends VBox {

    private static Logger logger = LoggerFactory.getLogger(ResultItem.class);

    ResultItem(TextVisitPatientDTO data) {
        super(4);
        setFillWidth(true);
        getChildren().addAll(
                createPatientRow(data.patient),
                createVisitedAtRow(data.visit),
                createContent(data.text)
        );
    }

    private Node createPatientRow(PatientDTO patient){
        String text = String.format("(%d) %s %s", patient.patientId, patient.lastName,
                patient.firstName);
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add("result-item-patient");
        return label;
    }

    private Node createVisitedAtRow(VisitDTO visit){
        String text = DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add("result-item-visited-at");
        return label;
    }

    private Node createContent(TextDTO text){
        TextFlow textFlow = new TextFlow(new Text(text.content));
        textFlow.getStyleClass().add("result-item-content");
        return textFlow;
    }

}
