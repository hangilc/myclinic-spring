package jp.chang.myclinic.rcpt.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShinryouItemList {

    private List<ShinryouItem> items = new ArrayList<>();

    public ShinryouItemList() {

    }

    public void add(ShinryouItem item){
        int shinryoucode = item.getShinryoucode();
        for(ShinryouItem e: items){
            if( e.getShinryoucode() == shinryoucode ){
                e.setCount(e.getCount() + item.getCount());
                return;
            }
        }
        items.add(item);
    }

    public void merge(ShinryouItemList src){
        src.items.forEach(this::add);
    }

    public void forEach(Consumer<ShinryouItem> f){
        items.forEach(f);
    }

    public int getTen(){
        return items.stream().mapToInt(item -> item.getTensuu() * item.getCount()).sum();
    }

}
