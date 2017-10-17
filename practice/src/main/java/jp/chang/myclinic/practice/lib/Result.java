package jp.chang.myclinic.practice.lib;

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
}
