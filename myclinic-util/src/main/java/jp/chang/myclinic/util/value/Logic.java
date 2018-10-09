package jp.chang.myclinic.util.value;

public interface Logic<T> {
    T getValue(String name, ErrorMessages em);
    default <U> Logic<U> chain(Converter<T, U> conv){
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
}
