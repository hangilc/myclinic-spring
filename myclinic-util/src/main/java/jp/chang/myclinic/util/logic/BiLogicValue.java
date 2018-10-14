package jp.chang.myclinic.util.logic;

public class BiLogicValue<T> implements BiLogic<T> {

    private T left;
    private T right;

    public BiLogicValue(T left, T right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public BiValue<T> getValues(String leftName, String rightName, ErrorMessages em) {
        return new BiValue<>(left, right);
    }
}
