package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.DiseaseFullDTO;

public class DiseaseEnteredEvent extends Event {

    public static EventType<DiseaseEnteredEvent> eventType = new EventType<>("DISEASE_ENTERED_EVENT");

    private DiseaseFullDTO disease;

    public DiseaseEnteredEvent(DiseaseFullDTO disease) {
        super(eventType);
        this.disease = disease;
    }

    public DiseaseFullDTO getDisease() {
        return disease;
    }
}
