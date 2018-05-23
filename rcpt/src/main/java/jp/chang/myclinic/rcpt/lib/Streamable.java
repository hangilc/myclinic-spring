package jp.chang.myclinic.rcpt.lib;

import java.util.stream.Stream;

public interface Streamable<T> {

    Stream<T> stream();

}
