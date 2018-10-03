package jp.chang.myclinic.util.logic;

public interface Converter<T,V> {
    T convert(V src, ErrorMessages em);
}
