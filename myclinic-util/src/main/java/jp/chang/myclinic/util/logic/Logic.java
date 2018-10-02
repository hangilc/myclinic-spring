package jp.chang.myclinic.util.logic;

import java.util.List;
import java.util.function.Consumer;

public interface Logic<T> {

    T getValue(Consumer<String> errorHandler);
    String setValue(T value);

    default boolean verify(Consumer<T> valueHandler, Consumer<String> errorHandler){
        class Local {
            private boolean hasError = false;
            private String errorMessage;
        }
        Local local = new Local();
        T value = getValue(e -> {
            local.hasError = true;
            local.errorMessage = e;
        });
        if( !local.hasError ){
            valueHandler.accept(value);
            return true;
        } else {
            errorHandler.accept(local.errorMessage);
            return false;
        }
    }

    default boolean verify(Consumer<T> valueHandler, List<String> errorAccum){
        return verify(valueHandler, errorAccum::add);
    }

    default String setValueFromStorage(String storageValue, Converter<T, String> converter){
        class Local {
            private boolean hasError = false;
            private String errorMessage;
        }
        Local local = new Local();
        T value = converter.convert(storageValue, e -> {
            local.hasError = true;
            local.errorMessage = e;
        });
        if( local.hasError ){
            return local.errorMessage;
        } else {
            return setValue(value);
        }
    }

    default String getStorageValue(Converter<String,T> converter, Consumer<String> errorHandler){
        class Local {
            private boolean hasError = false;
        }
        Local local = new Local();
        T value = getValue(e -> {
            if( errorHandler != null ){
                errorHandler.accept(e);
                local.hasError = true;
            }
        });
        if( local.hasError ){
            return null;
        } else {
            return converter.convert(value, errorHandler);
        }
    }

}
