package jp.chang.myclinic.util.value;

public class ImmediateLogic<T> implements Logic<T>{

    private T value;

    public ImmediateLogic(T value) {
        this.value = value;
    }

    @Override
    public T getValue(String name, ErrorMessages em) {
        return value;
    }
}
