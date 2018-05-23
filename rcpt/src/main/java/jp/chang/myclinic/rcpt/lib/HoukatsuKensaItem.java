package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.consts.HoukatsuKensaKind;

import java.time.LocalDate;
import java.util.*;

public class HoukatsuKensaItem<T> extends RcptItemBase
        implements Mergeable<HoukatsuKensaItem<T>>, Extendable<HoukatsuKensaItem<T>> {

    private static HoukatsuKensa houkatsuDb = HoukatsuKensa.load();

    private HoukatsuKensaKind kind;
    private LocalDate at;
    private Set<Integer> shinryoucodes = new HashSet<>();
    private List<T> dataList = new ArrayList<>();

    public HoukatsuKensaItem(HoukatsuKensaKind kind, LocalDate at,
                             int shinryoucode, int tanka, T data) {
        super(tanka, 1);
        this.kind = kind;
        this.at = at;
        this.shinryoucodes.add(shinryoucode);
        this.dataList.add(data);
    }

    @Override
    public boolean canExtend(HoukatsuKensaItem<T> arg){
        return kind == arg.kind;
    }

    @Override
    public void extend(HoukatsuKensaItem<T> arg){
        this.shinryoucodes.addAll(arg.shinryoucodes);
        incTanka(arg.getTanka());
        this.dataList.addAll(arg.dataList);
    }

    @Override
    public boolean canMerge(HoukatsuKensaItem<T> src) {
        return kind == src.kind && shinryoucodes.equals(src.shinryoucodes);
    }

    @Override
    public void merge(HoukatsuKensaItem<T> src) {
        incCount(src.getCount());
    }

    @Override
    public int getTanka() {
        return houkatsuDb.calcTen(kind, shinryoucodes.size(), at).orElse(super.getTanka());
    }

    public List<T> getDataList() {
        return dataList;
    }

}
