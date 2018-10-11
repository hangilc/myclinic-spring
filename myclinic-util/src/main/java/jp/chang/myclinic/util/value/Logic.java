package jp.chang.myclinic.util.value;

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
        Logic<T> self = this;
        return (name, em) -> {
            int ne = em.getNumberOfErrors();
            T t = self.getValue(name, em);
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
