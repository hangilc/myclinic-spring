package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.consts.ConductKind;

import java.util.Objects;

public class ConductItem<D, K> implements RcptItem, Mergeable<ConductItem<D, K>> {

    private ConductKind kind;
    private String gazouLabel;
    private ShinryouItemList shinryouList = new ShinryouItemList();
    private ConductDrugItemList<D> drugs = new ConductDrugItemList<>();
    private KizaiItemList<K> kizaiList = new KizaiItemList<>();
    private int count;

    public ConductItem(ConductKind kind) {
        this.kind = kind;
        this.count = 1;
    }

    public void setGazouLabel(String gazouLabel){
        this.gazouLabel = gazouLabel;
    }

    public void add(ShinryouItem item){
        shinryouList.add(item);
    }

    public void add(ConductDrugItem<D> item){
        drugs.add(item);
    }

    public void add(KizaiItem<K> item){
        kizaiList.add(item);
    }

    @Override
    public int getTanka() {
        return shinryouList.getTen() + drugs.getTen() + kizaiList.getTen();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean canMerge(ConductItem<D, K> src) {
        return kind == src.kind && shinryouList.eqv(src.shinryouList) &&
                drugs.eqv(src.drugs) && kizaiList.eqv(src.kizaiList) &&
                Objects.equals(gazouLabel, src.gazouLabel);
    }

    @Override
    public void merge(ConductItem<D, K> src) {
        count += src.count;
    }

}
