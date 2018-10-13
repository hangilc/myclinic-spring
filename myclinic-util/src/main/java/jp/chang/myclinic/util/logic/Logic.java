package jp.chang.myclinic.util.logic;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Logic<T> {

    void apply(Consumer<T> successHandler, Runnable errorCallback, String name, ErrorMessages em);

    default T getValueOrElse(T elseValue, String name, ErrorMessages em) {
        class Local {
            private T value = null;
        }
        Local local = new Local();
        apply(
                value -> local.value = value,
                () -> local.value = elseValue,
                name,
                em);
        return local.value;
    }

    default T getValue(String name, ErrorMessages em) {
        return getValueOrElse(null, name, em);
    }

    default <U> Logic<U> convert(Converter<T, U> converter) {
        Logic<T> self = this;
        return (successHandler, errorHandler, name, em) -> {
            self.apply(
                    t -> {
                        int ne = em.getNumberOfErrors();
                        U u = converter.convert(t, name, em);
                        if (em.hasErrorSince(ne)) {
                            errorHandler.run();
                        } else {
                            successHandler.accept(u);
                        }
                    },
                    errorHandler,
                    name,
                    em);
        };
    }

    default Logic<T> validate(Validator<T> validator){
        return convert(validator.toConverter());
    }

    default <U> Logic<U> map(Function<T, U> f) {
        Logic<T> self = this;
        return (successHandler, errorHandler, name, em) -> {
            self.apply(
                    t -> successHandler.accept(f.apply(t)),
                    errorHandler,
                    name,
                    em);
        };
    }
}
