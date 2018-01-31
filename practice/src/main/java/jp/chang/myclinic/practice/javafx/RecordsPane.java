package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitFull2DTO;

public class RecordsPane extends VBox {

    public RecordsPane(){
        setFillWidth(true);
        getStylesheets().add("css/Record.css");
    }

    public void addRecord(VisitFull2DTO visit){
        Record record = new Record(visit);
        getChildren().add(record);
    }
}
