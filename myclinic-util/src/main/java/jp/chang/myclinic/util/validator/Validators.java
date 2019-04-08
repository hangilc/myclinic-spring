package jp.chang.myclinic.util.validator;

import java.util.function.Function;
import static jp.chang.myclinic.util.validator.Validated.*;

public class Validators {

    public static <T> Validated<T> startValue(T value){
        return success(value);
    }

    public static <T> Function<T, Validated<T>> isNotNull(){
        return value -> {
            if( value != null ){
                return success(value);
            } else {
                return fail("Null value");
            }
        }
    }

}
