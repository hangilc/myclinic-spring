package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import jp.chang.myclinic.dto.DrugFullDTO;

public class DrugEnteredEvent extends Event {

    private DrugFullDTO enteredDrug;

    public DrugEnteredEvent(DrugFullDTO enteredDrug){
        super(EventTypes.drugEnteredEventType);
        this.enteredDrug = enteredDrug;
    }

    public DrugFullDTO getEnteredDrug() {
        return enteredDrug;
    }

}
