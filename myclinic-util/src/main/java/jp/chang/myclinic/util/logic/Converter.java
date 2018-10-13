package jp.chang.myclinic.util.logic;

public interface Converter<S, T> {

    T convert(S src, String name, ErrorMessages em);
}
