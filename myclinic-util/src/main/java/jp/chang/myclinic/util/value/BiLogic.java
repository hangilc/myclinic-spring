package jp.chang.myclinic.util.value;

public class BiLogic<T> {

    private Logic<T> left;
    private Logic<T> right;

    public BiLogic(Logic<T> left, Logic<T> right) {
        this.left = left;
        this.right = right;
    }

    public Logic<T> getLeft(){
        return left;
    }

    public Logic<T> getRight(){
        return right;
    }

    public <U> BiLogic<U> convert(Converter<T, U> converter){
        return new BiLogic<>(left.convert(converter), right.convert(converter));
    }

    public BiLogic<T> validate(Validator<T> leftValidator, Validator<T> rightValidator){
        Logic<T> newLeft = leftValidator == null ? left : left.validate(leftValidator);
        Logic<T> newRight = rightValidator == null ? right : right.validate(rightValidator);
        return new BiLogic<>(newLeft, newRight);
    }

    public BiLogic<T> validate(Validator<T> validator){
        return validate(validator, validator);
    }

}
