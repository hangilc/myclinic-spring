package jp.chang.myclinic.util.logic;

import java.util.function.BiConsumer;

public interface BiLogic<T> {

    BiValue<T> getValues(String leftName, String rightName, ErrorMessages em);

    default void apply(BiConsumer<T, T> successHandler, String leftName, String rightName, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        BiValue<T> values = getValues(leftName, rightName, em);
        if( em.hasNoErrorSince(ne) ){
            successHandler.accept(values.left, values.right);
        }
    }

    default void verify(String leftName, String rightName, ErrorMessages em){
        apply((a, b) -> {}, leftName, rightName, em);
    }

    static <S> BiLogic<S> combine(Logic<S> left, Logic<S> right){
        return (leftName, rightName, em) -> {
            int ne = em.getNumberOfErrors();
            S leftValue = left.getValue(leftName, em);
            S rightValue = right.getValue(rightName, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            return new BiValue<>(leftValue, rightValue);
        };
    }

    default <U> BiLogic<U> convert(Converter<T, U> converter){
        BiLogic<T> self = this;
        return (leftName, rightName, em) -> {
            int ne = em.getNumberOfErrors();
            BiValue<T> values = self.getValues(leftName, rightName, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            U p = converter.convert(values.left, leftName, em);
            U q = converter.convert(values.right, rightName, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            return new BiValue<>(p, q);
        };
    }

    default BiLogic<T> validate(Validator<T> validator){
        return convert(validator.toConverter());
    }

    default BiLogic<T> validate(Validator<T> leftValidator, Validator<T> rightValidator){
        BiLogic<T> self = this;
        return (leftName, rightName, em) -> {
            int ne = em.getNumberOfErrors();
            BiValue<T> values = self.getValues(leftName, rightName, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            leftValidator.validate(values.left, leftName, em);
            rightValidator.validate(values.right, rightName, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            return values;
        };
    }

    default BiLogic<T> validate(BiValidator<T> validator){
        BiLogic<T> self = this;
        return (leftName, rightName, em) -> {
            int ne = em.getNumberOfErrors();
            BiValue<T> values = self.getValues(leftName, rightName, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            validator.validate(values.left, values.right, leftName, rightName, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            return values;
        };
    }

}
