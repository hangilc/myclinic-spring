package jp.chang.myclinic.util.logic;

public interface Logic<T> {

    T getValue(ErrorMessages em);
    void setValue(T value, ErrorMessages em);

    default T fromStorageValue(String storageValue, ErrorMessages em){
        throw new RuntimeException("Not implemented");
    }

    default String toStorageValue(T value, ErrorMessages em){
        throw new RuntimeException("Not implemented");
    }

    default String getStorageValue(ErrorMessages em){
        int ne = em.getNumberOfErrors();
        T value = getValue(em);
        if( em.hasErrorSince(ne) ){
            return null;
        } else {
            return toStorageValue(value, em);
        }
    }

    default void setStorageValue(String storageValue, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        T value = fromStorageValue(storageValue, em);
        if( em.hasNoErrorSince(ne) ){
            setValue(value, em);
        }
    }

}
