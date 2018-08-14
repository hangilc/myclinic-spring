package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.ShoukiDTO;

public class ShoukiChangedEvent extends Event {

    public static EventType<ShoukiChangedEvent> eventType = new EventType<>("ShoukiChangedEvent");

    private int visitId;
    private ShoukiDTO shoukiDTO;

    public ShoukiChangedEvent(int visitId, ShoukiDTO shoukiDTO) {
        super(eventType);
        this.visitId = visitId;
        this.shoukiDTO = shoukiDTO;
    }

    public int getVisitId() {
        return visitId;
    }

    public ShoukiDTO getShoukiDTO() {
        return shoukiDTO;
    }
}
