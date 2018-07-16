package jp.chang.myclinic.reception.event;

import javafx.event.Event;
import javafx.event.EventType;

public class RefreshEvent extends Event {

    public static EventType<RefreshEvent> eventType = new EventType<>("Reception_RefreshEvent");

    public RefreshEvent() {
        super(eventType);
    }

}
