package jp.chang.myclinic.rcpt.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class RcptShuukeiImpl<T extends RcptItem & Mergeable<T>> implements RcptShuukei<T> {

    private List<T> items = new ArrayList<>();

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void add(T item) {
        items.add(item);
    }

    @Override
    public Stream<T> stream() {
        return items.stream();
    }

    List<T> getItems(){
        return items;
    }
}
