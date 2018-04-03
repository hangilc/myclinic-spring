package jp.chang.myclinic.medicalcheck.dateinput;

import java.util.function.Consumer;

public class Result<T,E> {

    private T value;
    private E error;

    public static <T,E> Result<T,E>  createError(E err){
        return new Result<>(null, err);
    }

    public static <T,E> Result<T,E> createValue(T value){
        return new Result<>(value, null);
    }

    private Result(T value, E error){
        this.value = value;
        this.error = error;
    }

    public boolean hasValue(){
        return error == null;
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

    public void accept(Consumer<T> valueConsumer, Consumer<E> errorConsumer){
        if( hasValue() ){
            valueConsumer.accept(getValue());
        } else {
            errorConsumer.accept(getError());
        }
    }
}
