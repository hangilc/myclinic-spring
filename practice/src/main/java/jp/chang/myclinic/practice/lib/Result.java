package jp.chang.myclinic.practice.lib;

import java.util.function.Consumer;

public class Result<T,E> {

    private T value;
    private E error;

    public Result(T value){
        this(value, null);
    }

    public Result(T value, E error){
        this.value = value;
        this.error = error;
    }

    public boolean hasValue(){
        return error != null;
    }

    public T getValue(){
        return value;
    }

    public E getError(){
        return error;
    }

    public Result<T,E> ifPresent(Consumer<T> consumer){
        if( hasValue() ){
            consumer.accept(getValue());
        }
        return this;
    }

    public Result<T,E> ifError(Consumer<E> consumer){
        if( !hasValue() ){
            consumer.accept(getError());
        }
        return this;
    }
}
