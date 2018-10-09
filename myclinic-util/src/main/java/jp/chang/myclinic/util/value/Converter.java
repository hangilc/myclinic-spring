package jp.chang.myclinic.util.value;

public interface Converter<S, T> {

    T convert(S src, String name, ErrorMessages em);
}
