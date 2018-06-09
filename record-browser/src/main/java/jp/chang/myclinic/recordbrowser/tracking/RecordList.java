package jp.chang.myclinic.recordbrowser.tracking;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.recordbrowser.tracking.model.Text;
import jp.chang.myclinic.recordbrowser.tracking.model.Visit;
import jp.chang.myclinic.recordbrowser.tracking.ui.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RecordList extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(RecordList.class);

    private List<Record> records = new ArrayList<>();

    RecordList() {

    }

    public void addVisit(Visit visit){
        Record record = new Record(visit);
        getChildren().add(record);
        records.add(record);
    }

    private Optional<Record> getRecord(int visitId){
        return records.stream().filter(rec -> rec.getVisitId() == visitId).findAny();
    }

    public void addText(Text text){
        getRecord(text.getVisitId()).ifPresent(record -> {
            record.addText(text);
        });
    }

}
