package jp.chang.myclinic.pharma.javafx.event;

import javafx.event.Event;
import javafx.event.EventType;

public class PrescCancelEvent extends Event {

    public static EventType<PrescCancelEvent> eventType = new EventType<>("Pharma_PrescCancelEvent");

    public PrescCancelEvent() {
        super(eventType);
    }

}
