package jp.chang.myclinic.hotline;

import javafx.event.Event;
import javafx.event.EventType;

public class ResizeRequiredEvent extends Event {

    public static EventType<ResizeRequiredEvent> eventType = new EventType<>("ResizeRequiredEvent");

    public ResizeRequiredEvent() {
        super(eventType);
    }

}
