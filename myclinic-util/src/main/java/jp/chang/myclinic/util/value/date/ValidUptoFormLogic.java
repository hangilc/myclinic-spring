package jp.chang.myclinic.util.value.date;

import jp.chang.myclinic.util.value.ErrorMessages;

import java.time.LocalDate;
import java.util.Objects;

public class ValidUptoFormLogic extends DateFormLogic {

    //private static Logger logger = LoggerFactory.getLogger(ValidUptoFormLogic.class);

    @Override
    public LocalDate getValue(String name, ErrorMessages em) {
        if( isEmpty() ){
            return LocalDate.MAX;
        } else {
            return super.getValue(name, em);
        }
    }

    public String convertToStorageValue(LocalDate value) {
        if( value == null ){
            return null;
        } else if( Objects.equals(LocalDate.MAX, value) ){
            return "0000-00-00";
        } else {
            return value.toString();
        }
    }

    @Override
    public void setValue(LocalDate value) {
        if( Objects.equals(LocalDate.MAX, value) ){
            super.clear();
        } else {
            super.setValue(value);
        }
    }

    @Override
    public void setValueFromStorage(String store) {
        if( store == null || Objects.equals("0000-00-00", store) ){
            super.clear();
        } else {
            super.setValueFromStorage(store);
        }
    }

}
