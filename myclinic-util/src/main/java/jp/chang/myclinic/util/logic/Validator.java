package jp.chang.myclinic.util.logic;

public interface Validator<T> {
    void validate(T value, String name, ErrorMessages em);

    default Converter<T, T> toConverter(){
        return (value, name, em) -> {
            validate(value, name, em);
            return value;
        };
    }
}
