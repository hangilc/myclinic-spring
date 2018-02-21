package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.ShinryouFullDTO;

public class ShinryouEnteredEvent extends Event {
    public static EventType<ShinryouEnteredEvent> eventType = new EventType<ShinryouEnteredEvent>("SHINRYOU_ENTERED_EVVENT");

    private ShinryouFullDTO shinryou;

    public ShinryouEnteredEvent(ShinryouFullDTO shinryou){
        super(eventType);
        this.shinryou = shinryou;
    }

    public ShinryouFullDTO getShinryou() {
        return shinryou;
    }
}
