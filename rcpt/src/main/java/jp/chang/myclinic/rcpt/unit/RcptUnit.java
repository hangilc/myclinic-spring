package jp.chang.myclinic.rcpt.unit;

import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RcptUnit {

    private static Logger logger = LoggerFactory.getLogger(RcptUnit.class);
    private Map<Integer, SimpleShinryouItem> simpleShinryouMap = new HashMap<>();
    private List<HoukatsuKensaItem> houkatsuKensaItems = new ArrayList<>();

    private static <T extends Extendable<T>> void extendList(List<T> list, T item){
        for(T curr: list){
            if( curr.isExtendableWith(item) ){
                curr.extendWith(item);
                return;
            }
        }
        list.add(item);
    }

    private static <T extends Mergeable<T>> void mergeListOne(List<T> list, T item){
        for(T dst: list){
            if( dst.isMergeableWith(item) ){
                dst.incCount(item.getCount());
                return;
            }
        }
        list.add(item);
    }

    private static <T extends Mergeable<T> & Countable> void mergeList(List<T> dst, List<T> src){
        src.forEach(s -> mergeListOne(dst, s));
    }

    private static <K, T extends Countable> void mergeMapOne(Map<K, T> map, K key, T item){
        T curr = map.get(key);
        if( curr == null ){
            map.put(key, item);
        } else {
            curr.incCount(item.getCount());
        }
    }

    private static <K, T extends Countable> void mergeMap(Map<K, T> dst, Map<K, T> src){
        src.forEach((k, v) -> mergeMapOne(dst, k, v));
    }

    RcptUnit() {

    }

    RcptUnit(VisitFull2DTO visit){
        visit.shinryouList.forEach(this::addShinryou);
    }

    void merge(RcptUnit arg){
        mergeMap(simpleShinryouMap, arg.simpleShinryouMap);
        mergeList(houkatsuKensaItems, arg.houkatsuKensaItems);
    }

    private void addShinryou(ShinryouFullDTO shinryou){
        HoukatsuKensaKind kind = HoukatsuKensaKind.fromCode(shinryou.master.houkatsukensa);
        if( kind == HoukatsuKensaKind.NONE ){
            mergeMapOne(simpleShinryouMap, shinryou.master.shinryoucode, new SimpleShinryouItem(shinryou.master));
        } else {
            extendList(houkatsuKensaItems, new HoukatsuKensaItem(shinryou.master));
        }
    }

    @Override
    public String toString() {
        return "RcptUnit{" +
                "simpleShinryouMap=" + simpleShinryouMap +
                ", houkatsuKensaItems=" + houkatsuKensaItems +
                '}';
    }

}
