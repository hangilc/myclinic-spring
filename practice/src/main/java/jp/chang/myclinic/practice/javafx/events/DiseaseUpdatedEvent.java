package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.DiseaseFullDTO;

public class DiseaseUpdatedEvent extends Event {

    public static EventType<DiseaseUpdatedEvent> eventType = new EventType<>("DISEASE_UPDATED_EVENT");
    private DiseaseFullDTO disease;

    public DiseaseUpdatedEvent(DiseaseFullDTO disease) {
        super(eventType);
        this.disease = disease;
    }

    public DiseaseFullDTO getDisease() {
        return disease;
    }
}
