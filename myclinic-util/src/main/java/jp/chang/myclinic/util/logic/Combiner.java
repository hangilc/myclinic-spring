package jp.chang.myclinic.util.logic;

public interface Combiner<T, U> {
    U combine(T left, T right, String leftName, String rightName, ErrorMessages em);
}
