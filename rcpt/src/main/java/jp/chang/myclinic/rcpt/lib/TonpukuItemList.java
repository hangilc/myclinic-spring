package jp.chang.myclinic.rcpt.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TonpukuItemList<T> implements RcptShuukei<TonpukuItem<T>>{

    private List<TonpukuItem<T>> items = new ArrayList<>();

    @Override
    public void add(TonpukuItem<T> item){
        items.add(item);
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public Stream<TonpukuItem<T>> stream() {
        return items.stream();
    }
}
