package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.DiseaseFullDTO;

import java.util.List;

public class CurrentDiseasesChangedEvent extends Event {

    public static EventType<CurrentDiseasesChangedEvent> eventType = new EventType<>("CURRENT_DISEASES_CHANGED_EVENT");

    private List<DiseaseFullDTO> diseases;

    public CurrentDiseasesChangedEvent(List<DiseaseFullDTO> diseases) {
        super(eventType);
        this.diseases = diseases;
    }

    public List<DiseaseFullDTO> getDiseases() {
        return diseases;
    }
}
