package jp.chang.myclinic.util.logic;

import java.util.function.Consumer;

public class LogicValue<T> implements Logic<T>{

    private T value;

    public LogicValue(T value) {
        this.value = value;
    }

    @Override
    public void apply(Consumer<T> successHandler, Runnable errorCallback, String name, ErrorMessages em) {
        successHandler.accept(value);
    }

}
