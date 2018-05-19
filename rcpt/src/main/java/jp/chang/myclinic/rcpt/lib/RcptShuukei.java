package jp.chang.myclinic.rcpt.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface RcptShuukei<T extends RcptItem & Mergeable<T>> {

    boolean isEmpty();
    void add(T item);
    Stream<T> stream();
    default int getTen(){
        return stream().mapToInt(a -> a.getTanka() * a.getCount()).sum();
    }
    default int getTotalCount(){
        return stream().mapToInt(T::getCount).sum();
    }
    default void merge(RcptShuukei<T> src){
        List<T> toBeAdded = new ArrayList<>();
        src.stream().forEach(srcItem -> {
            Optional<T> mergeable =  stream().filter(item -> item.canMerge(srcItem)).findFirst();
            mergeable.ifPresentOrElse(
                    m -> m.merge(srcItem),
                    () -> toBeAdded.add(srcItem)
            );
        });
        toBeAdded.forEach(this::add);
    }
}
