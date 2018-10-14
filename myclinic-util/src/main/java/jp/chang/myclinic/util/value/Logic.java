package jp.chang.myclinic.util.value;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface Logic<T> {
    T getValue(String name, ErrorMessages em);

    default int getValueAsInt(String name, ErrorMessages em){
        T t = getValue(name, em);
        if( t == null ){
            return 0;
        } else {
            return (int)t;
        }
    }

    default void apply(Consumer<T> handler, String name, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        T t = getValue(name, em);
        if( em.hasNoErrorSince(ne) ){
            handler.accept(t);
        }
    }

    default void applyInt(IntConsumer handler, String name, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        int i = getValueAsInt(name, em);
        if( em.hasNoErrorSince(ne) ){
            handler.accept(i);
        }
    }

    default Logic<T> peek(Consumer<T> handler, String name, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        T t = getValue(name, em);
        if( em.hasNoErrorSince(ne) ){
            handler.accept(t);
        }
        return this;
    }

    default <U> Logic<U> convert(Converter<T, U> conv){
        Logic<T> self = this;
        return (name, em) -> {
            int ne = em.getNumberOfErrors();
            T t = self.getValue(name, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            return conv.convert(t, name, em);
        };
    }

    default Logic<T> validate(Validator<T> validator){
        return (name, em) -> {
            int ne = em.getNumberOfErrors();
            T t = getValue(name, em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            validator.validate(t, name, em);
            if( em.hasErrorSince(ne) ){
                return null;
            } else {
                return t;
            }
        };
    }

}
