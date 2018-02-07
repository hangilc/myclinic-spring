package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;

public class VisitDeletedEvent extends Event {

    private int visitId;

    public VisitDeletedEvent(int visitId){
        super(EventTypes.visitDeletedEventType);
        this.visitId = visitId;
    }

    public int getVisitId() {
        return visitId;
    }
}
