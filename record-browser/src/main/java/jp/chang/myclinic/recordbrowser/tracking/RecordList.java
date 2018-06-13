package jp.chang.myclinic.recordbrowser.tracking;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.recordbrowser.tracking.model.*;
import jp.chang.myclinic.recordbrowser.tracking.ui.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RecordList extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(RecordList.class);

    private List<Record> records = new ArrayList<>();

    RecordList() {

    }

    public void addVisit(Visit visit, Runnable cb){
        Record record = new Record(visit);
        getChildren().add(record);
        records.add(record);
        cb.run();
    }

    private Optional<Record> getRecord(int visitId){
        return records.stream().filter(rec -> rec.getVisitId() == visitId).findAny();
    }

    public void addDrug(Drug drug){
        getRecord(drug.getVisitId()).ifPresent(record -> {
            record.addDrug(drug);
        });
    }

    public void addShinryou(Shinryou shinryou){
        getRecord(shinryou.getVisitId()).ifPresent(record -> {
            record.addShinryou(shinryou);
        });
    }

    public void addConduct(Conduct conduct){
        getRecord(conduct.getVisitId()).ifPresent(record -> {
            record.addConduct(conduct);
        });
    }

}
