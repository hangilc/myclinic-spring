package jp.chang.myclinic.util.logic;

public interface BiLogic<T> {

    BiValue<T> getValues(String leftName, String rightName, ErrorMessages em);

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
