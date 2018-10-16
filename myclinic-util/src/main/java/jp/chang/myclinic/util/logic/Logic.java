package jp.chang.myclinic.util.logic;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Logic<T> {

    T getValue(String name, ErrorMessages em);

    default T getValueOrElseGet(Supplier<T> supplier, String name, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        T t = getValue(name, em);
        if( em.hasErrorSince(ne) ){
            return supplier.get();
        }
        return t;
    }

    default T getValueOrElse(T elseValue, String name, ErrorMessages em) {
        return getValueOrElseGet(() -> elseValue, name, em);
    }

    default <P> T getPartValue(P part, String name, List<CompositeError<P>> errors){
        ErrorMessages em = new ErrorMessages();
        T value = getValue(name, em);
        if( em.hasError() ){
            em.getMessages().forEach(message -> errors.add(new CompositeError<P>(part, message)));
        }
        return value;
    }

    default void verify(String name, ErrorMessages em){
        getValue(name, em);
    }

    default <U> Logic<U> convert(Converter<T, U> converter) {
        Logic<T> self = this;
        return (name, em) -> {
            int ne = em.getNumberOfErrors();
            T t = self.getValue(name, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            U u = converter.convert(t, name, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            return u;
        };
    }

    default <U> Logic<U> convert(Chainer<T, U> chainer){
        return chainer.chain(this);
    }

    default Logic<T> validate(Validator<T> validator){
        return convert(validator.toConverter());
    }

    default Logic<T> validate(Chainer<T, T> chainer){
        return chainer.chain(this);
    }

    default <U> Logic<U> map(Function<T, U> fun){
        return convert((value, name, em) -> fun.apply(value));
    }

}
