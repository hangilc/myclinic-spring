package jp.chang.myclinic.util.logic;

import java.util.function.Function;

public class BiValue<T> {

    public T left;
    public T right;

    public BiValue(T left, T right){
        this.left = left;
        this.right = right;
    }

    public <U> BiValue<U> map(Function<T, U> fun){
        return new BiValue<>(fun.apply(left), fun.apply(right));
    }

}
