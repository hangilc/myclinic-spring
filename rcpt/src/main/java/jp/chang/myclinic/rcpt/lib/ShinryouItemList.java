package jp.chang.myclinic.rcpt.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ShinryouItemList implements RcptShuukei<ShinryouItem> {

    private List<ShinryouItem> items = new ArrayList<>();

    @Override
    public void add(ShinryouItem item){
        items.add(item);
    }

    public void forEach(Consumer<ShinryouItem> f){
        items.forEach(f);
    }

    @Override
    public Stream<ShinryouItem> stream(){
        return items.stream();
    }

    @Override
    public boolean isEmpty(){
        return items.isEmpty();
    }

    public int getTen(){
        return items.stream().mapToInt(item -> item.getTensuu() * item.getCount()).sum();
    }

}
