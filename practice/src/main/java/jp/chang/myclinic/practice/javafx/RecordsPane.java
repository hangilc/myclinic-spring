package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.javafx.events.EventTypes;

public class RecordsPane extends VBox {

    public RecordsPane(){
        setFillWidth(true);
        addEventHandler(EventTypes.visitDeletedEventType, event -> {
            deleteRecord(event.getVisitId());
        });
    }

    public void addRecord(VisitFull2DTO visit){
        Record record = new Record(visit);
        getChildren().add(record);
    }

    private void deleteRecord(int visitId){
        Record record = null;
        for(Node node :getChildren()){
            if( node instanceof Record ){
                Record r = (Record)node;
                if( r.getVisitId() == visitId ){
                    record = r;
                    break;
                }
            }
        }
        if( record != null ){
            getChildren().remove(record);
        }
    }
}
