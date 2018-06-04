package jp.chang.myclinic.recordbrowser;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordListByPatient extends VBox {

    private static Logger logger = LoggerFactory.getLogger(RecordListByPatient.class);

    RecordListByPatient() {
        super(4);
    }

    void add(VisitFull2DTO data){
        getChildren().add(new VBox(
                createTitle(data),
                new RecordBody(data)
        ));
    }

    void clear(){
        getChildren().clear();
    }

    private Node createTitle(VisitFull2DTO visit) {
        String s = dateTimeString(visit.visit.visitedAt);
        Label label = new Label(s);
        label.getStyleClass().add("record-title");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private String dateTimeString(String at){
        return DateTimeUtil.sqlDateTimeToKanji(at,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
    }

}
