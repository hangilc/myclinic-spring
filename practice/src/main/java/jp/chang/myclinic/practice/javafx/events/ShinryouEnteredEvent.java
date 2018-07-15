package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;

public class ShinryouEnteredEvent extends Event {
    public static EventType<ShinryouEnteredEvent> eventType = new EventType<ShinryouEnteredEvent>("SHINRYOU_ENTERED_EVVENT");

    private ShinryouFullDTO shinryou;
    private ShinryouAttrDTO attr;

    public ShinryouEnteredEvent(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        super(eventType);
        this.shinryou = shinryou;
        this.attr = attr;
    }

    public ShinryouFullDTO getShinryou() {
        return shinryou;
    }

    public ShinryouAttrDTO getAttr() {
        return attr;
    }
}
