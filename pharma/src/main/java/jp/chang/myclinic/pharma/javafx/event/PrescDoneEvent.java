package jp.chang.myclinic.pharma.javafx.event;

import javafx.event.Event;
import javafx.event.EventType;

public class PrescDoneEvent extends Event {

    public static EventType<PrescDoneEvent> eventType = new EventType<>("Pharma_PrescDoneEvent");

    public PrescDoneEvent() {
        super(eventType);
    }

}
