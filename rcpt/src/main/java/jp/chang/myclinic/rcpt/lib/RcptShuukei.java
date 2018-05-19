package jp.chang.myclinic.rcpt.lib;

import java.util.stream.Stream;

public interface RcptShuukei<T extends RcptItem> {

    boolean isEmpty();
    Stream<T> stream();
    default int getTen(){
        return stream().mapToInt(a -> a.getTanka() * a.getCount()).sum();
    }
    default int getTotalCount(){
        return stream().mapToInt(T::getCount).sum();
    }

}
