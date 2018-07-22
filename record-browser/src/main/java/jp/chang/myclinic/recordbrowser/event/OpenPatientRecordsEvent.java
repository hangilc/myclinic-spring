package jp.chang.myclinic.recordbrowser.event;

import javafx.event.Event;
import javafx.event.EventType;

public class OpenPatientRecordsEvent extends Event {

    public static EventType<OpenPatientRecordsEvent> eventType = new EventType<>("OpenPatientRecords");

    private int patientId;

    public OpenPatientRecordsEvent(int patientId) {
        super(eventType);
        this.patientId = patientId;
    }

    public int getPatientId() {
        return patientId;
    }
}
