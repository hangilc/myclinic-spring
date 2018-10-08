package jp.chang.myclinic.util.value;

public interface ValueConverter<S, T> {
    T convert(S source, ErrorMessages em);
}
