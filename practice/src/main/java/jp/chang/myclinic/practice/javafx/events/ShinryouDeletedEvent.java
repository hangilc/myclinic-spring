package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.ShinryouDTO;

public class ShinryouDeletedEvent extends Event {

    public static EventType<ShinryouDeletedEvent> eventType = new EventType<>("SHINRYOU_DELETED");

    private int visitId;
    private int shinryouId;

    public ShinryouDeletedEvent(int visitId, int shinryouId){
        super(eventType);
        this.visitId = visitId;
        this.shinryouId = shinryouId;
    }

    public ShinryouDeletedEvent(ShinryouDTO shinryou) {
        this(shinryou.visitId, shinryou.shinryouId);
    }

    public int getShinryouId() {
        return shinryouId;
    }

    public int getVisitId() {
        return visitId;
    }
}
