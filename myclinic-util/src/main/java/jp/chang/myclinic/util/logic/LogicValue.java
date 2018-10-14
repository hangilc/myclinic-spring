package jp.chang.myclinic.util.logic;

public class LogicValue<T> implements Logic<T>{

    private T value;

    public LogicValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue(String name, ErrorMessages em){
        return value;
    }
}
