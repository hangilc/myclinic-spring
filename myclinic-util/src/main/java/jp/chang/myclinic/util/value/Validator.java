package jp.chang.myclinic.util.value;

@FunctionalInterface
public interface Validator<T> {
    void validate(T value, String name, ErrorMessages em);
}
