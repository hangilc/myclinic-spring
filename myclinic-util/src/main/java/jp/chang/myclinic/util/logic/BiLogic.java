package jp.chang.myclinic.util.logic;

import java.util.function.BiConsumer;

public interface BiLogic<T> {
    void apply(BiConsumer<T, T> successHandler, Runnable errorHandler,
               String leftName, String rightName, ErrorMessages em);

    default <U> BiLogic<U> convert(Converter<T, U> converter){
        BiLogic<T> self = this;
        return (successHandler, errorHandler, leftName, rightName, em) -> {
            self.apply(
                    (a, b) -> {
                        int ne = em.getNumberOfErrors();
                        U p = converter.convert(a, leftName, em);
                        U q = converter.convert(b, rightName, em);
                        if( em.hasNoErrorSince(ne) ){
                            successHandler.accept(p, q);
                        } else {
                            errorHandler.run();
                        }
                    },
                    errorHandler,
                    leftName,
                    rightName,
                    em);
        };
    }

    default BiLogic<T> validate(Validator<T> validator){
        return convert(validator.toConverter());
    }

    default BiLogic<T> validate(Validator<T> leftValidator, Validator<T> rightValidator){
        BiLogic<T> self = this;
        return (successHandler, errorHandler, leftName, rightName, em) -> {
            self.apply(
                    (a, b) -> {
                        int ne = em.getNumberOfErrors();
                        leftValidator.validate(a, leftName, em);
                        rightValidator.validate(b, rightName, em);
                        if( em.hasNoErrorSince(ne) ){
                            successHandler.accept(a, b);
                        } else {
                            errorHandler.run();
                        }
                    },
                    errorHandler,
                    leftName,
                    rightName,
                    em
            );
        };
    }

    default BiLogic<T> validate(BiValidator<T> validator){
        BiLogic<T> self = this;
        return (successHandler, errorHandler, leftName, rightName, em) -> {
            self.apply(
                    (a, b) -> {
                        int ne = em.getNumberOfErrors();
                        validator.validate(a, b, leftName, rightName, em);
                        if( em.hasNoErrorSince(ne) ){
                            successHandler.accept(a, b);
                        } else {
                            errorHandler.run();
                        }
                    },
                    errorHandler,
                    leftName,
                    rightName,
                    em
            );
        };
    }

}
