package jp.chang.myclinic.rcpt.create.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RcptShuukei<T extends RcptItem & Mergeable<T>> implements Streamable<T>, Addable<T> {

    private List<T> items = new ArrayList<>();

    public boolean isEmpty(){
        return items.isEmpty();
    }

    @Override
    public void add(T item){
        items.add(item);
    }

    @Override
    public Stream<T> stream(){
        return items.stream();
    }

    public int getTen(){
        return stream().mapToInt(a -> a.getTanka() * a.getCount()).sum();
    }

    public int getTotalCount(){
        return stream().mapToInt(T::getCount).sum();
    }

    public void merge(RcptShuukei<T> src){
        List<T> toBeAdded = new ArrayList<>();
        src.items.forEach(srcItem -> {
            boolean merged = stream().anyMatch(item -> {
                if( item.canMerge(srcItem) ){
                    item.merge(srcItem);
                    return true;
                } else {
                    return false;
                }
            });
            if( !merged ){
                toBeAdded.add(srcItem);
            }
        });
        toBeAdded.forEach(this::add);
    }

}
