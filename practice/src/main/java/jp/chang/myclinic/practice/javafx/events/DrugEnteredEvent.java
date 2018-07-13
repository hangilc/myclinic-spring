package jp.chang.myclinic.practice.javafx.events;

import javafx.event.Event;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugFullDTO;

public class DrugEnteredEvent extends Event {

    private DrugFullDTO enteredDrug;
    private DrugAttrDTO attr;

    public DrugEnteredEvent(DrugFullDTO enteredDrug, DrugAttrDTO attr){
        super(EventTypes.drugEnteredEventType);
        this.enteredDrug = enteredDrug;
        this.attr = attr;
    }

    public DrugFullDTO getEnteredDrug() {
        return enteredDrug;
    }

    public DrugAttrDTO getAttr() {
        return attr;
    }
}
