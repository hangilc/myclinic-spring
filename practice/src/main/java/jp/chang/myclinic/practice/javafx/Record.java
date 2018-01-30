package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;

public class Record extends VBox {

    public Record(VisitFull2DTO visit){
        getChildren().addAll(createTitle(visit.visit));
    }

    private Node createTitle(VisitDTO visit){
        RecordTitle recordTitle = new RecordTitle(visit);
        return recordTitle;
    }
}
