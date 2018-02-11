package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.ConductFullDTO;

public class ConductEnteredEvent extends Event {

    public static EventType<ConductEnteredEvent> eventType = new EventType<>("CONDUCT_ENTERED");

    private ConductFullDTO conduct;

    public ConductEnteredEvent(ConductFullDTO conduct){
        super(eventType);
        this.conduct = conduct;
    }

    public ConductFullDTO getConduct() {
        return conduct;
    }
}
