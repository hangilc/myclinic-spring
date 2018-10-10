package jp.chang.myclinic.util.value;

public interface ObjectSource<T> extends Logic<T> {
    boolean isEmpty();
    void clear();
}
