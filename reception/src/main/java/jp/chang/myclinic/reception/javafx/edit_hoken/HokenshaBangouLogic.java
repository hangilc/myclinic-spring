package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;

class HokenshaBangouLogic implements Logic<Integer> {

    //private static Logger logger = LoggerFactory.getLogger(HokenshaBangouLogic.class);
    private StringProperty hokenshaBangou = new SimpleStringProperty();

    void clear(){
        hokenshaBangou.setValue("");
    }

    @Override
    public Integer getValue(ErrorMessages em) {
        return null;
    }

    @Override
    public void setValue(Integer value, ErrorMessages em) {
        if( value == null ){
            clear();
        } else {
            hokenshaBangou.setValue("" + value);
        }
    }
}
