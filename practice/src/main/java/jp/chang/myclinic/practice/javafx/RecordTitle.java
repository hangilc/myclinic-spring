package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.util.DateTimeUtil;

public class RecordTitle extends VBox {

    public RecordTitle(VisitDTO visit){
        getChildren().addAll(createTitle(visit));
    }

    private Node createTitle(VisitDTO visit){
        TextFlow textFlow = new TextFlow();
        String text = DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
        Text title = new Text(text);
        textFlow.getChildren().add(title);
        textFlow.getStyleClass().add("record-title-text");
        if( PracticeEnv.INSTANCE.getCurrentVisitId() == visit.visitId ){
            textFlow.getStyleClass().add("current-visit");
        } else if( PracticeEnv.INSTANCE.getTempVisitId() == visit.visitId ){
            textFlow.getStyleClass().add("temp-visit");
        }
        return textFlow;
    }
}
