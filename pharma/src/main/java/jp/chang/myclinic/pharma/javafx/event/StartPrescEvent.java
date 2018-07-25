package jp.chang.myclinic.pharma.javafx.event;

import javafx.event.Event;
import javafx.event.EventType;

public class StartPrescEvent extends Event {

    public static EventType<StartPrescEvent> eventType = new EventType<>("Pharma_StartPrescEvent");

    private int visitId;

    public StartPrescEvent(int visitId) {
        super(eventType);
        this.visitId = visitId;
    }

    public int getVisitId() {
        return visitId;
    }
}
