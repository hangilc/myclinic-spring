package jp.chang.myclinic.util.logic;

import java.util.function.Consumer;

public interface Converter<T,V> {
    T convert(V src, Consumer<String> errorHandler);
}
