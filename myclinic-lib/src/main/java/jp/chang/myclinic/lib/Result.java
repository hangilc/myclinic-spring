package jp.chang.myclinic.lib;

import java.util.function.Consumer;

public class Result<T,E> {
    private T value;
    private E error;

    public Result(T value, E error){
        this.value = value;
        this.error = error;
    }

    public Result(T value){
        this(value, null);
    }

    public boolean hasError(){
        return error != null;
    }

    public T getValue(){
        return value;
    }

    public E getError(){
        return error;
    }

    public Result<T,E> ifPresent(Consumer<T> consumer){
        if( !hasError() ){
            consumer.accept(value);
        }
        return this;
    }

    public Result<T,E> ifError(Consumer<E> consumer){
        if( hasError() ){
            consumer.accept(error);
        }
        return this;
    }

}
