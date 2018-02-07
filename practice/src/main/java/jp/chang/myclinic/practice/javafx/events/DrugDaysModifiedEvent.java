package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import jp.chang.myclinic.dto.DrugDTO;

public class DrugDaysModifiedEvent extends Event {

    private int drugId;
    private int visitId;
    private int days;

    public DrugDaysModifiedEvent(DrugDTO origDrug, int days){
        super(EventTypes.drugDaysModifiedEventType);
        this.drugId = origDrug.drugId;
        this.visitId = origDrug.visitId;
        this.days = days;
    }

    public int getDrugId() {
        return drugId;
    }

    public int getVisitId() {
        return visitId;
    }

    public int getDays() {
        return days;
    }
}
