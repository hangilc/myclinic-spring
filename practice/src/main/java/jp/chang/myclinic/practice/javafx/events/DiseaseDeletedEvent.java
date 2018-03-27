package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;

public class DiseaseDeletedEvent extends Event {

    public static EventType<DiseaseDeletedEvent> eventType = new EventType<>("DiseaseDeletedEvent");
    private int diseaseId;

    public DiseaseDeletedEvent(int diseaseId) {
        super(eventType);
        this.diseaseId = diseaseId;
    }

    public int getDiseaseId() {
        return diseaseId;
    }
}
