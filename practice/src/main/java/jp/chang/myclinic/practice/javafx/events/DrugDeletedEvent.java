package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import jp.chang.myclinic.dto.DrugDTO;

public class DrugDeletedEvent extends Event {

    private int drugId;
    private int visitId;

    public DrugDeletedEvent(DrugDTO drug){
        super(EventTypes.drugDeletedEventType);
        this.drugId = drug.drugId;
        this.visitId = drug.visitId;
    }

    public int getDrugId() {
        return drugId;
    }

    public int getVisitId() {
        return visitId;
    }
}
