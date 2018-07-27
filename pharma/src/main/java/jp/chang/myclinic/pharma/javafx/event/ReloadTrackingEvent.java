package jp.chang.myclinic.pharma.javafx.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ReloadTrackingEvent extends Event {

    public static EventType<ReloadTrackingEvent> eventType = new EventType<>("Pharma_ReloadTrackingEvent");

    public ReloadTrackingEvent() {
        super(eventType);
    }

}
