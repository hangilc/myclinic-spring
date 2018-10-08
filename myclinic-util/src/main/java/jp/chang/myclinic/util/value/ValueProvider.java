package jp.chang.myclinic.util.value;

public interface ValueProvider<T> {
    T get(ErrorMessages em);
    default <U> ValueProvider<U> chain(ValueConverter<T, U> converter){
        return em -> {
            if( em.hasError() ){
                return null;
            }
            int ne = em.getNumberOfErrors();
            T value = get(em);
            if( em.hasErrorSince(ne) ){
                return null;
            }
            return converter.convert(value, em);
        };
    }
}
