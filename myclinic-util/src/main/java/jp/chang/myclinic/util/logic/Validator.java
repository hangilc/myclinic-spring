package jp.chang.myclinic.util.logic;

import java.util.function.Supplier;

class Validator {

    //private static Logger logger = LoggerFactory.getLogger(Validator.class);
    private String name;

    Validator(String name) {
        this.name = name;
    }

    public boolean validate(Supplier<Boolean> pred, ErrorMessages em, String format, Object... args){
        if( pred.get() ){
            return true;
        } else {
            em.add(String.format(format, args));
            return false;
        }
    }

    public boolean validatePositiveInteger(int value, ErrorMessages em){
        return validate(() -> value > 0, em, "%sの値が正でありません。", name);
    }

}
