package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.ShinryouDTO;

public class ShinryouDeletedEvent extends Event {

    public static EventType<ShinryouDeletedEvent> eventType = new EventType<>("SHINRYOU_DELETED");

    private int shinryouId;
    private int visitId;

    public ShinryouDeletedEvent(ShinryouDTO shinryou) {
        super(eventType);
        this.shinryouId = shinryou.shinryouId;
        this.visitId = shinryou.visitId;
    }

    public int getShinryouId() {
        return shinryouId;
    }

    public int getVisitId() {
        return visitId;
    }
}
