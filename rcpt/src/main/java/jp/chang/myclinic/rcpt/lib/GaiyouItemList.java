package jp.chang.myclinic.rcpt.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GaiyouItemList<T> implements RcptShuukei<GaiyouItem<T>> {

    private List<GaiyouItem<T>> items = new ArrayList<>();

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void add(GaiyouItem<T> item) {
        items.add(item);
    }

    @Override
    public Stream<GaiyouItem<T>> stream() {
        return items.stream();
    }
}
