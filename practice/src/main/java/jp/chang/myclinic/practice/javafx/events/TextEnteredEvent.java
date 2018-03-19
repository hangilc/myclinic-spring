package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import javafx.event.EventType;
import jp.chang.myclinic.dto.TextDTO;

public class TextEnteredEvent extends Event {

    public static EventType<TextEnteredEvent> eventType = new EventType<>("text-entered-event");

    private TextDTO enteredText;

    public TextEnteredEvent(TextDTO enteredText) {
        super(eventType);
        this.enteredText = enteredText;
    }

    public TextDTO getEnteredText() {
        return enteredText;
    }

}
