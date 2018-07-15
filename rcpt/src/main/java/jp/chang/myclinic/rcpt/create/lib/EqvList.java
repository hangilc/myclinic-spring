package jp.chang.myclinic.rcpt.create.lib;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

interface EqvList<T extends Eqv> extends Streamable<T> {

    default boolean eqv(EqvList<T> src){
        return Objects.equals(toEqvCodes(), src.toEqvCodes());
    }

    default List<String> toEqvCodes(){
        return stream().map(T::eqvCode).sorted().collect(Collectors.toList());
    }

}
