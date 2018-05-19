package jp.chang.myclinic.rcpt.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NaifukuItemList<T> implements RcptShuukei<NaifukuItem<T>> {

    private List<NaifukuItem<T>> items = new ArrayList<>();

    public void extendOrAdd(String usage, int days, int iyakuhincode, double amountTimesYakka, T drug){
        for(NaifukuItem<T> e: items){
            if( e.canExtend(usage, days) ){
                e.extend(iyakuhincode, amountTimesYakka, drug);
                return;
            }
        }
        NaifukuItem<T> item = new NaifukuItem<>(usage, days, iyakuhincode, amountTimesYakka, drug);
        add(item);
    }

    @Override
    public void add(NaifukuItem<T> item){
        items.add(item);
    }

    @Override
    public boolean isEmpty(){
        return items.isEmpty();
    }

    @Override
    public Stream<NaifukuItem<T>> stream(){
        return items.stream();
    }

}
