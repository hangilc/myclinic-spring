package jp.chang.myclinic.util.logic;

import java.util.function.BiConsumer;

public class BiLogicValue<T> implements BiLogic<T> {

    private T left;
    private T right;

    public BiLogicValue(T left, T right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void apply(BiConsumer<T, T> successHandler, Runnable errorHandler, String leftName, String rightName, ErrorMessages em) {
        successHandler.accept(left, right);
    }
}
