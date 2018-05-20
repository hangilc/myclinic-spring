package jp.chang.myclinic.rcpt.lib;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface EqvList<T extends Eqv> {

    default boolean eqvStream(Stream<T> a, Stream<T> b){
        return Objects.equals(toEqvCodes(a), toEqvCodes(b));
    }

    default List<String> toEqvCodes(Stream<T> stream){
        return stream.map(T::eqvCode).sorted().collect(Collectors.toList());
    }

}
