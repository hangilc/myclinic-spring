package jp.chang.myclinic.util.logic;

public class BiValue<T> {

    public T left;
    public T right;

    public BiValue(T left, T right){
        this.left = left;
        this.right = right;
    }

}
