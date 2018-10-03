package jp.chang.myclinic.util.logic;

public interface Logic<T> {

    T getValue(ErrorMessages em);
    boolean setValue(T value, ErrorMessages em);

    default String getStorageValue(Converter<String, T> converter, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        T value = getValue(em);
        if( em.hasErrorSince(ne) ){
            return null;
        } else {
            return converter.convert(value, em);
        }
    }

    default boolean setStorageValue(String storageValue, Converter<T, String> converter, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        T value = converter.convert(storageValue, em);
        if( em.hasErrorSince(ne) ){
            return false;
        } else {
            return setValue(value, em);
        }
    }


//    T getValue(Consumer<String> errorHandler);
//    String setValue(T value);
//
//    default boolean verify(Consumer<T> valueHandler, Consumer<String> errorHandler){
//        class Local {
//            private boolean hasError = false;
//            private String errorMessage;
//        }
//        Local local = new Local();
//        T value = getValue(e -> {
//            local.hasError = true;
//            local.errorMessage = e;
//        });
//        if( !local.hasError ){
//            valueHandler.accept(value);
//            return true;
//        } else {
//            errorHandler.accept(local.errorMessage);
//            return false;
//        }
//    }
//
//    default boolean verify(Consumer<T> valueHandler, List<String> errorAccum){
//        return verify(valueHandler, errorAccum::add);
//    }
//
//    default String setValueFromStorage(String storageValue, Converter<T, String> converter){
//        class Local {
//            private boolean hasError = false;
//            private String errorMessage;
//        }
//        Local local = new Local();
//        T value = converter.convert(storageValue, e -> {
//            local.hasError = true;
//            local.errorMessage = e;
//        });
//        if( local.hasError ){
//            return local.errorMessage;
//        } else {
//            return setValue(value);
//        }
//    }
//
//    default String getStorageValue(Converter<String,T> converter, Consumer<String> errorHandler){
//        class Local {
//            private boolean hasError = false;
//        }
//        Local local = new Local();
//        T value = getValue(e -> {
//            if( errorHandler != null ){
//                errorHandler.accept(e);
//                local.hasError = true;
//            }
//        });
//        if( local.hasError ){
//            return null;
//        } else {
//            return converter.convert(value, errorHandler);
//        }
//    }

}
