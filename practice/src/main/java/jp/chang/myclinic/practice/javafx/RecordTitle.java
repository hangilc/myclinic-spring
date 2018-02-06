package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.util.DateTimeUtil;

public class RecordTitle extends TextFlow {

    private int visitId;

    public RecordTitle(VisitDTO visit) {
        this.visitId = visit.visitId;
        getStyleClass().add("record-title-text");
        getChildren().addAll(new Text(createText(visit.visitedAt));
        adaptToEnv();
        PracticeEnv.INSTANCE.currentVisitIdProperty().addListener((obs, oldValue, newValue) -> adaptToEnv());
        PracticeEnv.INSTANCE.tempVisitIdProperty().addListener((obs, oldValue, newValue) -> adaptToEnv());
    }

    private void adaptToEnv(){
        getStyleClass().removeAll("current-visit", "temp-visit");
        if (PracticeEnv.INSTANCE.getCurrentVisitId() == visitId) {
            getStyleClass().add("current-visit");
        } else if (PracticeEnv.INSTANCE.getTempVisitId() == visitId) {
            getStyleClass().add("temp-visit");
        }
    }

    private String createText(String at) {
        return DateTimeUtil.sqlDateTimeToKanji(at,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
    }
}
