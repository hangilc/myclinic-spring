package jp.chang.myclinic.rcpt.create.lib;

import java.util.stream.Stream;

public interface Streamable<T> {

    Stream<T> stream();

}
