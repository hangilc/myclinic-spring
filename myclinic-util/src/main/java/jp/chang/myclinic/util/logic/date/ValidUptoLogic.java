package jp.chang.myclinic.util.logic.date;

import jp.chang.myclinic.util.logic.ErrorMessages;

import java.time.LocalDate;

public class ValidUptoLogic extends DateLogic {

    //private static Logger logger = LoggerFactory.getLogger(ValidUptoLogic.class);

    @Override
    public LocalDate getValue(ErrorMessages em) {
        if( isEmpty() ){
            return LocalDate.MAX;
        } else {
            return super.getValue(em);
        }
    }

    @Override
    public void setValue(LocalDate value, ErrorMessages em) {
        if( value == LocalDate.MAX ){
            clear();
        } else {
            super.setValue(value, em);
        }
    }

    @Override
    public LocalDate fromStorageValue(String storageValue, ErrorMessages em) {
        if( storageValue == null || storageValue.equals("0000-00-00") ){
            return LocalDate.MAX;
        } else {
            return super.fromStorageValue(storageValue, em);
        }
    }

    @Override
    public String toStorageValue(LocalDate value, ErrorMessages em) {
        if( value == LocalDate.MAX ){
            return "0000-00-00";
        } else {
            return super.toStorageValue(value, em);
        }
    }

}
