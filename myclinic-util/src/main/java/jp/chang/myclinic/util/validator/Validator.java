package jp.chang.myclinic.util.validator;

public interface Validator<T, U> {
    Validated<U> validate(T value);

    default <S> Validator<T, S> chain(Validator<U, S> validator){
        return value -> {
            Validated<U> v = validate(value);
            if( v.isSuccess() ){
                return validator.validate(v.getValue());
            } else {
                return Validated.fail(v.getErrorMessage());
            }
        };
    }
}
