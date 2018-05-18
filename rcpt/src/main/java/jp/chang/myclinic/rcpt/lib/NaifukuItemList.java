package jp.chang.myclinic.rcpt.lib;

import java.util.ArrayList;
import java.util.List;

public class NaifukuItemList<T> {

    private List<NaifukuItem<T>> items = new ArrayList<>();

    public void add(NaifukuItem<T> item){
        String usage = item.getUsage();
        int days = item.getDays();
        for(NaifukuItem<T> e: items){
            if( e.canExtend(usage, days) ){
                e.extend(item.getIyakuhincodes(), item.getKingaku(), item.getDrugs());
                return;
            }
        }
        items.add(item);
    }

    public void merge(NaifukuItemList<T> src){
        List<NaifukuItem<T>> toBeAdded = new ArrayList<>();
        for(NaifukuItem<T> srcItem: src.items){
            boolean merged = false;
            for(NaifukuItem<T> myItem: items){
                if( myItem.canMerge(srcItem) ){
                    myItem.merge(srcItem);
                    merged = true;
                    break;
                }
            }
            if( !merged ){
                toBeAdded.add(srcItem);
            }
        }
        items.addAll(toBeAdded);
    }

}
