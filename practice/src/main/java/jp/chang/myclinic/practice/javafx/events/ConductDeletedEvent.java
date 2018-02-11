package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.ConductDTO;

public class ConductDeletedEvent extends Event {

    public static EventType<ConductDeletedEvent> eventType = new EventType<>("CONDUCT_DELETED");

    private int conductId;
    private int visitId;

    public ConductDeletedEvent(ConductDTO conduct) {
        super(eventType);
        this.conductId = conduct.conductId;
        this.visitId = conduct.visitId;
    }

    public int getConductId() {
        return conductId;
    }

    public int getVisitId() {
        return visitId;
    }
}
