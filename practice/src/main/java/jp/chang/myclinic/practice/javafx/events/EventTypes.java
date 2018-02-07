package jp.chang.myclinic.practice.javafx.events;

import javafx.event.EventType;

public class EventTypes {

    public static EventType<DrugEnteredEvent> drugEnteredEventType = new EventType<>("DRUG_ENTERED");
    public static EventType<VisitDeletedEvent> visitDeletedEventType = new EventType<>("VISIT_DELETED");
    public static EventType<DrugDaysModifiedEvent> drugDaysModifiedEventType = new EventType<>("DRUG_DAYS_MODIFIED");
    public static EventType<DrugDeletedEvent> drugDeletedEventType = new EventType<>("DRUG_DELETED");

}
